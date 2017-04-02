package com.ding.chat.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Erick on 2016/10/5.
 */
public class NoticeDetailEntity implements Serializable {

    /**
     * code : 1
     * data : {"Accept":"2871","send":"172","Accepted":"2871"}
     * fileList : [{"fileId":1,"createTime":"2016-09-23 18:24:19","createUserName":"系统管理员","fileType":"image/jpeg","fileName":"360wallpaper.jpg","fileRealPath":"/upload/files/20160923_062307_31530354211271.jpg","createUserId":168}]
     * msg : 请求成功
     */

    private int code;
    /**
     * Accept : 2871
     * send : 172
     * Accepted : 2871
     */

    private DataBean data;
    private String msg;
    /**
     * fileId : 1
     * createTime : 2016-09-23 18:24:19
     * createUserName : 系统管理员
     * fileType : image/jpeg
     * fileName : 360wallpaper.jpg
     * fileRealPath : /upload/files/20160923_062307_31530354211271.jpg
     * createUserId : 168
     */

    private List<FileListBean> fileList;

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

    public List<FileListBean> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileListBean> fileList) {
        this.fileList = fileList;
    }

    public static class DataBean implements Serializable {
        private String Accept;
        private String send;
        private String Accepted;

        public String getAccept() {
            return Accept;
        }

        public void setAccept(String Accept) {
            this.Accept = Accept;
        }

        public String getSend() {
            return send;
        }

        public void setSend(String send) {
            this.send = send;
        }

        public String getAccepted() {
            return Accepted;
        }

        public void setAccepted(String Accepted) {
            this.Accepted = Accepted;
        }
    }

    public static class FileListBean implements Serializable {
        private int fileId;
        private String createTime;
        private String createUserName;
        private String fileType;
        private String fileName;
        private String fileRealPath;
        private int createUserId;
        //自己加的一个字段
        private boolean fileExist;

        public boolean isFileExist() {
            return fileExist;
        }

        public void setFileExist(boolean fileExist) {
            this.fileExist = fileExist;
        }

        public int getFileId() {
            return fileId;
        }

        public void setFileId(int fileId) {
            this.fileId = fileId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCreateUserName() {
            return createUserName;
        }

        public void setCreateUserName(String createUserName) {
            this.createUserName = createUserName;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileRealPath() {
            return fileRealPath;
        }

        public void setFileRealPath(String fileRealPath) {
            this.fileRealPath = fileRealPath;
        }

        public int getCreateUserId() {
            return createUserId;
        }

        public void setCreateUserId(int createUserId) {
            this.createUserId = createUserId;
        }
    }
}
