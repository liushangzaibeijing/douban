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
import com.xiu.crawling.douban.utils.HttpUtil;
import com.xiu.crawling.douban.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * author  Administrator
 * date   2018/10/12
 */
@Slf4j
@RunWith(SpringRunner.class)
public class CrawlingMovieTest {

    @Test
    public void parseMovie(){
        String url = "https://movie.douban.com/subject/27172891//";
        String result = HttpUtil.doGet(url,null);


        MovieThreadTask task = new MovieThreadTask(null,null,null,null,null,null,null,null,null);
        Movie movie = task.parseMovie( Jsoup.parse(result),"");

        log.info("movie info {}",JsonUtil.obj2str(movie));
    }

    @Test
    public void testAAA(){
        String result = "法国 / 德国 / 俄罗斯 / 立陶宛 / 荷兰 / 乌克兰 / 拉脱维亚";
        log.info("length :  {}",result.length());
    }
}