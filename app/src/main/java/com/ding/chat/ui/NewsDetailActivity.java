package com.ding.chat.ui;

import com.ding.chat.R;
import com.ding.easeui.utils.CommonUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class NewsDetailActivity extends BaseActivity {
    private String TAG = "NewsDetailActivity_Debugs";
    private WebView webview;
    private String url;
    private String content;
    private ProgressBar pBar;
    private ImageView newsListImage;

    public static String img_center = "<style>*{margin:0px;padding:0px}img{margin:0 auto;width:90%;display:block;height:auto !important}h4{text-align:center !important}</style>";
    public static final String MIMETYPE = "text/html";
    public static final String ENCODING = "utf-8";

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
                    showToastShort("网络错误");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        webview = (WebView) findViewById(R.id.news_webview);
        pBar = (ProgressBar) findViewById(R.id.load_progressbar);
        url = getIntent().getStringExtra("url");
        url = url.contains("http") ? url : "http://www.wh5yy.com" + url;
        newsListImage = (ImageView) findViewById(R.id.newsList);
        newsListImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewsDetailActivity.this, SettingActivity.class));
            }
        });
        initData();
    }

    private void initData() {
        if (!CommonUtils.isNetWorkConnected(NewsDetailActivity.this)) {
            handler.sendEmptyMessage(200);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).get();
                    String mode = doc.select("div.ny_news_list").html();
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
        webview.loadDataWithBaseURL(null, img_center + doc, MIMETYPE, ENCODING, null);

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

    public void back(View view) {
        finish();
        if (android.os.Build.VERSION.SDK_INT > 5) {
            overridePendingTransition(
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }

}
