/**
  * Copyright 2019 bejson.com 
  */
package com.xiu.crawling.douban.bean.dto;
import java.util.Date;

/**
 * Auto-generated: 2019-09-03 12:53:16
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Album {

    private long id;
    private String mid;
    private String name;
    private String title;
    private String subtitle;
    private Date time_public;
    public void setId(long id) {
         this.id = id;
     }
     public long getId() {
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

    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    public void setSubtitle(String subtitle) {
         this.subtitle = subtitle;
     }
     public String getSubtitle() {
         return subtitle;
     }

    public void setTime_public(Date time_public) {
         this.time_public = time_public;
     }
     public Date getTime_public() {
         return time_public;
     }

}