package com.wust.newsmartschool.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Erick on 2016/11/15.
 */
public class ALLdepartmentId1 implements Serializable {
    private String typeName;
    private String typeId;
    private List<databean> data;

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

    public List<databean> getData() {
        return data;
    }

    public void setData(List<databean> data) {
        this.data = data;
    }

    public static class databean implements Serializable {
        private int id;
        private String name;
        private int pId;
        private int usersNum;
        List<databean> children;

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

        public int getUsersNum() {
            return usersNum;
        }

        public void setUsersNum(int usersNum) {
            this.usersNum = usersNum;
        }

        public List<databean> getChildren() {
            return children;
        }

        public void setChildren(List<databean> children) {
            this.children = children;
        }
    }
}
