package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wust.newsmartschool.R;


public class ToAcademyActivity extends Activity {
    private WebView webView;
    private TextView textView;
    private String[] academy = {
            "http://cy.wust.edu.cn/Home",
            "http://202.114.242.231:8006/default.html",
            "http://202.114.242.231:8007/default.html",
            "http://202.114.242.231:8037/default.html",
            "http://202.114.242.231:8041/default.html",
            "http://202.114.255.64:8001/default.html",
            "http://www.wis.wust.edu.cn/",
            "http://202.114.242.231:8021/default.html",
            "http://202.114.242.231:8022/default.html",
            "http://202.114.242.231:8039/default.html",
            "http://202.114.242.231:8038/default.html",
            "http://202.114.242.233:8052/default.html",
            "http://www.cs.wust.edu.cn/",
            "http://202.114.242.231:8040/default.html",
            "http://202.114.242.231:8050/default.html",
            "",
            "",
            "",
            "http://my.wust.edu.cn/",
            ""
    };
    private String[] nameStrings = {
            "材料与冶金学院","汽车与交通工程学院","城市建设学院","外国语学院",
            "管理学院","文法与经济学院","国际学院","信息科学与工程学院","化学工程与技术学院","医学院",
            "机械自动化学院","艺术与设计学院","计算机科学与技术学院","资源与环境工程学院","理学院",
            "电子技术学院","临床学院、附属医院","职业技术学院","马克思主义教研部","继续教育学院"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_to_academy);

        ImageView backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webView = (WebView)this.findViewById(R.id.webview_academy);
        textView = (TextView)this.findViewById(R.id.textview_name);
        Intent intent = getIntent();
        String nameString = intent.getStringExtra("name");
        textView.setText(nameString);
        int i;
        for ( i= 0; i < nameStrings.length; i++) {
            if (nameString.equals(nameStrings[i])) {
                break;
            }

        }
        webView.loadUrl(academy[i]);

    }

}

