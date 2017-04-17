package com.wust.newsmartschool.domain;


public class PsyRecordItem {

    private int id;//id
    private String date;//日期
    private String expertName;//咨询师姓名
    private String expertPic;//咨询师头像
    private String type;//诊断类型
    private String status;//预约状态
    private String time;//上下午“1、2”

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getExpertName() {
        return expertName;
    }
    public void setExpertName(String expertName) {
        this.expertName = expertName;
    }
    public String getExpertPic() {
        return expertPic;
    }
    public void setExpertPic(String expertPic) {
        this.expertPic = expertPic;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }


    public PsyRecordItem(int id, String date, String expertName,
                         String expertPic, String type, String status, String time) {
        super();
        this.id = id;
        this.date = date;
        this.expertName = expertName;
        this.expertPic = expertPic;
        this.type = type;
        this.status = status;
        this.time = time;
    }
    public PsyRecordItem() {
        super();
    }
}

