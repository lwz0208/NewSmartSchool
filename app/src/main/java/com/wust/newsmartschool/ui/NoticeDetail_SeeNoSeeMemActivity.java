package com.wust.newsmartschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.NoticeDetailSeeMembers_Adapter;
import com.wust.newsmartschool.domain.NoticeDetail_SeeMemList;
import com.wust.newsmartschool.views.ECProgressDialog;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.CommonUtils;
import com.wust.easeui.utils.PreferenceManager;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

public class NoticeDetail_SeeNoSeeMemActivity extends Activity {
    String TAG = "NoticeDetail_SeeNoSeeMemActivity_Debugs";
    private int getStatus;
    private String getInformId = "";
    private ECProgressDialog pd;
    private NoticeDetailSeeMembers_Adapter members_ListAdapter;
    private List<NoticeDetail_SeeMemList.MenbersBean> noticeDetail_seeMemList;
    /**
     * 一些控件
     */
    private String title_display;
    private TextView noticedetail_memberstitle;
    private PullToRefreshListView noticedetailmembers_listview;
    //刷新的一些操作参数
    private int page = 1;
    //回调给通知详情的数据，如果已看未看数据不对应则刷新。
    int returnNum = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail__see_no_see_mem);
        getStatus = getIntent().getIntExtra("status", -1);
        getInformId = getIntent().getStringExtra("noticeid");
        noticeDetail_seeMemList = new ArrayList<>();
        //绑定控件
        noticedetail_memberstitle = (TextView) findViewById(R.id.noticedetail_memberstitle);
        noticedetailmembers_listview = (PullToRefreshListView) findViewById(R.id.noticedetailmembers_listview);
        members_ListAdapter = new NoticeDetailSeeMembers_Adapter(NoticeDetail_SeeNoSeeMemActivity.this, noticeDetail_seeMemList);
        noticedetailmembers_listview.setAdapter(members_ListAdapter);
        noticedetailmembers_listview.setMode(PullToRefreshBase.Mode.BOTH);
        pd = new ECProgressDialog(NoticeDetail_SeeNoSeeMemActivity.this);
        pd.show();
        try {
            getWhoSeeWhoNoSeeMenberList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //展示的名字在别的地方要用喔，所以就这么弄了。
        if (getStatus != -1) {
            if (getStatus == 0)
                title_display = "未看人员列表";
            else if (getStatus == 1)
                title_display = "已看人员列表";
        } else {
            title_display = "人员列表";
        }
        noticedetail_memberstitle.setText(title_display);
        noticedetailmembers_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                    String beclickuserId = ((NoticeDetail_SeeMemList.MenbersBean) members_ListAdapter.getItem(position - 1)).getUserId();
                                                                    startActivity(new Intent(NoticeDetail_SeeNoSeeMemActivity.this,
                                                                            DeptMemInfoActivity2.class).putExtra("userId", beclickuserId));
                                                                }
                                                            }

        );

        noticedetailmembers_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

            @SuppressWarnings("rawtypes")
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                //下拉刷新的时候，page归位，数据源清空。
                page = 1;
                noticeDetail_seeMemList.clear();
                noticedetailmembers_listview.setRefreshing(true);
                try {
                    getWhoSeeWhoNoSeeMenberList();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @SuppressWarnings("rawtypes")
            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                noticedetailmembers_listview.setRefreshing(true);
                page++;
                try {
                    getWhoSeeWhoNoSeeMenberList();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
    }


    private void getWhoSeeWhoNoSeeMenberList() throws JSONException {
        JSONObject whoJson = new JSONObject();
        whoJson.put("id", getInformId);
        whoJson.put("page", page);
        whoJson.put("pageSize", Constant.pageSize);
        whoJson.put("status", getStatus);
        CommonUtils.setCommonJson(NoticeDetail_SeeNoSeeMemActivity.this, whoJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i(TAG, whoJson.toString());

        OkHttpUtils.postString().url(Constant.GETWHOSEEWHONOSEE_URL)
                .content(whoJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                             @Override
                             public void onResponse(String arg0) {
                                 pd.dismiss();
                                 noticedetailmembers_listview.onRefreshComplete();
                                 Log.e(TAG, arg0.toString());
                                 JSONObject jObject;
                                 try {
                                     jObject = new JSONObject(arg0);
                                     if (jObject.getInt("code") == 1) {
                                         NoticeDetail_SeeMemList noticeDetail_seeMemEntity = new Gson().fromJson(arg0, NoticeDetail_SeeMemList.class);
                                         returnNum = noticeDetail_seeMemEntity.getRecords();
                                         noticedetail_memberstitle.setText(title_display + "(" + noticeDetail_seeMemEntity.getRecords() + ")");
                                         if (noticeDetail_seeMemEntity.getData().size() != 0) {
                                             noticeDetail_seeMemList.addAll(noticeDetail_seeMemEntity.getData());
                                             members_ListAdapter.notifyDataSetChanged();
                                         }
                                     } else if (jObject.getInt("code") == 2) {
                                         Toast.makeText(getApplicationContext(),
                                                 "暂无更多数据", Toast.LENGTH_SHORT).show();
                                     } else {
                                         Toast.makeText(getApplicationContext(),
                                                 "暂无数据", Toast.LENGTH_SHORT).show();
                                     }
                                     setResult(RESULT_OK, new Intent().putExtra("returnNum", returnNum));
                                 } catch (JSONException e) {
                                     Toast.makeText(getApplicationContext(),
                                             "暂无数据", Toast.LENGTH_SHORT).show();
                                     e.printStackTrace();
                                 }
                             }

                             @Override
                             public void onError(Call arg0, Exception arg1) {
                                 pd.dismiss();
                                 noticedetailmembers_listview.onRefreshComplete();
                                 Toast.makeText(getApplicationContext(), "数据获取失败",
                                         Toast.LENGTH_SHORT).show();

                             }
                         }

                );

    }

    public void back(View view) {
        finish();
        if (android.os.Build.VERSION.SDK_INT > 5) {
            overridePendingTransition(
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }

}
