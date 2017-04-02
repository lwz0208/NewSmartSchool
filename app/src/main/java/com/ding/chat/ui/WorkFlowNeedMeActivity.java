package com.ding.chat.ui;

import java.util.ArrayList;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import com.ding.chat.*;
import com.ding.chat.adapter.MyFlowApplyListAdapter;
import com.ding.chat.domain.MyJFlowEntity_IApply;
import com.ding.chat.domain.MyJFlowEntity_IApply_Out;
import com.ding.chat.domain.UserInfoEntity;
import com.ding.easeui.utils.PreferenceManager;
import com.ding.chat.views.ECProgressDialog;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.ding.easeui.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class WorkFlowNeedMeActivity extends Activity {
    String TAG = "WorkFlowNeedMeActivity_Debugs";
    private LinearLayout ll_workflow_dealed;
    private PullToRefreshListView workflow_needme_listview;
    private ArrayList<MyJFlowEntity_IApply> workflowList;
    MyJFlowEntity_IApply_Out workflowList_out;
    private MyFlowApplyListAdapter workflow_ListAdapter;
    private ECProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_flow_need_me);
        ll_workflow_dealed = (LinearLayout) findViewById(R.id.ll_workflow_dealed);
        workflow_needme_listview = (PullToRefreshListView) findViewById(R.id.workflow_needme_listview);
        dialog = new ECProgressDialog(WorkFlowNeedMeActivity.this);
        dialog.show();
        workflowList = new ArrayList<MyJFlowEntity_IApply>();
        workflowList_out = new MyJFlowEntity_IApply_Out();
        ILoadingLayout startLabels = workflow_needme_listview
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        initLocalData();
        workflow_ListAdapter = new MyFlowApplyListAdapter(this, workflowList);
        workflow_needme_listview.setAdapter(workflow_ListAdapter);
        workflow_needme_listview.setMode(Mode.PULL_FROM_START);
        workflow_needme_listview
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        MyJFlowEntity_IApply oncliItem = new MyJFlowEntity_IApply();
                        oncliItem = (MyJFlowEntity_IApply) workflow_ListAdapter
                                .getItem(arg2 - 1);
                        if (oncliItem.getFk_flow().equals("006")) {
                            Bundle sent = new Bundle();
                            sent.putString("sid", PreferenceManager.getInstance().getCurrentUserFlowSId());
                            sent.putString("userId", PreferenceManager.getInstance().getCurrentUserId());
                            sent.putString("fk_flow", oncliItem.getFk_flow());
                            sent.putString("shenheren", ((UserInfoEntity) DemoApplication.getInstance().mCache
                                    .getAsObject(Constant.MY_KEY_USERINFO)).getData().getUserRealname().toString());
                            sent.putString("workID", oncliItem.getWorkid());
                            sent.putString("ispass", "1");
                            sent.putString("starterName", oncliItem.getStartername());
                            startActivity(new Intent(WorkFlowNeedMeActivity.this,
                                    MyReactActivity.class).putExtras(sent));
                        } else if (oncliItem.getFk_flow().equals("002")) {
                            startActivity(new Intent(WorkFlowNeedMeActivity.this,
                                    JFlowDetailActivity.class).putExtra(
                                    "clickitem", oncliItem).putExtra("from",
                                    "WorkFlowNeedMeActivity"));
                        }
                    }
                });
        ll_workflow_dealed.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkFlowNeedMeActivity.this,
                        WorkFlowDealedActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    overridePendingTransition(R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);
                }

            }
        });

        workflow_needme_listview
                .setOnRefreshListener(new OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        workflow_needme_listview.setRefreshing(true);
                        initLocalData();
                    }
                });
    }

    private void initLocalData() {
        Log.e(TAG, PreferenceManager.getInstance().getCurrentUserId() + "." + PreferenceManager.getInstance().getCurrentUserFlowSId());
        OkHttpUtils
                .post()
                .url(Constant.FLOWLIST_WAITDEAL_URL)
                .addParams("userId",
                        PreferenceManager.getInstance().getCurrentUserId())
                .addParams("sid",
                        PreferenceManager.getInstance().getCurrentUserFlowSId())
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String arg0) {
                dialog.dismiss();
                workflow_needme_listview.onRefreshComplete();
                Log.e("onResponse", arg0.toString());
                JSONObject jObject;
                try {
                    jObject = new JSONObject(arg0);
                    if (jObject.getInt("code") == 1) {
                        Log.e("onResponse", jObject.getString("msg"));
                        workflowList_out = new Gson().fromJson(arg0,
                                MyJFlowEntity_IApply_Out.class);
                        workflowList.clear();
                        workflowList.addAll(workflowList_out.getData());
                        workflow_ListAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Call arg0, Exception arg1) {
                dialog.dismiss();
                workflow_needme_listview.onRefreshComplete();
                Log.e("onResponse", arg0.toString());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        dialog.show();
        initLocalData();
    }

    public void back(View view) {
        finish();
    }

}
