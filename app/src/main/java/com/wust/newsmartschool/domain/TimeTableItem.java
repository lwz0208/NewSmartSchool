package com.wust.newsmartschool.domain;

import java.io.Serializable;


import java.io.Serializable;

/**
 *@Description: 课表数据
 *@Author: Effall
 *@Date: 2014年10月25日
 */

public class TimeTableItem implements Serializable {

    private String bjmc;//合班名称
    private String kcmc;//课程名称
    private String jsxm;//教师姓名
    private String kcsj;//上课时间
    private String kkzc;//上课周次
    private String jsmc;//教室
    private String jxl;//教学楼
    public String getBjmc() {
        return bjmc;
    }
    public void setBjmc(String bjmc) {
        this.bjmc = bjmc;
    }
    public String getKcmc() {
        return kcmc;
    }
    public void setKcmc(String kcmc) {
        this.kcmc = kcmc;
    }
    public String getJsxm() {
        return jsxm;
    }
    public void setJsxm(String jsxm) {
        this.jsxm = jsxm;
    }
    public String getKcsj() {
        return kcsj;
    }
    public void setKcsj(String kcsj) {
        this.kcsj = kcsj;
    }
    public String getKkzc() {
        return kkzc;
    }
    public void setKkzc(String kkzc) {
        this.kkzc = kkzc;
    }
    public String getJsmc() {
        return jsmc;
    }
    public void setJsmc(String jsmc) {
        this.jsmc = jsmc;
    }
    public String getJxl() {
        return jxl;
    }
    public void setJxl(String jxl) {
        this.jxl = jxl;
    }



}

