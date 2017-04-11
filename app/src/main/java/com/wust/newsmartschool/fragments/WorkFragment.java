package com.wust.newsmartschool.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.ui.LibSeatActivity;

public class WorkFragment extends Fragment implements OnClickListener {
    String TAG = "WorkFragment2_Degugs";
    private RelativeLayout rl_stu_home;
    private RelativeLayout rl_stu_service;
    private RelativeLayout rl_xuanke_pj;
    private RelativeLayout rl_book_service;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment2_work, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rl_stu_home = (RelativeLayout) getView().findViewById(
                R.id.rl_stu_home);
        rl_stu_service = (RelativeLayout) getView().findViewById(
                R.id.rl_stu_service);
        rl_xuanke_pj = (RelativeLayout) getView().findViewById(
                R.id.rl_xuanke_pj);
        rl_book_service = (RelativeLayout) getView().findViewById(
                R.id.rl_book_service);

        rl_stu_home.setOnClickListener(this);
        rl_stu_service.setOnClickListener(this);
        rl_xuanke_pj.setOnClickListener(this);
        rl_book_service.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rl_stu_home:
                Toast.makeText(getActivity(), "先给屏蔽了吧", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getContext(), WorkFragNoticeActivity.class));
//                if (android.os.Build.VERSION.SDK_INT > 5) {
//                    getActivity().overridePendingTransition(
//                            R.anim.slide_in_from_right, R.anim.slide_out_to_left);
//                }
                break;
            case R.id.rl_stu_service:
                Toast.makeText(getActivity(), "先给屏蔽了吧", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getContext(),
//                        WorkFragApplyActivity.class));
//                if (android.os.Build.VERSION.SDK_INT > 5) {
//                    getActivity().overridePendingTransition(
//                            R.anim.slide_in_from_right, R.anim.slide_out_to_left);
//                }
                break;
            case R.id.rl_xuanke_pj:
                Toast.makeText(getActivity(), "先给屏蔽了吧", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getContext(),
//                        MeetingActivity.class));
//                if (android.os.Build.VERSION.SDK_INT > 5) {
//                    getActivity().overridePendingTransition(
//                            R.anim.slide_in_from_right, R.anim.slide_out_to_left);
//                }
                break;
            case R.id.rl_book_service:
                intent.setClass(getActivity(), LibSeatActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }


}
