/**
 * @Title: ItemBean.java
 * @Package com.example.test
 * @Description: TODO(��һ�仰�������ļ���ʲô)
 * @date 2014-6-25 ����9:45:29
 */
package com.ding.chat.domain;

import java.io.Serializable;

public class Groupnameid implements Serializable {

    private String id;
    private String name;

    public Groupnameid() {
    }

    public Groupnameid(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
