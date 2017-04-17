package com.wust.newsmartschool.domain;

/**
 *@Description:
 *@Author: Effall
 *@Date: 2014年8月26日
 */

public class DateItem {
    public String num;
    public String date;
    public String week;

    public DateItem(String num,String date,String week) {
        this.num = num;
        this.date = date;
        this.week = week;
    }
    public DateItem(){

    }
    public String getNum() {
        return num;
    }
    public void setNum(String num) {
        this.num = num;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String toString() {
        return date+"（"+week+"）";
    }
    public void setWeek(String week) {
        this.week = week;
    }
    public String getWeek() {
        return num;
    }


}

