package com.wust.newsmartschool.domain;

import java.io.Serializable;

public class MyJFlowEntity_IApply implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    String title;
    String nodename;
    String flowname;
    String workid;
    String deptname;
    String fk_flow;
    String fk_node;
    String startername;
    String starter;
    String rdt;
    int fid;
    int wfstate;

    public String getRdt() {
        return rdt;
    }

    public void setRdt(String rdt) {
        this.rdt = rdt;
    }

    public String getStarter() {
        return starter;
    }

    public void setStarter(String starter) {
        this.starter = starter;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getStartername() {
        return startername;
    }

    public void setStartername(String startername) {
        this.startername = startername;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public int getWfstate() {
        return wfstate;
    }

    public void setWfstate(int wfstate) {
        this.wfstate = wfstate;
    }

    public String getFk_node() {
        return fk_node;
    }

    public void setFk_node(String fk_node) {
        this.fk_node = fk_node;
    }

    public String getFk_flow() {
        return fk_flow;
    }

    public void setFk_flow(String fk_flow) {
        this.fk_flow = fk_flow;
    }

    public String getWorkid() {
        return workid;
    }

    public void setWorkid(String workid) {
        this.workid = workid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNodename() {
        return nodename;
    }

    public void setNodename(String nodename) {
        this.nodename = nodename;
    }

    public String getFlowname() {
        return flowname;
    }

    public void setFlowname(String flowname) {
        this.flowname = flowname;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
