package com.xiu.crawling.douban.bean;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2019-09-14
 */
public class CurrPage {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 正在爬取的当前页
     */
    private Integer currPage;

    /**
     *  1歌手 2专辑 3歌曲
     */
    private Integer type;

    /**
     * 错误的详细信息 之截取500
     */
    private String message;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCurrPage() {
        return currPage;
    }

    public void setCurrPage(Integer currPage) {
        this.currPage = currPage;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }
}