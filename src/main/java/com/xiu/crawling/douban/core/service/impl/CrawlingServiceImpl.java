package com.xiu.crawling.douban.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.xiu.crawling.douban.bean.UrlInfo;
import com.xiu.crawling.douban.bean.UrlInfoExample;
import com.xiu.crawling.douban.common.ActiveEnum;
import com.xiu.crawling.douban.common.Constant;
import com.xiu.crawling.douban.common.MarkEnum;
import com.xiu.crawling.douban.common.MvTypeEnum;
import com.xiu.crawling.douban.core.BookThreadTask;
import com.xiu.crawling.douban.core.MovieThreadTask;
import com.xiu.crawling.douban.core.service.CrawlingService;
import com.xiu.crawling.douban.core.service.ProxyService;
import com.xiu.crawling.douban.mapper.*;
import com.xiu.crawling.douban.proxypool.job.ScheduleJobs;
import com.xiu.crawling.douban.utils.HttpUtil;
import com.xiu.crawling.douban.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * author  Administrator
 * date   2018/10/18
 */
@Slf4j
@Service
public class CrawlingServiceImpl implements CrawlingService{
    public static Integer THREAD_NUMBER = 1;
    @Autowired
    private UrlInfoMapper urlInfoMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private ErrUrlMapper errUrlMapper;
    @Autowired
    private ProxyService proxyService;

    @Autowired
    private ScheduleJobs scheduleJobs;


    @Autowired
    private ErrTempurlMapper errTempurlMapper;


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

        //创建多线程任务
        List<BookThreadTask> bookThreadTasks = new ArrayList<>();
        for(UrlInfo urlInfo : urlInfos){
            BookThreadTask bookThreadTask = new BookThreadTask(urlInfo.getDescption(),urlInfo.getUrl(),bookMapper,urlInfoMapper,
                    errUrlMapper,proxyService,latch,scheduleJobs);
            executor.execute(bookThreadTask);
        }
        //Now wait till all services are checked
        latch.await();

        log.info("end ............");
    }

    @Override
    public void crawlingMovie() throws InterruptedException {

        try {
            //爬取电影和电视剧
            //https://movie.douban.com/j/search_subjects?type=movie&tag=%E7%83%AD%E9%97%A8&sort=recommend&page_limit=100000000000&page_start=0
            PageHelper.startPage(1, THREAD_NUMBER);
            UrlInfoExample urlInfoExample = new UrlInfoExample();
            urlInfoExample.createCriteria().andMarkBetween(MarkEnum.TV_MOVIE.getCode(),MarkEnum.TAG.getCode())
              .andActiveEqualTo(ActiveEnum.ACTIVE.getCode());
            List<UrlInfo> urlInfos = urlInfoMapper.selectByExample(urlInfoExample);

            //使用多线程去进行爬取数据
            CountDownLatch latch = new CountDownLatch(THREAD_NUMBER);
            ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBER);

            //创建多线程任务
            for (UrlInfo urlInfo : urlInfos) {

                MovieThreadTask bookThreadTask = new MovieThreadTask(urlInfo.getLabel(), urlInfo.getUrl(), urlInfo.getMark(), movieMapper, urlInfoMapper,
                        errUrlMapper, proxyService, latch,scheduleJobs,errTempurlMapper);
                executor.execute(bookThreadTask);
            }
            //Now wait till all services are checked
            latch.await();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 爬取电影信息
     * @throws InterruptedException
     */
    @Override
    public void crawlingMovieURL() throws InterruptedException {
        //分为两种 一种js 获取json数据
        /**
         *  https://movie.douban.com/j/search_tags?type=movie  //获取url地址
         *  https://movie.douban.com/j/search_subjects?type=movie&tag=%E7%83%AD%E9%97%A8&sort=recommend&page_limit=100000000000&page_start=0
         *
         *   https://movie.douban.com/j/search_tags?type=tv
         */

        //保存电影url
        saveTVUrl(Constant.MOVIETAG,MvTypeEnum.MOVIE.getCode());

        //保存电视剧url
        saveTVUrl(Constant.TVTAG,MvTypeEnum.TV.getCode());

        //分类排行榜
        saveChartUrl(Constant.CHART);

        //保存选影视的标签
        saveTagUrl(Constant.TAG);

    }

    @Override
    public void raisePraise() throws InterruptedException {
        AtomicInteger pageNum = new AtomicInteger(1);
        while(true){
            //获取所有文章列表
            String articleListHtml = HttpUtil.doGet("https://blog.csdn.net/liushangzaibeijing/article/list/"+pageNum.getAndIncrement());
            if(StringUtils.isEmpty(articleListHtml)){
                log.info("获取不到内容");
                return;
            }
            Document document = Jsoup.parse(articleListHtml);
            Elements articleList = document.select(" div.article-list a");
            if(articleList.size()<=0){
                return;
            }
            for(Element element: articleList){
                //遍历访问文章列表
                String href =  element.attr("href");
                if(href.contains("download.csdn.net")){
                    continue;
                }
                //文章内容
                String contentName = element.text();
                String content = HttpUtil.doGet(href);
                if(!StringUtils.isEmpty(content)){
                    log.info("{}文章访问成功",contentName);
                }
            }
        }

    }

    /**
     * 保存电视剧和电影信息
     * @param tag
     */
    private void saveTagUrl(String tag) {
        for(MvTypeEnum typeEnum : MvTypeEnum.values()){
           String url = Constant.SELECT_FILM +typeEnum.getName();
           UrlInfo urlInfo = new UrlInfo();
           urlInfo.setUrl(url);
           urlInfo.setActive(ActiveEnum.ACTIVE.getCode());
           urlInfo.setLabel(typeEnum.getName());
           urlInfo.setMark(5);
           urlInfo.setDescption("选影视");
           UrlInfoExample urlInfoExample = new UrlInfoExample();
           urlInfoExample.createCriteria().andUrlEqualTo(urlInfo.getUrl());
           List<UrlInfo> urlInfoTemps = urlInfoMapper.selectByExample(urlInfoExample);
           if(urlInfoTemps==null || urlInfoTemps.size()==0){
               urlInfoMapper.insert(urlInfo);
           }
        }
    }

    /**
     * 电影消费排行榜
     * @param chart
     */
    private void saveChartUrl(String chart) {
        HttpHost proxy = proxyService.findCanUseProxy();

        String result = HttpUtil.doGet(chart,proxy);
        //解析html
        List<UrlInfo> urlInfos = parseMVTags(result);

        for(UrlInfo urlInfo : urlInfos){
            UrlInfoExample urlInfoExample = new UrlInfoExample();
            urlInfoExample.createCriteria().andUrlEqualTo(urlInfo.getUrl());
            List<UrlInfo> urlInfoTemps = urlInfoMapper.selectByExample(urlInfoExample);
            if(urlInfoTemps==null || urlInfoTemps.size()==0){
                urlInfoMapper.insert(urlInfo);
            }
        }


    }

    private List<UrlInfo> parseMVTags(String result) {
        /**
         *  type_name 电影标签名称
         * type_id  电影标签id
         * interval_id=hight:lower   该分页下好于 hight 到 lower 的片子（两者之间相差必须为10）  ----》 100:90  好于100%-90%的剧情片 (589)
         * https://movie.douban.com/typerank?type_name=%E5%89%A7%E6%83%85&type=11&interval_id=100:90&action=
         * js中获取对象信息
         * 主要是获取type_name 和type
         * 获取总记录数
         * https://movie.douban.com/j/chart/top_list_count?type=11&interval_id=100%3A90
         * 获取记录信息
         * https://movie.douban.com/j/chart/top_list?type=11&interval_id=100%3A90&action=&start=0&limit=20
         */
        Document document = Jsoup.parseBodyFragment(result);
        Elements elements = document.select("div[class=types] span a");

        List<UrlInfo> urlInfos = new ArrayList<>();
        for(Element element : elements){
           String href =  element.attr("href");
           //type_name=%E5%89%A7%E6%83%85&type=11

           int startTypeNameIndex = href.indexOf("type_name")+10;
           int endTypeNameIndex = href.indexOf("type=")-1;

           int startTypeIndex = endTypeNameIndex+6;
           int endTypeIndex = href.indexOf("interval_id")-1;

           String typeName = href.substring(startTypeNameIndex,endTypeNameIndex);
           String type = href.substring(startTypeIndex,endTypeIndex);

           String url = Constant.CHART_TOP+"?type="+type;
           UrlInfo urlInfo = new UrlInfo();
           urlInfo.setUrl(url);
           urlInfo.setActive(ActiveEnum.ACTIVE.getCode());
           urlInfo.setDescption("分类排行榜");
           urlInfo.setMark(4);
           urlInfo.setLabel(typeName);

           urlInfos.add(urlInfo);

        }

        return  urlInfos;

    }

    private void saveTVUrl(String tagurl,String type) {
        HttpHost proxy = proxyService.findCanUseProxy();

        String result = HttpUtil.doGet(tagurl,proxy);

        if(result!=null){

            JSONObject object =  JSON.parseObject(result);

            JSONArray array = object.getJSONArray("tags");

            if(array!=null&&array.size()>0){
                for(int i=0;i<array.size();i++){
                    String tvURL = Constant.TV_BASE_URL+"?type="+type;
                    tvURL +="&tag="+array.getString(i);
                    UrlInfo urlInfo = new UrlInfo();
                    urlInfo.setUrl(tvURL);
                    urlInfo.setActive(ActiveEnum.ACTIVE.getCode());
                    urlInfo.setDescption("选电影 影视");
                    urlInfo.setLabel(array.getString(i));
                    //电影的一级标签 3
                    urlInfo.setMark(3);
                    //去重
                    UrlInfoExample urlInfoExample = new UrlInfoExample();
                    urlInfoExample.createCriteria().andUrlEqualTo(tvURL);
                    List<UrlInfo> urlInfos = urlInfoMapper.selectByExample(urlInfoExample);
                    if(urlInfos==null || urlInfos.size()==0){
                        urlInfoMapper.insert(urlInfo);
                    }
                }
            }
        }
    }
}
