package com.wust.newsmartschool.domain;


public class IM_DepartMent_Item
{
    private String CardNumber;//部门工号
    private String DepartmentName;//部门名称
    private String Content;//部门简介
    public IM_DepartMent_Item()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    public IM_DepartMent_Item(String cardNumber, String departmentName,
                              String content)
    {
        super();
        CardNumber = cardNumber;
        DepartmentName = departmentName;
        Content = content;
    }
    public String getCardNumber()
    {
        return CardNumber;
    }
    public void setCardNumber(String cardNumber)
    {
        CardNumber = cardNumber;
    }
    public String getDepartmentName()
    {
        return DepartmentName;
    }
    public void setDepartmentName(String departmentName)
    {
        DepartmentName = departmentName;
    }
    public String getContent()
    {
        return Content;
    }
    public void setContent(String content)
    {
        Content = content;
    }
    @Override
    public String toString()
    {
        return "IM_DepartMent_Item [CardNumber=" + CardNumber
                + ", DepartmentName=" + DepartmentName + ", Content=" + Content
                + "]";
    }



}

