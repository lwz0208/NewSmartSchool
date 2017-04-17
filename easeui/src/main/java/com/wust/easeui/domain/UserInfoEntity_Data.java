package com.wust.easeui.domain;

import java.io.Serializable;

public class UserInfoEntity_Data implements Serializable {


    /**
     * "studentType":1,//学生类型（0：本科  1：研究生）
     * "collegeId":1,//学院id
     * "id":"201613703031",//学号
     * "sex":"男",//性别
     * "classId":1,//班级id
     * "name":"啊啊啊",//姓名
     * "className":"软件工程",//班级名称
     * "year":2016,//年级
     * "majorName":"软件工程",//专业
     * "collegeName":"计算机学院",//学院名称
     * "majorId":1，//专业id
     * "type":0 //用户类型（0：学生，1：老师，2：其他）
     */


    private int studentType;
    private String collegeName;
    private int classId;
    private int majorId;
    private int year;
    private String sex;
    private int collegeId;
    private String name;
    private String className;
    private String id;
    private String majorName;
    private int type;

    public int getStudentType() {
        return studentType;
    }

    public void setStudentType(int studentType) {
        this.studentType = studentType;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getMajorId() {
        return majorId;
    }

    public void setMajorId(int majorId) {
        this.majorId = majorId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
