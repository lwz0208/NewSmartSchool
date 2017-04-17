package com.wust.newsmartschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wust.newsmartschool.R;

public class Security_Department_Activity extends AppCompatActivity implements View.OnClickListener {
    // 标题控件
    private Toolbar toolbar;
    // 功能控件
    private TextView h_t1;// 户口文本1
    private TextView h_t2;// 户口文本2
    private TextView h_t3;// 户口文本3
    private TextView b_t;// 报警文本
    private TextView c_t1;// 车辆门禁文本
    private TextView c_t2;
    private TextView c_t3;
    private TextView s_t1;// 宿舍方面的文本
    private TextView s_t2;

    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security__department_);
        InitView();
    }

    private void InitView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 初始化功能控件
        h_t1 = (TextView) findViewById(R.id.h_tx1);
        h_t2 = (TextView) findViewById(R.id.h_tx2);
        h_t3 = (TextView) findViewById(R.id.h_tx3);
        b_t = (TextView) findViewById(R.id.b_tx);
        c_t1 = (TextView) findViewById(R.id.che_tx1);
        c_t2 = (TextView) findViewById(R.id.che_tx2);
        c_t3 = (TextView) findViewById(R.id.che_tx3);
        s_t1 = (TextView) findViewById(R.id.s_tx1);
        s_t2 = (TextView) findViewById(R.id.s_tx2);
        h_t1.setOnClickListener(this);
        h_t2.setOnClickListener(this);
        h_t3.setOnClickListener(this);
        b_t.setOnClickListener(this);
        c_t1.setOnClickListener(this);
        c_t2.setOnClickListener(this);
        c_t3.setOnClickListener(this);
        s_t1.setOnClickListener(this);
        s_t2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent=new Intent(Security_Department_Activity.this,Security_Department_detail_Activity.class);
        switch (v.getId()) {
            case R.id.h_tx1:
                intent.putExtra("id", 1);
                break;
            case R.id.h_tx2:
                intent.putExtra("id", 2);
                break;
            case R.id.h_tx3:
                intent.putExtra("id", 3);
                break;
            case R.id.b_tx:
                intent.putExtra("id", 4);
                break;
            case R.id.che_tx1:
                intent.putExtra("id", 5);
                break;
            case R.id.che_tx2:
                intent.putExtra("id", 6);
                break;
            case R.id.che_tx3:
                intent.putExtra("id", 7);
                break;
            case R.id.s_tx1:
                intent.putExtra("id", 8);
                break;
            case R.id.s_tx2:
                intent.putExtra("id", 9);
                break;
            default:
                break;
        }
        startActivity(intent);
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
}

