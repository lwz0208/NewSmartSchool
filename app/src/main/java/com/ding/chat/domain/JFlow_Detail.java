package com.ding.chat.domain;

import java.io.Serializable;

public class JFlow_Detail implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    String content;
    String create_time;
    String user_name;
    String title;
    String topic_title;

    public String getTopic_title() {
        return topic_title;
    }

    public void setTopic_title(String topic_title) {
        this.topic_title = topic_title;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
