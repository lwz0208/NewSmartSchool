package com.wust.newsmartschool.domain;

import android.os.Parcel;
import android.os.Parcelable;


import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class RecordMeal implements Parcelable
{
    private String price;//单价
    private String dishType;//菜的类别，分为1，2，3，
    private String dishName;//菜名名称
    private String dishStatus;//订单的状态，分为订单取消，订单完成，待评价
    private String imgUrl;//菜品类别图片，1，2，3分别对应炒菜，套餐，特色美食
    private String orderTime;//订餐时间
    private String mealOrderId;
    public String getOrderTime()
    {
        return orderTime;
    }
    public String getMealOrderId()
    {
        return mealOrderId;
    }
    public void setMealOrderId(String mealOrderId)
    {
        this.mealOrderId = mealOrderId;
    }
    public void setOrderTime(String orderTime)
    {
        this.orderTime = orderTime;
    }
    public String getDishName()
    {
        return dishName;
    }

    public void setDishName(String dishName)
    {
        this.dishName = dishName;
    }
    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getDishType()
    {
        return dishType;
    }

    public void setDishType(String dishType)
    {
        this.dishType = dishType;
    }

    public String getDishStatus()
    {
        return dishStatus;
    }

    public void setDishStatus(String dishStatus)
    {
        this.dishStatus = dishStatus;
    }

    public String getImgUrl()
    {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
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
        dest.writeString(dishName);
        dest.writeString(dishStatus);
        dest.writeString(dishType);
        dest.writeString(imgUrl);
        dest.writeString(orderTime);
        dest.writeString(price);
        dest.writeString(mealOrderId);
    }
    public String toString()
    {
        return "MealItem [dishName=" + dishName + ",dishStatus="+dishStatus+",dishType="+dishType+",imgUrl="
                +imgUrl+",orderTime="+orderTime+",price="+price+",mealOrderId="+mealOrderId+"]";
    }
    public static final Parcelable.Creator<RecordMeal> CREATOR = new Creator<RecordMeal>()
    {

        @Override
        public RecordMeal createFromParcel(Parcel source)
        {
            // TODO Auto-generated method stub
            RecordMeal recordMeal=new RecordMeal();
            recordMeal.setDishStatus(source.readString());
            recordMeal.setDishType(source.readString());
            recordMeal.setImgUrl(source.readString());
            recordMeal.setOrderTime(source.readString());
            recordMeal.setPrice(source.readString());
            recordMeal.setDishName(source.readString());
            recordMeal.setMealOrderId(source.readString());
            return recordMeal;
        }

        @Override
        public RecordMeal[] newArray(int size)
        {
            // TODO Auto-generated method stub
            return new RecordMeal[size];
        }

    };

}

