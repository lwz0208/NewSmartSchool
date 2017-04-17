package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.SchoolLeader;


public class LeaderDetailActivity extends Activity {
    private TextView tv_name;
    private TextView tv_job;
    private TextView tv_desc;
    private ImageView backImageView;
    private ImageView iv_headIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_leader_detail);

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_job = (TextView) findViewById(R.id.tv_job);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        iv_headIcon = (ImageView) findViewById(R.id.iv_headIcon);
        backImageView = (ImageView) findViewById(R.id.iv_back);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = this.getIntent();
        SchoolLeader leader =(SchoolLeader)intent.getSerializableExtra("leader");

        tv_name.setText(leader.getName());
        tv_job.setText(leader.getJob());
        tv_desc.setText("    " + leader.getDesc());
        Glide.with(LeaderDetailActivity.this).load(leader.getImg()).into(iv_headIcon);
    }
}

