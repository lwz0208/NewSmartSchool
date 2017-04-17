package com.wust.newsmartschool.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Li Wenzhao on 2016/12/2.
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT = 9;
    private String tabTitles[] = new String[]{"要闻", "综合", "院系", "学术", "人物", "声音", "深度", "聚焦", "媒体"};
    private LWZNewsFragment[] fragments = new LWZNewsFragment[PAGE_COUNT];

    public SimpleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        for(int i = 0; i < PAGE_COUNT; i++)
            fragments[i] = null;
    }

    @Override
    public Fragment getItem(int position) {
        if(fragments[position] == null)
            fragments[position] = LWZNewsFragment.newInstance(position);
        return fragments[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}

