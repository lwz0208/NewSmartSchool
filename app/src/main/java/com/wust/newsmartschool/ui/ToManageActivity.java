package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wust.newsmartschool.R;

public class ToManageActivity extends Activity {
    private WebView webView;
    private TextView textView;
    private String[] manage = {
            "http://202.114.242.233:8043/default.html",
            "http://202.114.242.233:8045/default.html",
            "http://jwjc.wust.edu.cn/",
            "http://202.114.242.233:8008/default.html",
            "http://202.114.242.231:8005/default.html",
            "http://www.211.wust.edu.cn/",
            "http://202.114.242.233:8033/default.html",
            "http://202.114.242.233:8034/default.html",
            "http://202.114.242.233:8036/default.html",
            "http://202.114.242.233:8002/default.html",
            "http://www.yjsc.wust.edu.cn/index.aspx",
            "http://rsc.wust.edu.cn/",
            "http://202.114.242.231:8036/default.html",
            "http://xsh.wust.edu.cn/",
            "http://202.114.242.233:8046/default.html",
            "http://202.114.242.233:8047/default.html",
            "http://202.114.242.233:8000/default.html",
            "http://sbc.wust.edu.cn/",
            "http://jhc.wust.edu.cn/",
            "http://iro.wust.edu.cn/",
            "http://202.114.242.233:8026/default.html",
            "http://202.114.242.233:8048/default.html",
            "http://202.114.242.233:8049/default.html",
            "http://cgzb.wust.edu.cn/",
            "http://202.114.242.233:8003/default.html",
            "http://www.lib.wust.edu.cn/",
            "http://dag.wust.edu.cn/",
            "http://202.114.242.233:8035/default.html",
            "http://gjs.wust.edu.cn/",
            "http://wkdxb.wust.edu.cn/",
            "http://hqjt.wust.edu.cn/",
            "http://www.whkjdxyy.com/",
            "http://202.114.242.233:8001/default.html",
            "",
            "",
            "http://jnjp.wust.edu.cn/",
            "http://202.114.242.233:8060/default.html"
    };
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
        setContentView(R.layout.activity_to_manage);

        ImageView backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webView = (WebView)this.findViewById(R.id.webview_manage);
        textView = (TextView)this.findViewById(R.id.textview_name1);
        Intent intent = getIntent();
        String nameString = intent.getStringExtra("name1");
        textView.setText(nameString);
        int i;
        for ( i= 0; i < nameList.length; i++) {
            if (nameString.equals(nameList[i])) {
                break;
            }

        }
        if (i==34 || i==33) {
            Toast.makeText(getApplicationContext(), "暂无信息", Toast.LENGTH_SHORT).show();
        }
        else {
            webView.loadUrl(manage[i]);
        }


    }

}

