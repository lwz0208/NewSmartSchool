package com.ding.chat.domain;

import java.io.Serializable;

public class Company_Dep implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2497397975786444583L;
    /**
     *
     */
    String deptName;
    int level;
    int usersNum;
    String members;
    int deptId;
    int superId;

    public String getDepName() {
        return deptName;
    }

    public void setDepName(String depName) {
        this.deptName = depName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getUsersNum() {
        return usersNum;
    }

    public void setUsersNum(int usersNum) {
        this.usersNum = usersNum;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public int getSuperId() {
        return superId;
    }

    public void setSuperId(int superId) {
        this.superId = superId;
    }

}
