package com.wust.newsmartschool.adapter;

import android.annotation.SuppressLint;
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
import com.wust.newsmartschool.domain.JobItem;
import com.wust.newsmartschool.ui.JobMessageDetailActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JobAdapter extends BaseAdapter
{
    private ArrayList<JobItem> jobList;
    private Context context;

    public JobAdapter()
    {
    }

    public JobAdapter(Context context, ArrayList<JobItem> jobList)
    {
        this.jobList = jobList;
        this.context = context;
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return jobList.size();
    }

    @Override
    public JobItem getItem(int position)
    {
        // TODO Auto-generated method stub
        return jobList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TextView jobTitle;
        TextView jobTime;
        ImageView jobImage;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.job_item, null);
            jobTitle = (TextView) convertView.findViewById(R.id.jobTitle);
            jobTime = (TextView) convertView.findViewById(R.id.jobTime);
            //jobImage = (ImageView) convertView.findViewById(R.id.newsImg);
            ViewHolder holder = new ViewHolder(jobTitle, jobTime);
            convertView.setTag(holder);
        }
        else
        {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            jobTitle = holder.jobTitle;
            jobTime = holder.jobTime;
            //jobImage = holder.jobImage;
        }
        // 填充数据

        JobItem jobItem = (JobItem) getItem(position);
        String titleAndTime = jobItem.getTitle();

        String title = titleAndTime.substring(0, titleAndTime.length() - 24);
        String time ="发布日期："
                + ChangeDateFormat(titleAndTime.substring(titleAndTime.length() - 17,
                titleAndTime.length() - 7));

        jobTitle.setText(title);
        jobTitle.setTextColor(Color.DKGRAY);

        jobTime.setText(time);

        convertView.setOnClickListener(new JobClickListener(jobItem.getId(),title.substring(6,title.length()-1)));

        return convertView;
    }

    // 将"yyyy-MM-dd"格式转化为"yyyy年MM月dd日"格式
    @SuppressLint("SimpleDateFormat")
    private String ChangeDateFormat(String dateString)
    {
        String formatedString = dateString;
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sd2 = new SimpleDateFormat("yyyy年MM月dd日");

        Date date;
        try
        {
            date = sd.parse(dateString);
            formatedString = sd2.format(date);
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return formatedString;
    }

    private class ViewHolder
    {
        public TextView jobTitle;
        public TextView jobTime;
        //public ImageView jobImage;

        public ViewHolder(TextView jobTitle, TextView jobTime)
        {
            this.jobTitle = jobTitle;
            this.jobTime = jobTime;
            //this.jobImage = jobImage;
        }
    }

    private class JobClickListener implements View.OnClickListener
    {

        private String jobId; // 新闻的id;
        private String jobTitle; // 新闻的title;

        public JobClickListener(String jobId,String jobTitle)
        {
            this.jobId = jobId;
            this.jobTitle = jobTitle;
        }

        @Override
        public void onClick(View arg0)
        {
            // TODO Auto-generated method stub
            Intent intent = new Intent();
            intent.setClass(context, JobMessageDetailActivity.class);
            intent.putExtra("id", jobId);
            intent.putExtra("title", jobTitle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}

