package com.ding.chat.ui;

import android.app.Activity;
import android.os.Bundle;

import com.ding.chat.R;
import com.ding.chat.fragments.ContactListFragment;
import com.ding.chat.fragments.NewsFragment;
import com.ding.chat.fragments.SettingsFragment;

public class SettingActivity extends BaseActivity {
    //    private SettingsFragment settingsFragment;
    private NewsFragment dingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        dingFragment = new NewsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.setting_fragment, dingFragment)
                .show(dingFragment).commit();

    }
}
