package com.wust.newsmartschool.domain;

public class HjhBusItem {
    private String department;
    private String time;
    private String contentString;

    public void setDepartment(String department){
        this.department=department;
    }
    public String getDepartment(){
        return department;
    }

    public void setTime(String time){
        this.time=time;
    }
    public String getTime(){
        return time;
    }

    public void setContent(String contentString){
        this.contentString=contentString;
    }
    public String getContent(){
        return contentString;
    }

}

