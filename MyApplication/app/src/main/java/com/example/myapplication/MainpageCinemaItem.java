package com.example.myapplication;

import java.math.BigDecimal;

public class MainpageCinemaItem {
    //主页面的电影信息类别

    private Integer id;
    private String name;
    private String picUrl;//图片资源
    private BigDecimal grade;//评分

    public MainpageCinemaItem() {
    }

    public MainpageCinemaItem(Integer id, String name, String picUrl, BigDecimal grade) {
        this.id = id;
        this.name = name;
        this.picUrl = picUrl;
        this.grade = grade;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }
}