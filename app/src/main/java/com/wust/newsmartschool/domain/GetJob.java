package com.wust.newsmartschool.domain;
public class GetJob {
    private String  getjob_xwbh;
    private String  getjob_xwbt;//内容
    private String  getjob_fbsj;//发布时间
    private String  messageResource; //信息来源
    private int clickNum;

    public int getClickNum() {
        return clickNum;
    }

    public void setClickNum(int clickNum) {
        this.clickNum = clickNum;
    }

    public String getjob_xwbh() {
        return getjob_xwbh;
    }

    public void set_getjob_xwbh(String getjob_xwbh) {
        this.getjob_xwbh = getjob_xwbh;
    }

    public String getjob_xwbt() {
        return getjob_xwbt;
    }

    public void set_getjob_xwbt(String getjob_xwbt) {
        this.getjob_xwbt = getjob_xwbt;
    }

    public String getjob_fbsj() {
        return getjob_fbsj;
    }

    public void set_getjob_fbsj(String getjob_fbsj) {
        this.getjob_fbsj = getjob_fbsj;
    }

    public String getMessageResource() {
        return messageResource;
    }

    public void setMessageResource(String messageResource) {
        this.messageResource = messageResource;
    }
}

