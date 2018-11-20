package com.xiu.crawling.douban.bean;

public class ErrTempurl {
    private Integer id;

    private String url;

    private String sort;

    private Integer type;

    private String intervalid;

    private String label;

    private Integer mark;

    private Integer pageindex;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort == null ? null : sort.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIntervalid() {
        return intervalid;
    }

    public void setIntervalid(String intervalid) {
        this.intervalid = intervalid == null ? null : intervalid.trim();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label == null ? null : label.trim();
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public Integer getPageindex() {
        return pageindex;
    }

    public void setPageindex(Integer pageindex) {
        this.pageindex = pageindex;
    }
}