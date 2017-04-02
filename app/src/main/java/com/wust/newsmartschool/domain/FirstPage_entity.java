package com.wust.newsmartschool.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Li Wenzhao on 2017/2/28.
 */
public class FirstPage_entity implements Serializable {
    /**
     * subMenu : [{"name":"医院通知","alias":"","pid":1,"id":22,"sort":1,"event":"inform_unread","userId":168,"status":1},{"name":"我的任务","alias":"待我完成的任务","pid":1,"id":23,"sort":2,"event":"task_by_me_unfinish","userId":168,"status":1},{"name":"发出任务","alias":"我发出的未完成任务","pid":1,"id":24,"sort":3,"event":"task_by_other_unfinish","userId":168,"status":1},{"name":"待我审批","alias":"流程-待我审批","pid":1,"id":25,"sort":4,"event":"jflow_for_my_approval","userId":168,"status":1},{"name":"我的申请","alias":"流程-我的申请","pid":1,"id":26,"sort":5,"event":"jflow_my_apply_unfinish","userId":168,"status":1}]
     * id : 1
     */

    private String id;
    /**
     * name : 医院通知
     * alias :
     * pid : 1
     * id : 22
     * sort : 1
     * event : inform_unread
     * userId : 168
     * status : 1
     */

    private List<SubMenuBean> subMenu;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SubMenuBean> getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(List<SubMenuBean> subMenu) {
        this.subMenu = subMenu;
    }

    public static class SubMenuBean implements Serializable {
        private String name;
        private String alias;
        private int pid;
        private int id;
        private int sort;
        private String event;
        private int userId;
        private String url;
        private int status;
        private String picture;

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
