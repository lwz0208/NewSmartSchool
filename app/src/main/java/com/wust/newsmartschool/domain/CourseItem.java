package com.wust.newsmartschool.domain;

import android.util.Log;

import java.io.Serializable;


import java.io.Serializable;

import android.util.Log;

public class CourseItem implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -2396458612058037079L;
    private String jx0501id;// 退选时需要传入的数据
    private String jx0504id;// 选课需要传入的数据id

    public String getJx0504id() {
        return jx0504id;
    }

    public void setJx0504id(String jx0504id) {
        this.jx0504id = jx0504id;
    }

    public String getJx0501id() {
        return jx0501id;
    }

    public void setJx0501id(String jx0501id) {
        this.jx0501id = jx0501id;
    }

    private String kcmc;// 课程名称
    private String kkdw;// 开课单位
    private String xf;// 学分
    private String skjs;// 授课教师
    private String skbj;// 上课班级
    private String skzc;// 上课周次
    private String sksj;// 上课时间
    private String skdd;// 上课地点
    private String xkjd;// 选中阶段
    private String kcsx;// 课程属性
    private String kcxz;// 课程性质
    private String fzm;// 分组名
    private String xbyq;// 性别要求
    private String xkbj;// 选课班级
    private String jcmc;// 教材名称
    private int kcyl;// 课程余量

    public int getKcyl() {
        return kcyl;
    }

    public void setKcyl(int kcyl) {
        if (kcyl < 0) {
            Log.e(Thread.currentThread().getStackTrace()[1].getMethodName(),
                    "kcyl<0");
            kcyl = 0;
        }
        this.kcyl = kcyl;
    }

    public String getKcmc() {
        return kcmc;
    }

    public void setKcmc(String kcmc) {
        this.kcmc = kcmc;
    }

    public String getKkdw() {
        return kkdw;
    }

    public void setKkdw(String kkdw) {
        this.kkdw = kkdw;
    }

    public String getXf() {
        return xf;
    }

    public void setXf(String xf) {
        this.xf = xf;
    }

    public String getSkjs() {
        return skjs;
    }

    public void setSkjs(String skjs) {
        this.skjs = skjs;
    }

    public String getSkbj() {
        return skbj;
    }

    public void setSkbj(String skbj) {
        this.skbj = skbj;
    }

    public String getSkzc() {
        return skzc;
    }

    public void setSkzc(String skzc) {
        this.skzc = skzc;
    }

    public String getSksj() {
        return sksj;
    }

    public void setSksj(String sksj) {
        this.sksj = sksj;
    }

    public String getSkdd() {
        return skdd;
    }

    public void setSkdd(String skdd) {
        this.skdd = skdd;
    }

    public String getXkjd() {
        return xkjd;
    }

    public void setXkjd(String xkjd) {
        this.xkjd = xkjd;
    }

    public String getKcsx() {
        return kcsx;
    }

    public void setKcsx(String kcsx) {
        this.kcsx = kcsx;
    }

    public String getKcxz() {
        return kcxz;
    }

    public void setKcxz(String kcxz) {
        this.kcxz = kcxz;
    }

    public String getFzm() {
        return fzm;
    }

    public void setFzm(String fzm) {
        this.fzm = fzm;
    }

    public String getXbyq() {
        return xbyq;
    }

    public void setXbyq(String xbyq) {
        this.xbyq = xbyq;
    }

    public String getXkbj() {
        return xkbj;
    }

    public void setXkbj(String xkbj) {
        this.xkbj = xkbj;
    }

    public String getJcmc() {
        return jcmc;
    }

    public void setJcmc(String jcmc) {
        this.jcmc = jcmc;
    }

}

