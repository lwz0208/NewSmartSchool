package com.wust.newsmartschool.domain;

/**
 *@Description:
 *@Author: Effall
 *@Date: 2014年8月26日
 */

public class BuildingItem {
    public String jzwid;//教学楼id
    public String jzwmc;//教学楼名
    public BuildingItem(){
        jzwid="";
        jzwmc="";
    }
    public  BuildingItem(String jzwid,String jzwmc){
        this.jzwid=jzwid;
        this.jzwmc=jzwmc;
    }
    public String toString(){
        return jzwmc;
    }
    public String getJzwid(){
        return jzwid;
    }
    public String getJzwmc(){
        return jzwmc;
    }
    public void setJzwid(String jzwid) {
        this.jzwid = jzwid;
    }
    public void setJzwmc(String jzwmc) {
        this.jzwmc = jzwmc;
    }

}

