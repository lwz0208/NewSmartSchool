package com.ding.chat.ui;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import com.ding.chat.R;
import com.ding.easeui.utils.PreferenceManager;
import com.ding.chat.views.ECProgressDialog;
import com.ding.easeui.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class JFlowItemApplyActivity extends Activity {
    String TAG = "JFlowItemApplyActivity_Debugs";
    private TextView jflowapply_title;
    private EditText ed_apply_content;
    private EditText ed_apply_title;
    private TextView jflowapply_finish;
    private ECProgressDialog dialog;
    String fk_flow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jflow_item_apply);
        fk_flow = getIntent().getStringExtra("fk_flow");
        Log.e(TAG, fk_flow);
        jflowapply_title = (TextView) findViewById(R.id.jflowapply_title);
        ed_apply_content = (EditText) findViewById(R.id.ed_apply_content);
        ed_apply_title = (EditText) findViewById(R.id.ed_apply_title);
        jflowapply_finish = (TextView) findViewById(R.id.jflowapply_finish);
        dialog = new ECProgressDialog(JFlowItemApplyActivity.this);
        dialog.setPressText("正在申请");
        jflowapply_finish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(ed_apply_content.getText())
                        || TextUtils.isEmpty(ed_apply_title.getText())) {
                    Toast.makeText(getApplicationContext(), "题目或内容不能为空",
                            Toast.LENGTH_SHORT).show();
                } else {
                    dialog.show();
                    getData();
                }

            }
        });
    }

    private void getData() {
        Log.e(TAG, PreferenceManager.getInstance().getCurrentUserId() + "/" + PreferenceManager.getInstance().getCurrentUserFlowSId() + "/" + fk_flow + "/" + ed_apply_content.getText().toString() + "/" + ed_apply_title.getText().toString());
        OkHttpUtils.post().url(Constant.FLOWLIST_MEETTINGAPPLY_URL)
                .addParams("userId", PreferenceManager.getInstance().getCurrentUserId())
                .addParams("sid", PreferenceManager.getInstance().getCurrentUserFlowSId())
                .addParams("fk_flow", fk_flow)
                .addParams("content", ed_apply_content.getText().toString())
                .addParams("topic_title", ed_apply_title.getText().toString())
                .addParams("length_time", "length_time")
                .addParams("fils_ids", "123").build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        dialog.dismiss();
                        Log.e(TAG, arg0.toString());
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1) {
                                Toast.makeText(getApplicationContext(),
                                        "流程申请成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(JFlowItemApplyActivity.this,
                                        jObject.getInt("msg"), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        dialog.dismiss();
                        Toast.makeText(JFlowItemApplyActivity.this, "请求服务器失败",
                                Toast.LENGTH_SHORT).show();
                        Log.e(TAG, arg0.toString() + "/" + arg1.toString());
                    }
                });

    }

    public void back(View view) {
        finish();

    }
}
