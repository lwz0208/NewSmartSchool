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

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bugtags.library.Bugtags;
import com.ding.easeui.ui.EaseBaseActivity;

public class BaseActivity extends EaseBaseActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // umeng
        //MobclickAgent.onResume(this);
        Bugtags.onResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // umeng
        //MobclickAgent.onPause(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 注：回调 2
        Bugtags.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // 注：回调 3
        Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }

    /**
     * @param value
     */
    public void showToastShort(String value) {
        if (value == null) {
            return;
        }
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
    }

    public void showToastShort(int value) {
        if (value <= 0) {
            return;
        }
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
    }

    public void showToastLong(String value) {
        Toast.makeText(this, value, Toast.LENGTH_LONG).show();
    }

    public void warningUnknow() {
        Toast.makeText(this, "网络故障，请检查网络设置！", Toast.LENGTH_SHORT).show();
    }

    public void warningNoData() {
        Toast.makeText(this, "暂无数据", Toast.LENGTH_SHORT).show();
    }

    public void hideSoftInputWindow() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
