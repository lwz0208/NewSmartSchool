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

import okhttp3.Call;
import okhttp3.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import com.wust.easeui.utils.PreferenceManager;
import com.wust.easeui.utils.CommonUtils;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.wust.newsmartschool.DemoHelper;
import com.wust.newsmartschool.R;
import com.wust.easeui.Constant;
import com.hyphenate.exceptions.HyphenateException;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 绑定手机
 */
public class RegisterActivity extends BaseActivity {
    String TAG = "RegisterActivity";
    private EditText codeEditText;
    private EditText telephone_register;
    private EditText passwordEditText;
    private EditText confirmPwdEditText;
    private Button btn_get_checkcode;
    private TextView register_title;
    private Button btn_sure_register;
    String code;//验证码
    String phone;//电话好吗
    String pwd;//密码
    String confirm_pwd;//确认的密码
    String checkdata;//收到的验证码
    ProgressDialog pd;
    String fromStr;
    String GETCODE_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_register);
        register_title = (TextView) findViewById(R.id.register_title);
        codeEditText = (EditText) findViewById(R.id.code_register);
        telephone_register = (EditText) findViewById(R.id.telephone_register);
        passwordEditText = (EditText) findViewById(R.id.password_register);
        confirmPwdEditText = (EditText) findViewById(R.id.confirm_password_register);
        btn_get_checkcode = (Button) findViewById(R.id.btn_get_checkcode);
        btn_sure_register = (Button) findViewById(R.id.btn_sure_register);
        pd = new ProgressDialog(this);
        fromStr = getIntent().getStringExtra("from");
        if (fromStr != null) {
            if (fromStr.equals("forgetpsw")) {
                register_title.setText("找回密码");
            } else {
                register_title.setText("绑定手机");
            }
        } else {
            fromStr = "";
        }
        btn_get_checkcode.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     //如果不为空，就调用发送短信接口
                                                     if (!telephone_register.getText().toString().trim().equals("")) {
                                                         new TimeCount(60 * 1000, 1000).start();
                                                         try {
                                                             if (fromStr.equals("forgetpsw")) {
                                                                 GETCODE_URL = Constant.GETCHECKCODEFORPSW_URL;
                                                             } else {
                                                                 GETCODE_URL = Constant.GETCHECKCODE_URL;
                                                             }
                                                             getCodetoPhone();
                                                         } catch (JSONException e) {
                                                             e.printStackTrace();
                                                         }
                                                     } else {
                                                         showToastShort("请输入手机号");
                                                     }
                                                 }
                                             }

        );
        btn_sure_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = codeEditText.getText().toString().trim();
                phone = telephone_register.getText().toString().trim();
                pwd = passwordEditText.getText().toString().trim();
                confirm_pwd = confirmPwdEditText.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.User_name_cannot_be_empty),
                            Toast.LENGTH_SHORT).show();
                    telephone_register.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(code)) {
                    Toast.makeText(RegisterActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    codeEditText.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(
                            RegisterActivity.this,
                            getResources().getString(R.string.Password_cannot_be_empty),
                            Toast.LENGTH_SHORT).show();
                    passwordEditText.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(confirm_pwd)) {
                    Toast.makeText(
                            RegisterActivity.this,
                            getResources().getString(
                                    R.string.Confirm_password_cannot_be_empty),
                            Toast.LENGTH_SHORT).show();
                    confirmPwdEditText.requestFocus();
                    return;
                } else if (!pwd.equals(confirm_pwd)) {
                    Toast.makeText(RegisterActivity.this,
                            getResources().getString(R.string.Two_input_password),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(pwd)
                        && !TextUtils.isEmpty(code)) {
                    try {
                        if (fromStr.equals("forgetpsw")) {
                            surefindpsw();
                        } else {
                            checkcode();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void getCodetoPhone() throws JSONException {
        JSONObject checkPhoJson = new JSONObject();
        checkPhoJson.put("phone", telephone_register.getText().toString().trim());
        CommonUtils.setCommonJson(RegisterActivity.this, checkPhoJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i(TAG, checkPhoJson.toString());
        OkHttpUtils.postString().url(GETCODE_URL)
                .content(checkPhoJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        showToastShort("请求失败");
                    }

                    @Override
                    public void onResponse(String s) {
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(s);
                            if (jObject.getInt("code") == 1) {
                                showToastShort("请注意留意短信信息");
                                checkdata = String.valueOf(jObject.getInt("data"));
                            } else if (jObject.getInt("code") == 2) {
                                if (fromStr.equals("forgetpsw")) {
                                    showToastShort("该手机未注册");
                                } else {
                                    showToastShort("该手机已注册");
                                }

                            } else {
                                showToastShort("请求失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    public void checkcode() throws JSONException {
        pd.show();
        JSONObject loginJson = new JSONObject();
        loginJson.put("phone", phone);
        loginJson.put("smsCode", code);
        CommonUtils.setCommonJson(RegisterActivity.this, loginJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i(TAG, loginJson.toString());

        OkHttpUtils.postString().url(Constant.CHECKTHECODE)
                .content(loginJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String arg0) {
                        pd.dismiss();
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1) {
                                register_self();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        jObject.getString("msg"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "绑定失败...", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    public void register_self() throws JSONException {
        pd.show();
        JSONObject loginJson = new JSONObject();
        loginJson.put("phone", phone);
        loginJson.put("code", code);
        loginJson.put("password", (CommonUtils.encode(pwd)).toUpperCase());
        loginJson.put("userId", PreferenceManager.getInstance().getCurrentUserId());
        CommonUtils.setCommonJson(RegisterActivity.this, loginJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i(TAG, loginJson.toString());
        OkHttpUtils.postString().url(Constant.GEGISTER_URL)
                .content(loginJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        pd.dismiss();
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1) {
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        jObject.getString("msg"),
                                        Toast.LENGTH_SHORT).show();
                            }
//                            Log.i("jObject", jObject.getInt("code")
//                                    + "---" + jObject.getString("msg"));

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "绑定失败...", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void surefindpsw() throws JSONException {
        pd.show();
        JSONObject loginJson = new JSONObject();
        loginJson.put("phone", phone);
        loginJson.put("code", code);
        loginJson.put("password", (CommonUtils.encode(pwd)).toUpperCase());
        CommonUtils.setCommonJson(RegisterActivity.this, loginJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i(TAG, loginJson.toString());

        OkHttpUtils.postString().url(Constant.FINDFORGETPSW_URL)
                .content(loginJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String arg0) {
                        pd.dismiss();
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1) {
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        jObject.getString("msg"),
                                        Toast.LENGTH_SHORT).show();
                            }
//                            Log.i("jObject", jObject.getInt("code")
//                                    + "---" + jObject.getString("msg"));

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "绑定失败...", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    /***
     * 获取验证码倒计时
     */
    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_get_checkcode.setClickable(false);
            btn_get_checkcode.setText(millisUntilFinished / 1000 + "秒后可重发");
        }

        @Override
        public void onFinish() {
            btn_get_checkcode.setText("重新获取");
            btn_get_checkcode.setClickable(true);
        }

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
                            phone.substring(0, 11), pwd);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!RegisterActivity.this.isFinishing())
                                pd.dismiss();
                            // 保存用户名
                            DemoHelper.getInstance().setCurrentUserName(
                                    phone.substring(0, 11));
                            Toast.makeText(
                                    getApplicationContext(),
                                    getResources().getString(
                                            R.string.Registered_successfully),
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!RegisterActivity.this.isFinishing())
                                pd.dismiss();
                            int errorCode = e.getErrorCode();
                            if (errorCode == EMError.NETWORK_ERROR) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        getResources().getString(
                                                R.string.network_anomalies),
                                        Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        getResources().getString(
                                                R.string.User_already_exists),
                                        Toast.LENGTH_SHORT).show();
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

    public void back(View view) {
        finish();
    }

}
