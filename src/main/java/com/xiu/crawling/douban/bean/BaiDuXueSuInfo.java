package com.xiu.crawling.douban.bean;

public class BaiDuXueSuInfo {
    private Integer id;

    private String title;

    private String author;

    private String abstractInfo;

    private String keyword;

    private String cited;

    private String publicYear;

    private String periodicalName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public String getAbstractInfo() {
        return abstractInfo;
    }

    public void setAbstractInfo(String abstractInfo) {
        this.abstractInfo = abstractInfo == null ? null : abstractInfo.trim();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    public String getCited() {
        return cited;
    }

    public void setCited(String cited) {
        this.cited = cited == null ? null : cited.trim();
    }

    public String getPublicYear() {
        return publicYear;
    }

    public void setPublicYear(String publicYear) {
        this.publicYear = publicYear == null ? null : publicYear.trim();
    }

    public String getPeriodicalName() {
        return periodicalName;
    }

    public void setPeriodicalName(String periodicalName) {
        this.periodicalName = periodicalName == null ? null : periodicalName.trim();
    }
}