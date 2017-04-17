package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.NewsAdapter2;
import com.wust.newsmartschool.domain.News;
import com.wust.newsmartschool.utils.HttpServer;
import com.wust.newsmartschool.utils.MyListView;

import java.util.ArrayList;
import java.util.List;

public class QualityActivity extends Activity implements MyListView.IXListViewListener
{

    private MyListView myListView;
    private Intent mIntent;
    private int id = 1, page = 1;
    private String sharedPreferenceFileName;
    private ArrayList<News> newsList = new ArrayList<News>();
    private NewsAdapter2 adapter;

    private static final int INIT_PAGE = 100;
    private static final int LOAD_PAGE = 101;
    private static final int NO_NETWOR = 102;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if (msg.what == INIT_PAGE)
            {
                newsList = (ArrayList<News>) (msg.obj);
                adapter = new NewsAdapter2(getApplicationContext(), newsList);
                myListView.setAdapter(adapter);
                page = 2;
                if (newsList.size() == 0)
                {
                    Toast.makeText(getApplicationContext(), "暂无信息",
                            Toast.LENGTH_LONG).show();
                }
            }
            else if (msg.what == LOAD_PAGE)
            {
                ArrayList<News> newsList2 = (ArrayList<News>) (msg.obj);
                if (newsList2.size() == 0)
                {
                    Toast.makeText(getApplicationContext(), "已是最后一条",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    newsList.addAll(newsList2);
                    adapter.notifyDataSetChanged();
                    page++;
                }

            }
            else if (msg.what == NO_NETWOR)
            {
                Toast.makeText(getApplicationContext(), "网速不给力",
                        Toast.LENGTH_SHORT).show();
            }
            myListView.stopRefresh();
            myListView.stopLoadMore();
            myListView.setRefreshTime("刚刚");
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_quality);

        id = getIntent().getIntExtra("id", 1);
        sharedPreferenceFileName = "Quality" + id;

        InitView();
        loadCache();
        getMessageList(1);
    }

    private void InitView()
    {

        myListView = (MyListView) findViewById(R.id.QNewslistView);
        myListView.setPullLoadEnable(true);
        myListView.setXListViewListener(this);

    }

    private void loadCache()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(
                sharedPreferenceFileName, Context.MODE_APPEND);
        String jsonString = sharedPreferences.getString("EDFR-ERFW-AAAB-SQEB",
                "");
        if (!(jsonString.equals("")))
        { // 有缓存
            HttpServer httpServer = new HttpServer();
            List<News> newsList = httpServer.getQualityList(jsonString);
            // 用缓存数据显示
            mHandler.obtainMessage(100, newsList).sendToTarget();
        }
    }

    private void getMessageList(final int page)
    {
        Runnable runnable = new Runnable()
        {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                HttpServer httpServer = new HttpServer();
                String url = "http://sztz.wust.edu.cn/QualityList.php?id=" + id+ "&page=" + page;

                String jsonString = httpServer.getData(url);
                if (jsonString != null&&jsonString!="")
                {
                    Log.d("数据", jsonString);
                    List<News> newsList = new ArrayList<>();
                    newsList = httpServer.getQualityList(jsonString);

                    if (newsList != null && newsList.size() != 0)
                    {
                        if (page == 1)
                        {

                            SharedPreferences sharedPreferences = getSharedPreferences(
                                    sharedPreferenceFileName,
                                    Context.MODE_APPEND);
                            String cacheString = sharedPreferences.getString(
                                    "EDFR-ERFW-AAAB-SQEB", "");
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            if (!cacheString.equals(""))
                            {
                                editor.remove(sharedPreferenceFileName);
                            }
                            editor.putString("EDFR-ERFW-AAAB-SQEB", jsonString);
                            editor.commit();
                            mHandler.obtainMessage(INIT_PAGE, newsList)
                                    .sendToTarget();
                        }
                        else
                        {
                            mHandler.obtainMessage(LOAD_PAGE, newsList)
                                    .sendToTarget();
                        }
                    }
                }
                else
                {
                    mHandler.obtainMessage(NO_NETWOR).sendToTarget();
                }
            }
        };

        new Thread(runnable).start();
    }

    @Override
    public void onRefresh()
    {
        // TODO Auto-generated method stub
        getMessageList(1);
    }

    @Override
    public void onLoadMore()
    {
        // TODO Auto-generated method stub
        getMessageList(page);
    }
}

