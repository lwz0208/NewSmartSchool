package com.wust.newsmartschool.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.wust.newsmartschool.DemoApplication;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.TaskList_Adapter;
import com.wust.newsmartschool.domain.CompanyEntity;
import com.wust.newsmartschool.domain.TaskEntity;
import com.wust.newsmartschool.domain.myTaskMajor;
import com.wust.newsmartschool.domain.myTaskType;
import com.wust.newsmartschool.views.ECProgressDialog;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.CommonUtils;
import com.wust.easeui.utils.PreferenceManager;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

public class SearchTaskActivity extends BaseActivity implements View.OnClickListener {
    String TAG = "SearchTaskActivity_Debugs";
    //上下来刷新的控件
    private PtrClassicFrameLayout ptrClassicFrameLayout;
    private ListView lv_searchtask;
    private TaskList_Adapter searchtask_Adapter;
    List<Map<String, Object>> searchtaskList;
    private EditText query_task;
    private ImageButton clearSearch;
    //等待的圈圈
    private ECProgressDialog pd;
    private RelativeLayout tasksearchBydept;
    private RelativeLayout tasksearchBymajor;
    private RelativeLayout tasksearchBytimefrom;
    private RelativeLayout tasksearchBytimeend;
    private RelativeLayout tasksearch_bystatus;
    private RelativeLayout tasksearch_bytype;
    private LinearLayout ll_searchtask_Tips;
    private TextView sure_searchtask;
    //翻页用的page
    int page = 1;
    //怼到接口里的数据
    int createUserDeptId = -1;
    int priorityId = -1;
    int status = -1;
    int taskTypeId = -1;
    String startTime = "";
    String endTime = "";
    CompanyEntity temp_Com;
    private List<myTaskMajor> taskmajorList;
    private List<myTaskType> tasktypeList;
    //记录每一个选择的那个痘痘的位置，下次点击的时候应该在对应位置上而不是永远在第一个
    int posit_bydept = 0;
    int posit_bymajor = 0;
    int posit_bytype = 0;
    int posit_bystatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_task);
        initView();
        setupView();
    }


    private void initView() {
        ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.search_listview_frame);
        tasksearchBydept = (RelativeLayout) findViewById(R.id.tasksearch_bydept);
        tasksearch_bystatus = (RelativeLayout) findViewById(R.id.tasksearch_bystatus);
        tasksearch_bytype = (RelativeLayout) findViewById(R.id.tasksearch_bytype);
        tasksearchBymajor = (RelativeLayout) findViewById(R.id.tasksearch_bymajor);
        tasksearchBytimefrom = (RelativeLayout) findViewById(R.id.tasksearch_bytimefrom);
        tasksearchBytimeend = (RelativeLayout) findViewById(R.id.tasksearch_bytimeend);
        ll_searchtask_Tips = (LinearLayout) findViewById(R.id.ll_searchtask_Tips);
        lv_searchtask = (ListView) findViewById(R.id.lv_searchtask);
        clearSearch = (ImageButton) findViewById(R.id.search_clear_task);
        sure_searchtask = (TextView) findViewById(R.id.sure_searchtask);
        query_task = (EditText) findViewById(R.id.query_task);
        pd = new ECProgressDialog(SearchTaskActivity.this);
        pd.setPressText("正在搜索...");
        searchtaskList = new ArrayList<>();
        tasksearch_bystatus.setOnClickListener(this);
        tasksearch_bytype.setOnClickListener(this);
        tasksearchBydept.setOnClickListener(this);
        tasksearchBymajor.setOnClickListener(this);
        tasksearchBytimefrom.setOnClickListener(this);
        tasksearchBytimeend.setOnClickListener(this);
        sure_searchtask.setOnClickListener(this);
    }

    private void setupView() {
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query_task.getText().clear();
                lv_searchtask.setVisibility(View.GONE);
                ptrClassicFrameLayout.setVisibility(View.GONE);
                ll_searchtask_Tips.setVisibility(View.VISIBLE);
            }
        });
        query_task.addTextChangedListener(new watcher());
        searchtask_Adapter = new TaskList_Adapter(SearchTaskActivity.this, searchtaskList);
        lv_searchtask.setAdapter(searchtask_Adapter);
        //短按进入他的子任务菜单
        lv_searchtask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //需要先判断他是否有子任务
                pd.show();
                Map<String, Object> map = (Map<String, Object>) searchtask_Adapter.getItem(position);
                getClickChildTask(map.get("taskID").toString(), map.get("title").toString());
            }
        });
        //长按进入详情
        lv_searchtask.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchTaskActivity.this, TaskDetailActivity.class);
                Map<String, Object> map = (Map<String, Object>) searchtask_Adapter.getItem(position);
                intent.putExtra("taskID", map.get("taskID").toString());
                startActivity(intent);
                return true;
            }
        });
        temp_Com = new CompanyEntity();
        temp_Com = (CompanyEntity) DemoApplication.getInstance().mCache.getAsObject(Constant.MY_KEY_COMPANY);
        taskmajorList = new ArrayList<>();
        tasktypeList = new ArrayList<>();
        initTaskMajor();
        initTaskType();
        ptrClassicFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrClassicFrameLayout.autoRefresh(false);
            }
        }, 150);

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                try {
                    SearchTheData(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                page++;
                try {
                    SearchTheData(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getClickChildTask(String taskID, final String title) {
        JSONObject taskJson = new JSONObject();
        try {
            taskJson.put("id", taskID);
            CommonUtils.setCommonJson(SearchTaskActivity.this, taskJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
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
                                    //由于此段代码由两个人写，所以有两种解析方式。List<Map>的数据方式没有序列化接口，不允许putextra传递参数。
                                    TaskEntity taskextra = new TaskEntity();
                                    taskextra = new Gson().fromJson(arg0, TaskEntity.class);
                                    startActivity(new Intent(SearchTaskActivity.this, SearchTaskChildActivity.class).putExtra("searchtaskList", (Serializable) taskextra.getData()).putExtra("title", title));
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

    private void SearchTheData(final boolean refreshMode) throws JSONException {
        JSONObject taskJson = new JSONObject();
        if (createUserDeptId != -1)
            taskJson.put("createUserDeptId", createUserDeptId);
        if (!startTime.equals(""))
            taskJson.put("startTime", startTime);
        if (!endTime.equals(""))
            taskJson.put("endTime", endTime);
        if (priorityId != -1)
            taskJson.put("priorityId", priorityId);
        if (status != -1)
            taskJson.put("status", status);
        if (taskTypeId != -1)
            taskJson.put("taskTypeId", taskTypeId);
        if (!query_task.getText().toString().equals(""))
            taskJson.put("searchString", query_task.getText().toString());
        taskJson.put("page", page);
        taskJson.put("pageSize", Constant.pageSize);
        CommonUtils.setCommonJson(SearchTaskActivity.this, taskJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i(TAG, taskJson.toString());
        OkHttpUtils.postString().url(Constant.SEARCHTASKLIST_URL)
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
                                    sure_searchtask.setText("取消");
                                    sure_searchtask.setTextColor(getResources().getColor(R.color.bottom_bar_normal_bg));
                                    List<Map<String, Object>> tempData = new ArrayList<>();
                                    for (int i = 0; i < array.size(); i++) {
                                        Map<String, Object> value = new HashMap<>();
                                        value.put("title", array.getJSONObject(i).getString("title"));
                                        value.put("type", array.getJSONObject(i).getString("type"));
                                        value.put("time", array.getJSONObject(i).getString("deadline"));
                                        value.put("taskID", array.getJSONObject(i).getIntValue("id"));
                                        value.put("priorityId", array.getJSONObject(i).getIntValue("priorityId"));
                                        value.put("status", array.getJSONObject(i).getIntValue("status"));
                                        value.put("createUserId", array.getJSONObject(i).getIntValue("createUserId"));
                                        tempData.add(value);
                                    }
                                    if (page == 1)
                                        searchtaskList.clear();
                                    searchtaskList.addAll(tempData);
                                    lv_searchtask.setVisibility(View.VISIBLE);
                                    ptrClassicFrameLayout.setVisibility(View.VISIBLE);
                                    ll_searchtask_Tips.setVisibility(View.GONE);
                                    searchtask_Adapter.notifyDataSetChanged();
                                    if (array.size() < Constant.pageSize) {
                                        ptrClassicFrameLayout.setLoadMoreEnable(false);
                                        showToastShort("暂无更多数据");
                                    } else
                                        ptrClassicFrameLayout.setLoadMoreEnable(true);

                                    if (refreshMode)
                                        ptrClassicFrameLayout.refreshComplete();
                                    else
                                        ptrClassicFrameLayout.loadMoreComplete(true);
                                } else {
                                    showToastShort("未查询到数据");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tasksearch_bydept:

                final String[] DeptArray = new String[temp_Com.getData().size() + 1];
                for (int i = 0; i < temp_Com.getData().size(); i++) {
                    DeptArray[i] = temp_Com.getData().get(i).getDepName();
                }
                DeptArray[temp_Com.getData().size()] = "忽略";
                AlertDialog.Builder builder_dept = new AlertDialog.Builder(SearchTaskActivity.this);
                builder_dept.setTitle("选择搜索部门");
                builder_dept.setSingleChoiceItems(DeptArray, posit_bydept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        posit_bydept = which;
//                        showToastShort(DeptArray[which]);
                        ((TextView) findViewById(R.id.tasksearch_bydept_tv)).setText(DeptArray[which]);
                        if (which < temp_Com.getData().size())
                            createUserDeptId = temp_Com.getData().get(which).getDeptId();
                        else
                            createUserDeptId = -1;
                        dialog.cancel();
                    }
                });
                builder_dept.show();
                break;
            case R.id.tasksearch_bymajor:
                final String[] MajorArray = new String[taskmajorList.size() + 1];
                if (taskmajorList.size() > 0) {
                    for (int i = 0; i < taskmajorList.size(); i++) {
                        MajorArray[i] = taskmajorList.get(i).getName();
                    }
                } else {
                    showToastShort("暂无优先级列表");
                    return;
                }
                MajorArray[taskmajorList.size()] = "忽略";
                AlertDialog.Builder builder_major = new AlertDialog.Builder(SearchTaskActivity.this);
                builder_major.setTitle("选择搜索优先级");
                builder_major.setSingleChoiceItems(MajorArray, posit_bymajor, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        posit_bymajor = which;
//                        showToastShort(MajorArray[which]);
                        ((TextView) findViewById(R.id.tasksearch_bymajor_tv)).setText(MajorArray[which]);
                        if (which < taskmajorList.size())
                            priorityId = taskmajorList.get(which).getId();
                        else
                            priorityId = -1;
                        dialog.cancel();
                    }
                });
                builder_major.show();
                break;
            case R.id.tasksearch_bytype:
                final String[] TyperArray = new String[tasktypeList.size() + 1];
                if (tasktypeList.size() > 0) {
                    for (int i = 0; i < tasktypeList.size(); i++) {
                        TyperArray[i] = tasktypeList.get(i).getTaskName();
                    }
                } else {
                    showToastShort("暂无类型列表");
                    return;
                }
                TyperArray[tasktypeList.size()] = "忽略";
                AlertDialog.Builder builder_type = new AlertDialog.Builder(SearchTaskActivity.this);
                builder_type.setTitle("选择搜索类型");
                builder_type.setSingleChoiceItems(TyperArray, posit_bytype, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        posit_bytype = which;
//                        showToastShort(TyperArray[which]);
                        ((TextView) findViewById(R.id.tasksearch_bytype_tv)).setText(TyperArray[which]);
                        if (which < tasktypeList.size())
                            taskTypeId = tasktypeList.get(which).getTaskID();
                        else
                            taskTypeId = -1;

                        dialog.cancel();
                    }
                });
                builder_type.show();
                break;
            case R.id.tasksearch_bytimefrom:
                Calendar calendar1 = Calendar.getInstance();
                new DatePickerDialog(SearchTaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // TODO Auto-generated method stub
                                ((TextView) findViewById(R.id.tasksearch_bytimefrom_tv)).setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);
                                startTime = year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth;
                            }
                        }, calendar1.get(Calendar.YEAR), calendar1
                        .get(Calendar.MONTH), calendar1
                        .get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.tasksearch_bytimeend:
                Calendar calendar2 = Calendar.getInstance();
                new DatePickerDialog(SearchTaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // TODO Auto-generated method stub
                                ((TextView) findViewById(R.id.tasksearch_bytimeend_tv)).setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);
                                endTime = year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth;
                            }
                        }, calendar2.get(Calendar.YEAR), calendar2
                        .get(Calendar.MONTH), calendar2
                        .get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.tasksearch_bystatus:
                //0未完成，1已完成
                final String[] StatusArray = {"未完成", "已完成", "忽略"};
                AlertDialog.Builder builder_status = new AlertDialog.Builder(SearchTaskActivity.this);
                builder_status.setTitle("选择搜索状态");
                builder_status.setSingleChoiceItems(StatusArray, posit_bystatus, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        posit_bystatus = which;
//                        showToastShort(StatusArray[which]);
                        ((TextView) findViewById(R.id.tasksearch_bystatus_tv)).setText(StatusArray[which]);
                        if (which < 2)
                            status = which;
                        else
                            status = -1;
                        dialog.cancel();
                    }
                });

                builder_status.show();
                break;
            case R.id.sure_searchtask:
                if (sure_searchtask.getText().equals("搜索")) {
                    pd.show();
                    try {
                        SearchTheData(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (sure_searchtask.getText().equals("取消")) {
                    sure_searchtask.setTextColor(getResources().getColor(R.color.btn_green_noraml));
                    sure_searchtask.setText("搜索");
                    query_task.getText().clear();
                    lv_searchtask.setVisibility(View.GONE);
                    ptrClassicFrameLayout.setVisibility(View.GONE);
                    ll_searchtask_Tips.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    private void initTaskType() {
        org.json.JSONObject jObject = new org.json.JSONObject();
        try {
            CommonUtils.setCommonJson(SearchTaskActivity.this, jObject, PreferenceManager.getInstance().getCurrentUserFlowSId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils.postString().url(Constant.GETTASKTYPE_URL).content(jObject.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        Log.i("task", "获取任务类型接口失败返回结果：" + arg0.toString() + " :    " + arg1.toString());
                    }

                    @Override
                    public void onResponse(String arg0) {
                        Log.i("task", "获取任务类型接口成功返回结果：" + arg0);
                        com.alibaba.fastjson.JSONObject json = JSON.parseObject(arg0);
                        if (json.getIntValue("code") == 1) {
                            if (!json.getString("msg").equals("无数据")) {
                                tasktypeList = new ArrayList<myTaskType>();
                                JSONArray array = json.getJSONArray("data");
                                for (int i = 0; i < array.size(); i++) {
                                    myTaskType task = new myTaskType(
                                            array.getJSONObject(i).getIntValue(
                                                    "id"), array.getJSONObject(
                                            i).getString("name"));
                                    tasktypeList.add(task);
                                }
                            }
                        }
                    }
                });
    }

    private void initTaskMajor() {
        org.json.JSONObject jObject = new org.json.JSONObject();
        try {
            CommonUtils.setCommonJson(SearchTaskActivity.this, jObject, PreferenceManager.getInstance().getCurrentUserFlowSId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils.postString().url(Constant.GETTASKMAJOR_URL).content(jObject.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        Log.i("task", "获取任务类型接口失败返回结果：" + arg0.toString() + " :    " + arg1.toString());
                    }

                    @Override
                    public void onResponse(String arg0) {
                        com.alibaba.fastjson.JSONObject json = JSON.parseObject(arg0);
                        Log.e(TAG, arg0);
                        if (json.getIntValue("code") == 1) {
                            JSONArray TaskMajordata = json.getJSONArray("data");
                            for (int i = 0; i < TaskMajordata.size(); i++) {
                                myTaskMajor temp = new myTaskMajor();
                                temp.setName(TaskMajordata.getJSONObject(i).getString("name"));
                                temp.setId(TaskMajordata.getJSONObject(i).getInteger("id"));
                                taskmajorList.add(temp);
                            }
                        }
                    }
                });
    }


    class watcher implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (s.length() > 0) {
                clearSearch.setVisibility(View.VISIBLE);
            } else {
                clearSearch.setVisibility(View.INVISIBLE);
            }

        }

    }

    public void back(View view) {
        finish();
        if (Build.VERSION.SDK_INT > 5) {
            overridePendingTransition(
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }

    }
}
