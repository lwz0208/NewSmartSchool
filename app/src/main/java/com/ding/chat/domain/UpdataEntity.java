package com.ding.chat.domain;

/**
 * Created by Erick on 2016/9/24.
 */
public class UpdataEntity {
    /**
     * code : 1
     * data : {"apkUrl":"http://222.243.141.146/qiniu-app-cdn.pgyer.com/b68082ad6ecc417e79d088d24fea9089.apk?e=1477873271&attname=moa1.0.1.apk","remark":"1、评论添加推送功能；\r\n2、任务新增优先级和综合搜索查看功能；\r\n3、任务详情里面的任务参与人点击可查看个人信息；\r\n4、个人信息中手机号码对非好友不可见；\r\n5、通知新加转发功能；\r\n6、完善聊天中的@功能。","id":3,"type":"Android","versionName":"1.0.1","versionNumber":"1.01","versionCode":2,"status":1}
     * msg : 请求成功
     */

    private int code;
    /**
     * apkUrl : http://222.243.141.146/qiniu-app-cdn.pgyer.com/b68082ad6ecc417e79d088d24fea9089.apk?e=1477873271&attname=moa1.0.1.apk
     * remark : 1、评论添加推送功能；
     * 2、任务新增优先级和综合搜索查看功能；
     * 3、任务详情里面的任务参与人点击可查看个人信息；
     * 4、个人信息中手机号码对非好友不可见；
     * 5、通知新加转发功能；
     * 6、完善聊天中的@功能。
     * id : 3
     * type : Android
     * versionName : 1.0.1
     * versionNumber : 1.01
     * versionCode : 2
     * status : 1
     */

    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        private String apkUrl;
        private String remark;
        private int id;
        private String type;
        private String versionName;
        private String versionNumber;
        private int versionCode;
        private int status;

        public String getApkUrl() {
            return apkUrl;
        }

        public void setApkUrl(String apkUrl) {
            this.apkUrl = apkUrl;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(String versionNumber) {
            this.versionNumber = versionNumber;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
