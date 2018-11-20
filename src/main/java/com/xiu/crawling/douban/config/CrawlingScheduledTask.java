package com.xiu.crawling.douban.config;

import com.xiu.crawling.douban.core.service.CrawlingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * author  xieqx
 * date   2018/10/18
 * 爬取豆瓣的定时任务
 */
@Slf4j
@Component
public class CrawlingScheduledTask {
    @Autowired
    private CrawlingService crawlingService;

    /**
     * 每二十分钟执行一次 参考http://cron.qqe2.com/
     */
    //@Scheduled(cron = "0 0/20 * * * ? *")
    //@Scheduled(fixedRate=1000*60*5)
    public void crawlBookScheduledTask(){
        log.info("爬取书籍的定时任务开始");
        try {
            crawlingService.crawlingBook();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("爬取书籍的定时任务结束");
    }

    @Scheduled(fixedRate=1000*60*10)
    public void crawlMovieScheduledTask(){
        log.info("爬取电影的定时任务开始");
        try {
            crawlingService.crawlingMovie();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("爬取电影的定时任务结束");
    }
}
