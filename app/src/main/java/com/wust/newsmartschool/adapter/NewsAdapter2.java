package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.News;
import com.wust.newsmartschool.ui.QualityDetialActivity;

import java.util.ArrayList;


public class NewsAdapter2 extends BaseAdapter
{

    private ArrayList<News> newsList;
    private Context context;

    public NewsAdapter2() {
    }

    public NewsAdapter2(Context context, ArrayList<News> newsList) {
        this.newsList = newsList;
        this.context = context;
    }

    public int getCount() {
        return newsList.size();
    }

    public Object getItem(int position) {
        return newsList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView newsTitle;
        TextView newsTime;
        ImageView newsImage;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_news, null);
            newsTitle = (TextView) convertView.findViewById(R.id.newsTitle);
            newsTime = (TextView) convertView.findViewById(R.id.newsTime);
            //newsImage = (ImageView) convertView.findViewById(R.id.newsImg);
            ViewHolder holder = new ViewHolder(newsTitle, newsTime);
            convertView.setTag(holder);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            newsTitle = holder.newsTitle;
            newsTime = holder.newsTime;
            //newsImage = holder.newsImage;
        }
        // 填充数据
        News newsItem = (News) getItem(position);
        newsTitle.setText(newsItem.getTITLE());
        newsTitle.setTextColor(Color.DKGRAY);


        newsTime.setText("发布日期：" + newsItem.getCREATETIME());

        //newsTime.setText("发布日期："+newsItem.getNewsTime());
        convertView.setOnClickListener(new NewsClickListener(String.valueOf(newsItem.getCREATETIME())));

        return convertView;
    }

    private class ViewHolder {
        public TextView newsTitle;
        public TextView newsTime;
        public ImageView newsImage;

        public ViewHolder(TextView newsTitle, TextView newsTime) {
            this.newsTitle = newsTitle;
            this.newsTime = newsTime;
            //this.newsImage = newsImage;
        }
    }

    // 新闻的点击事件
    private class NewsClickListener implements View.OnClickListener {

        private String newsId; // 新闻的id;

        public NewsClickListener(String newsId) {
            this.newsId = newsId;
        }

        public void onClick(View v) {
            // 跳转到详细新闻页面
            Intent intent = new Intent(context, QualityDetialActivity.class);
            intent.putExtra("id", newsId);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }
}
