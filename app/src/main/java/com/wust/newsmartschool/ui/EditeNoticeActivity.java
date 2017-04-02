package com.wust.newsmartschool.ui;

import okhttp3.Call;
import okhttp3.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.views.ECProgressDialog;
import com.wust.easeui.utils.CommonUtils;
import com.wust.easeui.utils.PreferenceManager;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.wust.easeui.Constant;
import com.wust.easeui.domain.Notice_data;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.os.Bundle;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.Context;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;

public class EditeNoticeActivity extends BaseActivity implements OnClickListener {
    private String groupId;
    private ImageView groupnotice_rightimage;
    private EditText group_edit;
    String userName;
    private Button btn_groupnotice;
    private LinearLayout ll_no_notice;
    // 获取群公告尸体
    Notice_data data;
    private ECProgressDialog pd;
    boolean noticeFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite__notice);
        groupId = getIntent().getStringExtra("groupId");
        groupnotice_rightimage = (ImageView) findViewById(R.id.groupnotice_rightimage);
        groupnotice_rightimage.setOnClickListener(this);
        group_edit = (EditText) findViewById(R.id.group_edit);
        btn_groupnotice = (Button) findViewById(R.id.btn_groupnotice);
        ll_no_notice = (LinearLayout) findViewById(R.id.ll_no_notice);
        pd = new ECProgressDialog(EditeNoticeActivity.this);

        pd.show();
        btn_groupnotice.setOnClickListener(this);

        data = new Notice_data();
        userName = EMClient.getInstance().getCurrentUser();
        try {
            getGroupNotice();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void getGroupNotice() throws JSONException {
        JSONObject noticeJson = new JSONObject();
        noticeJson.put("groupId", groupId);
        noticeJson.put("userName", userName);
        CommonUtils.setCommonJson(EditeNoticeActivity.this, noticeJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i("noticeJson", noticeJson.toString());
        OkHttpUtils.postString().url(Constant.GETGROUPNOTICE_URL)
                .content(noticeJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        JSONObject jObject;
                        try {
                            pd.dismiss();
                            jObject = new JSONObject(arg0);
                            Log.i("jObject_LoginActivity",
                                    jObject.getInt("code") + "-"
                                            + jObject.getString("msg") + "-"
                                            + jObject.getString("data"));
                            if (jObject.getInt("code") == 1
                                    && jObject.getString("msg").equals("请求成功")) {
                                Log.i("jObject_LoginActivity", "有数据!null");
                                noticeFlag = true;
                                data = new Gson().fromJson(
                                        jObject.getString("data"),
                                        Notice_data.class);
                                group_edit.setVisibility(View.VISIBLE);
                                btn_groupnotice.setVisibility(View.VISIBLE);
                                group_edit.setText(data.getContent());
                                group_edit.setEnabled(false);
                                btn_groupnotice.setText("重新编辑");
                            } else if (jObject.getInt("code") == 1
                                    && jObject.getString("msg").equals("无数据")) {
                                Log.i("jObject_LoginActivity", "无数据null");
                                noticeFlag = false;
                                ll_no_notice.setVisibility(View.VISIBLE);
                                groupnotice_rightimage
                                        .setVisibility(View.VISIBLE);
                                groupnotice_rightimage
                                        .setImageResource(R.drawable.group_notice_write);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            showToastShort("请求失败");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        pd.dismiss();

                    }
                });

    }

    private void sentGroupNotice() throws JSONException {
        JSONObject editnoticeJson = new JSONObject();
        editnoticeJson.put("groupId", groupId);
        editnoticeJson.put("content", group_edit.getText().toString());
        editnoticeJson.put("editor", userName);
        CommonUtils.setCommonJson(EditeNoticeActivity.this, editnoticeJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i("noticeJson", editnoticeJson.toString());

        OkHttpUtils.postString().url(Constant.EDITGROUPNOTICE_URL)
                .content(editnoticeJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        JSONObject jObject;
                        try {
                            pd.dismiss();
                            jObject = new JSONObject(arg0);
                            Log.i("jObject_LoginActivity",
                                    jObject.getInt("code") + "-"
                                            + jObject.getString("msg") + "-"
                                            + jObject.getString("data"));
                            if (jObject.getInt("code") == 1) {
                                Toast.makeText(EditeNoticeActivity.this,
                                        jObject.getString("msg"),
                                        Toast.LENGTH_SHORT).show();
                                //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
                                EMMessage message = EMMessage.createTxtSendMessage("群公告：\n" + group_edit.getText().toString(), groupId);
                                message.setChatType(ChatType.GroupChat);
                                //发送消息
                                EMClient.getInstance().chatManager().sendMessage(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        EditeNoticeActivity.this.finish();
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        pd.dismiss();
                        Toast.makeText(EditeNoticeActivity.this,
                                "发布公告失败,请稍后再试。", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    public void back(View view) {
        finish();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_groupnotice:
                btn_groupnotice.setVisibility(View.GONE);
                groupnotice_rightimage.setVisibility(View.VISIBLE);
                group_edit.setEnabled(true);
                group_edit.setFocusable(true);
                group_edit.requestFocus();
                group_edit.setFocusableInTouchMode(true);
                InputMethodManager immInputMethod = (InputMethodManager) group_edit
                        .getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                immInputMethod.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                Selection.setSelection(group_edit.getText(), group_edit.length());

                break;
            case R.id.groupnotice_rightimage:
                if (noticeFlag) {
                    pd.show();
                    try {
                        sentGroupNotice();
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else {
                    // 把没有内容的相关图标隐藏
                    ll_no_notice.setVisibility(View.GONE);
                    groupnotice_rightimage
                            .setImageResource(R.drawable.group_notice_yes);
                    // 把开始写内容的相关图标显示
                    group_edit.setVisibility(View.VISIBLE);
                    group_edit.setHint("输入文字...");
                    group_edit.setEnabled(true);
                    group_edit.setFocusable(true);
                    group_edit.requestFocus();
                    group_edit.setFocusableInTouchMode(true);
                    InputMethodManager immInputMethod2 = (InputMethodManager) group_edit
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    immInputMethod2.toggleSoftInput(0,
                            InputMethodManager.SHOW_FORCED);
                    if (group_edit.getText().length() != 0) {
                        try {
                            sentGroupNotice();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(EditeNoticeActivity.this, "请输入公告内容",
                                Toast.LENGTH_SHORT).show();

                    }

                }

                break;
            default:
                break;
        }

    }
}
