package com.wust.newsmartschool.domain;

import java.io.Serializable;
import java.util.List;

public class UserInfoEntity_Data implements Serializable {
    /**
     * departmentName : 开发人员测试科室
     * roleId : 2
     * departmentId : 90
     * userNickname :
     * telephone : 15107118210
     * userId : 2867
     * partyBranchId : 0
     * workNumber :
     * userRealname : 王嵩222
     * personnelId : ws222
     * positionalTitleId : 16
     * userImgurl : 2867.png
     * userGender : 2
     * jobTitleIds : [{"classifyId":5,"name":"产科主任","id":6}]
     * partHead : 0
     * email :
     */

    private String departmentName;
    private int roleId;
    private String roleName;
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
    private int receiveStatus;

    /**
     * classifyId : 5
     * name : 产科主任
     * id : 6
     */


    private List<JobTitleIdsBean> jobTitleIds;

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

    public List<JobTitleIdsBean> getJobTitleIds() {
        return jobTitleIds;
    }

    public void setJobTitleIds(List<JobTitleIdsBean> jobTitleIds) {
        this.jobTitleIds = jobTitleIds;
    }

    public int getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(int receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public static class JobTitleIdsBean implements Serializable {
        private int classifyId;
        private String name;
        private int id;

        public int getClassifyId() {
            return classifyId;
        }

        public void setClassifyId(int classifyId) {
            this.classifyId = classifyId;
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
    }
}
