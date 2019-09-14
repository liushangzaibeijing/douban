package com.xiu.crawling.douban.cache;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@Scope("singleton")
public class GlobalVariables {
    /**
     * 爬取歌手暂停退出的当前页码
     */
    private AtomicInteger  singerCrawlCurrent;

    /**
     * 根据类型设置相关的爬取信息的当前页（type区分类型 1歌手 2专辑 3歌曲）
     * @param currentPage
     * @param type
     */
    public synchronized void setSingerCrawlCurrent(AtomicInteger currentPage,Integer type){
        if(type ==1){
            log.info("当前歌手爬取到第{}页",currentPage);
            singerCrawlCurrent = currentPage;
        }
    }

    /**
     * 根据类型获取相关的爬取信息的当前页（type区分类型 1歌手 2专辑 3歌曲）
     * @param type
     */
    public synchronized AtomicInteger getSingerCrawlCurrent(Integer type){
        if(type ==1){
            log.info("从当前歌手信息的第{}页 开始爬取",this.singerCrawlCurrent);
            if(singerCrawlCurrent==null){
                return new AtomicInteger(1);
            }
            return singerCrawlCurrent;
        }
        return null;
    }




}
