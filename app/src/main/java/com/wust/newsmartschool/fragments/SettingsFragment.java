/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p/>
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
package com.wust.newsmartschool.fragments;

import com.bumptech.glide.Glide;
import com.wust.newsmartschool.DemoApplication;
import com.wust.newsmartschool.DemoHelper;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.UpdataEntity;
import com.wust.newsmartschool.domain.UserInfoEntity;
import com.wust.newsmartschool.ui.LoginActivity;
import com.wust.newsmartschool.ui.MessageSettingActivity;
import com.wust.newsmartschool.ui.UserInfoActivity;
import com.wust.newsmartschool.utils.appUseUtils;
import com.wust.newsmartschool.views.ECProgressDialog;
import com.wust.easeui.utils.BadgeUtil;
import com.wust.easeui.utils.CommonUtils;
import com.wust.easeui.utils.PreferenceManager;
import com.wust.easeui.widget.GlideRoundTransform;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.wust.easeui.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.model.UpdateParser;
import org.lzh.framework.updatepluginlib.strategy.UpdateStrategy;

import java.math.BigDecimal;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * 设置界面
 */
public class SettingsFragment extends Fragment implements OnClickListener {
    String TAG = "SettingsFragment_Debugs";
    private ImageView head_imag;
    private TextView username;
    // 下面的按钮项
    RelativeLayout rl_setting_layout;
    RelativeLayout rl_check_updata_apk;
    RelativeLayout new_userinfo_layout;
    RelativeLayout rl_clear_cache;
    UpdataEntity updataEntity;
    ECProgressDialog pd;
    /**
     * 退出按钮
     */
    private Button logoutBtn;
    //多出用到，故作为全局变量。
    UserInfoEntity userInfoEntity;
    //缓存大小
    private TextView tv_memorysize;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.em_fragment2_conversation_settings,
                container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null
                && savedInstanceState.getBoolean("isConflict", false))
            return;
        pd = new ECProgressDialog(getActivity());
        username = (TextView) getView().findViewById(R.id.new_username);
        head_imag = (ImageView) getView()
                .findViewById(R.id.new_head_imag);
        tv_memorysize = (TextView) getView().findViewById(R.id.tv_memorysize);
        rl_clear_cache = (RelativeLayout) getView().findViewById(
                R.id.rl_clear_cache);
        rl_check_updata_apk = (RelativeLayout) getView().findViewById(
                R.id.rl_check_updata_apk);
        new_userinfo_layout = (RelativeLayout) getView().findViewById(
                R.id.new_userinfo_layout);
        rl_setting_layout = (RelativeLayout) getView().findViewById(
                R.id.rl_setting_layout);
        logoutBtn = (Button) getView().findViewById(R.id.btn_logout);
        logoutBtn.setOnClickListener(this);
        new_userinfo_layout.setOnClickListener(this);
        rl_check_updata_apk.setOnClickListener(this);
        rl_setting_layout.setOnClickListener(this);
        rl_clear_cache.setOnClickListener(this);
        getUserInfo();
    }

    /* 获取个人信息 */
    public void getUserInfo() {
        username.setText(PreferenceManager.getInstance().getCurrentRealName());
        new Thread(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().updateCurrentUserNick(
                        PreferenceManager.getInstance().getCurrentRealName());
            }
        });
        Glide.with(getActivity())
                .load(Constant.GETHEADIMAG_URL
                        + PreferenceManager.getInstance().getCurrentUserId()
                        + ".png").transform(new GlideRoundTransform(getActivity())).placeholder(R.drawable.ease_default_avatar)
                .into(head_imag);
    }


    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            // 退出登陆
            case R.id.btn_logout:
                //退出之后清除个人信息的缓存，并且暂停JPush
                DemoApplication.getInstance().mCache.remove(Constant.MY_KEY_USERINFO);
                JPushInterface.stopPush(getActivity().getApplicationContext());
                logout();
                break;

            // 新添加，跳转到用户展示界面
            case R.id.new_userinfo_layout:
                if (username.getText().equals("")) {
                    Toast.makeText(getContext(), "个人信息获取失败，请重新启动", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    startActivity(new Intent(getActivity(), UserInfoActivity.class));
                    if (android.os.Build.VERSION.SDK_INT > 5) {
                        getActivity().overridePendingTransition(
                                R.anim.slide_in_from_right,
                                R.anim.slide_out_to_left);
                    }
                }

                break;
            case R.id.rl_setting_layout:
                startActivity(new Intent(getContext(), MessageSettingActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    getActivity().overridePendingTransition(
                            R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);
                }
                break;
            case R.id.rl_check_updata_apk:
                pd.setPressText("正在检查更新...");
                pd.show();
                try {
                    Updata();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rl_clear_cache:
                pd.setPressText("正在清除...");
                pd.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(getActivity()).clearDiskCache();
                    }
                }).start();
                Glide.get(getActivity()).clearMemory();
                DemoApplication.getInstance().mCache.clear();
                GetMyInfo(getActivity());
                break;
            default:
                break;
        }
    }

    //请求服务器比对最新版本号
    public void Updata() throws JSONException {
        JSONObject jj = new JSONObject();
        jj.put("type", "Android");
        CommonUtils.setCommonJson(getActivity(), jj, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.e(TAG, jj.toString());
        OkHttpUtils.postString().url(Constant.GETUPDATA_URL)
                .content(jj.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e(TAG, call.toString() + "-" + e.toString());
                        pd.dismiss();
                        Toast.makeText(getActivity(), "检查更新失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String s) {
                        pd.dismiss();
                        Log.e(TAG, s.toString());
                        updataEntity = new Gson().fromJson(s, UpdataEntity.class);
                        PackageManager pm = getActivity().getPackageManager();
                        try {
                            Log.e(TAG, updataEntity.getData().getApkUrl().toString());
                            PackageInfo pi = pm.getPackageInfo(getActivity().getPackageName(), 0);
                            Log.e(TAG, pi.versionCode + "");
                            Log.e(TAG, updataEntity.getData().getVersionCode() + "");
                            if (pi.versionCode != updataEntity.getData().getVersionCode()) {
                                UpdateBuilder.create().url("http://www.qq.com/").jsonParser(new UpdateParser() {
                                    @Override
                                    public Update parse(String response) {
                                        Log.e(TAG, response);
                                        Update update = new Update("Android");
                                        update.setUpdateTime(System.currentTimeMillis());
                                        update.setUpdateUrl(updataEntity.getData().getApkUrl());
                                        update.setVersionCode(updataEntity.getData().getVersionCode());
//                                        update.setVersionCode(1);
                                        update.setVersionName(updataEntity.getData().getVersionName());
//                                        update.setVersionName("1.0.1");
                                        update.setUpdateContent(updataEntity.getData().getRemark());
                                        update.setForced(true); // 此apk包是否为强制更新
                                        update.setIgnore(false);// 是否显示忽略此次版本更新
                                        return update;
                                    }
                                }).strategy(new UpdateStrategy() {
                                    @Override
                                    public boolean isShowUpdateDialog(Update update) {
                                        return true;// 有新更新直接展示
                                    }

                                    @Override
                                    public boolean isAutoInstall() {
                                        return true;
                                    }

                                    @Override
                                    public boolean isShowDownloadDialog() {
                                        return true;// 展示下载进度
                                    }
                                })
                                        .check(getActivity());
                            } else {
                                Toast.makeText(getActivity(), "已经是最新版本", Toast.LENGTH_SHORT).show();
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "检查更新失败", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }

    void logout() {
        //退出登录后就重置APP图标上的未读消息条数
        BadgeUtil.resetBadgeCount(getActivity());
        final ProgressDialog pd = new ProgressDialog(getActivity());
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        DemoHelper.getInstance().logout(false, new EMCallBack() {

            @Override
            public void onSuccess() {
                JPushInterface.clearLocalNotifications(getActivity());
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        // 重新显示登陆页面
                        getActivity().finish();
                        startActivity(new Intent(getActivity(),
                                LoginActivity.class));

                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(getActivity(),
                                "unbind devicetokens failed",
                                Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    private void GetMyInfo(Context mContext) {

        JSONObject phone = new JSONObject();
        try {
            phone.put("userId", EMClient.getInstance().getCurrentUser());
            CommonUtils.setCommonJson(mContext, phone, PreferenceManager.getInstance().getCurrentUserFlowSId());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        OkHttpUtils.postString().url(Constant.USERINFO_URL)
                .content(phone.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        pd.dismiss();
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1 && jObject.getJSONObject("data") != null) {
                                Toast.makeText(getActivity(), "清除成功", Toast.LENGTH_SHORT).show();
                                tv_memorysize.setText(appUseUtils.getCacheSize(getActivity()));
                                userInfoEntity = new Gson().fromJson(arg0,
                                        UserInfoEntity.class);
                                DemoApplication.getInstance().mCache.put(
                                        Constant.MY_KEY_USERINFO,
                                        userInfoEntity);
                                PreferenceManager.getInstance().setCurrentUserRealName(userInfoEntity.getData()
                                        .getName().toString());
//                                PreferenceManager.getInstance().setfriendsMsgOnly(userInfoEntity.getData().getReceiveStatus());
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        EMClient.getInstance().updateCurrentUserNick(
                                                userInfoEntity.getData()
                                                        .getName());
                                    }
                                });

                            } else {
                                Toast.makeText(getActivity(), "清除失败", Toast.LENGTH_SHORT).show();
                                DemoApplication.getInstance().mCache.remove(Constant.MY_KEY_USERINFO);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        pd.dismiss();
                        Toast.makeText(getActivity(), "清除失败", Toast.LENGTH_SHORT).show();
                        Log.i("SettingsFragment", arg0 + "---" + arg1.toString());
                        DemoApplication.getInstance().mCache.remove(Constant.MY_KEY_USERINFO);
                    }
                });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //如果出现在眼前。。。
        if (hidden == false) {
            Double ASimpleCacheSize = DemoApplication.getInstance().mCache.CacheSize();
            String GlideCacheSize = appUseUtils.getCacheSize(getActivity());
            Log.e("FormatCacheSize", ASimpleCacheSize + "/-/" + GlideCacheSize);
            tv_memorysize.setText(FormatCacheSize(ASimpleCacheSize + Double.valueOf(GlideCacheSize)));
            Glide.with(getActivity())
                    .load(Constant.GETHEADIMAG_URL
                            + PreferenceManager.getInstance().getCurrentUserId()
                            + ".png").transform(new GlideRoundTransform(getActivity())).placeholder(R.drawable.ease_default_avatar)
                    .into(head_imag);
        }
    }

    private String FormatCacheSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

}

