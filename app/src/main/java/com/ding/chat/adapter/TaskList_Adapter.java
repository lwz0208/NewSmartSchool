package com.ding.chat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ding.chat.R;
import com.ding.easeui.Constant;
import com.ding.easeui.widget.GlideRoundTransform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskList_Adapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ViewHolder vh;
    List<Map<String, Object>> Items = new ArrayList<>();
    Context mContext;

    public TaskList_Adapter(Context con,
                            List<Map<String, Object>> itemlist) {
        mContext = con;
        mInflater = LayoutInflater.from(con);
        Items = itemlist;
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
            convertView = mInflater.inflate(R.layout.task_item, parent, false);
            vh = new ViewHolder();
            vh.iv_status = (ImageView) convertView.findViewById(R.id.iv_status);
            vh.tv_taskTitle = (TextView) convertView.findViewById(R.id.tv_taskTitle);
            vh.tv_taskType = (TextView) convertView.findViewById(R.id.tv_taskType);
            vh.tv_taskTime = (TextView) convertView.findViewById(R.id.tv_taskTime);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        if ((int) Items.get(position).get("status") == 1) {
            vh.iv_status.setImageResource(R.drawable.work_task_done);
        } else {
            Glide.with(mContext)
                    .load(Constant.GETHEADIMAG_URL
                            + Items.get(position).get("createUserId") + ".png").transform(new GlideRoundTransform(mContext))
                    .placeholder(R.drawable.ease_default_avatar)
                    .into(vh.iv_status);
        }
        vh.tv_taskTitle.setText((String) Items.get(position).get("title"));
        vh.tv_taskTime.setText("截止时间:" + (String) Items.get(position).get("time"));
        //为1表示的是十万火急,2紧急，3一般
        vh.tv_taskType.setText("优先级：" + Items.get(position).get("priorityName"));
        try {
            vh.tv_taskTitle.setTextColor(Color.parseColor((String) Items.get(position).get("priorityColor")));
            vh.tv_taskType.setTextColor(Color.parseColor((String) Items.get(position).get("priorityColor")));
        } catch (Exception e) {
            vh.tv_taskTitle.setTextColor(mContext.getResources().getColor(R.color.common_bottom_bar_normal_bg));
            vh.tv_taskType.setTextColor(mContext.getResources().getColor(R.color.common_bottom_bar_normal_bg));
            e.printStackTrace();
        }
        return convertView;
    }

    private static class ViewHolder {
        private ImageView iv_status;
        private TextView tv_taskTitle;
        private TextView tv_taskType;
        private TextView tv_taskTime;
    }

}
