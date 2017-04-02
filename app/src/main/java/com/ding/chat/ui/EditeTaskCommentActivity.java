package com.ding.chat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.ding.chat.R;
import com.ding.chat.domain.NoticeCommentEntity;
import com.ding.chat.views.ECProgressDialog;
import com.ding.easeui.Constant;
import com.ding.easeui.utils.CommonUtils;
import com.ding.easeui.utils.PreferenceManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MediaType;

public class EditeTaskCommentActivity extends BaseActivity {

    String TAG = "EditeTaskCommentActivity";
    int taskID_comment;
    private EditText noticecomment_edit;
    private ImageView noticecomment_btn;
    ECProgressDialog pd;
    String toUserIds = "";
    //接收传过来的评论实体，如果有此数据，那就表明这个是点击评论列表里过来的，也就是这是一个回复评论
    NoticeCommentEntity.DataBean recommentdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_noticecomment);
        noticecomment_edit = (EditText) findViewById(R.id.noticecomment_edit);
        noticecomment_btn = (ImageView) findViewById(R.id.noticecomment_btn);
        noticecomment_btn.setEnabled(false);
        noticecomment_btn.setVisibility(View.GONE);
        recommentdata = (NoticeCommentEntity.DataBean) getIntent().getSerializableExtra("recomment");
        taskID_comment = getIntent().getIntExtra("taskid", -1);
        toUserIds = getIntent().getStringExtra("toUserIds");
        pd = new ECProgressDialog(EditeTaskCommentActivity.this);
        pd.setPressText("正在发表评论");
        if (recommentdata != null) {
            noticecomment_edit.setHint("回复给" + recommentdata.getUserRealname());
        }
        noticecomment_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    noticecomment_btn.setEnabled(true);
                    noticecomment_btn.setVisibility(View.VISIBLE);
                } else {
                    noticecomment_btn.setEnabled(false);
                    noticecomment_btn.setVisibility(View.GONE);
                }

            }
        });
        noticecomment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                sentCommentContent();
            }
        });

    }


    //发布评论
    private void sentCommentContent() {
        JSONObject commentJson = new JSONObject();
        try {
            commentJson.put("userId", PreferenceManager.getInstance().getCurrentUserId());
            commentJson.put("userRealname", PreferenceManager.getInstance().getCurrentRealName());
            if (recommentdata != null) {
                try {
                    commentJson.put("toUserName", recommentdata.getToUserName());
                    commentJson.put("toUserId", recommentdata.getUserId());
                    commentJson.put("pid", recommentdata.getCommentId());
                    commentJson.put("content", "回复" + recommentdata.getUserRealname() + ":" + noticecomment_edit.getText().toString());
                } catch (Exception e) {
                    commentJson.put("toUserName", "");
                    commentJson.put("toUserId", "");
                    commentJson.put("pid", 0);
                    commentJson.put("content", noticecomment_edit.getText().toString());
                }
            } else {
                commentJson.put("toUserName", "");
                commentJson.put("toUserId", "");
                commentJson.put("pid", 0);
                commentJson.put("content", noticecomment_edit.getText().toString());
            }
            commentJson.put("objectId", taskID_comment);
            commentJson.put("objectTable", "task");


            commentJson.put("toUserIds", toUserIds);
            CommonUtils.setCommonJson(EditeTaskCommentActivity.this, commentJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("commentJson", commentJson.toString());

        OkHttpUtils.postString().url(Constant.EDITCOMMENT_URL)
                .content(commentJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                             @Override
                             public void onError(Call call, Exception e) {
                                 pd.dismiss();
                                 showToastShort("发表评论失败");
                             }

                             @Override
                             public void onResponse(String s) {
                                 Log.e(TAG, s);
                                 pd.dismiss();
                                 JSONObject jObject;
                                 try {
                                     jObject = new JSONObject(s);
                                     if (jObject.getInt("code") == 1) {
                                         showToastShort("发表评论成功");
                                         setResult(
                                                 RESULT_OK,
                                                 new Intent()
                                                         .putExtra("needfreshcomment",
                                                                 1));
                                         finish();

                                     }
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }
//
                             }
                         }

                );

    }

    public void back(View view) {
        setResult(
                RESULT_CANCELED,
                null);
        finish();

    }

}
