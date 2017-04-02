package com.ding.chat.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.ding.chat.DemoApplication;
import com.ding.chat.WJPush;
import com.ding.chat.domain.ChildrenItem;
import com.ding.chat.domain.GroupItem;
import com.ding.chat.domain.UnreadMsgNum;
import com.ding.chat.domain.UserInfoEntity;
import com.ding.easeui.Constant;
import com.ding.easeui.utils.BadgeUtil;
import com.ding.easeui.utils.CommonUtils;
import com.ding.easeui.utils.PreferenceManager;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by Erick on 2016/10/7.
 */
public class appUseUtils {

    public static List<ChildrenItem> checkedChildrenList = new ArrayList<ChildrenItem>();
    public static List<GroupItem> checkedGroupList = new ArrayList<GroupItem>();


    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public static String getCacheSize(Context context) {
        try {
            return getFormatSize(getFolderSize(new File(context.getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    private static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    private static String getFormatSize(double size) {

//        double kiloByte = size / 1024;
        return size + "";

//        double kiloByte = size / 1024;
//        if (kiloByte < 1) {
//            return size + "Byte";
//        }
//
//        double megaByte = kiloByte / 1024;
//        if (megaByte < 1) {
//            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
//            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
//        }
//
//        double gigaByte = megaByte / 1024;
//        if (gigaByte < 1) {
//            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
//            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
//        }
//
//        double teraBytes = gigaByte / 1024;
//        if (teraBytes < 1) {
//            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
//            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
//        }
//        BigDecimal result4 = new BigDecimal(teraBytes);
//
//        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    public static void RefreshMyInfo(Context mContext) {
        JSONObject phone = new JSONObject();
        try {
            phone.put("userId", EMClient.getInstance().getCurrentUser());
            CommonUtils.setCommonJson(mContext, phone, PreferenceManager.getInstance().getCurrentUserFlowSId());
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        OkHttpUtils.postString().url(Constant.USERINFO_URL)
                .content(phone.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String arg0) {
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1 && jObject.getJSONObject("data") != null) {
                                UserInfoEntity userInfoEntity = new UserInfoEntity();
                                userInfoEntity = new Gson().fromJson(arg0,
                                        UserInfoEntity.class);
                                DemoApplication.getInstance().mCache.put(
                                        Constant.MY_KEY_USERINFO,
                                        userInfoEntity);
                                PreferenceManager.getInstance().setCurrentUserRealName(userInfoEntity.getData()
                                        .getUserRealname().toString());
                                PreferenceManager.getInstance().setfriendsMsgOnly(userInfoEntity.getData().getReceiveStatus());
                                EMClient.getInstance().updateCurrentUserNick(
                                        userInfoEntity.getData()
                                                .getUserRealname());
                            } else {
                                DemoApplication.getInstance().mCache.remove(Constant.MY_KEY_USERINFO);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        Log.i("SettingsFragment", arg0 + "---" + arg1.toString());
                        DemoApplication.getInstance().mCache.remove(Constant.MY_KEY_USERINFO);
                    }
                });
    }

    // 打印所有的 intent extra 数据
    public static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i("printBundle", "This message has no Extra data");
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
                    Log.e("printBundle", "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }


    public static void GetMyUnreadMumAndJPush(final Context mCt) {
        JSONObject userIdJson = new JSONObject();
        try {
            userIdJson.put("userId", PreferenceManager.getInstance().getCurrentUserId());
            userIdJson.put("type", "all");
            CommonUtils.setCommonJson(mCt, userIdJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
            Log.e("GetMyUnreadMumAndJPush", userIdJson.toString());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        OkHttpUtils.postString().url(Constant.GETUNREADNUM_URL)
                .content(userIdJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1) {
                                Log.e("GetMyUnreadMumAndJPush", arg0.toString());
                                UnreadMsgNum unreadMsgNum = new Gson().fromJson(arg0, UnreadMsgNum.class);
                                PreferenceManager.getInstance().setNoticeUnreadNum(unreadMsgNum.getData().getInform());
                                PreferenceManager.getInstance().setTaskUnreadNum(unreadMsgNum.getData().getTask());
                                PreferenceManager.getInstance().setFlowUnreadNum(unreadMsgNum.getData().getJflow());
                                PreferenceManager.getInstance().setMeetingUnreadNum(unreadMsgNum.getData().getMeeting());
                                PreferenceManager.getInstance().setAmountUnreadNum(unreadMsgNum.getData().getNum());
                                //最后来一个大存
                                DemoApplication.getInstance().mCache.put(Constant.MY_KEY_UNREADMSGNUM, unreadMsgNum.getData());
                                // 连接Wpush,现启用JPush自己计数
                                WJPush.SentUnreadNum(mCt);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
//                        Log.e("GetMyUnreadMum", arg0.toString());
                    }
                });
    }

    //加减已读未读那个数字，数字在缓存里，统一加减后发广播。
//    public static void reduceUnreadNum(Context context, String type, int option) {
//        UnreadMsgNum.DataBean optionData = (UnreadMsgNum.DataBean) DemoApplication.getInstance().mCache.getAsObject(Constant.MY_KEY_UNREADMSGNUM);
//        switch (type) {
//            case "unread_work_notice":
//                if (option == 1) {
//                    optionData.setMsg_working_notice(optionData.getMsg_working_notice() + 1);
//                    //为1就是++操作
//                } else {
//                    //为0就是--操作
//                    if (optionData.getMsg_working_notice() != 0)//总不能减成负数吧
//                        optionData.setMsg_working_notice(optionData.getMsg_working_notice() - 1);
//                }
//                break;
//            case "msg_working_flow":
//                if (option == 1) {
//                    optionData.setMsg_working_flow(optionData.getMsg_working_flow() + 1);
//                    //为1就是++操作
//                } else {
//                    //为0就是--操作
//                    if (optionData.getMsg_working_flow() != 0)//总不能减成负数吧
//                        optionData.setMsg_working_flow(optionData.getMsg_working_flow() - 1);
//                }
//                break;
//            case "msg_working_task":
//                if (option == 1) {
//                    optionData.setMsg_working_task(optionData.getMsg_working_task() + 1);
//                    //为1就是++操作
//                } else {
//                    //为0就是--操作
//                    if (optionData.getMsg_working_task() != 0)//总不能减成负数吧
//                        optionData.setMsg_working_task(optionData.getMsg_working_task() - 1);
//                }
//                break;
//            case "msg_working_meeting":
//                if (option == 1) {
//                    optionData.setMsg_working_meeting(optionData.getMsg_working_meeting() + 1);
//                    //为1就是++操作
//                } else {
//                    //为0就是--操作
//                    if (optionData.getMsg_working_meeting() != 0)//总不能减成负数吧
//                        optionData.setMsg_working_meeting(optionData.getMsg_working_meeting() - 1);
//                }
//                break;
//
//        }
//        //直接判断是否为+1-1操作，在总数上直接加减
//        if (option == 1) {
//            optionData.setMsg_working_amount(optionData.getMsg_working_amount() + 1);
//            //为1就是++操作
//        } else {
//            //为0就是--操作
//            if (optionData.getMsg_working_amount() != 0)//总不能减成负数吧
//                optionData.setMsg_working_amount(optionData.getMsg_working_amount() - 1);
//        }
//        //把操作完的对象塞回去
//        DemoApplication.getInstance().mCache.put(Constant.MY_KEY_UNREADMSGNUM, optionData);
//        // 连接Wpush,现启用JPush自己计数
//        WJPush.SentUnreadNum(context);
//    }

    public static void SentJpushData(Context mCtx, JSONObject jsonData) {
        Intent intent = new Intent();
        intent.setAction(Constant.BROAD_JPUSHDATA);
        intent.putExtra(Constant.BROAD_JPUSHDATA, jsonData.toString());
        mCtx.sendBroadcast(intent);
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
}
