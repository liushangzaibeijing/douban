package com.xiu.crawling.douban.csdn;

import com.xiu.crawling.douban.DoubanApplication;
import com.xiu.crawling.douban.common.Constant;
import com.xiu.crawling.douban.core.service.CrawlingService;
import com.xiu.crawling.douban.core.service.CsdnService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DoubanApplication.class)
@WebAppConfiguration
public class CsdnServiceTest {
    @Autowired
    private CrawlingService crawlingService;

    @Autowired
    private CsdnService csdnService;
    @Test
    public void raisePraise() {
        try {
            crawlingService.raisePraise();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void mockComment() {
       csdnService.mockComment(Constant.csdnAllArticle);
    }

}
