package com.wust.newsmartschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.Gradlistview_Adapter;
import com.wust.newsmartschool.domain.CompanyEntity;
import com.wust.newsmartschool.views.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class GradeActivity extends BaseActivity {
    String TAG = "CompanyActivity_Debugs";
    private TextView company_title;
    private ListViewForScrollView colleagelistview;
    private Gradlistview_Adapter colleagelistview_adapter;
    // 搜索的框框的控件
    private EditText query;
    private ImageButton clearSearch;
    List<CompanyEntity.DataBean.SubListBean.SubListBean2> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional);
        data = new ArrayList<>();
        data = (List<CompanyEntity.DataBean.SubListBean.SubListBean2>) getIntent().getSerializableExtra("grade");
        // 搜索框
        query = (EditText) findViewById(R.id.query);
        query.addTextChangedListener(new watcher());
        // 搜索框中清除button
        clearSearch = (ImageButton) findViewById(R.id.search_clear);
        company_title = (TextView) findViewById(R.id.company_title);
        if (getIntent().getStringExtra("title") != null) {
            company_title.setText(getIntent().getStringExtra("title"));
        } else {
            company_title.setText("武汉科技大学");
        }
        colleagelistview = (ListViewForScrollView) findViewById(R.id.up_listview);
        colleagelistview_adapter = new Gradlistview_Adapter(this, data);
        colleagelistview.setAdapter(colleagelistview_adapter);
        colleagelistview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击事件
                startActivity(new Intent(GradeActivity.this, DeptMembersActivity.class).putExtra("classid", data.get(position).getId()).putExtra("title", data.get(position).getName()));

            }
        });
        clearSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
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
