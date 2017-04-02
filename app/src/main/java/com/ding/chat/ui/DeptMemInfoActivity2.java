package com.ding.chat.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ding.chat.DemoHelper;
import com.ding.chat.R;
import com.ding.chat.db.UserDao;
import com.ding.chat.domain.UserInfoEntity;
import com.ding.chat.views.CircleImageView;
import com.ding.chat.views.ECProgressDialog;
import com.ding.chat.widget.RecyclingImageView;
import com.ding.easeui.Constant;
import com.ding.easeui.domain.EaseUser;
import com.ding.easeui.utils.CommonUtils;
import com.ding.easeui.utils.PreferenceManager;
import com.ding.easeui.widget.GlideCircleImage;
import com.ding.easeui.widget.GlideRoundTransform;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import uk.co.senab.photoview.PhotoViewAttacher;

public class DeptMemInfoActivity2 extends BaseActivity implements
        OnClickListener {
    String TAG = "DeptMemInfoActivity_Log";
    String Phone = "";
    String userId;
    UserInfoEntity userInfoEntity;
    private Button btn_addfriends;
    private TextView deptmem_realname;
    private TextView deptmem_phone;
    private TextView deptmem_dept;
    private TextView deptmem_gender;
    private TextView deptmem_jobtitle;
    private TextView roleName;
    private TextView username_type;
    private TextView deptinfo_company;
    private ImageView deptmem_callphone;
    private ImageView sendshortmessage;
    private ImageView head_imag_mem;
    private ECProgressDialog pd;
    private LinearLayout ll_sentmessage;
    // 接受本地存储的好友列表类
    private Map<String, EaseUser> getContactList;
    private ScrollView up_scrollview;
    //是否是好友关系
    boolean isFriend = false;
    private RelativeLayout rl_bottom;
    private final int RESULT_BLACK = 1;
    private final int RESULT_DELETE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dept_mem_info2);
        pd = new ECProgressDialog(DeptMemInfoActivity2.this);
        getContactList = DemoHelper.getInstance().getContactList();
        rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        deptmem_realname = (TextView) findViewById(R.id.deptmem_realname);
        deptmem_phone = (TextView) findViewById(R.id.deptmem_phone);
        up_scrollview = (ScrollView) findViewById(R.id.up_scrollview);
        deptmem_jobtitle = (TextView) findViewById(R.id.deptmem_jobtitle);
        username_type = (TextView) findViewById(R.id.username_type);
        deptmem_dept = (TextView) findViewById(R.id.deptmem_dept);
        deptmem_callphone = (ImageView) findViewById(R.id.deptmem_callphone);
        sendshortmessage = (ImageView) findViewById(R.id.sendshortmessage);
        head_imag_mem = (ImageView) findViewById(R.id.head_imag_mem);
        btn_addfriends = (Button) findViewById(R.id.btn_addfriends);
        deptmem_gender = (TextView) findViewById(R.id.deptmem_gender);
        deptinfo_company = (TextView) findViewById(R.id.deptinfo_company);
        roleName = (TextView) findViewById(R.id.roleName);
        ll_sentmessage = (LinearLayout) findViewById(R.id.ll_sentmessage);
        deptmem_callphone.setOnClickListener(this);
        sendshortmessage.setOnClickListener(this);
        btn_addfriends.setOnClickListener(this);
        userInfoEntity = new UserInfoEntity();

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        Log.e(TAG, userId);
        if (userId != null) {
            //显示头像
            Glide.with(DeptMemInfoActivity2.this)
                    .load(Constant.GETHEADIMAG_URL
                            + userId
                            + ".png").transform(new GlideCircleImage(DeptMemInfoActivity2.this)).placeholder(R.drawable.ease_default_avatar)
                    .into(head_imag_mem);
            for (Map.Entry<String, EaseUser> entry : getContactList.entrySet()) {
                if (entry.getKey().toString().equals(userId)
                        || userId.equals(PreferenceManager.getInstance().getCurrentUserId())) {
                    isFriend = true;
                    break;
                }
            }
            //判断是不是好友关系，如果是的那就隐藏加好友按钮。不是好友就连电话都不让打。
            if (isFriend) {
                btn_addfriends.setVisibility(View.GONE);
            } else {
                deptmem_callphone.setVisibility(View.GONE);
                sendshortmessage.setVisibility(View.GONE);
                rl_bottom.setVisibility(View.INVISIBLE);
            }
            //判断是不是自己，如果是自己去掉打电话和发送消息（ps不能给自己打电话吧，更不能给自己发消息吧）
            if (userId.equals(PreferenceManager.getInstance().getCurrentUserId())) {
                rl_bottom.setVisibility(View.INVISIBLE);
                deptmem_callphone.setVisibility(View.GONE);
                sendshortmessage.setVisibility(View.GONE);
                btn_addfriends.setVisibility(View.GONE);
                ll_sentmessage.setVisibility(View.GONE);
//                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                layoutParams.setMargins(0, 45, 0, 0);//4个参数按顺序分别是左上右下
//                up_scrollview.setLayoutParams(layoutParams);
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

        head_imag_mem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("headUrl", Constant.GETHEADIMAG_URL + userId + ".png");
                intent.setClass(DeptMemInfoActivity2.this, HeadImgActivity.class);
                startActivity(intent);
            }
        });

        rl_bottom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(DeptMemInfoActivity2.this, DeleteAndBlackActivity.class), 0);
            }
        });

    }

    /* 获取个人信息 */
    public void getClickUserInfo() throws FileNotFoundException, JSONException {
        JSONObject phone = new JSONObject();
        phone.put("userId", userId);
        CommonUtils.setCommonJson(DeptMemInfoActivity2.this, phone, PreferenceManager.getInstance().getCurrentUserFlowSId());
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
                                // 本应该显示的是昵称，但数据库里昵称基本为空，故显示为真实姓名
                                if (userInfoEntity.getData() != null) {
                                    //接口没有返回公司信息，所以只有先写死了。
                                    deptinfo_company.setText("武汉市第五医院");
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
                                        username_type.setText(s);
                                    } else {
                                        deptmem_jobtitle.setText("");
                                        username_type.setText("");
                                    }
                                    deptmem_dept.setText(userInfoEntity.getData()
                                            .getDepartmentName());

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


                            } else if (jObject.getInt("code") == 0) {
                                Toast.makeText(getApplicationContext(),
                                        jObject.getString("msg"),
                                        Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                            deptmem_callphone.setVisibility(View.GONE);
                            sendshortmessage.setVisibility(View.GONE);
                            ll_sentmessage.setVisibility(View.GONE);
                            btn_addfriends.setVisibility(View.GONE);
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
                        sendshortmessage.setVisibility(View.GONE);
                        btn_addfriends.setVisibility(View.GONE);
                        ll_sentmessage.setVisibility(View.GONE);
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
                    Toast.makeText(DeptMemInfoActivity2.this, "对方设置了只接受好友消息",
                            Toast.LENGTH_SHORT).show();
                }
            }
        } else if (userId.equals(PreferenceManager.getInstance().getCurrentUserId())) {
            Toast.makeText(DeptMemInfoActivity2.this, "您不能与自己聊天",
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
            case R.id.sendshortmessage:
                if (Phone.equals("")) {
                    showToastShort("不是有效的号码");
                } else {
                    Uri uri = Uri.parse("smsto:" + Phone);
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                    sendIntent.putExtra("sms_body", "");
                    startActivity(sendIntent);
                }
                break;
            case R.id.btn_addfriends:
                new android.app.AlertDialog.Builder(DeptMemInfoActivity2.this)
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
                                        Toast.makeText(DeptMemInfoActivity2.this,
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_BLACK:
                AlertDialog.Builder builderBlack = new AlertDialog.Builder(DeptMemInfoActivity2.this);
                builderBlack.setTitle("加入黑名单");
                builderBlack.setMessage("加入黑名单后，你将不再收到对方发来的消息");
                builderBlack.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveToBlacklist();
                    }
                }).setNegativeButton("取消", null);
                builderBlack.show();
                break;
            case RESULT_DELETE:
                AlertDialog.Builder builderDelete = new AlertDialog.Builder(DeptMemInfoActivity2.this);
                builderDelete.setTitle("删除联系人");
                builderDelete.setMessage("删除该联系人，同时删除与该联系人相关的聊天记录");
                builderDelete.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteContact();
                    }
                }).setNegativeButton("取消", null);
                builderDelete.show();
                break;
            default:
                break;
        }
    }

    private void deleteContact() {
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().contactManager()
                            .deleteContact(userId);
                    // 删除db和内存中此用户的数据
                    UserDao dao = new UserDao(DeptMemInfoActivity2.this);
                    dao.deleteContact(userId);
                    DemoHelper.getInstance().getContactList()
                            .remove(userId);
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "删除失败" + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void moveToBlacklist() {
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    // 加入到黑名单
                    EMClient.getInstance().contactManager()
                            .addUserToBlackList(userId, false);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "加入黑名单成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "加入黑名单失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}
