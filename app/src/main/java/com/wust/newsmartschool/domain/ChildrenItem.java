/**
 * @Title: ItemBean.java
 * @Package com.example.test
 * @Description: TODO(��һ�仰�������ļ���ʲô)
 * @date 2014-6-25 ����9:45:29
 */
package com.wust.newsmartschool.domain;

import java.io.Serializable;

public class ChildrenItem implements Serializable {

    private String id;
    private String name;
    private String jobtitleName;
    private String departmentName;
    private int roleId;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getJobtitleName() {
        return jobtitleName;
    }

    public void setJobtitleName(String jobtitleName) {
        this.jobtitleName = jobtitleName;
    }

    public ChildrenItem() {
    }

    public ChildrenItem(String id, String name) {
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
