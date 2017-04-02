package com.ding.chat.domain;

import java.util.List;

/**
 * Created by Erick on 2016/12/27.
 */
public class NoticeDetail_newEntity {

    /**
     * code : 1
     * data : {"departmentName":"开发人员测试科室","createTime":"2016-12-26 21:50:36","userRealname":"周小琳","userPhone":"15527667612","departmentId":90,"userImgurl":"2916.png","informId":601,"title":"pop","send":"2916","content":"ooooo"}
     * fileList : []
     * msg : 请求成功
     */

    private int code;
    /**
     * departmentName : 开发人员测试科室
     * createTime : 2016-12-26 21:50:36
     * userRealname : 周小琳
     * userPhone : 15527667612
     * departmentId : 90
     * userImgurl : 2916.png
     * informId : 601
     * title : pop
     * send : 2916
     * content : ooooo
     */

    private NoticeEntity data;
    private String msg;
    private List<NoticeDetailEntity.FileListBean> fileList;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public NoticeEntity getData() {
        return data;
    }

    public void setData(NoticeEntity data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<NoticeDetailEntity.FileListBean> getFileList() {
        return fileList;
    }

    public void setFileList(List<NoticeDetailEntity.FileListBean> fileList) {
        this.fileList = fileList;
    }

}
