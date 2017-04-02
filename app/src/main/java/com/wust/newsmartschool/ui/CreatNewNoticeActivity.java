package com.wust.newsmartschool.ui;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wust.newsmartschool.DemoApplication;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.NoticeAttach_Adapter;
import com.wust.newsmartschool.domain.NoticeDetailEntity;
import com.wust.newsmartschool.domain.UpdataonResponse;
import com.wust.newsmartschool.domain.UserInfoEntity;
import com.wust.newsmartschool.views.ListViewForScrollView;
import com.wust.easeui.utils.PreferenceManager;
import com.wust.newsmartschool.views.ECProgressDialog;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.CommonUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CreatNewNoticeActivity extends BaseActivity implements OnClickListener {
    String TAG = "CreatNewNoticeActivity_DeBugs";
    private TextView btn_creatnotice_finish;
    private EditText edite_creat_notice_title;
    private EditText edite_creat_notice_content;
    private ImageView notice_updatafile;
    private LinearLayout ll_creatnotice_sentpeople;
    boolean edite_creat_notice_title_havecontent;
    boolean edite_creat_notice_content_havecontent;
    private TextView tv_noticechoose_members;
    //选中的返回的人员id和部门id，此处没有做去重处理
    String pickeduserid;
    String pickedgroupid;
    ECProgressDialog progressDialog;
    //新建通知中的deptType参数所需要的东东
    String getCallType;
    //新建通知选择的附件
    File upnoticefile;
    List<File> Files;
    UpdataonResponse updataonResponse;
    //新建通知参数里的fileids
    JSONArray noticeAttachList;
    //选中需要上传文件显示的listview与实体与适配器
    ListViewForScrollView newnotice_attachlistview;
    NoticeAttach_Adapter newnotice_attachadapter;
    List<NoticeDetailEntity.FileListBean> AttachListEntitytoadapter;
    //上传附件的bar
    ProgressDialog mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_new_notice);
        noticeAttachList = new JSONArray();
        Files = new ArrayList<>();
        //初始化上传附件的那个bar
        mProgressBar = new ProgressDialog(CreatNewNoticeActivity.this);
        mProgressBar.setMessage("正在上传附件文件...");
        mProgressBar.setCancelable(true);
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressBar.setProgress(0);
        mProgressBar.setMax(100);
        AttachListEntitytoadapter = new ArrayList<>();
        pickeduserid = "";
        pickedgroupid = "";
        btn_creatnotice_finish = (TextView) findViewById(R.id.btn_creatnotice_finish);
        newnotice_attachlistview = (ListViewForScrollView) findViewById(R.id.newnotice_attachlist);
        edite_creat_notice_title = (EditText) findViewById(R.id.edite_creat_notice_title);
        edite_creat_notice_content = (EditText) findViewById(R.id.edite_creat_notice_content);
        ll_creatnotice_sentpeople = (LinearLayout) findViewById(R.id.ll_creatnotice_sentpeople);
        tv_noticechoose_members = (TextView) findViewById(R.id.tv_noticechoose_members);
        notice_updatafile = (ImageView) findViewById(R.id.notice_updatafile);
        newnotice_attachadapter = new NoticeAttach_Adapter(CreatNewNoticeActivity.this, AttachListEntitytoadapter);
        newnotice_attachlistview.setAdapter(newnotice_attachadapter);
        edite_creat_notice_title_havecontent = false;
        edite_creat_notice_content_havecontent = false;
        btn_creatnotice_finish.setOnClickListener(this);
        ll_creatnotice_sentpeople.setOnClickListener(this);
        notice_updatafile.setOnClickListener(this);
        progressDialog = new ECProgressDialog(CreatNewNoticeActivity.this);
        //判断是否有内容，如果有内容则填充，如果无则表示是新建。内容的有无是根据是否是从通知详情转发跳转过来的
        try {
            if (getIntent().getStringExtra("forwading_title") != null) {
                edite_creat_notice_title.setText("转发：" + getIntent().getStringExtra("forwading_title"));
                btn_creatnotice_finish.setVisibility(View.VISIBLE);
                edite_creat_notice_title_havecontent = true;
            }
        } catch (NullPointerException e) {
            edite_creat_notice_title.setText("");

        }
        try {
            if (getIntent().getStringExtra("forwading_content") != null) {
                edite_creat_notice_content.setText("转发：" + getIntent().getStringExtra("forwading_content"));
                btn_creatnotice_finish.setVisibility(View.VISIBLE);
                edite_creat_notice_content_havecontent = true;
            }
        } catch (NullPointerException e) {
            edite_creat_notice_content.setText("");

        }

        edite_creat_notice_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, s + "_edite_creat_notice_title");
                if (s.length() != 0) {
                    edite_creat_notice_title_havecontent = true;

                } else {
                    edite_creat_notice_title_havecontent = false;
                }
                if (edite_creat_notice_content_havecontent == true
                        && edite_creat_notice_title_havecontent == true) {
                    btn_creatnotice_finish.setVisibility(View.VISIBLE);

                } else {
                    btn_creatnotice_finish.setVisibility(View.INVISIBLE);
                }

            }
        });
        edite_creat_notice_content.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    edite_creat_notice_content_havecontent = true;

                } else {
                    edite_creat_notice_content_havecontent = false;
                }
                if (edite_creat_notice_content_havecontent == true
                        && edite_creat_notice_title_havecontent == true) {
                    btn_creatnotice_finish.setVisibility(View.VISIBLE);

                } else {
                    btn_creatnotice_finish.setVisibility(View.INVISIBLE);
                }

            }
        });

        newnotice_attachlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File beclickfile = Files.get(position);
                CommonUtils.openFileUnknowType(CreatNewNoticeActivity.this, beclickfile);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_creatnotice_finish:
                // 怼发送通知接口
                if (!(tv_noticechoose_members.equals("请选择人员"))) {
                    progressDialog.show();
                    try {
                        SentNotice();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showToastShort("请选择通知人员");
                }
                break;
            case R.id.ll_creatnotice_sentpeople:
                // 回调选人界面的函数
                startActivityForResult((new Intent(CreatNewNoticeActivity.this,
                                NoticeChooseMemActivitySingleChoose.class)),
                        Constant.CHOOSE_NOTICEPEOPLE);
                break;
            //选择附件
            case R.id.notice_updatafile:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
                            Constant.FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(CreatNewNoticeActivity.this, "请安装文件管理器", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }


    private void SentNotice() throws JSONException {
        JSONArray pickeduseridJsonArr = new JSONArray();
        JSONArray pickedgroupidJsondArr = new JSONArray();
        if (!pickeduserid.equals("")) {
            //按接口需求整理一下传参的参数格式
            String pickeduseridArr[] = pickeduserid.split(",");
            //将数组一个个填充到JSONArray中来
            for (int i = 0; i < pickeduseridArr.length; i++) {
                int tempadd = Integer.parseInt(pickeduseridArr[i]);
                pickeduseridJsonArr.put(tempadd);
            }
        }
        if (!pickedgroupid.equals("")) {
            String pickedgroupidArr[] = pickedgroupid.split(",");
            //将数组一个个填充到JSONArray中来
            for (int i = 0; i < pickedgroupidArr.length; i++) {
                int tempadd = Integer.parseInt(pickedgroupidArr[i]);
                pickedgroupidJsondArr.put(tempadd);
            }
        }

        JSONObject noticeJson = new JSONObject();
        noticeJson.put("send", PreferenceManager.getInstance()
                .getCurrentUserId());
        noticeJson.put("acceptUser", pickeduseridJsonArr);
        noticeJson.put("acceptDept", pickedgroupidJsondArr);
        noticeJson.put("deptType", getCallType.toString());
        noticeJson.put("title", edite_creat_notice_title.getText().toString());
        noticeJson.put("content", edite_creat_notice_content.getText()
                .toString());
        noticeJson.put("fileIds", noticeAttachList);
        CommonUtils.setCommonJson(CreatNewNoticeActivity.this, noticeJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.e(TAG, noticeJson.toString());

        OkHttpUtils.postString().url(Constant.SENTNOTICE_URL)
                .content(noticeJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        Log.i(TAG, arg0);
                        progressDialog.dismiss();
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1) {
                                Toast.makeText(CreatNewNoticeActivity.this, "发送成功",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (jObject.getInt("code") == 5) {
                                Toast.makeText(CreatNewNoticeActivity.this, R.string.common_no_power,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                showToastShort(jObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            showToastShort("请求失败");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        progressDialog.dismiss();
                        Log.i(TAG, arg0.toString() + "/*/" + arg1.toString());
                        Toast.makeText(getApplicationContext(), "发送失败",
                                Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.CHOOSE_NOTICEPEOPLE:// 添加群成员
                    // 有的接口要电话，有的接口要id，所以都返回回来了。全局变量的自取所需。
                    String newmembers = data.getStringExtra("members_name");
                    String group_name = data.getStringExtra("group_name");
                    pickeduserid = data.getStringExtra("members_userid");
                    pickedgroupid = data.getStringExtra("group_userid");
                    getCallType = data.getStringExtra("deptType");
                    //如果发送的人中有自己的id或者说有自己的部门id，辣么回去的时候应该刷新一下。否则在收到的通知列表中就看不到这个通知了
                    if (pickeduserid.contains(PreferenceManager.getInstance().getCurrentUserId()) || pickedgroupid.contains(((UserInfoEntity) DemoApplication.getInstance().mCache
                            .getAsObject(Constant.MY_KEY_USERINFO)).getData().getDepartmentId() + "")) {
                        setResult(RESULT_OK);
                    }
                    String displayText = "";
                    //变为数组
                    if (!group_name.equals("")) {
                        String[] group_namearray = group_name.split(",");
                        for (int i = 0; i < group_namearray.length; i++) {
                            displayText = "[" + group_namearray[i] + "]," + displayText;
                        }
                    }
                    displayText = displayText + newmembers;
                    tv_noticechoose_members.setText(displayText);

                    Log.e("CHOOSE_NOTICEPEOPLE", "newmembers/" + newmembers);
                    Log.e("CHOOSE_NOTICEPEOPLE", "membersuserid/" + pickeduserid);
                    Log.e("CHOOSE_NOTICEPEOPLE", "group_id/" + pickedgroupid);
                    Log.e("CHOOSE_NOTICEPEOPLE", "group_name/" + group_name);
                    break;
                case Constant.FILE_SELECT_CODE:
                    Uri uri = data.getData();
                    Log.e(TAG, uri.toString());
                    try {
                        upnoticefile = new File(CommonUtils.FileUtilsgetPathUp4p4(CreatNewNoticeActivity.this, uri));
                        UpdataAttach(upnoticefile);
                        progressDialog.show();
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

    public void UpdataAttach(File upmeetingfile) throws JSONException {
        OkHttpUtils.post().url(Constant.UPDATAATTACH_URL).addParams("userId", PreferenceManager.getInstance().getCurrentUserId().toString()).addFile("file1", upmeetingfile.getName(), upmeetingfile).build()
                .connTimeOut(20000)
                .readTimeOut(20000)
                .writeTimeOut(20000).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Log.i("UpdataAttach", call.toString() + "/*/" + e.toString());
                showToastShort("上传附件失败");
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(String s) {
                Log.i("UpdataAttach", s);
                progressDialog.dismiss();
                try {
                    org.json.JSONObject response = new org.json.JSONObject(s);
                    if (response.getInt("code") == 1) {
                        showToastShort("上传附件成功");
                        updataonResponse = new Gson().fromJson(s, UpdataonResponse.class);
                        String temp = updataonResponse.getData().getFilesList().get(0).getFileName();
                        //存在接口里需要的noticeAttachList
                        noticeAttachList.put(Integer.valueOf(updataonResponse.getData().getFilesIds().replaceAll(",", "")));
                        NoticeDetailEntity.FileListBean tempattach = new NoticeDetailEntity.FileListBean();
                        tempattach.setFileName(temp);
                        AttachListEntitytoadapter.add(tempattach);
                        newnotice_attachadapter.notifyDataSetChanged();
                        //上传成功并且listview更新之后再加到这个List里面去，保证了上传了的一定在这个List里面
                        Files.add(upnoticefile);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                } catch (NullPointerException e) {
                    showToastShort("请求失败");
                    e.printStackTrace();
                    return;
                } catch (Exception e) {
                    showToastShort("请求失败");
                    e.printStackTrace();
                }

            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                progressDialog.dismiss();
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

    public void back(View view) {
        finish();
        if (android.os.Build.VERSION.SDK_INT > 5) {
            overridePendingTransition(
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }

}
