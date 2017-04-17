package com.wust.newsmartschool.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.JobDeatils;
import com.wust.newsmartschool.utils.HttpServer;

public class JobMessageDetailActivity extends Activity {

    private WebView webView;
    private ProgressBar progressBar;
    private String Id;
    private JobDeatils detail;
    private String urlString;

    private TextView tv_title,t;

    private ImageView img_goback;


    private Handler handler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == 100)
            {
                detail=(JobDeatils) msg.obj;

                webView.setVisibility(View.VISIBLE);

                final String mimeType = "text/html";
                final String encoding = "utf-8";
                String html = detail.getJobDeatilContent();
                webView.loadDataWithBaseURL(null, html, mimeType, encoding, "");
                progressBar.setVisibility(View.GONE);
            }
            else if (msg.what == 101)
            {
                Toast.makeText(getApplicationContext(), "网速不给力",
                        Toast.LENGTH_SHORT).show();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_job_message_detail);
        t = (TextView) findViewById(R.id.head_title);
        t.setText("就业公告详情");
        InitView();

        getDetail();
    }

    private void getDetail()
    {
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {

                urlString = "http://202.114.242.198:8090/EmploymentAnnounceDetail.php?url="+Id;
                HttpServer httpServer = new HttpServer();
                String jsonString = httpServer.getData(urlString);
                if (jsonString != null)
                {
                    JobDeatils newsItem = new JobDeatils();
                    newsItem = httpServer.getRecruitmentMessageDeatil(jsonString);
                    handler.sendMessage(handler.obtainMessage(100, newsItem));
                }
                else
                {
                    handler.sendMessage(handler.obtainMessage(101));
                }
            }
        };
        new Thread(runnable).start();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void InitView()
    {
        tv_title = (TextView) findViewById(R.id.tv_job_title);
        tv_title.setText(getIntent().getStringExtra("title"));

        webView = (WebView) findViewById(R.id.job_webview);
        progressBar = (ProgressBar) findViewById(R.id.progressbarjob);
        Id = getIntent().getStringExtra("id");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(true);
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result)
            {
                return super.onJsAlert(view, url, message, result);
            }

        });

        img_goback =  (ImageView)findViewById(R.id.goback);
        img_goback.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                JobMessageDetailActivity.this.finish();
            }
        });
    }

}

