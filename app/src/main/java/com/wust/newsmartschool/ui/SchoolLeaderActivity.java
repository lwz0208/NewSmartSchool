package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.LeaderAdapter;
import com.wust.newsmartschool.domain.SchoolLeader;
import com.wust.newsmartschool.utils.URL_UNIVERSAL;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

public class SchoolLeaderActivity extends Activity {
    private ImageView backImageView;
    private ListView listView;
    List<SchoolLeader> leaderList;
    private LeaderAdapter adapter;

    private PtrClassicFrameLayout ptrClassicFrameLayout;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_school_leader);

        ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.test_list_view_frame);
        backImageView = (ImageView) findViewById(R.id.iv_back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = (ListView) findViewById(R.id.listView);

        leaderList = new ArrayList<>();
        adapter = new LeaderAdapter(SchoolLeaderActivity.this, leaderList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SchoolLeaderActivity.this, LeaderDetailActivity.class);
                intent.putExtra("leader", leaderList.get(position));
                startActivity(intent);
            }
        });

        ptrClassicFrameLayout.setLoadMoreEnable(false);
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
                        initLeaderList();
                    }
                }, 1500);
            }
        });
    }

    private void initLeaderList() {
        JSONObject jsonObject = new JSONObject();
        OkHttpUtils.postString().url(URL_UNIVERSAL.URL_SCHOOL_LEADER).content(jsonObject.toJSONString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.i("LeaderResult", "学校领导接口访问失败：" + call + "---" + e);
                        ptrClassicFrameLayout.refreshComplete();
                        Toast.makeText(SchoolLeaderActivity.this, "网络开小差了哦", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.i("LeaderResult", "学校领导接口访问成功：" + response);
                        JSONObject jsonObject = JSON.parseObject(response);
                        if(jsonObject.getIntValue("code") == 1) {
                            List<SchoolLeader> tempLeaderList = JSON.parseArray(jsonObject.getJSONArray("data").toJSONString(),SchoolLeader.class);
                            leaderList.clear();
                            leaderList.addAll(tempLeaderList);
                            adapter.notifyDataSetChanged();
                        } else if(jsonObject.getIntValue("code") == 0){
                            Toast.makeText(SchoolLeaderActivity.this, "暂时没有数据哦", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SchoolLeaderActivity.this, "服务器异常...", Toast.LENGTH_SHORT).show();
                        }
                        ptrClassicFrameLayout.refreshComplete();
                    }
                });
    }
}

