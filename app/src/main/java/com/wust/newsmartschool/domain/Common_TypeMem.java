package com.wust.newsmartschool.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Erick on 2016/9/5.
 */
public class Common_TypeMem implements Serializable {


    /**
     * code : 1
     * data : [{"name":"赵日天","id":"201213137182"},{"name":"周小琳","id":"201213138004"},{"name":"王蔓子","id":"201313137038"},{"name":"杨璐","id":"201313138045"},{"name":"叶良晨","id":"201513704092"},{"name":"李行洲","id":"201613703031"}]
     * msg : 操作成功
     */

    private int code;
    private String msg;
    /**
     * name : 赵日天
     * id : 201213137182
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
        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
