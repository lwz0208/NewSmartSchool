package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wust.newsmartschool.R;

import java.util.ArrayList;

public class ClassRoomAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> list;
    LayoutInflater inflater;

    public ClassRoomAdapter(Context context,ArrayList<String>list){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        int i=list.size();
        if(i%2==0){
            i=i/2;
        }else{
            list.add("");
            i=i/2+1;
        }
        return i;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        String t1;
        String t2=null;
//		if(list.size()%2==0){

        t1=list.get(position*2);
        t2=list.get(position*2+1);
//		}else{
//			list.add("");
//			position+=position;
//			t1=list.get(position*2-1);
//			t2=list.get(position*2);
//		}
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.emptyclassroom_row, null);
            viewHolder=new ViewHolder();
            viewHolder.tv1=(TextView)convertView.findViewById(R.id.classroom_t1);
            viewHolder.tv2=(TextView)convertView.findViewById(R.id.classroom_t2);

            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.tv1.setText(t1);
        viewHolder.tv2.setText(t2);

        return convertView;
    }
    private static class ViewHolder{
        public TextView tv1;
        public TextView tv2;
    }
}

