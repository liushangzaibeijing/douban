package com.xiu.crawling.douban.bean;

import java.util.Date;

public class Proxydata {
    private Integer id;

    private String type;

    private String ip;

    private Integer port;

    private Date successTime;

    private Integer canuse;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Date getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }

    public Integer getCanuse() {
        return canuse;
    }

    public void setCanuse(Integer canuse) {
        this.canuse = canuse;
    }
}