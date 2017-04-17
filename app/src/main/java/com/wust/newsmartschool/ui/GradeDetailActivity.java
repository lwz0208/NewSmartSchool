package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.utils.GlobalVar;


public class GradeDetailActivity extends Activity {

    private TextView tv1,tv2;
    private TextView tv21,tv22,tv23,tv24,tv25,tv26,tv27,tv28,tv29;

    private TextView title;
    private ImageView backImageView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_grade_detail);


        title = (TextView) findViewById(R.id.head_title);
        title.setText("成绩详情");
        backImageView = (ImageView) findViewById(R.id.goback);
        backImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GradeDetailActivity.this.finish();
            }
        });
        tv1=(TextView)findViewById(R.id.xuhao);
        tv2=(TextView)findViewById(R.id.xh);
        tv21=(TextView)findViewById(R.id.xm);
        tv22=(TextView)findViewById(R.id.kkxq);
        tv23=(TextView)findViewById(R.id.kcmc);
        tv24=(TextView)findViewById(R.id.zcj);
        tv25=(TextView)findViewById(R.id.zxs);
        tv26=(TextView)findViewById(R.id.jd);
        tv27=(TextView)findViewById(R.id.kcsx);
        tv28=(TextView)findViewById(R.id.xf);
        tv29=(TextView)findViewById(R.id.kcxz);


        Bundle bundle=getIntent().getExtras();
        tv1.setText("序号："+bundle.getString("position"));
        tv2.setText("学号："+ GlobalVar.userid);
        tv21.setText("姓名："+GlobalVar.username);
        tv22.setText("开课学期："+bundle.getString("kkxq"));
        tv23.setText("课程名称："+bundle.getString("kcmc"));
        tv24.setText("总成绩："+bundle.getString("zcj"));
        tv25.setText("总学时："+bundle.getString("zxs"));
        tv26.setText("绩点："+bundle.getString("jd"));
        tv27.setText("课程属性："+bundle.getString("kcsx"));
        tv28.setText("学分："+bundle.getString("xf"));
        tv29.setText("课程性质："+bundle.getString("kcxz"));

    }
}

