package com.xiu.crawling.douban;

import com.github.pagehelper.PageHelper;
import com.xiu.crawling.douban.bean.*;
import com.xiu.crawling.douban.common.ActiveEnum;
import com.xiu.crawling.douban.core.BookThreadTask;
import com.xiu.crawling.douban.core.MovieThreadTask;
import com.xiu.crawling.douban.core.service.ProxyService;
import com.xiu.crawling.douban.mapper.*;
import com.xiu.crawling.douban.utils.HttpUtil;
import com.xiu.crawling.douban.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * author  Administrator
 * date   2018/10/12
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DoubanApplication.class)
@WebAppConfiguration
public class CrawlingMovieTest {

    @Autowired
    private ErrUrlMapper errUrlMapper;
    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private ProxydataMapper proxydataMapper;


    @Test
    public void parseMovie(){
        String url = "https://movie.douban.com/subject/1305069/";
        String result = HttpUtil.doGet(url,null);


        MovieThreadTask task = new MovieThreadTask(null,null,null,null,null,null,null,null,null,null);
        Movie movie = task.parseMovie(Jsoup.parse(result),"");
        log.info("movie info {}",JsonUtil.obj2str(movie));
        saveMovie(movie);

    }




    /**
     * 保存出错的电影信息
     */
    @Test
    public void insertErrMovie(){
        //查询出错的url 电影
        PageHelper.startPage(0,10);;
        ErrUrlExample errUrlExample = new ErrUrlExample();
        errUrlExample.createCriteria().andModuleEqualTo("电影");

        List<ErrUrl> errUrls = errUrlMapper.selectByExample(errUrlExample);

        for(ErrUrl errUrl : errUrls){
            String result = HttpUtil.doGet(errUrl.getErrorUrl(),null);
            if(result.equals("404")){
                ErrUrlExample deleteErr = new ErrUrlExample();
                deleteErr.createCriteria().andIdEqualTo(errUrl.getId());
                errUrlMapper.deleteByExample(deleteErr);
                continue;
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            MovieThreadTask task = new MovieThreadTask(null,null,null,null,null,null,null,null,null,null);
            Movie movie = task.parseMovie(Jsoup.parse(result),errUrl.getName());

            Integer errId = errUrl.getId();
            saveMovie(movie);
            ErrUrlExample deleteErr = new ErrUrlExample();
            deleteErr.createCriteria().andIdEqualTo(errId);
            errUrlMapper.deleteByExample(deleteErr);

        }


    }

    private void saveMovie(Movie movie) {
        try {
            MovieExample movieExample = new MovieExample();
            movieExample.createCriteria().andNameEqualTo(movie.getName());

            List<Movie> movieList = movieMapper.selectByExample(movieExample);
            if (movieList == null || movieList.size() == 0) {
                int index = movieMapper.insert(movie);
            }
         }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void testAAA(){
        String result = "1929-01-30(String), Germany: 133 分钟(restored version)/USA:109分钟/Belgium:152分钟(CopywithFrenchtitlesatBrusselsMuséeduCinéma)/131分钟(originalversion)(21.4fps)/110分钟(VHSversion)";
        //result = result.replace(" ","");
        log.info("length :  {}",result.length());

    }

    @Test
    public void tsetMapperCache(){

       List<Proxydata> proxydatas =  proxydataMapper.selectByExample(new ProxydataExample());
        log.info("proxy list size:  {}",proxydatas.size());
       proxydatas =  proxydataMapper.selectByExample(new ProxydataExample());
        log.info("proxy list size :  {}",proxydatas.size());

    }







}