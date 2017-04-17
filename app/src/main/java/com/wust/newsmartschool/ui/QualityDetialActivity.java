package com.wust.newsmartschool.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.wust.newsmartschool.domain.News;
import com.wust.newsmartschool.utils.HttpServer;


@SuppressWarnings("deprecation")
public class QualityDetialActivity extends Activity
{
    private WebView webView;
    private ProgressBar progressBar;
    private String serveid;
    private News newsdetail;
    private String urlString;
    // 以下是显示数据用组件
    private TextView tv_title;
    private TextView tv_time;
    private TextView tv_hits;

    private ImageView img_goback;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == 100)
            {
                newsdetail = (News) msg.obj;
                //tv_title.setText(newsdetail.getNewsTitle());
                //tv_time.setText(newsdetail.getNewsTime());
                //tv_hits.setText("点击：" + newsdetail.getNewsTitle());
                webView.setVisibility(View.VISIBLE);

                final String mimeType = "text/html";
                final String encoding = "utf-8";
                //String html = newsdetail.getNewsTitle();
                //webView.loadDataWithBaseURL(null, html, mimeType, encoding, "");
                progressBar.setVisibility(View.GONE);
                // webView.addJavascriptInterface(new
                // ServeDetail(),"getServeDetail");// 向网页暴露本地接口
                // webView.loadUrl("http://202.114.255.74/huangguangplat/plamt.php?id="+id);//暂未数据
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
        setContentView(R.layout.activity_quality_detial);

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
                //Toast.makeText(QualityDetialActivity.this, serveid, Toast.LENGTH_LONG).show();
                urlString = "http://sztz.wust.edu.cn/QualityDetail.php?id="+serveid;
                Log.e("素质拓展",urlString);
                HttpServer httpServer = new HttpServer();
                String jsonString = httpServer.getData(urlString);
                if (jsonString != null)
                {
                    News newsItem = new News();
                    //newsItem = httpServer.getQualityDetail(jsonString);
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
        tv_title = (TextView) findViewById(R.id.tv_quality_title);
        tv_time = (TextView) findViewById(R.id.tv_quality_time);
        tv_hits = (TextView) findViewById(R.id.tv_quality_clicked);
        webView = (WebView) findViewById(R.id.webview_quality);
        progressBar = (ProgressBar) findViewById(R.id.progressbar_job);
        serveid = getIntent().getStringExtra("id");

        webView = (WebView) findViewById(R.id.webview_quality);
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
                QualityDetialActivity.this.finish();
            }
        });
    }

}

