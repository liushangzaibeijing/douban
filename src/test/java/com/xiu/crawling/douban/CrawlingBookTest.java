package com.xiu.crawling.douban;

import com.github.pagehelper.PageHelper;
import com.xiu.crawling.douban.bean.UrlInfo;
import com.xiu.crawling.douban.bean.UrlInfoExample;
import com.xiu.crawling.douban.common.ActiveEnum;
import com.xiu.crawling.douban.core.BookThreadTask;
import com.xiu.crawling.douban.mapper.BookMapper;
import com.xiu.crawling.douban.mapper.UrlInfoMapper;
import com.xiu.crawling.douban.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * author  Administrator
 * date   2018/10/12
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DoubanApplication.class)
@WebAppConfiguration
public class CrawlingBookTest {
    @Autowired
    private UrlInfoMapper urlInfoMapper;
    @Autowired
    private BookMapper bookMapper;

    /**
     * 爬取豆瓣的书籍信息
     */
    @Test
    public void crawlingBook() throws InterruptedException {
         //先进行查询书籍url 分页处理
        Integer count = 2;
        PageHelper.startPage(1,count);
        UrlInfoExample urlInfoExample = new UrlInfoExample();
        urlInfoExample.createCriteria().andUrlLike("https://book.douban.com/tag/%").andActiveEqualTo(ActiveEnum.ACTIVE.getCode());
        List<UrlInfo> urlInfos = urlInfoMapper.selectByExample(urlInfoExample);
        log.info("urlinfos {}",JsonUtil.obj2str(urlInfos));

        //使用多线程去进行爬取数据
        CountDownLatch latch = new CountDownLatch(count);

        //固定线程池
        Executor executor =Executors.newFixedThreadPool(count);
        //创建多线程任务
        List<BookThreadTask> bookThreadTasks = new ArrayList<>();
        for(UrlInfo urlInfo : urlInfos){
            BookThreadTask bookThreadTask = new BookThreadTask(urlInfo.getDescption(),urlInfo.getUrl(),bookMapper,urlInfoMapper,latch);
            executor.execute(bookThreadTask);
        }
        //Now wait till all services are checked
        latch.await();

        log.info("书籍爬取完毕");
    }
}