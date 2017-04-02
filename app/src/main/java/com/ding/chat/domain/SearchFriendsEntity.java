package com.ding.chat.domain;

import java.util.List;

/**
 * Created by Erick on 2016/9/13.
 */
public class SearchFriendsEntity {


    /**
     * code : 1
     * data : [{"departmentName":"开发人员测试科室","jobtitleName":"","personnelId":"ws111","departmentId":90,"name":"王嵩111","telephone":"15107118210","id":2874,"status":0},{"departmentName":"开发人员测试科室","jobtitleName":"","personnelId":"ws333","departmentId":90,"name":"王嵩333","telephone":"15107118210","id":2871,"status":0},{"departmentName":"开发人员测试科室","jobtitleName":"","personnelId":"ws222","departmentId":90,"name":"王嵩222","telephone":"15671628673","id":2877,"status":0},{"departmentName":"开发人员测试科室","jobtitleName":"财务科科长","personnelId":"ws1","departmentId":90,"name":"王嵩","telephone":"15678901754","id":2897,"status":0}]
     * msg : 请求成功
     * page : 0
     * records : 0
     * total : 0
     */

    private int code;
    private String msg;
    private int page;
    private int records;
    private int total;
    /**
     * departmentName : 开发人员测试科室
     * jobtitleName :
     * personnelId : ws111
     * departmentId : 90
     * name : 王嵩111
     * telephone : 15107118210
     * id : 2874
     * status : 0
     */

    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String departmentName;
        private String jobtitleName;
        private String personnelId;
        private int departmentId;
        private String name;
        private String telephone;
        private int id;
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

        public String getPersonnelId() {
            return personnelId;
        }

        public void setPersonnelId(String personnelId) {
            this.personnelId = personnelId;
        }

        public int getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(int departmentId) {
            this.departmentId = departmentId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
