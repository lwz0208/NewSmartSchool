package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.utils.URL_UNIVERSAL;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.MediaType;


public class SchoolActivity extends Activity {
    private TextView schoolTxt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_school);

        ImageView backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        schoolTxt1 = (TextView) findViewById(R.id.shooltxt1);
        initContent();
    }

    private void initContent() {
        JSONObject jsonObject = new JSONObject();
        OkHttpUtils.postString().url(URL_UNIVERSAL.URL_SCHOOL_INTRODUCTION).content(jsonObject.toJSONString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.i("NewsResult", "校园简介接口访问失败：" + call + "---" + e);
                        Toast.makeText(SchoolActivity.this, "网络开小差了哦", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.i("NewsResult", "校园简介接口访问成功：" + response);
                        JSONObject jsonObject = JSON.parseObject(response);
                        if(jsonObject.getIntValue("code") == 1) {
                            schoolTxt1.setText(jsonObject.getString("data"));
                        } else if(jsonObject.getIntValue("code") == 0) {
                            Toast.makeText(SchoolActivity.this, "暂时没有数据哦", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SchoolActivity.this, "服务器异常...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}

