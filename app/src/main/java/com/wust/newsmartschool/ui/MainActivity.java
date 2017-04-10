/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p/>
 * Author:Erick-ShangGao shixiong  Created by 2016年6月22日
 * Say:I know laboratory's project inherit&maintain is a big problem,
 * so I the left my QQ number(158956573),laboratory's brothers&sisters
 * if U maintaining the project and have problem can
 * contact me,I must be enthusiastic&patient to help you
 * solve the problem(if I remember o(∩_∩)o 哈哈)
 */
package com.wust.newsmartschool.ui;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.model.UpdateParser;
import org.lzh.framework.updatepluginlib.strategy.UpdateStrategy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wust.newsmartschool.DemoApplication;
import com.wust.newsmartschool.DemoHelper;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.db.InviteMessgeDao;
import com.wust.newsmartschool.db.UserDao;
import com.wust.newsmartschool.domain.InviteMessage;
import com.wust.newsmartschool.domain.UnreadMsgNum;
import com.wust.newsmartschool.domain.UpdataEntity;
import com.wust.newsmartschool.domain.UserInfoEntity;
import com.wust.newsmartschool.fragments.ContactListFragment;
import com.wust.newsmartschool.fragments.ConversationListFragment;
import com.wust.newsmartschool.fragments.MainIndexFragment;
import com.wust.newsmartschool.fragments.SettingsFragment;
import com.wust.newsmartschool.fragments.UsualContactFragment;
import com.wust.newsmartschool.fragments.WorkFragment;
import com.wust.newsmartschool.utils.appUseUtils;
import com.wust.easeui.utils.BadgeUtil;
import com.wust.easeui.utils.CommonUtils;
import com.wust.easeui.utils.PreferenceManager;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMMessage;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.MediaType;

public class MainActivity extends BaseActivity {

    protected static final String TAG = "MainActivity_Debugs";
    // 未读消息textview
    private TextView unreadLabel;
    // 未读通讯录textview
    private TextView unreadAddressLable;
    private TextView contactfragment_unread_address_number;
    private TextView unread_work_notice_number;
    private TextView unread_work_jflow_number;
    private TextView unread_work_duty_number;
    private TextView unread_work_meet_number;
    // 工作的未读点点
    private TextView unread_work_number;

    //退出时候用的全局变量
    private Button[] mTabs;
    private ConversationListFragment conversationListFragment;
    private UsualContactFragment usualcontactFragment;
    private MainIndexFragment mainIndexFragment;
    //    private NewsFragment dingFragment;
    private SettingsFragment settingsFragment;
    private WorkFragment workFragment;
    private Fragment[] fragments;
    private int index;
    // 当前fragment的index
    private int currentTabIndex;
    // 账号在别处登录
    public boolean isConflict = false;
    // 账号被移除
    private boolean isCurrentAccountRemoved = false;

    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    private boolean isConflictDialogShow;
    private boolean isAccountRemovedDialogShow;
    private BroadcastReceiver internalDebugReceiver;
    private ContactListFragment contactListFragment;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;

    // 全局广播监听者
    MyBroadcastReceiver receiver;

    public static boolean isForeground = false;
    UpdataEntity updataEntity;

    /**
     * 检查当前用户是否被删除
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注册广播
        IntentFilter filter_dynamic = new IntentFilter();
        receiver = new MyBroadcastReceiver();
        filter_dynamic.addAction(Constant.BROAD_UNREADMSG);
        registerReceiver(receiver, filter_dynamic);
        updataEntity = new UpdataEntity();
        if (savedInstanceState != null
                && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED,
                false)) {
            // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            DemoHelper.getInstance().logout(false, null);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (savedInstanceState != null
                && savedInstanceState.getBoolean("isConflict", false)) {
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        setContentView(R.layout.em_activity_main);
        initView();
        if (getIntent().getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)
                && !isConflictDialogShow) {
            //退出登录后就重置APP图标上的未读消息条数
            BadgeUtil.resetBadgeCount(MainActivity.this);
            //退出后就当没用过
            PreferenceManager.getInstance().setH5ISFIRSTUSE(true);
            //被挤掉以后也要停止推送
            showConflictDialog();
            JPushInterface.stopPush(MainActivity.this);
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
                && !isAccountRemovedDialogShow) {
            //退出登录后就重置APP图标上的未读消息条数
            BadgeUtil.resetBadgeCount(MainActivity.this);
            //退出后就当没用过
            PreferenceManager.getInstance().setH5ISFIRSTUSE(true);
            //被移除以后也要停止推送
            showAccountRemovedDialog();
            JPushInterface.stopPush(MainActivity.this);
        }

        inviteMessgeDao = new InviteMessgeDao(this);
        userDao = new UserDao(this);
        conversationListFragment = new ConversationListFragment();
        contactListFragment = new ContactListFragment();
        usualcontactFragment = new UsualContactFragment();
        workFragment = new WorkFragment();
//        dingFragment = new NewsFragment();
        settingsFragment = new SettingsFragment();
        mainIndexFragment = new MainIndexFragment();

        fragments = new Fragment[]{conversationListFragment, workFragment, mainIndexFragment,
                usualcontactFragment, settingsFragment};
        // 添加显示第一个fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, conversationListFragment)
                .add(R.id.fragment_container, usualcontactFragment)
                .add(R.id.fragment_container, settingsFragment)
                .add(R.id.fragment_container, workFragment)
                .add(R.id.fragment_container, mainIndexFragment).hide(settingsFragment)
                .hide(workFragment).hide(usualcontactFragment).hide(conversationListFragment)
                .show(mainIndexFragment).commit();

        // 注册local广播接收者，用于接收demohelper中发出的群组联系人的变动通知
        registerBroadcastReceiver();

        EMClient.getInstance().contactManager()
                .setContactListener(new MyContactListener());
        // 内部测试方法，请忽略
        registerInternalDebugReceiver();

        EMLog.d(TAG, "width:" + getScreenWidth(this) + "  height:"
                + getScreenHeight(this));
        // 异步更新nickname，此操作比较费事，放哪儿都卡。。。
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                // 更新自己的用户昵称
                UserInfoEntity userInfoEntity = (UserInfoEntity) DemoApplication
                        .getInstance().mCache
                        .getAsObject(Constant.MY_KEY_USERINFO);
                if (userInfoEntity != null) {
                    DemoHelper
                            .getInstance()
                            .getUserProfileManager()
                            .updateCurrentUserNickName(
                                    userInfoEntity.getData().getName());
                }
            }
        }).start();
        //检查更新的代码
        try {
            Updata();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //请求服务器比对最新版本号
    public void Updata() throws JSONException, NullPointerException {
        JSONObject jj = new JSONObject();
        jj.put("type", "Android");
        CommonUtils.setCommonJson(MainActivity.this, jj, PreferenceManager.getInstance().getCurrentUserFlowSId());
        OkHttpUtils.postString().url(Constant.GETUPDATA_URL)
                .content(jj.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e(TAG, call.toString() + "-" + e.toString());
                    }

                    @Override
                    public void onResponse(String s) {
                        Log.e(TAG, s.toString());
                        try {
                            updataEntity = new Gson().fromJson(s, UpdataEntity.class);
//                        Log.e(TAG, updataEntity.getData().getApkUrl().toString());
                            UpdateBuilder.create().url("http://www.qq.com/").jsonParser(new UpdateParser() {
                                @Override
                                public Update parse(String response) {
                                    Log.e(TAG, response);
                                    Update update = new Update(response);
                                    update.setUpdateTime(System.currentTimeMillis());
                                    update.setUpdateUrl(updataEntity.getData().getApkUrl());
//                                update.setUpdateUrl("http://61.136.163.26/qiniu-app-cdn.pgyer.com/33de7c410a656bbafc1741661312f39a.apk?e=1474806331&attname=MOA.apk&token=6fYeQ7_TVB5L0QSzosNFfw2HU8eJhAirMF5VxV9G:DvA8eMJ6nhziif15E2I5UUvscB4=&wsiphost=local");
                                    update.setVersionCode(updataEntity.getData().getVersionCode());
                                    update.setVersionName(updataEntity.getData().getVersionName());
                                    update.setUpdateContent(updataEntity.getData().getRemark());
                                    update.setForced(false); // 此apk包是否为强制更新
                                    update.setIgnore(false);// 是否显示忽略此次版本更新
                                    return update;
                                }
                            }).strategy(new UpdateStrategy() {
                                @Override
                                public boolean isShowUpdateDialog(Update update) {
                                    return true;// 有新更新直接展示
                                }

                                @Override
                                public boolean isAutoInstall() {
                                    return true;
                                }

                                @Override
                                public boolean isShowDownloadDialog() {
                                    return true;// 展示下载进度
                                }
                            })
                                    .check(MainActivity.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    // for receive customer msg from jpush server
//    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

//    public void registerMessageReceiver() {
//        mMessageReceiver = new MessageReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
//        filter.addAction(MESSAGE_RECEIVED_ACTION);
//        registerReceiver(mMessageReceiver, filter);
//    }

//    public class MessageReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
//                String messge = intent.getStringExtra(KEY_MESSAGE);
//                String extras = intent.getStringExtra(KEY_EXTRAS);
//                StringBuilder showMsg = new StringBuilder();
//                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
//                if (!ExampleUtil.isEmpty(extras)) {
//                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
//                }
//
//            }
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();
        contactfragment_unread_address_number = (TextView) usualcontactFragment.getView()
                .findViewById(
                        R.id.contactfragment_unread_address_number);
        unread_work_notice_number = (TextView) workFragment.getView()
                .findViewById(R.id.unread_work_notice_number);
        unread_work_jflow_number = (TextView) workFragment.getView()
                .findViewById(R.id.unread_work_jflow_number);
        unread_work_duty_number = (TextView) workFragment.getView()
                .findViewById(R.id.unread_work_duty_number);
        unread_work_meet_number = (TextView) workFragment.getView()
                .findViewById(R.id.unread_work_meet_number);
        //获取自己已读未读数字的信息并且缓存在本地并且推送
        appUseUtils.GetMyUnreadMumAndJPush(MainActivity.this);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.BROAD_UNREADMSG)) {
//                String msg = intent.getStringExtra(Constant.BROAD_UNREADMSG)
//                        .toString();
                try {
                    UnreadMsgNum.DataBean getIntent = (UnreadMsgNum.DataBean) intent.getSerializableExtra(Constant.BROAD_UNREADMSG);

//                    JSONObject unreadList = new JSONObject(msg);
                    // 通知count;
                    if (getIntent.getInform() > 0) {
//                        Log.e("unread_work_notice",
//                                getIntent.getMsg_working_notice() + "");
                        unread_work_notice_number.setVisibility(View.VISIBLE);
                        unread_work_notice_number.setText(getIntent.getInform() + "");
                    } else {
                        unread_work_notice_number.setVisibility(View.INVISIBLE);
                    }
                    // 流程count;
                    if (getIntent.getJflow() > 0) {
//                    Toast.makeText(MainActivity.this, "你收到一条流程信息",
//                            Toast.LENGTH_SHORT).show();
                        unread_work_jflow_number.setVisibility(View.VISIBLE);
                        unread_work_jflow_number.setText(getIntent.getJflow() + "");
                    } else {
                        unread_work_jflow_number.setVisibility(View.INVISIBLE);
                    }
                    // 任务count
                    Log.e("unread_work_duty_number",
                            getIntent.getJflow() + "");
                    if (getIntent.getTask() > 0) {
//                    Toast.makeText(MainActivity.this, "你收到一条任务信息",
//                            Toast.LENGTH_SHORT).show();
                        unread_work_duty_number.setVisibility(View.VISIBLE);
                        unread_work_duty_number.setText(getIntent.getTask() + "");
                    } else {
                        unread_work_duty_number.setVisibility(View.INVISIBLE);
                    }
                    // 会议count
                    if (getIntent.getMeeting() > 0) {
//                    Toast.makeText(MainActivity.this, "你收到一条会议信息",
//                            Toast.LENGTH_SHORT).show();
                        Log.e("unread_work_meet_number",
                                getIntent.getMeeting() + "");
                        unread_work_meet_number.setVisibility(View.VISIBLE);
                        unread_work_meet_number.setText(getIntent.getMeeting() + "");
                    } else {
                        unread_work_meet_number.setVisibility(View.INVISIBLE);
                    }
                    if (getIntent.getNum() > 0) {
                        unread_work_number.setVisibility(View.VISIBLE);
                        unread_work_number.setText(getIntent.getNum() + "");
                    } else {
                        unread_work_number.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    // 获取屏幕的高度
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
        unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);
        unread_work_number = (TextView) findViewById(R.id.unread_work_number);
        unreadLabel.bringToFront();
        unreadAddressLable.bringToFront();
        unread_work_number.bringToFront();
        mTabs = new Button[5];
        mTabs[0] = (Button) findViewById(R.id.btn_conversation);
        mTabs[1] = (Button) findViewById(R.id.btn_work_list);
        mTabs[2] = (Button) findViewById(R.id.btn_setting);
        mTabs[3] = (Button) findViewById(R.id.btn_address_list);
        mTabs[4] = (Button) findViewById(R.id.btn_ding_list);


        // 把第一个tab设为选中状态
        mTabs[2].setSelected(true);
        currentTabIndex = 2;
    }

    /**
     * button点击事件
     *
     * @param view
     */
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_conversation:
                index = 0;
                break;
            case R.id.btn_ding_list:
                index = 4;
                break;
            case R.id.btn_work_list:
                index = 1;
                break;
            case R.id.btn_address_list:
                index = 3;
                break;
            case R.id.btn_setting:
                index = 2;
                break;
        }
        //TMD这儿也会闪退啊，我真是醉了。TMD先给他try起来，先给劳资老实点，有时间再来收拾。
        try {
            if (currentTabIndex != index) {
                FragmentTransaction trx = getSupportFragmentManager()
                        .beginTransaction();
                trx.hide(fragments[currentTabIndex]);
                if (!fragments[index].isAdded()) {
                    trx.add(R.id.fragment_container, fragments[index]);
                }
                trx.show(fragments[index]).commit();

            }
            mTabs[currentTabIndex].setSelected(false);
            // 把当前tab设为选中状态
            mTabs[index].setSelected(true);
            currentTabIndex = index;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //当前APP在回话列表而不在聊天页面的时候的监听新消息的函数
    EMMessageListener messageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // 提示新消息
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
//                Log.e(TAG, message.getBody().toString());
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // 刷新bottom bar消息未读数
                updateUnreadLabel();
                if (currentTabIndex == 0) {
                    // 当前页面如果为聊天历史页面，刷新此页面
                    if (conversationListFragment != null) {
                        conversationListFragment.refresh();
                    }
                }
            }
        });
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                updateUnreadAddressLable();
                // 更新联系人页面上面的“好友列表”红豆豆
                // usualcontactFragment.
                if (currentTabIndex == 0) {
                    // 当前页面如果为聊天历史页面，刷新此页面
                    if (conversationListFragment != null) {
                        conversationListFragment.refresh();
                    }
                } else if (currentTabIndex == 1) {
                    if (contactListFragment != null) {
                        contactListFragment.refresh();
                    }
                }
                String action = intent.getAction();
                if (action.equals(Constant.ACTION_GROUP_CHANAGED)) {
                    if (EaseCommonUtils.getTopActivity(MainActivity.this)
                            .equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
        }

        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatActivity.activityInstance != null
                            && ChatActivity.activityInstance.toChatUsername != null
                            && username
                            .equals(ChatActivity.activityInstance.toChatUsername)) {
                        String st10 = getResources().getString(
                                R.string.have_you_removed);
                        Toast.makeText(
                                MainActivity.this,
                                ChatActivity.activityInstance
                                        .getToChatUsername() + st10, Toast.LENGTH_SHORT).show();
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
        }

        @Override
        public void onContactInvited(String username, String reason) {
        }

        @Override
        public void onContactAgreed(String username) {
        }

        @Override
        public void onContactRefused(String username) {
        }
    }

    private void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }
        unregisterBroadcastReceiver();

        try {
            unregisterReceiver(internalDebugReceiver);
            unregisterReceiver(receiver);
        } catch (Exception e) {
        }

    }

    /**
     * 刷新未读消息数
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            unreadLabel.setText(String.valueOf(count));
            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 刷新申请与通知消息数
     */
    public void updateUnreadAddressLable() {
        runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();
                if (count > 0) {
                    // unreadAddressLable.setText(String.valueOf(count));
                    unreadAddressLable.setVisibility(View.VISIBLE);
                    contactfragment_unread_address_number
                            .setVisibility(View.VISIBLE);
                } else {
                    unreadAddressLable.setVisibility(View.INVISIBLE);
                    contactfragment_unread_address_number
                            .setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    /**
     * 获取未读申请与通知消息
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager()
                .getUnreadMsgsCount();
        for (EMConversation conversation : EMClient.getInstance().chatManager()
                .getAllConversations().values()) {
            if (conversation.getType() == EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount
                        + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }

    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;

    /**
     * 保存提示新消息
     *
     * @param msg
     */
    private void notifyNewIviteMessage(InviteMessage msg) {
        saveInviteMsg(msg);
        // 提示有新消息
        DemoHelper.getInstance().getNotifier().viberateAndPlayTone(null);

        // 刷新bottom bar消息未读数
        updateUnreadAddressLable();
        // 刷新好友页面ui
        if (currentTabIndex == 1)
            contactListFragment.refresh();
    }

    /**
     * 保存邀请等msg
     *
     * @param msg
     */
    private void saveInviteMsg(InviteMessage msg) {
        // 保存msg
        inviteMessgeDao.saveMessage(msg);
        // 保存未读数，这里没有精确计算
        inviteMessgeDao.saveUnreadMessageCount(1);
    }


    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();
            updateUnreadAddressLable();
        }

        // unregister this event listener when this activity enters the
        // background
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);

        EMClient.getInstance().chatManager()
                .addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        isForeground = false;
        EMClient.getInstance().chatManager()
                .removeMessageListener(messageListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(
                            MainActivity.this);
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage(R.string.connect_conflict);
                conflictBuilder.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                conflictBuilder = null;
                                finish();
                                Intent intent = new Intent(MainActivity.this,
                                        LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG,
                        "---------color conflictBuilder error" + e.getMessage());
            }

        }

    }

    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st5 = getResources().getString(R.string.Remove_the_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(
                            MainActivity.this);
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage(R.string.em_user_remove);
                accountRemovedBuilder.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                accountRemovedBuilder = null;
                                finish();
                                startActivity(new Intent(MainActivity.this,
                                        LoginActivity.class));
                            }
                        });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
                EMLog.e(TAG,
                        "---------color userRemovedBuilder error"
                                + e.getMessage());
            }

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)
                && !isConflictDialogShow) {
            showConflictDialog();
            JPushInterface.stopPush(MainActivity.this);
        } else if (intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
                && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
            JPushInterface.stopPush(MainActivity.this);
        }
    }

    /**
     * 内部测试代码，开发者请忽略
     */
    private void registerInternalDebugReceiver() {
        internalDebugReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                DemoHelper.getInstance().logout(false, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                // 重新显示登陆页面
                                finish();
                                startActivity(new Intent(MainActivity.this,
                                        LoginActivity.class));

                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String message) {
                    }
                });
            }
        };
        IntentFilter filter = new IntentFilter(getPackageName()
                + ".em_internal_debug");
        registerReceiver(internalDebugReceiver, filter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }


}