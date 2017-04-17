package com.wust.newsmartschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.PreferenceManager;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.Uplistview_Adapter;
import com.wust.newsmartschool.domain.CompanyEntity;
import com.wust.newsmartschool.views.ListViewForScrollView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.MediaType;

public class CompanyActivity extends BaseActivity {
    String TAG = "CompanyActivity_Debugs";
    private TextView company_title;
    private ListViewForScrollView colleagelistview;
    private Uplistview_Adapter colleagelistview_adapter;
    //点击标题的标题
    CompanyEntity companyentity;
    // 搜索的框框的控件
    private EditText query;
    private ImageButton clearSearch;
    List<CompanyEntity.DataBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        data = new ArrayList<>();
        // 搜索框
        query = (EditText) findViewById(R.id.query);
        query.addTextChangedListener(new watcher());
        // 搜索框中清除button
        clearSearch = (ImageButton) findViewById(R.id.search_clear);
        company_title = (TextView) findViewById(R.id.company_title);
        company_title.setText("武汉科技大学");
        colleagelistview = (ListViewForScrollView) findViewById(R.id.up_listview);
        colleagelistview_adapter = new Uplistview_Adapter(this, data);
        colleagelistview.setAdapter(colleagelistview_adapter);
        colleagelistview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击事件
                startActivity(new Intent(CompanyActivity.this, ProfessionalActivity.class).putExtra("professional", (Serializable) data.get(position).getSubList()).putExtra("title", data.get(position).getName()));
            }
        });
        clearSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
            }
        });
        try {
            getSchoollist();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void getSchoollist() throws JSONException {
        JSONObject school = new JSONObject();
        school.put("type", PreferenceManager.getInstance()
                .getCurrentUserstudentType());
        school.put("year", PreferenceManager.getInstance()
                .getCurrentUserYear());
        Log.i(TAG, school.toString());
        OkHttpUtils.postString().url(Constant.GETSCHOOL_URL)
                .content(school.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        Log.i(TAG, arg0);
                        try {
                            JSONObject json = new JSONObject(arg0);
                            if (json.getInt("code") == 1) {
                                companyentity = new Gson().fromJson(arg0,
                                        CompanyEntity.class);
                                data.addAll(companyentity.getData());
                                colleagelistview_adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                    }
                });
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
            // TODO Auto-generated method stub
            Pattern p = Pattern.compile(s.toString());
            if (s.length() > 0) {
                clearSearch.setVisibility(View.VISIBLE);
            } else {
                clearSearch.setVisibility(View.INVISIBLE);
            }

        }

    }


    public void back(View view) {
        finish();
        if (android.os.Build.VERSION.SDK_INT > 5) {
            overridePendingTransition(
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }

}
