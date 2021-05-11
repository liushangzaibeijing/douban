package com.xiu.crawling.douban;

import com.xiu.crawling.douban.core.service.CrawlingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * author  Administrator
 * date   2018/10/12
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DoubanApplication.class)
@WebAppConfiguration
public class CrawlingBaiDuXueSuTest {
    @Autowired
    private CrawlingService crawlingService;
    /**
     * 爬取豆瓣的书籍信息 使用for循环进行爬取 但是在多线程的情况下，一次性会生成上百个线程，容易造成ip封锁 不可取
     */
    @Test
    public void crawlingBaiDuXueSu() {
        try {
            crawlingService.crawlingBaiduXueSu();
        } catch (InterruptedException e) {


        }
    }



}