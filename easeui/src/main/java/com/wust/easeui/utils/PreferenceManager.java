/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wust.easeui.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    /**
     * 保存Preference的name
     */
    public static final String PREFERENCE_NAME = "saveInfo";
    private static SharedPreferences mSharedPreferences;
    private static PreferenceManager mPreferencemManager;
    private static SharedPreferences.Editor editor;

    private String SHARED_KEY_SETTING_NOTIFICATION = "shared_key_setting_notification";
    private String SHARED_KEY_SETTING_SOUND = "shared_key_setting_sound";
    private String SHARED_KEY_SETTING_VIBRATE = "shared_key_setting_vibrate";
    private String SHARED_KEY_SETTING_SPEAKER = "shared_key_setting_speaker";

    private static String SHARED_KEY_SETTING_CHATROOM_OWNER_LEAVE = "shared_key_setting_chatroom_owner_leave";
    private static String SHARED_KEY_SETTING_DELETE_MESSAGES_WHEN_EXIT_GROUP = "shared_key_setting_delete_messages_when_exit_group";
    private static String SHARED_KEY_SETTING_AUTO_ACCEPT_GROUP_INVITATION = "shared_key_setting_auto_accept_group_invitation";
    private static String SHARED_KEY_SETTING_ADAPTIVE_VIDEO_ENCODE = "shared_key_setting_adaptive_video_encode";

    private static String SHARED_KEY_SETTING_GROUPS_SYNCED = "SHARED_KEY_SETTING_GROUPS_SYNCED";
    private static String SHARED_KEY_SETTING_CONTACT_SYNCED = "SHARED_KEY_SETTING_CONTACT_SYNCED";
    private static String SHARED_KEY_SETTING_BALCKLIST_SYNCED = "SHARED_KEY_SETTING_BALCKLIST_SYNCED";

    private static String SHARED_KEY_CURRENTUSER_USERNAME = "SHARED_KEY_CURRENTUSER_USERNAME";
    private static String MY_KEY_SHARED_KEY_CURRENTUSER_USERID = "MY_KEY_SHARED_KEY_CURRENTUSER_USERID";
    private static String MY_KEY_SHARED_KEY_CURRENTUSER_USERREALNAME = "MY_KEY_SHARED_KEY_CURRENTUSER_USERREALNAME";
    private static String SHARED_KEY_CURRENTUSER_NICK = "SHARED_KEY_CURRENTUSER_NICK";
    private static String SHARED_KEY_CURRENTUSER_AVATAR = "SHARED_KEY_CURRENTUSER_AVATAR";
    private static String MY_KEY_SHARED_KEY_CURRENTUSER_FLOWSID = "MY_KEY_SHARED_KEY_CURRENTUSER_FLOWSID";
    private static String MY_KEY_SHARED_KEY_CURRENTUSER_DEPTMENTID = "MY_KEY_SHARED_KEY_CURRENTUSER_DEPTMENTID";
    private static String MY_KEY_SHARED_KEY_CURRENTUSER_DEPTMENTNAME = "MY_KEY_SHARED_KEY_CURRENTUSER_DEPTMENTNAME";
    private static String MY_KEY_SET_FRIENDSMSG_ONLY = "MY_KEY_SET_FRIENDSMSG_ONLY";
    private static String DISPLAYMESSAGEDETAIL = "DISPLAYMESSAGEDETAIL";
    private static String CurrentUserClassName = "CurrentUserClassName";
    private static String CurrentUserClassId = "CurrentUserClassId";
    private static String ISFIRSTUSE = "ISFIRSTUSE";
    private static String studentType = "studentType";
    private static String year = "year";

    private PreferenceManager(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public static synchronized void init(Context cxt) {
        if (mPreferencemManager == null) {
            mPreferencemManager = new PreferenceManager(cxt);
        }
    }

    /**
     * 单例模式，获取instance实例
     */
    public synchronized static PreferenceManager getInstance() {
        if (mPreferencemManager == null) {
            throw new RuntimeException("please init first!");
        }

        return mPreferencemManager;
    }

    public void setSettingMsgNotification(boolean paramBoolean) {
        editor.putBoolean(SHARED_KEY_SETTING_NOTIFICATION, paramBoolean);
        editor.commit();
    }

    public boolean getSettingMsgNotification() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_NOTIFICATION,
                true);
    }

    public void setSettingMsgSound(boolean paramBoolean) {
        editor.putBoolean(SHARED_KEY_SETTING_SOUND, paramBoolean);
        editor.commit();
    }

    public boolean getSettingMsgSound() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_SOUND, true);
    }

    public void setSettingMsgVibrate(boolean paramBoolean) {
        editor.putBoolean(SHARED_KEY_SETTING_VIBRATE, paramBoolean);
        editor.commit();
    }

    public boolean getSettingMsgVibrate() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_VIBRATE, true);
    }

    public void setSettingMsgSpeaker(boolean paramBoolean) {
        editor.putBoolean(SHARED_KEY_SETTING_SPEAKER, paramBoolean);
        editor.commit();
    }

    public boolean getSettingMsgSpeaker() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_SPEAKER, true);
    }

    public void setSettingAllowChatroomOwnerLeave(boolean value) {
        editor.putBoolean(SHARED_KEY_SETTING_CHATROOM_OWNER_LEAVE, value);
        editor.commit();
    }

    public boolean getSettingAllowChatroomOwnerLeave() {
        return mSharedPreferences.getBoolean(
                SHARED_KEY_SETTING_CHATROOM_OWNER_LEAVE, true);
    }

    public void setDeleteMessagesAsExitGroup(boolean value) {
        editor.putBoolean(SHARED_KEY_SETTING_DELETE_MESSAGES_WHEN_EXIT_GROUP,
                value);
        editor.commit();
    }

    public boolean isDeleteMessagesAsExitGroup() {
        return mSharedPreferences.getBoolean(
                SHARED_KEY_SETTING_DELETE_MESSAGES_WHEN_EXIT_GROUP, true);
    }

    public void setAutoAcceptGroupInvitation(boolean value) {
        editor.putBoolean(SHARED_KEY_SETTING_AUTO_ACCEPT_GROUP_INVITATION,
                value);
        editor.commit();
    }

    public boolean isAutoAcceptGroupInvitation() {
        return mSharedPreferences.getBoolean(
                SHARED_KEY_SETTING_AUTO_ACCEPT_GROUP_INVITATION, true);
    }

    public void setAdaptiveVideoEncode(boolean value) {
        editor.putBoolean(SHARED_KEY_SETTING_ADAPTIVE_VIDEO_ENCODE, value);
        editor.commit();
    }

    public boolean isAdaptiveVideoEncode() {
        return mSharedPreferences.getBoolean(
                SHARED_KEY_SETTING_ADAPTIVE_VIDEO_ENCODE, false);
    }

    public void setGroupsSynced(boolean synced) {
        editor.putBoolean(SHARED_KEY_SETTING_GROUPS_SYNCED, synced);
        editor.commit();
    }

    public boolean isGroupsSynced() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_GROUPS_SYNCED,
                false);
    }

    public void setContactSynced(boolean synced) {
        editor.putBoolean(SHARED_KEY_SETTING_CONTACT_SYNCED, synced);
        editor.commit();
    }

    public boolean isContactSynced() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_CONTACT_SYNCED,
                false);
    }

    public void setBlacklistSynced(boolean synced) {
        editor.putBoolean(SHARED_KEY_SETTING_BALCKLIST_SYNCED, synced);
        editor.commit();
    }

    public boolean isBacklistSynced() {
        return mSharedPreferences.getBoolean(
                SHARED_KEY_SETTING_BALCKLIST_SYNCED, false);
    }

    public void setCurrentUserNick(String nick) {
        editor.putString(SHARED_KEY_CURRENTUSER_NICK, nick);
        editor.commit();
    }

    public void setCurrentUserAvatar(String avatar) {
        editor.putString(SHARED_KEY_CURRENTUSER_AVATAR, avatar);
        editor.commit();
    }

    public String getCurrentUserNick() {
        return mSharedPreferences.getString(SHARED_KEY_CURRENTUSER_NICK, null);
    }

    public String getCurrentUserAvatar() {
        return mSharedPreferences
                .getString(SHARED_KEY_CURRENTUSER_AVATAR, null);
    }

    public void setCurrentUserName(String username) {
        editor.putString(SHARED_KEY_CURRENTUSER_USERNAME, username);
        editor.commit();
    }

    public String getCurrentUsername() {
        return mSharedPreferences.getString(SHARED_KEY_CURRENTUSER_USERNAME,
                null);
    }

    public void removeCurrentUserInfo() {
        editor.remove(SHARED_KEY_CURRENTUSER_NICK);
        editor.remove(SHARED_KEY_CURRENTUSER_AVATAR);
        editor.commit();
    }


    public void setCurrentUserFlowSId(String userid) {
        editor.putString(MY_KEY_SHARED_KEY_CURRENTUSER_FLOWSID, userid);
        editor.commit();
    }

    public String getCurrentUserFlowSId() {
        return mSharedPreferences.getString(
                MY_KEY_SHARED_KEY_CURRENTUSER_FLOWSID, "");
    }

    // 自己加的

    public void setCurrentUserId(String userid) {
        editor.putString(MY_KEY_SHARED_KEY_CURRENTUSER_USERID, userid);
        editor.commit();
    }

    public String getCurrentUserId() {
        return mSharedPreferences.getString(
                MY_KEY_SHARED_KEY_CURRENTUSER_USERID, null);
    }

    public void setCurrentUserRealName(String realname) {
        editor.putString(MY_KEY_SHARED_KEY_CURRENTUSER_USERREALNAME, realname);
        editor.commit();
    }

    public String getCurrentRealName() {
        return mSharedPreferences.getString(
                MY_KEY_SHARED_KEY_CURRENTUSER_USERREALNAME, null);
    }

    public void setCurrentUserClassName(String name) {
        editor.putString(CurrentUserClassName, name);
        editor.commit();
    }

    public String getCurrentUserClassName() {
        return mSharedPreferences.getString(
                CurrentUserClassName, null);
    }


    public void setCurrentUserYear(int UserYear) {
        editor.putInt(year, UserYear);
        editor.commit();
    }

    public int getCurrentUserYear() {
        return mSharedPreferences.getInt(
                year, -1);
    }


    public void setCurrentUserstudentType(int Type) {
        editor.putInt(studentType, Type);
        editor.commit();
    }

    public int getCurrentUserstudentType() {
        return mSharedPreferences.getInt(
                studentType, -1);
    }

    public void setCurrentUserClassId(int id) {
        editor.putInt(CurrentUserClassId, id);
        editor.commit();
    }

    public int getCurrentUserClassId() {
        return mSharedPreferences.getInt(
                CurrentUserClassId, -1);
    }

    public void setCurrentUserColleageName(String name) {
        editor.putString(MY_KEY_SHARED_KEY_CURRENTUSER_DEPTMENTNAME, name);
        editor.commit();
    }

    public String getCurrentUserColleageName() {
        return mSharedPreferences.getString(
                MY_KEY_SHARED_KEY_CURRENTUSER_DEPTMENTNAME, null);
    }

    public void setCurrentUserColleageId(int id) {
        editor.putInt(MY_KEY_SHARED_KEY_CURRENTUSER_DEPTMENTID, id);
        editor.commit();
    }

    public int getCurrentUserColleageId() {
        return mSharedPreferences.getInt(
                MY_KEY_SHARED_KEY_CURRENTUSER_DEPTMENTID, -1);
    }


    //缓存群详情里最下面的那个开关按钮，缓存是否是“接受消息但不提醒”
    public void setGroupMsgNobibi(String Key_groupid, String Value) {
        editor.putString(Key_groupid + "_GroupMsgNobibi", Value);
        editor.commit();
    }

    public String getGroupMsgNobibi(String Key_groupid) {
        return mSharedPreferences.getString(Key_groupid + "_GroupMsgNobibi", "");
    }

    /*对应四个工作中的未读消息条数以及一个总和*/
//通知消息msg_working_notice
    public void setNoticeUnreadNum(int num) {
        editor.putInt("msg_working_notice", num);
        editor.commit();
    }

    public int getNoticeUnreadNum() {
        return mSharedPreferences.getInt("msg_working_notice", 0);
    }

    //流程消息msg_working_flow
    public void setFlowUnreadNum(int num) {
        editor.putInt("msg_working_flow", num);
        editor.commit();
    }

    public int getFlowUnreadNum() {
        return mSharedPreferences.getInt(
                "msg_working_flow", 0);
    }

    //任务消息msg_working_task
    public void setTaskUnreadNum(int num) {
        editor.putInt("msg_working_task", num);
        editor.commit();
    }

    public int getTaskUnreadNum() {
        return mSharedPreferences.getInt(
                "msg_working_task", 0);
    }

    //会议消息msg_working_meeting
    public void setMeetingUnreadNum(int num) {
        editor.putInt("msg_working_meeting", num);
        editor.commit();
    }

    public int getMeetingUnreadNum() {
        return mSharedPreferences.getInt(
                "msg_working_meeting", 0);
    }

    //总合消息msg_working_amount
    public void setAmountUnreadNum(int num) {
        editor.putInt("msg_working_amount", num);
        editor.commit();
    }

    public int getAmountUnreadNum() {
        return mSharedPreferences.getInt(
                "msg_working_amount", 0);
    }


    //是否设置只接受好友信息
    //缓存群详情里最下面的那个开关按钮，缓存是否是“接受消息但不提醒”
    public void setfriendsMsgOnly(int receiveStatus) {
        editor.putInt(MY_KEY_SET_FRIENDSMSG_ONLY, receiveStatus);
        editor.commit();
    }

    public int getfriendsMsgOnly() {
        return mSharedPreferences.getInt(MY_KEY_SET_FRIENDSMSG_ONLY, -1);
    }

    //缓存群详情里最下面的那个开关按钮，缓存是否是“接受消息但不提醒”
    public void setDisplayMessageDetail(int receiveStatus) {
        editor.putInt(DISPLAYMESSAGEDETAIL, receiveStatus);
        editor.commit();
    }

    public int getDisplayMessageDetail() {
        return mSharedPreferences.getInt(DISPLAYMESSAGEDETAIL, -1);
    }

    //缓存是否是第一次进入APP应用
    public void setH5ISFIRSTUSE(boolean Status) {
        editor.putBoolean(ISFIRSTUSE, Status);
        editor.commit();
    }

    public boolean getH5ISFIRSTUSE() {
        return mSharedPreferences.getBoolean(ISFIRSTUSE, true);
    }


}
