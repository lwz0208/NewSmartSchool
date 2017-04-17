package com.wust.newsmartschool.domain;

import java.io.Serializable;


import java.io.Serializable;

/**
 *@Description:
 *@Author: Effall
 *@Date: 2014年11月12日
 */

public class BookItem implements Serializable {
    private String redr_cert_id;//读者条码号
    private String name;//姓名
    private String m_title;//书名
    private String prop_no_f;//图书条码号
    private String call_no;//图书索书号
    private String m_author;//作者
    private String lend_date;//借书日期
    private String norm_ret_date;//应还日期
    private String ret_date;//实际还书日期
    private String m_publisher;//出版社
    private String location_name;//馆藏地名称
    private String location_dept;//馆藏地所在校区名称
    private String location;//馆藏地编码
    private String book_lend_flag;//图书借阅标识： 0可借 1不可借
    private String M_isbn;

    public String getM_isbn() {
        return M_isbn;
    }
    public void setM_isbn(String m_isbn) {
        M_isbn = m_isbn;
    }
    public String getRedr_cert_id() {
        return redr_cert_id;
    }
    public void setRedr_cert_id(String redr_cert_id) {
        this.redr_cert_id = redr_cert_id;
    }
    public String getM_title() {
        return m_title;
    }
    public void setM_title(String m_title) {
        this.m_title = m_title;
    }
    public String getProp_no_f() {
        return prop_no_f;
    }
    public void setProp_no_f(String prop_no_f) {
        this.prop_no_f = prop_no_f;
    }
    public String getCall_no() {
        return call_no;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCall_no(String call_no) {
        this.call_no = call_no;
    }
    public String getM_author() {
        return m_author;
    }
    public void setM_author(String m_author) {
        this.m_author = m_author;
    }
    public String getLend_date() {
        return lend_date;
    }
    public void setLend_date(String lend_date) {
        this.lend_date = lend_date;
    }
    public String getNorm_ret_date() {
        return norm_ret_date;
    }
    public void setNorm_ret_date(String norm_ret_date) {
        this.norm_ret_date = norm_ret_date;
    }
    public String getRet_date() {
        return ret_date;
    }
    public void setRet_date(String ret_date) {
        this.ret_date = ret_date;
    }
    public String getM_publisher() {
        return m_publisher;
    }
    public void setM_publisher(String m_publisher) {
        this.m_publisher = m_publisher;
    }
    public String getLocation_name() {
        return location_name;
    }
    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }
    public String getLocation_dept() {
        return location_dept;
    }
    public void setLocation_dept(String location_dept) {
        this.location_dept = location_dept;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getBook_lend_flag() {
        return book_lend_flag;
    }
    public void setBook_lend_flag(String book_lend_flag) {
        this.book_lend_flag = book_lend_flag;
    }



}

