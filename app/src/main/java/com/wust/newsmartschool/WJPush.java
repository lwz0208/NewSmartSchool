package com.wust.newsmartschool;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wust.newsmartschool.domain.UnreadMsgNum;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.BadgeUtil;

public class WJPush {
    //为了复用最开始用的WPush，所以就用这一套。发挥一下以前代码的余热。
    public static void SentUnreadNum(Context mCtx) {
        Intent intent = new Intent();
        intent.setAction(Constant.BROAD_UNREADMSG);
        try {
            if (DemoApplication.getInstance().mCache.getAsObject(Constant.MY_KEY_UNREADMSGNUM) != null) {
                intent.putExtra(Constant.BROAD_UNREADMSG, (UnreadMsgNum.DataBean) DemoApplication.getInstance().mCache.getAsObject(Constant.MY_KEY_UNREADMSGNUM));
                Log.i("args_哈哈_unreadMsg", ((UnreadMsgNum.DataBean) DemoApplication.getInstance().mCache.getAsObject(Constant.MY_KEY_UNREADMSGNUM)).getTask() + "");
                BadgeUtil.setBadgeCount(mCtx, ((UnreadMsgNum.DataBean) DemoApplication.getInstance().mCache.getAsObject(Constant.MY_KEY_UNREADMSGNUM)).getNum());
            }
            mCtx.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
