package com.wust.newsmartschool.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.ui.AboutSchoolActivity;
import com.wust.newsmartschool.ui.ComprehensiveServiceActivity;
import com.wust.newsmartschool.ui.DepartOnlineActivity;
import com.wust.newsmartschool.ui.LibSeatActivity;
import com.wust.newsmartschool.ui.NewsListActivity;
import com.wust.newsmartschool.ui.NoticesActivity;
import com.wust.newsmartschool.ui.SchoolServiceActivity;
import com.wust.newsmartschool.ui.StudentHomeActivity;
import com.wust.newsmartschool.ui.StudentSelectActivity;

public class MainIndexFragment extends Fragment implements View.OnClickListener {
    private LinearLayout ll_school_news, ll_school_notice, ll_school_intro, ll_handing_guide;
    private LinearLayout ll_part_online, ll_stu_home, ll_stu_service, ll_xuanke_pj, ll_book_service;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mainindex_new, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        ll_school_news = (LinearLayout) getView().findViewById(R.id.ll_school_news);
        ll_school_notice = (LinearLayout) getView().findViewById(R.id.ll_school_notice);
        ll_school_intro = (LinearLayout) getView().findViewById(R.id.ll_school_intro);
        ll_handing_guide = (LinearLayout) getView().findViewById(R.id.ll_handing_guide);
        ll_part_online = (LinearLayout) getView().findViewById(R.id.ll_part_online);
        ll_stu_home = (LinearLayout) getView().findViewById(R.id.ll_stu_home);
        ll_stu_service = (LinearLayout) getView().findViewById(R.id.ll_stu_service);
        ll_xuanke_pj = (LinearLayout) getView().findViewById(R.id.ll_xuanke_pj);
        ll_book_service = (LinearLayout) getView().findViewById(R.id.ll_book_service);

        ll_school_news.setOnClickListener(this);
        ll_school_notice.setOnClickListener(this);
        ll_school_intro.setOnClickListener(this);
        ll_handing_guide.setOnClickListener(this);
        ll_part_online.setOnClickListener(this);
        ll_stu_home.setOnClickListener(this);
        ll_stu_service.setOnClickListener(this);
        ll_xuanke_pj.setOnClickListener(this);
        ll_book_service.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ll_school_news:
                intent.setClass(getActivity(), NewsListActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_school_notice:
                intent.setClass(getActivity(), NoticesActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_school_intro:
                intent.setClass(getActivity(), AboutSchoolActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_handing_guide:
                intent.setClass(getActivity(), ComprehensiveServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_part_online:
                intent.setClass(getActivity(), DepartOnlineActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_stu_home:
                intent.setClass(getActivity(), StudentHomeActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_stu_service:
                intent.setClass(getActivity(), SchoolServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_xuanke_pj:
                intent.setClass(getActivity(), StudentSelectActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_book_service:
                intent.setClass(getActivity(), LibSeatActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
