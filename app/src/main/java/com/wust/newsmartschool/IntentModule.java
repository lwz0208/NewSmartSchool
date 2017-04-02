package com.wust.newsmartschool;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/**
 * Created by bsg on 2016/9/21.
 */
public class IntentModule extends ReactContextBaseJavaModule {

    public IntentModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "IntentModule";
    }

    /**
     * Activtiy跳转到JS页面，传输数据
     *
     * @param successBack
     * @param errorBack
     */
    @ReactMethod
    public void dataToJS(Callback successBack, Callback errorBack) {
        try {
            Activity currentActivity = getCurrentActivity();
            Bundle bundle = currentActivity.getIntent().getExtras();
            String sid = bundle.getString("sid");
            String userId = bundle.getString("userId");
            String fk_flow = bundle.getString("fk_flow");
            String shenheren = bundle.getString("shenheren");
            String workID = bundle.getString("workID");
            String ispass = bundle.getString("ispass");
            String starterName = bundle.getString("starterName");
//            successBack.invoke(sid);
            successBack.invoke(sid, userId, fk_flow, shenheren, workID, ispass, starterName);
        } catch (Exception e) {
            errorBack.invoke(e.getMessage());
        }
    }

}
