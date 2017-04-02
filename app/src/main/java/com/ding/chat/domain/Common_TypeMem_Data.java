package com.ding.chat.domain;

import java.io.Serializable;

/**
 * Created by Erick on 2016/9/5.
 */
public class Common_TypeMem_Data implements Serializable {


    /**
     * departmentName : 财务科
     * jobtitleName :
     * userRealname : 付培英
     * personnelId : JG0117
     * roleId : 2
     * departmentId : 93
     * roleName : 正式员工
     * telephone :
     * receiveStatus : 0
     * userId : 2713
     * status : 0
     */

    private String departmentName;
    private String jobtitleName;
    private String userRealname;
    private String personnelId;
    private int roleId;
    private int departmentId;
    private String roleName;
    private String telephone;
    private int receiveStatus;
    private int userId;
    private int status;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getJobtitleName() {
        return jobtitleName;
    }

    public void setJobtitleName(String jobtitleName) {
        this.jobtitleName = jobtitleName;
    }

    public String getUserRealname() {
        return userRealname;
    }

    public void setUserRealname(String userRealname) {
        this.userRealname = userRealname;
    }

    public String getPersonnelId() {
        return personnelId;
    }

    public void setPersonnelId(String personnelId) {
        this.personnelId = personnelId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(int receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
