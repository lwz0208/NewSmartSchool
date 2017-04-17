package com.wust.newsmartschool.domain;


public class News {


    /**
     * NEWSADDRESS : http://www.cnwust.com/news/82652_1
     * TITLE : 学校党委中心组召开学习贯彻巡视工作指示精神扩大会
     * CREATETIME : 2016-10-12 09:32:01
     */

    private String NEWSADDRESS;
    private String TITLE;
    private String CREATETIME;

    public String getNEWSADDRESS() {
        return NEWSADDRESS;
    }

    public void setNEWSADDRESS(String NEWSADDRESS) {
        this.NEWSADDRESS = NEWSADDRESS;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getCREATETIME() {
        return CREATETIME;
    }

    public void setCREATETIME(String CREATETIME) {
        this.CREATETIME = CREATETIME;
    }
}


