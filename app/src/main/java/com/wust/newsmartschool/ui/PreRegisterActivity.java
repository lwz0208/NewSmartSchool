package com.wust.newsmartschool.ui;

import okhttp3.Call;
import okhttp3.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.R.id;
import com.wust.easeui.utils.PreferenceManager;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.CommonUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PreRegisterActivity extends BaseActivity {
    private EditText register_phoneEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_register);
        register_phoneEdit = (EditText) findViewById(id.register_phone);
    }

    public void preRegister_btn(View view) throws JSONException {
        final String register_phone = register_phoneEdit.getText().toString()
                .trim();
        if (TextUtils.isEmpty(register_phone)) {
            Toast.makeText(this, "电话号码不可以为空", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject loginJson = new JSONObject();
        loginJson.put("phone", register_phone);
        CommonUtils.setCommonJson(PreRegisterActivity.this, loginJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i("jObject", loginJson.toString());

        OkHttpUtils.postString().url(Constant.GEGISTER_URL)
                .content(loginJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            Log.i("jObject", jObject.getString("data"));
                            if (jObject.getInt("code") == 1
                                    || jObject.getInt("code") == 2) {
                                startActivity(new Intent(
                                        PreRegisterActivity.this,
                                        RegisterActivity.class).putExtra(
                                        "phone", jObject.getString("data")));
                                Log.i("jObject", jObject.getString("data"));
                                finish();

                            } else

                                Toast.makeText(getApplicationContext(),
                                        jObject.getString("msg"),
                                        Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        Toast.makeText(getApplicationContext(),
                                "发送验证码失败,请稍后再试", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void back(View view) {
        finish();
    }

}
