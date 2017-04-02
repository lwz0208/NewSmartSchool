package com.ding.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.ding.chat.R;
import com.ding.chat.domain.ChildTaskEntitiy;

import java.util.List;

public class ChildTask_Adapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ViewHolder vh;
    List<ChildTaskEntitiy> Items;
    Context mCon;

    public ChildTask_Adapter(Context con, List<ChildTaskEntitiy> objects) {
        mInflater = LayoutInflater.from(con);
        Items = objects;
        mCon = con;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Items.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return Items.get(position);
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
            convertView = mInflater.inflate(R.layout.childtask, null);
            vh = new ViewHolder();
            vh.childtask_name = (TextView) convertView
                    .findViewById(R.id.childtask_name);
            vh.childtask_time = (TextView) convertView
                    .findViewById(R.id.childtask_time);
            vh.isfinish = (ImageView) convertView
                    .findViewById(R.id.isfinish);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.childtask_name.setText(Items.get(position).getTitle());
        vh.childtask_time.setText(Items.get(position).getDeadline() + "截至");
        if (Items.get(position).getStatus() == 0) {
            vh.isfinish.setVisibility(View.INVISIBLE);
        } else {
            vh.isfinish.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private static class ViewHolder {
        private TextView childtask_name;
        private TextView childtask_time;
        private ImageView isfinish;
    }

}
