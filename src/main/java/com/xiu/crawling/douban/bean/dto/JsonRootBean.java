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
public class JsonRootBean {

    private Singer singer;
    private int code;
    private long ts;
    public void setSinger(Singer singer) {
         this.singer = singer;
     }
     public Singer getSinger() {
         return singer;
     }

    public void setCode(int code) {
         this.code = code;
     }
     public int getCode() {
         return code;
     }

    public void setTs(long ts) {
         this.ts = ts;
     }
     public long getTs() {
         return ts;
     }

}