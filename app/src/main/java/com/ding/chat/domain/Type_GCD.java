package com.ding.chat.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Erick on 2016/9/5.
 */
public class Type_GCD implements Serializable {


    /**
     * code : 1
     * data : [{"name":"妇儿党支部","id":1},{"name":"刑政党支部","id":2},{"name":"机关党支部","id":3},{"name":"门诊党支部","id":4},{"name":"内科党支部","id":5},{"name":"外五党支部","id":6},{"name":"血肿党支部","id":7},{"name":"医技党支部","id":8}]
     * msg : 请求成功
     */

    private int code;
    private String msg;
    /**
     * name : 妇儿党支部
     * id : 1
     */

    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String name;
        private int id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
