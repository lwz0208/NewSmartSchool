package com.wust.newsmartschool.domain;

import java.io.Serializable;

public class DeptArchTree_UserInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * departmentName : 急诊科
     * jobtitleName : 急诊科副主任
     * roleId : 2
     * departmentId : 111
     * userNickname :
     * telephone :
     * userId : 2431
     * partyBranchId : 4
     * workNumber : 0498,2498
     * userRealname : 郑先念
     * personnelId : ZJA0160
     * positionalTitleId : 48
     * userImgurl :
     * userGender : 2
     * partHead : 0
     * email :
     * status : 0
     */

    private String departmentName;
    private String jobtitleName;
    private int roleId;
    private int departmentId;
    private String userNickname;
    private String telephone;
    private int userId;
    private int partyBranchId;
    private String workNumber;
    private String userRealname;
    private String personnelId;
    private int positionalTitleId;
    private String userImgurl;
    private int userGender;
    private int partHead;
    private String email;
    private int status;


    public static long getSerialversionuid() {
        return serialVersionUID;
    }

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

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPartyBranchId() {
        return partyBranchId;
    }

    public void setPartyBranchId(int partyBranchId) {
        this.partyBranchId = partyBranchId;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
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

    public int getPositionalTitleId() {
        return positionalTitleId;
    }

    public void setPositionalTitleId(int positionalTitleId) {
        this.positionalTitleId = positionalTitleId;
    }

    public String getUserImgurl() {
        return userImgurl;
    }

    public void setUserImgurl(String userImgurl) {
        this.userImgurl = userImgurl;
    }

    public int getUserGender() {
        return userGender;
    }

    public void setUserGender(int userGender) {
        this.userGender = userGender;
    }

    public int getPartHead() {
        return partHead;
    }

    public void setPartHead(int partHead) {
        this.partHead = partHead;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
