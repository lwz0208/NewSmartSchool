package com.ding.chat.domain;

import java.io.Serializable;

/**
 * Created by Erick on 2016/10/7.
 */
public class UnreadMsgNum implements Serializable {


    /**
     * code : 1
     * data : {"inform":2,"task":2,"num":16,"type":"all","jflow":12,"meeting":0}
     * msg : 请求成功
     */

    private int code;
    /**
     * inform : 2
     * task : 2
     * num : 16
     * type : all
     * jflow : 12
     * meeting : 0
     */

    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean implements Serializable {
        private int inform;
        private int task;
        private int num;
        private String type;
        private int jflow;
        private int meeting;

        public int getInform() {
            return inform;
        }

        public void setInform(int inform) {
            this.inform = inform;
        }

        public int getTask() {
            return task;
        }

        public void setTask(int task) {
            this.task = task;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getJflow() {
            return jflow;
        }

        public void setJflow(int jflow) {
            this.jflow = jflow;
        }

        public int getMeeting() {
            return meeting;
        }

        public void setMeeting(int meeting) {
            this.meeting = meeting;
        }
    }
}
