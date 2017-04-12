package com.wust.newsmartschool.utils;

public class URL_UNIVERSAL {
    public static final String URL_BASE = "http://202.114.255.100";

    //登录
    public static final String URL_LOGIN = URL_BASE + "/Xsda/Student/login.php";
    //在线更新
    public static final String URL_UPDATE = URL_BASE + "/SchoolUpdate/";

    //学校新闻
    public static final String URL_SCHOOL_NEWS = URL_BASE + ":8080/WKSW/user/1001.spring";
    //学校公告
    public static final String URL_SCHOOL_NOTICE = URL_BASE + ":8080/WKSW/user/1002.spring";
    //学校校车
    public static final String URL_SCHOOL_BUS = URL_BASE + ":8080/WKSW/user/1003.spring";

    //学校简介
    public static final String URL_SCHOOL_INTRODUCTION = URL_BASE + ":8080/SchoolApp//library/1001.spring";
    //学校领导简介
    public static final String URL_SCHOOL_LEADER = URL_BASE + ":8080/SchoolApp//library/1000.spring";

    //获取部门
    public static final String URL_DEPART = URL_BASE + ":8080/SchoolApp/admin/1101.spring";

    //获取空闲座位
    public static final String URL_EMPTY_SEAT = URL_BASE + "/Xsda/ThinkCMS/application/Seatbook/Action/kxzw.php";
    //高级预约
    public static final String URL_ADVANCED_RESERVE = URL_BASE + "/Xsda/ThinkCMS/application/Seatbook/Action/gjyy.php";
    //一键预约
    public static final String URL_ONEKEY_RESERVE = URL_BASE + "/Xsda/ThinkCMS/application/Seatbook/Action/yjyy.php";
    //取消预约
    public static final String URL_CANCEL_RESERVE = URL_BASE + "/Xsda/ThinkCMS/application/Seatbook/Action/qxyy.php";
    //信息中心
    public static final String URL_INFORMATION_CENTER = URL_BASE + "/Xsda/ThinkCMS/application/Seatbook/Action/xxzx.php";
    //扫描二维码
    public static final String URL_SCAN_QR_CODE = URL_BASE + "/Xsda/ThinkCMS/application/Seatbook/Action/smewm.php";
    //暂时离开和释放座位
    public static final String URL_LEAVE_RELEASE = URL_BASE + "/Xsda/ThinkCMS/application/Seatbook/Action/leaveseat.php";

}

