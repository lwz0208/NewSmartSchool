package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.PsyRecordItem;

import java.util.ArrayList;



public class Psy_RecordListViewAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<PsyRecordItem> dataArrayList;
    private Context context;

    public Psy_RecordListViewAdapter(Context context,
                                     ArrayList<PsyRecordItem> folders) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.dataArrayList = folders;
    }

    @Override
    public int getCount() {
        return dataArrayList.size();
    }

    @Override
    public PsyRecordItem getItem(int i) {
        return dataArrayList.get(i);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater
                    .inflate(R.layout.item_psy_record, null);
            holder = new ViewHolder();
            holder.recordDate = (TextView) convertView
                    .findViewById(R.id.psy_date);
            holder.expertName = (TextView) convertView
                    .findViewById(R.id.psy_expertname);
            holder.typename = (TextView) convertView
                    .findViewById(R.id.psy_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PsyRecordItem serviceItem = getItem(position);
        // 设置日期、医生姓名、类型名
        holder.recordDate.setText(serviceItem.getDate());
        holder.expertName.setText(serviceItem.getExpertName());
        if (serviceItem.getType()==null||serviceItem.getType().equals("null"))
            holder.typename.setText("");
        else
            holder.typename.setText(serviceItem.getType());
        return convertView;
    }

    private class ViewHolder {
        public TextView recordDate;
        public TextView expertName;
        public TextView typename;
    }
}
