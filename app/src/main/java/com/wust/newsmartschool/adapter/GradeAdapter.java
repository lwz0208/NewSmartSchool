package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.GradeItem;

import java.util.ArrayList;


/**
 *@Description:
 *@Author: Effall
 *@Date: 2014年10月23日
 */

public class GradeAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<GradeItem> list;
    private LayoutInflater inflater;

    public GradeAdapter(Context context,ArrayList<GradeItem> list){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }
    public int getCount() {
        return list.size();
    }

    public GradeItem getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.gradeitem, null);
            holder=new ViewHolder();
            holder.tv1=(TextView)convertView.findViewById(R.id.grade_id);
            holder.tv2=(TextView)convertView.findViewById(R.id.grade_name);
            holder.tv3=(TextView)convertView.findViewById(R.id.grade_grade);
            holder.tv4=(TextView)convertView.findViewById(R.id.grade_class);
            convertView.setTag(holder);
        }
        else {
            holder=(ViewHolder)convertView.getTag();
        }
        GradeItem item=list.get(position);
        holder.tv1.setText(""+(position+1));
        holder.tv2.setText(item.getKcmc());
        holder.tv3.setText(item.getZcj());
        holder.tv4.setText(item.getKcsx());

        return convertView;
    }
    private static class ViewHolder{
        public TextView tv1;
        public TextView tv2;
        public TextView tv3;
        public TextView tv4;
    }

}

