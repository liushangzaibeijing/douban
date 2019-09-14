package com.xiu.crawling.douban.bean;

public class Song {
    private Integer id;

    private Long songId;

    private String songMid;

    private String songName;

    private String songType;

    private String albumId;

    private String singerMid;

    private String timePublic;

    private String songAttr;

    private String interval;

    private String lyric;

    private String songUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getSongId() {
        return songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }

    public String getSongMid() {
        return songMid;
    }

    public void setSongMid(String songMid) {
        this.songMid = songMid == null ? null : songMid.trim();
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName == null ? null : songName.trim();
    }

    public String getSongType() {
        return songType;
    }

    public void setSongType(String songType) {
        this.songType = songType == null ? null : songType.trim();
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId == null ? null : albumId.trim();
    }

    public String getSingerMid() {
        return singerMid;
    }

    public void setSingerMid(String singerMid) {
        this.singerMid = singerMid == null ? null : singerMid.trim();
    }

    public String getTimePublic() {
        return timePublic;
    }

    public void setTimePublic(String timePublic) {
        this.timePublic = timePublic == null ? null : timePublic.trim();
    }

    public String getSongAttr() {
        return songAttr;
    }

    public void setSongAttr(String songAttr) {
        this.songAttr = songAttr == null ? null : songAttr.trim();
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval == null ? null : interval.trim();
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric == null ? null : lyric.trim();
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl == null ? null : songUrl.trim();
    }
}