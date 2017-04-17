package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.ServiceItem;
import com.wust.newsmartschool.utils.GlobalVar;
import com.wust.newsmartschool.utils.PictureServer;

import java.util.ArrayList;

public class ServiceListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<ServiceItem> dataArrayList;
    private PictureServer imageFileCache;
    private Context context;
    public ServiceListAdapter(Context context, ArrayList<ServiceItem> folders) {
        layoutInflater = LayoutInflater.from(context);
        this.context=context;
        this.dataArrayList = folders;
        imageFileCache=new PictureServer(context, GlobalVar.cacheFile);
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
            convertView = layoutInflater.inflate(R.layout.service_item, null);
            holder=new ViewHolder();
            holder.imageView = (ImageView)convertView.findViewById(R.id.service_image);
            holder.nameTextView = (TextView)convertView.findViewById(R.id.service_name);
            holder.desTextView = (TextView)convertView.findViewById(R.id.service_describe);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }
        ServiceItem serviceItem = getItem(position);

//		Log.i("pic", ServiceItem.get("pciUrl"));
//		asyncOP.asyncDownPic(imageView, ServiceItem.get("pciUrl"), cachefile);
//		holder.imageView.setImageBitmap(imageFileCache.getImage(ServiceItem.getGridImgUrl(), 80, 80));
        int drawableId = serviceItem.getServiceImgDrawable();
        if(drawableId == 0)
        {
            holder.imageView.setVisibility(View.GONE);
        }
        else
        {
            Drawable drawable=context.getResources().getDrawable(drawableId);
            holder.imageView.setImageDrawable(drawable);
        }
//		imageView.setLayoutParams(new RelativeLayout.LayoutParams(100,90));
        Log.i("name", serviceItem.getServiceName());
        holder.nameTextView.setText(serviceItem.getServiceName());
        holder.nameTextView.setTextColor(Color.DKGRAY);
//		holder.nameTextView.setTextColor(color);
        holder.desTextView.setText(serviceItem.getDescription());
        holder.desTextView.setTextColor(Color.GRAY);
//		holder.nameTextView.setTextColor(Color.BLACK);

        return convertView;
    }

    private class ViewHolder{
        public ImageView imageView;
        public TextView nameTextView;
        public TextView desTextView;
    }
}

