package com.ding.chat.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.ding.chat.R;
import com.ding.chat.domain.CommonNews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * @author rockstore
 * */

public class NewsListAdapter extends BaseAdapter {
    private ArrayList<CommonNews> mData;
    private Context mCt;
    private LayoutInflater inflater;

    public NewsListAdapter(ArrayList<CommonNews> data, Context ct) {
        mData = data;
        mCt = ct;
        inflater = (LayoutInflater) mCt.getSystemService(mCt.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (mData == null)
            return 0;
        return mData.size();
    }

    @Override
    public Object getItem(int arg0) {
        if (mData == null)
            return null;
        return mData.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        if (mData == null)
            return 0;
        return arg0;
    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {
        CommonNews news = mData.get(arg0);
        ViewBundle viewBundle = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_commonnews, null);
            viewBundle = new ViewBundle();
            viewBundle.thum = (ImageView) convertView.findViewById(R.id.news_thum);
            viewBundle.title = (TextView) convertView.findViewById(R.id.news_title);
            viewBundle.content = (TextView) convertView.findViewById(R.id.news_content);
            convertView.setTag(viewBundle);
        } else
            viewBundle = (ViewBundle) convertView.getTag();

        viewBundle.title.setText(news.getTitle());
        viewBundle.content.setText(news.getContent());

        String img = news.getImageurl1();
        if (null != img && !img.equals("null") && !img.equals("")) {
            img = img.contains("http://www.wh5yy.com") ? img : "http://www.wh5yy.com" + img;
            viewBundle.thum.setVisibility(View.VISIBLE);
            Glide.with(mCt).load(img).placeholder(R.drawable.em_default_image)
                    .into(viewBundle.thum);
        }
        return convertView;
    }

    private class ViewBundle {
        ImageView thum;
        TextView title;
        TextView content;
    }

}
