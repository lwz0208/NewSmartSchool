package com.ding.chat.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.platform.comapi.map.C;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.ding.chat.R;
import com.ding.chat.adapter.MainIndexMsgAdapter;
import com.ding.chat.domain.CommonNews;
import com.ding.chat.domain.FlowWorkMenuList;
import com.ding.chat.domain.NewsPicture;
import com.ding.chat.domain.UndoData;
import com.ding.chat.ui.JFlowDetailActivity;
import com.ding.chat.ui.MainActivity;
import com.ding.chat.ui.MeetingActivity;
import com.ding.chat.ui.MeetingDetailActivity;
import com.ding.chat.ui.NewsDetailActivity;
import com.ding.chat.ui.NoticeDetailActivity_new;
import com.ding.chat.ui.SettingActivity;
import com.ding.chat.ui.TaskDetailActivity;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

public class MainIndexFragment extends Fragment implements OnClickListener {
    String TAG = "MainIndexFragment_Debugs";
    private ListView mainfrag_deal;
    private View headerView;
    private ConvenientBanner convenientBanner;
    private List<NewsPicture> NewsPictureList;
    // 全局广播监听者
    MyBroadcastReceiverForData receiver;
    //下面List的数据源
    List<UndoData> undoDatas;
    private List<UndoData> tempUndoDatas;
    MainIndexMsgAdapter mainIndexMsgAdapter;
    //全局动态变量
    FlowWorkMenuList.RetBean WorkMenuList;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    convenientBanner.setPages(new CBViewHolderCreator() {
                        @Override
                        public Object createHolder() {
                            return new LocalImageHolderView();
                        }
                    }, NewsPictureList) //设置需要切换的View
                            .setPointViewVisible(true)    //设置指示器是否可见
                            //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                            .setPageIndicator(new int[]{R.drawable.small_circle_white_alpha, R.drawable.small_circle_white})
                            //设置指示器位置（左、中、右）
                            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                            .setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    startActivity(new Intent(getActivity(), NewsDetailActivity.class).putExtra("url", NewsPictureList.get(position).getImg()));
                                    if (android.os.Build.VERSION.SDK_INT > 5) {
                                        getActivity().overridePendingTransition(R.anim.slide_in_from_right,
                                                R.anim.slide_out_to_left);
                                    }
                                }
                            })
                            .startTurning(3000)     //设置自动切换（同时设置了切换时间间隔）
                            .setManualPageable(true);  //设置手动影响（设置了该项无法手动切换）
                    break;

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mainindex, container,
                false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        undoDatas = new ArrayList<>();
        WorkMenuList = new FlowWorkMenuList.RetBean();
        mainfrag_deal = (ListView) getView().findViewById(R.id.mainfrag_deal);
        LayoutInflater infla = getActivity().getLayoutInflater();
        headerView = infla.inflate(R.layout.headerformainfragment, null);
        mainfrag_deal.addHeaderView(headerView);
        convenientBanner = (ConvenientBanner) headerView.findViewById(R.id.convenientBanner);
        //动态设置轮播图高宽
        ViewGroup.LayoutParams params = convenientBanner.getLayoutParams();
        params.height = (int) (MainActivity.getScreenWidth(getContext()) * 0.56);
        convenientBanner.setLayoutParams(params);
        headerView.findViewById(R.id.ll_metting).setOnClickListener(this);
        headerView.findViewById(R.id.ll_thingsshenqing).setOnClickListener(this);
        headerView.findViewById(R.id.ll_combad).setOnClickListener(this);
        headerView.findViewById(R.id.ll_checkfee).setOnClickListener(this);
        headerView.findViewById(R.id.ll_chufangcount).setOnClickListener(this);
        headerView.findViewById(R.id.ll_feebaoxiao).setOnClickListener(this);

        mainIndexMsgAdapter = new MainIndexMsgAdapter(getActivity(), undoDatas);
        mainfrag_deal.setAdapter(mainIndexMsgAdapter);
        getNewsPicture();
        initUndoData();
        getWorkFlowList();
        // 注册广播
        IntentFilter filter_dynamic = new IntentFilter();
        receiver = new MyBroadcastReceiverForData();
        filter_dynamic.addAction(Constant.BROAD_JPUSHDATA);
        getActivity().registerReceiver(receiver, filter_dynamic);

        mainfrag_deal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UndoData clickItem = (UndoData) mainIndexMsgAdapter.getItem(position - 1);
                switch (clickItem.getType()) {
                    case "inform":
                        startActivity(new Intent(getActivity(),
                                NoticeDetailActivity_new.class).putExtra(
                                "KEY_CLICK_NOTICE", clickItem.getId() + "").putExtra("ItemFrom", "WorkFragNoticeActivity"));
                        undoDatas.remove(position - 1);
                        mainIndexMsgAdapter.notifyDataSetChanged();
                        break;
                    case "task":
                        startActivity(new Intent(getActivity(),
                                TaskDetailActivity.class).putExtra(
                                "taskID", clickItem.getId() + ""));
                        break;
                    case "jflow":
                        startActivity(new Intent(getActivity(),
                                WorkFragWorkFlowActivity.class));
                        break;
                    case "meeting":
                        Intent intent = new Intent(getActivity(),
                                MeetingDetailActivity.class);
                        intent.putExtra("meetingID", clickItem.getId() + "");
                        intent.putExtra("meetingtitle", clickItem.getTitle());
                        //从这儿点进去的肯定是0未开始的
                        intent.putExtra("meetingStatus", 0 + "");
                        startActivityForResult(intent, 0);
                        break;
                    case "task_modify":
                        String content_task_modify = clickItem.getContent();
                        String[] content_split_task_modify = content_task_modify.split(",");
                        Intent i = new Intent(getActivity(), TaskDetailActivity.class).putExtra("taskID", content_split_task_modify[1]);
                        getActivity().startActivity(i);
                        break;
                    case "comment":
                        String content_comment = clickItem.getContent();
                        String[] content_split_comment = content_comment.split(",");
                        Intent i1;
                        switch (content_split_comment[0]) {
                            case "task":
                                i1 = new Intent(getActivity(), TaskDetailActivity.class).putExtra("taskID", content_split_comment[1]);
                                break;
                            case "inform":
                                i1 = new Intent(getContext(), NoticeDetailActivity_new.class).putExtra(
                                        "KEY_CLICK_NOTICE", content_split_comment[1]).putExtra("ItemFrom", "WorkFragNoticeActivity");
                                break;
                            default:
                                i1 = new Intent(getActivity(), MainActivity.class);
                                break;
                        }
                        undoDatas.remove(position - 1);
                        mainIndexMsgAdapter.notifyDataSetChanged();
                        getActivity().startActivity(i1);
                        break;
                }

            }
        });
    }

    private void getWorkFlowList() {
        try {
            org.json.JSONObject getWorkListJson = new org.json.JSONObject();
            getWorkListJson.put("domain_id", "56fb8aaa17f52a1de0b9677c");
            CommonUtils.setCommonJson(getActivity(), getWorkListJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
            Log.e(TAG, getWorkListJson.toString());
            OkHttpUtils.postString().url(Constant.FLOWAPPLYLIST_URL).content(getWorkListJson.toString())
                    .mediaType(MediaType.parse("application/json")).build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            Log.e(TAG, "call=" + call + "//Exception=" + e.toString());
                        }

                        @Override
                        public void onResponse(String s) {
                            Log.e(TAG, s);
                            try {
                                org.json.JSONObject getdata = new org.json.JSONObject(s);
                                if (getdata.getInt("code") == 200) {
                                    FlowWorkMenuList menuList = new Gson().fromJson(s, FlowWorkMenuList.class);
                                    WorkMenuList = menuList.getRet();
                                    //不想用gridview而给自己挖的坑，就这么填充吧。。。我都不想碰这个代码了。
                                    if (menuList.getRet().getTotal_num() > 0) {
                                        for (int i = 1; i <= menuList.getRet().getTotal_num(); i++) {
                                            switch (i) {
                                                case 1:
                                                    ((TextView) headerView.findViewById(R.id.tv_01)).setText(menuList.getRet().getWorkflow_def_list().get(0).getName());
                                                    break;
                                                case 2:
                                                    ((TextView) headerView.findViewById(R.id.tv_02)).setText(menuList.getRet().getWorkflow_def_list().get(1).getName());
                                                    break;
                                                case 3:
                                                    ((TextView) headerView.findViewById(R.id.tv_03)).setText(menuList.getRet().getWorkflow_def_list().get(2).getName());
                                                    break;
                                                case 4:
                                                    ((TextView) headerView.findViewById(R.id.tv_04)).setText(menuList.getRet().getWorkflow_def_list().get(3).getName());
                                                    break;
                                                case 5:
                                                    ((TextView) headerView.findViewById(R.id.tv_05)).setText(menuList.getRet().getWorkflow_def_list().get(4).getName());
                                                    break;
                                                case 6:
                                                    ((TextView) headerView.findViewById(R.id.tv_06)).setText(menuList.getRet().getWorkflow_def_list().get(5).getName());
                                                    break;
                                            }
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, e.toString());

        }
    }


    private class MyBroadcastReceiverForData extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.BROAD_JPUSHDATA)) {
                try {
//                    Log.e(TAG, intent.getStringExtra(Constant.BROAD_JPUSHDATA));
                    UndoData undoData = new Gson().fromJson(intent.getStringExtra(Constant.BROAD_JPUSHDATA), UndoData.class);
                    undoDatas.add(0, undoData);
                    mainIndexMsgAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_metting:
                if (WorkMenuList.getTotal_num() > 0) {
                    startActivity(new Intent(getContext(),
                            WorkFragApplyActivity.class).putExtra("webid_create", WorkMenuList.getWorkflow_def_list().get(0).getId()));
                    if (android.os.Build.VERSION.SDK_INT > 5) {
                        getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                    }
                } else {
                    Toast.makeText(getActivity(), "暂未获取", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_thingsshenqing:
                if (WorkMenuList.getTotal_num() > 1) {
                    startActivity(new Intent(getContext(),
                            WorkFragApplyActivity.class).putExtra("webid_create", WorkMenuList.getWorkflow_def_list().get(1).getId()));
                    if (android.os.Build.VERSION.SDK_INT > 5) {
                        getActivity().overridePendingTransition(
                                R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                    }
                } else {
                    Toast.makeText(getActivity(), "暂未获取", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_combad:
                if (WorkMenuList.getTotal_num() > 2) {
                    startActivity(new Intent(getContext(), WorkFragApplyActivity.class).putExtra("webid_create", WorkMenuList.getWorkflow_def_list().get(2).getId()));
                    if (android.os.Build.VERSION.SDK_INT > 5) {
                        getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                    }
                } else {
                    Toast.makeText(getActivity(), "暂未获取", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_checkfee:
                if (WorkMenuList.getTotal_num() > 3) {
                    startActivity(new Intent(getContext(), WorkFragApplyActivity.class).putExtra("webid_create", WorkMenuList.getWorkflow_def_list().get(3).getId()));
                    if (android.os.Build.VERSION.SDK_INT > 5) {
                        getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                    }
                } else {
                    Toast.makeText(getActivity(), "暂未获取", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_chufangcount:
                if (WorkMenuList.getTotal_num() > 4) {
                    startActivity(new Intent(getContext(), WorkFragApplyActivity.class).putExtra("webid_create", WorkMenuList.getWorkflow_def_list().get(4).getId()));
                    if (android.os.Build.VERSION.SDK_INT > 5) {
                        getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                    }
                } else {
                    Toast.makeText(getActivity(), "暂未获取", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_feebaoxiao:
                if (WorkMenuList.getTotal_num() > 5) {
                    startActivity(new Intent(getContext(), WorkFragApplyActivity.class).putExtra("webid_create", WorkMenuList.getWorkflow_def_list().get(5).getId()));
                    if (android.os.Build.VERSION.SDK_INT > 5) {
                        getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                    }
                } else {
                    Toast.makeText(getActivity(), "暂未获取", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public class LocalImageHolderView implements Holder<NewsPicture> {
        private View view;

        @Override
        public View createView(Context context) {
            view = LayoutInflater.from(context).inflate(R.layout.banner_item, null, false);
            return view;
        }

        @Override
        public void UpdateUI(Context context, int position, NewsPicture data) {
            ((TextView) view.findViewById(R.id.tv_title)).setText(data.getTitle());
            Glide.with(getActivity()).load(data.getUrl()).into(((ImageView) view.findViewById(R.id.iv_title)));
        }
    }

    private void getNewsPicture() {
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject();
            CommonUtils.setCommonJson(getActivity(), jsonObject, PreferenceManager.getInstance().getCurrentUserFlowSId());
            OkHttpUtils.postString().url(Constant.GETNEWSINDEX_URL).content(jsonObject.toString())
                    .mediaType(MediaType.parse("application/json")).build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            Log.i("resultError", call.toString() + "--" + e);
                        }

                        @Override
                        public void onResponse(String s) {
                            Log.i("resultRight", s);
                            JSONObject jsonObject = JSON.parseObject(s);
                            if (jsonObject.getIntValue("code") == 1) {
                                NewsPictureList = JSON.parseArray(jsonObject.getJSONArray("data").toJSONString(), NewsPicture.class);
                                handler.sendEmptyMessage(100);
                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initUndoData() {
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject();
            jsonObject.put("userId", Integer.parseInt(PreferenceManager.getInstance().getCurrentUserId()));
            CommonUtils.setCommonJson(getActivity(), jsonObject, PreferenceManager.getInstance().getCurrentUserFlowSId());
            OkHttpUtils.postString().url(Constant.UNDOTHINGS_URL).content(jsonObject.toString())
                    .mediaType(MediaType.parse("application/json")).build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            Log.i("resultError", call.toString() + "--" + e);
                        }

                        @Override
                        public void onResponse(String s) {
                            Log.i("resultRight", s);
                            JSONObject jsonObject = JSON.parseObject(s);
                            if (jsonObject.getIntValue("code") == 1) {
                                tempUndoDatas = JSON.parseArray(jsonObject.getJSONArray("data").toJSONString(), UndoData.class);
                                undoDatas.clear();
                                undoDatas.addAll(tempUndoDatas);
                                mainIndexMsgAdapter.notifyDataSetChanged();

                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        Log.e(TAG, hidden + "");
        //如果出现在眼前。。。
        if (hidden == false) {
            initUndoData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }
}
