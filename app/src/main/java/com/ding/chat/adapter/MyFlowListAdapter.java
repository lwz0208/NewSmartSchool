package com.ding.chat.adapter;

import java.util.ArrayList;
import java.util.List;

import com.ding.chat.R;
import com.ding.chat.domain.MyJFlowEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyFlowListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ViewHolder vh = new ViewHolder();
    private List<MyJFlowEntity> mData = new ArrayList<MyJFlowEntity>();
    Context mContext;

    public MyFlowListAdapter(Context con, List<MyJFlowEntity> itemlist) {
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
            convertView = mInflater.inflate(R.layout.item_myjflow, null);

            vh.thumView = (ImageView) convertView
                    .findViewById(R.id.notice_avatar);
            vh.titleView = (TextView) convertView
                    .findViewById(R.id.notice_title);
            vh.contentView = (TextView) convertView
                    .findViewById(R.id.notice_content);

            vh.titleView.setText(mData.get(position).getName());
            vh.contentView.setText(mData.get(position).getFk_flowsorttext());

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    private static class ViewHolder {
        ImageView thumView;
        TextView titleView;
        TextView contentView;
    }

}
