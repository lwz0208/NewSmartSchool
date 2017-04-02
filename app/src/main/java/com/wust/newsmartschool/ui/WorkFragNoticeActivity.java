package com.wust.newsmartschool.ui;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.NoticeListAdapter;
import com.wust.newsmartschool.domain.NoticeEntity;
import com.wust.newsmartschool.domain.Notice_OutEntity;
import com.wust.easeui.utils.PreferenceManager;
import com.wust.newsmartschool.views.ECProgressDialog;
import com.wust.easeui.utils.CommonUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.wust.easeui.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class WorkFragNoticeActivity extends BaseActivity {
    private LinearLayout ll_receive_notice;
    private ImageView add_notice;
    PullToRefreshListView workfrag_notice_listview;
    Notice_OutEntity notice_OutEntity;
    List<NoticeEntity> noticeList;
    private NoticeListAdapter notice_ListAdapter;
    private ECProgressDialog pd;
    private ImageView search_notice;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_frag_notice);
        search_notice = (ImageView) findViewById(R.id.search_notice);
        ll_receive_notice = (LinearLayout) findViewById(R.id.ll_receive_notice);
        workfrag_notice_listview = (PullToRefreshListView) findViewById(R.id.workfrag_notice_listview);
        add_notice = (ImageView) findViewById(R.id.add_notice);
        noticeList = new ArrayList<NoticeEntity>();
        notice_OutEntity = new Notice_OutEntity();
        //I接下来就是干货了
        ILoadingLayout startLabelse = workfrag_notice_listview.getLoadingLayoutProxy(true, false);
        startLabelse.setPullLabel("下拉可以加载更多");// 刚下拉时，显示的提示
        startLabelse.setRefreshingLabel("加载中");// 刷新时
        startLabelse.setReleaseLabel("松开加载更多");// 下来达到一定距离时，显示的提示
        ILoadingLayout endLabelsr = workfrag_notice_listview.getLoadingLayoutProxy(false, true);
        endLabelsr.setPullLabel("上拉可以刷新");// 刚下拉时，显示的提示
        endLabelsr.setLastUpdatedLabel("正在刷新");// 刷新时
        endLabelsr.setReleaseLabel("松开后刷新");// 下来达到一定距离时，显示的提示
        notice_ListAdapter = new NoticeListAdapter(this, noticeList);
        pd = new ECProgressDialog(WorkFragNoticeActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        workfrag_notice_listview.setAdapter(notice_ListAdapter);
        workfrag_notice_listview.setMode(Mode.BOTH);
        search_notice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkFragNoticeActivity.this,
                        SearchNoticeActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    overridePendingTransition(R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);
                }

            }
        });
        add_notice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(WorkFragNoticeActivity.this,
                        CreatNewNoticeActivity.class), 0);
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    overridePendingTransition(R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);
                }

            }
        });
        ll_receive_notice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkFragNoticeActivity.this,
                        MeSentNoticeActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    overridePendingTransition(R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);
                }

            }
        });


        workfrag_notice_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

            @SuppressWarnings("rawtypes")
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                //下拉刷新的时候，page归位，数据源清空。
                page = 1;
                noticeList.clear();
                workfrag_notice_listview.setRefreshing(true);
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
                workfrag_notice_listview.setRefreshing(true);
                page++;
                try {
                    initLocalData();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });

        workfrag_notice_listview
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int position, long arg3) {
                        ((TextView) arg1.findViewById(R.id.unread_notice_number)).setVisibility(View.INVISIBLE);
                        NoticeEntity clickNoticeEntity = new NoticeEntity();
                        clickNoticeEntity = (NoticeEntity) notice_ListAdapter
                                .getItem(position - 1);
                        String informId = clickNoticeEntity.getInformId();
                        startActivity(new Intent(WorkFragNoticeActivity.this,
                                NoticeDetailActivity_new.class).putExtra(
                                "KEY_CLICK_NOTICE", informId).putExtra("ItemFrom", "WorkFragNoticeActivity"));
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
            e.printStackTrace();
        }
    }

    // 模拟本地数据，真实数据的时候就要删掉哦
    private void initLocalData() throws JSONException {
        JSONObject noticeJson = new JSONObject();
        noticeJson.put("page", page);
        noticeJson.put("pageSize", Constant.pageSize);
        noticeJson.put("accept", PreferenceManager.getInstance()
                .getCurrentUserId());
        CommonUtils.setCommonJson(WorkFragNoticeActivity.this, noticeJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i("noticeJson", noticeJson.toString());

        OkHttpUtils.postString().url(Constant.GETNOTICELIST_URL)
                .content(noticeJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        workfrag_notice_listview.onRefreshComplete();
                        pd.dismiss();
//                        Log.i("onResponse", arg0.toString());
                        try {
                            JSONObject temp = new JSONObject(arg0);
                            if (temp.getInt("code") == 1) {
                                notice_OutEntity = new Gson().fromJson(arg0,
                                        Notice_OutEntity.class);
                                noticeList.addAll(notice_OutEntity.getData());
//                                    Log.i("notice_OutEntity", noticeList.get(0)
//                                            .getContent().toString());
                                notice_ListAdapter.notifyDataSetChanged();
                            } else if (temp.getInt("code") == 2) {
                                showToastShort("没有更多通知");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        Log.i("onResponse", arg0.toString());
                        workfrag_notice_listview.onRefreshComplete();
                        pd.dismiss();
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            pd.show();
            page = 1;
            noticeList.clear();
            try {
                initLocalData();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void back(View view) {
        finish();
        if (android.os.Build.VERSION.SDK_INT > 5) {
            overridePendingTransition(
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }

    }
}
