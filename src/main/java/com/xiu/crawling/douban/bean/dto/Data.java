/**
  * Copyright 2019 bejson.com 
  */
package com.xiu.crawling.douban.bean.dto;
import java.util.List;

/**
 * Auto-generated: 2019-09-03 12:53:16
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Data {

    /**
     * 歌曲列表
     */
    private List<Songlist> songlist;
    /**
     * 歌手信息
     */
    private Singer_info singer_info;
    /**
     * 歌手简介
     */
    private String singer_brief;
    private List<String> music_grp;
    /**
     * 所有专辑数
     */

    private int total_album;
    /**
     * 所有mv
     */
    private int total_mv;
    /**
     * 总歌曲数
     */
    private int total_song;
    /**
     * 音乐人
     */
    private String yinyueren;
    /**
     * 是否显示歌手描述
     */
    private boolean show_singer_desc;
    /**
     * 每一个歌曲的播放量信息
     */
    private List<Extras> extras;
    public void setSonglist(List<Songlist> songlist) {
         this.songlist = songlist;
     }
     public List<Songlist> getSonglist() {
         return songlist;
     }

    public void setSinger_info(Singer_info singer_info) {
         this.singer_info = singer_info;
     }
     public Singer_info getSinger_info() {
         return singer_info;
     }

    public void setSinger_brief(String singer_brief) {
         this.singer_brief = singer_brief;
     }
     public String getSinger_brief() {
         return singer_brief;
     }

    public void setMusic_grp(List<String> music_grp) {
         this.music_grp = music_grp;
     }
     public List<String> getMusic_grp() {
         return music_grp;
     }

    public void setTotal_album(int total_album) {
         this.total_album = total_album;
     }
     public int getTotal_album() {
         return total_album;
     }

    public void setTotal_mv(int total_mv) {
         this.total_mv = total_mv;
     }
     public int getTotal_mv() {
         return total_mv;
     }

    public void setTotal_song(int total_song) {
         this.total_song = total_song;
     }
     public int getTotal_song() {
         return total_song;
     }

    public void setYinyueren(String yinyueren) {
         this.yinyueren = yinyueren;
     }
     public String getYinyueren() {
         return yinyueren;
     }

    public void setShow_singer_desc(boolean show_singer_desc) {
         this.show_singer_desc = show_singer_desc;
     }
     public boolean getShow_singer_desc() {
         return show_singer_desc;
     }

    public void setExtras(List<Extras> extras) {
         this.extras = extras;
     }
     public List<Extras> getExtras() {
         return extras;
     }

}