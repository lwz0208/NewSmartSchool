package com.ding.chat.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ding.chat.R;
import com.ding.chat.domain.MeetingTopics;
import com.ding.chat.domain.NoticeDetailEntity;
import com.ding.chat.utils.appUseUtils;
import com.ding.chat.views.ECProgressDialog;
import com.ding.chat.views.ListViewForScrollView;
import com.ding.easeui.Constant;
import com.ding.easeui.utils.CommonUtils;
import com.ding.easeui.utils.PreferenceManager;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

public class MeetingDetailActivity extends BaseActivity {
    String TAG = "MeetingDetailActivity_Debugs";
    private final static String URL = Constant.GETMEETINGDETAIL_URL;
    private final static String URL_UPDATE_STATUS = Constant.CHANGEMEETINGSTATUS;
    private final static String URL_FINISH_TOPICTASK = Constant.URL_FINISH_TOPICTASK;
    private ListViewForScrollView lv_topic;
    private SimpleAdapter adapter;
    private TextView tv_add_task;
    private TextView tv_meeting_finish;
    private TextView tv_startTime;
    private TextView tv_place;
    private TextView tv_meetingMember;
    private ImageView meetingfiles;
    private int meetingID = -1;
    List<Map<String, Object>> data;
    List<myMeetingTopic> meetingTopicList;
    private LinearLayout ll_bottom;
    private LinearLayout ll_bottom_finish;
    private RelativeLayout rl_meetingPoint;
    private String tag = "";
    //接口中新加的附件List
    List<NoticeDetailEntity.FileListBean> meetingFilesList;
    private final int resultCode_POINT = 1;
    //转动圈
    ECProgressDialog pd;
    //标识是未开始还是已完结会议
    int meetingStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail);

        lv_topic = (ListViewForScrollView) findViewById(R.id.lv_topic);
        data = new ArrayList<>();
        meetingFilesList = new ArrayList<>();
        meetingTopicList = new ArrayList<>();
        pd = new ECProgressDialog(MeetingDetailActivity.this);
        pd.show();
        adapter = new SimpleAdapter(this, data, R.layout.topic_item,
                new String[]{"title", "content"}, new int[]{
                R.id.tv_topicTitle, R.id.tv_topicContent});
        lv_topic.setAdapter(adapter);
        lv_topic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MeetingDetailActivity.this, TaskDetailActivity.class);
                intent.putExtra("taskID", meetingTopicList.get(position).getTaskId() + "");
                intent.putExtra("from", "MeetingDetailActivity");
                intent.putExtra("issueID", meetingTopicList.get(position).getIssueId() + "");
                startActivity(intent);

            }
        });

        meetingfiles = (ImageView) findViewById(R.id.meetingfiles);
        tv_add_task = (TextView) findViewById(R.id.tv_add_task);
        tv_meeting_finish = (TextView) findViewById(R.id.tv_meeting_finish);
        tv_startTime = (TextView) findViewById(R.id.tv_startTime);
        tv_place = (TextView) findViewById(R.id.tv_place);
        tv_meetingMember = (TextView) findViewById(R.id.tv_meetingMember);

        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        ll_bottom_finish = (LinearLayout) findViewById(R.id.ll_bottom_finish);
        rl_meetingPoint = (RelativeLayout) findViewById(R.id.rl_meetingPoint);
//meetingType标记的是0未开始与1已完结
        if (getIntent().getStringExtra("meetingStatus").equals("1")) {
            meetingStatus = 1;
            ll_bottom_finish.setVisibility(View.GONE);
            ll_bottom.setVisibility(View.VISIBLE);
            rl_meetingPoint.setVisibility(View.VISIBLE);
        }

        if (getIntent().getStringExtra("meetingtitle") != null) {
            String title = getIntent().getStringExtra("meetingtitle");
            ((TextView) findViewById(R.id.creat_meeting_title)).setText(title);
            if (title.equals("普通会议")) {
                ((TextView) findViewById(R.id.iv_receive)).setVisibility(View.GONE);
            }
        }
        meetingfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, meetingFilesList.size() + "");
                if (meetingFilesList.size() == 0) {
                    showToastShort("该会议没有附件");
                    return;
                }
                //复用一下通知附件列表界面
                startActivity(new Intent(MeetingDetailActivity.this, NoticeAttachActivity.class).putExtra("noticeattachlist", (Serializable) meetingFilesList));

            }
        });

        meetingID = Integer.parseInt(getIntent().getStringExtra("meetingID"));

        org.json.JSONObject jObject = new org.json.JSONObject();
        try {
            jObject.put("meetingId", meetingID);
            CommonUtils.setCommonJson(MeetingDetailActivity.this, jObject, PreferenceManager.getInstance().getCurrentUserFlowSId());

            Log.e(TAG, jObject.toString());

            OkHttpUtils.postString().url(URL).content(jObject.toString())
                    .mediaType(MediaType.parse("application/json")).build()
                    .execute(new StringCallback() {

                        @Override
                        public void onError(Call call, Exception e) {
                            pd.dismiss();
                            showToastShort("会议详情获取失败");
                            Log.i("meeting", "会议接口访问失败：" + call.toString() + e.toString());
                        }

                        @Override
                        public void onResponse(String s) {
                            pd.dismiss();
                            Log.i("meeting", "会议详情接口访问成功：" + s.toString());
                            JSONObject json = JSON.parseObject(s);
                            if (json.getIntValue("code") == 1 && json.getString("msg").equals("请求成功")) {
                                JSONObject meetingDetailJSONObject = json.getJSONObject("data");
                                JSONArray meetingMemberArray = meetingDetailJSONObject.getJSONArray("userList");
                                tv_startTime.setText(meetingDetailJSONObject.getString("startTime"));
                                tv_place.setText(meetingDetailJSONObject.getString("address"));
                                tag = meetingDetailJSONObject.getString("tag");
//                            Log.i("meeting", "tag:" + tag);

                                //返回回来的附件字段
                                if (meetingDetailJSONObject.getJSONArray("fileList").size() > 0) {
                                    for (int i = 0; i < meetingDetailJSONObject.getJSONArray("fileList").size(); i++) {
                                        NoticeDetailEntity.FileListBean temp = new NoticeDetailEntity.FileListBean();
                                        temp.setFileType(((JSONObject) (meetingDetailJSONObject.getJSONArray("fileList").get(i))).getString("type"));
                                        temp.setFileRealPath(((JSONObject) (meetingDetailJSONObject.getJSONArray("fileList").get(i))).getString("path"));
                                        temp.setFileName(((JSONObject) (meetingDetailJSONObject.getJSONArray("fileList").get(i))).getString("name"));
                                        temp.setFileId(((JSONObject) (meetingDetailJSONObject.getJSONArray("fileList").get(i))).getInteger("id"));
                                        meetingFilesList.add(temp);
                                    }
                                }
                                String meetingMember = "";

                                for (int i = 0; i < meetingMemberArray.size(); i++) {
                                    meetingMember = meetingMember + meetingMemberArray.getJSONObject(i).getString("userName") + " ";
                                }
                                tv_meetingMember.setText(meetingMember);

                                data.clear();
                                JSONArray meetingIssueArray = meetingDetailJSONObject.getJSONArray("taskList");
                                for (int i = 0; i < meetingIssueArray.size(); i++) {
                                    Map<String, Object> value = new HashMap<>();
                                    value.put("title", meetingIssueArray.getJSONObject(i).getString("title"));
                                    value.put("content", meetingIssueArray.getJSONObject(i).getString("content"));
                                    data.add(value);

                                    myMeetingTopic myMeetingTopic = new myMeetingTopic();
                                    myMeetingTopic.setTitle(meetingIssueArray.getJSONObject(i).getString("title"));
                                    myMeetingTopic.setContent(meetingIssueArray.getJSONObject(i).getString("content"));
                                    myMeetingTopic.setCreateTime(meetingIssueArray.getJSONObject(i).getString("createTime"));
                                    myMeetingTopic.setStatus(meetingIssueArray.getJSONObject(i).getIntValue("status"));
                                    myMeetingTopic.setTaskId(meetingIssueArray.getJSONObject(i).getIntValue("taskId"));
                                    myMeetingTopic.setIssueId(meetingIssueArray.getJSONObject(i).getIntValue("issueId"));
                                    meetingTopicList.add(myMeetingTopic);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        rl_meetingPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeetingDetailActivity.this, MeetingPointActivity.class);
                intent.putExtra("meetingID", meetingID);
                intent.putExtra("startTime", tv_startTime.getText().toString());
                intent.putExtra("meetingPlace", tv_place.getText().toString());
                intent.putExtra("meetingMember", tv_meetingMember.getText().toString());
                if (tag == null)
                    tag = "";
                intent.putExtra("tag", tag);
                startActivityForResult(intent, 0);
            }
        });

        tv_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MeetingDetailActivity.this, NewTaskActivity.class));
            }
        });

        tv_meeting_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                org.json.JSONObject jObject = new org.json.JSONObject();
                try {
                    jObject.put("meetingId", meetingID);
                    CommonUtils.setCommonJson(MeetingDetailActivity.this, jObject, PreferenceManager.getInstance().getCurrentUserFlowSId());
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                OkHttpUtils.postString().url(URL_UPDATE_STATUS).content(jObject.toString())
                        .mediaType(MediaType.parse("application/json")).build()
                        .execute(new StringCallback() {

                            @Override
                            public void onError(Call call, Exception e) {
                                Log.i("meeting", "改变会议状态接口访问失败：" + call.toString() + e.toString());
                            }

                            @Override
                            public void onResponse(String s) {
                                Log.i("meeting", "改变会议状态接口访问成功：" + s.toString());
                                try {
                                    org.json.JSONObject jObject = new org.json.JSONObject(s);
                                    if (jObject.getInt("code") == 1) {
                                        Toast.makeText(getApplicationContext(), "结束会议成功", Toast.LENGTH_SHORT).show();
                                        ll_bottom_finish.setVisibility(View.INVISIBLE);
                                        ll_bottom.setVisibility(View.VISIBLE);
                                        rl_meetingPoint.setVisibility(View.VISIBLE);
                                        FinishTopicMeeting();
                                        setResult(RESULT_OK);
                                        //会议-1操作
                                        appUseUtils.GetMyUnreadMumAndJPush(MeetingDetailActivity.this);
                                    } else if (jObject.getInt("code") == 5) {
                                        Toast.makeText(MeetingDetailActivity.this, R.string.common_no_power,
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        showToastShort(jObject.getString("msg"));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });
    }

    public void back(View view) {
        finish();
    }

    private void FinishTopicMeeting() {
        org.json.JSONObject jsonObject = new org.json.JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < meetingTopicList.size(); i++)
            jsonArray.add(i, meetingTopicList.get(i).getTaskId());
        try {
            jsonObject.put("ids", jsonArray.toString());
            CommonUtils.setCommonJson(MeetingDetailActivity.this, jsonObject, PreferenceManager.getInstance().getCurrentUserFlowSId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpUtils.postString().url(URL_FINISH_TOPICTASK).content(jsonObject.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e) {
                        Log.i("meeting", "议题任务完成接口访问失败：" + call.toString() + e.toString());
                    }

                    @Override
                    public void onResponse(String s) {
                        Log.i("meeting", "议题任务完成接口访问成功：" + s.toString());
                        JSONObject json = JSON.parseObject(s);
                        if (json.getIntValue("code") == 1) {
                            Log.i("meeting", json.getString("msg") + "---" + json.getString("data"));
                        }
                    }
                });
    }

    class myMeetingTopic {
        String title;
        int status;
        String createTime;
        String content;
        int taskId;
        int issueId;

        public int getIssueId() {
            return issueId;
        }

        public void setIssueId(int issueId) {
            this.issueId = issueId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getTaskId() {
            return taskId;
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case resultCode_POINT:
                tag = data.getStringExtra("point");
                break;
        }
    }
}
