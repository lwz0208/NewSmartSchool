package com.ding.chat.ui;

import java.util.ArrayList;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import com.ding.chat.R.id;
import com.ding.chat.R.layout;
import com.ding.chat.adapter.MyFlowListAdapter;
import com.ding.chat.domain.MyJFlowEntity;
import com.ding.chat.domain.MyJFlowEntity_Out;
import com.ding.easeui.utils.PreferenceManager;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.ding.easeui.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class WorkFlowCopyMeUnreadActivity extends Activity {
    private PullToRefreshListView workflow_copyunread_listview;
    private ArrayList<MyJFlowEntity> workflowList;
    MyJFlowEntity_Out workflowList_out;
    private MyFlowListAdapter workflow_ListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_work_flow_copy_me_unread);
        workflow_copyunread_listview = (PullToRefreshListView) findViewById(id.workflow_copyunread_listview);

        workflowList = new ArrayList<MyJFlowEntity>();
        workflowList_out = new MyJFlowEntity_Out();
        initLocalData();
        workflow_ListAdapter = new MyFlowListAdapter(this, workflowList);
        workflow_copyunread_listview.setAdapter(workflow_ListAdapter);
        workflow_copyunread_listview.setMode(Mode.PULL_FROM_START);
        workflow_copyunread_listview
                .setOnRefreshListener(new OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        workflow_copyunread_listview.setRefreshing(true);
                        initLocalData();
                    }
                });

    }

    private void initLocalData() {
        OkHttpUtils.post().url(Constant.FLOWLIST_COPYREAD_URL)
                .addParams("userId", PreferenceManager.getInstance().getCurrentUserId())
                .addParams("sid", PreferenceManager.getInstance().getCurrentUserFlowSId()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        workflow_copyunread_listview.onRefreshComplete();
                        Log.e("onResponse", arg0.toString());
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1) {
                                Log.e("onResponse", jObject.getString("msg"));
                                workflowList_out = new Gson().fromJson(arg0,
                                        MyJFlowEntity_Out.class);
                                workflowList.addAll(workflowList_out.getData());
                                workflow_ListAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        workflow_copyunread_listview.onRefreshComplete();
                        Log.e("onResponse", arg0.toString());
                    }
                });

    }

    public void back(View view) {
        finish();

    }

}
