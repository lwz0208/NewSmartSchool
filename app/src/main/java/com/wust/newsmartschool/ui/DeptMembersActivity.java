package com.wust.newsmartschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wust.easeui.Constant;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.TypeMembers_Adapter;
import com.wust.newsmartschool.domain.Common_TypeMem;
import com.wust.newsmartschool.domain.Company_Dep;
import com.wust.newsmartschool.views.ECProgressDialog;
import com.wust.newsmartschool.views.ListViewForScrollView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

public class DeptMembersActivity extends BaseActivity {
    String TAG = "DeptMembersActivity_Log";
    private LinearLayout ll_nopeople;
    private TextView brand_title;
    private ListViewForScrollView deptmem_listview;
    private TypeMembers_Adapter typememb_adapter;
    List<Common_TypeMem.DataBean> data;
    private ImageView no_memberimage;
    private ECProgressDialog pd;
    private EditText query;
    private ImageButton clearSearch;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dept_members);
        deptmem_listview = (ListViewForScrollView) findViewById(R.id.deptmem_listview);
        brand_title = (TextView) findViewById(R.id.brand_title);
        if (getIntent().getStringExtra("title") != null) {
            brand_title.setText(getIntent().getStringExtra("title"));
        } else {
            brand_title.setText("武汉科技大学");
        }
        data = new ArrayList<>();
        clearSearch = (ImageButton) findViewById(R.id.search_clear);
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
            }
        });
        query = (EditText) findViewById(R.id.query);
        pd = new ECProgressDialog(DeptMembersActivity.this);
        pd.show();
        ll_nopeople = (LinearLayout) findViewById(R.id.ll_nopeople);
        no_memberimage = (ImageView) findViewById(R.id.no_memberimage);
        typememb_adapter = new TypeMembers_Adapter(DeptMembersActivity.this, data);
        deptmem_listview.setAdapter(typememb_adapter);
        try {
            getTypeMembers();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        deptmem_listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //点击事件
                startActivity(new Intent(DeptMembersActivity.this, DeptMemInfoActivity.class).putExtra("userId", data.get(arg2).getId()));
            }
        });
    }


    private void getTypeMembers() throws JSONException {
        JSONObject loginJson = new JSONObject();
        loginJson.put("id", getIntent().getIntExtra("classid", -1));
        Log.i(TAG, loginJson.toString());
        OkHttpUtils.postString().url(Constant.GETMYCLASSTYPE_URL)
                .content(loginJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        Log.e(TAG, arg0);
                        pd.dismiss();
                        try {
                            JSONObject json = new JSONObject(arg0);
                            if (!json.getString("msg").equals("没有数据")) {
                                Common_TypeMem common_typeMem = new Gson().fromJson(arg0,
                                        Common_TypeMem.class);
                                ll_nopeople.setVisibility(View.VISIBLE);
                                data.addAll(common_typeMem.getData());
                                typememb_adapter.notifyDataSetChanged();
                            } else {
                                no_memberimage.setVisibility(View.VISIBLE);
                                Toast.makeText(DeptMembersActivity.this,
                                        "去火星了...",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        pd.dismiss();
                        showToastShort("获取人员列表失败");
                        Log.e(TAG, arg0.toString() + "/*/" + arg1.toString());
                    }
                });

    }


    public void back(View view) {
        finish();
        if (android.os.Build.VERSION.SDK_INT > 5) {
            overridePendingTransition(
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }


}
