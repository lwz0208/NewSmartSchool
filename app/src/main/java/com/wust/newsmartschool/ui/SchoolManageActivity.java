package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.ListViewAdapter;
import com.wust.newsmartschool.domain.ServiceItem;

import java.util.ArrayList;

public class SchoolManageActivity extends Activity {

    private Context context;
    private ArrayList<ServiceItem> arraylist;
    private ListView listView;
    private ListViewAdapter listAdapter;
    private ProgressDialog dialog;
    private String[] nameList={"学校办公室（政策法规研究室）","校友会办公室","纪委办公室（监察处）",
            "党委组织部（统战部）","党委宣传部（新闻中心、党校）","党委学生工作部（武装部、学生工作处）",
            "工会","妇女委员会","团委","学生会","科学技术发展院","研究生院（党委研究生工作部）","人事处","教务处","教学质量监控与评估处","财务处","审计处",
            "实验室与设备管理处","基建与后勤管理处","国际交流合作处","离退休工作处","保卫处（党委保卫部）",
            "住宅建设与改革领导小组办公室","采购与招标管理办公室","工程训练中心","图书馆","档案馆","现代教育信息中心",
            "高等教育研究所","学报编辑部","后勤集团","校医院","资产经营有限公司","国际钢铁研究院",
            "武钢—武科大钢铁新技术研究院","绿色制造与节能减排中心","高性能钢铁材料及其应用湖北省协同创新中心"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_school_manage);

        ImageView backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        context = getApplicationContext();
        listView=(ListView)findViewById(R.id.listview_manage);
        getListData();
        listAdapter=new ListViewAdapter(context, arraylist);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                //	dialog = ProgressDialog.show(
                //			SchoolManageActivity.this, "加载中，请稍后。。。", "", true, true);

                // 显示等待框
                //		Runnable runnable = new Runnable() {
                //			@Override
                //			public void run() {

                //			}
                //		};
                //	new Thread(runnable).start();

                Intent intent = new Intent();
                intent.setClass(SchoolManageActivity.this, ToManageActivity.class);
                intent.putExtra("name1", nameList[position]);
                startActivity(intent);
            }

        });

    }


    public void getListData(){
        arraylist=new ArrayList<ServiceItem>();

        for(int i=0;i<nameList.length;i++){
            ServiceItem item=new ServiceItem();
            item.setServiceName(nameList[i]);
            arraylist.add(item);
        }
    }


}

