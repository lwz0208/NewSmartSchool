package com.ding.chat.domain;

import java.io.Serializable;

/**
 * Created by Erick on 2016/10/6.
 */
public class MeetingFilesEntity implements Serializable {

    /**
     * path : /upload/files/20161006_074202_691269767397338.png
     * name : v5.png
     * id : 177
     * type : image/png
     */

    private String path;
    private String name;
    private int id;
    private String type;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
