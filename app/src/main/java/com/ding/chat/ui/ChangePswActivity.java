package com.ding.chat.ui;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ding.chat.R;
import com.ding.chat.views.ECProgressDialog;
import com.ding.easeui.Constant;
import com.ding.easeui.utils.CommonUtils;
import com.ding.easeui.utils.PreferenceManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MediaType;

public class ChangePswActivity extends BaseActivity {
    String TAG = "ChangePswActivity_Debugs";
    private EditText txt_edit_oldpsw;
    private EditText txt_edit_newpsw;
    private EditText txt_edit_comfirmnewpsw;
    private Button btn_surechangepsw;
    ECProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_psw);
        initView();
    }

    private void initView() {
        txt_edit_oldpsw = (EditText) findViewById(R.id.txt_edit_oldpsw);
        txt_edit_newpsw = (EditText) findViewById(R.id.txt_edit_newpsw);
        txt_edit_comfirmnewpsw = (EditText) findViewById(R.id.txt_edit_comfirmnewpsw);
        btn_surechangepsw = (Button) findViewById(R.id.btn_surechangepsw);
        progressDialog = new ECProgressDialog(ChangePswActivity.this);
        btn_surechangepsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_edit_oldpsw.getText().toString().equals("")) {
                    showToastShort("请填写原密码");
                    return;
                } else if (txt_edit_newpsw.getText().toString().equals("") || txt_edit_comfirmnewpsw.getText().equals("")) {
                    showToastShort("请填写新密码");
                    return;
                } else if (!txt_edit_newpsw.getText().toString().equals(txt_edit_comfirmnewpsw.getText().toString())) {
                    showToastShort("两次输入的新密码不一致");
                    return;
                } else {
                    progressDialog.show();
                    JSONObject changepsw = new JSONObject();
                    try {
                        changepsw.put("userId", PreferenceManager.getInstance().getCurrentUserId());
                        changepsw.put("oldPassword", (CommonUtils.encode(txt_edit_oldpsw.getText().toString().trim())).toUpperCase());
                        changepsw.put("newPassword", txt_edit_newpsw.getText().toString().trim());
                        CommonUtils.setCommonJson(ChangePswActivity.this, changepsw, PreferenceManager.getInstance().getCurrentUserFlowSId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG, changepsw.toString());
                    OkHttpUtils.postString().url(Constant.CHANGEUSERPSW_URL)
                            .content(changepsw.toString())
                            .mediaType(MediaType.parse("application/json")).build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e) {
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onResponse(String s) {
                                    progressDialog.dismiss();
                                    Log.e(TAG, s);
                                    try {
                                        JSONObject result = new JSONObject(s);
                                        if (result.getInt("code") == 1) {
                                            showToastShort("修改成功");
                                            finish();
                                            if (Build.VERSION.SDK_INT > 5) {
                                                overridePendingTransition(
                                                        R.anim.slide_in_from_left, R.anim.slide_out_to_right);
                                            }
                                        } else {
                                            showToastShort("修改失败");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (NullPointerException e) {
                                        showToastShort("修改失败");
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            }
        });
    }

    public void back(View view) {
        finish();
        if (Build.VERSION.SDK_INT > 5) {
            overridePendingTransition(
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }

}
