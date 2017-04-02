package com.wust.newsmartschool.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.wust.newsmartschool.DemoApplication;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.UserInfoEntity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import com.wust.newsmartschool.utils.CropOption;
import com.wust.newsmartschool.utils.CropOptionAdapter;
import com.wust.newsmartschool.utils.appUseUtils;
import com.wust.easeui.utils.PreferenceManager;
import com.wust.easeui.widget.GlideRoundTransform;
import com.wust.easeui.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Toast;


import okhttp3.Call;
import okhttp3.Request;

public class UserInfoActivity extends BaseActivity implements OnClickListener {
    UserInfoEntity userInfoEntity;
    TextView realname;
    TextView usergender;
    TextView usertelephone;
    TextView companyname;
    TextView deptname;
    TextView createtime;
    TextView workid;
    ImageView user_info_headimage;
    RelativeLayout rl_changepsw;
    RelativeLayout rl_changeavatar;

    //更换头像
    // 拍照
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    // 相册
    private static final int PICK_FROM_FILE = 3;

    private Uri imgUri;
    //全局文件名字
    private Bitmap mBitMap;

    //上传附件的bar
    ProgressDialog mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_info);
        userInfoEntity = (UserInfoEntity) DemoApplication.getInstance().mCache.getAsObject(Constant.MY_KEY_USERINFO);
        initView();
    }

    private void initView() {
        realname = (TextView) findViewById(R.id.realname);
        usergender = (TextView) findViewById(R.id.usergender);
        usertelephone = (TextView) findViewById(R.id.usertelephone);
        companyname = (TextView) findViewById(R.id.companyname);
        createtime = (TextView) findViewById(R.id.createtime);
        deptname = (TextView) findViewById(R.id.deptname);
        user_info_headimage = (ImageView) findViewById(R.id.user_info_headimage);
        workid = (TextView) findViewById(R.id.workid);
        rl_changepsw = (RelativeLayout) findViewById(R.id.rl_changepsw);
        rl_changeavatar = (RelativeLayout) findViewById(R.id.rl_changeavatar);
        rl_changeavatar.setOnClickListener(this);
        rl_changepsw.setOnClickListener(this);
        //初始化上传附件的那个bar
        mProgressBar = new ProgressDialog(UserInfoActivity.this);
        mProgressBar.setMessage("正在上传附件文件...");
        mProgressBar.setCancelable(true);
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressBar.setProgress(0);
        mProgressBar.setMax(100);
        Glide.with(this)
                .load(Constant.GETHEADIMAG_URL
                        + PreferenceManager.getInstance().getCurrentUserId() + ".png").transform(new GlideRoundTransform(this))
                .placeholder(R.drawable.ease_default_avatar)
                .into(user_info_headimage);
        realname.setText(PreferenceManager.getInstance().getCurrentRealName());
        if (userInfoEntity != null) {
            if (userInfoEntity.getData().getUserGender() == 0) {
                usergender.setText("男");
            } else if (userInfoEntity.getData().getUserGender() == 1) {
                usergender.setText("女");
            } else if (userInfoEntity.getData().getUserGender() == 2) {
                usergender.setText("保密");
            }
            workid.setText(userInfoEntity.getData().getPersonnelId());
            usertelephone.setText(userInfoEntity.getData().getTelephone());
//        companyname.setText(userInfoEntity.getData().getDepartmentName());
            createtime.setText(userInfoEntity.getData().getWorkNumber());
            deptname.setText(userInfoEntity.getData().getDepartmentName());
        } else {
            showToastShort("获取个人信息失败");
        }
    }

    public void back(View view) {
        finish();
        if (android.os.Build.VERSION.SDK_INT > 5) {
            overridePendingTransition(
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_changeavatar:
                /**上传头像功能，该功能在app部分已经完成，但是暂时没有接口，为不对用户产生误解，故先屏蔽掉*/
                uploadHeadPhoto();
                break;
            case R.id.rl_changepsw:
                startActivity(new Intent(UserInfoActivity.this, ChangePswActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    overridePendingTransition(R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);
                }
                break;
            default:
                break;
        }

    }

    private void uploadHeadPhoto() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
                UserInfoActivity.this);
        // 指定下拉列表的显示数据
        final String[] items = {"相册", "拍照"};
        builder.setTitle("请选择头像");
        // 设置一个下拉的列表选择项
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // 方式1，直接打开图库，只能选择图库的图片
                        Intent intent_fromxiangce = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        // 方式2，会先让用户选择接收到该请求的APP，可以从文件系统直接选取图片
//                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
//                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent_fromxiangce.setType("image/*");
                        startActivityForResult(intent_fromxiangce, PICK_FROM_FILE);
                        break;
                    case 1:
                        Intent intent_fromzhaoxiang = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        imgUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "avatar_"
                                + String.valueOf(System.currentTimeMillis())
                                + ".png"));
                        intent_fromzhaoxiang.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                        startActivityForResult(intent_fromzhaoxiang, PICK_FROM_CAMERA);
                        break;
                }
            }
        });
        builder.show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PICK_FROM_CAMERA:
                doCrop3();
                break;
            case PICK_FROM_FILE:
                imgUri = data.getData();
                Log.e("PICK_FROM_FILE", imgUri.toString());
                doCrop3();
                break;
            case CROP_FROM_CAMERA:
                if (null != data) {
                    setCropImg(data);
                }
                break;
        }
    }

    /**
     * set the bitmap
     *
     * @param picdata
     */
    private void setCropImg2(Intent picdata) {
        if (null != picdata.getData()) {
            Uri uri = picdata.getParcelableExtra("src_uri");
//            Log.e("setCropImg", picdata.getDataString());
//            Log.e("setCropImg", picdata.getStringExtra("data"));
            Log.e("setCropImg", appUseUtils.printBundle(picdata.getExtras()));
//            Bitmap mBitmap = null;
//            try {
//                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//            saveBitmap(Environment.getExternalStorageDirectory() + "/crop_"
//                    + System.currentTimeMillis() + ".png", mBitmap);
//            } catch (Exception e) {
//                Log.e("[Android]", "目录为：" + uri);
//                e.printStackTrace();
//            }
//

        }
    }

    /**
     * set the bitmap
     *
     * @param picdata
     */
    private void setCropImg(Intent picdata) {

        if (null != picdata) {
            Uri uri = null;
            uri = picdata.getData();

            if (uri == null) {
                Bitmap mHWBitmap = (Bitmap) picdata.getExtras().getParcelable("data");
                saveBitmap(Environment.getExternalStorageDirectory() + "/crop_"
                        + System.currentTimeMillis() + ".png", mHWBitmap);
            } else {
                try {
                    Bitmap mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    saveBitmap(Environment.getExternalStorageDirectory() + "/crop_"
                            + System.currentTimeMillis() + ".png", mBitmap);
                } catch (Exception e) {
                    Log.e("[Android]", "目录为：" + uri);
                    e.printStackTrace();

                }
            }
        }
    }

    private void doCrop3() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imgUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        startActivityForResult(intent, CROP_FROM_CAMERA);
    }

    /**
     * save the crop bitmap
     *
     * @param fileName
     * @param mBitmap
     */
    public void saveBitmap(String fileName, Bitmap mBitmap) {
        File f = new File(fileName);
        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fOut.close();
                mBitMap = mBitmap;
                uploadHeadPhotoToserver(f);
//                Toast.makeText(this, "save success", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(
                intent, 0);
        int size = list.size();

        if (size == 0) {
            Toast.makeText(this, "找不到裁剪的应用", Toast.LENGTH_SHORT)
                    .show();
            return;
        } else {
            intent.setData(imgUri);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            // only one
            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);
                i.setComponent(new ComponentName(res.activityInfo.packageName,
                        res.activityInfo.name));
                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
//                 many crop app
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();
                    co.title = getPackageManager().getApplicationLabel(
                            res.activityInfo.applicationInfo);
                    co.icon = getPackageManager().getApplicationIcon(
                            res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);
                    co.appIntent
                            .setComponent(new ComponentName(
                                    res.activityInfo.packageName,
                                    res.activityInfo.name));
                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(
                        UserInfoActivity.this, cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("请选择一个应用");
                builder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                startActivityForResult(
                                        cropOptions.get(item).appIntent,
                                        CROP_FROM_CAMERA);
                            }
                        });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (imgUri != null) {
                            getContentResolver().delete(imgUri, null, null);
                            imgUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }

    }

    private void uploadHeadPhotoToserver(File mFile) {
        //将来Bitmap处理为File
        Log.e("uploadHeadPhotoToserver", mFile + "/**/" + mFile.exists());
        if (mFile.exists()) {
            OkHttpUtils.post().url(Constant.UPLOADAVATAR_URL).addParams("userId", PreferenceManager.getInstance().getCurrentUserId().toString()).addFile("uploadImageFile", mFile.getName(), mFile).build()
                    .connTimeOut(20000)
                    .readTimeOut(20000)
                    .writeTimeOut(20000).execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    Log.i("UpdataAttach", call.toString() + "/*/" + e.toString());
                    showToastShort("上传失败");

                }

                @Override
                public void onResponse(String s) {
                    Log.i("UpdataAttach", s);
                    try {
                        org.json.JSONObject response = new org.json.JSONObject(s);
                        if (response.getInt("code") == 1) {
                            showToastShort("上传成功");
                            //上传成功就把图片显示出来
//                            user_info_headimage.setImageBitmap(mBitMap);
//                            Glide.with(UserInfoActivity.this)
//                                    .load(Constant.GETHEADIMAG_URL
//                                            + PreferenceManager.getInstance().getCurrentUserId() + ".png").transform(new GlideRoundTransform(UserInfoActivity.this))
//                                    .placeholder(R.drawable.ease_default_avatar)
//                                    .into(user_info_headimage);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Glide.get(UserInfoActivity.this).clearDiskCache();
                                }
                            }).start();
                            Glide.get(UserInfoActivity.this).clearMemory();

                        }
                    } catch (Exception e) {
                        showToastShort("请求失败");
                        e.printStackTrace();
                    }

                }

                @Override
                public void onBefore(Request request) {
                    super.onBefore(request);
                    mProgressBar.show();

                }

                @Override
                public void inProgress(float progress) {
                    super.inProgress(progress);
                    mProgressBar.setProgress((int) (progress * 100));
                }

                @Override
                public void onAfter() {
                    super.onAfter();
                    mProgressBar.dismiss();
                }
            });

        } else {
            showToastShort("文件不存在");
        }
    }


}
