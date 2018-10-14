package com.xiu.crawling.douban.bean;

import java.util.Date;

public class Book {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 书名
     */
    private String name;

    /**
     * 作者
     */
    private String author;

    /**
     * 译者
     */
    private String translator;

    /**
     * 出版社
     */
    private String publisHouse;

    /**
     * 出版日期
     */
    private Date publicationDate;

    /**
     * 价格
     */
    private Double price;

    /**
     * 得分
     */
    private Double score;

    /**
     * 评价人数
     */
    private Integer evaluateNumber;

    /**
     * 图片
     */
    private String picture;

    /**
     * 描述
     */
    private String descption;

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
        this.name = name == null ? null : name.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator == null ? null : translator.trim();
    }

    public String getPublisHouse() {
        return publisHouse;
    }

    public void setPublisHouse(String publisHouse) {
        this.publisHouse = publisHouse == null ? null : publisHouse.trim();
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getEvaluateNumber() {
        return evaluateNumber;
    }

    public void setEvaluateNumber(Integer evaluateNumber) {
        this.evaluateNumber = evaluateNumber;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture == null ? null : picture.trim();
    }

    public String getDescption() {
        return descption;
    }

    public void setDescption(String descption) {
        this.descption = descption == null ? null : descption.trim();
    }
}