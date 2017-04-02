package com.wust.newsmartschool.domain;

/**
 * Created by Erick on 2016/10/11.
 */
public class ChildTaskEntitiy {

    /**
     * id : 158
     * title : 哈哈测试自任务
     * deadline : 2016-10-11
     * type : 普通任务
     * status : 1
     */

    private int id;
    private String title;
    private String deadline;
    private String type;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
