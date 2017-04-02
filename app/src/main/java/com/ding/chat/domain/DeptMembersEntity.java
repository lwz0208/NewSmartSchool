package com.ding.chat.domain;

import java.io.Serializable;
import java.util.List;

public class DeptMembersEntity implements Serializable {
    int code;
    String msg;
    List<DeptMembersEntity_Person> data;

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

    public List<DeptMembersEntity_Person> getData() {
        return data;
    }

    public void setData(List<DeptMembersEntity_Person> data) {
        this.data = data;
    }

}
