package com.wust.newsmartschool.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wust.newsmartschool.DemoApplication;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.BaseAppAdapter;
import com.wust.newsmartschool.domain.FirstPageUpdateNum;
import com.wust.newsmartschool.domain.FirstPage_entity;
import com.wust.newsmartschool.ui.MeetingActivity;
import com.wust.newsmartschool.ui.PublishTaskActivity;
import com.wust.newsmartschool.ui.SettingActivity;
import com.wust.newsmartschool.ui.UserInfoActivity;
import com.wust.newsmartschool.ui.WorkFragApplyActivity;
import com.wust.newsmartschool.ui.WorkFragNoticeActivity;
import com.wust.newsmartschool.ui.WorkFragTaskActivity;
import com.wust.newsmartschool.utils.MyGridView;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.CommonUtils;
import com.wust.easeui.utils.PreferenceManager;
import com.wust.easeui.widget.GlideCircleImage;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

public class MainIndexFragment extends Fragment implements View.OnClickListener {
    private ImageView head_img;
    private List<FirstPage_entity> firstPage_entity;
    private List<FirstPageUpdateNum> firstPageUpdateNa;
    private ImageView iv_baseapp_arrow, iv_usualapply_arrow;
    private ImageView iv_notice, iv_mytask, iv_publishtask, iv_unapply, iv_myapply;
    private ImageView fpimage_notice, fpimage_mytask, fpimage_publishtask, fpimage_waitdeal, fpimage_myapprove;
    private TextView tv_notice, tv_mytask, tv_publishtask, tv_unapply, tv_myapply;
    private TextView tv_noticeNum, tv_mytaskNum, tv_publishtaskNum, tv_unapplyNum, tv_myapplyNum;
    private TextView hospital_notice_subcontent, my_task_subcontent, publish_task_subcontent, unapply_notice_subcontent, my_apply_notice_subcontent;
    private MyGridView baseappGV, usualapplyGV;
    private LinearLayout baseapp_title, usualapply_title;
    private LinearLayout ll_hospital_notice, ll_my_task, ll_publish_task, ll_unapply, ll_my_apply;
    BaseAppAdapter baseAppAdapter, usualapplyAdapter;
    List<FirstPage_entity.SubMenuBean> orignal_baseAppEntities, orignal_usualapplyEntities;
    List<FirstPage_entity.SubMenuBean> toadapter_baseAppEntities, toadapter_usualapplyEntities;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mainindex_new, container,
                false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        if (DemoApplication.getInstance().mCache.getAsObject("first_data0") == null) {
            getLocalData();
            Log.i("fitstPage", "获取了菜单接口onActivityCreated");
        } else {
            setMenuData();
        }
        getUpdateData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        try {
            if (hidden) {
                if (DemoApplication.getInstance().mCache.getAsObject("first_data0") == null) {
                    getLocalData();
                    Log.i("fitstPage", "获取了菜单接口onHiddenChanged");
                } else {
                    setMenuData();
                }
                getUpdateData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //模拟从网络接口请求到了参数，此实体并不是最终的参数格式
    private void getUpdateData() {
        JSONObject numJson = new JSONObject();
        try {
            numJson.put("event", "inform_unread,task_by_me_unfinish,task_by_other_unfinish,jflow_for_my_approval,jflow_my_apply_unfinish");
            numJson.put("userId", PreferenceManager.getInstance().getCurrentUserId());
            CommonUtils.setCommonJson(getActivity(), numJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpUtils.postString().url(Constant.GETFIRSTPAGEUNREADNUM_URL).content(numJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.i("fitstPage", "获取更新模块接口访问失败---" + call + e);
                    }

                    @Override
                    public void onResponse(String s) {
                        Log.i("fitstPage", "获取更新模块接口访问成功---" + s);
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(s);
                            if (jObject.getInt("code") == 1) {
                                firstPageUpdateNa.clear();
                                for (int i = 0; i < jObject.getJSONArray("data").length(); i++) {
                                    FirstPageUpdateNum tempFirstPage_entity = new Gson().fromJson(jObject.getJSONArray("data").get(i).toString(), FirstPageUpdateNum.class);
                                    firstPageUpdateNa.add(i, tempFirstPage_entity);
                                    switch (firstPageUpdateNa.get(i).getEvent()) {
                                        case "inform_unread":
                                            setUpdateMsg(iv_notice, tv_noticeNum, firstPageUpdateNa.get(i).getData());
                                            break;
                                        case "task_by_me_unfinish":
                                            setUpdateMsg(iv_mytask, tv_mytaskNum, firstPageUpdateNa.get(i).getData());
                                            break;
                                        case "task_by_other_unfinish":
                                            setUpdateMsg(iv_publishtask, tv_publishtaskNum, firstPageUpdateNa.get(i).getData());
                                            break;
                                        case "jflow_for_my_approval":
                                            setUpdateMsg(iv_unapply, tv_unapplyNum, firstPageUpdateNa.get(i).getData());
                                            break;
                                        case "jflow_my_apply_unfinish":
                                            setUpdateMsg(iv_myapply, tv_myapplyNum, firstPageUpdateNa.get(i).getData());
                                            break;
                                    }
                                }

                            } else {
//                                Toast.makeText(getActivity(), "获取信息异常", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void setMenuData() {
        for (int i = 0; i < 3; i++) {
            firstPage_entity.add((FirstPage_entity) DemoApplication.getInstance().mCache.getAsObject("first_data" + i));
        }
        for (int i = 0; i < firstPage_entity.get(0).getSubMenu().size(); i++) {
            switch (firstPage_entity.get(0).getSubMenu().get(i).getEvent()) {
                case "inform_unread":
                    tv_notice.setText(firstPage_entity.get(0).getSubMenu().get(i).getName());
//                    hospital_notice_subcontent.setText(firstPage_entity.get(0).getSubMenu().get(i).getAlias());
                    Glide.with(getActivity())
                            .load(Constant.FP_IMAGEURL + firstPage_entity.get(0).getSubMenu().get(i).getId() + ".png").placeholder(R.drawable.need_approval)
                            .into(fpimage_notice);
                    break;
                case "task_by_me_unfinish":
                    tv_mytask.setText(firstPage_entity.get(0).getSubMenu().get(i).getName());
                    Glide.with(getActivity())
                            .load(Constant.FP_IMAGEURL + firstPage_entity.get(0).getSubMenu().get(i).getId() + ".png").placeholder(R.drawable.need_approval)
                            .into(fpimage_mytask);
                    break;
                case "task_by_other_unfinish":
                    tv_publishtask.setText(firstPage_entity.get(0).getSubMenu().get(i).getName());
                    Glide.with(getActivity())
                            .load(Constant.FP_IMAGEURL + firstPage_entity.get(0).getSubMenu().get(i).getId() + ".png").placeholder(R.drawable.need_approval)
                            .into(fpimage_publishtask);
                    break;
                case "jflow_for_my_approval":
                    tv_unapply.setText(firstPage_entity.get(0).getSubMenu().get(i).getName());
                    Glide.with(getActivity())
                            .load(Constant.FP_IMAGEURL + firstPage_entity.get(0).getSubMenu().get(i).getId() + ".png").placeholder(R.drawable.need_approval)
                            .into(fpimage_waitdeal);
                    break;
                case "jflow_my_apply_unfinish":
                    tv_myapply.setText(firstPage_entity.get(0).getSubMenu().get(i).getName());
                    Glide.with(getActivity())
                            .load(Constant.FP_IMAGEURL + firstPage_entity.get(0).getSubMenu().get(i).getId() + ".png").placeholder(R.drawable.need_approval)
                            .into(fpimage_myapprove);
                    break;
            }
        }

        if (orignal_baseAppEntities.size() == 0) {
            //获取基础应用的数据
            for (int i = 0; i < firstPage_entity.get(1).getSubMenu().size(); i++) {
                orignal_baseAppEntities.add(firstPage_entity.get(1).getSubMenu().get(i));
            }
        }
        if (orignal_usualapplyEntities.size() == 0) {
            //获取常用申请的数据
            for (int i = 0; i < firstPage_entity.get(2).getSubMenu().size(); i++) {
                orignal_usualapplyEntities.add(firstPage_entity.get(2).getSubMenu().get(i));
            }
        }

        /**
         * 网络请求到的数据源（orignal_baseAppEntities）不要改变，只改变进入adapter的数据（toadapter_baseAppEntities）。
         *
         * 先判断一下，是否取前四个
         **/

        toadapter_baseAppEntities.clear();
        toadapter_usualapplyEntities.clear();

        if (orignal_baseAppEntities.size() >= 4) {
            //如果大于或者等于4，则只取前四个。
            for (int i = 0; i < 4; i++) {
                toadapter_baseAppEntities.add(orignal_baseAppEntities.get(i));
            }
        } else {
            //否则就取全部
            toadapter_baseAppEntities.addAll(orignal_baseAppEntities);
        }

        if (orignal_usualapplyEntities.size() >= 4) {
            //如果大于或者等于4，则只取前四个。
            for (int i = 0; i < 4; i++) {
                toadapter_usualapplyEntities.add(orignal_usualapplyEntities.get(i));
            }
        } else {
            //否则就取全部
            toadapter_usualapplyEntities.addAll(orignal_usualapplyEntities);
        }

        baseAppAdapter.notifyDataSetChanged();
        usualapplyAdapter.notifyDataSetChanged();

    }

    private void getLocalData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("menuIds", "1,2,3");
            CommonUtils.setCommonJson(getActivity(), jsonObject, PreferenceManager.getInstance().getCurrentUserFlowSId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils.postString().url(Constant.GETFIRSTPAGELIST_URL).content(jsonObject.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.i("fitstPage_getLocalData", "获取菜单接口访问失败---" + call + e);
                    }

                    @Override
                    public void onResponse(String s) {
                        Log.i("fitstPage_getLocalData", "获取菜单接口访问成功---" + s);
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(s);
                            if (jObject.getInt("code") == 1) {
                                if (firstPage_entity.size() == 0) {
                                    for (int i = 0; i < jObject.getJSONArray("data").length(); i++) {
                                        FirstPage_entity tempFirstPage_entity = new Gson().fromJson(jObject.getJSONArray("data").get(i).toString(), FirstPage_entity.class);
                                        firstPage_entity.add(i, tempFirstPage_entity);
                                        DemoApplication.getInstance().mCache.put("first_data" + i, tempFirstPage_entity);
                                    }
                                }
                                setMenuData();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initView() {
        orignal_baseAppEntities = new ArrayList<>();
        orignal_usualapplyEntities = new ArrayList<>();
        toadapter_baseAppEntities = new ArrayList<>();
        toadapter_usualapplyEntities = new ArrayList<>();
        firstPage_entity = new ArrayList<>();
        firstPageUpdateNa = new ArrayList<>();

        head_img = (ImageView) getView().findViewById(R.id.head_img);
        iv_notice = (ImageView) getView().findViewById(R.id.iv_notice);
        iv_mytask = (ImageView) getView().findViewById(R.id.iv_mytask);
        iv_publishtask = (ImageView) getView().findViewById(R.id.iv_publishtask);
        iv_unapply = (ImageView) getView().findViewById(R.id.iv_unapply);
        iv_myapply = (ImageView) getView().findViewById(R.id.iv_myapply);

        fpimage_notice = (ImageView) getView().findViewById(R.id.fpimage_notice);
        fpimage_mytask = (ImageView) getView().findViewById(R.id.fpimage_mytask);
        fpimage_publishtask = (ImageView) getView().findViewById(R.id.fpimage_publishtask);
        fpimage_waitdeal = (ImageView) getView().findViewById(R.id.fpimage_waitdeal);
        fpimage_myapprove = (ImageView) getView().findViewById(R.id.fpimage_myapprove);

        iv_baseapp_arrow = (ImageView) getView().findViewById(R.id.iv_baseapp_arrow);
        iv_usualapply_arrow = (ImageView) getView().findViewById(R.id.iv_usualapply_arrow);
        baseappGV = (MyGridView) getView().findViewById(R.id.baseappGV);
        usualapplyGV = (MyGridView) getView().findViewById(R.id.usualapplyGV);

        tv_notice = (TextView) getView().findViewById(R.id.tv_notice);
        tv_mytask = (TextView) getView().findViewById(R.id.tv_mytask);
        tv_publishtask = (TextView) getView().findViewById(R.id.tv_publishtask);
        tv_unapply = (TextView) getView().findViewById(R.id.tv_unapply);
        tv_myapply = (TextView) getView().findViewById(R.id.tv_myapply);
        tv_noticeNum = (TextView) getView().findViewById(R.id.tv_noticeNum);
        tv_mytaskNum = (TextView) getView().findViewById(R.id.tv_mytaskNum);
        tv_publishtaskNum = (TextView) getView().findViewById(R.id.tv_publishtaskNum);
        tv_unapplyNum = (TextView) getView().findViewById(R.id.tv_unapplyNum);
        tv_myapplyNum = (TextView) getView().findViewById(R.id.tv_myapplyNum);

        //与我相关副标题控件
        hospital_notice_subcontent = (TextView) getView().findViewById(R.id.hospital_notice_subcontent);
        my_task_subcontent = (TextView) getView().findViewById(R.id.my_task_subcontent);
        publish_task_subcontent = (TextView) getView().findViewById(R.id.publish_task_subcontent);
        unapply_notice_subcontent = (TextView) getView().findViewById(R.id.unapply_notice_subcontent);
        my_apply_notice_subcontent = (TextView) getView().findViewById(R.id.my_apply_notice_subcontent);

        baseappGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (firstPage_entity.get(1).getSubMenu().get(position).getEvent()) {
                    case "wh5yy_news":
                        intent.setClass(getActivity(), SettingActivity.class);
                        startActivity(intent);
                        break;
                    case "task":
                        intent.setClass(getActivity(), WorkFragTaskActivity.class);
                        startActivity(intent);
                        break;
                    case "meeting":
                        intent.setClass(getActivity(), MeetingActivity.class);
                        startActivity(intent);
                        break;
                    case "inform":
                        intent.setClass(getActivity(), WorkFragNoticeActivity.class);
                        startActivity(intent);
                        break;
                    case "salary":
                        Toast.makeText(getActivity(), "开发中", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        usualapplyGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    startActivity(new Intent(getContext(), WorkFragApplyActivity.class).putExtra("webid_create", firstPage_entity.get(2).getSubMenu().get(position).getUrl()));
                    if (android.os.Build.VERSION.SDK_INT > 5) {
                        getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        baseapp_title = (LinearLayout) getView().findViewById(R.id.baseapp_title);
        usualapply_title = (LinearLayout) getView().findViewById(R.id.usualapply_title);
        ll_hospital_notice = (LinearLayout) getView().findViewById(R.id.ll_hospital_notice);
        ll_my_task = (LinearLayout) getView().findViewById(R.id.ll_my_task);
        ll_publish_task = (LinearLayout) getView().findViewById(R.id.ll_publish_task);
        ll_unapply = (LinearLayout) getView().findViewById(R.id.ll_unapply);
        ll_my_apply = (LinearLayout) getView().findViewById(R.id.ll_my_apply);

        baseapp_title.setOnClickListener(this);
        usualapply_title.setOnClickListener(this);
        ll_hospital_notice.setOnClickListener(this);
        ll_my_task.setOnClickListener(this);
        ll_publish_task.setOnClickListener(this);
        ll_unapply.setOnClickListener(this);
        ll_my_apply.setOnClickListener(this);
        head_img.setOnClickListener(this);

        baseAppAdapter = new BaseAppAdapter(getActivity(), toadapter_baseAppEntities);
        usualapplyAdapter = new BaseAppAdapter(getActivity(), toadapter_usualapplyEntities);

        baseappGV.setAdapter(baseAppAdapter);
        usualapplyGV.setAdapter(usualapplyAdapter);

        Glide.with(getActivity())
                .load(Constant.GETHEADIMAG_URL
                        + PreferenceManager.getInstance().getCurrentUserId()
                        + ".png").transform(new GlideCircleImage(getActivity())).placeholder(R.drawable.ease_default_avatar)
                .into(head_img);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.baseapp_title:
                //判断当前显示的个数来决定是否是显示多个还是只显示四个。
                if (toadapter_baseAppEntities.size() <= 4) {
                    toadapter_baseAppEntities.clear();
                    //取全部显示
                    toadapter_baseAppEntities.addAll(orignal_baseAppEntities);
                    iv_baseapp_arrow.setImageResource(R.drawable.up_arrow);
                } else if (toadapter_baseAppEntities.size() > 4) {
                    toadapter_baseAppEntities.clear();
                    if (orignal_baseAppEntities.size() > 4) {
                        //本来显示的是大于或者等于4，则只取前四个。
                        for (int i = 0; i < 4; i++) {
                            toadapter_baseAppEntities.add(orignal_baseAppEntities.get(i));
                        }
                    }
                    iv_baseapp_arrow.setImageResource(R.drawable.down_arrow);
                }
                baseAppAdapter.notifyDataSetChanged();
                break;
            case R.id.usualapply_title:
                if (toadapter_usualapplyEntities.size() <= 4) {
                    toadapter_usualapplyEntities.clear();
                    //取全部显示
                    toadapter_usualapplyEntities.addAll(orignal_usualapplyEntities);
                    iv_usualapply_arrow.setImageResource(R.drawable.up_arrow);

                } else if (toadapter_usualapplyEntities.size() > 4) {
                    toadapter_usualapplyEntities.clear();
                    if (orignal_usualapplyEntities.size() > 4) {
                        //本来显示的是大于或者等于4，则只取前四个。
                        for (int i = 0; i < 4; i++) {
                            toadapter_usualapplyEntities.add(orignal_usualapplyEntities.get(i));
                        }
                    }
                    iv_usualapply_arrow.setImageResource(R.drawable.down_arrow);
                }
                usualapplyAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_hospital_notice:
                startActivity(new Intent(getContext(), WorkFragNoticeActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    getActivity().overridePendingTransition(
                            R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }
                break;
            case R.id.ll_my_task:
                startActivity(new Intent(getContext(), WorkFragTaskActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    getActivity().overridePendingTransition(
                            R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }
                break;
            case R.id.ll_publish_task:
                startActivity(new Intent(getContext(), PublishTaskActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    getActivity().overridePendingTransition(
                            R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }
                break;
            case R.id.ll_unapply:
                startActivity(new Intent(getContext(), WorkFragApplyActivity.class).putExtra("webid_first_page", Constant.FIRSTPAGE_WAITDEAL_URL));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }
                break;
            case R.id.ll_my_apply:
                startActivity(new Intent(getContext(), WorkFragApplyActivity.class).putExtra("webid_first_page", Constant.FIRSTPAGE_MYAPPROVE_URL));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }
                break;
            case R.id.head_img:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    getActivity().overridePendingTransition(
                            R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);
                }
                break;
        }
    }

    private void setUpdateMsg(ImageView imageView, TextView textView, int data) {
        if (data == 0) {
            textView.setText("(0)");
            textView.setTextColor(getResources().getColor(R.color.black));
            imageView.setVisibility(View.INVISIBLE);
        } else {
            imageView.setVisibility(View.VISIBLE);
            textView.setTextColor(getResources().getColor(R.color.white));
            switch (String.valueOf(data).length()) {
                case 1:
                    textView.setText(data + "");
                    imageView.setImageResource(R.drawable.unread_msg_1);
                    break;
                case 2:
                    textView.setText(data + "");
                    imageView.setImageResource(R.drawable.unread_msg_2);
                    break;
                case 3:
                    textView.setText(data + "");
                    imageView.setImageResource(R.drawable.unread_msg_3);
                    break;
                case 4:
                    textView.setText("999+");
                    imageView.setImageResource(R.drawable.unread_msg_4);
                    break;
            }
        }

    }
}
