package com.wust.newsmartschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.TypeMembers_Adapter;
import com.wust.newsmartschool.domain.Common_TypeMem;
import com.wust.newsmartschool.domain.Common_TypeMem_Data;
import com.wust.newsmartschool.domain.Company_Dep;
import com.wust.newsmartschool.views.ECProgressDialog;
import com.wust.newsmartschool.views.ListViewForScrollView;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.CommonUtils;
import com.wust.easeui.utils.PreferenceManager;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.MediaType;

public class DeptMembersActivity extends BaseActivity {
    String TAG = "DeptMembersActivity_Log";
    private LinearLayout ll_nopeople;
    Company_Dep company_Dep;
    private TextView brand_title;
    private ListViewForScrollView deptmem_listview;
    private TypeMembers_Adapter typememb_adapter;
    List<Common_TypeMem_Data> data;
    private ImageView no_memberimage;
    private ECProgressDialog pd;
    private EditText query;
    private ImageButton clearSearch;
    int id;
    String type;
    //送上一个页面传过来的type
    String type_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dept_members);
        deptmem_listview = (ListViewForScrollView) findViewById(R.id.deptmem_listview);
        brand_title = (TextView) findViewById(R.id.brand_title);
        data = new ArrayList<>();
        clearSearch = (ImageButton) findViewById(R.id.search_clear);
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
            }
        });
        query = (EditText) findViewById(R.id.query);
        query.addTextChangedListener(new watcher());
        pd = new ECProgressDialog(DeptMembersActivity.this);
        pd.show();
        ll_nopeople = (LinearLayout) findViewById(R.id.ll_nopeople);
        no_memberimage = (ImageView) findViewById(R.id.no_memberimage);
        //判断传过来的type来决定访问那一个接口和显示的数据
        type_intent = getIntent().getStringExtra("type");
        typememb_adapter = new TypeMembers_Adapter(DeptMembersActivity.this, data);
        deptmem_listview.setAdapter(typememb_adapter);
        switchType();
        deptmem_listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String userId;
                userId = String
                        .valueOf(((Common_TypeMem_Data) typememb_adapter.getItem(arg2)).getUserId());
                Log.e(TAG, userId);
                startActivity(new Intent(getApplicationContext(),
                        DeptMemInfoActivity.class).putExtra("userId",
                        userId));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    overridePendingTransition(R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);
                }
            }
        });
    }

    public void switchType() {
        switch (type_intent) {
            case "departmentId"://部门
                Log.e(TAG, "departmentId");
                company_Dep = (Company_Dep) getIntent().getSerializableExtra("company_Dep");
                type = "departmentId";
                id = company_Dep.getDeptId();
                brand_title.setText(company_Dep.getDepName());
                Log.e(TAG, id + "//departmentId");
                // 获取成员列表
                try {
                    getTypeMembers();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "partyBranchId"://党支部
                Log.e(TAG, "partyBranchId");
                brand_title.setText(getIntent().getStringExtra("title") + "成员列表");
                id = getIntent().getIntExtra("type_gcd", -1);
                type = "partyBranchId";
                // 获取成员列表
                try {
                    getTypeMembers();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "jobTitleId"://按职务
                Log.e(TAG, "jobTitleId");
                brand_title.setText(getIntent().getStringExtra("title") + "成员列表");
                id = getIntent().getIntExtra("type_gcd", -1);
                type = "jobTitleId";
                // 获取成员列表
                try {
                    getTypeMembers();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "positionalTitleId"://按职称
                Log.e(TAG, "positionalTitleId");
                brand_title.setText(getIntent().getStringExtra("title") + "成员列表");
                id = getIntent().getIntExtra("type_gcd", -1);
                type = "positionalTitleId";
                // 获取成员列表
                try {
                    getTypeMembers();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void getTypeMembers() throws JSONException {
        JSONObject loginJson = new JSONObject();
        loginJson.put("typeId", type);
        loginJson.put("id", id);
        Log.i(TAG, loginJson.toString());
        CommonUtils.setCommonJson(DeptMembersActivity.this, loginJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        OkHttpUtils.postString().url(Constant.GETMEMBYTYPE_URL)
                .content(loginJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        Log.e(TAG, arg0);
                        Common_TypeMem common_typeMem = new Gson().fromJson(arg0,
                                Common_TypeMem.class);
                        if (common_typeMem.getData().size() != 0) {
                            ll_nopeople.setVisibility(View.VISIBLE);
                            data.addAll(common_typeMem.getData());
                            typememb_adapter.notifyDataSetChanged();
                        } else {
                            no_memberimage.setVisibility(View.VISIBLE);
                            Toast.makeText(DeptMembersActivity.this,
                                    "暂无成员",
                                    Toast.LENGTH_SHORT).show();
                        }
                        pd.dismiss();
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

    class watcher implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            {
                Pattern p = Pattern.compile(s.toString());
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                }
                List<Common_TypeMem_Data> temp_dept = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    Common_TypeMem_Data pp = data.get(i);
                    Matcher matcher = p.matcher(pp.getUserRealname().toString());
                    if (matcher.find()) {
                        temp_dept.add(pp);
                    }
                }
                typememb_adapter = new TypeMembers_Adapter(DeptMembersActivity.this, temp_dept);
                deptmem_listview.setAdapter(typememb_adapter);
            }


        }

    }
}
