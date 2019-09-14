package com.xiu.crawling.douban.exception;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义的业务异常
 */
public class CrawlingException extends Exception {
    /**
     * 爬取出错的当前页
     */
    private AtomicInteger currentPage;
    /**
     * 类型 歌手 专辑 歌曲
     */
    private Integer type;
    /**
     * 详细的消息信息便于打印日志
     */
    private String msg;


    public CrawlingException(AtomicInteger currentPage,Integer type,String msg){
        this.currentPage = currentPage;
        this.type =type;
        this.msg = msg;
    }

    public AtomicInteger getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(AtomicInteger currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
