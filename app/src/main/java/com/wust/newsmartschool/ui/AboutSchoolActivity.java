package com.wust.newsmartschool.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.ServiceListAdapter;
import com.wust.newsmartschool.domain.ServiceItem;

import java.util.ArrayList;

public class AboutSchoolActivity extends AppCompatActivity {
    private Context context;
    private ArrayList<ServiceItem> arraylist;
    private ListView listView;
    private ServiceListAdapter listAdapter;
    private String[] nameList = {"学校简介", "现任领导", "校史沿革", "院系设置", "管理机构", "美丽校园"};
    private String[] decripList = {
            "武汉科技大学是一所",
            "孔建益---党委书记",
            "1898年 湖北工艺学",
            "无机非金属材料工程",
            "学校办公室（政策法",
            "美丽校园"
    };
    private int img1 = R.drawable.campus_introduction;
    private int img2 = R.drawable.campus_leaders;
    private int img3 = R.drawable.campus_history;
    private int img4 = R.drawable.campus_setting;
    private int img5 = R.drawable.campus_manage;
    private int img6 = R.drawable.campus_beautiful_school;
    private int[] imgList = {img1, img2, img3, img4, img5, img6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_school);
        context = getApplicationContext();


        listView = (ListView) findViewById(R.id.aboutschoollist);
        getListData();
        listAdapter = new ServiceListAdapter(context, arraylist);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent();
                // TODO Auto-generated method stub
                switch (position) {
                    case 0:
                        intent.setClass(AboutSchoolActivity.this, SchoolActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(AboutSchoolActivity.this, SchoolLeaderActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(AboutSchoolActivity.this, SchoolHistoryActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(AboutSchoolActivity.this, SchoolAcademyActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent.setClass(AboutSchoolActivity.this, SchoolManageActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent.setClass(AboutSchoolActivity.this, BeautifulSchoolActivity.class);
                        startActivity(intent);
                        break;
                }

            }

        });

    }


    public void getListData() {
        arraylist = new ArrayList<ServiceItem>();

        for (int i = 0; i < nameList.length; i++) {
            ServiceItem item = new ServiceItem();
            item.setServiceName(nameList[i]);
            item.setDescription(decripList[i] + "...");
            item.setServiceImgDrawable(imgList[i]);
            arraylist.add(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void back(View view) {
        finish();
        if (android.os.Build.VERSION.SDK_INT > 5) {
            overridePendingTransition(
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }

}

