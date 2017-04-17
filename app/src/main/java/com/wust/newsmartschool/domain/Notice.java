package com.wust.newsmartschool.domain;


/**
 * Created by Li Wenzhao on 2016/10/22.
 */
public class Notice {
    /**
     * NEWSADDRESS : http://
     * PUBLISHER : kjc
     * TITLE : 省科技厅关于开展2016年度湖北省校企共建研发中心认定工作的通知
     * ID : 147703051000773351
     * RM : 1
     * CREATETIME : 2016-10-21 14:08:20
     */

    private String NEWSADDRESS;
    private String PUBLISHER;
    private String TITLE;
    private String ID;
    private int RM;
    private String CREATETIME;

    public String getNEWSADDRESS() {
        return NEWSADDRESS;
    }

    public void setNEWSADDRESS(String NEWSADDRESS) {
        this.NEWSADDRESS = NEWSADDRESS;
    }

    public String getPUBLISHER() {
        return PUBLISHER;
    }

    public void setPUBLISHER(String PUBLISHER) {
        this.PUBLISHER = PUBLISHER;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getRM() {
        return RM;
    }

    public void setRM(int RM) {
        this.RM = RM;
    }

    public String getCREATETIME() {
        return CREATETIME;
    }

    public void setCREATETIME(String CREATETIME) {
        this.CREATETIME = CREATETIME;
    }
}
