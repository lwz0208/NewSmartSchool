package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.JobAdapter2;
import com.wust.newsmartschool.domain.JobItem;
import com.wust.newsmartschool.utils.HttpServer;
import com.wust.newsmartschool.utils.MyListView;

import java.util.ArrayList;


public class RecruitmentActivity extends Activity implements MyListView.IXListViewListener {


    private ProgressBar progressBar;
    private ArrayList<JobItem> jobList = new ArrayList<JobItem>();
    private MyListView listView;
    private JobAdapter2 adapter;
    private int pagenum = 1;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                jobList = (ArrayList<JobItem>) msg.obj;
                Log.i("TAG", "jobList"+jobList.size());
                adapter=new JobAdapter2(getApplicationContext(),jobList);
                listView.setAdapter(adapter);
                pagenum = 1;
                progressBar.setVisibility(View.GONE);

                if (jobList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "暂无信息",
                            Toast.LENGTH_LONG).show();
                }
            } else if (msg.what == 102) {
                ArrayList<JobItem> jobList2 = (ArrayList<JobItem>) (msg.obj);
                if (jobList2.size() == 0) {
                    Toast.makeText(getApplicationContext(), "已是最后一条",
                            Toast.LENGTH_SHORT).show();
                } else {
                    jobList.addAll(jobList2);
                    adapter.notifyDataSetChanged();
                    pagenum++;
                }
            } else if (msg.what == 101) {
                Toast.makeText(getApplicationContext(), "网速不给力",
                        Toast.LENGTH_SHORT).show();
            }

            listView.stopRefresh();
            listView.stopLoadMore();
            listView.setRefreshTime("刚刚");
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recruitment);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        listView = (MyListView) findViewById(R.id.listView);
        listView.setPullLoadEnable(true);
        listView.setXListViewListener(this);

        getNewsList(0);
    }

    private void getNewsList(final int pagenum) {
        Runnable runnable = new Runnable() {
            public void run() {
                HttpServer httpServer = new HttpServer();
                String urlString = "http://202.114.242.198:8090/EmploymentInformation.php?pagenum="
                        + pagenum;
                String jsonString = httpServer.getData(urlString);
                if (jsonString != null) {
                    ArrayList<JobItem> jobList = new ArrayList<JobItem>();
                    jobList = httpServer.getjobList(jsonString);

                    if (jobList != null) {
                        if (pagenum == 0) {
                            handler.sendMessage(handler.obtainMessage(100,
                                    jobList));
                        } else {
                            handler.sendMessage(handler.obtainMessage(102,
                                    jobList));
                        }
                    }
                } else {
                    handler.sendMessage(handler.obtainMessage(101));
                }
            }
        };
        new Thread(runnable).start();
    }

    public void onRefresh() {
        getNewsList(0);
    }

    public void onLoadMore() {
        getNewsList(pagenum);
    }

}

