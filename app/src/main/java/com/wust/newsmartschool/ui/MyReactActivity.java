package com.wust.newsmartschool.ui;

import com.wust.newsmartschool.BuildConfig;
import com.wust.newsmartschool.DemoApplication;
import com.wust.newsmartschool.MyReactPackage;
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;

import java.util.Arrays;
import java.util.List;

public class MyReactActivity extends ReactActivity {


    @Override
    protected String getMainComponentName() {
        return "AwesomeProject";
    }

    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(DemoApplication.getInstance()) {
        @Override
        protected boolean getUseDeveloperSupport() {
            return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.<ReactPackage>asList(
                    new MainReactPackage(),
                    new MyReactPackage()
            );
        }
    };

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }
}




