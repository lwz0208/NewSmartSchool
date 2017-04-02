package com.wust.newsmartschool.utils;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by Erick on 2017/1/12.
 */
public class WebParams {
    public Context context;

    public WebParams(Context context){
        this.context = context;
    }

    @JavascriptInterface
    public void acllJs(){
        Toast.makeText(context, "点击了登录按钮！", Toast.LENGTH_SHORT).show();
    }
}
