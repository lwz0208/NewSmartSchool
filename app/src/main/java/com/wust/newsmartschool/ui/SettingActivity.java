package com.wust.newsmartschool.ui;

import android.os.Bundle;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.fragments.NewsFragment;

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
