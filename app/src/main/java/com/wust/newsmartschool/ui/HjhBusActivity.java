package com.wust.newsmartschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.SchoolBusAdapter;
import com.wust.newsmartschool.domain.SchoolBus;
import com.wust.newsmartschool.utils.URL_UNIVERSAL;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;


public class HjhBusActivity extends AppCompatActivity {
    private Toolbar toolbar;
    List<SchoolBus> busInfoList;
    private ListView listView;
    private SchoolBusAdapter adapter;
    private int page = 1;
    private int pageSize = 20;

    private PtrClassicFrameLayout ptrClassicFrameLayout;
    Handler handler = new Handler();

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hjh_bus);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.test_list_view_frame);
        listView = (ListView) findViewById(R.id.listView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        busInfoList = new ArrayList<>();
        adapter = new SchoolBusAdapter(HjhBusActivity.this, busInfoList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HjhBusActivity.this, SchoolBusDetailActivity.class);
                intent.putExtra("Url", "http://www.wust.edu.cn/news/" + busInfoList.get(position).getID() + ".html");
                startActivity(intent);
            }
        });

        ptrClassicFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrClassicFrameLayout.autoRefresh(true);
            }
        }, 150);

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        initNoticeList(true);
                    }
                }, 1500);
            }
        });

        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        initNoticeList(false);
                    }
                }, 1000);
            }
        });

    }

    private void initNoticeList(final boolean refreshFalg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("page", page);
        jsonObject.put("pageSize", 20);
        OkHttpUtils.postString().url(URL_UNIVERSAL.URL_SCHOOL_BUS).content(jsonObject.toJSONString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.i("NoticeResult", "校车接口访问失败：" + call + "---" + e);
                        ptrClassicFrameLayout.refreshComplete();
                        Toast.makeText(HjhBusActivity.this, "网络开小差了哦", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.i("NoticeResult", "校车接口访问成功：" + response);
                        JSONObject jsonObject = JSON.parseObject(response);
                        if(jsonObject.getIntValue("code") == 1) {
                            List<SchoolBus> tempBusInfoList = JSON.parseArray(jsonObject.getJSONArray("data").toJSONString(),SchoolBus.class);
                            if(tempBusInfoList.size() != 0) {
                                if(page == 1)
                                    busInfoList.clear();
                                busInfoList.addAll(tempBusInfoList);
                                adapter.notifyDataSetChanged();
                                if(tempBusInfoList.size() < pageSize)
                                    ptrClassicFrameLayout.setLoadMoreEnable(false);
                                else {
                                    ptrClassicFrameLayout.setLoadMoreEnable(true);
                                    page++;
                                }
                            } else {
                                Toast.makeText(HjhBusActivity.this, "没有更多数据了哦 ", Toast.LENGTH_SHORT).show();
                                ptrClassicFrameLayout.setLoadMoreEnable(false);
                            }
                        } else if(jsonObject.getIntValue("code") == 0) {
                            Toast.makeText(HjhBusActivity.this, "暂时没有数据哦", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HjhBusActivity.this, "服务器异常...", Toast.LENGTH_SHORT).show();
                        }
                        if(refreshFalg)
                            ptrClassicFrameLayout.refreshComplete();
                        else
                            ptrClassicFrameLayout.loadMoreComplete(true);
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

