package com.ding.chat.ui;

import java.util.ArrayList;
import java.util.Collections;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import com.ding.chat.R;
import com.ding.chat.adapter.MyFlowApplyListAdapter;
import com.ding.chat.domain.MyJFlowEntity_IApply;
import com.ding.chat.domain.MyJFlowEntity_IApply_Out;
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
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WorkFragWorkFlowActivity extends BaseActivity {
    String TAG = "WorkFragWorkFlowActivity_Debugs";
    private RelativeLayout ll_workflow_needmetodeal;
    private LinearLayout ll_workflow_copytome;
    private TextView workfrag_creatworkflow;
    private TextView unread_jflow_number;
    private PullToRefreshListView workfrag_workflow_listview;
    public static String jflow_number_needtodeal;
    private ArrayList<MyJFlowEntity_IApply> workflowList;
    MyJFlowEntity_IApply_Out workflowList_out;
    private MyFlowApplyListAdapter workflow_ListAdapter;
    private ECProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_flow);
        unread_jflow_number = (TextView) findViewById(R.id.unread_jflow_number);
        ll_workflow_needmetodeal = (RelativeLayout) findViewById(R.id.ll_workflow_needmetodeal);
        ll_workflow_copytome = (LinearLayout) findViewById(R.id.ll_workflow_copytome);
        workfrag_workflow_listview = (PullToRefreshListView) findViewById(R.id.workfrag_workflow_listview);
        workfrag_creatworkflow = (TextView) findViewById(R.id.workfrag_creatworkflow);
        workflowList = new ArrayList<>();
        workflowList_out = new MyJFlowEntity_IApply_Out();
        ILoadingLayout startLabels = workfrag_workflow_listview
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        initLocalData();
        workflow_ListAdapter = new MyFlowApplyListAdapter(this, workflowList);
        workfrag_workflow_listview.setAdapter(workflow_ListAdapter);
        workfrag_workflow_listview.setMode(Mode.PULL_FROM_START);
        workfrag_creatworkflow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkFragWorkFlowActivity.this,
                        CreatWorkFlowActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    overridePendingTransition(R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);
                }
            }
        });
        ll_workflow_needmetodeal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkFragWorkFlowActivity.this,
                        WorkFlowNeedMeActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    overridePendingTransition(R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);
                }

            }
        });
        ll_workflow_copytome.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkFragWorkFlowActivity.this,
                        WorkFlowCopyToMeActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    overridePendingTransition(R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);
                }

            }
        });
        workfrag_workflow_listview
                .setOnRefreshListener(new OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        workfrag_workflow_listview.setRefreshing(true);
                        initLocalData();

                    }
                });
        workfrag_workflow_listview
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        MyJFlowEntity_IApply oncliItem = new MyJFlowEntity_IApply();
                        oncliItem = (MyJFlowEntity_IApply) workflow_ListAdapter
                                .getItem(arg2 - 1);
                        startActivity(new Intent(WorkFragWorkFlowActivity.this,
                                JFlowDetailActivity.class).putExtra(
                                "clickitem", oncliItem).putExtra("from",
                                "WorkFragWorkFlowActivity"));
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initLocalData();
        progressDialog = new ECProgressDialog(WorkFragWorkFlowActivity.this);
        progressDialog.show();
        if (jflow_number_needtodeal != null) {
            if (Integer.parseInt(jflow_number_needtodeal) > 0) {
                unread_jflow_number.setVisibility(View.VISIBLE);
                unread_jflow_number.setText(jflow_number_needtodeal);
            } else {
                unread_jflow_number.setVisibility(View.INVISIBLE);
            }
        }

    }

    private void initLocalData() {
        // FLOWLIST_ALLRUN_URL
        OkHttpUtils
                .post()
                .url(Constant.FLOWLIST_MYAPPLY_URL)
                .addParams("userId",
                        PreferenceManager.getInstance().getCurrentUserId())
                .addParams("sid",
                        PreferenceManager.getInstance().getCurrentUserFlowSId())
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String arg0) {
                progressDialog.dismiss();
                workfrag_workflow_listview.onRefreshComplete();
                Log.e(TAG, arg0.toString());
                JSONObject jObject;
                try {
                    jObject = new JSONObject(arg0);

                    if (jObject.getInt("code") == 1) {
                        workflowList_out = new Gson().fromJson(arg0,
                                MyJFlowEntity_IApply_Out.class);
                        workflowList.clear();
                        workflowList.addAll(workflowList_out.getData());
                        Collections.reverse(workflowList);
                        workflow_ListAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Call arg0, Exception arg1) {
                progressDialog.dismiss();
                Log.e(TAG, arg0.toString());
                workfrag_workflow_listview.onRefreshComplete();
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
