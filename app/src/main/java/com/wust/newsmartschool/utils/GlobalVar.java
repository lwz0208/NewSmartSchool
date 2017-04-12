package com.wust.newsmartschool.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.wust.newsmartschool.domain.MsgItem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GlobalVar
{
    public static Typeface tf = null; // 统一的字体风格
    public static boolean viewpageflip = false; // viewpage滑动的时候，侧边栏不会出现的标志
    public static File cacheFile; // ，缓存文件夹"smtsch"
    public static int deviceWidth; // 屏幕的宽度
    public static int _1dp2px; // 1dp到1px的值
    public static int deviceHeight; // 屏幕的高度
    public static String userid;// 学号
    public static String username;
    public static String password;
    public static String usertype;//用户类型，0130代表：0：青山校区，1：空教室管理员，3：教三，0：教三所有区
    public static String tiebaip = "http://202.114.255.75:8089/";
    public static String serviceip = "http://202.114.255.75:8090/";
    public static String serverClient = "http://202.114.255.75:8085/newclient/";
    public static String schoolip = "http://202.114.255.100/Xsda/loginnews/Login.php";
    public static String schoolupdateip = "http://app.wust.edu.cn/SchoolUpdate/";
    public static String userImg;
    // public static String phonenum;
    public static String identifiy; // 用户身份标识
    public static String companyid = "7CF7EAD1-5A69-A241-E236-2514D7A6119D";// 注册贴吧专用
    public static EditText editText;
    public static int _1px2dp; // 1px到1dp的值

    // 以下是心理咨询-》我的预约：保存信息的变量
    public static String psy_name = "无";// 咨询师姓名
    public static String psy_status = "无预约";// 预约状态
    public static String psy_date_time = "无";// 预约时间

    // 部门在线个人信息
    // public static String IM_id;//--->子账号VOIP
    // public static String IM_subAccount;//--->子账号Account
    // public static String IM_subToken;//---->子账号Token
    // public static String IM_psw;//--->子账号VOIP密码
    // public static String IM_createDate;

    public static ArrayList<String> ids;
    public static ArrayList<String> departnames;
    public static String appId = "aaf98f894b00309b014b05be427d0351";// --->IM所需的AppId
    public static String IM_mainAccount = "8a48b5514b003ad2014b05bbc49d038c";// --->主账号Account
    public static String IM_mainToken = "9251cdfdffd74670aa17ce951e4d317b";// ---->主账号Token
    //public static SubAccount subAccount = new SubAccount();

    public static boolean isLogin = false;

//    public static String BaseURL = "https://sandboxapp.cloopen.com:8883/"
//            + Build.REST_VERSION_NAME;
//    public static String QueryURL = BaseURL + "/Accounts/" + IM_mainAccount
//            + "/QuerySubAccountByName?sig=";
//    public static String ApplyURL = BaseURL + "/Accounts/" + IM_mainAccount
//            + "/SubAccounts?sig=";
    // 聊天信息数据库版本号
    public static int IM_DB_version = 3;
    //学生之家数据库版本号
    public static int Student_DB_version = 2;
    // 聊天数据-数据结构
    public static boolean isLoginXMPP = false;// 是否登入
    public static String presentChatPerson = null;// 当前聊天对象
    public static int presentChatType = -1;//当前聊天是否匿名
    public static boolean isChatFaceOnTop = false;// 聊天界面是否在栈顶
    // public static LinkedList<ChatMessageItem> messageItems = new
    // LinkedList<ChatMessageItem>();//消息队列
    public static Map<String, String> chatlines = new HashMap<String, String>();// 最新信息数
    public static Map<String, ArrayList<MsgItem>> chatMsgListMap = new HashMap<String, ArrayList<MsgItem>>();

    // public static LinkedList<ChatMessageItem> messageItems = new
    // LinkedList<ChatMessageItem>();

    public static boolean isInfoGCBySystem()
    {
        Log.d("SmartSchool", "校园app测试");
        boolean b = false;

        if (TextUtils.isEmpty(userid))
        {
            b = true;
        }
        if (TextUtils.isEmpty(username))
        {
            b = true;
        }
        if (TextUtils.isEmpty(password))
        {
            b = true;
        }
        if(TextUtils.isEmpty(usertype))
        {
            b=false;
        }

        Log.d("SmartSchoolCBH", "校园app测试"+Boolean.toString(b));
        return b;
    }

    public static void initGlobalvar(Activity context)
    { // 初始化全局变量
        GlobalVar.tf = Typeface.createFromAsset(context.getAssets(),
                "font/Roboto_Light.ttf");
        GlobalVar.cacheFile = new File(
                Environment.getExternalStorageDirectory(), "SmartSchool");
        if (!(GlobalVar.cacheFile.exists()))
        {
            GlobalVar.cacheFile.mkdirs();
        }
        GlobalVar.deviceWidth = context.getWindowManager().getDefaultDisplay()
                .getWidth();
        GlobalVar.deviceHeight = context.getWindowManager().getDefaultDisplay()
                .getHeight();

        SharedPreferences sharedPreferences2 = context.getSharedPreferences(
                "userinfo", Context.MODE_PRIVATE);
        GlobalVar.username = sharedPreferences2.getString("username", "");
        GlobalVar.password = sharedPreferences2.getString("password", "");
        GlobalVar.userid = sharedPreferences2.getString("userid", "");
        GlobalVar.usertype = sharedPreferences2.getString("usertype", "");
        Log.e("----Globalvar----11111", usertype);



//        SharedPreferenceUtils sharedPreferenceUtils = new SharedPreferenceUtils(
//                context);
//        sharedPreferenceUtils.GetUserInfo();

    }

}

