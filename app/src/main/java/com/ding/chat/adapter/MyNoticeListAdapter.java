package com.ding.chat.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.ding.chat.R;
import com.ding.chat.domain.NoticeEntity;
import com.ding.easeui.widget.GlideRoundTransform;
import com.ding.easeui.Constant;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyNoticeListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<NoticeEntity> mData = new ArrayList<NoticeEntity>();
    Context mContext;


    public MyNoticeListAdapter(Context con, List<NoticeEntity> itemlist) {
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
        ViewHolder vh;
//        if (convertView == null) {
        convertView = mInflater.inflate(R.layout.item_notice, null);
        vh = new ViewHolder();
        vh.timeView = (TextView) convertView.findViewById(R.id.notice_time);
        vh.thumView = (ImageView) convertView
                .findViewById(R.id.notice_avatar);
        vh.titleView = (TextView) convertView
                .findViewById(R.id.notice_title);
        vh.contentView = (TextView) convertView
                .findViewById(R.id.notice_content);
        vh.unread_notice_number = (TextView) convertView
                .findViewById(R.id.unread_notice_number);
        vh.notice_dept = (TextView) convertView
                .findViewById(R.id.notice_dept);

        convertView.setTag(vh);
//        } else {
//            vh = (ViewHolder) convertView.getTag();
//        }

        //设置头像
        Glide.with(mContext)
                .load(Constant.GETHEADIMAG_URL
                        + mData.get(position).getSend()
                        + ".png").transform(new GlideRoundTransform(mContext)).placeholder(R.drawable.ease_default_avatar)
                .into(vh.thumView);
        if (!TextUtils.isEmpty(mData.get(position).getTitle())) {
            vh.titleView.setVisibility(View.VISIBLE);
            vh.titleView.setText(mData.get(position).getTitle());
        } else {
            vh.titleView.setVisibility(View.INVISIBLE);
        }
//以前是显示通知内容，现在改为显示通知发起人的姓名
        if (!TextUtils.isEmpty(mData.get(position).getUserRealname())) {
            vh.contentView.setVisibility(View.VISIBLE);
            vh.contentView.setText(mData.get(position).getUserRealname());
        } else {
            vh.contentView.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(mData.get(position).getDepartmentName())) {
            vh.notice_dept.setVisibility(View.VISIBLE);
            vh.notice_dept.setText(mData.get(position).getDepartmentName());
        } else {
            vh.notice_dept.setVisibility(View.INVISIBLE);
        }
        if (!TextUtils.isEmpty(mData.get(position).getCreateTime())) {
            vh.timeView.setVisibility(View.VISIBLE);
            vh.timeView.setText(mData.get(position).getCreateTime());
        } else {
            vh.timeView.setVisibility(View.INVISIBLE);
        }


        return convertView;

    }

    private static class ViewHolder {
        TextView unread_notice_number;
        ImageView thumView;
        TextView timeView;
        TextView titleView;
        TextView contentView;
        TextView notice_dept;
    }

}
