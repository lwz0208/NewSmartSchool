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
package com.wust.newsmartschool.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
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

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.EaseCommonUtils;
import com.wust.easeui.utils.PreferenceManager;
import com.wust.newsmartschool.DemoApplication;
import com.wust.newsmartschool.DemoHelper;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.db.DemoDBManager;
import com.wust.newsmartschool.domain.UserInfoEntity;
import com.wust.newsmartschool.utils.WebServiceUtils;
import com.wust.newsmartschool.utils.appUseUtils;
import com.wust.newsmartschool.views.CircleImageView;
import com.wust.newsmartschool.views.ECProgressDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.LinkedHashMap;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.MediaType;

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
    private String result1;
    private ProgressDialog dialog;

    private String currentUsername;
    private String currentPassword;
    private ECProgressDialog pd;
    public JSONArray flagpj = new JSONArray();
    CircleImageView pro_headimg;

    boolean usernameEditTextpass = false;
    boolean passwordEditTextpass = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            try {
                if (msg.what == 100) {
                    String resultString = (String) (msg.obj);
                    Log.i("resultString", resultString);
                    if ((resultString.substring(resultString.indexOf("=") + 1, resultString.indexOf("=") + 2)).equals("1")) {
/**
 *教务处验证成功后，获取咱们后台的个人信息并且缓存起来，缓存起来后就调用登录环信的方法，最后跳转。
 *
 * */
                        PreferenceManager.getInstance().setCurrentUserId(currentUsername);
                        GetMyInfo();
//                        startActivity(new Intent(LoginActivity.this, MainActivity.class));

                    } else {

                        {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    pd.dismiss();
                                }
                            });
                            showToastShort("用户名或密码错误");
                        }

                        if (msg.what == 101) {
                            warningUnknow();
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    pd.dismiss();
                                }
                            });
                        }
                    }
                }
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
    };

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
                    loginWebService();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        forgetpsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("找回密码");
                builder.setMessage("请登录教务处网站修改密码");
                builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                // startActivity(new Intent(LoginActivity.this, RegisterActivity.class).putExtra("from", "forgetpsw"));
            }
        });


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
                        loginWebService();
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
    public void loginWebService() throws JSONException {

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

        LinkedHashMap mapParams = new LinkedHashMap();

        mapParams.put("username", currentUsername);
        mapParams.put("password", currentPassword);
        mapParams.put("time", appUseUtils.getTime());
        mapParams.put("chkvalue", appUseUtils.getParamtowebservice());
        WebServiceUtils.call(Constant.SERVICE_URL, Constant.NAMESPACE, "newlogin", mapParams, new WebServiceUtils.Response() {


            @Override
            public void onSuccess(final SoapObject result) {
                result1 = result.toString();
                Runnable runnable = new Runnable() {

                    @Override
                    public void run() {
                        if (result1 != null) {
                            System.out.println(result1);
                            handler.sendMessage(handler.obtainMessage(100, result1));

                        } else {
                            handler.sendMessage(handler.obtainMessage(101));
                        }
                    }
                };
                new Thread(runnable).start();

            }

            @Override
            public void onError(Exception e) {

            }
        });


    }


    private void GetMyInfo() throws JSONException {
        JSONObject userIdJson = new JSONObject();
        try {
            userIdJson.put("id", currentUsername);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        OkHttpUtils.postString().url(Constant.USERINFO_URL)
                .content(userIdJson.toString())
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
                                //这里是整体存储，存的是一个实体。
                                DemoApplication.getInstance().mCache.put(
                                        Constant.MY_KEY_USERINFO,
                                        userInfoEntity);
                                //一下三个因为常会用到，所以单独存储一遍，方便使用。
                                PreferenceManager.getInstance().setCurrentUserRealName(userInfoEntity.getData().getName().toString());
                                PreferenceManager.getInstance().setCurrentUserColleageName(userInfoEntity.getData().getCollegeName());
                                PreferenceManager.getInstance().setCurrentUserColleageId(userInfoEntity.getData().getCollegeId());
                                PreferenceManager.getInstance().setCurrentUserClassId(userInfoEntity.getData().getClassId());
                                PreferenceManager.getInstance().setCurrentUserClassName(userInfoEntity.getData().getClassName());
                                // 登录环信
//                                loginChat();
                                register();
                                //环信暂时出了点问题，跳过。
//                                startActivity(new Intent(LoginActivity.this,
//                                        MainActivity.class));
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
                        Log.i(TAG, arg0 + "---"
                                + arg1.toString());
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
        DemoHelper.getInstance().setCurrentUserName(currentUsername);

        // 调用sdk登陆方法登陆聊天服务器
        EMClient.getInstance().login(currentUsername, currentPassword,
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
     * 注册环信
     */

    public void register() {

        new Thread(new Runnable() {
            public void run() {
                try {
                    // 调用sdk注册方法
                    EMClient.getInstance().createAccount(
                            currentUsername, currentPassword);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!LoginActivity.this.isFinishing())
                                pd.dismiss();
                            // 保存用户名
                            DemoHelper.getInstance().setCurrentUserName(
                                    currentUsername);
                            loginChat();
//                            Toast.makeText(
//                                    getApplicationContext(),
//                                    getResources().getString(
//                                            R.string.Registered_successfully),
//                                    Toast.LENGTH_SHORT).show();

                        }
                    });
                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!LoginActivity.this.isFinishing())
                                pd.dismiss();
                            int errorCode = e.getErrorCode();
                            if (errorCode == EMError.NETWORK_ERROR) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        getResources().getString(
                                                R.string.network_anomalies),
                                        Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_ALREADY_EXIST) {
//                                Toast.makeText(
//                                        getApplicationContext(),
//                                        getResources().getString(
//                                                R.string.User_already_exists),
//                                        Toast.LENGTH_SHORT).show();
                                loginChat();
                            } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        getResources()
                                                .getString(
                                                        R.string.registration_failed_without_permission),
                                        Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        getResources().getString(
                                                R.string.illegal_user_name),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        getResources().getString(
                                                R.string.Registration_failed)
                                                + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();

    }

    //    /**
//     * 注册
//     *
//     * @param view
//     */
//    public void register(View view) {
//        startActivityForResult(new Intent(this, PreRegisterActivity.class), 0);
//    }
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
