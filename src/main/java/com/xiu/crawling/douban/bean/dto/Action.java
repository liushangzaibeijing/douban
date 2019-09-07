/**
  * Copyright 2019 bejson.com 
  */
package com.xiu.crawling.douban.bean.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Auto-generated: 2019-09-07 14:46:10
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Action {

    @JsonProperty("switch")
    private long switch_id;
    private int msgid;
    private int alert;
    private long icons;
    private int msgshare;
    private int msgfav;
    private int msgdown;
    private int msgpay;
    public void setSwitch_id(long switch_id) {
         this.switch_id = switch_id;
     }
     public long getSwitch_id() {
         return switch_id;
     }

    public void setMsgid(int msgid) {
         this.msgid = msgid;
     }
     public int getMsgid() {
         return msgid;
     }

    public void setAlert(int alert) {
         this.alert = alert;
     }
     public int getAlert() {
         return alert;
     }

    public void setIcons(long icons) {
         this.icons = icons;
     }
     public long getIcons() {
         return icons;
     }

    public void setMsgshare(int msgshare) {
         this.msgshare = msgshare;
     }
     public int getMsgshare() {
         return msgshare;
     }

    public void setMsgfav(int msgfav) {
         this.msgfav = msgfav;
     }
     public int getMsgfav() {
         return msgfav;
     }

    public void setMsgdown(int msgdown) {
         this.msgdown = msgdown;
     }
     public int getMsgdown() {
         return msgdown;
     }

    public void setMsgpay(int msgpay) {
         this.msgpay = msgpay;
     }
     public int getMsgpay() {
         return msgpay;
     }

}