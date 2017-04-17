package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.ServiceItem;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<ServiceItem> dataArrayList;
    private Context context;
    public ListViewAdapter(Context context, ArrayList<ServiceItem> folders) {
        layoutInflater = LayoutInflater.from(context);
        this.context=context;
        this.dataArrayList = folders;
    }

    @Override
    public int getCount() {
        return dataArrayList.size();
    }

    @Override
    public ServiceItem getItem(int i) {
        return dataArrayList.get(i);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.listview2_item, null);
            holder=new ViewHolder();

            holder.nameTextView = (TextView)convertView.findViewById(R.id.service_name);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }
        ServiceItem serviceItem = getItem(position);

//		Log.i("pic", ServiceItem.get("pciUrl"));
//		asyncOP.asyncDownPic(imageView, ServiceItem.get("pciUrl"), cachefile);
//		holder.imageView.setImageBitmap(imageFileCache.getImage(ServiceItem.getGridImgUrl(), 80, 80));

//		imageView.setLayoutParams(new RelativeLayout.LayoutParams(100,90));
        Log.i("name", serviceItem.getServiceName());
        holder.nameTextView.setText(serviceItem.getServiceName());
        holder.nameTextView.setTextColor(Color.DKGRAY);
//		holder.nameTextView.setTextColor(color);

//		holder.nameTextView.setTextColor(Color.BLACK);

        return convertView;
    }

    private class ViewHolder{
        public TextView nameTextView;
    }
}

