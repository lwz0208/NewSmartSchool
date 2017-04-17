package com.wust.newsmartschool.domain;

import java.util.ArrayList;


import java.util.ArrayList;


public class ClassroomItem {
    //	public String xqid;//校区id
//	public String building;//教学楼
//	public String term;//学期
//	public String date;//日期
//	public String week;//周几
    public ArrayList<BuildingItem> buildings;
    public ArrayList<String> jcList;
    public ArrayList<String> classrooms;
    public ArrayList<DateItem> dateList;

    public ArrayList<BuildingItem> getBuildings() {
        return buildings;
    }
    public void setBuildings(ArrayList<BuildingItem> buildings) {
        this.buildings = buildings;
    }
    public ArrayList<String> getJcList() {
        return jcList;
    }
    public void setJcList(ArrayList<String> jcList) {
        this.jcList = jcList;
    }


}

