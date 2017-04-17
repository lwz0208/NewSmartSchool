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
import com.wust.newsmartschool.domain.JobItem;
import com.wust.newsmartschool.ui.RecruitmentMessageDetail;

import java.util.ArrayList;


public class JobAdapter2 extends BaseAdapter
{
    private ArrayList<JobItem> jobList;
    private Context context;

    public JobAdapter2()
    {
    }

    public JobAdapter2(Context context, ArrayList<JobItem> jobList)
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
        String title = jobItem.getTitle();

        jobTitle.setText(title);
        jobTitle.setTextColor(Color.DKGRAY);

        jobTime.setText(null);

        convertView.setOnClickListener(new JobClickListener(jobItem.getId(),"来自"+jobItem.getTitle()+"的招聘信息"));

        return convertView;
    }

    private class ViewHolder
    {
        public TextView jobTitle;
        public TextView jobTime;
        public ImageView jobImage;

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
        private String jobTitle; //新闻标题title

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
            intent.setClass(context, RecruitmentMessageDetail.class);
            intent.putExtra("nextId", jobId);
            intent.putExtra("title", jobTitle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
