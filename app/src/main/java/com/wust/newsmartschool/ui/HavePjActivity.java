package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wust.newsmartschool.R;

public class HavePjActivity extends Activity {

    private TextView head_title=null;
    private ImageView goback=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_have_pj);
        head_title= (TextView) findViewById(R.id.head_title);
        head_title.setText("评价结果");
        goback= (ImageView) findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
