package com.wust.newsmartschool.domain;

import android.os.Parcel;
import android.os.Parcelable;


import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class RemarkDetail implements Parcelable
{
    private String remarkId;//评论人的id
    private String remarkTime;//评论的时间
    private String remarkStar;//评论的赞的个数
    private String remarkContent;//评论的内容
    public String getRemarkId()
    {
        return remarkId;
    }
    public void setRemarkId(String remarkId)
    {
        this.remarkId = remarkId;
    }
    public String getRemarkTime()
    {
        return remarkTime;
    }
    public void setRemarkTime(String remarkTime)
    {
        this.remarkTime = remarkTime;
    }
    public String getRemarkStar()
    {
        return remarkStar;
    }
    public void setRemarkStar(String remarkStar)
    {
        this.remarkStar = remarkStar;
    }
    public String getRemarkContent()
    {
        return remarkContent;
    }
    public void setRemarkContent(String remarkContent)
    {
        this.remarkContent = remarkContent;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(remarkContent);
        dest.writeString(remarkId);
        dest.writeString(remarkStar);
        dest.writeString(remarkTime);
    }
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    public static final Parcelable.Creator<RemarkDetail> CREATOR = new Creator<RemarkDetail>()
    {
        public RemarkDetail createFromParcel(Parcel source)
        {
            RemarkDetail remark = new RemarkDetail();
            remark.setRemarkContent(source.readString());
            remark.setRemarkId(source.readString());
            remark.setRemarkStar(source.readString());
            remark.setRemarkTime(source.readString());
            return remark;

        }

        @Override
        public RemarkDetail[] newArray(int size)
        {
            // TODO Auto-generated method stub
            return new RemarkDetail[size];
        }
    };

}

