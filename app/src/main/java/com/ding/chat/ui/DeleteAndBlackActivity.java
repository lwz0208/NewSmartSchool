package com.ding.chat.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.ding.chat.R;

public class DeleteAndBlackActivity extends Activity {
    private RelativeLayout rl_black;
    private RelativeLayout rl_delete;

    private final int RESULT_BLACK = 1;
    private final int RESULT_DELETE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_and_black);

        rl_black = (RelativeLayout) findViewById(R.id.rl_black);
        rl_delete = (RelativeLayout) findViewById(R.id.rl_delete);
        rl_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_BLACK, new Intent());
                finish();
            }
        });
        rl_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_DELETE, new Intent());
                finish();
            }
        });
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
