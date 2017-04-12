package com.wust.newsmartschool.domain;

public class MsgItem {
    private String userid;
    private String msg;
    private String date;
    private String from;
//	private int chatline_num=0;//这个int是用来存储针对该好友，未读的信息数，在list中位置为0的为特殊Item

//	public MsgItem(int id) {
//		chatline_num = id;
//	}
//
//	public int getChatline_num() {
//		return chatline_num;
//	}
//
//	public void setChatline_num(int chatline_num) {
//		this.chatline_num = chatline_num;
//	}

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public MsgItem(String userid, String msg, String date, String from) {
        super();
        this.userid = userid;
        this.msg = msg;
        this.date = date;
        this.from = from;
    }

}

