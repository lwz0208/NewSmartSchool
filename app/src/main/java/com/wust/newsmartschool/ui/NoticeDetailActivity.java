package com.wust.newsmartschool.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.Notice_CommentListAdapter;
import com.wust.newsmartschool.adapter.Notice_SeeListAdapter;
import com.wust.newsmartschool.domain.NoticeCommentEntity;
import com.wust.newsmartschool.domain.NoticeDetailEntity;
import com.wust.newsmartschool.domain.NoticeEntity;
import com.wust.newsmartschool.utils.appUseUtils;
import com.wust.newsmartschool.views.ECProgressDialog;
import com.wust.easeui.utils.PreferenceManager;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.CommonUtils;
import com.wust.easeui.widget.GlideRoundTransform;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class NoticeDetailActivity extends BaseActivity implements OnClickListener {
    String TAG = "NoticeDetailActivity_DeBugs";
    private TextView noticedetail_title;
    private TextView noticedetail_time;
    private TextView noticedetail_publisher;
    private TextView noticedetail_content;
    private LinearLayout noticeattach_dowmload;
    private ImageView noticedetail_avatar;
    private LinearLayout noticedetail_commenttextview;
    private ImageView noticedetail_commenttextview_bottomview;
    private LinearLayout noticedetail_nosee;
    private ImageView noticedetail_nosee_bottomview;
    private LinearLayout noticedetail_see;
    private ImageView noticedetail_see_bottomview;
    private EditText noticecomment_edittext;
    private Button btn_surecomment;
    private TextView seeAttach_Num;
    NoticeEntity beclicknotice;
    private TextView noticedetail_userdept;
    //评论的实体和评论的适配器
    private ArrayList<NoticeCommentEntity.DataBean> commentEntities;
    private Notice_CommentListAdapter commentListAdapter;
    private ListView noticedetail_comment;
    //已看未看的两个TextView
    private ArrayList<String> whoseeList;
    private TextView tv_see;
    private TextView tv_nosee;
    private Notice_SeeListAdapter whoseeListAdapter;
    //接口返回的几个东东
    NoticeDetailEntity noticeDetailEntity;
    String AcceptedPhone;
    String AcceptPhone;
    //附件是否为空，初始化为空（否）。
    boolean fileListisEmptity = false;
    ECProgressDialog pd;
    //表示当前的条条是在那一个
    /**
     * 1:评论/2：未看/3.已看
     **/
    int status;
    //表示当前刷新是因为评论刷新还是因为第一次进入刷新
    /**
     * 0：评论/1:第一次进入
     **/
    int FLAG = 1;
    //从哪儿点过来的，从我发送的地方点过来的不需要表示已读未读。
    String ItemFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        tv_see = (TextView) findViewById(R.id.tv_see);
        tv_nosee = (TextView) findViewById(R.id.tv_nosee);
        pd = new ECProgressDialog(NoticeDetailActivity.this);
        pd.show();
        noticeDetailEntity = new NoticeDetailEntity();
        noticedetail_avatar = (ImageView) findViewById(R.id.noticedetail_avatar);
        noticeattach_dowmload = (LinearLayout) findViewById(R.id.noticeattach_dowmload);
        noticedetail_title = (TextView) findViewById(R.id.noticedetail_title);
        noticedetail_userdept = (TextView) findViewById(R.id.noticedetail_userdept);
        noticedetail_time = (TextView) findViewById(R.id.noticedetail_time);
        seeAttach_Num = (TextView) findViewById(R.id.seeAttach_Num);
        noticedetail_content = (TextView) findViewById(R.id.noticedetail_content);
        noticedetail_comment = (ListView) findViewById(R.id.noticedetail_comment);
        noticedetail_commenttextview = (LinearLayout) findViewById(R.id.noticedetail_commenttextview);
        noticedetail_commenttextview_bottomview = (ImageView) findViewById(R.id.noticedetail_commenttextview_bottomview);
        noticedetail_nosee = (LinearLayout) findViewById(R.id.noticedetail_nosee);
        noticedetail_nosee_bottomview = (ImageView) findViewById(R.id.noticedetail_nosee_bottomview);
        noticedetail_see = (LinearLayout) findViewById(R.id.noticedetail_see);
        noticedetail_see_bottomview = (ImageView) findViewById(R.id.noticedetail_see_bottomview);
        noticecomment_edittext = (EditText) findViewById(R.id.noticecomment_edittext);
        noticedetail_publisher = (TextView) findViewById(R.id.noticedetail_publisher);
        btn_surecomment = (Button) findViewById(R.id.btn_surecomment);
        noticedetail_commenttextview.setOnClickListener(this);
        noticedetail_nosee.setOnClickListener(this);
        noticedetail_see.setOnClickListener(this);
        btn_surecomment.setOnClickListener(this);
        btn_surecomment.setEnabled(false);
        commentEntities = new ArrayList<>();
        whoseeList = new ArrayList<>();
        Intent intent = getIntent();
        beclicknotice = (NoticeEntity) intent
                .getSerializableExtra("KEY_CLICK_NOTICE");
        ItemFrom = intent.getStringExtra("ItemFrom");
        //设置头像
        Glide.with(NoticeDetailActivity.this)
                .load(Constant.GETHEADIMAG_URL
                        + beclicknotice.getSend()
                        + ".png").transform(new GlideRoundTransform(this)).placeholder(R.drawable.ease_default_avatar)
                .into(noticedetail_avatar);
        noticedetail_userdept.setText(beclicknotice.getDepartmentName());
        if (beclicknotice != null) {
            noticedetail_title.setText(beclicknotice.getTitle());
            noticedetail_time.setText(beclicknotice.getCreateTime());
            noticedetail_content.setText(beclicknotice.getContent());
            noticedetail_publisher.setText(beclicknotice.getUserRealname());
        }
        if (ItemFrom != null) {
            //如果是从收到的通知点击过来的，其他地方均不表示已读未读
            if (ItemFrom.equals("WorkFragNoticeActivity")) {
                //如果是未读（status=0）将通知标记已读并且反馈给服务器
                if (beclicknotice.getStatus() == 0) {
                    try {
                        readTheNotice();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        //或者这条消息的已读未读的人
        try {
            getWhoSeeWhoNoSee();
            getCommentData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        whoseeListAdapter = new Notice_SeeListAdapter(whoseeList,
                this);
        noticedetail_comment.setAdapter(whoseeListAdapter);
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
        noticecomment_edittext.addTextChangedListener(new

                                                              TextWatcher() {

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
        //下面那列表的点击事件
        noticedetail_comment.setOnItemClickListener(new AdapterView.OnItemClickListener()

                                                    {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                            if (status == 1) {
                                                                String beclickuserId = ((NoticeCommentEntity.DataBean) commentListAdapter.getItem(position)).getUserId() + "";
                                                                Log.e(TAG, beclickuserId + "/" + position);
                                                                startActivity(new Intent(NoticeDetailActivity.this,
                                                                        DeptMemInfoActivity.class).putExtra("userId", beclickuserId));
                                                            } else {
                                                                startActivity(new Intent(NoticeDetailActivity.this,
                                                                        DeptMemInfoActivity.class).putExtra("userId",
                                                                        whoseeListAdapter.getItem(position)));
                                                            }
                                                        }
                                                    }

        );
        //附件下载的按钮
        noticeattach_dowmload.setOnClickListener(new OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         if (!fileListisEmptity) {
                                                             showToastShort("该通知没有附件");
                                                             return;
                                                         }
                                                         startActivity(new Intent(NoticeDetailActivity.this, NoticeAttachActivity.class).putExtra("noticeattachlist", (Serializable) noticeDetailEntity.getFileList()));
                                                     }
                                                 }

        );

    }

    private void readTheNotice() throws JSONException {
        JSONObject readJson = new JSONObject();
        readJson.put("informId", beclicknotice.getInformId());
        readJson.put("accept", PreferenceManager.getInstance()
                .getCurrentUserId());
        readJson.put("status", "1");
        CommonUtils.setCommonJson(NoticeDetailActivity.this, readJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i("noticeJson", readJson.toString());

        OkHttpUtils.postString().url(Constant.READTHENOTICE_URL)
                .content(readJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        //通知-1操作
                        appUseUtils.GetMyUnreadMumAndJPush(NoticeDetailActivity.this);
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {

                    }
                });
    }

    private void getWhoSeeWhoNoSee() throws JSONException {
        JSONObject whoJson = new JSONObject();
        whoJson.put("id", beclicknotice.getInformId());
        CommonUtils.setCommonJson(NoticeDetailActivity.this, whoJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i("noticeJson", whoJson.toString());

        OkHttpUtils.postString().url(Constant.GETNOTICEDETAIL_URL)
                .content(whoJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        Log.e(TAG, arg0.toString());
//                        pd.dismiss();
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1) {
                                if (jObject.getJSONObject("data")
                                        .getString("Accepted") != null) {
                                    noticeDetailEntity = new Gson().fromJson(arg0, NoticeDetailEntity.class);
//                                    AcceptedPhone = jObject.getJSONObject("data")
//                                            .getString("Accepted");
                                    seeAttach_Num.setText("查看附件(共" + noticeDetailEntity.getFileList().size() + "个)");
                                    AcceptedPhone = noticeDetailEntity.getData().getAccepted();
                                }
                                if (noticeDetailEntity.getFileList().size() != 0) {
                                    fileListisEmptity = true;
                                }
//                                Log.e(TAG, noticeDetailEntity.getFileList().size() + "");
//                                AcceptPhone = jObject.getJSONObject("data")
//                                        .getString("Accept");
                                AcceptPhone = noticeDetailEntity.getData().getAccept();
//                                Log.e(TAG, AcceptedPhone);
//                                Log.e(TAG, AcceptPhone);

                                {
                                    status = 2;
//                                    Log.e(TAG, "noticedetail_nosee");
                                    noticedetail_commenttextview_bottomview
                                            .setVisibility(View.INVISIBLE);
                                    noticedetail_nosee_bottomview.setVisibility(View.VISIBLE);
                                    noticedetail_see_bottomview.setVisibility(View.INVISIBLE);
                                    whoseeList.clear();
                                    initNoticenoseeData();
                                    whoseeListAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "数据获取失败", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
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

    public void back(View view) {
        finish();
        if (android.os.Build.VERSION.SDK_INT > 5) {
            overridePendingTransition(
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.noticedetail_commenttextview:
                status = 1;
                noticedetail_commenttextview_bottomview.setVisibility(View.VISIBLE);
                noticedetail_nosee_bottomview.setVisibility(View.INVISIBLE);
                noticedetail_see_bottomview.setVisibility(View.INVISIBLE);
                commentListAdapter = new Notice_CommentListAdapter(commentEntities, NoticeDetailActivity.this);
                noticedetail_comment.setAdapter(commentListAdapter);
                break;
            case R.id.noticedetail_nosee:
//                Log.e(TAG, "noticedetail_nosee");
                status = 2;
                noticedetail_commenttextview_bottomview
                        .setVisibility(View.INVISIBLE);
                noticedetail_nosee_bottomview.setVisibility(View.VISIBLE);
                noticedetail_see_bottomview.setVisibility(View.INVISIBLE);
                whoseeList.clear();
                try {
                    initNoticenoseeData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                whoseeListAdapter = new Notice_SeeListAdapter(whoseeList, NoticeDetailActivity.this);
                noticedetail_comment.setAdapter(whoseeListAdapter);
                break;

            case R.id.noticedetail_see:
                status = 3;
//                Log.e(TAG, "noticedetail_see");
                noticedetail_commenttextview_bottomview
                        .setVisibility(View.INVISIBLE);
                noticedetail_nosee_bottomview.setVisibility(View.INVISIBLE);
                noticedetail_see_bottomview.setVisibility(View.VISIBLE);
                whoseeList.clear();
                initNoticeseeData();
                whoseeListAdapter = new Notice_SeeListAdapter(whoseeList, NoticeDetailActivity.this);
                noticedetail_comment.setAdapter(whoseeListAdapter);
                break;
            case R.id.btn_surecomment:
                if (btn_surecomment.isEnabled()) {
                    pd.show();
                    sentCommentContent();
                } else {
                    showToastShort("请输入...");
                }
                break;

        }
    }

    //发布评论
    private void sentCommentContent() {
        JSONObject commentJson = new JSONObject();
        try {
            commentJson.put("userId", PreferenceManager.getInstance().getCurrentUserId());
            commentJson.put("userRealname", PreferenceManager.getInstance().getCurrentRealName());
            commentJson.put("toUserName", "");
            commentJson.put("toUserId", "");
            commentJson.put("objectId", beclicknotice.getInformId());
            commentJson.put("objectTable", "inform");
            commentJson.put("content", noticecomment_edittext.getText().toString());
            commentJson.put("pid", 0);
            commentJson.put("toUserIds", AcceptPhone);
            CommonUtils.setCommonJson(NoticeDetailActivity.this, commentJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
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
                                         FLAG = 0;
                                         getCommentData();
                                     }
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }
//
                             }
                         }
                );
    }


    //获得评论的数据
    private void getCommentData() {
        JSONObject commentJson = new JSONObject();
        try {
            commentJson.put("objectTable", "inform");
            commentJson.put("objectId", beclicknotice.getInformId());
            CommonUtils.setCommonJson(NoticeDetailActivity.this, commentJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("commentJson", commentJson.toString());

        OkHttpUtils.postString().url(Constant.GETCOMMENTLIST_URL)
                .content(commentJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                             @Override
                             public void onError(Call call, Exception e) {
                                 showToastShort("获取评论失败");
                             }

                             @Override
                             public void onResponse(String s) {
                                 Log.i(TAG, s);
                                 if (pd.isShowing()) {
                                     pd.dismiss();
                                 }
                                 JSONObject jObject;
                                 try {
                                     jObject = new JSONObject(s);
                                     if (jObject.getInt("code") == 1) {
                                         NoticeCommentEntity temp = new Gson().fromJson(s, NoticeCommentEntity.class);
                                         commentEntities.clear();
                                         commentEntities.addAll(temp.getData());
                                         Collections.reverse(commentEntities);
                                         if (FLAG == 1) {
                                             //如果为第一次进入
                                         } else {
                                             /**如果为刷新进入,切换到评论，并且刷新一下数据*/
                                             status = 1;
                                             noticedetail_commenttextview_bottomview.setVisibility(View.VISIBLE);
                                             noticedetail_nosee_bottomview.setVisibility(View.INVISIBLE);
                                             noticedetail_see_bottomview.setVisibility(View.INVISIBLE);
                                             commentListAdapter = new Notice_CommentListAdapter(commentEntities, NoticeDetailActivity.this);
                                             noticedetail_comment.setAdapter(commentListAdapter);
                                         }
                                     }
                                 } catch (Exception e) {
                                     e.printStackTrace();
                                 }
                             }
                         }

                );

    }


    private void initNoticeseeData() {
        if (AcceptedPhone != null) {
            String[] AcceptedPhoneArray = AcceptedPhone.split(",");
            for (int i = 0; i < AcceptedPhoneArray.length; i++) {
                whoseeList.add(i, AcceptedPhoneArray[i]);
            }
        }

    }

    private void initNoticenoseeData() throws JSONException {
        if (AcceptPhone != null && AcceptedPhone != null) {
            String[] AcceptPhoneArray = AcceptPhone.split(",");
            //如果接受人数是大于以前的，基本上可以说就是发送全员的。
//            if (AcceptPhoneArray.length > 1000) {
//                getAllNoticeName(AcceptPhoneArray);
//            } else {
//                pd.dismiss();
//            }
            String[] AcceptedPhoneArray = AcceptedPhone.split(",");
            //显示未读已读后面的人数
            tv_see.setText("(" + AcceptedPhoneArray.length + ")");
            tv_nosee.setText("(" + (AcceptPhoneArray.length - AcceptedPhoneArray.length) + ")");
            //将来数组转为List
            List<String> AcceptPhoneList = new ArrayList<String>();
            Collections.addAll(AcceptPhoneList, AcceptPhoneArray);
            List<String> AcceptedPhoneList = new ArrayList<String>();
            Collections.addAll(AcceptedPhoneList, AcceptedPhoneArray);
            //筛选一下
            for (int i = 0; i < AcceptedPhoneList.size(); i++) {
                for (int j = 0; j < AcceptPhoneList.size(); j++) {
                    if (AcceptedPhoneList.get(i).equals(AcceptPhoneList.get(j))) {
                        AcceptPhoneList.remove(j);
                    }
                }
            }
            for (int aa = 0; aa < AcceptPhoneList.size(); aa++) {
                whoseeList.add(aa, AcceptPhoneList.get(aa));
            }
        }

    }


    //转发的函数
    public void forward_notice(View view) {
        startActivity(new Intent(NoticeDetailActivity.this, CreatNewNoticeActivity.class).putExtra("forwading_title", noticedetail_title.getText().toString()).putExtra("forwading_content", noticedetail_content.getText().toString()));
    }
}
