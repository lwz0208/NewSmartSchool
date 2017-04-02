package com.wust.newsmartschool.domain;

import java.io.Serializable;

public class JFlowTrackEntity_Out implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    int code;
    String msg;
    JFlowTrackEntity data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JFlowTrackEntity getData() {
        return data;
    }

    public void setData(JFlowTrackEntity data) {
        this.data = data;
    }

}
