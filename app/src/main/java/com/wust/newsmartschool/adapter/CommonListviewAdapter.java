package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wust.newsmartschool.R;

/**
 *@Description:
 *@Author: Effall
 *@Date: 2014年9月23日
 */

public class CommonListviewAdapter extends BaseAdapter
{

    private Context context;
    private int [] imgList;
    private String[] item;
    private LayoutInflater layoutInflater;

    public CommonListviewAdapter(Context context,int [] imgList,String [] item)
    {
        this.context=context;
        this.imgList=imgList;
        this.item=item;
        layoutInflater=LayoutInflater.from(context);
    }
    public int getCount()
    {
        return item.length;
    }

    public String getItem(int position)
    {
        return item[position];
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder=null;
        if(convertView==null)
        {
            holder=new ViewHolder();
            convertView=layoutInflater.inflate(R.layout.commonlist, null);
            holder.imageView=(ImageView)convertView.findViewById(R.id.commonImage);
            holder.textView=(TextView)convertView.findViewById(R.id.commonText);
            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)convertView.getTag();
        }
        Drawable drawable=context.getResources().getDrawable(imgList[position]);
        holder.imageView.setImageDrawable(drawable);
        holder.textView.setText(item[position]);
        return convertView;
    }

    private class ViewHolder
    {
        public ImageView imageView;
        public TextView textView;
    }

}
