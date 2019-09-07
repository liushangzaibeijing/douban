/**
  * Copyright 2019 bejson.com 
  */
package com.xiu.crawling.douban.bean.dto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;
import java.util.Date;

/**
 * Auto-generated: 2019-09-07 14:46:10
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Songlist {

    private long id;
    private int type;
    private String mid;
    private String name;
    private String title;
    private String subtitle;
    private List<Singer> singer;
    private Album album;
    private Mv mv;
    private int interval;
    private int isonly;
    private int language;
    private int genre;
    private int index_cd;
    private int index_album;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date time_public;
    private int status;
    private int fnote;
    private File file;
    private Pay pay;
    private Action action;
    private Ksong ksong;
    private Volume volume;
    private String label;
    private String url;
    private int bpm;
    private int version;
    private String trace;
    private int data_type;
    private int modify_stamp;
    private String pingpong;
    public void setId(long id) {
         this.id = id;
     }
     public long getId() {
         return id;
     }

    public void setType(int type) {
         this.type = type;
     }
     public int getType() {
         return type;
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

    public void setSinger(List<Singer> singer) {
         this.singer = singer;
     }
     public List<Singer> getSinger() {
         return singer;
     }

    public void setAlbum(Album album) {
         this.album = album;
     }
     public Album getAlbum() {
         return album;
     }

    public void setMv(Mv mv) {
         this.mv = mv;
     }
     public Mv getMv() {
         return mv;
     }

    public void setInterval(int interval) {
         this.interval = interval;
     }
     public int getInterval() {
         return interval;
     }

    public void setIsonly(int isonly) {
         this.isonly = isonly;
     }
     public int getIsonly() {
         return isonly;
     }

    public void setLanguage(int language) {
         this.language = language;
     }
     public int getLanguage() {
         return language;
     }

    public void setGenre(int genre) {
         this.genre = genre;
     }
     public int getGenre() {
         return genre;
     }

    public void setIndex_cd(int index_cd) {
         this.index_cd = index_cd;
     }
     public int getIndex_cd() {
         return index_cd;
     }

    public void setIndex_album(int index_album) {
         this.index_album = index_album;
     }
     public int getIndex_album() {
         return index_album;
     }

    public void setTime_public(Date time_public) {
         this.time_public = time_public;
     }
     public Date getTime_public() {
         return time_public;
     }

    public void setStatus(int status) {
         this.status = status;
     }
     public int getStatus() {
         return status;
     }

    public void setFnote(int fnote) {
         this.fnote = fnote;
     }
     public int getFnote() {
         return fnote;
     }

    public void setFile(File file) {
         this.file = file;
     }
     public File getFile() {
         return file;
     }

    public void setPay(Pay pay) {
         this.pay = pay;
     }
     public Pay getPay() {
         return pay;
     }

    public void setAction(Action action) {
         this.action = action;
     }
     public Action getAction() {
         return action;
     }

    public void setKsong(Ksong ksong) {
         this.ksong = ksong;
     }
     public Ksong getKsong() {
         return ksong;
     }

    public void setVolume(Volume volume) {
         this.volume = volume;
     }
     public Volume getVolume() {
         return volume;
     }

    public void setLabel(String label) {
         this.label = label;
     }
     public String getLabel() {
         return label;
     }

    public void setUrl(String url) {
         this.url = url;
     }
     public String getUrl() {
         return url;
     }

    public void setBpm(int bpm) {
         this.bpm = bpm;
     }
     public int getBpm() {
         return bpm;
     }

    public void setVersion(int version) {
         this.version = version;
     }
     public int getVersion() {
         return version;
     }

    public void setTrace(String trace) {
         this.trace = trace;
     }
     public String getTrace() {
         return trace;
     }

    public void setData_type(int data_type) {
         this.data_type = data_type;
     }
     public int getData_type() {
         return data_type;
     }

    public void setModify_stamp(int modify_stamp) {
         this.modify_stamp = modify_stamp;
     }
     public int getModify_stamp() {
         return modify_stamp;
     }

    public void setPingpong(String pingpong) {
         this.pingpong = pingpong;
     }
     public String getPingpong() {
         return pingpong;
     }

}