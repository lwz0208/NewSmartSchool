package com.ding.chat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ding.chat.DemoApplication;
import com.ding.chat.R;
import com.ding.chat.domain.DeptArchTree;
import com.ding.chat.domain.PartyBranchIdEntity;
import com.ding.chat.ui.MeetingActivity;
import com.ding.chat.ui.WorkFragApplyActivity;
import com.ding.chat.ui.WorkFragNoticeActivity;
import com.ding.chat.ui.WorkFragTaskActivity;
import com.ding.chat.ui.WorkFragWorkFlowActivity;
import com.ding.easeui.Constant;
import com.ding.easeui.utils.CommonUtils;
import com.ding.easeui.utils.PreferenceManager;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MediaType;

public class WorkFragment2 extends Fragment implements OnClickListener {
    String TAG = "WorkFragment2_Degugs";
    private RelativeLayout rl_work_notice;
    private RelativeLayout rl_work_meetting;
    private RelativeLayout rl_work_workline;
    private RelativeLayout rl_work_duty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment2_work, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rl_work_notice = (RelativeLayout) getView().findViewById(
                R.id.rl_work_notice);
        rl_work_meetting = (RelativeLayout) getView().findViewById(
                R.id.rl_work_meetting);
        rl_work_workline = (RelativeLayout) getView().findViewById(
                R.id.rl_work_workline);
        rl_work_duty = (RelativeLayout) getView().findViewById(
                R.id.rl_work_duty);
        rl_work_duty.setOnClickListener(this);
        rl_work_notice.setOnClickListener(this);
        rl_work_meetting.setOnClickListener(this);
        rl_work_workline.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_work_notice:
                startActivity(new Intent(getContext(), WorkFragNoticeActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    getActivity().overridePendingTransition(
                            R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }
                break;
            case R.id.rl_work_workline:
                startActivity(new Intent(getContext(),
                        WorkFragApplyActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    getActivity().overridePendingTransition(
                            R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }
                break;
            case R.id.rl_work_meetting:
                startActivity(new Intent(getContext(),
                        MeetingActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    getActivity().overridePendingTransition(
                            R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }
                break;
            case R.id.rl_work_duty:
                startActivity(new Intent(getContext(), WorkFragTaskActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    getActivity().overridePendingTransition(
                            R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }
                break;

            default:
                break;
        }
    }


}
