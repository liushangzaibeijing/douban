package com.xiu.crawling.douban.core.service.impl;

import com.github.pagehelper.PageHelper;
import com.xiu.crawling.douban.bean.UrlInfo;
import com.xiu.crawling.douban.bean.UrlInfoExample;
import com.xiu.crawling.douban.common.ActiveEnum;
import com.xiu.crawling.douban.core.BookThreadTask;
import com.xiu.crawling.douban.core.service.CrawlingService;
import com.xiu.crawling.douban.mapper.BookMapper;
import com.xiu.crawling.douban.mapper.ErrUrlMapper;
import com.xiu.crawling.douban.mapper.UrlInfoMapper;
import com.xiu.crawling.douban.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * author  Administrator
 * date   2018/10/18
 */
@Slf4j
@Service
public class CrawlingServiceImpl implements CrawlingService{
    public static Integer THREAD_NUMBER = 5;
    @Autowired
    private UrlInfoMapper urlInfoMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private ErrUrlMapper errUrlMapper;

    @Override
    public void crawlingBook() throws InterruptedException {
        //先进行查询书籍url 分页处理
        PageHelper.startPage(1,THREAD_NUMBER);
        UrlInfoExample urlInfoExample = new UrlInfoExample();
        urlInfoExample.createCriteria().andUrlLike("https://book.douban.com/tag/%").andActiveEqualTo(ActiveEnum.ACTIVE.getCode());
        List<UrlInfo> urlInfos = urlInfoMapper.selectByExample(urlInfoExample);
        log.info("urlinfos {}", JsonUtil.obj2str(urlInfos));

        //使用多线程去进行爬取数据
        CountDownLatch latch = new CountDownLatch(THREAD_NUMBER);

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBER);

        /*手动创建线程池*/
        //ThreadPoolExecutor executor = new ThreadPoolExecutor(THREAD_NUMBER,THREAD_NUMBER,1000, TimeUnit.MILLISECONDS,null);
        //创建多线程任务
        List<BookThreadTask> bookThreadTasks = new ArrayList<>();
        for(UrlInfo urlInfo : urlInfos){
            BookThreadTask bookThreadTask = new BookThreadTask(urlInfo.getDescption(),urlInfo.getUrl(),bookMapper,urlInfoMapper,errUrlMapper,latch);
            executor.execute(bookThreadTask);
        }
        //Now wait till all services are checked
        latch.await();

        log.info("end ............");
    }
}
