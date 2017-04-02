package com.wust.newsmartschool.ui;

import java.util.ArrayList;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.MyFlowListAdapter;
import com.wust.newsmartschool.domain.MyJFlowEntity;
import com.wust.newsmartschool.domain.MyJFlowEntity_Out;
import com.wust.easeui.utils.PreferenceManager;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.wust.easeui.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;

public class CreatWorkFlowActivity extends BaseActivity {
    String TAG = "CreatWorkFlowActivity_Debugs";
    private PullToRefreshListView workflow_creat_listview;
    MyJFlowEntity_Out workflowList_out;
    private ArrayList<MyJFlowEntity> workflowList;
    private MyFlowListAdapter workflow_ListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_work_flow);
        workflow_creat_listview = (PullToRefreshListView) findViewById(R.id.workflow_creat_listview);
        workflowList = new ArrayList<MyJFlowEntity>();
        workflowList_out = new MyJFlowEntity_Out();
        initLocalData();
        ILoadingLayout startLabels = workflow_creat_listview
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时

        workflow_ListAdapter = new MyFlowListAdapter(this, workflowList);
        workflow_creat_listview.setAdapter(workflow_ListAdapter);
        workflow_creat_listview.setMode(Mode.DISABLED);
        workflow_creat_listview
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        Log.e(TAG, arg2 + "");
                        MyJFlowEntity clickJFlow = (MyJFlowEntity) workflow_ListAdapter.getItem(arg2 - 1);
                        startActivity(new Intent(CreatWorkFlowActivity.this,
                                JFlowItemApplyActivity.class).putExtra("fk_flow", clickJFlow.getNo()));
                    }
                });

    }

    private void initLocalData() {
        OkHttpUtils
                .post()
                .url(Constant.FLOWLIST_APPLY_URL)
                .addParams("userId",
                        PreferenceManager.getInstance().getCurrentUserId())
                .addParams("sid",
                        PreferenceManager.getInstance().getCurrentUserFlowSId())
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String arg0) {
//                Log.e("onResponse", arg0.toString());
                JSONObject jObject;
                try {
                    jObject = new JSONObject(arg0);
                    if (jObject.getInt("code") == 1) {
                        workflowList_out = new Gson().fromJson(arg0,
                                MyJFlowEntity_Out.class);
                        workflowList.addAll(workflowList_out.getData());
                        workflow_ListAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    showToastShort("请求失败");
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Call arg0, Exception arg1) {
                Log.e("onResponse", arg0.toString());
            }
        });

    }

    public void back(View view) {
        finish();

    }

}
