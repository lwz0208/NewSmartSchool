package com.wust.newsmartschool.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.CommonListviewAdapter;
import com.wust.newsmartschool.utils.SysApplication;

public class StudentSelectActivity extends BaseActivity {
    private ListView listView;
    private CommonListviewAdapter listAdapter;
    private static final String[] nameList = {"选课", "评教"};
    private static final int[] imgList = {R.drawable.campus_bus_service_hjh, R.drawable.campus_bus_hjh};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.student_select_home);
        SysApplication.getInstance().addActivity(this);

        listView = (ListView) findViewById(R.id.student_select_list);

//		getListData();
        listAdapter = new CommonListviewAdapter(this, imgList, nameList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                       /* intent=new Intent(StudentSelectActivity.this, SelectActivity.class);
                        startActivity(intent);*/
                        Toast.makeText(getApplicationContext(), "接口有问题，现在不能用！",
                                Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        intent = new Intent(StudentSelectActivity.this, PjActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }

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




