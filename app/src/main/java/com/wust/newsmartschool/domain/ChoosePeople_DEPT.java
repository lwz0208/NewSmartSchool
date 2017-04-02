package com.wust.newsmartschool.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Erick on 2016/9/18.
 */
public class ChoosePeople_DEPT implements Serializable {
    String typeName;
    String typeId;

    List<Data> data;

    public static class Data implements Serializable {
        int id;
        String name;
        int pId;
        List<Data> children;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getpId() {
            return pId;
        }

        public void setpId(int pId) {
            this.pId = pId;
        }

        public List<Data> getChildren() {
            return children;
        }

        public void setChildren(List<Data> children) {
            this.children = children;
        }
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
