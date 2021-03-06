package com.wust.newsmartschool.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.wust.newsmartschool.R;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.CommonUtils;
import com.wust.easeui.utils.PreferenceManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

public class PublishTaskActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_add_task;
    private ListView lv_task;
    private ListView title_list;
    private SimpleAdapter adapter;
    private PopupWindow mPopWindow;
    private RelativeLayout title_workfrag_noticedetail;
    private List<Map<String, Object>> data;

    private PtrClassicFrameLayout ptrClassicFrameLayout;
    Handler handler = new Handler();

    private int page = 1;

    private boolean isRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_task);

        View contentView = LayoutInflater.from(PublishTaskActivity.this).inflate(
                R.layout.title_popup, null);
        mPopWindow = new PopupWindow(contentView);

        ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.test_list_view_frame);
        title_list = (ListView) contentView.findViewById(R.id.title_list);
        data = new ArrayList<Map<String, Object>>();

        iv_add_task = (ImageView) findViewById(R.id.iv_add_task);
        lv_task = (ListView) findViewById(R.id.lv_task);
        title_workfrag_noticedetail = (RelativeLayout) findViewById(R.id.title_workfrag_noticedetail);

        iv_add_task.setOnClickListener(this);
        lv_task.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(PublishTaskActivity.this,
                        TaskDetailActivity.class);
                Map<String, Object> map = (Map<String, Object>) lv_task
                        .getItemAtPosition(position);
                intent.putExtra("taskID", map.get("taskID").toString());
                startActivityForResult(intent, 1);
            }
        });

        initTaskItem();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_task:
                showPopupWindow();
                break;
        }
    }

    public void back(View view) {
        if (isRefresh)
            setResult(RESULT_OK);
        else
            setResult(RESULT_CANCELED);
        finish();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isRefresh)
                setResult(RESULT_OK);
            else
                setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initTaskItem() {
        adapter = new SimpleAdapter(this, data, R.layout.task_item,
                new String[]{"status", "title", "type", "time", "taskID"},
                new int[]{R.id.iv_status, R.id.tv_taskTitle,
                        R.id.tv_taskType, R.id.tv_taskTime});
        lv_task.setAdapter(adapter);

        page = 1;
        getData(true);


        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        getData(true);
                    }
                }, 100);
            }
        });

        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        page++;
                        getData(false);
                    }
                }, 100);
            }
        });
    }

    public void getData(final boolean refreshMode) {
        org.json.JSONObject jObject = new org.json.JSONObject();
        try {
            jObject.put("userId", Integer.parseInt(PreferenceManager.getInstance().getCurrentUserId()));
            jObject.put("page", page);
            jObject.put("pageSize", Constant.pageSize);
            CommonUtils.setCommonJson(PublishTaskActivity.this, jObject, PreferenceManager.getInstance().getCurrentUserFlowSId());
        } catch (org.json.JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        OkHttpUtils.postString().url(Constant.GETMYTASKLIST_URL).content(jObject.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        //pDialog.dismiss();
                        Log.i("task", arg0);
                        JSONObject json = JSON.parseObject(arg0);
                        // 这里就解析返回的arg0，注意是String类型的需要类型转换。
                        try {
                            if (json.getIntValue("code") == 1) {
                                JSONArray array = json.getJSONArray("data");
                                if (array.size() != 0) {
                                    List<Map<String, Object>> tempData = new ArrayList<>();
                                    for (int i = 0; i < array.size(); i++) {
                                        Map<String, Object> value = new HashMap<>();
                                        if (array.getJSONObject(i).getIntValue("status") == 1)
                                            value.put("status", R.drawable.working_flow_done);
                                        else
                                            value.put("status", R.drawable.working_flow_ing);
                                        value.put("title", array.getJSONObject(i).getString("title"));
                                        value.put("type", array.getJSONObject(i).getString("type"));
                                        value.put("time", array.getJSONObject(i).getString("deadline"));
                                        value.put("taskID", array.getJSONObject(i).getIntValue("id"));
                                        tempData.add(value);
                                    }
                                    if (page == 1)
                                        data.clear();
                                    data.addAll(tempData);
                                    adapter.notifyDataSetChanged();
                                    if (array.size() < Constant.pageSize)
                                        ptrClassicFrameLayout.setLoadMoreEnable(false);
                                    else
                                        ptrClassicFrameLayout.setLoadMoreEnable(true);

                                    if (refreshMode)
                                        ptrClassicFrameLayout.refreshComplete();
                                    else
                                        ptrClassicFrameLayout.loadMoreComplete(true);
                                } else {
                                    showToastShort("暂时没有数据");
                                    ptrClassicFrameLayout.refreshComplete();
                                    ptrClassicFrameLayout.setLoadMoreEnable(false);
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            ptrClassicFrameLayout.refreshComplete();
                            ptrClassicFrameLayout.setLoadMoreEnable(false);
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        //pDialog.dismiss();
                        // TODO Auto-generated method stub
                        Log.i("task", arg0.toString());
                        showToastShort("网络错误");
                        if (refreshMode)
                            ptrClassicFrameLayout.refreshComplete();
                        else
                            ptrClassicFrameLayout.loadMoreComplete(true);
                        page--;
                    }
                });
    }

    private void showPopupWindow() {

        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Map<String, Object> value = new HashMap<String, Object>();
        value.put("text", "新建任务");
        data.add(value);
        value = new HashMap<String, Object>();
        value.put("text", "新建会议");
        data.add(value);
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                R.layout.title_popup_item, new String[]{"icon", "text"},
                new int[]{R.id.iv_icon, R.id.tv_text});
        title_list.setAdapter(adapter);
        title_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                mPopWindow.dismiss();

                Map<String, Object> map = (Map<String, Object>) title_list
                        .getItemAtPosition(position);
                if (map.get("text").toString().equals("新建任务"))
                    startActivityForResult(new Intent(PublishTaskActivity.this, NewTaskActivity.class), 0);
                else if (map.get("text").toString().equals("新建会议")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            PublishTaskActivity.this);
                    // 指定下拉列表的显示数据
                    final String[] items = {"院方会议", "普通会议"};
                    // 设置一个下拉的列表选择项
                    builder.setItems(items,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent intent = new Intent(
                                            PublishTaskActivity.this,
                                            NewMeetingActivity.class);
                                    intent.putExtra("meetingType", items[which]);
                                    startActivity(intent);
                                }
                            });
                    builder.show();
                }
            }
        });

        // 获得listview的宽度，使popwindow设置为自适应的宽度
        title_list.measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        mPopWindow.setWidth(title_list.getMeasuredWidth());
        mPopWindow.setHeight(LayoutParams.WRAP_CONTENT);
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setFocusable(true);
        // 获取状态栏高度
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        mPopWindow.showAtLocation(iv_add_task, Gravity.RIGHT | Gravity.TOP,
                Dp2Px(this, 5f),
                frame.top + title_workfrag_noticedetail.getHeight());
    }

    // 将Android的dp转换为屏幕的px
    public int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (mPopWindow.isShowing())
            mPopWindow.dismiss();
        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            ptrClassicFrameLayout.autoRefresh();
            isRefresh = true;
        }
    }
}
