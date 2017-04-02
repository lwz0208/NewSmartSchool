package com.wust.newsmartschool.ui;

import com.wust.newsmartschool.R;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.CommonUtils;
import com.wust.easeui.utils.PreferenceManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import okhttp3.Call;
import okhttp3.MediaType;

public class MeetingPointActivity extends Activity {
    private final static String URL = Constant.GETMEETINGTAG_URL;

    private TextView tv_startTime;
    private TextView tv_place;
    private TextView tv_meetingMember;
    private TextView tv_finish;

    private EditText et_meetingPoint;

    private int meetingID = -1;
    private final int resultCode_POINT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_point);

        tv_startTime = (TextView) findViewById(R.id.tv_startTime);
        tv_place = (TextView) findViewById(R.id.tv_place);
        tv_meetingMember = (TextView) findViewById(R.id.tv_meetingMember);
        tv_finish = (TextView) findViewById(R.id.tv_finish);

        et_meetingPoint = (EditText) findViewById(R.id.et_meetingPoint);

        meetingID = getIntent().getIntExtra("meetingID", -1);

        tv_startTime.setText(getIntent().getStringExtra("startTime"));
        tv_place.setText(getIntent().getStringExtra("meetingPlace"));
        tv_meetingMember.setText(getIntent().getStringExtra("meetingMember"));

        if (!(getIntent().getStringExtra("tag").equals("")))
            et_meetingPoint.setText(getIntent().getStringExtra("tag"));

        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                org.json.JSONObject jObject = new org.json.JSONObject();
                try {
                    jObject.put("meetingId", meetingID);
                    jObject.put("tag", et_meetingPoint.getText().toString());
                    CommonUtils.setCommonJson(MeetingPointActivity.this, jObject, PreferenceManager.getInstance().getCurrentUserFlowSId());
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                OkHttpUtils.postString().url(URL).content(jObject.toString())
                        .mediaType(MediaType.parse("application/json")).build()
                        .execute(new StringCallback() {

                            @Override
                            public void onError(Call call, Exception e) {
                                Log.i("meeting", "更新会议纪要接口访问失败：" + call.toString() + e.toString());
                            }

                            @Override
                            public void onResponse(String s) {
                                Log.i("meeting", "更新会议纪要接口访问成功：" + s.toString());
                                Toast.makeText(getApplicationContext(), "更新会议纪要成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.putExtra("point", et_meetingPoint.getText().toString());
                                setResult(resultCode_POINT, intent);
                                finish();
                            }
                        });
            }
        });
    }

    public void back(View view) {
        finish();

    }
}
