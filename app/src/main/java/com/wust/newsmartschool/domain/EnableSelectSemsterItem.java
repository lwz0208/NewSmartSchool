package com.wust.newsmartschool.domain;

import java.io.Serializable;


import java.io.Serializable;

public class EnableSelectSemsterItem implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3496955760973835413L;
    // xnxq学年学期
    // xklb选课类别
    // xkjd选课阶段
    // xkkssj选课开始时间
    // xkjssj选课结束时间
    // jx0502id获取选课数据和选课时需要传入的值

    private String xnxq;
    private String xklb;
    private String xkjd;
    private String xkkssj;
    private String xkjssj;
    private String jx0502id;

    /**
     * 获取学年学期
     *
     * @return
     */
    public String getXnxq() {
        return xnxq;
    }

    /**
     * 设置学年学期
     *
     * @param xnxq
     */
    public void setXnxq(String xnxq) {
        this.xnxq = xnxq;
    }

    /**
     * 获取选课类别
     *
     * @return
     */
    public String getXklb() {
        return xklb;
    }

    public void setXklb(String xklb) {
        this.xklb = xklb;
    }

    /**
     * 获取选课阶段
     *
     * @return
     */
    public String getXkjd() {
        return xkjd;
    }

    public void setXkjd(String xkjd) {
        this.xkjd = xkjd;
    }

    /**
     * 选课开始时间
     *
     * @return
     */
    public String getXkkssj() {
        return xkkssj;
    }

    public void setXkkssj(String xkkssj) {
        this.xkkssj = xkkssj;
    }

    /**
     * 获取选课结束时间
     *
     * @return
     */
    public String getXkjssj() {
        return xkjssj;
    }

    public void setXkjssj(String xkjssj) {
        this.xkjssj = xkjssj;
    }

    /**
     * 获取选课要用的数据
     *
     * @return
     */
    public String getJx0502id() {
        return jx0502id;
    }

    public void setJx0502id(String jx0502) {
        this.jx0502id = jx0502;
    }

    public EnableSelectSemsterItem() {
        // TODO Auto-generated constructor stub
    }

}
