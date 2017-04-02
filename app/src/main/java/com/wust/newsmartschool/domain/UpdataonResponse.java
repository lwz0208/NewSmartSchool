package com.wust.newsmartschool.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Erick on 2016/10/6.
 */
public class UpdataonResponse implements Serializable {

    /**
     * code : 1
     * data : {"filesList":[{"fileTrueName":"20161006_071941_689928971649227.png","fileName":"v5.png","realPath":"/upload/files/20161006_071941_689928971649227.png","fileId":175}],"filesIds":",175,"}
     * msg : ????
     */

    private int code;
    /**
     * filesList : [{"fileTrueName":"20161006_071941_689928971649227.png","fileName":"v5.png","realPath":"/upload/files/20161006_071941_689928971649227.png","fileId":175}]
     * filesIds : ,175,
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
        private String filesIds;
        /**
         * fileTrueName : 20161006_071941_689928971649227.png
         * fileName : v5.png
         * realPath : /upload/files/20161006_071941_689928971649227.png
         * fileId : 175
         */

        private List<FilesListBean> filesList;

        public String getFilesIds() {
            return filesIds;
        }

        public void setFilesIds(String filesIds) {
            this.filesIds = filesIds;
        }

        public List<FilesListBean> getFilesList() {
            return filesList;
        }

        public void setFilesList(List<FilesListBean> filesList) {
            this.filesList = filesList;
        }

        public static class FilesListBean {
            private String fileTrueName;
            private String fileName;
            private String realPath;
            private int fileId;

            public String getFileTrueName() {
                return fileTrueName;
            }

            public void setFileTrueName(String fileTrueName) {
                this.fileTrueName = fileTrueName;
            }

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            public String getRealPath() {
                return realPath;
            }

            public void setRealPath(String realPath) {
                this.realPath = realPath;
            }

            public int getFileId() {
                return fileId;
            }

            public void setFileId(int fileId) {
                this.fileId = fileId;
            }
        }
    }
}
