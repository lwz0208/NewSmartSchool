package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.Psy_ExpertListViewAdapter;
import com.wust.newsmartschool.domain.PsychologistInfoItem;
import com.wust.newsmartschool.utils.HttpServer;

import java.util.ArrayList;

public class Psy_ExpertIntroduceActivity extends Activity {
    private TextView head;
    private ImageView back;
    private ProgressBar progressBar;
    private Psy_ExpertListViewAdapter myAdapter;
    private ListView myListView;
    private ArrayList<PsychologistInfoItem> dataArrayList = new ArrayList<PsychologistInfoItem>();

    private Handler myHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if(msg.what==100){
                myAdapter = new Psy_ExpertListViewAdapter(Psy_ExpertIntroduceActivity.this, dataArrayList);
                myListView.setAdapter(myAdapter);
                myListView.setClickable(false);
                progressBar.setVisibility(View.GONE);
            }
        }

    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_psy__expert_introduce);
        initView();
        getExpertinfoList();
        myListView = (ListView) findViewById(R.id.psy_expertlist);
    }

    private void initView() {
        // TODO Auto-generated method stub
        head = (TextView) findViewById(R.id.head_title);
        back = (ImageView) findViewById(R.id.goback);
        head.setText("专家介绍");
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Psy_ExpertIntroduceActivity.this.finish();
            }
        });
        progressBar=(ProgressBar)findViewById(R.id.progressbar);
    }

    private void getExpertinfoList(){
        Runnable runnable=new Runnable() {
            public void run() {
                HttpServer httpServer=new HttpServer();
                String urlString="http://202.114.242.198:8090/psyconcertinfo.php";
                String jsonString=httpServer.getData(urlString);
                if(jsonString!=null){
                    dataArrayList=httpServer.getExpertInfoList(jsonString);
                    myHandler.sendEmptyMessage(100);
                }
            }
        };
        new Thread(runnable).start();
    }
}

