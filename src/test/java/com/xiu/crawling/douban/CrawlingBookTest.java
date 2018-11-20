package com.xiu.crawling.douban;

import com.github.pagehelper.PageHelper;
import com.xiu.crawling.douban.bean.Movie;
import com.xiu.crawling.douban.bean.UrlInfo;
import com.xiu.crawling.douban.bean.UrlInfoExample;
import com.xiu.crawling.douban.common.ActiveEnum;
import com.xiu.crawling.douban.core.BookThreadTask;
import com.xiu.crawling.douban.core.MovieThreadTask;
import com.xiu.crawling.douban.core.service.ProxyService;
import com.xiu.crawling.douban.mapper.BookMapper;
import com.xiu.crawling.douban.mapper.ErrUrlMapper;
import com.xiu.crawling.douban.mapper.UrlInfoMapper;
import com.xiu.crawling.douban.proxypool.job.ScheduleJobs;
import com.xiu.crawling.douban.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
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
    public static Integer THREAD_NUMBER = 5;
    @Autowired
    private UrlInfoMapper urlInfoMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private ErrUrlMapper errUrlMapper;
    /**
     * 爬取豆瓣的书籍信息 使用for循环进行爬取 但是在多线程的情况下，一次性会生成上百个线程，容易造成ip封锁 不可取
     */
    @Test
    public void crawlingBookIndex() throws InterruptedException {
        //先查询总记录数 按照记录数循环查询
        UrlInfoExample urlInfoExampleU = new UrlInfoExample();
        urlInfoExampleU.createCriteria().andUrlLike("https://book.douban.com/tag/%").andActiveEqualTo(ActiveEnum.ACTIVE.getCode());

        List<UrlInfo> urlInfoCounts = urlInfoMapper.selectByExample(urlInfoExampleU);

        int count = urlInfoCounts.size()%5==0?urlInfoCounts.size()/5:urlInfoCounts.size()/5+1;

        log.info("需要爬取的书籍中的总的url个数：{}",count);
        for(int i =0;i<count;i++){
            log.info("当前第几次爬取：{}",i);
            crawlingBook();
        }



    }

    private void crawlingBook() throws InterruptedException {
        //先进行查询书籍url 分页处理
        PageHelper.startPage(1,THREAD_NUMBER);
        UrlInfoExample urlInfoExample = new UrlInfoExample();
        urlInfoExample.createCriteria().andUrlLike("https://book.douban.com/tag/%").andActiveEqualTo(ActiveEnum.ACTIVE.getCode());
        List<UrlInfo> urlInfos = urlInfoMapper.selectByExample(urlInfoExample);
        log.info("urlinfos {}", JsonUtil.obj2str(urlInfos));

        //使用多线程去进行爬取数据
        CountDownLatch latch = new CountDownLatch(THREAD_NUMBER);

        //固定线程池
        Executor executor = Executors.newFixedThreadPool(THREAD_NUMBER);
        //创建多线程任务
        List<BookThreadTask> bookThreadTasks = new ArrayList<>();
        for(UrlInfo urlInfo : urlInfos){
            BookThreadTask bookThreadTask = new BookThreadTask(urlInfo.getDescption(),urlInfo.getUrl(),bookMapper,urlInfoMapper,errUrlMapper,null,latch, null);
            executor.execute(bookThreadTask);
        }
        //Now wait till all services are checked
        latch.await();

        log.info("end ............");
    }


    @Test
    public void testCrawling(){
        MovieThreadTask movieThreadTask = new MovieThreadTask(null, null, null, null, null, null, new ProxyService() {
            @Override
            public HttpHost findCanUseProxy() {
                return null;
            }

            @Override
            public Boolean updateProxy(Boolean flag, HttpHost proxy) {
                return null;
            }
        },null,null);
        //String urlDetail = "https://movie.douban.com/subject/26636712/";
        String urlDetail = "https://movie.douban.com/subject/30317630/";
        Movie movie = movieThreadTask.crawlMovie("电影", urlDetail);
    }
}