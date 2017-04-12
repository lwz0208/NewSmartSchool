package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wust.newsmartschool.R;

import java.util.List;


public class LibrarySeatGridViewAdapter extends BaseAdapter
{
    private Context context;
    private List<String> list;

    public LibrarySeatGridViewAdapter(Context context, List<String> list)
    {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            View view = View.inflate(context, R.layout.item_lib_seat, null);
            convertView = view;
        }

        String seatId = list.get(position);
        switch (seatId.length())
        {
            case 1:
                seatId = "00" + seatId;
                break;

            case 2:
                seatId = "0" + seatId;
                break;

            default:
                break;
        }
        ((TextView) convertView.findViewById(R.id.tv_lib_seat_id)).setText(seatId);

        return convertView;
    }
}

