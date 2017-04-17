package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wust.newsmartschool.R;

import java.util.ArrayList;

public class ClassRoomAdapter2 extends BaseAdapter {
    private Context context;
    private ArrayList<String> list;
    private int[] colors;
    LayoutInflater inflater;

    private ICallBack callBack;

    public interface ICallBack {
        public void click(View v, String jsid, int position);
    }

    public ClassRoomAdapter2(Context context, ArrayList<String> list,
                             ICallBack callBack, int[] colors) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.callBack = callBack;
        this.colors = colors;
        // this.colors = new int[5];
    }

    @Override
    public int getCount() {
        int i = list.size();
        if (i % 2 == 0) {
            i = i / 2;
        } else {
            list.add("");
            i = i / 2 + 1;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String t1;
        final String t2;

        t1 = list.get(position * 2);
        t2 = list.get(position * 2 + 1);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.emptyclassroom_row, null);
            viewHolder = new ViewHolder();
            viewHolder.tv1 = (TextView) convertView
                    .findViewById(R.id.classroom_t1);
            viewHolder.tv2 = (TextView) convertView
                    .findViewById(R.id.classroom_t2);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv1.setText(t1);
        viewHolder.tv2.setText(t2);
        viewHolder.tv1.setTag(position);
        viewHolder.tv2.setTag(position);

//		colors[3] = 1;
//		colors[4] = 1;
//		colors[5] = 1;
//		colors[6] = 1;
//		colors[1] = 1;
//		colors[8] = 1;
        if (colors[position * 2] == 1) {
            viewHolder.tv1.setTextColor(android.graphics.Color.GREEN);
            Log.e("colors[position * 2]:", (position * 2) + ""
                    + colors[position * 2]);
        }
        if (position * 2 + 1 < colors.length) {
            if (colors[position * 2 + 1] == 1) {
                viewHolder.tv2.setTextColor(android.graphics.Color.GREEN);
            }

        }
        if (colors[position * 2] == 0) {
            viewHolder.tv1.setTextColor(0xff88817B);
        }
        if (position * 2 + 1 < colors.length) {

            if (colors[position * 2 + 1] == 0) {
                viewHolder.tv2.setTextColor(0xff88817B);
            }
        }

        viewHolder.tv1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callBack.click(v, t1,position*2);

            }
        });
        viewHolder.tv2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callBack.click(v, t2,position*2+1);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        public TextView tv1;
        public TextView tv2;
    }

}

