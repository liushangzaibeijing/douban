package com.xiu.crawling.douban.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiu.crawling.douban.bean.*;
import com.xiu.crawling.douban.bean.vo.Result;
import com.xiu.crawling.douban.bean.vo.UrlVO;
import com.xiu.crawling.douban.common.ActiveEnum;
import com.xiu.crawling.douban.common.Constant;
import com.xiu.crawling.douban.common.SortEnum;
import com.xiu.crawling.douban.core.service.AbstractThreadTask;
import com.xiu.crawling.douban.core.service.ProxyService;
import com.xiu.crawling.douban.mapper.BookMapper;
import com.xiu.crawling.douban.mapper.ErrUrlMapper;
import com.xiu.crawling.douban.mapper.MovieMapper;
import com.xiu.crawling.douban.mapper.UrlInfoMapper;
import com.xiu.crawling.douban.proxypool.job.ScheduleJobs;
import com.xiu.crawling.douban.utils.HttpUtil;
import com.xiu.crawling.douban.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author  xieqx
 * date   2018/10/13
 * desc 爬取
 */
@Slf4j
public class MovieThreadTask extends AbstractThreadTask implements  Runnable {
    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 标签的url
     */
    private String url;

    /**
     * mark 标识
     */
    private Integer mark;

    /**
     * 持久化的电影对象
     */
    private MovieMapper movieMapper;

    /**
     * 存储url信息
     */
    private UrlInfoMapper urlInfoMapper;

    /**
     * 存储出错的url信息
     */
    private ErrUrlMapper errUrlMapper;

    /**
     * 并行操作
     */
    private CountDownLatch latch;

    /*
     用于存储可用的代理对象
     */
    private HttpHost proxy;


    public MovieThreadTask(String tagName, String url, Integer mark, MovieMapper movieMapper, UrlInfoMapper urlInfoMapper, ErrUrlMapper errUrlMapper, ProxyService proxyService) {
        super(proxyService,errUrlMapper);
        this.tagName = tagName;
        this.url = url;
        this.mark = mark;
        this.movieMapper = movieMapper;
        this.urlInfoMapper = urlInfoMapper;
        this.errUrlMapper = errUrlMapper;

    }

    public MovieThreadTask(String tagName, String url, Integer mark, MovieMapper movieMapper, UrlInfoMapper urlInfoMapper, ErrUrlMapper errUrlMapper, ProxyService proxyService, CountDownLatch latch) {
        super(proxyService,errUrlMapper);
        this.tagName = tagName;
        this.url = url;
        this.mark = mark;
        this.movieMapper = movieMapper;
        this.urlInfoMapper = urlInfoMapper;
        this.errUrlMapper = errUrlMapper;
        this.latch = latch;
    }

    /**
     * 支持首页列表 以及第几页数据的爬取操作
     */
    @Override
    public void run() {
        try {
            //开始爬取以及持久化
            crawlMovie(this.tagName, this.url, mark);
            //爬取结束后将该url的状态置为已经爬取过 模糊查询
            endCrawl(this.tagName, this.url);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //该并行线程结束
            latch.countDown();
        }

    }

    /**
     * 设置爬取的url信息状态为已经爬取完成
     *
     * @param url
     */
    private void endCrawl(String tagName, String url) {
        UrlInfoExample urlInfoExample = new UrlInfoExample();
        urlInfoExample.createCriteria().andUrlEqualTo(url);

        UrlInfo urlInfo = new UrlInfo();
        urlInfo.setActive(ActiveEnum.NO_ACTIVE.getCode());
        Integer count = urlInfoMapper.updateByExampleSelective(urlInfo, urlInfoExample);
        if (count != null) {
            log.info("{} 标签下的电影信息爬取完毕", tagName);
        }
    }

    /**
     * @param tagName 标签名
     * @param url     爬取得url地址
     * @param mark    电影的不同类型 3 电影 电视剧  4 排行榜  5 标签
     */
    private void crawlMovie(String tagName, String url, Integer mark) {
        proxy = proxyService.findCanUseProxy();
        String result = null;

        //转换url https://movie.douban.com/j/search_subjects?type=movie&tag=%E7%83%AD%E9%97%A8&sort=recommend&page_limit=100000000000&page_start=0
        //https://movie.douban.com/j/search_subjectstype=tv&tag=纪录片


        if (mark == 3) {
            int pageIndex = 0;
            while (true) {
                UrlVO urlVO = new UrlVO();
                try {
                    urlVO.addUrl(url).addSort(SortEnum.RECOMMEND.getCode())
                            .addPageStart(pageIndex*Constant.pageSize)
                            .addPageLimit(Constant.pageSize);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log.info("请求的url : {}",urlVO.getBaseUrl());
                while(true){
                    result = HttpUtil.doGet(urlVO.getBaseUrl(), proxy);
                    //TODO 重新获取代理为null 的情况
                    proxy = checkProxy(result,proxy);
                    if(proxy == null){
                        break;
                    }
                }

                JSONObject obj = JSON.parseObject(result);
                JSONArray array = obj.getJSONArray("subjects");
                if (array.size() == 0) {
                    log.warn("爬取完成 pageIndex: {}",pageIndex);
                    break;
                }
                pageIndex ++;
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String urlDetail = object.getString("url");
                    try {
                        Movie movie = crawlMovie(tagName, urlDetail);
                        //电影去重
                        MovieExample movieExample = new MovieExample();
                        movieExample.createCriteria().andNameEqualTo(movie.getName());
                        List<Movie> movies = movieMapper.selectByExample(movieExample);
                        if (movies == null || movies.size() == 0) {
                            movieMapper.insert(movie);
                        }
                    }catch (Exception e){
                        log.info("{} 模块下的 {} 中的 {} 爬取出错，请重试", "电影", tagName, url);
                        insertErrUrl(urlDetail,tagName,e.getMessage(),"电影");
                    }
                }

            }


        }

    }

    public Movie crawlMovie(String tagName, String url) {
        proxy = proxyService.findCanUseProxy();
        String result = HttpUtil.doGet(url,proxy);
        while (true){
            result = HttpUtil.doGet(url, proxy);
            //TODO 重新获取代理为null 的情况
            proxy = checkProxy(result,proxy);
            if(proxy == null){
                break;
            }
        }

        if(!StringUtils.isEmpty(result)){
            Document document = Jsoup.parse(result);
            Movie movie = parseMovie(document,tagName);
            return  movie;
        }

       return null;
    }

    public Movie parseMovie(Document document,String tagName) {
        Movie movie = null;
        movie = new Movie();
        movie.setType(tagName);
        //获取导演 编剧主演的信息
        Elements elements =  document.select("#info > span");
        for(int i = 0; i<elements.size();i++) {
            String text = elements.get(i).text();
            //log.info(text);
            if (text.contains("导演")) {
                movie.setDirector(StringUtils.trimAllWhitespace(text.split(":")[1]));
            }
            if (text.contains("主演")) {
                movie.setLeadActor(StringUtils.trimAllWhitespace(text.split(":")[1]));
            }
            if (text.contains("编剧")) {
                movie.setScreenWriter(StringUtils.trimAllWhitespace(text.split(":")[1]));
            }
            //判断 如果三者不为null 则直接退出
            if(movie.getDirector()!=null&&movie.getLeadActor()!=null&&movie.getScreenWriter()!=null){
                break;
            }

        }
        //类型 v:genre
        //上映时间  v:initialReleaseDate
        //片长  v:runtime
        Elements typeElements =  document.select("#info > span[property=v:genre]");
        StringBuilder typebuilder = new StringBuilder();
        for(Element element : typeElements){
            typebuilder.append(element.text()).append("/");
        }
        typebuilder.replace(typebuilder.length()-1,typebuilder.length(),"");
        movie.setTag(typebuilder.toString());

        Elements relaseDataElements =  document.select("#info > span[property=v:initialReleaseDate]");
        StringBuilder relaseDataBuilder = new StringBuilder();
        for(Element element : relaseDataElements){
            relaseDataBuilder.append(element.text()).append("/");
        }
        relaseDataBuilder.replace(relaseDataBuilder.length()-1,relaseDataBuilder.length(),"");
        movie.setReleaseTime(relaseDataBuilder.toString());

        Elements runTimeElements =  document.select("#info > span[property=v:runtime]");
        if(runTimeElements.size()!=0){
            StringBuilder runTimeBuilder = new StringBuilder();
            for(Element element : runTimeElements){
                runTimeBuilder.append(element.text()).append("/");
            }
            runTimeBuilder.replace(runTimeBuilder.length()-1,runTimeBuilder.length(),"");
            movie.setMovieLength(runTimeBuilder.toString());
        }


        //制片国家/地区 / 语言: /又名:  / 集数  片长
        Map<String,String> info = parseMovieText(document);

        String filmmakingArea = info.get("filmmakingArea");
        if(!StringUtils.isEmpty(filmmakingArea)){
            movie.setFilmmakingArea(filmmakingArea);
        }
        String language = info.get("language");
        if(!StringUtils.isEmpty(language)){
            movie.setLanguage(language);
        }
        String number = info.get("number");
        if(!StringUtils.isEmpty(number)){
            movie.setSetNumber(Integer.parseInt(number));
        }
        String runTime = info.get("runTime");
        if(!StringUtils.isEmpty(runTime)){
            String runtime = movie.getMovieLength();
            if(!StringUtils.isEmpty(runtime)){
                runTime+=runtime;
            }
            movie.setMovieLength(runTime);
        }
        String alias = info.get("alias");
        if(!StringUtils.isEmpty(alias)){
            movie.setAlias(alias);
        }

        //得分 评价人数  图片 电影  名称 剧情 季数
        String num_text = document.select("span[property=v:votes]").text();
        Integer ratePeople =  Integer.parseInt(document.select("span[property=v:votes]").text());
        movie.setEvaluateNumber(ratePeople);
        Float score =  Float.parseFloat(document.select("strong.rating_num").text());
        movie.setScore(score);
        String  movieName = document.select("span[property=v:itemreviewed]").text();
        movie.setName(movieName);

        String  imgsrc = document.select("div#mainpic img").attr("src");
        movie.setPicture(imgsrc);

        String  descption = StringUtils.trimAllWhitespace(document.select("span[property=v:summary]").text());
        movie.setSynopsis(descption);


        log.info("movie info {}", JsonUtil.obj2str(movie));
        return  movie;
    }

    /**
     * 从文本中获取想要的电影信息
     * @param document
     * @return
     */
    private Map<String,String> parseMovieText(Document document) {

        //制片国家/地区 / 语言: /又名:textNodes()
        Elements elements = document.select("#info");
        StringBuilder textBuilder = new StringBuilder();
        if(elements.size()==1){
            Element element = elements.get(0);
            List<TextNode> nodes = element.textNodes();
            for(TextNode node : nodes){
                //如果是纯英文或者数字
                String text = node.text();
                if(!isCharterAndNum(text)){
                    text = StringUtils.trimAllWhitespace(text);
                }

                if(text.equals("")||StringUtils.trimAllWhitespace(text).equals("/")){
                    continue;
                }
                textBuilder.append(text).append("|");
            }
            textBuilder.replace(textBuilder.length()-1,textBuilder.length(),"");
        }

        // 中国大陆|汉语普通话|36|45分钟|原来你还在这里电视剧版
        // 美国|英语|/119分钟(中国大陆)|蚁侠2：黄蜂女现身(港)/蚁人2/蚁人与黄蜂女/Ant-Man2

        //截取 出来的长度 3  4 有集数
        String info = textBuilder.toString();
        log.info("info {}",info);
        String[] array =  info.split("\\|");
        Map<String,String> movieInfo = new HashMap<>();

        if(array!=null&&array.length>0){
            movieInfo.put("filmmakingArea",array[0]);
        }

        if(array.length==4){
            //movieInfo.put("filmmakingArea",array[0]);
            //movieInfo.put("language",array[1]);
            //movieInfo.put("number",array[2]);
            movieInfo.put("alias",array[3]);
        }

        if(array.length==3){
            //movieInfo.put("filmmakingArea",array[0]);
            // movieInfo.put("language",array[1]);
            movieInfo.put("alias",array[2]);
        }
        /*
        if(array.length==4){
            movieInfo.put("filmmakingArea",array[0]);
            movieInfo.put("language",array[1]);
            movieInfo.put("number",array[2]);
            movieInfo.put("runTime",array[3]);
        }
        */
        if(array.length==5){
            //movieInfo.put("filmmakingArea",array[0]);
            //movieInfo.put("language",array[1]);
            //movieInfo.put("number",array[2]);
            //movieInfo.put("runTime",array[3]);
            movieInfo.put("alias",array[4]);
        }

        for(String msg : array){
            boolean flag = isInteger(msg);
            if(flag){
                movieInfo.put("number",msg);
                continue;
            }
            //语言 不太恰当
            if(msg.contains("语")){
               String alias =  movieInfo.get("alias");
                if(!StringUtils.isEmpty(alias) && alias.equals(msg)){
                   //不做任何操作
                    continue;
                }else{
                    movieInfo.put("language",msg);
                    continue;
                }

            }
            if(msg.contains("分钟")){
                movieInfo.put("runTime",msg);
                continue;
            }
        }


        return movieInfo;
    }


}
