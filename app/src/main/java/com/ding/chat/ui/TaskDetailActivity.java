package com.ding.chat.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.ding.chat.R;
import com.ding.chat.adapter.ChildTask_Adapter;
import com.ding.chat.adapter.NoticeAttach_Adapter;
import com.ding.chat.adapter.TaskDetailMembers_Adapter;
import com.ding.chat.adapter.Notice_CommentListAdapter;
import com.ding.chat.domain.ChildTaskEntitiy;
import com.ding.chat.domain.DeptMembersEntity_Person;
import com.ding.chat.domain.MeetingTopics;
import com.ding.chat.domain.NoticeCommentEntity;
import com.ding.chat.domain.NoticeDetailEntity;
import com.ding.chat.views.ECProgressDialog;
import com.ding.chat.views.ListViewForScrollView;
import com.ding.easeui.Constant;
import com.ding.easeui.utils.CommonUtils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.ding.easeui.utils.PreferenceManager;
import com.ding.easeui.widget.GlideRoundTransform;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskDetailActivity extends BaseActivity implements View.OnClickListener {

    String TAG = "TaskDetailActivity_Debugs";
    private final static String URL = Constant.GETTASKDETAILT_URL;
    private final static String URL_UPDATE_STATUS = Constant.CHECKFINISHTASK_URL;

    private LinearLayout rl_finish;
    private RelativeLayout rl_status;

    private RelativeLayout title_workfrag_taskdetail;
    private TextView tv_taskStatus;
    private TextView taskdetail_createuser;
    private TextView iv_resource;
    private TextView iv_taskType;
    private TextView iv_taskTitle;
    private TextView iv_taskContent;
    private TextView iv_taskDescribe;
    private TextView iv_chargePeople;
    private TextView iv_taskMember;
    private TextView iv_major;
    private TextView iv_limitTime;
    private ImageView iv_changetask;
    //最下面的4个按钮,有一个隐藏了哦！
    private LinearLayout tv_finish;
    private LinearLayout tv_comment;
    private LinearLayout tv_addchildtask;
    private LinearLayout tv_changestatus;
    private ListViewForScrollView lv_taskdetailcomment;
    //三个Listview的前两个listview的标题，如果没有就隐藏，所以绑定以下
    private LinearLayout ll_taskdetailattach;
    private LinearLayout ll_taskdetailchildtask;
    Notice_CommentListAdapter taskcommentListAdapter;
    ArrayList<NoticeCommentEntity.DataBean> taskCommentList;
    //回复评论传的实体，由于发表评论接口需要的数据太多了，所以就直接传一个实体过去
    NoticeCommentEntity.DataBean recommentdata;
    //子任务适配器+数据+子任务的lv
    private ListViewForScrollView lv_taskdetailchildtask;
    ChildTask_Adapter childTask_adapter;
    List<ChildTaskEntitiy> childTaskList;
    //附件的控件+数据+适配器
    private ListViewForScrollView lv_taskdetailattach;
    NoticeAttach_Adapter noticeAttach_adapter;
    //接口中新加的附件List
    List<NoticeDetailEntity.FileListBean> TaskFilesList;
    private ImageView tasldetail_avater;
    private int taskID = -1;
    private boolean isNeedRefresh = false;
    private ECProgressDialog pd;
    //任务详情全局参数以及数据的全局参数
    JSONObject json;
    /*下面是一些为了把评论给悬浮起来的一些参数*/
    private ScrollView myScrollView;
    //任务参与人点击系列
    JSONArray membersArr;
    TaskDetailMembers_Adapter taskmembersAdapter;
    List<DeptMembersEntity_Person> persons_list;
    //传到评论页面的拼接字符串，作推送用
    String toUserIds = "";
    //点击下载附件的一个Item
    NoticeDetailEntity.FileListBean BeClickItem;
    //几个下载附件相关的东东
    ProgressDialog mProgressBar;
    OkHttpClient mOkHttpClient;
    Handler mHandler;
    //从点击点击过来的flag
    String from;
    //从会议详情里点击议题带过来的
    int issueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail_new);
        pd = new ECProgressDialog(TaskDetailActivity.this);
        //下载进度条控件
        //下载附件提示框的一些设置
        mProgressBar = new ProgressDialog(TaskDetailActivity.this);
        mProgressBar.setMessage("正在下载附件文件...");
        mProgressBar.setCancelable(true);
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressBar.setProgress(0);
        mProgressBar.setMax(100);
        mOkHttpClient = new OkHttpClient();
        myScrollView = (ScrollView) findViewById(R.id.taskdetail_ScrollView);
        title_workfrag_taskdetail = (RelativeLayout) findViewById(R.id.title_workfrag_taskdetail);
        rl_finish = (LinearLayout) findViewById(R.id.rl_finish);
        rl_status = (RelativeLayout) findViewById(R.id.rl_status);
        tv_taskStatus = (TextView) findViewById(R.id.tv_taskStatus);
        taskdetail_createuser = (TextView) findViewById(R.id.taskdetail_createuser);
        iv_major = (TextView) findViewById(R.id.iv_major);
        iv_resource = (TextView) findViewById(R.id.iv_resource);
        iv_taskType = (TextView) findViewById(R.id.iv_taskType);
        iv_taskTitle = (TextView) findViewById(R.id.iv_taskTitle);
        iv_taskContent = (TextView) findViewById(R.id.iv_taskContent);
        iv_taskDescribe = (TextView) findViewById(R.id.iv_taskDescribe);
        iv_chargePeople = (TextView) findViewById(R.id.iv_chargePeople);
        iv_taskMember = (TextView) findViewById(R.id.iv_taskMember);
        iv_limitTime = (TextView) findViewById(R.id.iv_limitTime);
        iv_changetask = (ImageView) findViewById(R.id.iv_changetask);
        tv_finish = (LinearLayout) findViewById(R.id.tv_finish);
        tv_comment = (LinearLayout) findViewById(R.id.tv_comment);
        tv_addchildtask = (LinearLayout) findViewById(R.id.tv_addchildtask);
        tv_changestatus = (LinearLayout) findViewById(R.id.tv_changestatus);
        ll_taskdetailattach = (LinearLayout) findViewById(R.id.ll_taskdetailattach);
        ll_taskdetailchildtask = (LinearLayout) findViewById(R.id.ll_taskdetailchildtask);
        iv_taskMember.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
        tv_comment.setOnClickListener(this);
        tv_addchildtask.setOnClickListener(this);
        tv_changestatus.setOnClickListener(this);
        iv_changetask.setOnClickListener(this);
        tasldetail_avater = (ImageView) findViewById(R.id.tasldetail_avater);
        lv_taskdetailcomment = (ListViewForScrollView) findViewById(R.id.lv_taskdetailcomment);
        lv_taskdetailchildtask = (ListViewForScrollView) findViewById(R.id.lv_taskdetailchildtask);
        lv_taskdetailattach = (ListViewForScrollView) findViewById(R.id.lv_taskdetailattach);
        taskID = Integer.parseInt(getIntent().getStringExtra("taskID"));
        from = getIntent().getStringExtra("from");
        if (from != null) {
            if (from.equals("MeetingDetailActivity")) {
                ((TextView) findViewById(R.id.id_tv_title)).setText("议题详情");
            }
        }
        getTaskDetail();
        //新添加评论模块
        taskCommentList = new ArrayList<>();
        taskcommentListAdapter = new Notice_CommentListAdapter(taskCommentList, TaskDetailActivity.this);
        lv_taskdetailcomment.setAdapter(taskcommentListAdapter);
        //新添加附件模块
        TaskFilesList = new ArrayList<>();
        noticeAttach_adapter = new NoticeAttach_Adapter(TaskDetailActivity.this, TaskFilesList);
        lv_taskdetailattach.setAdapter(noticeAttach_adapter);
        //新添加子任务模块
        childTaskList = new ArrayList<>();
        childTask_adapter = new ChildTask_Adapter(TaskDetailActivity.this, childTaskList);
        lv_taskdetailchildtask.setAdapter(childTask_adapter);
        //为了解决scrollview自动定位问题
        myScrollView.smoothScrollTo(0, 0);
        title_workfrag_taskdetail.setFocusable(true);
        title_workfrag_taskdetail.setFocusableInTouchMode(true);
        title_workfrag_taskdetail.requestFocus();
        lv_taskdetailchildtask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChildTaskEntitiy clickItem = (ChildTaskEntitiy) childTask_adapter.getItem(position);
                startActivityForResult(new Intent(TaskDetailActivity.this, TaskDetailActivity.class).putExtra
                        ("taskID", String.valueOf(clickItem.getId())), Constant.FINISHTHECHILDTASK);
            }
        });
        //点击任务成员系列代码
        persons_list = new ArrayList<>();
        taskmembersAdapter = new TaskDetailMembers_Adapter(TaskDetailActivity.this, persons_list);
        //点击评论列表回复评论，需要跳到编辑评论界面，并且把点击的评论实体带过去。
        lv_taskdetailcomment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userId = String.valueOf(((NoticeCommentEntity.DataBean) taskcommentListAdapter.getItem(position)).getUserId());
                startActivity(new Intent(getApplicationContext(),
                        DeptMemInfoActivity2.class).putExtra("userId",
                        userId));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    overridePendingTransition(R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);
                }

            }
        });
        //下载附件
        lv_taskdetailattach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int p = position;
                //赋值给点击的对象
                BeClickItem = (NoticeDetailEntity.FileListBean) noticeAttach_adapter.getItem(position);

                if (BeClickItem != null) {
//                    if (from == null) {

                    String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    File file = new File(SDPath, BeClickItem.getFileName().toString());
                    //如果文件存在辣么就直接打开，如果不存在那就下载咯！~
                    if (file.exists()) {
                        if (BeClickItem.getFileType() != null)
                            CommonUtils.openFile(TaskDetailActivity.this, file, BeClickItem.getFileType().toString());
                        else
                            CommonUtils.openFileUnknowType(TaskDetailActivity.this, file);
                    } else {
                        Log.e(TAG, Constant.BASE_URL + BeClickItem.getFileRealPath().toString());
                        mProgressBar.show();
                        Request request = new Request.Builder().url(Constant.BASE_URL + BeClickItem.getFileRealPath().toString()).build();
                        mOkHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e(TAG, "onFailure");
                                showToastShort("下载失败");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.e(TAG, response.toString());
                                InputStream is = null;
                                byte[] buf = new byte[2048];
                                int len = 0;
                                FileOutputStream fos = null;
                                String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                                try {
                                    is = response.body().byteStream();
                                    long total = response.body().contentLength();
                                    File file = new File(SDPath, BeClickItem.getFileName().toString());
                                    fos = new FileOutputStream(file);
                                    long sum = 0;
                                    while ((len = is.read(buf)) != -1) {
                                        fos.write(buf, 0, len);
                                        sum += len;
                                        int progress = (int) (sum * 1.0f / total * 100);
                                        Log.d("h_bl", "progress=" + progress);
                                        Message msg = mHandler.obtainMessage();
                                        msg.what = 1;
                                        msg.arg2 = p;
                                        msg.arg1 = progress;
                                        mHandler.sendMessage(msg);
                                    }
                                    fos.flush();
                                    if (BeClickItem.getFileType() != null)
                                        CommonUtils.openFile(TaskDetailActivity.this, file, BeClickItem.getFileType().toString());
                                    else
                                        CommonUtils.openFileUnknowType(TaskDetailActivity.this, file);
                                    mProgressBar.dismiss();
                                    Log.d("h_bl", "文件下载成功");
                                } catch (Exception e) {
                                    mProgressBar.dismiss();
                                    Log.d("h_bl", "文件下载失败" + "/" + e.toString());
                                } finally {
                                    try {
                                        if (is != null)
                                            is.close();
                                    } catch (IOException e) {
                                    }
                                    try {
                                        if (fos != null)
                                            fos.close();
                                    } catch (IOException e) {
                                    }
                                }
                            }
                        });
                    }
                } else {

                    showToastShort("下载失败");
                }
            }
        });
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        int progress = msg.arg1;
                        mProgressBar.setProgress(progress);
                        if (progress == 100) {
                            showToastShort("文件下载成功");
                            //将下载好的附件后面的图标弄成可以打开的样纸
                            TaskFilesList.get(msg.arg2).setFileExist(true);
                            noticeAttach_adapter.notifyDataSetChanged();
                        }
                        break;

                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    //模拟添加评论
    private void initTaskComment() {
        if (!pd.isShowing())
            pd.show();
        org.json.JSONObject commentJson = new org.json.JSONObject();
        try {
            commentJson.put("objectTable", "task");
            commentJson.put("objectId", taskID);
            CommonUtils.setCommonJson(TaskDetailActivity.this, commentJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("taskJson", commentJson.toString());
        OkHttpUtils.postString().url(Constant.GETCOMMENTLIST_URL)
                .content(commentJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                             @Override
                             public void onError(Call call, Exception e) {
                                 if (pd.isShowing())
                                     pd.dismiss();
                                 showToastShort("获取评论失败");
                             }

                             @Override
                             public void onResponse(String s) {
                                 Log.e(TAG, s);
                                 if (pd.isShowing())
                                     pd.dismiss();
                                 org.json.JSONObject jObject;
                                 try {
                                     jObject = new org.json.JSONObject(s);
                                     if (jObject.getInt("code") == 1) {
                                         NoticeCommentEntity temp = new Gson().fromJson(s, NoticeCommentEntity.class);
                                         taskCommentList.clear();
                                         taskCommentList.addAll(temp.getData());
                                         Collections.reverse(taskCommentList);
                                         taskcommentListAdapter.notifyDataSetChanged();
                                         for (int i = 0; i < taskCommentList.size(); i++) {
                                             toUserIds = temp.getData().get(i).getUserId() + "," + toUserIds;
                                         }
                                     }
                                 } catch (Exception e) {
                                     e.printStackTrace();
                                 }
                             }
                         }

                );

    }

    private void getTaskDetail() {
        if (!pd.isShowing())
            pd.show();
        org.json.JSONObject jObject = new org.json.JSONObject();
        try {
            jObject.put("id", taskID);
//            Log.e(TAG, jObject.toString());
            CommonUtils.setCommonJson(TaskDetailActivity.this, jObject, PreferenceManager.getInstance().getCurrentUserFlowSId());

            OkHttpUtils.postString().url(URL).content(jObject.toString())
                    .mediaType(MediaType.parse("application/json")).build()
                    .execute(new StringCallback() {

                        @Override
                        public void onError(Call arg0, Exception arg1) {
                            if (pd.isShowing())
                                pd.dismiss();
                            Log.i("task",
                                    "任务详情接口访问失败：：" + arg0.toString()
                                            + arg1.toString());
                        }

                        @Override
                        public void onResponse(String arg0) {
                            Log.e(TAG, arg0);
                            if (pd.isShowing())
                                pd.dismiss();
                            json = JSON.parseObject(arg0);
                            if (json.getIntValue("code") == 1) {
                                if (!json.getString("msg").equals("无数据")) {
                                    //数据的json
                                    JSONObject dataJson = json
                                            .getJSONObject("data");
                                    //附件的json
                                    JSONArray fileLiseJson = json
                                            .getJSONArray("fileList");
                                    TaskFilesList.clear();
                                    if (fileLiseJson.size() != 0) {
                                        ll_taskdetailattach.setVisibility(View.VISIBLE);
                                        //一个个的转化格式，由于一开始就没有用实体来解析。而这个是后来加的，所以转一下省的都要从来弄了。
                                        String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                                        for (int i = 0; i < fileLiseJson.size(); i++) {
                                            NoticeDetailEntity.FileListBean temp = new NoticeDetailEntity.FileListBean();
                                            temp.setFileId(((JSONObject) fileLiseJson.get(i)).getIntValue("fileId"));
                                            temp.setFileName(((JSONObject) fileLiseJson.get(i)).getString("fileName"));
                                            temp.setFileRealPath(((JSONObject) fileLiseJson.get(i)).getString("fileRealPath"));
                                            temp.setFileType(((JSONObject) fileLiseJson.get(i)).getString("fileType"));
                                            temp.setCreateUserId(((JSONObject) fileLiseJson.get(i)).getIntValue("createUserId"));
                                            temp.setCreateTime(((JSONObject) fileLiseJson.get(i)).getString("createTime"));
                                            temp.setCreateUserName(((JSONObject) fileLiseJson.get(i)).getString("createUserName"));
                                            //判断文件是否存在，存在与否需要显示不同的图标，在这儿改变数据源然后在adapter中去弄。
                                            File file = new File(SDPath, temp.getFileName());
                                            //如果文件存在辣么就直接打开，如果不存在那就下载咯！~
                                            if (file.exists()) {
                                                temp.setFileExist(true);
                                            } else {
                                                temp.setFileExist(false);
                                            }
                                            TaskFilesList.add(temp);
                                        }
                                        noticeAttach_adapter.notifyDataSetChanged();
                                    } else {
                                        ll_taskdetailattach.setVisibility(View.GONE);
                                    }
                                    //新添加处理子任务数据与适配器
                                    dealChildTask(json.getJSONArray("childTask"));
                                    if (dataJson.getIntValue("status") == 1) {
                                        if (dataJson.getString("finishTime") != null)
                                            tv_taskStatus.setText("已完成  " + dataJson.getString("finishTime"));
                                        else
                                            tv_taskStatus.setText("已完成");
                                        rl_status.setBackgroundColor(getResources().getColor(R.color.primary_green));
                                        rl_status.setVisibility(View.VISIBLE);
                                        tv_changestatus.setVisibility(View.VISIBLE);
                                        tv_addchildtask.setVisibility(View.GONE);
                                        tv_finish.setVisibility(View.GONE);
                                    } else {
                                        tv_taskStatus.setText("正在进行中...");
                                        rl_status.setBackgroundColor(getResources().getColor(R.color.orange));
                                        rl_status.setVisibility(View.VISIBLE);
                                        tv_changestatus.setVisibility(View.GONE);
                                        tv_addchildtask.setVisibility(View.VISIBLE);
                                        tv_finish.setVisibility(View.VISIBLE);
                                    }
                                    //设置头像
                                    Glide.with(TaskDetailActivity.this)
                                            .load(Constant.GETHEADIMAG_URL
                                                    + dataJson.getInteger("createUserId")
                                                    + ".png").transform(new GlideRoundTransform(TaskDetailActivity.this)).placeholder(R.drawable.ease_default_avatar)
                                            .into(tasldetail_avater);
                                    if (from != null) {
                                        if (from.equals("MeetingDetailActivity"))
                                            taskdetail_createuser.setText("发起人:" + dataJson.getString("headUserName") + " | " + "发起时间:" + dataJson.getString("createTime"));
                                    } else {
                                        taskdetail_createuser.setText("发起人:" + dataJson.getString("createUserName") + " | " + "发起时间:" + dataJson.getString("createTime"));
                                    }
                                    iv_major.setText(dataJson.getString("priorityName"));
                                    try {
                                        iv_major.setTextColor(Color.parseColor(dataJson.getString("priorityColor")));
                                    } catch (Exception e) {
                                        iv_major.setTextColor(getResources().getColor(R.color.common_bottom_bar_normal_bg));
                                        e.printStackTrace();
                                    }

                                    //先把发起者的推送弄进去
                                    toUserIds = dataJson.getString("createUserName");
                                    if (from != null) {
                                        if (from.equals("MeetingDetailActivity")) {
                                            //如果是从会议的议题点进来的，那就该不显示的就不显示
                                            findViewById(R.id.ll_taskdetailcomment).setVisibility(View.GONE);
                                            findViewById(R.id.iv_changetask).setVisibility(View.GONE);
                                            findViewById(R.id.ll_youxianji).setVisibility(View.GONE);
                                            findViewById(R.id.ll_jiezhiriqi).setVisibility(View.GONE);
                                            findViewById(R.id.ll_renwulaiyuan).setVisibility(View.GONE);
                                            findViewById(R.id.ll_renwuleixing).setVisibility(View.GONE);
                                            findViewById(R.id.ll_renwumiaoshu).setVisibility(View.GONE);
                                            findViewById(R.id.ll_renwuchengyuan).setVisibility(View.GONE);
                                            rl_finish.setVisibility(View.GONE);
                                            getMeetingTopicsDetail();
                                        }
                                    } else {
                                        iv_resource.setText(dataJson.getString("source"));
                                        iv_taskType.setText(dataJson.getString("type"));
                                        iv_taskTitle.setText(dataJson.getString("title"));
                                        iv_taskContent.setText(dataJson.getString("content"));
                                        iv_taskDescribe.setText(dataJson.getString("descri"));
                                        iv_limitTime.setText(dataJson.getString("deadline"));
                                        iv_chargePeople.setText(dataJson.getString("headUserName"));
                                        //再获取评论数据
                                        initTaskComment();
                                    }
                                    //设置为全局的JsonArray方便别的地方使用
                                    membersArr = json.getJSONArray("member");
                                    String taskMenmber = "";
                                    for (int i = 0; i < membersArr.size(); i++) {
                                        taskMenmber = taskMenmber
                                                + membersArr.getJSONObject(i).getString(
                                                "userName") + ";";
                                    }
                                    iv_taskMember.setText(taskMenmber);
                                    //将任务成员列表的数据转化为adapter能使用的数据
                                    for (int i = 0; i < membersArr.size(); i++) {
                                        DeptMembersEntity_Person temp = new DeptMembersEntity_Person();
                                        temp.setUserId(membersArr.getJSONObject(i).getInteger(
                                                "userId"));
                                        temp.setUserRealname(membersArr.getJSONObject(i).getString(
                                                "userName"));
                                        persons_list.add(temp);
                                    }
                                    taskmembersAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMeetingTopicsDetail() {
        //现在改为调用接口获取详情内容，然后显示。
        org.json.JSONObject getdetailjson = new org.json.JSONObject();
        issueId = Integer.parseInt(getIntent().getStringExtra("issueID"));
        try {
            getdetailjson.put("formId", 20);
            getdetailjson.put("form_ins_id", issueId);
            CommonUtils.setCommonJson(TaskDetailActivity.this, getdetailjson, PreferenceManager.getInstance().getCurrentUserFlowSId());
//            Log.e(TAG, getdetailjson.toString());
            OkHttpUtils.postString().url(Constant.GETMEETINGDETAILNEW_URL).content(getdetailjson.toString())
                    .mediaType(MediaType.parse("application/json")).build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            showToastShort("获取议题详情失败");
                            Log.e(TAG, "call=" + call.toString() + "//");
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String s) {
                            Log.e(TAG, "s=" + s.toString());
                            try {
                                org.json.JSONObject result = new org.json.JSONObject(s);
                                if (result.getInt("code") == 1) {
                                    MeetingTopics topics = new Gson().fromJson(s, MeetingTopics.class);
                                    iv_taskTitle.setText(topics.getData().get(0).getItem_value_list().get(0).getItem_value());
                                    iv_taskContent.setText(topics.getData().get(0).getItem_value_list().get(1).getItem_value());
                                    //修改显示tip和内容，复用一下。复用的动力就算懒。
                                    ((TextView) findViewById(R.id.iv_chargePeople_label)).setText("发起人部门：");
                                    ((TextView) findViewById(R.id.iv_chargePeople)).setText(topics.getData().get(0).getItem_value_list().get(2).getItem_value());
                                    String fileStr = topics.getData().get(0).getItem_value_list().get(3).getItem_value();
                                    //处理附件字符串
                                    if (!fileStr.equals("")) {
                                        ll_taskdetailattach.setVisibility(View.VISIBLE);
                                        //截取最先和最后一个字符，其实是个[];
                                        fileStr = fileStr.substring(2, fileStr.length() - 2);
                                        Log.e(TAG, fileStr);
                                        String[] fileLists = fileStr.split(",");
                                        for (int i = 0; i < fileLists.length; i++) {
                                            String fileName = fileLists[i].substring(fileLists[i].lastIndexOf("/") + 1);
                                            NoticeDetailEntity.FileListBean temp = new NoticeDetailEntity.FileListBean();
                                            temp.setFileName(fileName);
                                            temp.setFileRealPath(fileLists[i]);
                                            //判断文件是否存在，存在与否需要显示不同的图标，在这儿改变数据源然后在adapter中去弄。
                                            String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                                            File file = new File(SDPath, temp.getFileName());
                                            //如果文件存在辣么就直接打开，如果不存在那就下载咯！~
                                            if (file.exists()) {
                                                temp.setFileExist(true);
                                            } else {
                                                temp.setFileExist(false);
                                            }
                                            TaskFilesList.add(temp);
                                        }
                                        noticeAttach_adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    showToastShort(result.getString("msg"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //修改任务
            case R.id.iv_changetask:
                if (json.getJSONObject("data").getString("createUserId").equals(PreferenceManager.getInstance().getCurrentUserId())) {
                    if (json.getJSONObject("data").getIntValue("status") != 1) {
                        startActivityForResult(new Intent(TaskDetailActivity.this, NewTaskActivity.class).putExtra("from", "TaskDetailActivity_ChangeTask").putExtra("data", json.toString()), Constant.ADD_CHILDTASK_NEEDFRESH);
                    } else {
                        showToastShort("您不可以修改一个已完成的任务");
                    }
                } else {
                    showToastShort("抱歉，您没有该权限");
                }
                break;
            //评论
            case R.id.tv_comment:
                startActivityForResult(new Intent(TaskDetailActivity.this, EditeTaskCommentActivity.class).putExtra("taskid", taskID).putExtra("toUserIds", toUserIds), Constant.CHOOSE_TASKCOMMENT);
                break;
            //添加子任务
            case R.id.tv_addchildtask:
                try {
                    if (json.getJSONObject("data").getString("headUserId").equals(
                            PreferenceManager.getInstance().getCurrentUserId())) {
                        //新建任务功能
                        startActivityForResult(new Intent(TaskDetailActivity.this, NewTaskActivity.class).putExtra("fathertask", json.getJSONObject("data").getInteger("id")).putExtra("from", "TaskDetailActivity_NewChildTask"), Constant.ADD_CHILDTASK_NEEDFRESH);
                    } else {
                        showToastShort("抱歉，您没有该权限");
                    }
                } catch (Exception e) {
                    showToastShort("抱歉，您没有该权限");
                    e.printStackTrace();
                }
                break;
            //完成按钮
            case R.id.tv_finish:
                try {
                    //因为出现过负责人为空的情况，应要求做一个简单的判断，如果为空直接判断是不是创建人。
                    if (json.getJSONObject("data").getString("headUserId") != null) {
                        if (json.getJSONObject("data").getString("headUserId").equals(
                                PreferenceManager.getInstance().getCurrentUserId()) || json.getJSONObject("data").getString("createUserId").equals(
                                PreferenceManager.getInstance().getCurrentUserId())) {
                            //遍历一遍子任务的状态，如果全部完成则完成任务功能
                            int status = 0;
                            for (int i = 0; i < json.getJSONArray("childTask").size(); i++) {
                                ChildTaskEntitiy temp = new Gson().fromJson(json.getJSONArray("childTask").get(i).toString(), ChildTaskEntitiy.class);
                                status = temp.getStatus() + status;
                            }
                            //加的数量为子任务个数
                            if (status == json.getJSONArray("childTask").size()) {
                                finishBtn_OnClick();
                            } else {
                                showToastShort("抱歉，有未完成的子任务");
                            }
                        } else {
                            showToastShort("抱歉，您没有该权限");
                        }
                    } else {
                        if (json.getJSONObject("data").getString("createUserId").equals(
                                PreferenceManager.getInstance().getCurrentUserId())) {
                            //遍历一遍子任务的状态，如果全部完成则完成任务功能
                            int status = 0;
                            for (int i = 0; i < json.getJSONArray("childTask").size(); i++) {
                                ChildTaskEntitiy temp = new Gson().fromJson(json.getJSONArray("childTask").get(i).toString(), ChildTaskEntitiy.class);
                                status = temp.getStatus() + status;
                            }
                            //加的数量为子任务个数
                            if (status == json.getJSONArray("childTask").size()) {
                                finishBtn_OnClick();
                            } else {
                                showToastShort("抱歉，有未完成的子任务");
                            }
                        } else {
                            showToastShort("抱歉，您没有该权限");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToastShort("完成任务失败");
                }
                break;
            //修改状态
            case R.id.tv_changestatus:
                reStartBtn_OnClick();
                break;
            //点击任务成员要能看到人员信息
            case R.id.iv_taskMember:
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        TaskDetailActivity.this);
                builder.setTitle("任务参与人员");
                // 设置一个下拉的列表选择项
                builder.setAdapter(taskmembersAdapter, new DialogInterface.OnClickListener()

                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getApplicationContext(),
                                        DeptMemInfoActivity2.class).putExtra("userId",
                                        membersArr.getJSONObject(which).getString(
                                                "userId")));
                            }
                        }

                );
                builder.show();
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.CHOOSE_TASKCOMMENT:
                    int comment_restatus = data.getIntExtra("needfreshcomment", -1);
                    Log.e(TAG, comment_restatus + "");
                    //如果restatus为1表示添加了评论，则需要刷新
                    if (comment_restatus == 1) {
                        initTaskComment();
                    }
                    break;
                case Constant.ADD_CHILDTASK_NEEDFRESH:
                    isNeedRefresh = true;
                    int childtask_restatus = data.getIntExtra("needfreshchildtask", -1);
                    Log.e(TAG, childtask_restatus + "");
                    //如果restatus为1表示新建了子任务，则需要刷新
                    if (childtask_restatus == 1) {
                        getTaskDetail();
                    }
                    break;
                case Constant.FINISHTHECHILDTASK:
                    int childtask_isfinish = data.getIntExtra("needfinishchildtask", -1);
                    Log.e(TAG, childtask_isfinish + "");
                    //如果restatus为1表示完成了子任务，则需要刷新
                    if (childtask_isfinish == 1) {
                        getTaskDetail();
                    }
                    break;
            }
        }
    }

    private void dealChildTask(JSONArray childTaskArray) {
        if (!(childTaskArray.size() > 0)) {
            ll_taskdetailchildtask.setVisibility(View.GONE);
        } else {
            ll_taskdetailchildtask.setVisibility(View.VISIBLE);
            childTaskList.clear();
            for (int i = 0; i < childTaskArray.size(); i++) {
                ChildTaskEntitiy temp = new Gson().fromJson(childTaskArray.get(i).toString(), ChildTaskEntitiy.class);
                childTaskList.add(temp);
            }
            childTask_adapter.notifyDataSetChanged();
        }
    }

    private void finishBtn_OnClick() {
        new AlertDialog.Builder(TaskDetailActivity.this)
                .setMessage("是否标记任务状态为已完成")
                .setCancelable(true)
                .setPositiveButton("是",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                pd.show();
                                org.json.JSONObject jObject = new org.json.JSONObject();
                                try {
                                    jObject.put("id", taskID);
                                    jObject.put("status", 1);
                                    CommonUtils.setCommonJson(TaskDetailActivity.this, jObject, PreferenceManager.getInstance().getCurrentUserFlowSId());
                                    Log.e(TAG, jObject.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                OkHttpUtils
                                        .postString()
                                        .url(URL_UPDATE_STATUS)
                                        .content(jObject.toString())
                                        .mediaType(
                                                MediaType
                                                        .parse("application/json"))
                                        .build()
                                        .execute(new StringCallback() {

                                            @Override
                                            public void onError(
                                                    Call arg0,
                                                    Exception arg1) {
                                                pd.dismiss();
                                                Log.i("task",
                                                        "修改任务状态error："
                                                                + arg0.toString()
                                                                + "//////////"
                                                                + arg1.toString());

                                            }

                                            @Override
                                            public void onResponse(
                                                    String arg0) {
                                                pd.dismiss();
                                                Log.i("task",
                                                        "修改任务状态："
                                                                + arg0.toString());
                                                JSONObject json = JSON
                                                        .parseObject(arg0);
                                                if (json.getIntValue("code") == 1) {
                                                    if (!json
                                                            .getString(
                                                                    "msg")
                                                            .equals("无数据")) {
                                                        Toast.makeText(
                                                                getApplicationContext(),
                                                                "修改任务状态成功",
                                                                Toast.LENGTH_SHORT)
                                                                .show();
                                                        getTaskDetail();
                                                        isNeedRefresh = true;

                                                    }
                                                }
                                            }

                                        });

                            }
                        })
                .setNegativeButton("否",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).show();
    }

    //修改为未完成状态的接口
    private void reStartBtn_OnClick() {
        new AlertDialog.Builder(TaskDetailActivity.this)
                .setMessage("是否标记任务状态为未完成")
                .setCancelable(true)
                .setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                pd.show();
                                org.json.JSONObject jObject = new org.json.JSONObject();
                                try {
                                    jObject.put("id", taskID);
                                    jObject.put("status", 0);
                                    CommonUtils.setCommonJson(TaskDetailActivity.this, jObject, PreferenceManager.getInstance().getCurrentUserFlowSId());
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                OkHttpUtils
                                        .postString()
                                        .url(URL_UPDATE_STATUS)
                                        .content(jObject.toString())
                                        .mediaType(
                                                MediaType
                                                        .parse("application/json"))
                                        .build()
                                        .execute(new StringCallback() {

                                            @Override
                                            public void onError(
                                                    Call arg0,
                                                    Exception arg1) {
                                                pd.dismiss();
                                                Log.i("task",
                                                        "修改任务状态error："
                                                                + arg0.toString()
                                                                + "//////////"
                                                                + arg1.toString());

                                            }

                                            @Override
                                            public void onResponse(
                                                    String arg0) {
                                                pd.dismiss();
                                                Log.i("task",
                                                        "修改任务状态："
                                                                + arg0.toString());
                                                JSONObject json = JSON
                                                        .parseObject(arg0);
                                                if (json.getIntValue("code") == 1) {
                                                    if (!json
                                                            .getString(
                                                                    "msg")
                                                            .equals("无数据")) {
                                                        Toast.makeText(
                                                                getApplicationContext(),
                                                                "修改任务状态成功",
                                                                Toast.LENGTH_SHORT)
                                                                .show();
                                                        getTaskDetail();
                                                        isNeedRefresh = true;

                                                    }
                                                }
                                            }

                                        });

                            }
                        })
                .setNegativeButton("否",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).show();
    }

    public void back(View view) {
        if (isNeedRefresh) {
            setResult(
                    RESULT_OK,
                    new Intent()
                            .putExtra("needfinishchildtask",
                                    1));
        } else
            setResult(RESULT_CANCELED);
        finish();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isNeedRefresh)
                setResult(
                        RESULT_OK,
                        new Intent()
                                .putExtra("needfinishchildtask",
                                        1));
            else
                setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    //内部类
    public class Notice_CommentListAdapter extends BaseAdapter {
        private List<NoticeCommentEntity.DataBean> mData;
        private Context mCt;
        private LayoutInflater inflater;
        ViewBundle viewBundle;

        public Notice_CommentListAdapter(ArrayList<NoticeCommentEntity.DataBean> data,
                                         Context ct) {
            mData = data;
            mCt = ct;
            inflater = (LayoutInflater) mCt
                    .getSystemService(mCt.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int arg0) {
            return mData.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int arg0, View convertView, ViewGroup arg2) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_notice_comment, null);
                viewBundle = new ViewBundle();
                viewBundle.noticecomment_avatar = (ImageView) convertView
                        .findViewById(R.id.noticecomment_avatar);
                viewBundle.noticecomment_name = (TextView) convertView
                        .findViewById(R.id.noticecomment_name);
                viewBundle.noticecomment_content = (TextView) convertView
                        .findViewById(R.id.noticecomment_content);
                viewBundle.noticecomment_time = (TextView) convertView
                        .findViewById(R.id.noticecomment_time);
                viewBundle.noticecomment_reply = (TextView) convertView
                        .findViewById(R.id.noticecomment_reply);
                convertView.setTag(viewBundle);
            } else {
                viewBundle = (ViewBundle) convertView.getTag();
            }
            final int p = arg0;
            viewBundle.noticecomment_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (String.valueOf(((NoticeCommentEntity.DataBean) taskcommentListAdapter.getItem(p)).getUserId()).equals(PreferenceManager.getInstance().getCurrentUserId()))
                        showToastShort("您不能回复自己");
                    else {
                        recommentdata = (NoticeCommentEntity.DataBean) taskcommentListAdapter.getItem(p);
                        startActivityForResult(new Intent(TaskDetailActivity.this, EditeTaskCommentActivity.class).putExtra("taskid", taskID).putExtra("toUserIds", recommentdata.getUserId()).putExtra("recomment", recommentdata), Constant.CHOOSE_TASKCOMMENT);
                    }

                }
            });

            Glide.with(mCt)
                    .load(Constant.GETHEADIMAG_URL
                            + mData.get(arg0).getUserId()
                            + ".png").transform(new GlideRoundTransform(mCt)).placeholder(com.ding.easeui.R.drawable.ease_default_avatar)
                    .into(viewBundle.noticecomment_avatar);
            try {
                if (!mData.get(arg0)
                        .getUserRealname().equals("")) {
                    viewBundle.noticecomment_name.setText(mData.get(arg0)
                            .getUserRealname());
                } else {
                    viewBundle.noticecomment_name.setText("");
                }
            } catch (Exception e) {
                viewBundle.noticecomment_name.setText("");
            }
            try {
                if (!mData.get(arg0)
                        .getContent().equals("")) {
                    viewBundle.noticecomment_content.setText(mData.get(arg0)
                            .getContent());
                } else {
                    viewBundle.noticecomment_content.setText("");
                }
            } catch (Exception e) {
                viewBundle.noticecomment_content.setText("");
            }
            try {
                if (!mData.get(arg0)
                        .getCreateTime().equals("")) {
                    viewBundle.noticecomment_time.setText(mData.get(arg0)
                            .getCreateTime());
                } else {
                    viewBundle.noticecomment_time.setText("");
                }
            } catch (Exception e) {
                viewBundle.noticecomment_time.setText("");
            }
            return convertView;
        }

        private class ViewBundle {
            TextView noticecomment_reply;
            ImageView noticecomment_avatar;
            TextView noticecomment_name;
            TextView noticecomment_content;
            TextView noticecomment_time;
        }

    }
}
