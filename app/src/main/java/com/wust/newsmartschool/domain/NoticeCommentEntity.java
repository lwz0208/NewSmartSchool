package com.wust.newsmartschool.domain;

import java.io.Serializable;
import java.util.List;

public class NoticeCommentEntity implements Serializable {

    /**
     * code : 1
     * data : [{"userRealname":"张迎萍333","createTime":"2016-12-31 21:15:27","commentId":440,"toUserName":"","pid":0,"userId":2873,"toUserId":0,"content":"评论"},{"userRealname":"张迎萍333","createTime":"2016-12-31 21:40:23","commentId":441,"toUserName":"","pid":0,"userId":2873,"toUserId":0,"content":"回复: 哈哈测试回复评论"},{"userRealname":"张迎萍333","createTime":"2016-12-31 22:29:24","commentId":442,"toUserName":"","pid":0,"userId":2873,"toUserId":0,"content":"回复: 测试评论"},{"userRealname":"张迎萍333","createTime":"2016-12-31 22:30:36","commentId":443,"toUserName":"","pid":0,"userId":2873,"toUserId":0,"content":"回复评论"}]
     * msg : 请求成功
     */

    private int code;
    private String msg;
    /**
     * userRealname : 张迎萍333
     * createTime : 2016-12-31 21:15:27
     * commentId : 440
     * toUserName :
     * pid : 0
     * userId : 2873
     * toUserId : 0
     * content : 评论
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private String userRealname;
        private String createTime;
        private int commentId;
        private String toUserName;
        private int pid;
        private int userId;
        private int toUserId;
        private String content;

        public String getUserRealname() {
            return userRealname;
        }

        public void setUserRealname(String userRealname) {
            this.userRealname = userRealname;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getCommentId() {
            return commentId;
        }

        public void setCommentId(int commentId) {
            this.commentId = commentId;
        }

        public String getToUserName() {
            return toUserName;
        }

        public void setToUserName(String toUserName) {
            this.toUserName = toUserName;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getToUserId() {
            return toUserId;
        }

        public void setToUserId(int toUserId) {
            this.toUserId = toUserId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
