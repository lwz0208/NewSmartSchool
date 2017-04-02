package com.ding.chat.ui;

import com.ding.chat.DemoApplication;
import com.ding.chat.DemoHelper;
import com.ding.chat.DemoModel;
import com.ding.chat.R;
import com.ding.easeui.Constant;
import com.ding.easeui.utils.CommonUtils;
import com.ding.easeui.utils.PreferenceManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.ding.easeui.widget.EaseSwitchButton;
import com.hyphenate.util.EMLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.preference.PreferenceGroup;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MediaType;

public class MessageSettingActivity extends BaseActivity implements OnClickListener {

    String TAG = "MessageSettingActivity_Debugs";
    /**
     * 设置新消息通知布局
     */
    private RelativeLayout rl_switch_notification;
    /**
     * 设置声音布局
     */
    private RelativeLayout rl_switch_sound;
    /**
     * 设置震动布局
     */
    private RelativeLayout rl_switch_vibrate;
    /**
     * 设置扬声器布局
     */
    private RelativeLayout rl_switch_speaker;

    /**
     * 声音和震动中间的那条线
     */
    private TextView textview1, textview2;

    private LinearLayout blacklistContainer;

    private LinearLayout userProfileContainer;

    /**
     * 退出按钮
     */
    private Button logoutBtn;

    private RelativeLayout rl_switch_chatroom_leave;

    private RelativeLayout rl_switch_delete_msg_when_exit_group;
    private RelativeLayout rl_switch_auto_accept_group_invitation;
    private RelativeLayout rl_switch_adaptive_video_encode;
    private RelativeLayout rl_switch_only_get_friends_messages;
    private RelativeLayout rl_switch_display_message_detail;

    /**
     * 诊断
     */
    private LinearLayout llDiagnose;
    /**
     * iOS离线推送昵称
     */
    private LinearLayout pushNick;

    private EaseSwitchButton notifiSwitch;
    private EaseSwitchButton soundSwitch;
    private EaseSwitchButton vibrateSwitch;
    private EaseSwitchButton speakerSwitch;
    private EaseSwitchButton ownerLeaveSwitch;
    private EaseSwitchButton switch_delete_msg_when_exit_group;
    private EaseSwitchButton switch_auto_accept_group_invitation;
    private EaseSwitchButton switch_only_get_friends_messages;
    private EaseSwitchButton switch_adaptive_video_encode;
    private EaseSwitchButton switch_display_message_detail;
    private DemoModel settingsModel;
    private EMOptions chatOptions;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_orignal_fragment_conversation_settings);
        if (savedInstanceState != null
                && savedInstanceState.getBoolean("isConflict", false))
            return;
        pd = new ProgressDialog(MessageSettingActivity.this);
        pd.setMessage("正在设置");
        rl_switch_display_message_detail = (RelativeLayout) findViewById(R.id.rl_switch_display_message_detail);
        rl_switch_notification = (RelativeLayout) findViewById(R.id.rl_switch_notification);
        rl_switch_sound = (RelativeLayout) findViewById(R.id.rl_switch_sound);
        rl_switch_vibrate = (RelativeLayout) findViewById(R.id.rl_switch_vibrate);
        rl_switch_speaker = (RelativeLayout) findViewById(R.id.rl_switch_speaker);
        rl_switch_chatroom_leave = (RelativeLayout) findViewById(R.id.rl_switch_chatroom_owner_leave);
        rl_switch_delete_msg_when_exit_group = (RelativeLayout) findViewById(R.id.rl_switch_delete_msg_when_exit_group);
        rl_switch_auto_accept_group_invitation = (RelativeLayout) findViewById(R.id.rl_switch_auto_accept_group_invitation);
        rl_switch_adaptive_video_encode = (RelativeLayout) findViewById(R.id.rl_switch_adaptive_video_encode);
        rl_switch_only_get_friends_messages = (RelativeLayout) findViewById(R.id.rl_switch_only_get_friends_messages);
        notifiSwitch = (EaseSwitchButton) findViewById(R.id.switch_notification);
        switch_display_message_detail = (EaseSwitchButton) findViewById(R.id.switch_display_message_detail);
        soundSwitch = (EaseSwitchButton) findViewById(R.id.switch_sound);
        vibrateSwitch = (EaseSwitchButton) findViewById(R.id.switch_vibrate);
        speakerSwitch = (EaseSwitchButton) findViewById(R.id.switch_speaker);
        ownerLeaveSwitch = (EaseSwitchButton) findViewById(R.id.switch_owner_leave);
        switch_delete_msg_when_exit_group = (EaseSwitchButton) findViewById(R.id.switch_delete_msg_when_exit_group);
        switch_auto_accept_group_invitation = (EaseSwitchButton) findViewById(R.id.switch_auto_accept_group_invitation);
        switch_adaptive_video_encode = (EaseSwitchButton) findViewById(R.id.switch_adaptive_video_encode);
        switch_only_get_friends_messages = (EaseSwitchButton) findViewById(R.id.switch_only_get_friends_messages);
        logoutBtn = (Button) findViewById(R.id.btn_logout);
        if (!TextUtils.isEmpty(EMClient.getInstance().getCurrentUser())) {
            logoutBtn.setText(getString(R.string.button_logout) + "("
                    + EMClient.getInstance().getCurrentUser() + ")");
        }

        textview1 = (TextView) findViewById(R.id.textview1);
        textview2 = (TextView) findViewById(R.id.textview2);

        blacklistContainer = (LinearLayout) findViewById(R.id.ll_black_list);
        userProfileContainer = (LinearLayout) findViewById(R.id.ll_user_profile);
        llDiagnose = (LinearLayout) findViewById(R.id.ll_diagnose);
        pushNick = (LinearLayout) findViewById(R.id.ll_set_push_nick);

        settingsModel = DemoHelper.getInstance().getModel();
        chatOptions = EMClient.getInstance().getOptions();

        blacklistContainer.setOnClickListener(this);
        userProfileContainer.setOnClickListener(this);
        rl_switch_notification.setOnClickListener(this);
        rl_switch_sound.setOnClickListener(this);
        rl_switch_vibrate.setOnClickListener(this);
        rl_switch_speaker.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        llDiagnose.setOnClickListener(this);
        pushNick.setOnClickListener(this);
        rl_switch_chatroom_leave.setOnClickListener(this);
        rl_switch_delete_msg_when_exit_group.setOnClickListener(this);
        rl_switch_auto_accept_group_invitation.setOnClickListener(this);
        rl_switch_adaptive_video_encode.setOnClickListener(this);
        rl_switch_only_get_friends_messages.setOnClickListener(this);
        rl_switch_display_message_detail.setOnClickListener(this);
        // 震动和声音总开关，来消息时，是否允许此开关打开
        // the vibrate and sound notification are allowed or not?
        if (settingsModel.getSettingMsgNotification()) {
            notifiSwitch.openSwitch();
        } else {
            notifiSwitch.closeSwitch();
        }

        // 是否打开声音
        // sound notification is switched on or not?
        if (settingsModel.getSettingMsgSound()) {
            soundSwitch.openSwitch();
        } else {
            soundSwitch.closeSwitch();
        }

        // 是否打开震动
        // vibrate notification is switched on or not?
        if (settingsModel.getSettingMsgVibrate()) {
            vibrateSwitch.openSwitch();
        } else {
            vibrateSwitch.closeSwitch();
        }

        // 是否打开扬声器
        // the speaker is switched on or not?
        if (settingsModel.getSettingMsgSpeaker()) {
            speakerSwitch.openSwitch();
        } else {
            speakerSwitch.closeSwitch();
        }

        // 是否允许聊天室owner leave
        if (settingsModel.isChatroomOwnerLeaveAllowed()) {
            ownerLeaveSwitch.openSwitch();
        } else {
            ownerLeaveSwitch.closeSwitch();
        }

        // delete messages when exit group?
        if (settingsModel.isDeleteMessagesAsExitGroup()) {
            switch_delete_msg_when_exit_group.openSwitch();
        } else {
            switch_delete_msg_when_exit_group.closeSwitch();
        }

        if (settingsModel.isAutoAcceptGroupInvitation()) {
            switch_auto_accept_group_invitation.openSwitch();
        } else {
            switch_auto_accept_group_invitation.closeSwitch();
        }

        if (settingsModel.isAdaptiveVideoEncode()) {
            switch_adaptive_video_encode.openSwitch();
            EMClient.getInstance().callManager().getVideoCallHelper()
                    .setAdaptiveVideoFlag(true);
        } else {
            switch_adaptive_video_encode.closeSwitch();
            EMClient.getInstance().callManager().getVideoCallHelper()
                    .setAdaptiveVideoFlag(false);
        }
        Log.e(TAG, PreferenceManager.getInstance().getfriendsMsgOnly() + "");
        if (PreferenceManager.getInstance().getfriendsMsgOnly() == 1) {
            switch_only_get_friends_messages.openSwitch();
        } else {
            switch_only_get_friends_messages.closeSwitch();
        }
        if (PreferenceManager.getInstance().getDisplayMessageDetail() == 1) {
            switch_display_message_detail.openSwitch();
        } else {
            switch_display_message_detail.closeSwitch();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_switch_notification:
                if (notifiSwitch.isSwitchOpen()) {
                    notifiSwitch.closeSwitch();
                    rl_switch_sound.setVisibility(View.GONE);
                    rl_switch_vibrate.setVisibility(View.GONE);
                    textview1.setVisibility(View.GONE);
                    textview2.setVisibility(View.GONE);
                    settingsModel.setSettingMsgNotification(false);
                } else {
                    notifiSwitch.openSwitch();
                    rl_switch_sound.setVisibility(View.VISIBLE);
                    rl_switch_vibrate.setVisibility(View.VISIBLE);
                    textview1.setVisibility(View.VISIBLE);
                    textview2.setVisibility(View.VISIBLE);
                    settingsModel.setSettingMsgNotification(true);
                }
                break;
            case R.id.rl_switch_sound:
                if (soundSwitch.isSwitchOpen()) {
                    soundSwitch.closeSwitch();
                    settingsModel.setSettingMsgSound(false);
                } else {
                    soundSwitch.openSwitch();
                    settingsModel.setSettingMsgSound(true);
                }
                break;
            case R.id.rl_switch_vibrate:
                if (vibrateSwitch.isSwitchOpen()) {
                    vibrateSwitch.closeSwitch();
                    settingsModel.setSettingMsgVibrate(false);
                } else {
                    vibrateSwitch.openSwitch();
                    settingsModel.setSettingMsgVibrate(true);
                }
                break;
            case R.id.rl_switch_speaker:
                if (speakerSwitch.isSwitchOpen()) {
                    speakerSwitch.closeSwitch();
                    settingsModel.setSettingMsgSpeaker(false);
                } else {
                    speakerSwitch.openSwitch();
                    settingsModel.setSettingMsgVibrate(true);
                }
                break;
            case R.id.rl_switch_chatroom_owner_leave:
                if (ownerLeaveSwitch.isSwitchOpen()) {
                    ownerLeaveSwitch.closeSwitch();
                    settingsModel.allowChatroomOwnerLeave(false);
                    chatOptions.allowChatroomOwnerLeave(false);
                } else {
                    ownerLeaveSwitch.openSwitch();
                    settingsModel.allowChatroomOwnerLeave(true);
                    chatOptions.allowChatroomOwnerLeave(true);
                }
                break;
            case R.id.rl_switch_delete_msg_when_exit_group:
                if (switch_delete_msg_when_exit_group.isSwitchOpen()) {
                    switch_delete_msg_when_exit_group.closeSwitch();
                    settingsModel.setDeleteMessagesAsExitGroup(false);
                    chatOptions.setDeleteMessagesAsExitGroup(false);
                } else {
                    switch_delete_msg_when_exit_group.openSwitch();
                    settingsModel.setDeleteMessagesAsExitGroup(true);
                    chatOptions.setDeleteMessagesAsExitGroup(true);
                }
                break;
            case R.id.rl_switch_auto_accept_group_invitation:
                if (switch_auto_accept_group_invitation.isSwitchOpen()) {
                    switch_auto_accept_group_invitation.closeSwitch();
                    settingsModel.setAutoAcceptGroupInvitation(false);
                    chatOptions.setAutoAcceptGroupInvitation(false);
                } else {
                    switch_auto_accept_group_invitation.openSwitch();
                    settingsModel.setAutoAcceptGroupInvitation(true);
                    chatOptions.setAutoAcceptGroupInvitation(true);
                }
                break;
            //新添加，由于有的职工在这个APP里面乱骂领导，所以领导提出了这个需求“只接受好友消息”
            case R.id.rl_switch_only_get_friends_messages:
                pd.show();
                if (switch_only_get_friends_messages.isSwitchOpen()) {
                    switch_only_get_friends_messages.closeSwitch();
                    only_get_friends_messages(0);
                    PreferenceManager.getInstance().setfriendsMsgOnly(0);
                } else {
                    switch_only_get_friends_messages.openSwitch();
                    only_get_friends_messages(1);
                    PreferenceManager.getInstance().setfriendsMsgOnly(1);
                }
                break;
            //新添加，是否在通知栏显示内容详情的开关。
            //0是关，1是开
            case R.id.rl_switch_display_message_detail:
                if (switch_display_message_detail.isSwitchOpen()) {
                    switch_display_message_detail.closeSwitch();
                    PreferenceManager.getInstance().setDisplayMessageDetail(0);
                } else {
                    switch_display_message_detail.openSwitch();
                    PreferenceManager.getInstance().setDisplayMessageDetail(1);
                }
                break;
            case R.id.rl_switch_adaptive_video_encode:
                EMLog.d("switch", "" + !switch_adaptive_video_encode.isSwitchOpen());
                if (switch_adaptive_video_encode.isSwitchOpen()) {
                    switch_adaptive_video_encode.closeSwitch();
                    settingsModel.setAdaptiveVideoEncode(false);
                    EMClient.getInstance().callManager().getVideoCallHelper()
                            .setAdaptiveVideoFlag(false);
                } else {
                    switch_adaptive_video_encode.openSwitch();
                    settingsModel.setAdaptiveVideoEncode(true);
                    EMClient.getInstance().callManager().getVideoCallHelper()
                            .setAdaptiveVideoFlag(true);
                }
                break;
            case R.id.ll_black_list:
                startActivity(new Intent(this, BlacklistActivity.class));
                break;
            case R.id.ll_diagnose:
                startActivity(new Intent(this, DiagnoseActivity.class));
                break;
            case R.id.ll_set_push_nick:
                startActivity(new Intent(this, OfflinePushNickActivity.class));
                break;
            case R.id.ll_user_profile:
                startActivity(new Intent(this, UserProfileActivity.class).putExtra(
                        "setting", true).putExtra("username",
                        EMClient.getInstance().getCurrentUser()));
                break;
            default:
                break;
        }

    }

    private void only_get_friends_messages(int receiveStatus) {
        JSONObject change_json = new JSONObject();
        try {
            change_json.put("userId", PreferenceManager.getInstance().getCurrentUserId());
            change_json.put("receiveStatus", receiveStatus);
            CommonUtils.setCommonJson(MessageSettingActivity.this, change_json, PreferenceManager.getInstance().getCurrentUserFlowSId());
            Log.e(TAG, change_json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils.postString().url(Constant.GETFRIENDSMSGONLY_URL)
                .content(change_json.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        pd.dismiss();
                        showToastShort("设置失败");
                    }

                    @Override
                    public void onResponse(String s) {
                        Log.e(TAG, s);
                        pd.dismiss();
                        try {
                            JSONObject result = new JSONObject(s);
                            if (result.getInt("code") == 1) {
                                showToastShort("设置成功");
                            } else {
                                showToastShort("设置失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void back(View view) {
        finish();
        if (android.os.Build.VERSION.SDK_INT > 5) {
            overridePendingTransition(
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }
}
