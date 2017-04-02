package com.ding.chat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ding.chat.DemoApplication;
import com.ding.chat.DemoHelper;
import com.ding.chat.WJPush;
import com.ding.chat.domain.UnreadMsgNum;
import com.ding.chat.ui.LoginActivity;
import com.ding.chat.ui.MainActivity;
import com.ding.chat.ui.MeetingActivity;
import com.ding.chat.ui.WorkFragApplyActivity;
import com.ding.chat.ui.WorkFragTaskActivity;
import com.ding.chat.ui.TaskDetailActivity;
import com.ding.chat.ui.WorkFlowNeedMeActivity;
import com.ding.chat.ui.WorkFragNoticeActivity;
import com.ding.chat.utils.appUseUtils;
import com.ding.easeui.Constant;
import com.ding.easeui.utils.CommonUtils;
import com.ding.easeui.utils.PreferenceManager;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush_Log";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
                + ", extras: " + printBundle(bundle));
        Log.d(TAG, bundle.toString());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle
                    .getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            // send the Registration Id to your server...
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
            Log.d(TAG,
                    "[MyReceiver] 接收到推送下来的自定义消息: "
                            + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            processCustomMessage(context, bundle);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (extraJson.getString("type").equals("jflow_disposed")) {
                        //用于数字的更新
                        appUseUtils.GetMyUnreadMumAndJPush(context);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                .getAction())) {
            //在标题栏显示的通知一般都会经过这里。
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle
                    .getInt(JPushInterface.EXTRA_NOTIFICATION_ID);

            try {
                JSONObject json = new JSONObject(
                        bundle.getString(JPushInterface.EXTRA_EXTRA));
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的json_EXTRA_EXTRA: " + json.toString());
                if (json.getString("type").contains("jflow")) {
                    //+1操作
//                    appUseUtils.reduceUnreadNum(context, "msg_working_flow", 1);
//                    appUseUtils.GetMyUnreadMumAndJPush(context);
                    //主要是用于给首页更新信息。
                    appUseUtils.SentJpushData(context, json);
                } else if (json.getString("type").equals("inform")) {
                    //+1操作
//                    appUseUtils.reduceUnreadNum(context, "unread_work_notice", 1);
//                    appUseUtils.GetMyUnreadMumAndJPush(context);
                    //主要是用于给首页更新信息。
                    appUseUtils.SentJpushData(context, json);
                } else if (json.getString("type").equals("meeting")) {
                    //+1操作
//                    appUseUtils.reduceUnreadNum(context, "msg_working_meeting", 1);
//                    appUseUtils.GetMyUnreadMumAndJPush(context);
                    //主要是用于给首页更新信息。
                    appUseUtils.SentJpushData(context, json);
                } else if (json.getString("type").equals("task")) {
//                    appUseUtils.GetMyUnreadMumAndJPush(context);
                    //主要是用于给首页更新信息。
                    appUseUtils.SentJpushData(context, json);
                }
                //用于数字的更新
                appUseUtils.GetMyUnreadMumAndJPush(context);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
                .getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            // 打开自定义的Activity
            Log.e(TAG, bundle.toString());
            try {
                JSONObject json = new JSONObject(
                        bundle.getString(JPushInterface.EXTRA_EXTRA));
                if (!DemoHelper.getInstance().isLoggedIn()) {
                    Intent i = new Intent(context, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                } else if (json.getString("type").equals("jflow")) {
                    Intent i = new Intent(context, WorkFragApplyActivity.class).putExtra("webid_detail", json.getString("id"));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                } else if (json.getString("type").equals("jflow_refused")) {
                    Intent i = new Intent(context, WorkFragApplyActivity.class).putExtra("webid_detail", json.getString("id"));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                } else if (json.getString("type").equals("jflow_success")) {
                    Intent i = new Intent(context, WorkFragApplyActivity.class).putExtra("webid_detail", json.getString("id"));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                } else if (json.getString("type").equals("inform")) {
                    Intent i = new Intent(context, WorkFragNoticeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                } else if (json.getString("type").equals("meeting")) {
                    Intent i = new Intent(context, MeetingActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                } else if (json.getString("type").equals("task_modify")) {
                    String content = json.getString("content");
                    String[] content_split = content.split(",");
                    Intent i = new Intent(context, TaskDetailActivity.class).putExtra("taskID", content_split[1]);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                } else if (json.getString("type").equals("task")) {
                    Intent i = new Intent(context, WorkFragTaskActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                } else if (json.getString("type").equals("comment")) {
                    String content = json.getString("content");
                    String[] content_split = content.split(",");
                    Intent i;
                    switch (content_split[0]) {
                        case "task":
                            i = new Intent(context, TaskDetailActivity.class).putExtra("taskID", content_split[1]);
                            break;
                        case "inform":
                            i = new Intent(context, WorkFragNoticeActivity.class);
                            break;
                        default:
                            i = new Intent(context, MainActivity.class);
                            break;
                    }
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                } else {
                    Intent i = new Intent(context, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
                .getAction())) {
            Log.d(TAG,
                    "[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
                            + bundle.getString(JPushInterface.EXTRA_EXTRA));
            // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
            // 打开一个网页等..
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
                .getAction())) {
            boolean connected = intent.getBooleanExtra(
                    JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction()
                    + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

//    private void GetMyUnreadMum(final Context mCt) {
//        JSONObject userIdJson = new JSONObject();
//        try {
//            userIdJson.put("userId", PreferenceManager.getInstance().getCurrentUserId());
//            CommonUtils.setCommonJson(mCt, userIdJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
//        } catch (JSONException e1) {
//            e1.printStackTrace();
//        }
//
////        Log.i("jObject_Login", userIdJson.toString());
//
//        OkHttpUtils.postString().url(Constant.GETUNREADNUM_URL)
//                .content(userIdJson.toString())
//                .mediaType(MediaType.parse("application/json")).build()
//                .execute(new StringCallback() {
//
//                    @Override
//                    public void onResponse(String arg0) {
//                        JSONObject jObject;
//                        try {
//                            jObject = new JSONObject(arg0);
//                            if (jObject.getInt("code") == 1) {
////                                Log.e("GetMyUnreadMum", arg0.toString());
//                                UnreadMsgNum unreadMsgNum = new Gson().fromJson(arg0, UnreadMsgNum.class);
//                                PreferenceManager.getInstance().setNoticeUnreadNum(unreadMsgNum.getData().getMsg_working_notice());
//                                PreferenceManager.getInstance().setTaskUnreadNum(unreadMsgNum.getData().getMsg_working_task());
//                                PreferenceManager.getInstance().setFlowUnreadNum(unreadMsgNum.getData().getMsg_working_flow());
//                                PreferenceManager.getInstance().setMeetingUnreadNum(unreadMsgNum.getData().getMsg_working_meeting());
//                                PreferenceManager.getInstance().setAmountUnreadNum(unreadMsgNum.getData().getMsg_working_amount());
//                                //最后来一个大存
////                                DemoApplication.getInstance().mCache.put(Constant.MY_KEY_UNREADMSGNUM, jObject.getJSONObject("data"));
//                                DemoApplication.getInstance().mCache.put(Constant.MY_KEY_UNREADMSGNUM, unreadMsgNum.getData());
//                                WJPush.SentUnreadNum(mCt);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Call arg0, Exception arg1) {
////                        Log.e("GetMyUnreadMum", arg0.toString());
//                    }
//                });
//    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(
                            bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" + myKey + " - "
                                + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    // send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        if (MainActivity.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (null != extraJson && extraJson.length() > 0) {
                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
                    }
                } catch (JSONException e) {

                }

            }
            context.sendBroadcast(msgIntent);
        }
    }
}
