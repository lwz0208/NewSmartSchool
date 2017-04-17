package com.wust.newsmartschool.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ListView;


import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ListView;
import android.widget.TextView;

/**
 *@Description: 订餐item
 *@Author: Effall
 *@Date: 2015年2月9日
 *@Update by YorekLiu
 */

public class MealItem implements Parcelable
{
    private String name;//菜品名称
    private String price;//菜品现价
    private String prePrice;//菜品原价
    private int saleNum;//月售量
    private int count;//预订数量
    private String descripation;//菜品描述
    private String imgUrl;//菜品图片
    private String mealId;
    private ListView listView;
    private int totalMoneyTextView;
    private String num;

    public String getNum()
    {
        return num;
    }
    public void setNum(String num)
    {
        this.num=num;
    }
    public int getTotalMoneyTextView()
    {
        return totalMoneyTextView;
    }
    public void setTotalMoneyTextView(int totalMoneyTextView)
    {
        this.totalMoneyTextView=totalMoneyTextView;
    }
    public String getMealId()
    {
        return mealId;
    }
    public void setMealId(String mealId)
    {
        this.mealId=mealId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getPrePrice()
    {
        return prePrice;
    }

    public void setPrePrice(String prePrice)
    {
        this.prePrice = prePrice;
    }

    public int getSaleNum()
    {
        return saleNum;
    }

    public void setSaleNum(int saleNum)
    {
        this.saleNum = saleNum;
    }

    public String getDescripation()
    {
        return descripation;
    }

    public void setDescripation(String descripation)
    {
        this.descripation = descripation;
    }

    public String getImgUrl()
    {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(name);
        dest.writeString(price);
        dest.writeInt(count);
        //dest.writeInt(saleNum);//
        dest.writeString(mealId);
        dest.writeString(imgUrl);
        dest.writeString(descripation);
    }




    @Override
    public String toString()
    {
        return "MealItem [name=" + name + ", price=" + price + ", prePrice="
                + prePrice + ", saleNum=" + saleNum + ", count=" + count
                + ", descripation=" + descripation + ", imgUrl=" + imgUrl
                + ", mealId=" + mealId + ", listView=" + listView + "]";
    }




    public static final Parcelable.Creator<MealItem> CREATOR = new Creator<MealItem>()
    {
        @Override
        public MealItem createFromParcel(Parcel source)
        {
            MealItem mealItem = new MealItem();
            mealItem.setName(source.readString());
            mealItem.setPrice(source.readString());
            mealItem.setCount(source.readInt());
            mealItem.setMealId(source.readString());
            mealItem.setImgUrl(source.readString());
            mealItem.setDescripation(source.readString());
            return mealItem;
        }

        @Override
        public MealItem[] newArray(int size)
        {
            return new MealItem[size];
        }



    };
}

