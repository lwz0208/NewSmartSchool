package com.wust.newsmartschool.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wust.newsmartschool.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;


public class SchoolBusDetailActivity extends AppCompatActivity {
    private WebView webview;
    private ProgressBar pBar;
    private Toolbar toolbar;
    private String url;
    private String content;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    pBar.setVisibility(View.GONE);
                    content = (String) msg.obj;
                    setupView(content);
                    break;
                case 200:
                    pBar.setVisibility(View.GONE);
                    Toast.makeText(SchoolBusDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_bus_detail);

        webview = (WebView) findViewById(R.id.news_webview);
        pBar = (ProgressBar) findViewById(R.id.load_progressbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        url = getIntent().getStringExtra("Url");
        initData();
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).get();
                    String mode = "<font style='font-weight:bold;color:black;font-size:20px;'>" + doc.select("div.title > h3").html()
                            + "</font><br>"
                            + "<font style='color:gray;font-size:14px;line-height:2.5;'>" + doc.select("span.pubtime").html().substring(5, 21)
                            + "&nbsp;&nbsp;&nbsp;&nbsp;"
                            + doc.select("span.bm").html().substring(5)
                            + "</font>"
                            + doc.select("div.xwcon").html();
                    handler.sendMessage(handler.obtainMessage(100, mode));
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(200);
                }
            }
        }).start();
    }

    private void setupView(String doc) {
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 设置默认缩放方式尺寸是far
        settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        webview.loadDataWithBaseURL(null, doc, "text/html", "utf-8", null);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            // 页面开始加载
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            // 页面加载完成
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            // WebView加载的所有资源url
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
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

