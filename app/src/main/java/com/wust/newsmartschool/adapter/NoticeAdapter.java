package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.Notice;

import java.util.List;


public class NoticeAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Notice> noticeList;

    public NoticeAdapter(Context context, List<Notice> noticeList) {
        this.noticeList = noticeList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    private class ViewHolder {
        public TextView newsTitle;
        public TextView newsTime;
    }

    @Override
    public int getCount() {
        return noticeList.size();
    }

    @Override
    public Object getItem(int position) {
        return noticeList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.item_news, null);
            // 获取控件对象
            myViewHolder.newsTitle = (TextView) convertView
                    .findViewById(R.id.newsTitle);
            myViewHolder.newsTime = (TextView) convertView
                    .findViewById(R.id.newsTime);
            // 设置控件集到convertView
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (ViewHolder) convertView.getTag();
        }

        myViewHolder.newsTitle.setText(noticeList.get(position).getTITLE());
        myViewHolder.newsTime.setText(noticeList.get(position).getCREATETIME().substring(0,16));

        return convertView;
    }
}

