package com.ding.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ding.chat.R;
import com.ding.chat.domain.Company_Dep;
import com.ding.chat.domain.Type_GCD;

import java.util.List;

public class UplistviewGCD_Adapter extends BaseAdapter {
    private LayoutInflater mInflater;
    Type_GCD itemlist;
    private ViewHolder vh = new ViewHolder();

    public UplistviewGCD_Adapter(Context con, Type_GCD objects) {
        mInflater = LayoutInflater.from(con);
        itemlist = objects;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return itemlist.getData().size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return itemlist.getData().get(position);
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
            convertView = mInflater.inflate(R.layout.mycompanygcd_item, null);
            vh.brand_name = (TextView) convertView
                    .findViewById(R.id.brand_name);
            vh.brand_num = (TextView) convertView.findViewById(R.id.brand_num);
            vh.brand_name.setText(itemlist.getData().get(position).getName());

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
