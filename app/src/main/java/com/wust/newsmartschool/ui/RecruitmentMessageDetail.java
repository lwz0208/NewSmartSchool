package com.wust.newsmartschool.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.JobDeatils;
import com.wust.newsmartschool.utils.HttpServer;


public class RecruitmentMessageDetail extends Activity
{

    private TextView title, head;
    private ImageView backImageView;
    private ProgressBar progressBar;
    private WebView webView;
    private WebSettings webSettings;
    private String nextId;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {

        public void handleMessage(Message msg)
        {

            if (msg.what == 100)
            {

                webView.setVisibility(View.VISIBLE);
                JobDeatils jobDetails = (JobDeatils) msg.obj;

                //title全部为null故此不需要设置title
                //title.setText(jobDetails.getJobDeatilTitle());
                progressBar.setVisibility(View.GONE);
                webView.loadDataWithBaseURL(null, jobDetails.getJobDeatilContent(), "text/html", "utf-8", null);

//				final String mimeType = "text/html";
//				final String encoding = "utf-8";
//				String jobDetailContent = jobDetails.getJobDeatilContent();
//
//				if (jobDetailContent != null && jobDetailContent.length() != 0
//						&& !jobDetailContent.equals("null"))
//				{
//
//					// 网址需要改
//					webView.loadDataWithBaseURL(null, jobDetailContent,
//							mimeType, encoding, "");
//				}
            }
            else if (msg.what == 101)
            {
                Toast.makeText(getApplicationContext(), "网速不给力",
                        Toast.LENGTH_SHORT).show();
            }
        }

    };

    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recruitment_message_detail);

        head = (TextView) findViewById(R.id.RE_title);
        head.setText("招聘信息详情");
        webView = (WebView) findViewById(R.id.message_content);
        webView.setHapticFeedbackEnabled(false);
        webView.setWebViewClient(new WebViewClientDemo());
        webView.setDownloadListener(new MyWebViewDownLoadListener());
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(true);
        title = (TextView) findViewById(R.id.message_title);
        title.setText(getIntent().getStringExtra("title"));
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View arg0)
            {

                RecruitmentMessageDetail.this.finish();

            }

        });

        nextId = getIntent().getStringExtra("nextId");

        Log.d("nextId", nextId);

        getRecruitmentMessageDetail(nextId);
    }

    private void getRecruitmentMessageDetail(final String nextId)
    {
        Runnable runnable = new Runnable()
        {

            public void run()
            {
                String urlString = "http://202.114.242.198:8090/EmploymentInformationDetail.php?url="
                        + nextId;
                try
                {
                    HttpServer httpServer = new HttpServer();
                    String recruitmentMessageString = httpServer
                            .getData(urlString);
                    Log.i("TAG", recruitmentMessageString);
                    if (recruitmentMessageString != null)
                    {
//						JSONObject jsonObject=new JSONObject(recruitmentMessageString);
//						String jobDetailString=jsonObject.optString("content");
                        JobDeatils details = new JobDeatils();
                        details = httpServer
                                .getRecruitmentMessageDeatil(recruitmentMessageString);
//						Log.i("TAG", "content:" + details.getJobDeatilContent());
//						String site = details.getJobDeatilContent().toString()
//								.trim();
//						if (site != null && !site.equals("")
//								&& !site.equals("null"))
//						{
//							Log.i("TAG", "siteAAAAAAAAAAAAAAAAAA");
//							site = httpServer.getData(site);
//							Log.i("TAG", "sitelll:" + site);
//							site = site
//									.substring(
//											site.indexOf("<div id=\"messagecontent\">"),
//											site.indexOf("<span id=\"keyw\">"))
//									.replace("<p>&nbsp;</p>", "")
//									.replaceAll("width=\"[1-9]\\d*\"",
//											"width=\"100%\"")// 用正则式匹配修改图片宽度
//									.replaceAll("height=\"[1-9]\\d*\"",
//											"height=\"200\"");// 用正则式匹配修改图片高度
//							details.setJobDeatilContent(site.trim());
//							Log.i("TAG",
//									"content1:" + details.getJobDeatilContent());
//						}
                        handler.sendMessage(handler.obtainMessage(100, details));
                    }
                    else
                    {
                        handler.sendMessage(handler.obtainMessage(101));
                    }
                }
                catch (Exception e)
                {

                }
            }
        };
        new Thread(runnable).start();
    }

    private class WebViewClientDemo extends WebViewClient
    {

        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            if (url.contains("UserFiles/File"))
            {
                url = url.substring(7, url.length());
                url = "http://www.wust.edu.cn/" + url;
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;
            }

            return false;

        }
    }

    private class MyWebViewDownLoadListener implements DownloadListener
    {

        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength)
        {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }

}

