package com.ding.chat.adapter;

import java.util.List;

import com.ding.chat.R;
import com.ding.chat.domain.Company_Dep;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Uplistview_Adapter extends BaseAdapter {
    private LayoutInflater mInflater;
    List<Company_Dep> itemlist;
    private ViewHolder vh = new ViewHolder();

    public Uplistview_Adapter(Context con, List<Company_Dep> objects) {
        mInflater = LayoutInflater.from(con);
        itemlist = objects;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return itemlist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return itemlist.get(position);
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
            convertView = mInflater.inflate(R.layout.mycompany_item, null);
            vh.brand_name = (TextView) convertView
                    .findViewById(R.id.brand_name);
            vh.brand_num = (TextView) convertView.findViewById(R.id.brand_num);
            vh.brand_name.setText(itemlist.get(position).getDepName());
            vh.brand_num.setText(itemlist.get(position).getUsersNum() + "人");
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    private static class ViewHolder {
        private TextView brand_name;
        private TextView brand_num;
    }

}
