package com.ding.chat.ui;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.ding.chat.R;
import com.ding.chat.adapter.JFlowInfoAdapter;
import com.ding.chat.domain.JFlowTrackEntity_Out;
import com.ding.chat.domain.JFlowTrackEntity_Track;
import com.ding.chat.domain.JFlow_Detail_Out;
import com.ding.chat.domain.MyJFlowEntity_IApply;
import com.ding.chat.utils.appUseUtils;
import com.ding.easeui.utils.PreferenceManager;
import com.ding.chat.views.ECProgressDialog;
import com.ding.chat.views.ListViewForScrollView;
import com.ding.easeui.widget.GlideRoundTransform;
import com.google.gson.Gson;
import com.ding.easeui.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ding.easeui.widget.AlertDialogWithEdite;

import android.widget.Toast;

public class JFlowDetailActivity extends BaseActivity {
    String TAG = "JFlowDetailActivity_Debugs";
    List<JFlowTrackEntity_Track> Track;
    private LinearLayout ll_detail_buttons;
    private TextView jflowapply_title;
    private TextView jflowdetail_title;
    private TextView jflowdetail_username;
    private TextView jflowdetail_applytime;
    private ImageView jflowdetail_avatar;
    private TextView jflowdetail_content;
    private Button btn_yes;
    private Button btn_no;
    private Button btn_return;
    private TextView jflowdetail_userposition;
    MyJFlowEntity_IApply oncliItem;
    JFlow_Detail_Out jFlow_Detail_Out;
    JFlowTrackEntity_Out jFlowTrackEntity_Out;
    private ListViewForScrollView applyinfo_listview;
    private JFlowInfoAdapter flowInfoAdapter;
    private ScrollView outscroll_listdetail;
    private ECProgressDialog pDialog;
    //来源,由那一个页面调到这儿来的
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jflow_listdetail);
        jflowdetail_avatar = (ImageView) findViewById(R.id.jflowdetail_avatar);
        jflowdetail_title = (TextView) findViewById(R.id.jflowdetail_title);
        jflowdetail_content = (TextView) findViewById(R.id.jflowdetail_content);
        outscroll_listdetail = (ScrollView) findViewById(R.id.outscroll_listdetail);
        ll_detail_buttons = (LinearLayout) findViewById(R.id.ll_detail_buttons);
        jflowapply_title = (TextView) findViewById(R.id.jflowapply_title);
        jflowdetail_username = (TextView) findViewById(R.id.jflowdetail_username);
        jflowdetail_applytime = (TextView) findViewById(R.id.jflowdetail_applytime);
        jflowdetail_userposition = (TextView) findViewById(R.id.jflowdetail_userposition);
        btn_yes = (Button) findViewById(R.id.btn_yes);
        btn_no = (Button) findViewById(R.id.btn_no);
        btn_return = (Button) findViewById(R.id.btn_return);
        pDialog = new ECProgressDialog(JFlowDetailActivity.this);
        pDialog.setCancelable(true);
        pDialog.show();
        btn_yes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 用AlertDialog再自定义一个带编辑框的dialog
                final AlertDialogWithEdite myDialogWithEdite = new AlertDialogWithEdite(
                        JFlowDetailActivity.this);
                myDialogWithEdite.builder().setTitle("请输入理由")
                        .setCancelable(false)
                        .setPositiveButton("确定", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pDialog.show();
                                v.setVisibility(View.GONE);
                                JFlow_btn_yes(myDialogWithEdite.getEditText()
                                        .toString());
                            }
                        }).setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setVisibility(View.GONE);
                    }
                }).show();
            }
        });
        btn_no.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 用AlertDialog再自定义一个带编辑框的dialog
                final AlertDialogWithEdite myDialogWithEdite = new AlertDialogWithEdite(
                        JFlowDetailActivity.this);
                myDialogWithEdite.builder().setTitle("请输入理由")
                        .setCancelable(false)
                        .setPositiveButton("确定", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pDialog.show();
                                v.setVisibility(View.GONE);
                                JFlow_btn_no(myDialogWithEdite.getEditText()
                                        .toString());
                            }
                        }).setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setVisibility(View.GONE);
                    }
                }).show();

            }
        });
        btn_return.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                JFlow_btn_return();
            }
        });
        outscroll_listdetail.smoothScrollTo(0, 0);
        oncliItem = new MyJFlowEntity_IApply();
        jFlow_Detail_Out = new JFlow_Detail_Out();
        jFlowTrackEntity_Out = new JFlowTrackEntity_Out();
        from = getIntent().getExtras().getString("from");
        oncliItem = new MyJFlowEntity_IApply();
        oncliItem = (MyJFlowEntity_IApply) getIntent().getSerializableExtra(
                "clickitem");
        applyinfo_listview = (ListViewForScrollView) findViewById(R.id.applyinfo_listview);
        Track = new ArrayList<JFlowTrackEntity_Track>();

        Log.e(TAG, oncliItem.getFk_flow() + "");
        Log.e(TAG, oncliItem.getFid() + "");
        Log.e(TAG, oncliItem.getWorkid());

        jflowapply_title.setText(oncliItem.getFlowname().toString());
        jflowdetail_userposition.setText(oncliItem.getDeptname().toString());
        getJFlowDetail();
        getJFlowTrack();
        //显示申请人的头像
        Glide.with(JFlowDetailActivity.this)
                .load(Constant.GETHEADIMAG_URL
                        + oncliItem.getStarter()
                        + ".png").transform(new GlideRoundTransform(JFlowDetailActivity.this)).placeholder(R.drawable.ease_default_avatar)
                .into(jflowdetail_avatar);

    }

    private void getJFlowTrack() {
        // FLOWLIST_ALLRUN_URL
        // http://120.132.85.24:8080/jflow-web/OA/Jflow/2051.do?userId=93&sid=47b4fceaf6ac45079db0e5bad9978928&workID=156
        Log.e(TAG, PreferenceManager.getInstance().getCurrentUserId() + "/" + PreferenceManager.getInstance().getCurrentUserFlowSId() + "/" + oncliItem.getFk_flow() + "/" + oncliItem.getFid() + "/" + oncliItem.getWorkid());
        OkHttpUtils
                .post()
                .url(Constant.FLOWLIST_GETTDETAILRACK_URL)
                .addParams("userId",
                        PreferenceManager.getInstance().getCurrentUserId())
                .addParams("sid",
                        PreferenceManager.getInstance().getCurrentUserFlowSId())
                .addParams("fk_flow", oncliItem.getFk_flow())
                .addParams("fid", String.valueOf(oncliItem.getFid()))
                .addParams("workID", oncliItem.getWorkid()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        pDialog.dismiss();
                        Log.e(TAG, arg0.toString());
                        System.out.print(arg0.toString());
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            Log.e(TAG, jObject.getInt("code") + "");
                            if (jObject.getInt("code") == 1 && jObject.getJSONObject("data") != null) {
                                jFlowTrackEntity_Out = new Gson().fromJson(
                                        arg0, JFlowTrackEntity_Out.class);
                                // WF_Node字段的数组暂时没有用
                                // WF_Node.clear();
                                // WF_Node.addAll(jFlowTrackEntity_Out.getData()
                                // .getWF_Node());
                                Track.clear();
                                Track.addAll(jFlowTrackEntity_Out.getData()
                                        .getTrack());
                                flowInfoAdapter = new JFlowInfoAdapter(
                                        JFlowDetailActivity.this, Track);
                                applyinfo_listview.setAdapter(flowInfoAdapter);
                                if (Track.get(jFlowTrackEntity_Out.getData().getTrack().size() - 1).getActiontype() == 5) {
                                    //如果是撤销无论从哪儿进都不能显示按钮了
                                    ll_detail_buttons.setVisibility(View.GONE);
//                                    outscroll_listdetail.setPadding(0, 0, 0, 10);
                                    ((LinearLayout) findViewById(R.id.outll_jflowdetail)).setPadding(0, 0, 0, 10);
                                } else if (Track.get(jFlowTrackEntity_Out.getData().getTrack().size() - 1).getActiontype() == 8) {
                                    //流程已完成
                                    ll_detail_buttons.setVisibility(View.GONE);
//                                    outscroll_listdetail.setPadding(0, 0, 0, 10);
                                    ((LinearLayout) findViewById(R.id.outll_jflowdetail)).setPadding(0, 0, 0, 10);
                                } else if (Track.get(jFlowTrackEntity_Out.getData().getTrack().size() - 1).getActiontype() == 1) {
                                    //如果从待我审批进入并且流程还没有完
                                    if (from.equals("WorkFlowNeedMeActivity")) {
                                        ll_detail_buttons.setVisibility(View.VISIBLE);
//                                        outscroll_listdetail.setPadding(0, 0, 0, 60);
                                        ((LinearLayout) findViewById(R.id.outll_jflowdetail)).setPadding(0, 0, 0, 60);
                                        btn_yes.setVisibility(View.VISIBLE);
                                        btn_no.setVisibility(View.VISIBLE);
                                    } else if (from.equals("WorkFragWorkFlowActivity")) {
                                        ll_detail_buttons.setVisibility(View.VISIBLE);
//                                        outscroll_listdetail.setPadding(0, 0, 0, 60);
                                        ((LinearLayout) findViewById(R.id.outll_jflowdetail)).setPadding(0, 0, 0, 60);
                                        btn_return.setVisibility(View.VISIBLE);
                                    } else if (from.equals("WorkFlowDealedActivity")) {
                                        //全部按钮都隐藏
                                        ll_detail_buttons.setVisibility(View.GONE);
//                                        outscroll_listdetail.setPadding(0, 0, 0, 10);
                                        ((LinearLayout) findViewById(R.id.outll_jflowdetail)).setPadding(0, 0, 0, 10);
                                    }

                                }
                            } else {
                                showToastShort(jObject.getString("msg"));

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        pDialog.dismiss();
                        Log.e("onResponse", arg0.toString());
                    }
                });

    }

    protected void JFlow_btn_yes(String reason) {
        OkHttpUtils
                .post()
                .url(Constant.FLOWLIST_BUTTON_YES_URL)
                .addParams("userId",
                        PreferenceManager.getInstance().getCurrentUserId())
                .addParams("fk_flow", oncliItem.getFk_flow())
                .addParams("sid",
                        PreferenceManager.getInstance().getCurrentUserFlowSId())
                .addParams("note", reason)
                .addParams("workID", oncliItem.getWorkid()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        pDialog.dismiss();
                        Log.e("onResponse", arg0.toString());
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            Log.e("onResponse", jObject.getInt("code") + "");
                            if (jObject.getInt("code") == 1) {
                                //流程-1操作
                                appUseUtils.GetMyUnreadMumAndJPush(JFlowDetailActivity.this);
                                Toast.makeText(JFlowDetailActivity.this,
                                        jObject.getString("msg"),
                                        Toast.LENGTH_SHORT).show();
                                if (jObject.getJSONObject("data").getBoolean(
                                        "isStopFlow") == true) {
                                    creatDuty();
                                }
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        pDialog.dismiss();
                        showToastShort("操作失败");
                        Log.e("onResponse", arg0.toString() + "/" + arg1.toString());
                    }
                });

    }

    protected void creatDuty() {
        JSONObject dutyCreatJson = new JSONObject();
        try {
            dutyCreatJson.put("title", "议题任务");
            dutyCreatJson.put("content", jFlow_Detail_Out.getData().get(0)
                    .getContent().toString());
            dutyCreatJson.put("typeId", 3);
            dutyCreatJson.put("member", 91);
            dutyCreatJson.put("descri", jFlow_Detail_Out.getData().get(0)
                    .getTitle().toString());
            dutyCreatJson.put("headUserId", 91);
            dutyCreatJson.put("source", "system");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        Log.i("dutyCreatJson", dutyCreatJson.toString());

        OkHttpUtils.postString().url(Constant.CREATDUTYFROMMEET_URL)
                .content(dutyCreatJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1) {
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                    }
                });

    }

    protected void JFlow_btn_no(String reason) {
        OkHttpUtils
                .post()
                .url(Constant.FLOWLIST_BUTTON_NO_URL)
                .addParams("userId",
                        PreferenceManager.getInstance().getCurrentUserId())
                .addParams("fk_flow", oncliItem.getFk_flow())
                .addParams("sid",
                        PreferenceManager.getInstance().getCurrentUserFlowSId())
                .addParams("note", reason)
                .addParams("currentNodeID", oncliItem.getFk_node())
                .addParams("returnToNodeID", "201")
                .addParams("workID", oncliItem.getWorkid())
                .addParams("fid", String.valueOf(oncliItem.getFid())).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        pDialog.dismiss();
                        Log.e("onResponse", arg0.toString());
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            Log.e("onResponse", jObject.getInt("code") + "");
                            Toast.makeText(JFlowDetailActivity.this,
                                    jObject.getString("msg"),
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        pDialog.dismiss();
                        Log.e("onResponse", arg0.toString());
                    }
                });

    }

    protected void JFlow_btn_return() {
        OkHttpUtils
                .post()
                .url(Constant.FLOWLIST_BUTTON_RETURN_URL)
                .addParams("userId",
                        PreferenceManager.getInstance().getCurrentUserId())
                .addParams("fk_flow", oncliItem.getFk_flow())
                .addParams("sid",
                        PreferenceManager.getInstance().getCurrentUserFlowSId())
                .addParams("workID", oncliItem.getWorkid()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        pDialog.dismiss();
                        Log.e("onResponse", arg0.toString());
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            Log.e("onResponse", jObject.getInt("code") + "");
                            if (jObject.getInt("code") == 1) {
                                Toast.makeText(JFlowDetailActivity.this,
                                        "撤销执行成功", Toast.LENGTH_SHORT).show();
                                finish();

                            } else if (jObject.getInt("code") == 0
                                    && jObject.getString("msg").equals(
                                    "请求失败当前节点是开始节点，所以您不能撤销。")) {
                                Toast.makeText(JFlowDetailActivity.this,
                                        "请求失败当前节点是开始节点，所以您不能撤销。",
                                        Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        pDialog.dismiss();
                        Log.e("onResponse", arg0.toString());
                    }
                });

    }

    private void getJFlowDetail() {
        // FLOWLIST_ALLRUN_URL
        // http://120.132.85.24:8080/jflow-web/OA/Jflow/2051.do?userId=93&sid=47b4fceaf6ac45079db0e5bad9978928&workID=156
        OkHttpUtils
                .post()
                .url(Constant.FLOWLIST_GETJFLOWDETAIL_URL)
                .addParams("userId",
                        PreferenceManager.getInstance().getCurrentUserId())
                .addParams("sid",
                        PreferenceManager.getInstance().getCurrentUserFlowSId())
                .addParams("workID", oncliItem.getWorkid()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        pDialog.dismiss();
                        Log.e("onResponse", arg0.toString());
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            Log.e("onResponse", jObject.getInt("code") + "");
                            if (jObject.getInt("code") == 1) {
                                jFlow_Detail_Out = new Gson().fromJson(arg0,
                                        JFlow_Detail_Out.class);
                                jflowdetail_content.setText(jFlow_Detail_Out
                                        .getData().get(0).getContent()
                                        .toString());
                                jflowdetail_title.setText(jFlow_Detail_Out
                                        .getData().get(0).getTopic_title());
                                jflowdetail_username.setText(jFlow_Detail_Out
                                        .getData().get(0).getUser_name());
                                jflowdetail_applytime.setText(jFlow_Detail_Out
                                        .getData().get(0).getCreate_time());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        pDialog.dismiss();
                        Log.e("onResponse", arg0.toString());
                    }
                });

    }

    public void back(View view) {
        finish();

    }

}
