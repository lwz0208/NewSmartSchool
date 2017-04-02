package com.wust.newsmartschool.ui;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.wust.newsmartschool.utils.appUseUtils;
import com.hyphenate.chat.EMClient;
import com.wust.easeui.Constant;
import com.wust.newsmartschool.DemoApplication;
import com.wust.newsmartschool.DemoHelper;
import com.wust.newsmartschool.R;

import java.util.Set;

/**
 * 开屏页
 */
public class SplashActivity extends Activity {
    String TAG = "SplashActivity_Debugs";
    private RelativeLayout rootLayout;
    // private TextView versionText;

    private static final int sleepTime = 2000;

    @Override
    protected void onCreate(Bundle arg0) {
        setContentView(R.layout.em_activity_splash);
        super.onCreate(arg0);
        rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1500);
        rootLayout.startAnimation(animation);

    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            public void run() {
                if (DemoHelper.getInstance().isLoggedIn()) {
                    // ** 免登陆情况 加载所有本地群和会话
                    // 不是必须的，不加sdk也会自动异步去加载(不会重复加载);
                    // 加上的话保证进了主页面会话和群组都已经load完毕
                    //如果个人信息为空，那就请求一下接口
                    if ((DemoApplication.getInstance().mCache.getAsObject(
                            Constant.MY_KEY_USERINFO)) == null)
                        appUseUtils.RefreshMyInfo(SplashActivity.this);
                    long start = System.currentTimeMillis();
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    //设置JPush的别名
                    JPushInterface.setAlias(SplashActivity.this, EMClient.getInstance().getCurrentUser(), new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {
                            Log.i("JPush111", "Jpush status: " + i + s);// 状态 为 0 时标示成功
                        }
                    });
                    long costTime = System.currentTimeMillis() - start;
                    // 等待sleeptime时长
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // 进入主页面
                    startActivity(new Intent(SplashActivity.this,
                            MainActivity.class));
                    finish();
                } else {
                    //如果没有登录，那肯定是会跳到登录界面的。就将推送停止
                    JPushInterface.stopPush(SplashActivity.this);
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                    startActivity(new Intent(SplashActivity.this,
                            LoginActivity.class));
                    finish();
                }
            }
        }).start();

    }


    /**
     * 获取当前应用程序的版本号
     */
    private String getVersion() {
        return EMClient.getInstance().getChatConfig().getVersion();
    }
}
