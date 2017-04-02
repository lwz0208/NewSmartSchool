package com.wust.newsmartschool.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.NoticeAttach_Adapter;
import com.wust.newsmartschool.domain.NoticeDetailEntity;
import com.wust.newsmartschool.views.ListViewForScrollView;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.CommonUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NoticeAttachActivity extends BaseActivity {
    String TAG = "NoticeAttachActivity_Debugs";
    ListViewForScrollView lv_notice_attach;
    NoticeAttach_Adapter noticeAttach_adapter;
    List<NoticeDetailEntity.FileListBean> getAttachList;
    //几个下载附件相关的东东
    ProgressDialog mProgressBar;
    OkHttpClient mOkHttpClient;
    Handler mHandler;
    //点击ListView里的一个Item
    NoticeDetailEntity.FileListBean BeClickItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_attach);
        getAttachList = (List<NoticeDetailEntity.FileListBean>) getIntent().getSerializableExtra("noticeattachlist");
        if (getAttachList.size() != 0) {
//            Log.e(TAG, getAttachList.get(0).getFileName().toString());
            noticeAttach_adapter = new NoticeAttach_Adapter(NoticeAttachActivity.this, getAttachList);
        } else {
            getAttachList = new ArrayList<>();
            noticeAttach_adapter = new NoticeAttach_Adapter(NoticeAttachActivity.this, getAttachList);
        }
        lv_notice_attach = (ListViewForScrollView) findViewById(R.id.lv_notice_attach);
        lv_notice_attach.setAdapter(noticeAttach_adapter);
        //下载附件提示框的一些设置
        mProgressBar = new ProgressDialog(NoticeAttachActivity.this);
        mProgressBar.setMessage("正在下载附件文件...");
        mProgressBar.setCancelable(true);
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressBar.setProgress(0);
        mProgressBar.setMax(100);
        mOkHttpClient = new OkHttpClient();
        lv_notice_attach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //赋值给点击的对象
                BeClickItem = (NoticeDetailEntity.FileListBean) noticeAttach_adapter.getItem(position);
                if (BeClickItem != null) {
                    String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    File file = new File(SDPath, BeClickItem.getFileName().toString());
                    //如果文件存在辣么就直接打开，如果不存在那就下载咯！~
                    if (file.exists()) {
                        CommonUtils.openFile(NoticeAttachActivity.this, file, BeClickItem.getFileType().toString());
                    } else {
                        Log.e(TAG, Constant.BASE_URL + BeClickItem.getFileRealPath().toString());
                        mProgressBar.show();
                        Request request = new Request.Builder().url(Constant.BASE_URL + BeClickItem.getFileRealPath().toString()).build();
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
                                    File file = new File(SDPath, BeClickItem.getFileName().toString());
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
                                    CommonUtils.openFile(NoticeAttachActivity.this, file, BeClickItem.getFileType().toString());
//                            CommonUtils.openFile(NoticeDetailActivity.this, file, "");
                                    mProgressBar.dismiss();
                                    Log.d("h_bl", "文件下载成功");
                                } catch (Exception e) {
//                            showToastShort("文件下载失败");
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
                } else {
                    showToastShort("下载失败");
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
                        if (progress == 100)
                            showToastShort("文件下载成功");
                        break;

                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    public void back(View view) {
        finish();
    }

}