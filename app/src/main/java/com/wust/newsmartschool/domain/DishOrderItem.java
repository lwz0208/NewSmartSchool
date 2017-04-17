package com.wust.newsmartschool.domain;

import java.util.List;


import java.util.List;


/**
 * This bean saves all you need of a order.
 * @author yorek
 */
public class DishOrderItem {
    String orderId;
    String arriveTime;
    String address;
    String userName;
    String userTel;
    List<DishItem> dishes;
    String acceptTime;
    String userSummitTime;
    private String totalMoney;
    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getUserSummitTime() {
        return userSummitTime;
    }

    public void setUserSummitTime(String userSummitTime) {
        this.userSummitTime = userSummitTime;
    }



    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public List<DishItem> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishItem> dishes) {
        this.dishes = dishes;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public static class DishItem {
        String dishName;
        String dishNum;

        public String getDishName() {
            return dishName;
        }

        public void setDishName(String dishName) {
            this.dishName = dishName;
        }

        public String getDishNum() {
            return dishNum + "份";
        }

        public void setDishNum(String dishNum) {
            this.dishNum = dishNum;
        }
    }
}

