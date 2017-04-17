package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.PsychologistInfoItem;
import com.wust.newsmartschool.utils.GlobalVar;
import com.wust.newsmartschool.utils.PictureServer;

import java.util.ArrayList;


public class Psy_ExpertListViewAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<PsychologistInfoItem> dataArrayList;
    private Context context;

    public Psy_ExpertListViewAdapter(Context context,
                                     ArrayList<PsychologistInfoItem> folders) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.dataArrayList = folders;
    }

    @Override
    public int getCount() {
        return dataArrayList.size();
    }

    @Override
    public PsychologistInfoItem getItem(int i) {
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
                    .inflate(R.layout.item_psy_expert, null);
            holder = new ViewHolder();
            holder.expetyPic = (ImageView) convertView
                    .findViewById(R.id.psy_expertImg);
            holder.expertName = (TextView) convertView
                    .findViewById(R.id.psy_expertname);
            holder.expertText = (TextView) convertView
                    .findViewById(R.id.psy_expertext);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PsychologistInfoItem serviceItem = getItem(position);
        // 设置头像、姓名、简介
        holder.expertName.setText(serviceItem.getName());
        holder.expertText.setText(serviceItem.getIntroduction());
        holder.expetyPic.setTag(serviceItem.getImgurl());
        PictureServer pictureServer = new PictureServer(context,
                GlobalVar.cacheFile);
        pictureServer.asyncDownPic(holder.expetyPic, serviceItem.getImgurl(),
                GlobalVar.cacheFile, 70, 70, null, null);
        return convertView;
    }

    private class ViewHolder {
        public ImageView expetyPic;
        public TextView expertName;
        public TextView expertText;
    }
}
