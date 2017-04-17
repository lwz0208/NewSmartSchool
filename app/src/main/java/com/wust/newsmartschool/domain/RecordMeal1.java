package com.wust.newsmartschool.domain;

import android.os.Parcel;
import android.os.Parcelable;


import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class RecordMeal1 implements Parcelable
{
    private String mealPrice;//单价
    private String mealCount;//预订数量
    private String mealName;//菜品名称
    private String mealId;//菜的ｉｄ

    private String stuId;//送餐人学号
    private String stuName;//送餐人姓名

    private String stuTel;//送餐人电话


    private String stuStatus;//订单状态
    private String buyerId;//评价人ＩＤ
    private String stuArriveTime;//送餐到达时间
    private String stuAcceptTime;//接单时间
    private String stuTotalMoney;//总价钱
    private String stuSummitTime;//用户提交订单时间
    private String stuCancelTime;//用户取消点单时间
    private String stuReviewTime;

    public String getStuReviewTime()
    {
        return stuReviewTime;
    }

    public void setStuReviewTime(String stuReviewTime)
    {
        this.stuReviewTime = stuReviewTime;
    }


    public String getStuCancelTime()
    {
        return stuCancelTime;
    }

    public void setStuCancelTime(String stuCancelTime)
    {
        this.stuCancelTime = stuCancelTime;
    }

    public String getBuyerId()
    {
        return buyerId;
    }

    public void setBuyerId(String buyerId)
    {
        this.buyerId = buyerId;
    }
    public String getMealId()
    {
        return mealId;
    }

    public void setMealId(String mealId)
    {
        this.mealId = mealId;
    }


    public String getStuSummitTime()
    {
        return stuSummitTime;
    }

    public void setStuSummitTime(String stuSummitTime)
    {
        this.stuSummitTime = stuSummitTime;
    }

    public String getStuTotalMoney()
    {
        return stuTotalMoney;
    }

    public void setStuTotalMoney(String stuTotalMoney)
    {
        this.stuTotalMoney = stuTotalMoney;
    }

    public String getStuAcceptTime()
    {
        return stuAcceptTime;
    }

    public void setStuAcceptTime(String stuAcceptTime)
    {
        this.stuAcceptTime = stuAcceptTime;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuTel() {
        return stuTel;
    }

    public void setStuTel(String stuTel) {
        this.stuTel = stuTel;
    }

    public String getStuStatus() {
        return stuStatus;
    }

    public void setStuStatus(String stuStatus) {
        this.stuStatus = stuStatus;
    }

    public String getStuArriveTime() {
        return stuArriveTime;
    }

    public void setStuArriveTime(String stuArriveTime) {
        this.stuArriveTime = stuArriveTime;
    }

    public void setMealPrice(String mealPrice)
    {
        this.mealPrice = mealPrice;
    }

    public String getMealCount()
    {
        return mealCount;
    }

    public void setMealCount(String mealCount)
    {
        this.mealCount = mealCount;
    }

    public String getMealName()
    {
        return mealName;
    }

    public void setMealName(String mealName)
    {
        this.mealName = mealName;
    }
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        // TODO Auto-generated method stub
        dest.writeString(mealName);
        dest.writeString(mealPrice);
        dest.writeString(mealCount);
        dest.writeString(mealId);
        dest.writeString(buyerId);
    }

    public String getMealPrice()
    {
        return mealPrice;
    }
    public String toString()
    {
        return "RecordMeal1 [mealName=" + mealName+ ", mealPrice=" + mealPrice+
                "mealCount=" + mealPrice+ ",mealId="+mealId+"]";
    }
    public static final Parcelable.Creator<RecordMeal1> CREATOR = new Creator<RecordMeal1>()
    {
        @Override
        public RecordMeal1 createFromParcel(Parcel source)
        {
            RecordMeal1 mealItem1 = new RecordMeal1();
            mealItem1.setMealName(source.readString());
            mealItem1.setMealPrice(source.readString());
            mealItem1.setMealCount(source.readString());
            mealItem1.setMealId(source.readString());
            mealItem1.setBuyerId(source.readString());
            return mealItem1;
        }

        @Override
        public RecordMeal1[] newArray(int size)
        {
            return new RecordMeal1[size];
        }
    };


}

