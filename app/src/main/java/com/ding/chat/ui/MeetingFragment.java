package com.ding.chat.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.ding.chat.R;
import com.ding.easeui.Constant;
import com.ding.easeui.utils.CommonUtils;
import com.ding.easeui.utils.PreferenceManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeetingFragment extends Fragment {
    String TAG = "MeetingFragment_Debugs";
    private ListView lv_meetingList;
    private MyAdapter adapter;
    private ImageView activity_add_task;
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private List<Map<String, Object>> data;
    private ProgressBar load_progress_bar;
    private int CreatMeetingPower = -1;

    public static MeetingFragment newInstance(int page) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE, page);
        MeetingFragment pageFragment = new MeetingFragment();
        pageFragment.setArguments(bundle);
        return pageFragment;
    }

    public MeetingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meeting, container,
                false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lv_meetingList = (ListView) getView().findViewById(R.id.lv_meetingList);
        load_progress_bar = (ProgressBar) getView().findViewById(R.id.load_progress_bar);
        activity_add_task = (ImageView) getActivity().findViewById(R.id.iv_add_task);
        activity_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        getActivity());
                switch (CreatMeetingPower) {
                    case -1:
                        Toast.makeText(getActivity(), "未获取创建会议权限", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        // 指定下拉列表的显示数据
                        final String[] items01 = {"普通会议"};
                        builder.setTitle("请选择会议类型");
                        // 设置一个下拉的列表选择项
                        builder.setItems(items01, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getContext(),
                                        NewMeetingActivity.class);
                                intent.putExtra("meetingType", items01[which]);
                                startActivityForResult(intent, 0);
                            }
                        });
                        builder.show();
                        break;
                    case 2:
                        // 指定下拉列表的显示数据
                        final String[] items02 = {"院方会议", "普通会议"};
                        builder.setTitle("请选择会议类型");
                        // 设置一个下拉的列表选择项
                        builder.setItems(items02, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getContext(),
                                        NewMeetingActivity.class);
                                intent.putExtra("meetingType", items02[which]);
                                startActivityForResult(intent, 0);
                            }
                        });
                        builder.show();
                        break;
                }

            }
        });
        data = new ArrayList<>();
        adapter = new MyAdapter(getActivity(), data);
        lv_meetingList.setAdapter(adapter);
        //获取自己可以创建的会议类型
        getCreatMeetingList();
        //获取会议列表
        getMeetingData(mPage);
        lv_meetingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(),
                        MeetingDetailActivity.class);
                intent.putExtra("meetingID", ((Map) adapter.getItem(position)).get("meetingID").toString());
                intent.putExtra("meetingStatus", mPage + "");
                intent.putExtra("meetingtitle", ((Map) adapter.getItem(position)).get("title").toString());
                startActivityForResult(intent, 0);
            }
        });
    }


    private void getMeetingData(int page) {
        org.json.JSONObject jObject = new org.json.JSONObject();
        try {
            jObject.put("userId", PreferenceManager.getInstance().getCurrentUserId());
            jObject.put("status", page);
            jObject.put("rows", 50);
            jObject.put("page", 1);
            CommonUtils.setCommonJson(getActivity(), jObject, PreferenceManager.getInstance().getCurrentUserFlowSId());

            Log.e(TAG, jObject.toString());

            OkHttpUtils.postString().url(Constant.GETBEGINMEET_URL).content(jObject.toString())
                    .mediaType(MediaType.parse("application/json")).build()
                    .execute(new StringCallback() {

                        @Override
                        public void onError(Call arg0, Exception arg1) {
                            Log.i(TAG, "会议接口访问失败：" + arg0 + arg1);
//                            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                            load_progress_bar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onResponse(String arg0) {
                            Log.i(TAG, "会议接口访问成功：" + arg0);
                            load_progress_bar.setVisibility(View.GONE);
                            JSONObject json = JSON.parseObject(arg0);
                            data.clear();
                            if (json.getIntValue("code") == 1 && json.getJSONObject("data") != null) {
                                try {
                                    JSONObject meetingJSONObject = json.getJSONObject("data");
                                    JSONArray array = meetingJSONObject.getJSONArray("meetingInfo");
                                    for (int i = 0; i < array.size(); i++) {
                                        Map<String, Object> value = new HashMap<>();
                                        value.put("title", array.getJSONObject(i).getString("title"));
                                        value.put("startTime", array.getJSONObject(i).getString("startTime"));
                                        value.put("meetingID", array.getJSONObject(i).getIntValue("id"));
                                        value.put("type", array.getJSONObject(i).getString("type"));
                                        data.add(value);
                                    }
                                    adapter.notifyDataSetChanged();
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            } else if (json.getIntValue("code") == 1 && json.getJSONObject("data") == null) {
                                adapter.notifyDataSetChanged();
//                            Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "服务器异常", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void getCreatMeetingList() {
        org.json.JSONObject jObject = new org.json.JSONObject();
        try {
            jObject.put("userId", PreferenceManager.getInstance().getCurrentUserId());
            CommonUtils.setCommonJson(getActivity(), jObject, PreferenceManager.getInstance().getCurrentUserFlowSId());
            Log.e(TAG, jObject.toString());

            OkHttpUtils.postString().url(Constant.GETCREATMEETINGLIST_URL).content(jObject.toString())
                    .mediaType(MediaType.parse("application/json")).build()
                    .execute(new StringCallback() {

                        @Override
                        public void onError(Call arg0, Exception arg1) {
                            arg1.printStackTrace();
                        }

                        @Override
                        public void onResponse(String arg0) {
                            Log.e(TAG, arg0.toString());
                            try {
                                org.json.JSONObject data = new org.json.JSONObject(arg0);
                                if (data.getInt("code") == 1) {
                                    if (data.getJSONArray("data") != null) {
                                        CreatMeetingPower = data.getJSONArray("data").length();
                                    } else {
                                        CreatMeetingPower = -1;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    class MyAdapter extends BaseAdapter {
        private Context context;
        private List<Map<String, Object>> list;
        private LayoutInflater listContainer;

        private class ViewHolder {
            public TextView tv_meetingTitle;
            public TextView tv_meetingStartTime;
            public ImageView image_meetingflage;
        }

        public MyAdapter(Context context, List<Map<String, Object>> listItems) {
            this.context = context;
            listContainer = LayoutInflater.from(context);
            this.list = listItems;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder myViewHolder = null;
            if (convertView == null) {
                myViewHolder = new ViewHolder();
                convertView = listContainer.inflate(R.layout.meeting_item, null);
                myViewHolder.tv_meetingTitle = (TextView) convertView.findViewById(R.id.tv_meetingTitle);
                myViewHolder.tv_meetingStartTime = (TextView) convertView.findViewById(R.id.tv_meetingStartTime);
                myViewHolder.image_meetingflage = (ImageView) convertView.findViewById(R.id.image_meetingflage);
                convertView.setTag(myViewHolder);
            } else {
                myViewHolder = (ViewHolder) convertView.getTag();
            }
            if ((list.get(position).get("type")).equals("1")) {
                Glide.with(context).load(R.drawable.working_meeting_pu).into(myViewHolder.image_meetingflage);
            } else if ((list.get(position).get("type")).equals("2")) {
                Glide.with(context).load(R.drawable.working_meeting_yuan).into(myViewHolder.image_meetingflage);
            } else {
                Glide.with(context).load(R.drawable.workfragment_meeting).into(myViewHolder.image_meetingflage);
            }

            myViewHolder.tv_meetingTitle.setText((String) list.get(position).get("title"));
            myViewHolder.tv_meetingStartTime.setText((String) list.get(position).get("startTime"));

            return convertView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "MeetingFragment_onActivityResult");
        if (resultCode == getActivity().RESULT_OK) {
            load_progress_bar.setVisibility(View.VISIBLE);
            getMeetingData(mPage);
        }
    }
}
