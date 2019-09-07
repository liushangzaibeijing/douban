package com.xiu.crawling.douban.bean;

import java.util.Date;

/**
 * 专辑
 * 
 * @author wcyong
 * 
 * @date 2019-09-06
 */
public class Album {
    /**
     * 主键
     */
    private Integer id;

    /**
     * qq音乐主键
     */
    private Integer albumId;

    /**
     * qq音乐的mid获取专辑信息的相关
     */
    private String albumMid;

    /**
     * 专辑名
     */
    private String albumName;

    /**
     * 专辑类型
     */
    private String albumType;

    /**
     * 专辑图片
     */
    private String albumPic;

    /**
     * 发行时间
     */
    private Date pubTime;

    /**
     * 唱片公司
     */
    private String companyName;

    /**
     * 专辑所属歌手 多个歌手mid
     */
    private String signerMid;

    /**
     * 专辑简介
     */
    private String desc;

    /**
     * 语言 语种
     */
    private String score;

    /**
     * 得分
     */
    private String lan;

    /**
     * 流派
     */
    private String school;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public String getAlbumMid() {
        return albumMid;
    }

    public void setAlbumMid(String albumMid) {
        this.albumMid = albumMid == null ? null : albumMid.trim();
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName == null ? null : albumName.trim();
    }

    public String getAlbumType() {
        return albumType;
    }

    public void setAlbumType(String albumType) {
        this.albumType = albumType == null ? null : albumType.trim();
    }

    public String getAlbumPic() {
        return albumPic;
    }

    public void setAlbumPic(String albumPic) {
        this.albumPic = albumPic == null ? null : albumPic.trim();
    }

    public Date getPubTime() {
        return pubTime;
    }

    public void setPubTime(Date pubTime) {
        this.pubTime = pubTime;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getSignerMid() {
        return signerMid;
    }

    public void setSignerMid(String signerMid) {
        this.signerMid = signerMid == null ? null : signerMid.trim();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score == null ? null : score.trim();
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan == null ? null : lan.trim();
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school == null ? null : school.trim();
    }
}