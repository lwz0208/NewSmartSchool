package com.ding.chat.ui;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ding.chat.DemoApplication;
import com.ding.chat.R;
import com.ding.chat.domain.UserInfoEntity;
import com.ding.chat.domain.UserInfoEntity_Data;
import com.ding.chat.utils.appUseUtils;
import com.ding.easeui.Constant;
import com.ding.easeui.utils.CommonUtils;
import com.ding.easeui.utils.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.FileUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WorkFragApplyActivity extends BaseActivity {
    String TAG = "WorkFragApplyActivity_Debugs";
    private WebView webview_apply;
    private String userInfojsonStr;
    private ImageView refresh_web;
    String TargetURL;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    //几个下载附件相关的东东
    ProgressDialog mProgressBar;
    OkHttpClient mOkHttpClient;
    Handler mHandler;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_frag_apply_webview);
        mContext = WorkFragApplyActivity.this;
        webview_apply = (WebView) findViewById(R.id.webview_apply);
        refresh_web = (ImageView) findViewById(R.id.refresh_web);
        refresh_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkFragApplyActivity.this,
                        WorkFragApplyActivity.class));
                finish();
            }
        });
        SwitchWebSite();
        initLocalStorageData();
        //一些设置
        //下载附件提示框的一些设置
        mProgressBar = new ProgressDialog(WorkFragApplyActivity.this);
        mProgressBar.setMessage("正在下载附件文件...");
        mProgressBar.setCancelable(true);
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressBar.setProgress(0);
        mProgressBar.setMax(100);
        mOkHttpClient = new OkHttpClient();
        webview_apply.setWebViewClient(new WebViewClient() {
                                           public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                                               view.loadUrl(url);
                                               return true;
                                           }

                                           @Override
                                           public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                               super.onPageStarted(view, url, favicon);
//                                               if (url.equals(Constant.FLOWHTML5_URL)) {
                                               dealJSData();
//                                               }


                                           }

                                           @Override
                                           public void onLoadResource(WebView view, String url) {
                                               super.onLoadResource(view, url);
//                                               if (!url.equals(Constant.FLOWHTML5_URL)) {
//                                                   dealJSData();
//                                               }
                                           }

                                           @Override
                                           public void onPageFinished(WebView view, String url) {
                                               super.onPageFinished(view, url);
                                               Log.e(TAG, "needFresh=" + PreferenceManager.getInstance().getH5ISFIRSTUSE());
                                               if (PreferenceManager.getInstance().getH5ISFIRSTUSE()) {
                                                   PreferenceManager.getInstance().setH5ISFIRSTUSE(false);
                                                   view.reload();
                                               }
                                               //因为每次要刷新一下才显示出来渲染的结果，所以这儿手动刷新一下。
//                                               if (url.equals(Constant.FLOWHTML5_URL) || url.contains("detail")) {


//                                               }
                                           }


                                       }

        );
        webview_apply.setWebChromeClient(new

                                                 WebChromeClient() {
                                                     public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//                Log.d(TAG, "openFileChoose(ValueCallback<Uri> uploadMsg)");
                                                         mUploadMessage = uploadMsg;
                                                         Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                                                         i.addCategory(Intent.CATEGORY_OPENABLE);
                                                         i.setType("*/*");
                                                         WorkFragApplyActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), 123);
                                                     }

                                                     public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
//                Log.d(TAG, "openFileChoose( ValueCallback uploadMsg, String acceptType )");
                                                         mUploadMessage = uploadMsg;
                                                         Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                                                         i.addCategory(Intent.CATEGORY_OPENABLE);
                                                         i.setType("*/*");
                                                         WorkFragApplyActivity.this.startActivityForResult(
                                                                 Intent.createChooser(i, "File Browser"),
                                                                 123);
                                                     }

                                                     public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//                Log.d(TAG, "openFileChoose(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
                                                         mUploadMessage = uploadMsg;
                                                         Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                                                         i.addCategory(Intent.CATEGORY_OPENABLE);
                                                         i.setType("*/*");
                                                         WorkFragApplyActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"), 123);
                                                     }

                                                     // For Android 5.0+
                                                     public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                                                         mUploadCallbackAboveL = filePathCallback;
                                                         Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                                                         i.addCategory(Intent.CATEGORY_OPENABLE);
                                                         i.setType("*/*");
                                                         WorkFragApplyActivity.this.startActivityForResult(
                                                                 Intent.createChooser(i, "File Browser"),
                                                                 123);
                                                         return true;
                                                     }
                                                 });

        webview_apply.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, final String mimetype, long contentLength) {
                Log.e(TAG, "url=" + url);
                Log.e(TAG, "userAgent=" + userAgent);
                Log.e(TAG, "contentDisposition=" + contentDisposition);
                Log.e(TAG, "mimetype=" + mimetype);
//                Uri uri = Uri.parse(url);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//截取地址中最后面文件的名字
                final String FileName = url.substring(url.lastIndexOf("/") + 1);
//                Log.e(TAG, "FileName=" + FileName);
                //采用自己的方法下载文件

                String
                        SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                File file = new File(SDPath, FileName);
                //如果文件存在辣么就直接打开，如果不存在那就下载咯！~
                if (file.exists()) {
                    if (!mimetype.equals("")) {
                        CommonUtils.openFile(WorkFragApplyActivity.this, file, mimetype);
                    } else {
                        CommonUtils.openFileUnknowType(WorkFragApplyActivity.this, file);
                    }
                } else {
                    mProgressBar.show();
                    Request request = new Request.Builder().url(url).build();
                    mOkHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e(TAG, "onFailure");
                            showToastShort("下载失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.e(TAG, response.toString());
                            InputStream is = null;
                            byte[] buf = new byte[2048];
                            int len = 0;
                            FileOutputStream fos = null;
                            String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                            try {
                                is = response.body().byteStream();
                                long total = response.body().contentLength();
                                File file = new File(SDPath, FileName);
                                fos = new FileOutputStream(file);
                                long sum = 0;
                                while ((len = is.read(buf)) != -1) {
                                    fos.write(buf, 0, len);
                                    sum += len;
                                    int progress = (int) (sum * 1.0f / total * 100);
                                    Log.d("h_bl", "progress=" + progress);
                                    Message msg = mHandler.obtainMessage();
                                    msg.what = 1;
                                    msg.arg1 = progress;
                                    mHandler.sendMessage(msg);
                                }
                                fos.flush();
                                Log.d("h_bl", "文件下载成功");
                                mProgressBar.dismiss();
                                if (!mimetype.equals("")) {
                                    CommonUtils.openFile(WorkFragApplyActivity.this, file, mimetype);
                                } else {
                                    CommonUtils.openFileUnknowType(WorkFragApplyActivity.this, file);
                                }

                            } catch (Exception e) {
                                mProgressBar.dismiss();
                                Log.d("h_bl", "文件下载失败" + "/" + e.toString());
                            } finally {
                                try {
                                    if (is != null)
                                        is.close();
                                } catch (IOException e) {
                                }
                                try {
                                    if (fos != null)
                                        fos.close();
                                } catch (IOException e) {
                                }
                            }
                        }
                    });
                }


            }
        });
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        int progress = msg.arg1;
                        mProgressBar.setProgress(progress);
                        if (progress == 100) {
                            showToastShort("文件下载成功");
                        }
                        break;

                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
        webview_apply.loadUrl(TargetURL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != 123
                || mUploadCallbackAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }

                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
        return;
    }


    private void SwitchWebSite() {
        try {
            String Webid_create = getIntent().getStringExtra("webid_create");
            String Webid_detail = getIntent().getStringExtra("webid_detail");
            String Webid_from_firstPage = getIntent().getStringExtra("webid_first_page");
//            Log.i("SwitchWebSite", Webid_create);
//            Log.i("SwitchWebSite", Webid_detail);
            if (Webid_create != null) {
                TargetURL = Constant.FLOWHTML5_CREATEURL + "?id=" + Webid_create;
            } else if (Webid_detail != null) {
                TargetURL = Constant.FLOWHTML5_DETAILURL + "?id=" + Webid_detail;
            } else if (Webid_from_firstPage != null) {
                TargetURL = Webid_from_firstPage;
            } else {
                TargetURL = Constant.FLOWHTML5_URL;
            }
//            Log.i("SwitchWebSite", TargetURL);
        } catch (Exception e) {
            Log.e("SwitchWebSite", "null");
            e.printStackTrace();
        }
    }

    private void dealJSData() {
        //将缓存到的个人信息实体类缓存到
        if ((DemoApplication.getInstance().mCache.getAsObject(
                Constant.MY_KEY_USERINFO)) == null) {
            GetMyInfo();
            return;
        } else {
            Gson gson;
            GsonBuilder builder = new GsonBuilder();
            gson = builder.create();
            UserInfoEntity userInfoEntity = (UserInfoEntity) DemoApplication.getInstance().mCache.getAsObject(Constant.MY_KEY_USERINFO);
            UserInfoEntity_Data userInfoDate = userInfoEntity.getData();
            userInfojsonStr = gson.toJson(userInfoDate, UserInfoEntity_Data.class);
//            Log.e(TAG, "dealJSData_gson=" + userInfojsonStr);
            runJS(userInfojsonStr);
        }

    }

    private void GetMyInfo() {
        JSONObject phone = new JSONObject();
        try {
            phone.put("userId", EMClient.getInstance().getCurrentUser());
            CommonUtils.setCommonJson(WorkFragApplyActivity.this, phone, PreferenceManager.getInstance().getCurrentUserFlowSId());
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        OkHttpUtils.postString().url(Constant.USERINFO_URL)
                .content(phone.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String arg0) {
                        Log.e(TAG, arg0);
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1 && jObject.getJSONObject("data") != null) {
                                UserInfoEntity userInfoEntity = new Gson().fromJson(arg0,
                                        UserInfoEntity.class);
                                DemoApplication.getInstance().mCache.put(
                                        Constant.MY_KEY_USERINFO,
                                        userInfoEntity);
                                PreferenceManager.getInstance().setCurrentUserRealName(userInfoEntity.getData()
                                        .getUserRealname().toString());
                                PreferenceManager.getInstance().setfriendsMsgOnly(userInfoEntity.getData().getReceiveStatus());
                                EMClient.getInstance().updateCurrentUserNick(
                                        userInfoEntity.getData()
                                                .getUserRealname());
                                //以上是为了加强缓存机制，居然缓存木有了那其他的可能也木有了。再怼他一遍。
                                //下面就是运行那一段js了。
                                Gson gson;
                                GsonBuilder builder = new GsonBuilder();
                                gson = builder.create();
                                UserInfoEntity_Data userInfoDate = userInfoEntity.getData();
                                userInfojsonStr = gson.toJson(userInfoDate, UserInfoEntity_Data.class);
                                Log.e(TAG, "GetMyInfo=" + userInfojsonStr);
                                runJS(userInfojsonStr);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        Log.i("SettingsFragment", arg0 + "---" + arg1.toString());
                        DemoApplication.getInstance().mCache.remove(Constant.MY_KEY_USERINFO);
                    }
                });
    }

    private void runJS(String jsonStr) {
        //将传过来的String变为jsonObject然后加入那四个权限控制的信息
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            CommonUtils.setCommonJson(WorkFragApplyActivity.this, jsonObj, PreferenceManager.getInstance().getCurrentUserFlowSId());
            //自定义JS调用，存储localStorage
            String call = "javascript:setData(" + jsonObj.toString() + ")";
//        String call = "javascript:setData(" + PreferenceManager.getInstance().getCurrentUserId() + ")";
//        String call = "javascript:setData(2911,123456)";
//            Log.e(TAG, "call=" + call);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webview_apply.evaluateJavascript(call, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
//                        Log.e(TAG, value);
                    }
                });
            } else {
                webview_apply.loadUrl(call);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runClearLocalStorage() {
        try {
            //自定义JS调用，刷新那个未处理消息的数字。
            String call = "javascript:clearlocalStorage()";
            Log.e(TAG, "call=" + call);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webview_apply.evaluateJavascript(call, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        //                        Log.e(TAG, value);
                    }
                });
            } else {
                webview_apply.loadUrl(call);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initLocalStorageData() {
        webview_apply.setWebChromeClient(new MywebChromeClient()); //在加载网页前加上这句就可以了
        WebSettings wSet = webview_apply.getSettings();
        wSet.setJavaScriptEnabled(true);
        /***打开本地缓存提供JS调用**/
        wSet.setDomStorageEnabled(true);
        // Set cache size to 8 mb by default. should be more than enough
        wSet.setAppCacheMaxSize(1024 * 1024 * 8);
//        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
//        wSet.setAppCachePath(appCachePath);
        //不使用缓存
        wSet.setCacheMode(WebSettings.LOAD_NO_CACHE);
        wSet.setAllowFileAccess(true);
        wSet.setAppCacheEnabled(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview_apply.canGoBack()) {
            webview_apply.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void back(View view) {

        if (webview_apply.canGoBack()) {
            webview_apply.goBack();// 返回前一个页面
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        webview_apply.clearCache(true);
//        webview_apply.clearFormData();
//        webview_apply.clearHistory();
        Log.e(TAG, "onDestroy");
//        runClearLocalStorage();
        //清空所有Cookie
        webview_apply.setWebChromeClient(null);
        webview_apply.setWebViewClient(null);
        webview_apply.getSettings().setJavaScriptEnabled(false);
        webview_apply.clearCache(true);
        webview_apply.clearHistory();
        webview_apply.clearFormData();
        mContext.deleteDatabase("WebView.db");
        mContext.deleteDatabase("WebViewCache.db");
        getCacheDir().delete();
        //清空所有Cookie
        CookieSyncManager.createInstance(mContext);  //Create a singleton CookieSyncManager within a context
        CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
        cookieManager.removeAllCookie();// Removes all cookies.
        CookieSyncManager.getInstance().sync(); // forces sync manager to sync now

    }

    class MywebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 final JsResult result) {
// 弹窗处理
            AlertDialog.Builder b2 = new AlertDialog.Builder(WorkFragApplyActivity.this)
                    .setTitle(R.string.app_name).setMessage(message)
                    .setPositiveButton("ok", new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    });


            b2.setCancelable(false);
            b2.create();
            b2.show();


            return true;
        }
    }


}
