package com.wust.newsmartschool.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.DocumentDetail;
import com.wust.newsmartschool.utils.DocumentServer;

import java.io.IOException;
import java.text.SimpleDateFormat;


@SuppressLint("JavascriptInterface") public class DocumentDetailActivity extends Activity {
    protected static final String TAG = "DocumentDetailActivity";
    private ProgressBar progressBar;
    private String newsid;
    private String push;
    private TextView tv_snotice_title;
    private TextView tv_notice_time;
    private TextView tv_notice_djsl;
    private WebView wv_snotice_content;
    private DocumentDetail documentDetail;
    private Handler mHandler;
    private WebSettings mWebSettings;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            progressBar.setVisibility(View.GONE);
            Bundle bundle = msg.getData();
            tv_snotice_title.setText(bundle.getString("notice_title"));
            tv_notice_time.setText(toTime(bundle.getString("fbsj")));
            tv_notice_djsl.setText("点击:"+bundle.getString("djsl"));
            final String mimeType = "text/html";
            final String encoding = "utf-8";
            final String html = bundle.getString("notice_content");// TODO 从本地读取HTML文件

            wv_snotice_content.loadDataWithBaseURL(null, html,
                    mimeType, encoding, "");
        }

    };
    private String toTime(String xwbt) {
        String str = xwbt.substring(6, xwbt.length() - 2);
        String time = new SimpleDateFormat("yyyy年MM月dd日").format(Long
                .parseLong(str));

        return time;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_document_detail);
        initView();

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        ImageView backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (push != null) {
                    Intent intent = new Intent(DocumentDetailActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });
        newsid = getIntent().getStringExtra("newsid");
        push = getIntent().getStringExtra("push");

        getNewsDetail(newsid);

    }

    private void initView() {
        tv_snotice_title = (TextView) findViewById(R.id.tv_snotice_title);
        tv_notice_time = (TextView) findViewById(R.id.tv_notice_time);
        tv_notice_djsl = (TextView) findViewById(R.id.tv_notice_djsl);
        wv_snotice_content = (WebView) findViewById(R.id.wv_snotice_content);
        // 设置支持JavaScript等
        mWebSettings = wv_snotice_content.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setLightTouchEnabled(true);
        mWebSettings.setSupportZoom(true);
        wv_snotice_content.setHapticFeedbackEnabled(false);
        // mWebView.setInitialScale(0); // 改变这个值可以设定初始大小

        // 重要,用于与页面交互!
        wv_snotice_content.addJavascriptInterface(new Object() {
            @SuppressWarnings("unused")
            public void oneClick(final String locX, final String locY) {// 此处的参数可传入作为js参数
                mHandler.post(new Runnable() {
                    public void run() {
                        wv_snotice_content.loadUrl("javascript:shows(" + locX
                                + "," + locY + ")");
                    }
                });
            }
        }, "demo");// 此名称在页面中被调用,方法如下:
        // <body onClick="window.demo.clickOnAndroid(event.pageX,event.pageY)">



    }

    private void getNewsDetail(final String newsid) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // String urlString = GlobalVar.serverClient +
                // "getNewsDetail?newsid="+newsid;
                // HttpServer httpServer = new HttpServer();
                // String jsonString = httpServer.getData(urlString);
                String url = "http://202.114.255.75:88/SchoolData/GetSDocumentsDetail?id="
                        + newsid;
                try {
                    documentDetail = DocumentServer.parseDetailJSON(url,
                            DocumentDetailActivity.this);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Bundle bundle = new Bundle();
                Message msg = Message.obtain();
                msg.setData(bundle);
                bundle.putString("notice_title", documentDetail.getXwbt());
                bundle.putString("notice_content", documentDetail.getXwnr());
                bundle.putString("fbsj", documentDetail.getFbsj());
                bundle.putString("djsl", documentDetail.getDjsl());
                // Log.e(TAG,sNoticeDetail.getXwbt());
                handler.sendMessage(msg);
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (push != null) {
                Intent intent = new Intent(DocumentDetailActivity.this,
                        LoginActivity.class);
                startActivity(intent);
            }
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }

}

