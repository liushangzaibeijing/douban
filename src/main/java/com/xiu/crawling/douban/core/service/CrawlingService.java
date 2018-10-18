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
}
