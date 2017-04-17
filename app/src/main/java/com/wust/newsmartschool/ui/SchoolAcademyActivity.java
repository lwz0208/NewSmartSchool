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
import com.wust.newsmartschool.adapter.ListView1Adapter;
import com.wust.newsmartschool.domain.ServiceItem;

import java.util.ArrayList;

public class SchoolAcademyActivity extends Activity {
    private String[] nameStrings = {
            "材料与冶金学院","汽车与交通工程学院","城市建设学院","外国语学院",
            "管理学院","文法与经济学院","国际学院","信息科学与工程学院","化学工程与技术学院","医学院",
            "机械自动化学院","艺术与设计学院","计算机科学与技术学院","资源与环境工程学院","理学院",
            "电子技术学院","临床学院、附属医院","职业技术学院","继续教育学院","马克思主义学院"
    };
    private String[] namalist = {"1、材料与冶金学院\n"
            + "     -----无机非金属材料工程系\n     -----金属材料工程系\n     -----冶金工程系\n     -----材料成型及控制工程系\n     -----热能与动力工程系",
            "2、汽车与交通工程学院\n"
                    + "     -----汽车工程系\n     -----交通工程系\n     -----交通运输系",
            "3、城市建设学院\n"
                    + "     -----土木工程系\n     -----建筑环境与设备系\n     -----建筑学系\n     -----给排水工程系",
            "4、外国语学院\n"
                    + "     -----英语系\n     -----德语系\n     -----大学生英语部\n     -----研究生公共外语部\n     -----英语语言学习中心",
            "5、管理学院\n"
                    + "     -----组织与人力资源系\n     -----管理科学与工程系\n     -----工商管理系\n     -----市场营销系\n     -----会计系\n     -----信息管理系",
            "6、文法与经济学院\n"
                    + "     -----法律系\n     -----国际经济与贸易系\n     -----公共管理系",
            "7、国际学院\n"
                    + "     -----商务教务部\n     -----语言教学部\n     -----英语培训中心\n     -----德语培训中心",
            "8、信息科学与工程学院\n"
                    + "     -----自动化系\n     -----电工电子基础部\n     -----电子信息工程系\n     -----电气工程系",
            "9、化学工程与技术学院\n"
                    + "     -----化学工程系\n     -----化工工艺系\n     -----生物工程系\n     -----化学系",
            "10、医学院\n"
                    + "     -----基础医学部\n     -----公共卫生学院\n     -----护理学系\n     -----药学系",
            "11、机械自动化学院\n"
                    + "     -----机械工程系\n     -----机械工学系\n     -----机械电子系\n     -----工业工程系\n     -----工程图学部\n     -----机械实验中心",
            "12、艺术与设计学院\n"
                    + "     -----工业设计系\n     -----美术系\n     -----建筑与艺术设计系",
            "13、计算机科学与技术学院\n"
                    + "     -----计算机科学系\n     -----计算机技术系\n     -----软件工程系\n     -----网络工程系\n     -----信息安全系",
            "14、资源与环境工程学院\n"
                    + "     -----矿物加工工程系\n     -----资源工程系\n     -----安全科学与工程系",
            "15、理学院\n"
                    + "     -----信息与计算机科学系\n     -----应用物理系\n     -----工程力学系\n     -----实验中心\n     -----环境科学与工程系",
            "16、电子技术学院\n"
                    + "     -----",
            "17、临床学院、附属医院\n"
                    + "     -----",
            "18、职业技术学院\n"
                    + "     -----",
            "19、继续教育学院\n"
                    + "     -----",
            "20、马克思主义学院\n"
                    +"     -----马克思主义原理系\n     -----马克思主义中国化系\n     -----思想政治教育系\n     -----中国近现代史系\n     -----艺术教研部\n"};
    private Context context;
    private ArrayList<ServiceItem> arraylist;
    private ListView listView;
    private ListView1Adapter listAdapter;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_school_academy);

        ImageView backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        context = getApplicationContext();
        listView=(ListView)findViewById(R.id.listview_academy);
        getListData();
        listAdapter=new ListView1Adapter(context, arraylist);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                //		dialog = ProgressDialog.show(
                //				SchoolAcademyActivity.this, "加载中，请稍后。。。", "", true, true);
                //		if (position==15 || position==16 || position==17 || position==19) {
                //			Toast.makeText(getApplicationContext(), "暂无信息",
                //					Toast.LENGTH_SHORT).show();
                //			dialog.cancel();
                //		} else {
                // 显示等待框
                //			Runnable runnable = new Runnable() {
                //				@Override
                //				public void run() {

                //				}
                //			};
                //		new Thread(runnable).start();

                Intent intent = new Intent();
                intent.setClass(SchoolAcademyActivity.this, ToAcademyActivity.class);
                intent.putExtra("name", nameStrings[position]);
                startActivity(intent);

            }

        });

    }


    public void getListData(){
        arraylist=new ArrayList<ServiceItem>();

        for(int i=0;i<namalist.length;i++){
            ServiceItem item=new ServiceItem();
            item.setServiceName(namalist[i]);
            arraylist.add(item);
        }
    }



}


