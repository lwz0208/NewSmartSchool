package com.ding.chat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.ding.chat.R;
import com.ding.chat.adapter.TaskList_Adapter;
import com.ding.chat.domain.TaskEntity;
import com.ding.chat.views.ECProgressDialog;
import com.ding.chat.views.ListViewForScrollView;
import com.ding.easeui.Constant;
import com.ding.easeui.utils.CommonUtils;
import com.ding.easeui.utils.PreferenceManager;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

public class SearchTaskChildActivity extends BaseActivity {
    String TAG = "SearchTaskChildActivity_Debugs";
    private ListViewForScrollView lv_searchchildtask;
    private TextView title_searchchiletask;
    List<Map<String, Object>> searchtaskList;
    List<TaskEntity.DataBean> taskextra;
    private TaskList_Adapter searchtask_Adapter;
    ECProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_task_child);
        initView();
    }

    private void initView() {
        lv_searchchildtask = (ListViewForScrollView) findViewById(R.id.lv_searchchildtask);
        title_searchchiletask = (TextView) findViewById(R.id.title_searchchiletask);
        searchtaskList = new ArrayList<>();
        taskextra = new ArrayList<>();
        taskextra = (List<TaskEntity.DataBean>) getIntent().getSerializableExtra("searchtaskList");
        //将来接收到的实体类解析为adapter可以用的数据类型
        List<Map<String, Object>> tempData = new ArrayList<>();
        for (int i = 0; i < taskextra.size(); i++) {
            Map<String, Object> value = new HashMap<>();
            value.put("title", taskextra.get(i).getTitle());
            value.put("type", taskextra.get(i).getType());
            value.put("time", taskextra.get(i).getDeadline());
            value.put("taskID", taskextra.get(i).getId());
            value.put("priorityId", taskextra.get(i).getPriorityId());
            value.put("status", taskextra.get(i).getStatus());
            value.put("createUserId", taskextra.get(i).getCreateUserId());
            tempData.add(value);
        }
        searchtaskList.addAll(tempData);
        searchtask_Adapter = new TaskList_Adapter(SearchTaskChildActivity.this, searchtaskList);
        lv_searchchildtask.setAdapter(searchtask_Adapter);
        if (getIntent().getStringExtra("title") != null) {
            title_searchchiletask.setText(getIntent().getStringExtra("title").toString());
        }
        pd = new ECProgressDialog(SearchTaskChildActivity.this);
        lv_searchchildtask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pd.show();
                Map<String, Object> map = (Map<String, Object>) searchtask_Adapter.getItem(position);
                getChildTaskList(map.get("taskID").toString(), map.get("title").toString());
            }
        });
        //长按进入详情
        lv_searchchildtask.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchTaskChildActivity.this, TaskDetailActivity.class);
                Map<String, Object> map = (Map<String, Object>) searchtask_Adapter.getItem(position);
                intent.putExtra("taskID", map.get("taskID").toString());
                startActivity(intent);
                return true;
            }
        });
    }

    private void getChildTaskList(String taskID, final String title) {
        JSONObject taskJson = new JSONObject();
        try {
            taskJson.put("id", taskID);
            CommonUtils.setCommonJson(SearchTaskChildActivity.this, taskJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils.postString().url(Constant.GETSEARCHCHILDTASK_URL)
                .content(taskJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        pd.dismiss();
                        Log.i(TAG, arg0);
                        com.alibaba.fastjson.JSONObject json = JSON.parseObject(arg0);
                        // 这里就解析返回的arg0，注意是String类型的需要类型转换。
                        try {
                            if (json.getIntValue("code") == 1) {
                                JSONArray array = json.getJSONArray("data");
                                if (array.size() != 0) {
                                    TaskEntity taskextra = new TaskEntity();
                                    taskextra = new Gson().fromJson(arg0, TaskEntity.class);
                                    startActivity(new Intent(SearchTaskChildActivity.this, SearchTaskChildActivity.class).putExtra("searchtaskList", (Serializable) taskextra.getData()).putExtra("title", title));
                                } else {
                                    showToastShort("该任务没有子任务，请长按查看任务详情。");
                                }
                            }
                        } catch (com.alibaba.fastjson.JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        Log.e(TAG, "onError: " + arg0 + "/*/" + arg1);
                        showToastShort("搜索失败");
                        pd.dismiss();
                    }
                });
    }

}
