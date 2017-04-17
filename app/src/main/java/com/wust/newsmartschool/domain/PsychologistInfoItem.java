package com.wust.newsmartschool.domain;


public class PsychologistInfoItem {

    private String id;
    private String name;//
    private String introduction;
    private String imgurl;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIntroduction() {
        return introduction;
    }
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
    public String getImgurl() {
        return imgurl;
    }
    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
    public PsychologistInfoItem(){

    }
    public PsychologistInfoItem(String id, String name, String introduction,
                                String imgurl) {
        super();
        this.id = id;
        this.name = name;
        this.introduction = introduction;
        this.imgurl = imgurl;
    }


}

