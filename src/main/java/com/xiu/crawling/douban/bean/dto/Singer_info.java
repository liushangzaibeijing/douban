/**
  * Copyright 2019 bejson.com 
  */
package com.xiu.crawling.douban.bean.dto;

/**
 * Auto-generated: 2019-09-03 12:53:16
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Singer_info {

    private int area;
    private int attribute4;
    private int genre;
    private String other_name;
    /**
     * 歌手唯一标识
     */
    private int id;
    /**
     * 歌手mid(业务相关的标识)
     */
    private String mid;
    private String name;
    private long fans;
    public void setArea(int area) {
         this.area = area;
     }
     public int getArea() {
         return area;
     }

    public void setAttribute4(int attribute4) {
         this.attribute4 = attribute4;
     }
     public int getAttribute4() {
         return attribute4;
     }

    public void setGenre(int genre) {
         this.genre = genre;
     }
     public int getGenre() {
         return genre;
     }

    public void setOther_name(String other_name) {
         this.other_name = other_name;
     }
     public String getOther_name() {
         return other_name;
     }

    public void setId(int id) {
         this.id = id;
     }
     public int getId() {
         return id;
     }

    public void setMid(String mid) {
         this.mid = mid;
     }
     public String getMid() {
         return mid;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setFans(long fans) {
         this.fans = fans;
     }
     public long getFans() {
         return fans;
     }

}