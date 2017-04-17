package com.wust.easeui;


public class Constant extends EaseConstant {
    public static final int pageSize = 10;
    public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
    public static final String GROUP_USERNAME = "item_groups";
    public static final String CHAT_ROOM = "item_chatroom";
    public static final String ACCOUNT_REMOVED = "account_removed";
    public static final String ACCOUNT_CONFLICT = "conflict";
    public static final String CHAT_ROBOT = "item_robots";
    public static final String MESSAGE_ATTR_ROBOT_MSGTYPE = "msgtype";
    public static final String ACTION_GROUP_CHANAGED = "action_group_changed";
    public static final String ACTION_CONTACT_CHANAGED = "action_contact_changed";

    // 服务器测试访问的地址
    //方便Copy:http://120.132.85.24:8080/MOA/
    public static final String ROOT_URL = "http://202.114.242.32:8080/";
    public static final String BASE_URL = ROOT_URL + "SchoolApp/";
    public static final String FLOW_BASE_URL = ROOT_URL + "jflow-web/OA/Jflow/";
    //    服务器正式的参数
    //方便Copy:http://219.140.188.58:8080/MOA/
//    public static final String ROOT_URL = "http://219.140.188.58:8080/";
//    public static final String BASE_URL = ROOT_URL + "MOA/";
//    public static final String FLOW_BASE_URL = ROOT_URL + "/jflow-web/OA/Jflow/";

    //登录webservice
    public static final String SERVICE_URL = "http://jwxt.wust.edu.cn/whkjdx/services/whkdapp";
    //登录强智的接口
    public static final String NAMESPACE = "http://webservices.qzdatasoft.com";
    public static final String SOAP_ACTION = "";
    // 登录
    public static final String LOGIN_URL = BASE_URL
            + "home/company/1406.spring";
    // 通过userId获取用户个人信息
    public static final String USERINFO_URL = BASE_URL
            + "user/2002.spring";
    public static final String DEPARTONLINE_URL = BASE_URL + "user/2001.spring";
    public static final String GETMYCLASSTYPE_URL = BASE_URL
            + "user/2004.spring";
    // 上传头像URL
    public static final String UPLOADAVATAR_URL = BASE_URL
            + "tools/upload/0003.spring";
    // 获得公司部门列表信息
    public static final String GETSCHOOL_URL = BASE_URL
            + "user/2003.spring";
    //通过userId的数组批量获取每个id对应的真实姓名 1011
    public static final String NAMEFORMIDARRAY_URL = BASE_URL
            + "home/user/1011.spring";
    // 获得公司树形架构
    public static final String GETDEPTARCH_URL = BASE_URL
            + "admin/user/1003.spring";
    // 添加好友的模糊匹配接口
    public static final String ADDFRIENDS_URL = BASE_URL
            + "home/user/1012.spring";
    // 获得按部门分类及其列表，第一个是树形结构第二个之后又变了的那个接口
    public static final String GETTYPEBRAND_URL = BASE_URL
            + "home/user/1024.spring";

    // 获得按部门分类及其列表，全部都是树形结构。
    public static final String GETTYPEBRANDTREE_URL = BASE_URL
            + "home/user/1027.spring";
    // 修改是否能收到非好友信息状态(1034)
    public static final String CHANGEUSERPSW_URL = BASE_URL
            + "home/user/1033.spring";
    // 修改是否能收到非好友信息状态(1034)
    public static final String GETFRIENDSMSGONLY_URL = BASE_URL
            + "home/user/1034.spring";
    // 获得公司部门列表信息
    public static final String GETCOMPANY_URL = BASE_URL
            + "home/company/1301.spring";
    // 获得部门里的成员列表
    public static final String GETDEPT_URL = BASE_URL
            + "home/company/1302.spring";
    // 获取首页架构列表结构
    public static final String GETFIRSTPAGELIST_URL = BASE_URL
            + "home/menu/2701.spring";
    // 获得首页里的未读数字
    public static final String GETFIRSTPAGEUNREADNUM_URL = BASE_URL
            + "home/menu/2702.spring";
    // 拼接首页里的基础应用图像的前部分url(+"id"+".png")
    public static final String FP_IMAGEURL = BASE_URL
            + "upload/web/";

    /* *****************************************************************************
    * 新需求添加接口，按照类别分类获取分类列表
    * */
    //获取职务列表 1020
    public static final String GETTYPEBYPOSITION_URL = BASE_URL
            + "home/user/1020.spring";
    //获取职务列表 1021 暂未用到
//    public static final String GETTYPEBYPOSITION_URL = BASE_URL
//            + "home/user/1020.spring";
    //获取党支部列表 1022
    public static final String GETTYPEBYGCD_URL = BASE_URL
            + "home/user/1022.spring";
    //获取职称列表 1023
    public static final String GETTYPEBYJOB_URL = BASE_URL
            + "home/user/1023.spring";
    //获四个类别+各类别下的部门（没有人哦，要人的话用1025）1024
    public static final String GETALLTYPEDEPT_URL = BASE_URL
            + "home/user/1024.spring";
    //按类别获取该类别的人员 1025
    public static final String GETMEMBYTYPE_URL = BASE_URL
            + "home/user/1025.spring";
    //按类别获取该类别的人员 1027
    public static final String GETMEMBYTYPEBYID_URL = BASE_URL
            + "home/user/1027.spring";

    //**********************************************************************************

    // 验证码接口
    public static final String CHECKTHECODE = BASE_URL
            + "admin/companyRegister/1503.spring";
    // 发送验证码接口
    public static final String GETCHECKCODE_URL = BASE_URL
            + "home/company/1409.spring";
    // 发送验证码接口（忘记密码）
    public static final String GETCHECKCODEFORPSW_URL = BASE_URL
            + "home/company/1422.spring";
    // 忘记密码接口
    public static final String FINDFORGETPSW_URL = BASE_URL
            + "home/company/1423.spring";
    // 绑定手机接口
    public static final String GEGISTER_URL = BASE_URL
            + "home/login/1413.spring";
    // 获取群组成员数组
    public static final String GETGROUPMEMS_URL = BASE_URL
            + "home/company/1418.spring";
    // 获得用户头像
    public static final String GETHEADIMAG_URL = BASE_URL + "upload/avatar/";
    // 获取群组公告接口
    public static final String GETGROUPNOTICE_URL = BASE_URL
            + "home/company/1601.spring";
    // 编辑群组公告接口
    public static final String EDITGROUPNOTICE_URL = BASE_URL
            + "home/company/1602.spring";
    // 标识公告已读接口
    public static final String CHECKGROUPNOTICEREAD_URL = BASE_URL
            + "home/company/1603.spring";
    // 发送通知新接口加入了depid和userId
    public static final String SENTNOTICE_URL = BASE_URL + "inform/1706.spring";
    // 获取各项待完成数据（替代wpush）
    public static final String GETUNREADNUM_URL = BASE_URL + "inform/1710.spring";
    // 通知列表接口
    public static final String GETNOTICELIST_URL = BASE_URL
            + "inform/1702.spring";
    // 通知详情接口
    public static final String GETNOTICEDETAIL_URL = BASE_URL
            + "inform/1703.spring";
    // 确认收到通知
    public static final String READTHENOTICE_URL = BASE_URL
            + "inform/1704.spring";
    //通知详情接口(1708)（新）
    public static final String GETNOTICEDETAIL_NEW_URL = BASE_URL
            + "inform/1708.spring";
    //通知成员（已读、未读）(1709)
    public static final String GETWHOSEEWHONOSEE_URL = BASE_URL
            + "inform/1709.spring";
    // 发布任务
    public static final String CREATTASK_URL = BASE_URL
            + "home/task/1804.spring";
    // 获得任务类型列表
    public static final String GETTASKTYPE_URL = BASE_URL
            + "home/task/1806.spring";
    // 获得任务优先级类型列表
    public static final String GETTASKMAJOR_URL = BASE_URL
            + "home/task/1810.spring";
    // 获得任务优先级类型列表
    public static final String GETSEARCHCHILDTASK_URL = BASE_URL
            + "home/task/1811.spring";
    // 任务综合查询
    public static final String SEARCHTASKLIST_URL = BASE_URL
            + "home/task/1812.spring";
    // 需要任务1813
    public static final String CHANGETASKDETAIL_URL = BASE_URL
            + "home/task/1813.spring";
    // 议题类型变为任务的创建议题型任务接口
    public static final String CREATDUTYFROMMEET_URL = BASE_URL
            + "home/task/1807.spring";
    // 收到任务列表
    public static final String GETTASKLIST_URL = BASE_URL
            + "home/task/1801.spring";
    // 任务详情
    public static final String GETTASKDETAILT_URL = BASE_URL
            + "home/task/1803.spring";
    // 确认完成任务
    public static final String CHECKFINISHTASK_URL = BASE_URL
            + "home/task/1805.spring";
    // 已发送的列表
    public static final String GETMYTASKLIST_URL = BASE_URL
            + "home/task/1802.spring";
    // 获得已开始会议的列表
    public static final String GETBEGINMEET_URL = BASE_URL
            + "home/meeting/1903.spring";
    // 新建普通会议
    public static final String CREATMEETING_URL = BASE_URL
            + "home/meeting/1901.spring";
    // 新建院办会议
    public static final String CREATYBMEETING_URL = BASE_URL
            + "home/meeting/1907.spring";
    // 获取议题任务
    public static final String GETMEETINGTASK_URL = BASE_URL
            + "home/task/1808.spring";
    // 完成议题任务
    public static final String URL_FINISH_TOPICTASK = BASE_URL
            + "home/task/1809.spring";
    // 获取议题详情
    public static final String GETMEETINGDETAIL_URL = BASE_URL
            + "home/meeting/1904.spring";
    // 获取议题详情（新）
    public static final String GETMEETINGDETAILNEW_URL = BASE_URL
            + "admin/workflowform/2505.spring";
    // 更新会议纪要
    public static final String GETMEETINGTAG_URL = BASE_URL
            + "home/meeting/1905.spring";
    // 获取当前人可创建的任务类型列表
    public static final String GETCREATMEETINGLIST_URL = BASE_URL
            + "home/meeting/1906.spring";
    // 更改会议状态
    public static final String CHANGEMEETINGSTATUS = BASE_URL
            + "home/meeting/1902.spring";
    // 更新APK的地址
    public static final String GETUPDATA_URL = BASE_URL
            + "home/company/0010.spring";
    // 上传附件统一地址
    public static final String UPDATAATTACH_URL = BASE_URL
            + "home/files/2201.spring";
    //爬取新闻图片
    public static final String GETNEWSINDEX_URL = BASE_URL
            + "tools/spider/0005.spring";
    //获取未处理事项的列表
    public static final String UNDOTHINGS_URL = BASE_URL
            + "/home/user/1035.spring";
    // 新建评论统一地址
    /**
     * objectTable
     * 任务：task
     * 会议：meeting
     * 通知：inform
     */
    public static final String EDITCOMMENT_URL = BASE_URL
            + "home/comment/2301.spring";
    // 获取评论列表统一地址
    public static final String GETCOMMENTLIST_URL = BASE_URL
            + "home/comment/2302.spring";

    /*****************************
     * 新工作流HTML5的页面地址
     ***************************************/
    //FLOWH5的详情地址
    public static final String FLOWHTML5_DETAILURL = BASE_URL + "MobileOA/static/workflow/workflow-H5/html/detail.html";
    // FlowH5创建根地址
    public static final String FLOWHTML5_CREATEURL = BASE_URL + "MobileOA/static/workflow/workflow-H5/html/create.html";
    // Flow首页H5地址
    public static final String FLOWHTML5_URL = BASE_URL + "MobileOA/static/workflow/workflow-H5/html/index.html";
    // 获取Flow工作流申请框框列表
    public static final String FLOWAPPLYLIST_URL = BASE_URL + "admin/workflow/2605.spring";
    // 首页待我审批地址入口
    public static final String FIRSTPAGE_WAITDEAL_URL = BASE_URL + "MobileOA/static/workflow/workflow-H5/html/waitmelist.html";
    // 获取Flow工作流申请框框列表
    public static final String FIRSTPAGE_MYAPPROVE_URL = BASE_URL + "MobileOA/static/workflow/workflow-H5/html/myapprovelist.html";
    /*****************************
     * 以下是FLOW的接口
     ***************************************/
    // Flow用户登录接口
    public static final String FLOWLOGIN_URL = FLOW_BASE_URL + "2001.do";
    // Flow用户当前权限下可以发起流程接口
    public static final String FLOWLIST_APPLY_URL = FLOW_BASE_URL + "2011.do";
    // Flow查询当前登陆人所有流程的待办 2012
    public static final String FLOWLIST_WAITDEAL_URL = FLOW_BASE_URL
            + "2012.do";
    // Flow查询当前登陆人某一类流程的待办2013
    // public static final String FLOWLIST_WAITDEAL_URL = FLOW_BASE_URL
    // + "2012.do";
    // Flow查询当前登陆人所有的正在运行的流程2014
    public static final String FLOWLIST_ALLRUN_URL = FLOW_BASE_URL + "2014.do";
    // 查询当前登陆人某一类流程的正在运行的流程2015
    public static final String FLOWLIST_ISRUNNING_URL = FLOW_BASE_URL
            + "2015.do";
    // 申请议题
    public static final String FLOWLIST_MEETTINGAPPLY_URL = FLOW_BASE_URL
            + "2022.do";
    // 获取指定人员的抄送列表(已读) 2031
    public static final String FLOWLIST_COPYREAD_URL = FLOW_BASE_URL
            + "2031.do";
    // 获取指定人员的抄送列表(未读) 2032
    public static final String FLOWLIST_COPYUNREAD_URL = FLOW_BASE_URL
            + "2032.do";
    // 获取我发起的流程列表 2020(放在流程一级菜单)
    public static final String FLOWLIST_MYAPPLY_URL = FLOW_BASE_URL + "2020.do";
    // 获取节点详情 2051
    public static final String FLOWLIST_GETJFLOWDETAIL_URL = FLOW_BASE_URL
            + "2051.do";
    // 发送流程（进行下一步）2016
    public static final String FLOWLIST_BUTTON_YES_URL = FLOW_BASE_URL
            + "2016.do";
    // 退回（审核不通过）2017
    public static final String FLOWLIST_BUTTON_NO_URL = FLOW_BASE_URL
            + "2017.do";
    // 撤销发送2018
    public static final String FLOWLIST_BUTTON_RETURN_URL = FLOW_BASE_URL
            + "2018.do";
    // 获取轨迹Track2041
    public static final String FLOWLIST_GETTDETAILRACK_URL = FLOW_BASE_URL
            + "2041.do";
    // 获取不是我发起的,但是我参与的流程(参与表示已经审批) 2037
    public static final String GETIHAVEDEALJFLOW_URL = FLOW_BASE_URL
            + "2037.do";

    /*****************************
     * 回调函数的一些列请求码
     ***************************************/
    public static final int scanQR = 1;
    public static final int readaddresslist = 2;
    public static final int creatgroupchat = 3;
    public static final int CHOOSE_PICTURE = 4;
    public static final int CHOOSE_NOTICEPEOPLE = 5;
    public static final int CHOOSE_TASKCOMMENT = 6;
    public static final int CHOOSE_ATPEOPLE = 7;
    public static final int FILE_SELECT_CODE = 8;
    public static final int ADD_CHILDTASK_NEEDFRESH = 9;
    public static final int FINISHTHECHILDTASK = 10;
    public static final int RecallSeeNoticedetailNum = 11;
    public static final int RecallNoSeeNoticedetailNum = 12;
    /*****************************
     * 缓存用的Key统一管理
     ***************************************/
    /*start选人界面信息缓存字段start*/
    public static String MY_KEY_ARCHTREE = "MY_KEY_ARCHTREE";
    public static String MY_KEY_PARTY = "partyBranchId";
    public static String MY_KEY_JOB = "jobClassifyId";
    public static String MY_KEY_POSITION = "positionalTitleId";
    /*end选人界面信息缓存字段end*/
    // 缓存个人信息对象
    public static String MY_KEY_USERINFO = "MY_KEY_USERINFO";
    // 缓存公司信息对象
    public static String MY_KEY_COMPANY = "MY_KEY_COMPANY";
    // 缓存新闻页面切换界面最后停留的位置，便于下次切回来的时候读取缓存还原位置。不然勒？不然又从第一条新闻开始浏览
    public static String MY_KEY_UNREADMSGNUM = "MY_KEY_UNREADMSGNUM";
    /*****************************
     * 广播用的Key统一管理
     ***************************************/
    // 注册广播的action
    //在JPush中发送代办数字的广播
    public static final String BROAD_UNREADMSG = "ontoweb.work.unreadMsg";
    //jpush有消息来了就在首页插入一条
    public static final String BROAD_JPUSHDATA = "ontoweb.work.jpushData";
    public static boolean viewpageflip = false; // viewpage滑动的时候，侧边栏不会出现的标志


}
