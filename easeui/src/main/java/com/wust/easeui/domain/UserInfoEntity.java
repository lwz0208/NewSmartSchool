package com.wust.easeui.domain;

import java.io.Serializable;

public class UserInfoEntity implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    int code;
    String msg;
    UserInfoEntity_Data data;

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

    public UserInfoEntity_Data getData() {
        return data;
    }

    public void setData(UserInfoEntity_Data data) {
        this.data = data;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
