/**
  * Copyright 2019 bejson.com 
  */
package com.xiu.crawling.douban.bean.dto;
import java.util.List;

/**
 * Auto-generated: 2019-09-03 20:5:10
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Tags {

    private List<Tag> area;
    private List<Tag> genre;
    private List<Tag> index;
    private List<Tag> sex;
    public void setArea(List<Tag> area) {
         this.area = area;
     }
     public List<Tag> getArea() {
         return area;
     }

    public List<Tag> getGenre() {
        return genre;
    }

    public void setGenre(List<Tag> genre) {
        this.genre = genre;
    }

    public List<Tag> getIndex() {
        return index;
    }

    public void setIndex(List<Tag> index) {
        this.index = index;
    }

    public List<Tag> getSex() {
        return sex;
    }

    public void setSex(List<Tag> sex) {
        this.sex = sex;
    }
}