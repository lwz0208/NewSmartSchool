/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ding.chat.ui;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.MediaType;

import com.ding.chat.DemoApplication;
import com.ding.chat.DemoHelper;
import com.ding.chat.R;
import com.ding.chat.db.DemoDBManager;
import com.ding.chat.domain.UnreadMsgNum;
import com.ding.chat.domain.UserInfoEntity;
import com.ding.chat.utils.appUseUtils;
import com.ding.easeui.utils.CommonUtils;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.ding.easeui.Constant;
import com.ding.easeui.utils.EaseCommonUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ding.easeui.utils.PreferenceManager;
import com.ding.chat.views.CircleImageView;
import com.ding.chat.views.ECProgressDialog;

import java.util.Set;

/**
 * 登陆页面
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity_Log";
    public static final int REQUEST_CODE_SETNICK = 1;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView forgetpsw;
    private Button bt_login;

    private boolean progressShow;
    private boolean autoLogin = false;

    private String currentUsername;
    private String currentPassword;
    private ECProgressDialog pd;

    CircleImageView pro_headimg;
    // 全局变量
    String userId;
    //最后登录环信的密码，现在采用接口返回.
    String logineasePWD;

    boolean usernameEditTextpass = false;
    boolean passwordEditTextpass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 如果登录成功过，直接进入主页面
        if (DemoHelper.getInstance().isLoggedIn()) {
            autoLogin = true;
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            return;
        }
        setContentView(R.layout.em_activity_login);
        pd = new ECProgressDialog(LoginActivity.this);

        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        pro_headimg = (CircleImageView) findViewById(R.id.pro_headimg);
        bt_login = (Button) findViewById(R.id.bt_login);
        forgetpsw = (TextView) findViewById(R.id.forgetpsw);
        //只要到了这个界面就代表是重新登录，重置使用过H5页面的缓存。
        PreferenceManager.getInstance().setH5ISFIRSTUSE(true);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loginMoa();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        forgetpsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class).putExtra("from", "forgetpsw"));
            }
        });

        // 如果用户名改变，清空密码
        if ((DemoApplication.getInstance().mCache.getAsObject(Constant.MY_KEY_USERINFO)) != null) {
            usernameEditText.setText(((UserInfoEntity) DemoApplication.getInstance().mCache.getAsObject(Constant.MY_KEY_USERINFO)).getData().getPersonnelId());
            Editable etext = usernameEditText.getText();
            Selection.setSelection(etext, etext.length());// 让光标显示在内容后面
            usernameEditTextpass = true;
        }

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    usernameEditTextpass = true;
                    if (passwordEditTextpass == true) {
                        bt_login.setEnabled(true);
                        bt_login.setBackgroundResource(R.drawable.em_button_login_bg);
                    }
                } else {
                    usernameEditTextpass = false;
                    bt_login.setEnabled(false);
                    bt_login.setBackgroundResource(R.drawable.bg_common_toast);
                }

            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    passwordEditTextpass = true;
                    if (usernameEditTextpass == true) {
                        bt_login.setEnabled(true);
                        bt_login.setBackgroundResource(R.drawable.em_button_login_bg);
                    }
                } else {
                    passwordEditTextpass = false;
                    bt_login.setEnabled(false);
                    bt_login.setBackgroundResource(R.drawable.bg_common_toast);
                }

            }
        });


        usernameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    //使第一个框失去焦点，第二个框获得焦点
                    usernameEditText.clearFocus();
                    passwordEditText.requestFocus();
                }
                return true;
            }
        });
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        loginMoa();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

    }

    /**
     * 点击登录先登录自己服务器，成功后登录环信
     *
     * @throws JSONException
     */
    public void loginMoa() throws JSONException {

        hideSoftInputWindow();

        progressShow = true;

        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        pd.setPressText(getString(R.string.Is_landing));
        pd.show();
        if (!EaseCommonUtils.isNetWorkConnected(this)) {
            pd.dismiss();
            showToastShort(R.string.network_isnot_available);
            return;
        }

        currentUsername = usernameEditText.getText().toString().trim();
        currentPassword = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(currentUsername)) {
            pd.dismiss();
            showToastShort(R.string.User_name_cannot_be_empty);
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            pd.dismiss();
            showToastShort(R.string.Password_cannot_be_empty);
            return;
        }

        JSONObject loginJson = new JSONObject();
        if (currentUsername.length() >= 10) {
            loginJson.put("phone", currentUsername);
            loginJson.put("personnelId", "");
            loginJson.put("userPassword", (CommonUtils.encode(currentPassword)).toUpperCase());
        } else {
            loginJson.put("phone", "");
            loginJson.put("personnelId", currentUsername);
            loginJson.put("userPassword", (CommonUtils.encode(currentPassword)).toUpperCase());
        }
        loginJson.put("deviceId", CommonUtils.getDeviceID(LoginActivity.this));
        loginJson.put("deviceType", "android");
        Log.i("jObject_LoginActivity", loginJson.toString());

        OkHttpUtils.postString().url(Constant.LOGIN_URL).content(loginJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        Log.e(TAG, arg0);
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1) {
                                userId = String.valueOf(jObject.getJSONObject("data").getInt("id"));
//                                Log.i("userId_LoginActivity", userId);
                                PreferenceManager.getInstance()
                                        .setCurrentUserId(userId);
                                logineasePWD = jObject.getJSONObject("data").getString("HXpsw");
                                String sid = jObject.getJSONObject("data").getString("HXpsw");
                                if (sid != null)
                                    PreferenceManager.getInstance()
                                            .setCurrentUserFlowSId(sid);
//                                Log.e(TAG,
//                                        currentUsername);
                                if (jObject.getJSONObject("data").getInt("telephone") == 1) {
                                    //获取自己已读未读数字的信息并且缓存在本地
//                                    appUseUtils.GetMyUnreadMum(LoginActivity.this);
                                    // 获取自己的流程信息并且缓存在本地的函数
//                                    GetMyFlow();
                                    GetMyInfo();
                                } else {
                                    pd.dismiss();
                                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                                }

                            } else {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        pd.dismiss();
                                    }
                                });
                                showToastShort("用户名或密码错误");
//                                 测试用
//                                loginChat();
                            }

//                            showToastShort(jObject.getString("msg"));

                        } catch (Exception e) {
                            warningUnknow();
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    pd.dismiss();
                                }
                            });
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        // Log.i("www", arg0 + "");
                        // Log.i("www", e + "");
                        runOnUiThread(new Runnable() {
                            public void run() {
                                pd.dismiss();
                            }
                        });
                        warningUnknow();
                        // 测试用
//                        loginChat();
                    }
                });
    }


    private void GetMyFlow() {
        // 只有几个userId才有数据，测试先用死数据
        // FlowJson.put("userId", "93");
        // Log.e("FlowJson", FlowJson.toString());

        OkHttpUtils
                .post()
                .url(Constant.FLOWLOGIN_URL)
                .addParams("userId",
                        PreferenceManager.getInstance().getCurrentUserId())
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String arg0) {
//                Log.e("onResponse", arg0.toString());
                JSONObject jObject;
                try {
                    jObject = new JSONObject(arg0);
                    if (jObject.getInt("code") == 1) {
//                        Log.e("onResponse", jObject.getString("msg"));
                        String sid = jObject.getJSONObject("data")
                                .getString("sid");
//                        Log.e("sid_LoginActivity", sid);
                        PreferenceManager.getInstance()
                                .setCurrentUserFlowSId(sid);
                        if (sid != null) {
                            GetMyInfo();
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                pd.dismiss();
                            }
                        });
//                        Toast.makeText(LoginActivity.this,
//                                "流程权限获取失败", Toast.LENGTH_SHORT)
//                                .show();
                        /**
                         * 测试用，有时候工作流获取不到sid，所以在这里如果获取不到就把他给写死咯~*/
//                        GetMyInfo();
//                        PreferenceManager.getInstance()
//                                .setCurrentUserFlowSId("balabalabalabala");
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                        }
                    });
                    warningUnknow();
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Call arg0, Exception arg1) {
//                Log.e("onResponse", arg0.toString());
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                    }
                });
                warningUnknow();
            }
        });
    }

    private void GetMyInfo() throws JSONException {

        JSONObject userIdJson = new JSONObject();
        try {
            userIdJson.put("userId", userId);
            userIdJson.put("sid", PreferenceManager.getInstance().getCurrentUserFlowSId());
            userIdJson.put("deviceId", CommonUtils.getDeviceID(LoginActivity.this));
            userIdJson.put("deviceType", "android");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

//        Log.i("jObject_Login", userIdJson.toString());

        OkHttpUtils.postString().url(Constant.USERINFO_URL)
                .content(userIdJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
//                        Log.e(TAG, arg0);
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1 && jObject.getJSONObject("data") != null) {
                                UserInfoEntity userInfoEntity = new UserInfoEntity();
                                userInfoEntity = new Gson().fromJson(arg0,
                                        UserInfoEntity.class);
                                DemoApplication.getInstance().mCache.put(
                                        Constant.MY_KEY_USERINFO,
                                        userInfoEntity);
                                PreferenceManager.getInstance().setCurrentUserRealName(userInfoEntity.getData().getUserRealname().toString());
                                PreferenceManager.getInstance().setCurrentUserDeptmentId(userInfoEntity.getData().getDepartmentId());
                                PreferenceManager.getInstance().setCurrentUserDeptmentName(userInfoEntity.getData().getDepartmentName());
//                                Log.i(TAG,
//                                        userInfoEntity.getData()
//                                                .getUserRealname() + userInfoEntity.getData().getUserId());
                                // 登录环信

                                loginChat();
                            }
                        } catch (JSONException e) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    pd.dismiss();
                                }
                            });
                            warningUnknow();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
//                        Log.i(TAG, arg0 + "---"
//                                + arg1.toString());
                        runOnUiThread(new Runnable() {
                            public void run() {
                                pd.dismiss();
                            }
                        });
                        warningUnknow();

                    }
                });

    }

    /**
     * 登录
     */
    public void loginChat() {
        // After logout，the DemoDB may still be accessed due to async callback,
        // so the DemoDB will be re-opened again.
        // close it before login to make sure DemoDB not overlap
        DemoDBManager.getInstance().closeDB();

        // reset current user name before login
        DemoHelper.getInstance().setCurrentUserName(userId);

        // 调用sdk登陆方法登陆聊天服务器
        //(CommonUtils.encode(currentPassword)).toUpperCase()
        EMClient.getInstance().login(userId, logineasePWD,
                new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        // 第一次登录或者之前logout后再登录，加载所有本地群和回话
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager()
                                .loadAllConversations();

                        // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                        // EMClient.getInstance().updateCurrentUserNick(
                        // DemoApplication.currentUserNick.trim());

                        // 异步获取当前用户的昵称和头像(从自己服务器获取，demo使用的一个第三方服务)
                        DemoHelper.getInstance().getUserProfileManager()
                                .asyncGetCurrentUserInfo();
                        //开启JPush
                        JPushInterface.resumePush(getApplicationContext());
                        //设置JPush的别名
                        JPushInterface.setAlias(LoginActivity.this, EMClient.getInstance().getCurrentUser(), new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {
                                Log.i("JPush111", "Jpush status: " + i + s);// 状态 为 0 时标示成功
                            }
                        });
                        runOnUiThread(new Runnable() {
                            public void run() {
                                pd.dismiss();
                            }
                        });
                        // 进入主页面
                        startActivity(new Intent(LoginActivity.this,
                                MainActivity.class));

                        finish();
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(final int code, final String message) {
                        if (!progressShow) {
                            return;
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                pd.dismiss();
                                showToastShort(getString(R.string.Login_failed)
                                        + message);
                            }
                        });
                    }
                });
    }

    /**
     * 注册
     *
     * @param view
     */
    public void register(View view) {
        startActivityForResult(new Intent(this, PreRegisterActivity.class), 0);
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (autoLogin) {
            return;
        }
    }

}
