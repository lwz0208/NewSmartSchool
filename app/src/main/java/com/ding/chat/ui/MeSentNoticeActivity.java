package com.ding.chat.ui;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import com.ding.chat.R;
import com.ding.chat.adapter.MyNoticeListAdapter;
import com.ding.chat.adapter.NoticeListAdapter;
import com.ding.chat.domain.NoticeEntity;
import com.ding.chat.domain.Notice_OutEntity;
import com.ding.easeui.utils.PreferenceManager;
import com.ding.chat.views.ECProgressDialog;
import com.ding.easeui.utils.CommonUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.ding.easeui.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.utils.Exceptions;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MeSentNoticeActivity extends BaseActivity {
    PullToRefreshListView mynotice_listview;
    private MyNoticeListAdapter notice_ListAdapter;
    private ImageView mynotice_add_notice;
    Notice_OutEntity notice_OutEntity;
    List<NoticeEntity> noticeList;
    private ECProgressDialog pd;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_sent_notice);
        mynotice_listview = (PullToRefreshListView) findViewById(R.id.mynotice_listview);
        mynotice_add_notice = (ImageView) findViewById(R.id.mynotice_add_notice);
        noticeList = new ArrayList<NoticeEntity>();
        notice_OutEntity = new Notice_OutEntity();
        notice_ListAdapter = new MyNoticeListAdapter(this, noticeList);
        mynotice_listview.setAdapter(notice_ListAdapter);
        mynotice_listview.setMode(Mode.BOTH);
        pd = new ECProgressDialog(MeSentNoticeActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        ILoadingLayout startLabels = mynotice_listview.getLoadingLayoutProxy(
                true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        mynotice_add_notice.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MeSentNoticeActivity.this,
                        CreatNewNoticeActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    overridePendingTransition(R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);
                }

            }
        });
        mynotice_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

            @SuppressWarnings("rawtypes")
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                //下拉刷新的时候，page归位，数据源清空。
                page = 1;
                noticeList.clear();
                mynotice_listview.setRefreshing(true);
                try {
                    initLocalData();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @SuppressWarnings("rawtypes")
            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                mynotice_listview.setRefreshing(true);
                page++;
                try {
                    initLocalData();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
        mynotice_listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                NoticeEntity clickNoticeEntity = new NoticeEntity();
                clickNoticeEntity = (NoticeEntity) notice_ListAdapter
                        .getItem(position - 1);
                String informId = clickNoticeEntity.getInformId();
                startActivity(new Intent(MeSentNoticeActivity.this,
                        NoticeDetailActivity_new.class).putExtra(
                        "KEY_CLICK_NOTICE", informId).putExtra("ItemFrom", "MeSentNoticeActivity"));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    overridePendingTransition(
                            R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);
                }
            }
        });
        try {
            initLocalData();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 模拟本地数据，真实数据的时候就要删掉哦
    private void initLocalData() throws JSONException {
        JSONObject noticeJson = new JSONObject();
        noticeJson.put("page", page);
        noticeJson.put("pageSize", Constant.pageSize);
        noticeJson.put("send", PreferenceManager.getInstance()
                .getCurrentUserId());
        Log.i("noticeJson", noticeJson.toString());
        CommonUtils.setCommonJson(MeSentNoticeActivity.this, noticeJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        OkHttpUtils.postString().url(Constant.GETNOTICELIST_URL)
                .content(noticeJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        pd.dismiss();
                        mynotice_listview.onRefreshComplete();
                        try {
                            JSONObject temp = new JSONObject(arg0);
                            if (temp.getInt("code") == 1) {
                                notice_OutEntity = new Gson().fromJson(arg0,
                                        Notice_OutEntity.class);
                                if (notice_OutEntity.getCode() == 1) {
                                    noticeList.addAll(notice_OutEntity.getData());
//                                    Log.i("MeSentNoticeActivity", noticeList.get(0)
//                                            .getContent().toString());
                                    notice_ListAdapter.notifyDataSetChanged();
                                }
                            } else if (temp.getInt("code") == 2) {
                                showToastShort("没有更多通知");
                            }
                        } catch (JSONException e) {
                            showToastShort("没有更多通知");
                            e.printStackTrace();
                        } catch (Exception e) {
                            showToastShort("请求失败");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        pd.dismiss();
                        mynotice_listview.onRefreshComplete();
                        showToastShort("获取信息失败");
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
