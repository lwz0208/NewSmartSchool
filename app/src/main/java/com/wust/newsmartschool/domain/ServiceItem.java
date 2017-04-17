package com.wust.newsmartschool.domain;


public class ServiceItem {
    private String serviceName;
    private String serviceImgUrl;
    private int serviceImgDrawable;
    private String description;

    public String getServiceName() {
        return serviceName;
    }
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    public String getServiceImgUrl() {
        return serviceImgUrl;
    }
    public void setServiceImgUrl(String serviceImgUrl) {
        this.serviceImgUrl = serviceImgUrl;
    }
    public int getServiceImgDrawable() {
        return serviceImgDrawable;
    }
    public void setServiceImgDrawable(int serviceImgDrawable) {
        this.serviceImgDrawable = serviceImgDrawable;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}

