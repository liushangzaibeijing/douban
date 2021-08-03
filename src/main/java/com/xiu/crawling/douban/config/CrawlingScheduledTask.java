package com.xiu.crawling.douban.config;

import com.xiu.crawling.douban.common.Constant;
import com.xiu.crawling.douban.core.service.CrawlingService;
import com.xiu.crawling.douban.core.service.CsdnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * author  xieqx
 * date   2018/10/18
 * 爬取豆瓣的定时任务
 */
@Slf4j
@Component
@EnableScheduling
public class CrawlingScheduledTask {
    @Autowired
    private CrawlingService crawlingService;

    @Autowired
    private CsdnService csdnService;


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

    //@Scheduled(fixedRate=1000*60*20)
    public void crawlMovieScheduledTask(){
        log.info("爬取电影的定时任务开始");
        try {
            crawlingService.crawlingMovie();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("爬取电影的定时任务结束");
    }
//    @Scheduled(fixedDelay=1000*60*1)
//    @Scheduled(cron="0 0/1 * * * ? ")
    public void raisePraise(){
        log.info("csdn博客访问定时任务开始");
        try {
            crawlingService.raisePraise();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("csdn博客访问定时任务结束");
    }

    @Scheduled(fixedDelay=1000*60*60)
//    @Scheduled(cron="0 0 0/1 * * ?")
    public void mockComent(){
        log.info("csdn博客评论定时任务开始");
        csdnService.mockComment(Constant.csdnAllArticle);
        log.info("csdn博客评论定时任务结束");
    }


}
