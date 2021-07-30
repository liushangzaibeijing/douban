package com.xiu.crawling.douban.core.service;

/**
 * author  xieqx
 * date   2018/10/18
 * 爬取的核心服务
 */
public interface CrawlingService {

    /**
     * 爬取书籍信息
     */
    void crawlingBook() throws InterruptedException;


    /**
     * 爬取电影信息
     */
    void crawlingMovie() throws InterruptedException;

    /**
     * 爬取电影啥的url信息
     * @throws InterruptedException
     */
    void crawlingMovieURL() throws InterruptedException;


    /**
     * 提高csdn访问数
     */
    void raisePraise() throws InterruptedException;


}
