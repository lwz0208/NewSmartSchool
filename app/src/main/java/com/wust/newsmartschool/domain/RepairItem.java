package com.wust.newsmartschool.domain;


public class RepairItem {

    private int id=-1;//保修单id
    private String sname=null;//报修人姓名
    private String sid=null;//报修人学号
    private String stel=null;//报修人电话
    private String sdormitory=null;//报修人寝室号
    private String date=null;//报修年月日
    private String time=null;//报修时间段
    private String dordetail=null;//寝室报修具体信息
    private String pubdetail=null;//非寝室报修具体信息
    private int wid=0;//维修人员工号
    private int status=-1;//报修状态
    private String campus=null;//校区
    private String refuse = null;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getSname() {
        return sname;
    }
    public void setSname(String sname) {
        this.sname = sname;
    }
    public String getSid() {
        return sid;
    }
    public void setSid(String sid) {
        this.sid = sid;
    }
    public String getStel() {
        return stel;
    }
    public void setStel(String stel) {
        this.stel = stel;
    }
    public String getSdormitory() {
        return sdormitory;
    }
    public void setSdormitory(String sdormitory) {
        this.sdormitory = sdormitory;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getDordetail() {
        return dordetail;
    }
    public void setDordetail(String dordetail) {
        this.dordetail = dordetail;
    }
    public String getPubdetail() {
        return pubdetail;
    }
    public void setPubdetail(String pubdetail) {
        this.pubdetail = pubdetail;
    }
    public int getWid() {
        return wid;
    }
    public void setWid(int wid) {
        this.wid = wid;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getCampus() {
        return campus;
    }
    public void setCampus(String campus) {
        this.campus = campus;
    }
    public RepairItem() {
        super();
    }
    public String getRefuse() {
        return refuse;
    }
    public void setRefuse(String refuse) {
        this.refuse = refuse;
    }
}

