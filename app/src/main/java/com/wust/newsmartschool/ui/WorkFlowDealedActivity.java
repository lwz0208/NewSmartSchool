package com.wust.newsmartschool.ui;

import java.util.ArrayList;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import com.wust.newsmartschool.DemoApplication;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.MyFlowApplyListAdapter;
import com.wust.newsmartschool.domain.MyJFlowEntity_IApply;
import com.wust.newsmartschool.domain.MyJFlowEntity_IApply_Out;
import com.wust.newsmartschool.domain.UserInfoEntity;
import com.wust.easeui.utils.PreferenceManager;
import com.wust.newsmartschool.views.ECProgressDialog;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.wust.easeui.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.content.Intent;

public class WorkFlowDealedActivity extends Activity {
    private PullToRefreshListView workflow_havedealed_listview;
    private ArrayList<MyJFlowEntity_IApply> workflowList;
    private ECProgressDialog dialog;
    private MyFlowApplyListAdapter workflow_ListAdapter;
    MyJFlowEntity_IApply_Out workflowList_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_flow_dealed);
        workflow_havedealed_listview = (PullToRefreshListView) findViewById(R.id.workflow_havedealed_listview);
        workflowList = new ArrayList<MyJFlowEntity_IApply>();
        dialog = new ECProgressDialog(WorkFlowDealedActivity.this);
        dialog.show();
        ILoadingLayout startLabels = workflow_havedealed_listview
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        initLocalData();
        workflow_ListAdapter = new MyFlowApplyListAdapter(this, workflowList);
        workflow_havedealed_listview.setAdapter(workflow_ListAdapter);
        workflow_havedealed_listview.setMode(Mode.PULL_FROM_START);
        workflow_havedealed_listview
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
                            sent.putString("ispass", "2");
                            sent.putString("starterName", oncliItem.getStartername());
                            startActivity(new Intent(WorkFlowDealedActivity.this,
                                    MyReactActivity.class).putExtras(sent));
                        } else if (oncliItem.getFk_flow().equals("002")) {
                            startActivity(new Intent(WorkFlowDealedActivity.this,
                                    JFlowDetailActivity.class).putExtra(
                                    "clickitem", oncliItem).putExtra("from",
                                    "WorkFlowDealedActivity"));
                        }
                    }
                });
        workflow_havedealed_listview
                .setOnRefreshListener(new OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        workflow_havedealed_listview.setRefreshing(true);

                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(WorkFlowDealedActivity.this,
                                        "暂无更多", Toast.LENGTH_SHORT).show();
                                workflow_ListAdapter.notifyDataSetChanged();
                                workflow_havedealed_listview
                                        .onRefreshComplete();

                            }
                        }, 1000);

                    }
                });
    }

    private void initLocalData() {
        OkHttpUtils
                .post()
                .url(Constant.GETIHAVEDEALJFLOW_URL)
                .addParams("userId",
                        PreferenceManager.getInstance().getCurrentUserId())
                .addParams("sid",
                        PreferenceManager.getInstance().getCurrentUserFlowSId())
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String arg0) {
                dialog.dismiss();
                workflow_havedealed_listview.onRefreshComplete();
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
                workflow_havedealed_listview.onRefreshComplete();
                Log.e("onResponse",
                        arg0.toString() + "---" + arg1.toString());
            }
        });

    }

    public void back(View view) {
        finish();

    }

}
