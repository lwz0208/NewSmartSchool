package com.wust.newsmartschool.adapter;

import java.util.ArrayList;
import java.util.List;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.MyJFlowEntity_IApply;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyFlowApplyListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ViewHolder vh;
    private List<MyJFlowEntity_IApply> mData = new ArrayList<MyJFlowEntity_IApply>();
    Context mContext;

    public MyFlowApplyListAdapter(Context con,
                                  List<MyJFlowEntity_IApply> itemlist) {
        mContext = con;
        mInflater = LayoutInflater.from(con);
        mData = itemlist;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_myapplyjflow, null);
            vh = new ViewHolder();
            vh.thumView = (ImageView) convertView
                    .findViewById(R.id.notice_avatar);
            vh.titleView = (TextView) convertView
                    .findViewById(R.id.notice_title);
            vh.contentView = (TextView) convertView
                    .findViewById(R.id.notice_content);
            vh.notice_time = (TextView) convertView
                    .findViewById(R.id.notice_time);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        if (mData.get(position).getWfstate() == 3) {
            vh.thumView.setImageResource(R.drawable.working_flow_done);
        } else if (mData.get(position).getWfstate() == 2) {
            vh.thumView.setImageResource(R.drawable.working_flow_ing);
        } else//最后只有一个数字，那就是5了
        {
            vh.thumView.setImageResource(R.drawable.working_flow_reject);
        }
        try {
            vh.titleView.setText(mData.get(position).getStartername());
        } catch (Exception e) {
            vh.titleView.setText("");
        }
        try {
            vh.contentView.setText(mData.get(position).getFlowname());
        } catch (Exception e) {
            vh.contentView.setText("");
        }
        try {
            vh.notice_time.setText(mData.get(position).getRdt());
        } catch (Exception e) {
            vh.notice_time.setText("");
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView thumView;
        TextView titleView;
        TextView contentView;
        TextView notice_time;
    }

}
