package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.Common_TypeMem_Data;

import java.util.List;

/**
 * Created by Erick on 2016/9/19.
 */
public class ChooseMemsMainAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    List<Common_TypeMem_Data> item;
    Context context;
    private ViewHolder vh = new ViewHolder();


    public ChooseMemsMainAdapter(Context context, List<Common_TypeMem_Data> item) {
        this.item = item;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.choosemems_main, null);
            vh.imageView1 = (ImageView) convertView
                    .findViewById(R.id.imageView1);
            vh.friend_name = (TextView) convertView
                    .findViewById(R.id.friend_name);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.friend_name.setText(item.get(position).getUserRealname());
        return convertView;
    }

    private static class ViewHolder {
        private ImageView imageView1;
        private TextView friend_name;
    }
}
