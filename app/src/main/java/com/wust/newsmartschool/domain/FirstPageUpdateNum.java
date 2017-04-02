package com.wust.newsmartschool.domain;

/**
 * Created by Li Wenzhao on 2017/3/2.
 */
public class FirstPageUpdateNum {
    /**
     * data : 0
     * event : inform_unread
     */

    private int data;
    private String event;

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
