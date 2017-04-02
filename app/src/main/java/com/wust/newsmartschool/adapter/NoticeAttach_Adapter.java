package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.NoticeDetailEntity;

import java.util.List;

public class NoticeAttach_Adapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ViewHolder vh;
    List<NoticeDetailEntity.FileListBean> adapter_list;
    Context mContext;

    public NoticeAttach_Adapter(Context con,
                                List<NoticeDetailEntity.FileListBean> itemlist) {
        mContext = con;
        mInflater = LayoutInflater.from(con);
        adapter_list = itemlist;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return adapter_list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return adapter_list.get(position);
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
            convertView = mInflater.inflate(R.layout.noticeattach, parent, false);
            vh = new ViewHolder();
            vh.noticeattach_item = (TextView) convertView
                    .findViewById(R.id.noticeattach_item);
            vh.attachstatusimg = (ImageView) convertView
                    .findViewById(R.id.attachstatusimg);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        try {
            vh.noticeattach_item.setText(adapter_list.get(position).getFileName().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //文件如果存在
            if (adapter_list.get(position).isFileExist()) {
                Glide.with(mContext).load(R.drawable.attachment_open).into(vh.attachstatusimg);
            } else {
                Glide.with(mContext).load(R.drawable.attachment_download).into(vh.attachstatusimg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private static class ViewHolder {
        private TextView noticeattach_item;
        private ImageView attachstatusimg;
    }

}
