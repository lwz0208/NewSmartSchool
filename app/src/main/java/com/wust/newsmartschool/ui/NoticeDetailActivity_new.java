package com.wust.newsmartschool.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.NoticeAttach_Adapter;
import com.wust.newsmartschool.domain.NoticeCommentEntity;
import com.wust.newsmartschool.domain.NoticeDetailEntity;
import com.wust.newsmartschool.domain.NoticeDetail_newEntity;
import com.wust.newsmartschool.domain.NoticeEntity;
import com.wust.newsmartschool.utils.appUseUtils;
import com.wust.newsmartschool.views.ECProgressDialog;
import com.wust.newsmartschool.views.ListViewForScrollView;
import com.wust.easeui.utils.PreferenceManager;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.CommonUtils;
import com.wust.easeui.widget.GlideRoundTransform;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class NoticeDetailActivity_new extends BaseActivity implements OnClickListener {
    String TAG = "NoticeDetailActivity_DeBugs";
    /**
     * 用户信息相关控件
     */
    private TextView noticedetail_title;
    private TextView noticedetail_time;
    private TextView noticedetail_publisher;
    private TextView noticedetail_content;
    private ImageView noticedetail_avatar;
    private TextView noticedetail_userdept;

    /**
     * 评论相关控件
     */
    private EditText noticecomment_edittext;
    private Button btn_surecomment;
    /**
     * 接受从列表点击传进来的通知信息实体
     */
    NoticeEntity beclicknotice;

    /**
     * 评论的数据实体和评论的适配器
     */
    LinearLayout noticeattach_download;
    //已看未看相关
    private TextView noseenoticemembers_tv;
    private TextView seenoticemembers_tv;
    private LinearLayout seenoticemembers_ll;
    private LinearLayout noseenoticemembers_ll;
    private ArrayList<NoticeCommentEntity.DataBean> commentEntities;
    private Notice_CommentListAdapter commentListAdapter;
    //评论展现列表
    private ListViewForScrollView noticedetail_comment;
    //附件展现列表
    private ListViewForScrollView noticedetail_attach;
    /**
     * 通知详情实体，和附件实体。由于以前接口本来就是拼接的，现在只是把他们分开。所以完全可以用一个实体。
     */
    NoticeDetailEntity noticeDetailEntity;
    NoticeDetailEntity noticeDetailfileListBean;
    NoticeDetail_newEntity noticeDetail_newEntity;
    ArrayList<NoticeDetailEntity.FileListBean> noticeDetailfileListBeanList;
    NoticeAttach_Adapter noticeAttach_adapter;
    ECProgressDialog pd;
    //从哪儿点过来的，从我发送的地方点过来的不需要表示已读未读。
    String ItemFrom;
    //几个下载附件相关的东东
    ProgressDialog mProgressBar;
    OkHttpClient mOkHttpClient;
    Handler mHandler;
    //点击ListView里的一个Item
    NoticeDetailEntity.FileListBean BeClickItem;
    //列表传过来的通知ID
    String informId;
    //评论使用的
    NoticeCommentEntity.DataBean commentbeclickuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail_new);
        //下载附件提示框的一些设置
        mProgressBar = new ProgressDialog(NoticeDetailActivity_new.this);
        mProgressBar.setMessage("正在下载附件文件...");
        mProgressBar.setCancelable(true);
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressBar.setProgress(0);
        mProgressBar.setMax(100);
        mOkHttpClient = new OkHttpClient();
        pd = new ECProgressDialog(NoticeDetailActivity_new.this);
        pd.show();
        noticeDetailEntity = new NoticeDetailEntity();
        noticeDetailfileListBeanList = new ArrayList<>();
        noticeDetailfileListBean = new NoticeDetailEntity();
        noticeDetail_newEntity = new NoticeDetail_newEntity();
        noseenoticemembers_tv = (TextView) findViewById(R.id.noseenoticemembers);
        seenoticemembers_tv = (TextView) findViewById(R.id.seenoticemembers);
        noseenoticemembers_ll = (LinearLayout) findViewById(R.id.noseenoticemembers_ll);
        seenoticemembers_ll = (LinearLayout) findViewById(R.id.seenoticemembers_ll);
        noticedetail_avatar = (ImageView) findViewById(R.id.noticedetail_avatar);
        noticeattach_download = (LinearLayout) findViewById(R.id.noticeattach_download);
        noticedetail_title = (TextView) findViewById(R.id.noticedetail_title);
        noticedetail_userdept = (TextView) findViewById(R.id.noticedetail_userdept);
        noticedetail_time = (TextView) findViewById(R.id.noticedetail_time);
        noticedetail_content = (TextView) findViewById(R.id.noticedetail_content);
        noticedetail_comment = (ListViewForScrollView) findViewById(R.id.noticedetail_comment);
        noticedetail_attach = (ListViewForScrollView) findViewById(R.id.noticedetail_attach);
        noticecomment_edittext = (EditText) findViewById(R.id.noticecomment_edittext);
        noticedetail_publisher = (TextView) findViewById(R.id.noticedetail_publisher);
        btn_surecomment = (Button) findViewById(R.id.btn_surecomment);
        btn_surecomment.setOnClickListener(this);
        seenoticemembers_ll.setOnClickListener(this);
        noseenoticemembers_ll.setOnClickListener(this);
        btn_surecomment.setEnabled(false);
        commentEntities = new ArrayList<>();
        Intent intent = getIntent();
        ItemFrom = intent.getStringExtra("ItemFrom");
        informId = intent.getStringExtra("KEY_CLICK_NOTICE");
        if (informId == null) {
            informId = "";
        }
        if (ItemFrom != null) {
            //如果是从收到的通知点击过来的，其他地方均不访问已读接口
            if (ItemFrom.equals("WorkFragNoticeActivity")) {
                //如果是未读（status=0）将通知标记已读并且反馈给服务器
                try {
                    readTheNotice();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }
        commentListAdapter = new Notice_CommentListAdapter(commentEntities, NoticeDetailActivity_new.this);
        noticedetail_comment.setAdapter(commentListAdapter);
        noticeAttach_adapter = new NoticeAttach_Adapter(NoticeDetailActivity_new.this, noticeDetailfileListBeanList);
        noticedetail_attach.setAdapter(noticeAttach_adapter);
        //或者这条消息的已读未读的人
        try {
            getnoticedetailAttach();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        noticecomment_edittext.setImeOptions(EditorInfo.IME_ACTION_SEND);
        noticecomment_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                             @Override
                                                             public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                                 if (actionId == EditorInfo.IME_ACTION_SEND) {
                                                                     pd.show();
                                                                     sentCommentContent();
                                                                 }
                                                                 return true;
                                                             }
                                                         }

        );
        noticecomment_edittext.addTextChangedListener(new TextWatcher() {

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
                                                                  btn_surecomment.setEnabled(true);

                                                              } else {
                                                                  btn_surecomment.setEnabled(false);
                                                              }

                                                          }
                                                      }

        );

        //评论列表的点击事件
        noticedetail_comment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                            String userId = String.valueOf(((NoticeCommentEntity.DataBean) commentListAdapter.getItem(position)).getUserId());
                                                            startActivity(new Intent(getApplicationContext(),
                                                                    DeptMemInfoActivity2.class).putExtra("userId",
                                                                    userId));
                                                            if (android.os.Build.VERSION.SDK_INT > 5) {
                                                                overridePendingTransition(R.anim.slide_in_from_right,
                                                                        R.anim.slide_out_to_left);
                                                            }

                                                        }
                                                    }

        );


        noticedetail_attach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int p = position;
                BeClickItem = (NoticeDetailEntity.FileListBean) noticeAttach_adapter.getItem(position);
                if (BeClickItem != null) {
                    String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    File file = new File(SDPath, BeClickItem.getFileName().toString());
                    //如果文件存在辣么就直接打开，如果不存在那就下载咯！~
                    if (file.exists()) {
                        CommonUtils.openFile(NoticeDetailActivity_new.this, file, BeClickItem.getFileType().toString());
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
                                        msg.arg1 = progress;
                                        msg.arg2 = p;
                                        mHandler.sendMessage(msg);
                                    }
                                    fos.flush();
                                    Log.d("h_bl", "文件下载成功");
                                    mProgressBar.dismiss();
                                    CommonUtils.openFile(NoticeDetailActivity_new.this, file, BeClickItem.getFileType().toString());
//                            CommonUtils.openFile(NoticeDetailActivity.this, file, "");


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
                            noticeDetailfileListBeanList.get(msg.arg2).setFileExist(true);
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


    private void readTheNotice() throws JSONException {
        JSONObject readJson = new JSONObject();
        readJson.put("informId", informId);
        readJson.put("accept", PreferenceManager.getInstance()
                .getCurrentUserId());
        readJson.put("status", "1");
        CommonUtils.setCommonJson(NoticeDetailActivity_new.this, readJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i("noticeJson", readJson.toString());

        OkHttpUtils.postString().url(Constant.READTHENOTICE_URL)
                .content(readJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
//                        Log.e(TAG, arg0);
                        //通知-1操作
                        appUseUtils.GetMyUnreadMumAndJPush(NoticeDetailActivity_new.this);
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {

                    }
                });
    }

    private void getWhoSeeWhoNoSee() throws JSONException {
        JSONObject whoJson = new JSONObject();
        whoJson.put("id", informId);
        CommonUtils.setCommonJson(NoticeDetailActivity_new.this, whoJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i("noticeJson", whoJson.toString());

        OkHttpUtils.postString().url(Constant.GETNOTICEDETAIL_URL)
                .content(whoJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        Log.e(TAG, arg0.toString());
                        pd.dismiss();
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1) {
                                noticeDetailEntity = new Gson().fromJson(arg0, NoticeDetailEntity.class);
                                String Allsee = noticeDetailEntity.getData().getAccept();
                                String[] AllseeArray = Allsee.split(",");
                                if (noticeDetailEntity.getData().getAccepted() != null) {
                                    String saw = noticeDetailEntity.getData().getAccepted();
                                    String[] SawArray = saw.split(",");
                                    seenoticemembers_tv.setText("已看(" + SawArray.length + ")");
                                    noseenoticemembers_tv.setText("未看(" + (AllseeArray.length - SawArray.length) + ")");
                                } else {
                                    seenoticemembers_tv.setText("已看(0)");
                                    noseenoticemembers_tv.setText("未看(" + AllseeArray.length + ")");
                                }
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "数据获取失败", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "数据获取失败",
                                Toast.LENGTH_SHORT).show();

                    }
                });

    }

    //获得改通知的附件
    private void getnoticedetailAttach() throws JSONException {
        JSONObject whoJson = new JSONObject();
        whoJson.put("id", informId);
        CommonUtils.setCommonJson(NoticeDetailActivity_new.this, whoJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i("noticeJson", whoJson.toString());

        OkHttpUtils.postString().url(Constant.GETNOTICEDETAIL_NEW_URL)
                .content(whoJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        //这个完了之后再请求评论数据
                        try {
                            getCommentData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e(TAG, arg0.toString());
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1) {
                                noticeDetail_newEntity = new Gson().fromJson(arg0, NoticeDetail_newEntity.class);

                                beclicknotice = noticeDetail_newEntity.getData();
                                //设置头像
                                Glide.with(NoticeDetailActivity_new.this)
                                        .load(Constant.GETHEADIMAG_URL
                                                + beclicknotice.getSend()
                                                + ".png").transform(new GlideRoundTransform(NoticeDetailActivity_new.this)).placeholder(R.drawable.ease_default_avatar)
                                        .into(noticedetail_avatar);
                                if (noticeDetail_newEntity.getFileList().size() != 0) {
                                    noticeattach_download.setVisibility(View.VISIBLE);
                                    //判断文件是否存在，存在与否需要显示不同的图标，在这儿改变数据源然后在adapter中去弄。
                                    String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                                    for (int i = 0; i < noticeDetail_newEntity.getFileList().size(); i++) {
                                        File file = new File(SDPath, noticeDetail_newEntity.getFileList().get(i).getFileName());
                                        //如果文件存在辣么就直接打开，如果不存在那就下载咯！~
                                        if (file.exists()) {
                                            noticeDetail_newEntity.getFileList().get(i).setFileExist(true);
                                        } else {
                                            noticeDetail_newEntity.getFileList().get(i).setFileExist(false);
                                        }
                                    }
                                    noticeDetailfileListBeanList.addAll(noticeDetail_newEntity.getFileList());
                                    noticeAttach_adapter.notifyDataSetChanged();

                                }
                                if (beclicknotice != null) {
                                    noticedetail_userdept.setText(beclicknotice.getDepartmentName());
                                    noticedetail_title.setText(beclicknotice.getTitle());
                                    noticedetail_time.setText(beclicknotice.getCreateTime());
                                    noticedetail_content.setText(beclicknotice.getContent());
                                    noticedetail_publisher.setText(beclicknotice.getUserRealname());
                                }

                            } else if (jObject.getInt("code") == 2) {
                                noticeattach_download.setVisibility(View.GONE);
                            } else {
                                noticeattach_download.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),
                                        "数据获取失败", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        pd.dismiss();
                        noticeattach_download.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "数据获取失败",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_surecomment:
                if (btn_surecomment.isEnabled()) {
                    pd.show();
                    sentCommentContent();
                } else {
                    showToastShort("请输入...");
                }
                break;
            case R.id.seenoticemembers_ll:
                startActivityForResult(new Intent(NoticeDetailActivity_new.this, NoticeDetail_SeeNoSeeMemActivity.class).putExtra("noticeid", beclicknotice.getInformId()).putExtra("status", 1), Constant.RecallSeeNoticedetailNum);
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    overridePendingTransition(R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);
                }
                break;
            case R.id.noseenoticemembers_ll:
                startActivityForResult(new Intent(NoticeDetailActivity_new.this, NoticeDetail_SeeNoSeeMemActivity.class).putExtra("noticeid", beclicknotice.getInformId()).putExtra("status", 0), Constant.RecallNoSeeNoticedetailNum);
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    overridePendingTransition(R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.RecallSeeNoticedetailNum:
                    int returnNum_See = data.getIntExtra("returnNum", -1);
                    if (returnNum_See != -1) {
                        seenoticemembers_tv.setText("已看(" + returnNum_See + ")");
                    }
                    break;
                case Constant.RecallNoSeeNoticedetailNum:
                    int returnNum_nosee = data.getIntExtra("returnNum", -1);
                    if (returnNum_nosee != -1) {
                        noseenoticemembers_tv.setText("未看(" + returnNum_nosee + ")");
                    }
                    break;
            }
        }
    }

    //发布评论
    private void sentCommentContent() {
        JSONObject commentJson = new JSONObject();
        String AcceptPhone = noticeDetailEntity.getData().getAccept();
        try {
            commentJson.put("userId", PreferenceManager.getInstance().getCurrentUserId());
            commentJson.put("userRealname", PreferenceManager.getInstance().getCurrentRealName());
            try {
                commentJson.put("toUserName", commentbeclickuser.getUserRealname());
                commentJson.put("toUserId", commentbeclickuser.getUserId());
                commentJson.put("pid", commentbeclickuser.getCommentId());
                commentJson.put("content", "回复" + commentbeclickuser.getUserRealname() + ":" + noticecomment_edittext.getText().toString());
                commentJson.put("toUserIds", commentbeclickuser.getUserId());
            } catch (Exception e) {
                commentJson.put("toUserName", "");
                commentJson.put("toUserId", "");
                commentJson.put("pid", 0);
                commentJson.put("content", noticecomment_edittext.getText().toString());
                commentJson.put("toUserIds", AcceptPhone);
            }
            commentJson.put("objectId", informId);
            commentJson.put("objectTable", "inform");


            CommonUtils.setCommonJson(NoticeDetailActivity_new.this, commentJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("commentJson", commentJson.toString());

        OkHttpUtils.postString().url(Constant.EDITCOMMENT_URL)
                .content(commentJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                             @Override
                             public void onError(Call call, Exception e) {
                                 pd.dismiss();
                                 showToastShort("发表评论失败");
                             }

                             @Override
                             public void onResponse(String s) {
                                 Log.e(TAG, s);
                                 JSONObject jObject;
                                 try {
                                     jObject = new JSONObject(s);
                                     if (jObject.getInt("code") == 1) {
                                         //发表成功之后清空编辑框
                                         noticecomment_edittext.getText().clear();
                                         getCommentData();
                                     }
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }
                             }
                         }
                );
    }


    //获得评论的数据
    private void getCommentData() throws JSONException {
        JSONObject commentJson = new JSONObject();
        commentJson.put("objectTable", "inform");
        commentJson.put("objectId", informId);
        CommonUtils.setCommonJson(NoticeDetailActivity_new.this, commentJson, PreferenceManager.getInstance().getCurrentUserFlowSId());

        Log.i("commentJson", commentJson.toString());

        OkHttpUtils.postString().url(Constant.GETCOMMENTLIST_URL)
                .content(commentJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                             @Override
                             public void onError(Call call, Exception e) {
                                 pd.dismiss();
                                 showToastShort("获取评论失败");
                             }

                             @Override
                             public void onResponse(String s) {
                                 try {
                                     getWhoSeeWhoNoSee();
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }
                                 Log.i(TAG, s);
                                 JSONObject jObject;
                                 try {
                                     jObject = new JSONObject(s);
                                     if (jObject.getInt("code") == 1) {
                                         NoticeCommentEntity temp = new Gson().fromJson(s, NoticeCommentEntity.class);
                                         commentEntities.clear();
                                         commentEntities.addAll(temp.getData());
                                         Collections.reverse(commentEntities);
                                         /**如果为刷新进入,切换到评论，并且刷新一下数据*/
                                         commentListAdapter.notifyDataSetChanged();
                                     }
                                 } catch (Exception e) {
                                     e.printStackTrace();
                                 }
                             }
                         }

                );

    }


    //转发的函数
    public void forward_notice(View view) {
        startActivity(new Intent(NoticeDetailActivity_new.this, CreatNewNoticeActivity.class).putExtra("forwading_title", noticedetail_title.getText().toString()).putExtra("forwading_content", noticedetail_content.getText().toString()));
        if (android.os.Build.VERSION.SDK_INT > 5) {
            overridePendingTransition(R.anim.slide_in_from_right,
                    R.anim.slide_out_to_left);
        }
    }

    public void back(View view) {
        finish();
        if (android.os.Build.VERSION.SDK_INT > 5) {
            overridePendingTransition(
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }

    //内部内，因为要用子Item的点击事件所以放在这儿。
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
            //评论Item里的“回复”的点击事件
            final int p = arg0;
            viewBundle.noticecomment_reply.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (String.valueOf(((NoticeCommentEntity.DataBean) commentListAdapter.getItem(p)).getUserId()).equals(PreferenceManager.getInstance().getCurrentUserId()))
                        showToastShort("您不能回复自己");
                    else {
                        commentbeclickuser = ((NoticeCommentEntity.DataBean) commentListAdapter.getItem(p));
                        //让编辑框弹出来，并显示对谁进行评论
                        noticecomment_edittext.setFocusable(true);
                        noticecomment_edittext.setFocusableInTouchMode(true);
                        noticecomment_edittext.requestFocus();
                        noticecomment_edittext.setHint("回复给" + commentbeclickuser.getUserRealname() + ":");
                        //打开软键盘
                        InputMethodManager imm = (InputMethodManager) NoticeDetailActivity_new.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            });

            Glide.with(mCt)
                    .load(Constant.GETHEADIMAG_URL
                            + mData.get(arg0).getUserId()
                            + ".png").transform(new GlideRoundTransform(mCt)).placeholder(com.wust.easeui.R.drawable.ease_default_avatar)
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
