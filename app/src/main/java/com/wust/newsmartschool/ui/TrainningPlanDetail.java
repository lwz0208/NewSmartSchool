package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.utils.GlobalVar;

public class TrainningPlanDetail extends Activity
{

    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11,
            tv12, tv13, tv14, tv15, tv16, tv17, tv18;
    private TextView title;
    private ImageView goback;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_trainning_plan_detail);
        InitViews();
        setData();

    }

    private void setData()
    {
        // TODO Auto-generated method stub
        Bundle bundle = new Bundle();
        bundle = getIntent().getBundleExtra("datas");
        tv1.setText("学号：" + GlobalVar.userid);
        tv2.setText("姓名：" + GlobalVar.username);
        tv3.setText("课程号：" + bundle.getString("KCH"));
        tv4.setText("课程名：" + bundle.getString("KCM"));
        tv5.setText("开课院系：" + bundle.getString("KKYX"));

        tv6.setText("上课院系：" + bundle.getString("SKYX"));
        tv7.setText("上课专业：" + bundle.getString("SKZY"));
        tv8.setText("课程性质：" + bundle.getString("KCXZ"));
        tv9.setText("学分：" + bundle.getString("XF"));
        tv10.setText("开设学期：" + bundle.getString("KSXQ"));
        tv11.setText("讲课学时：" + bundle.getString("JKXS"));
        tv12.setText("实验学时：" + bundle.getString("SYXS"));
        tv13.setText("上机学时：" + bundle.getString("SJXS"));
        tv14.setText("总学时：" + bundle.getString("ZXS"));
        tv15.setText("所属方向：" + bundle.getString("FXMC"));
        tv16.setText("考核方式：" + bundle.getString("HKFS"));
        tv17.setText("周学时：" + bundle.getString("ZHXS"));
        tv18.setText("审核状态：" + bundle.getString("SHZT"));

    }

    private void InitViews()
    {
        title = (TextView) findViewById(R.id.head_title);
        title.setText("培养方案详情");
        goback = (ImageView) findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                TrainningPlanDetail.this.finish();
            }
        });

        tv1 = (TextView) findViewById(R.id.xuehao);
        tv2 = (TextView) findViewById(R.id.xingming);
        tv3 = (TextView) findViewById(R.id.kechenghao);
        tv4 = (TextView) findViewById(R.id.kechengming);
        tv5 = (TextView) findViewById(R.id.kaikeyuanxi);
        tv6 = (TextView) findViewById(R.id.shangkeyuanxi);
        tv7 = (TextView) findViewById(R.id.shangkezhuanye);
        tv8 = (TextView) findViewById(R.id.kechengxingzhi);
        tv9 = (TextView) findViewById(R.id.xuefen);
        tv10 = (TextView) findViewById(R.id.kaishexueqi);
        tv11 = (TextView) findViewById(R.id.jiangkexueshi);
        tv12 = (TextView) findViewById(R.id.shiyanxueshi);
        tv13 = (TextView) findViewById(R.id.shangjixueshi);
        tv14 = (TextView) findViewById(R.id.zongxueshi);
        tv15 = (TextView) findViewById(R.id.suoshufangxiang);
        tv16 = (TextView) findViewById(R.id.kaohefangshi);
        tv17 = (TextView) findViewById(R.id.zhouxueshi);
        tv18 = (TextView) findViewById(R.id.shenhezhuangtai);

    }

}

