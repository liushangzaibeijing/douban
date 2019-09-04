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
public class Extras {

    private long listen_count;
    private Date upload_time;
    private int is_only;
    private int is_new;
    public void setListen_count(long listen_count) {
         this.listen_count = listen_count;
     }
     public long getListen_count() {
         return listen_count;
     }

    public void setUpload_time(Date upload_time) {
         this.upload_time = upload_time;
     }
     public Date getUpload_time() {
         return upload_time;
     }

    public void setIs_only(int is_only) {
         this.is_only = is_only;
     }
     public int getIs_only() {
         return is_only;
     }

    public void setIs_new(int is_new) {
         this.is_new = is_new;
     }
     public int getIs_new() {
         return is_new;
     }

}