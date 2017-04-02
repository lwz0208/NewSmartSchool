package com.ding.chat.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ding.chat.R;
import com.ding.chat.adapter.NoticeAttach_Adapter;
import com.ding.chat.domain.NoticeDetailEntity;
import com.ding.chat.domain.UpdataonResponse;
import com.ding.chat.views.ECProgressDialog;
import com.ding.chat.views.ListViewForScrollView;
import com.ding.chat.widget.MyTimePickerDialog;
import com.ding.easeui.Constant;
import com.ding.easeui.utils.CommonUtils;
import com.ding.easeui.utils.PreferenceManager;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;

public class NewMeetingActivity extends BaseActivity implements View.OnClickListener {
    String TAG = "NewMeetingActivity_Debugs";

    private final static String URL = Constant.CREATMEETING_URL;
    private TextView iv_title;
    private TextView tv_startTime;
    private TextView tv_meetingMember;
    private TextView tv_topic;
    private LinearLayout new_meetingrl_finish;
    private ImageView updatafile;
    private RelativeLayout rl_startTime;
    private RelativeLayout rl_meetingMember;
    private RelativeLayout rl_topic;
    private EditText et_place;
    private List<myMeeting> meetingList;
    private String[] items = null;
    private boolean[] isSelected = null;
    private ListViewForScrollView listViewForScrollView;
    List<Map<String, Object>> data;
    private MyAdapter adapter;

    private ECProgressDialog pd;

    String membersphone;
    String membersuserid;
    int meetingType = -1;
    //新建会议选择的附件
    File upmeetingfile;
    UpdataonResponse updataonResponse;
    //新建通知参数里的fileids
    String meetingAttachList = "";
    //选中需要上传文件显示的listview与实体与适配器
    ListViewForScrollView newmeeting_attachlistview;
    NoticeAttach_Adapter newmeeting_attachadapter;
    List<NoticeDetailEntity.FileListBean> AttachListEntitytoadapter;
    //选中的文件List
    List<File> updataFiles;
    //上传附件的bar
    ProgressDialog mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);
        //初始化上传附件的那个bar
        mProgressBar = new ProgressDialog(NewMeetingActivity.this);
        mProgressBar.setMessage("正在上传附件文件...");
        mProgressBar.setCancelable(true);
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressBar.setProgress(0);
        mProgressBar.setMax(100);
        AttachListEntitytoadapter = new ArrayList<>();
        pd = new ECProgressDialog(NewMeetingActivity.this);
        newmeeting_attachlistview = (ListViewForScrollView) findViewById(R.id.newmeeting_attachlistview);
        newmeeting_attachadapter = new NoticeAttach_Adapter(NewMeetingActivity.this, AttachListEntitytoadapter);
        newmeeting_attachlistview.setAdapter(newmeeting_attachadapter);
        rl_startTime = (RelativeLayout) findViewById(R.id.rl_startTime);
        rl_topic = (RelativeLayout) findViewById(R.id.rl_topic);
        rl_meetingMember = (RelativeLayout) findViewById(R.id.rl_meetingMember);
        tv_startTime = (TextView) findViewById(R.id.tv_startTime);
        tv_meetingMember = (TextView) findViewById(R.id.tv_meetingMember);
        tv_topic = (TextView) findViewById(R.id.tv_topic);
        new_meetingrl_finish = (LinearLayout) findViewById(R.id.new_meetingrl_finish);
        updatafile = (ImageView) findViewById(R.id.updatafile);
        iv_title = (TextView) findViewById(R.id.creat_meetting_title);
        iv_title.setText("创建" + getIntent().getStringExtra("meetingType"));
        listViewForScrollView = (ListViewForScrollView) findViewById(R.id.listViewForScrollView);

        if (getIntent().getStringExtra("meetingType").equals("普通会议")) {
            meetingType = 1;
            rl_topic.setVisibility(View.GONE);
        } else {
            meetingType = 2;
            rl_topic.setVisibility(View.VISIBLE);
        }
        data = new ArrayList<>();
        adapter = new MyAdapter(NewMeetingActivity.this, data);
        listViewForScrollView.setAdapter(adapter);

        et_place = (EditText) findViewById(R.id.et_place);
        meetingList = new ArrayList<>();
        updataFiles = new ArrayList<>();
        rl_startTime.setOnClickListener(this);
        rl_topic.setOnClickListener(this);
        rl_meetingMember.setOnClickListener(this);
        new_meetingrl_finish.setOnClickListener(this);
        updatafile.setOnClickListener(this);
        try {
            initMeetingTopic();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        newmeeting_attachlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File beclickfile = updataFiles.get(position);
                CommonUtils.openFileUnknowType(NewMeetingActivity.this, beclickfile);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_startTime:
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(NewMeetingActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                openTimerPicker(year, monthOfYear + 1, dayOfMonth);
                            }
                        }, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.rl_topic:
                if (items == null) {
                    items = new String[meetingList.size()];
                    isSelected = new boolean[meetingList.size()];
                    for (int i = 0; i < meetingList.size(); i++) {
                        items[i] = meetingList.get(i).getContent();
                        isSelected[i] = false;
                    }
                } else {
                    for (int i = 0; i < meetingList.size(); i++) {
                        isSelected[i] = false;
                    }
                }

                if (meetingList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "暂无可选的议题", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewMeetingActivity.this);
                    builder.setTitle("会议议题");
                    builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            isSelected[which] = isChecked;
                        }
                    });

                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (isSelected.length != 0) {
                                data.clear();
                                for (int i = 0; i < isSelected.length; i++) {
                                    if (isSelected[i]) {
                                        tv_topic.setText("");
                                        Map<String, Object> value = new HashMap<>();
                                        value.put("title", meetingList.get(i).getTitle());
                                        value.put("content", meetingList.get(i).getContent());
                                        data.add(value);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }
                break;
            case R.id.rl_meetingMember:
                startActivityForResult((new Intent(NewMeetingActivity.this,
                                NoticeChooseMemActivitySingleChooseForTask.class)),
                        Constant.CHOOSE_NOTICEPEOPLE);
                break;
            case R.id.new_meetingrl_finish:
                if (meetingType != -1)
                    creatmeetingSwitchType(meetingType);
                break;
            //选择附件
            case R.id.updatafile:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
                            Constant.FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(NewMeetingActivity.this, "请安装文件管理器", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    private void openTimerPicker(final int year,
                                 final int monthOfYear, final int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        new MyTimePickerDialog(NewMeetingActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tv_startTime.setTextColor(getResources().getColor(R.color.common_bottom_bar_normal_bg));
                tv_startTime.setText(year + "-" + monthOfYear
                        + "-" + dayOfMonth + " " + hourOfDay + ":" + minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();

    }


    private void creatmeetingSwitchType(int meetingType) {
        if (isCanCommit()) {

            pd.show();
            org.json.JSONObject jObject = new org.json.JSONObject();
            membersuserid = membersuserid.substring(0, membersuserid.length() - 1);
            try {
                jObject.put("userId", PreferenceManager.getInstance().getCurrentUserId());
                jObject.put("startTime", tv_startTime.getText().toString());
                jObject.put("address", et_place.getText().toString());
                jObject.put("fileIds", meetingAttachList);
                jObject.put("userIds", membersuserid);
                CommonUtils.setCommonJson(NewMeetingActivity.this, jObject, PreferenceManager.getInstance().getCurrentUserFlowSId());
                if (meetingType == 2) {
                    jObject.put("type", meetingType);
                    jObject.put("title", "院办会议");
                    jObject.put("content", "院办会议");
                    //院办会议可以选择议题
                    String s = "";
                    try {
                        for (int i = 0; i < meetingList.size(); i++)
                            if (isSelected[i]) {
                                s = s + meetingList.get(i).getId() + ",";
                                jObject.put("taskIds", s.substring(0, s.length() - 1));
                            }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.i("meeting", jObject.toString());
                    OkHttpUtils.postString().url(Constant.CREATYBMEETING_URL).content(jObject.toString())
                            .mediaType(MediaType.parse("application/json")).build()
                            .execute(new StringCallback() {
                                @Override
                                public void onResponse(String arg0) {
                                    pd.dismiss();
                                    // 这里就解析返回的arg0，注意是String类型的需要类型转换。
                                    Log.i("meeting", "新建会议接口访问成功返回结果：" + arg0.toString());
                                    JSONObject json = JSON.parseObject(arg0);
                                    if (json.getIntValue("code") == 1) {
                                        Toast.makeText(getApplicationContext(), "新建会议成功", Toast.LENGTH_SHORT).show();
                                        setResult(RESULT_OK);
                                        finish();
                                    } else if (json.getIntValue("code") == 5) {
                                        Toast.makeText(getApplicationContext(), R.string.common_no_power, Toast.LENGTH_SHORT).show();
                                    } else {
                                        showToastShort(json.getString("msg"));
                                    }
                                }

                                @Override
                                public void onError(Call arg0, Exception arg1) {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), "新建会议失败", Toast.LENGTH_SHORT).show();
                                    Log.i("meeting", "新建会议接口失败返回结果：" + arg0.toString() + "////////////////" + arg1.toString());
                                }
                            });
                } else {
                    jObject.put("type", meetingType);
                    jObject.put("title", "普通会议");
                    jObject.put("content", "普通会议");
                    Log.i("meeting", jObject.toString());
                    OkHttpUtils.postString().url(URL).content(jObject.toString())
                            .mediaType(MediaType.parse("application/json")).build()
                            .execute(new StringCallback() {
                                @Override
                                public void onResponse(String arg0) {
                                    pd.dismiss();
                                    // 这里就解析返回的arg0，注意是String类型的需要类型转换。
                                    Log.i("meeting", "新建会议接口访问成功返回结果：" + arg0.toString());
                                    JSONObject json = JSON.parseObject(arg0);
                                    if (json.getIntValue("code") == 1) {
                                        Toast.makeText(getApplicationContext(), "新建会议成功", Toast.LENGTH_SHORT).show();
                                        setResult(RESULT_OK);
                                        finish();
                                    } else if (json.getIntValue("code") == 5) {
                                        Toast.makeText(getApplicationContext(), R.string.common_no_power, Toast.LENGTH_SHORT).show();
                                    } else {
                                        showToastShort(json.getString("msg"));
                                    }
                                }

                                @Override
                                public void onError(Call arg0, Exception arg1) {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), "新建会议失败", Toast.LENGTH_SHORT).show();
                                    Log.i("meeting", "新建会议接口失败返回结果：" + arg0.toString() + "////////////////" + arg1.toString());
                                }
                            });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private void initMeetingTopic() throws JSONException {
        org.json.JSONObject jObject = new org.json.JSONObject();
        CommonUtils.setCommonJson(NewMeetingActivity.this, jObject, PreferenceManager.getInstance().getCurrentUserFlowSId());
        OkHttpUtils.postString().url(Constant.GETMEETINGTASK_URL).content(jObject.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        // TODO Auto-generated method stub
                        Log.i("meeting", "获取会议议题接口失败返回结果：" + arg0.toString() + " :    " + arg1.toString());
                    }

                    @Override
                    public void onResponse(String arg0) {
                        // TODO Auto-generated method stub
                        Log.i("meeting", "获取会议议题接口成功返回结果：" + arg0);
                        JSONObject json = JSON.parseObject(arg0);
                        if (json.getIntValue("code") == 1) {
                            if (!json.getString("msg").equals("无数据")) {
                                JSONArray array = json.getJSONArray("data");
                                for (int i = 0; i < array.size(); i++) {
                                    myMeeting meeting = new myMeeting(
                                            array.getJSONObject(i).getIntValue(
                                                    "id"), array.getJSONObject(
                                            i).getString("title"), array.getJSONObject(
                                            i).getString("content"), array.getJSONObject(
                                            i).getString("headUserName"));
                                    meetingList.add(meeting);
                                }
                                isSelected = new boolean[meetingList.size()];
                            }
                        }
                    }
                });
    }

    class myMeeting {
        int id;
        String title;
        String content;
        String headUserName;

        public myMeeting(int id, String title, String content, String headUserName) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.headUserName = headUserName;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getHeadUserName() {
            return headUserName;
        }
    }

    public void back(View view) {
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.CHOOSE_NOTICEPEOPLE:// 添加群成员
                    String newmembers = data.getStringExtra("members_name");
                    // 有的接口要电话，有的接口要id，所以都返回回来了。全局变量的自取所需。
                    membersphone = data.getStringExtra("members_phone");
                    membersuserid = data.getStringExtra("members_userid");
                    tv_meetingMember.setText(newmembers);
                    break;
                case Constant.FILE_SELECT_CODE:
                    Uri uri = data.getData();
                    try {
                        upmeetingfile = new File(CommonUtils.FileUtilsgetPathUp4p4(NewMeetingActivity.this, uri));
                        UpdataAttach(upmeetingfile);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        showToastShort("该文件不存在");
                    }
                    break;
            }
        }
    }

    public void UpdataAttach(File upfile) throws JSONException {
        OkHttpUtils.post().url(Constant.UPDATAATTACH_URL).addParams("userId", PreferenceManager.getInstance().getCurrentUserId().toString()).addFile("file1", upmeetingfile.getName(), upfile).build().connTimeOut(20000)
                .readTimeOut(20000)
                .writeTimeOut(20000).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Log.i("UpdataAttach", call.toString() + "/*/" + e.toString());
                showToastShort("上传附件失败");
            }

            @Override
            public void onResponse(String s) {
                Log.i("UpdataAttach", s);
                try {
                    org.json.JSONObject response = new org.json.JSONObject(s);
                    if (response.getInt("code") == 1) {
                        showToastShort("上传附件成功");
                        updataonResponse = new Gson().fromJson(s, UpdataonResponse.class);
                        String tempFileName = updataonResponse.getData().getFilesList().get(0).getFileName();
                        int tempFildid = updataonResponse.getData().getFilesList().get(0).getFileId();
                        meetingAttachList = tempFildid + "," + meetingAttachList;
                        NoticeDetailEntity.FileListBean tempattach = new NoticeDetailEntity.FileListBean();
                        tempattach.setFileName(tempFileName);
                        AttachListEntitytoadapter.add(tempattach);
                        newmeeting_attachadapter.notifyDataSetChanged();
                        updataFiles.add(upmeetingfile);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
            }

            @Override
            public void inProgress(float progress) {
                super.inProgress(progress);
                Log.e(TAG, progress + "");
                mProgressBar.show();
                mProgressBar.setProgress((int) (progress * 100));
            }

            @Override
            public void onAfter() {
                super.onAfter();
                mProgressBar.dismiss();

            }
        });

    }

    private boolean isCanCommit() {
        if (tv_startTime.getText().equals("请选择时间")) {
            Toast.makeText(getApplicationContext(), "请选择会议时间", Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_place.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "请输入会议地点", Toast.LENGTH_SHORT).show();
            return false;
        } else if (tv_meetingMember.getText().equals("选择成员")) {
            Toast.makeText(getApplicationContext(), "请选择会议成员", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    class MyAdapter extends BaseAdapter {
        private Context context;
        private List<Map<String, Object>> list;
        private LayoutInflater listContainer;

        private class ViewHolder {
            public TextView tv_meetingTitle;
            public TextView tv_meetingStartTime;
        }

        public MyAdapter(Context context, List<Map<String, Object>> listItems) {
            this.context = context;
            listContainer = LayoutInflater.from(context);
            this.list = listItems;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder myViewHolder = null;
            if (convertView == null) {
                myViewHolder = new ViewHolder();
                convertView = listContainer.inflate(R.layout.meeting_item, null);
                myViewHolder.tv_meetingTitle = (TextView) convertView.findViewById(R.id.tv_meetingTitle);
                myViewHolder.tv_meetingStartTime = (TextView) convertView.findViewById(R.id.tv_meetingStartTime);
                convertView.setTag(myViewHolder);
            } else {
                myViewHolder = (ViewHolder) convertView.getTag();
            }

            myViewHolder.tv_meetingTitle.setText((String) list.get(position).get("title"));
            myViewHolder.tv_meetingStartTime.setText((String) list.get(position).get("content"));

            return convertView;
        }
    }
}
