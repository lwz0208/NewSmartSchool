package com.ding.chat.domain;

import java.io.Serializable;
import java.util.List;

public class Notice_OutEntity implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    int code;
    String msg;
    int page;
    int records;
    int total;
    List<NoticeEntity> data;

    public List<NoticeEntity> getData() {
        return data;
    }

    public void setData(List<NoticeEntity> data) {
        this.data = data;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
