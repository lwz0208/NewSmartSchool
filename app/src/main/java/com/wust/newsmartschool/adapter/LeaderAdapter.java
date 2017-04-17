package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.SchoolLeader;

import java.util.List;


public class LeaderAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<SchoolLeader> leaderList;

    public LeaderAdapter(Context context, List<SchoolLeader> leaderList) {
        this.leaderList = leaderList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    private class ViewHolder {
        public TextView name;
        public TextView job;
    }

    @Override
    public int getCount() {
        return leaderList.size();
    }

    @Override
    public Object getItem(int position) {
        return leaderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder myViewHolder = null;
        if (convertView == null)
        {
            myViewHolder = new ViewHolder();
            // 获取list_item布局文件的视图
            convertView = layoutInflater.inflate(R.layout.item_leaders, null);
            // 获取控件对象
            myViewHolder.name = (TextView) convertView
                    .findViewById(R.id.leaderName);
            myViewHolder.job = (TextView) convertView
                    .findViewById(R.id.leaderJob);
            // 设置控件集到convertView
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (ViewHolder) convertView.getTag();
        }

        myViewHolder.name.setText(leaderList.get(position).getName());
        myViewHolder.job.setText(leaderList.get(position).getJob());

        return convertView;
    }
}
