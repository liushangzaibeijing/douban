/**
  * Copyright 2019 bejson.com 
  */
package com.xiu.crawling.douban.bean.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Auto-generated: 2019-09-07 14:46:10
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Extras {

    private long listen_count;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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