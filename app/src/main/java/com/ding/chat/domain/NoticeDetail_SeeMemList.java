package com.ding.chat.domain;

import java.util.List;

/**
 * Created by Erick on 2016/12/3.
 */
public class NoticeDetail_SeeMemList {
    /**
     * code : 1
     * data : [{"jobtitleName":"","userRealname":"张迎萍333","roleId":2,"roleName":"正式员工","userId":"2873"},{"jobtitleName":"","userRealname":"柯一鸣333","roleId":2,"roleName":"正式员工","userId":"2869"},{"jobtitleName":"","userRealname":"王嵩333","roleId":2,"roleName":"正式员工","userId":"2871"},{"jobtitleName":"","userRealname":"光强222","roleId":2,"roleName":"正式员工","userId":"2886"},{"jobtitleName":"保卫科副科长","userRealname":"童健7","roleId":2,"roleName":"正式员工","userId":"2893"},{"jobtitleName":"","userRealname":"张远航333","roleId":2,"roleName":"正式员工","userId":"2894"}]
     * msg : 请求成功
     * page : 1
     * records : 6
     * total : 1
     */

    private int code;
    private String msg;
    private int page;
    private int records;
    private int total;
    /**
     * jobtitleName :
     * userRealname : 张迎萍333
     * roleId : 2
     * roleName : 正式员工
     * userId : 2873
     */

    private List<MenbersBean> data;

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

    public List<MenbersBean> getData() {
        return data;
    }

    public void setData(List<MenbersBean> data) {
        this.data = data;
    }

    public static class MenbersBean {
        private String jobtitleName;
        private String userRealname;
        private int roleId;
        private String roleName;
        private String userId;
        private String departmentName;

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

        public int getRoleId() {
            return roleId;
        }

        public void setRoleId(int roleId) {
            this.roleId = roleId;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
