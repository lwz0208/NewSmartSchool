package com.wust.newsmartschool.domain;

/**
 * Created by Erick on 2016/12/23.
 */
public class UndoData {

    /**
     * createTime : 2016-12-26 19:22:56
     * id : 592
     * title : 嘿嘿嘿
     * type : inform
     * content : 嘿嘿嘿
     */

    private String createTime;
    private int id;
    private String title;
    private String type;
    private String content;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
