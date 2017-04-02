package com.ding.chat.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ding.chat.DemoHelper;
import com.ding.chat.R;
import com.ding.chat.domain.UserInfoEntity;
import com.ding.chat.views.CircleImageView;
import com.ding.chat.views.ECProgressDialog;
import com.ding.easeui.Constant;
import com.ding.easeui.domain.EaseUser;
import com.ding.easeui.utils.CommonUtils;
import com.ding.easeui.utils.PreferenceManager;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

public class DeptMemInfoActivity extends BaseActivity implements
        OnClickListener {
    String TAG = "DeptMemInfoActivity_Log";
    String Phone = "";
    String userId;
    UserInfoEntity userInfoEntity;
    private LinearLayout deptmem_layout;
    private LinearLayout bottom_three_btn;
    private TextView username_mem;
    private TextView deptmem_realname;
    private TextView deptmem_phone;
    private TextView deptmem_dept;
    private TextView deptmem_roleId;
    private TextView deptmem_gender;
    private TextView deptmem_jobtitle;
    private TextView roleName;
    private ImageView deptmem_callphone;
    private CircleImageView head_imag_mem;
    private ECProgressDialog pd;
    private ImageView addfriends_mem;
    private Button btn_sentmessage;
    // 接受本地存储的好友列表类
    private Map<String, EaseUser> getContactList;
    //是否是好友关系
    boolean isFriend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dept_mem_info);
        // 沉浸式模式
        // // 透明状态栏
        // DeptMemInfoActivity.this.getWindow().addFlags(
        // WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // // 透明导航栏
        // DeptMemInfoActivity.this.getWindow().addFlags(
        // WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        // 初始化缓存类
        pd = new ECProgressDialog(DeptMemInfoActivity.this);

        getContactList = DemoHelper.getInstance().getContactList();
        username_mem = (TextView) findViewById(R.id.username_mem);
        deptmem_realname = (TextView) findViewById(R.id.deptmem_realname);
        deptmem_phone = (TextView) findViewById(R.id.deptmem_phone);
        deptmem_jobtitle = (TextView) findViewById(R.id.deptmem_jobtitle);
        deptmem_dept = (TextView) findViewById(R.id.deptmem_dept);
        addfriends_mem = (ImageView) findViewById(R.id.addfriends_mem);
        deptmem_roleId = (TextView) findViewById(R.id.deptmem_roleId);
        deptmem_callphone = (ImageView) findViewById(R.id.deptmem_callphone);
        head_imag_mem = (CircleImageView) findViewById(R.id.head_imag_mem);
        deptmem_layout = (LinearLayout) findViewById(R.id.deptmem_layout);
        bottom_three_btn = (LinearLayout) findViewById(R.id.bottom_three_btn);
        deptmem_gender = (TextView) findViewById(R.id.deptmem_gender);
        roleName = (TextView) findViewById(R.id.roleName);
        btn_sentmessage = (Button) findViewById(R.id.btn_sentmessage);
        deptmem_callphone.setOnClickListener(this);
        addfriends_mem.setOnClickListener(this);
        userInfoEntity = new UserInfoEntity();
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        Log.e(TAG, userId);
        if (userId != null) {
            for (Map.Entry<String, EaseUser> entry : getContactList.entrySet()) {
                if (entry.getKey().toString().equals(userId)
                        || userId.equals(PreferenceManager.getInstance().getCurrentUserId())) {
//                    Log.i("Key = " + entry.getKey().toString(), "Value = "
//                            + entry.getValue().toString());
                    isFriend = true;
                    break;
                }
            }
            //判断是不是好友关系，如果是的那就隐藏加好友按钮。不是好友就连电话都不让打。
            if (isFriend) {
                addfriends_mem.setVisibility(View.GONE);
            } else {
                deptmem_callphone.setVisibility(View.GONE);
            }
            //判断是不是自己，如果是自己去掉打电话和发送消息（ps不能给自己打电话吧，更不能给自己发消息吧）
            if (userId.equals(PreferenceManager.getInstance().getCurrentUserId())) {
                deptmem_callphone.setVisibility(View.GONE);
                bottom_three_btn.setVisibility(View.GONE);
                addfriends_mem.setVisibility(View.GONE);
                btn_sentmessage.setVisibility(View.GONE);
            }
            pd.show();
            try {
                getClickUserInfo();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            showToastShort("获取好友信息失败");
        }
    }

    /* 获取个人信息 */
    public void getClickUserInfo() throws FileNotFoundException, JSONException {
        JSONObject phone = new JSONObject();
        phone.put("userId", userId);
        CommonUtils.setCommonJson(DeptMemInfoActivity.this, phone, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i("getClickUserInfo", phone.toString());
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
                            if (jObject.getInt("code") == 1) {
                                userInfoEntity = new Gson().fromJson(arg0,
                                        UserInfoEntity.class);
                                // username_mem.setText(userInfoEntity.getData()
                                // .getUserNickname());
                                // 本应该显示的是昵称，但数据库里昵称基本为空，故显示为真实姓名
                                if (userInfoEntity.getData() != null) {
                                    username_mem.setText(userInfoEntity.getData()
                                            .getUserRealname());
                                    deptmem_realname.setText(userInfoEntity
                                            .getData().getUserRealname());
                                    roleName.setText(userInfoEntity
                                            .getData().getRoleName());
                                    if (!userInfoEntity.getData()
                                            .getTelephone().equals("")) {
                                        //全局phone变量，用来给下面点击拨号作为参数使用。
                                        Phone = userInfoEntity.getData().getTelephone();
                                        if (isFriend) {
                                            deptmem_phone.setText(userInfoEntity.getData()
                                                    .getTelephone());
                                        } else {
                                            deptmem_phone.setText(userInfoEntity.getData()
                                                    .getTelephone().toString().substring(0, 3) + "********");
                                        }
                                    }
                                    if (userInfoEntity.getData().getJobTitleIds().size() != 0) {
                                        String s = "";
                                        for (int i = 0; i < userInfoEntity.getData().getJobTitleIds().size(); i++) {
                                            s = userInfoEntity.getData()
                                                    .getJobTitleIds().get(i).getName() + "、" + s;
                                        }
                                        s = s.substring(0, s.length() - 1);
                                        deptmem_jobtitle.setText(s);
                                    } else {
                                        deptmem_jobtitle.setText("");
                                    }
                                    deptmem_dept.setText(userInfoEntity.getData()
                                            .getDepartmentName());
                                    deptmem_roleId.setText(userInfoEntity.getData()
                                            .getUserId() + "");

                                    if (userInfoEntity.getData().getUserGender() == 0) {
                                        deptmem_gender.setText("男");

                                    } else if (userInfoEntity.getData()
                                            .getUserGender() == 1) {
                                        deptmem_gender.setText("女");
                                    } else if (userInfoEntity.getData()
                                            .getUserGender() == 2) {
                                        deptmem_gender.setText("保密");

                                    }

                                }
                                // 显示用户头像后面虚化的背景
                                Glide.with(DeptMemInfoActivity.this)
                                        .load(Constant.GETHEADIMAG_URL
                                                + userInfoEntity.getData()
                                                .getUserId()
                                                + ".png").asBitmap()
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onLoadStarted(
                                                    Drawable placeholder) {
                                                Log.e("Glide", "onLoadStarted");
                                            }

                                            @Override
                                            public void onLoadFailed(
                                                    Exception e,
                                                    Drawable errorDrawable) {
                                                head_imag_mem.setImageResource(R.drawable.ease_default_avatar);
                                                Log.e("Glide", "onLoadFailed");

                                            }

                                            @Override
                                            public void onResourceReady(
                                                    Bitmap resource,
                                                    GlideAnimation<? super Bitmap> glideAnimation) {
                                                Log.e("Glide",
                                                        "onResourceReady");
                                                head_imag_mem
                                                        .setImageBitmap(resource);
                                                Bitmap returnBitmap = CommonUtils
                                                        .fastblur(
                                                                DeptMemInfoActivity.this,
                                                                resource, 10);

                                                @SuppressWarnings("deprecation")
                                                Drawable drawable = new BitmapDrawable(
                                                        returnBitmap);
                                                deptmem_layout
                                                        .setBackground(drawable);

                                            }

                                            @Override
                                            public void onLoadCleared(
                                                    Drawable placeholder) {
                                                Log.e("Glide", "onLoadCleared");

                                            }

                                            @Override
                                            public void setRequest(
                                                    Request request) {
                                                Log.e("Glide", "setRequest");
                                            }

                                            @Override
                                            public Request getRequest() {
                                                Log.e("Glide", "getRequest");
                                                return null;
                                            }

                                            @Override
                                            public void onStart() {
                                                Log.e("Glide", "onStart");

                                            }

                                            @Override
                                            public void onStop() {
                                                Log.e("Glide", "onStop");
                                            }

                                            @Override
                                            public void onDestroy() {
                                                Log.e("Glide", "onDestroy");

                                            }

                                        });

                            } else if (jObject.getInt("code") == 0) {
                                Toast.makeText(getApplicationContext(),
                                        jObject.getString("msg"),
                                        Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                            deptmem_callphone.setVisibility(View.GONE);
                            btn_sentmessage.setVisibility(View.GONE);
                            addfriends_mem.setVisibility(View.GONE);
                            showToastShort("好友信息获取失败");
                            Log.e(TAG, e.toString());
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            public void run() {
                                pd.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        Log.e(TAG, arg0.toString() + "///" + arg1.toString());
                        deptmem_callphone.setVisibility(View.GONE);
                        addfriends_mem.setVisibility(View.GONE);
                        btn_sentmessage.setVisibility(View.GONE);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                pd.dismiss();
                            }
                        });
                        Toast.makeText(getApplicationContext(), "好友信息获取失败",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onClick_SentInfo(View view) {
        if (!userId.equals(PreferenceManager.getInstance().getCurrentUserId())) {
            //此处逻辑是：是好友的话就直接聊天，不是的话就先判断对方是不是开启了那个不接受非好友消息的设置，如果开启了那就不能发送，如果没有开启那就可以把消息给发送过去
            if (isFriend) {
                startActivity(new Intent(getApplicationContext(),
                        ChatActivity.class).putExtra("userId", userId));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    overridePendingTransition(R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);
                }
            } else {
                if (userInfoEntity.getData().getReceiveStatus() == 0) {
                    startActivity(new Intent(getApplicationContext(),
                            ChatActivity.class).putExtra("userId", userId));
                    if (android.os.Build.VERSION.SDK_INT > 5) {
                        overridePendingTransition(R.anim.slide_in_from_right,
                                R.anim.slide_out_to_left);
                    }
                } else {
                    Toast.makeText(DeptMemInfoActivity.this, "对方设置了只接受好友消息",
                            Toast.LENGTH_SHORT).show();
                }
            }
        } else if (userId.equals(PreferenceManager.getInstance().getCurrentUserId())) {
            Toast.makeText(DeptMemInfoActivity.this, "您不能与自己聊天",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick_CallPhone(View view) {

    }

    public void onClick_Ding(View view) {

    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deptmem_callphone:
                if (Phone.equals("")) {
                    showToastShort("不是有效的号码");
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + Phone));
                    startActivity(intent);
                }
                break;
            case R.id.addfriends_mem:
                new android.app.AlertDialog.Builder(DeptMemInfoActivity.this)
                        .setMessage("你确定要加对方为好友吗？")
                        .setCancelable(true)
                        .setPositiveButton("是",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        new Thread(new Runnable() {
                                            public void run() {
                                                String s = PreferenceManager.getInstance().getCurrentRealName();
                                                try {
                                                    EMClient.getInstance()
                                                            .contactManager()
                                                            .addContact(userId,
                                                                    s + "请求加你为好友");
                                                } catch (HyphenateException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();
                                        Toast.makeText(DeptMemInfoActivity.this,
                                                "已发送请求，请等待对方同意", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        ).
                        setNegativeButton("否",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                }

                        ).

                        show();
                break;
            default:
                break;
        }

    }


}
