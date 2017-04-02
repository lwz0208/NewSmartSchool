package com.ding.chat.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ding.chat.R;
import com.ding.chat.ui.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class WorkFragment extends Fragment {
    private GridView workfragment_gridView;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    private int[] icon = {R.drawable.common_items, R.drawable.common_items,
            R.drawable.common_items, R.drawable.common_items,
            R.drawable.common_items, R.drawable.common_items,
            R.drawable.common_items, R.drawable.common_items,};
    private String[] iconName = {"公告", "审批", "邮件", "云盘", "日志", "签到", "待办",
            "更多"};
    private ImageView upbg_ImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_work, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        workfragment_gridView = (GridView) getView().findViewById(
                R.id.workfragment_gridview);
        upbg_ImageView = (ImageView) getView().findViewById(
                R.id.workfragment_upbg);
        LayoutParams params = upbg_ImageView.getLayoutParams();
        params.height = (int) (MainActivity.getScreenWidth(getContext()) * 0.56);
        upbg_ImageView.setLayoutParams(params);
        data_list = new ArrayList<Map<String, Object>>();
        getData();
        String[] from = {"image", "text"};
        int[] to = {R.id.image, R.id.text};
        sim_adapter = new SimpleAdapter(getContext(), data_list,
                R.layout.item_workfragment, from, to);
        workfragment_gridView.setAdapter(sim_adapter);
        workfragment_gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
//				if (position == 1)
//				{
//					startActivity(new Intent(getContext(),
//						WorkItemsActivity.class));
//				}
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    getActivity().overridePendingTransition(
                            R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);
                }
                Toast.makeText(getContext(), "只有审批可点击", Toast.LENGTH_SHORT)
                        .show();

            }
        });
    }

    public void getData() {
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }

    }

}
