package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.TimeTableItem;

import java.util.ArrayList;


public class TimeTableDetailActivity extends Activity {
    private TextView tv1;
    private TextView tv21,tv23,tv24,tv25,tv26,tv27;

    private TextView tv01;
    private TextView tv021,tv023,tv024,tv025,tv026,tv027;
    private LinearLayout grade2;
    private TextView title;
    private ImageView backImageView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_time_table_detail);

        title = (TextView) findViewById(R.id.head_title);
        title.setText("课表详情");
        backImageView = (ImageView) findViewById(R.id.goback);
        backImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TimeTableDetailActivity.this.finish();
            }
        });
        tv1=(TextView)findViewById(R.id.timetable_kcmc);
        tv01=(TextView)findViewById(R.id.timetable_kcmc1);

        tv21=(TextView)findViewById(R.id.timetable_bjmc);
        tv021=(TextView)findViewById(R.id.timetable_bjmc1);


        tv23=(TextView)findViewById(R.id.timetable_kkzc);
        tv023=(TextView)findViewById(R.id.timetable_kkzc1);

        tv24=(TextView)findViewById(R.id.timetable_kcsj);
        tv024=(TextView)findViewById(R.id.timetable_kcsj1);

        tv25=(TextView)findViewById(R.id.timetable_jxl);
        tv025=(TextView)findViewById(R.id.timetable_jxl1);

        tv26=(TextView)findViewById(R.id.timetable_jsmc);
        tv026=(TextView)findViewById(R.id.timetable_jsmc1);

        tv27=(TextView)findViewById(R.id.timetable_jsxm);
        tv027=(TextView)findViewById(R.id.timetable_jsxm1);
        grade2=(LinearLayout)findViewById(R.id.grade2);

        ArrayList<TimeTableItem> list=new ArrayList<TimeTableItem>();
        list=(ArrayList<TimeTableItem>)getIntent().getSerializableExtra("items");
        if(list.size()==1){
            TimeTableItem item=list.get(0);
//			Bundle bundle=getIntent().getExtras();
            tv1.setText("课程名称："+item.getKcmc());
            tv21.setText("合班名称："+item.getBjmc());
            tv23.setText("上课周次："+item.getKkzc());
            tv24.setText("上课时间："+item.getKcsj());
            tv25.setText("教学楼："+item.getJxl());
            tv26.setText("教室："+item.getJsmc());
            tv27.setText("教师姓名："+item.getJsxm());

            tv01.setVisibility(View.GONE);
            tv021.setVisibility(View.GONE);
            tv023.setVisibility(View.GONE);
            tv024.setVisibility(View.GONE);
            tv025.setVisibility(View.GONE);
            tv026.setVisibility(View.GONE);
            tv027.setVisibility(View.GONE);
        }else if(list.size()==2){
            grade2.setVisibility(View.VISIBLE);
            TimeTableItem item1=list.get(0);
            TimeTableItem item2=list.get(1);
            tv1.setText("课程名称一："+item1.getKcmc());
            tv21.setText("合班名称："+item1.getBjmc());
            tv23.setText("上课周次："+item1.getKkzc());
            tv24.setText("上课时间："+item1.getKcsj());
            tv25.setText("教学楼："+item1.getJxl());
            tv26.setText("教室："+item1.getJsmc());
            tv27.setText("教师姓名："+item1.getJsxm());

            tv01.setText("课程名称二："+item2.getKcmc());
            tv021.setText("合班名称："+item2.getBjmc());
            tv023.setText("上课周次："+item2.getKkzc());
            tv024.setText("上课时间："+item2.getKcsj());
            tv025.setText("教学楼："+item2.getJxl());
            tv026.setText("教室："+item2.getJsmc());
            tv027.setText("教师姓名："+item2.getJsxm());

        }
//		TimeTableItem item=list.get(0);
//		tv1.setText(item.getKcmc());
//		tv21.setText(item.getBjmc());
//		Bundle bundle=getIntent().getExtras();
//		tv1.setText("课程名称："+bundle.getString("kcmc"));
//		tv21.setText("合班名称："+bundle.getString("bjmc"));
//		tv22.setText("开课学期："+bundle.getString("kkxq"));
//		tv23.setText("上课周次："+bundle.getString("kkzc"));
//		tv24.setText("上课时间："+bundle.getString("kcsj"));
//		tv25.setText("教学楼："+bundle.getString("jxl"));
//		tv26.setText("教室："+bundle.getString("jsmc"));
//		tv27.setText("教师姓名："+bundle.getString("jsxm"));

    }
}

