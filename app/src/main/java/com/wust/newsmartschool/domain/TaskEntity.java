package com.wust.newsmartschool.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Erick on 2016/10/29.
 */
public class TaskEntity implements Serializable {


    /**
     * code : 1
     * data : [{"priorityName":"十万火急","createUserId":2903,"createUserName":"张迎萍888","title":"新增任务44","type":"普通任务","createUserImgurl":"2903.png","priorityId":1,"createUserDeptId":90,"createUserDeptName":"开发人员测试科室","createUserTelephone":"15671628493","createTime":"2016-10-28","id":136,"deadline":"2016-10-30","status":0},{"priorityName":"一般","createUserId":2903,"createUserName":"张迎萍888","title":"任务测试","type":"普通任务","createUserImgurl":"2903.png","priorityId":3,"createUserDeptId":90,"createUserDeptName":"开发人员测试科室","createUserTelephone":"15671628493","createTime":"2016-10-28","id":133,"deadline":"2016-10-29","status":0},{"priorityName":"一般","createUserId":2903,"createUserName":"张迎萍888","title":"测试紧急任务","type":"普通任务","createUserImgurl":"2903.png","priorityId":3,"createUserDeptId":90,"createUserDeptName":"开发人员测试科室","createUserTelephone":"15671628493","createTime":"2016-10-28","id":132,"deadline":"2016-10-30","status":0},{"priorityName":"一般","createUserId":2903,"finishTime":"2016-10-28 21:54:29","createUserName":"张迎萍888","title":"测试任务33","type":"普通任务","createUserImgurl":"2903.png","priorityId":3,"createUserDeptId":90,"createUserDeptName":"开发人员测试科室","createUserTelephone":"15671628493","createTime":"2016-10-28","id":134,"deadline":"2016-10-28","status":1}]
     * msg : 请求成功
     * page : 1
     * records : 4
     * total : 1
     */

    private int code;
    private String msg;
    private int page;
    private int records;
    private int total;
    /**
     * priorityName : 十万火急
     * createUserId : 2903
     * createUserName : 张迎萍888
     * title : 新增任务44
     * type : 普通任务
     * createUserImgurl : 2903.png
     * priorityId : 1
     * createUserDeptId : 90
     * createUserDeptName : 开发人员测试科室
     * createUserTelephone : 15671628493
     * createTime : 2016-10-28
     * id : 136
     * deadline : 2016-10-30
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

    public static class DataBean implements Serializable {
        private String priorityName;
        private int createUserId;
        private String createUserName;
        private String title;
        private String type;
        private String createUserImgurl;
        private int priorityId;
        private int createUserDeptId;
        private String createUserDeptName;
        private String createUserTelephone;
        private String createTime;
        private int id;
        private String deadline;
        private int status;

        public String getPriorityName() {
            return priorityName;
        }

        public void setPriorityName(String priorityName) {
            this.priorityName = priorityName;
        }

        public int getCreateUserId() {
            return createUserId;
        }

        public void setCreateUserId(int createUserId) {
            this.createUserId = createUserId;
        }

        public String getCreateUserName() {
            return createUserName;
        }

        public void setCreateUserName(String createUserName) {
            this.createUserName = createUserName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreateUserImgurl() {
            return createUserImgurl;
        }

        public void setCreateUserImgurl(String createUserImgurl) {
            this.createUserImgurl = createUserImgurl;
        }

        public int getPriorityId() {
            return priorityId;
        }

        public void setPriorityId(int priorityId) {
            this.priorityId = priorityId;
        }

        public int getCreateUserDeptId() {
            return createUserDeptId;
        }

        public void setCreateUserDeptId(int createUserDeptId) {
            this.createUserDeptId = createUserDeptId;
        }

        public String getCreateUserDeptName() {
            return createUserDeptName;
        }

        public void setCreateUserDeptName(String createUserDeptName) {
            this.createUserDeptName = createUserDeptName;
        }

        public String getCreateUserTelephone() {
            return createUserTelephone;
        }

        public void setCreateUserTelephone(String createUserTelephone) {
            this.createUserTelephone = createUserTelephone;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDeadline() {
            return deadline;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
