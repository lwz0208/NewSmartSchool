package com.wust.newsmartschool.domain;

import java.io.Serializable;
import java.util.List;

public class DeptArchTree implements Serializable {

    private static final long serialVersionUID = 1L;
    int deptId;
    String deptName;
    int pId;
    int usersNum;
    List<DeptArchTree_UserInfo> usersInfo;
    List<DeptArchTree> children;

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
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

    public List<DeptArchTree_UserInfo> getUsersInfo() {
        return usersInfo;
    }

    public void setUsersInfo(List<DeptArchTree_UserInfo> usersInfo) {
        this.usersInfo = usersInfo;
    }

    public List<DeptArchTree> getChildren() {
        return children;
    }

    public void setChildren(List<DeptArchTree> children) {
        this.children = children;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
