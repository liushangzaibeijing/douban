package com.xiu.crawling.douban.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiu.crawling.douban.bean.*;
import com.xiu.crawling.douban.bean.vo.UrlVO;
import com.xiu.crawling.douban.common.ActiveEnum;
import com.xiu.crawling.douban.common.Constant;
import com.xiu.crawling.douban.common.SortEnum;
import com.xiu.crawling.douban.core.service.AbstractThreadTask;
import com.xiu.crawling.douban.core.service.ProxyService;
import com.xiu.crawling.douban.mapper.ErrTempurlMapper;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


/**
 * @author  xieqx
 * date   2018/10/13
 * desc 爬取
 */
@Slf4j
public class MovieThreadTask extends AbstractThreadTask implements  Runnable{

    private static final Integer SLEEPTIM = 3000;

    private static final int MOVIEANDTV = 3;
    private static final int CHART = 4;
    private static final int TAG = 5;
    private static final int INTERVALID = 10;

    private static final String NOT_FOUND = "404";

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
     * 存储爬取出错的url信息
     */
    private ErrTempurlMapper errTempurlMapper;

    /**
     * 并行操作
     */
    private CountDownLatch latch;

    /**
     * 用于存储可用的代理对象
     */
    private HttpHost proxy;


    public MovieThreadTask(String tagName, String url, Integer mark, MovieMapper movieMapper, UrlInfoMapper urlInfoMapper,
                           ErrUrlMapper errUrlMapper, ProxyService proxyService,ScheduleJobs scheduleJobs,ErrTempurlMapper errTempurlMapper) {
        super(proxyService,errUrlMapper,scheduleJobs);
        this.tagName = tagName;
        this.url = url;
        this.mark = mark;
        this.movieMapper = movieMapper;
        this.urlInfoMapper = urlInfoMapper;
        this.errUrlMapper = errUrlMapper;
        this.errTempurlMapper = errTempurlMapper;
    }

    public MovieThreadTask(String tagName, String url, Integer mark, MovieMapper movieMapper, UrlInfoMapper urlInfoMapper,
                           ErrUrlMapper errUrlMapper, ProxyService proxyService, CountDownLatch latch,ScheduleJobs scheduleJobs,ErrTempurlMapper errTempurlMapper) {
        super(proxyService,errUrlMapper,scheduleJobs);
        this.tagName = tagName;
        this.url = url;
        this.mark = mark;
        this.movieMapper = movieMapper;
        this.urlInfoMapper = urlInfoMapper;
        this.errUrlMapper = errUrlMapper;
        this.errTempurlMapper = errTempurlMapper;
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
     * @param url 爬取结束的url地址
     */
    private void endCrawl(String tagName, String url) {
        UrlInfoExample urlInfoExample = new UrlInfoExample();
        urlInfoExample.createCriteria().andUrlEqualTo(url);

        UrlInfo urlInfo = new UrlInfo();
        urlInfo.setActive(ActiveEnum.NO_ACTIVE.getCode());
        Integer count = urlInfoMapper.updateByExampleSelective(urlInfo, urlInfoExample);
        if (count >= 0) {
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

        //转换url https://movie.douban.com/j/search_subjects?type=movie&tag=%E7%83%AD%E9%97%A8&sort=recommend&page_limit=100000000000&page_start=0
        //https://movie.douban.com/j/search_subjectstype=tv&tag=纪录片

        //电影电视剧
        if (mark == MOVIEANDTV) {
            saveMovieAndTv(tagName, url,mark);
        }
        //排行榜
        if (mark == CHART) {
            saveMovieChart(tagName, url,mark);
        }
        //标签
        if (mark == TAG) {
            saveMovieTag(tagName, url,mark);
        }
        //

    }

    /**
     * 保存标签类型的电影信息
     * @param tagName 标签名称
     * @param url url地址
     */
    private void saveMovieTag(String tagName, String url,Integer mark) {
        String result;
        int pageIndex = 0;
        while (true) {
            UrlVO urlVO  = null;
            try {
                //先查询如果存在则将将新的url放入UrlVO
                urlVO = findTempUrl(tagName,mark);
                if(urlVO == null){
                    urlVO = new UrlVO();
                    urlVO.addUrl(url).addStart(pageIndex*Constant.pageSize);
                }else{
                    pageIndex = urlVO.getPageIndex();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info("请求的url : {}",urlVO.getBaseUrl());
            result = findReponse(tagName,mark,urlVO);
//            while(true){
//                result = HttpUtil.doGet(urlVO.getBaseUrl(), proxy);
//                try {
//                    Thread.sleep(SLEEPTIM);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                HttpHost newProxy = checkProxy(result,proxy);
//                if(newProxy == null){
//                    saveErrTempUrl(urlVO,tagName,mark);
//                }
//                else if(isSampleProxy(proxy,newProxy)){
//                    break;
//                }
//            }
            JSONObject data = JSON.parseObject(result);
            JSONArray array = JSON.parseArray(data.getString("data"));
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
                    if(movie == null){
                        continue;
                    }
                    //电影去重
                    MovieExample movieExample = new MovieExample();
                    movieExample.createCriteria().andNameEqualTo(movie.getName());
                    List<Movie> movies = movieMapper.selectByExample(movieExample);
                    if (movies == null || movies.size() == 0) {
                        movieMapper.insert(movie);
                    }
                }catch (Exception e){
                    log.info("{} 模块下的 {} 中的 {} 爬取出错，请重试", "电影", tagName, urlDetail);
                    insertErrUrl(urlDetail,tagName,e.getMessage(),"电影");
                    saveErrTempUrl(urlVO,tagName,mark);
                }
            }

        }
    }

    /**
     * 查询电影和电视剧信息
     * @param tagName 标签名
     * @param url URL地址
     */
    private void saveMovieAndTv(String tagName, String url,Integer mark) {
        String result;
        int pageIndex = 0;
        while (true) {
            UrlVO urlVO =null;
            try {
                urlVO = findTempUrl(tagName,mark);
                if(urlVO == null){
                    urlVO = new UrlVO();
                    urlVO.addUrl(url).addSort(SortEnum.RECOMMEND.getCode())
                            .addPageStart(pageIndex* Constant.pageSize)
                            .addPageLimit(Constant.pageSize);
                }else{
                    pageIndex = urlVO.getPageIndex();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info("请求的url : {}",urlVO.getBaseUrl());
            result = findReponse(tagName, mark, urlVO);

            JSONObject obj = JSON.parseObject(result);
            JSONArray array = obj.getJSONArray("subjects");
            if (array.size() == 0) {
                log.warn("爬取完成 pageIndex: {}",pageIndex);
                break;
            }
            pageIndex ++;
            for (int i = 0; i < array.size(); i++) {
                parserAndSave(tagName, url, mark, urlVO, array, i);
            }

        }
    }

    private String findReponse(String tagName, Integer mark, UrlVO urlVO) {
        String result = null ;
        while(true){
            try {
                result = HttpUtil.doGet(urlVO.getBaseUrl(), proxy);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(SLEEPTIM);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HttpHost newProxy = checkProxy(result,proxy);
            if(newProxy == null){
                saveErrTempUrl(urlVO,tagName,mark);
            }
            else if(isSampleProxy(proxy,newProxy)){
                break;
            }
        }
        return result;
    }

    private void parserAndSave(String tagName, String url, Integer mark, UrlVO urlVO, JSONArray array, int index) {
        JSONObject object = array.getJSONObject(index);
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
            saveErrTempUrl(urlVO,tagName,mark);
        }
    }


    /**
     * 获取排行榜中的电影
     * @param tagName 标签名
     * @param url url地址
     */
    private void saveMovieChart(String tagName, String url,Integer mark) {
        String result = null;//先查询总记录数
        UrlVO urlVO = null;
        urlVO = findTempUrl(tagName,mark);
        int currentIntervId = INTERVALID;
        Integer currentIndex = null;
        if(urlVO != null) {
            String intervalId = urlVO.getIntervalId();
            currentIntervId = Integer.parseInt(intervalId.split(":")[0])/10;
            currentIndex = urlVO.getPageIndex();
        }

        urlVO = null;
        for(int i=currentIntervId;i>0;i--){
            String intervalId = i*10+":"+(i-1)*10;
            UrlVO countUrl = new UrlVO();
            try {
                String type = url.split("\\?")[1].split("=")[1];
                countUrl.addUrl(Constant.CHART_TOP_COUNT).addType(Integer.parseInt(type)).addIntervalId(intervalId);
            } catch (Exception e) {
                e.printStackTrace();
            }
                //查询总记录数
                while(true){
                    try {
                        result = HttpUtil.doGet(countUrl.getBaseUrl(), proxy);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(SLEEPTIM);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                   HttpHost newProxy = checkProxy(result,proxy);
                    if(isSampleProxy(proxy,newProxy)){
                        break;
                    }

                }
                Integer total = JSONObject.parseObject(result).getInteger("total");

                log.warn("{} 下的影视有 {} 部",tagName,total);
                //https://movie.douban.com/j/chart/top_list?type=11&interval_id=100%3A90&action=&start=0&limit=20
                Integer pageNum = total%Constant.pageSize==0?total/Constant.pageSize:total/Constant.pageSize+1;

                Integer pageIndex = 0;
                if(currentIndex != null) {
                    pageIndex = currentIndex;
                    currentIndex = null;
                }

                //这边需要进行改变
                for(int j =pageIndex;j<pageNum;j++) {
                    UrlVO nerUrlVO = new UrlVO();
                    try {
                        nerUrlVO.addUrl(url).addIntervalId(intervalId).addStart(j*Constant.pageSize)
                                .addLimit(Constant.pageSize);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String  resultInfo = findReponse(tagName,mark,urlVO);
//                    while(true){
//                        resultInfo = HttpUtil.doGet(nerUrlVO.getBaseUrl(), proxy);
//                        log.info("请求的url:{}",nerUrlVO.getBaseUrl());
//                        try {
//                            Thread.sleep(SLEEPTIM);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        HttpHost newProxy = checkProxy(resultInfo,proxy);
//                        //比较proxy 如果是空则保存，和原来的porxy一样 退出，否则循环处理
//                        if(newProxy == null){
//                            saveErrTempUrl(urlVO,tagName,mark);
//                        }
//                        else if(isSampleProxy(proxy,newProxy)){
//                            break;
//                        }
//
//                    }
                    JSONArray array = JSON.parseArray(resultInfo);
                    for(int t = 0;t<array.size();t++) {
                        parserAndSave(tagName, url, mark, urlVO, array, t);
                    }

                }

        }
    }

    /**
     * 保存因为url 请求ip被封的时候的url地址
     * @param urlVO urlVo对象
     * @param tagName  标签
     * @param mark 标识
     */
    private void saveErrTempUrl(UrlVO urlVO, String tagName, Integer mark) {
        ErrTempurlExample errTempurlExample = new ErrTempurlExample();
        errTempurlExample.createCriteria().andLabelEqualTo(tagName);
        errTempurlExample.createCriteria().andMarkEqualTo(mark);

        //删除以前的数据
        if(errTempurlMapper.deleteByExample(errTempurlExample)>0){
            log.info("删除成功");
        }

        ErrTempurl errTempurl = new ErrTempurl();
        errTempurl.setUrl(urlVO.getUrl());
        errTempurl.setIntervalid(urlVO.getIntervalId());
        errTempurl.setLabel(tagName);
        errTempurl.setMark(mark);
        errTempurl.setSort(urlVO.getSort());
        errTempurl.setType(urlVO.getType());
        Integer pageIndex = null;
        if(mark == MOVIEANDTV){
            pageIndex = urlVO.getPageStart()/Constant.pageSize;
        }
        if(mark == CHART || mark == TAG){
            pageIndex = urlVO.getStart()/Constant.pageSize;
        }

        errTempurl.setPageindex(pageIndex);

        errTempurlMapper.insert(errTempurl);

    }


    public Movie crawlMovie(String tagName, String url) {
        HttpHost proxy = proxyService.findCanUseProxy();
        String result = null;
        while (true){
            try {
                result = HttpUtil.doGet(url, proxy);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(SLEEPTIM);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HttpHost  newProxy = checkProxy(result,proxy);
            if(isSampleProxy(proxy,newProxy)){
                break;
            }
        }

        if(result.equals("404")){
            return  null;
        }

        if(!StringUtils.isEmpty(result) || !result.equals(NOT_FOUND)){
            Document document = Jsoup.parse(result);
            return parseMovie(document,tagName);
        }

       return null;
    }

    public Movie parseMovie(Document document,String tagName) {
        Movie movie =  new Movie();
        movie.setType(tagName);

        String title = document.select("title").text();

        if(title.contains("页面不存在")){
            return null;
        }


        //获取导演 编剧主演的信息
        movie = fillMovieInfo(document, movie);


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
            String movieLength = movie.getMovieLength();
            if(!StringUtils.isEmpty(movieLength)){
                movieLength+=runTime;
            }
            movie.setMovieLength(movieLength);
        }
        String alias = info.get("alias");
        if(!StringUtils.isEmpty(alias)){
            movie.setAlias(alias);
        }

        log.info("movie info {}", JsonUtil.obj2str(movie));
        return  movie;
    }

    private Movie fillMovieInfo(Document document, Movie movie) {
        Elements elements =  document.select("#info > span");
        for(Element element : elements) {
            String text = element.text();
            if (text.contains("导演")) {
                String director = cutOutChar(StringUtils.trimAllWhitespace(text.split(":")[1]),Movie.DIRECTOR_LENGTH);
                movie.setDirector(director);
                continue;
            }
            if (text.contains("主演")) {
                String leadActor = cutOutChar(StringUtils.trimAllWhitespace(text.split(":")[1]),Movie.LEADACTOR_LENGTH);
                movie.setLeadActor(leadActor);
                continue;
            }
            if (text.contains("编剧")) {
                String screenWriter = cutOutChar(StringUtils.trimAllWhitespace(text.split(":")[1]),Movie.SCREENWRITER_LENGTH);
                movie.setScreenWriter(screenWriter);
                continue;
            }
            //判断 如果三者不为null 则直接退出
            if(movie.getDirector()!=null&&movie.getLeadActor()!=null&&movie.getScreenWriter()!=null){
                break;
            }
        }
        //类型 v:genre 上映时间  v:initialReleaseDate 片长  v:runtime
        Elements typeElements =  document.select("#info > span[property=v:genre]");
        if(typeElements.size()>0){
            StringBuilder typebuilder = new StringBuilder();
            for(Element element : typeElements){
                typebuilder.append(element.text()).append("/");
            }
            typebuilder.replace(typebuilder.length()-1,typebuilder.length(),"");
            movie.setTag(typebuilder.toString());
        }

        Elements relaseDataElements =  document.select("#info > span[property=v:initialReleaseDate]");
        if(relaseDataElements.size()>0) {
            StringBuilder relaseDataBuilder = new StringBuilder();
            for (Element element : relaseDataElements) {
                relaseDataBuilder.append(element.text()).append("/");
            }
            relaseDataBuilder.replace(relaseDataBuilder.length() - 1, relaseDataBuilder.length(), "");
            movie.setReleaseTime(cutOutChar(relaseDataBuilder.toString(),Movie.RELEASETIME_LENGTH));
        }
        Elements runTimeElements =  document.select("#info > span[property=v:runtime]");
        if(runTimeElements.size()>0){
            StringBuilder runTimeBuilder = new StringBuilder();
            for(Element element : runTimeElements){
                runTimeBuilder.append(element.text()).append("/");
            }
            runTimeBuilder.replace(runTimeBuilder.length()-1,runTimeBuilder.length(),"");
            movie.setMovieLength(runTimeBuilder.toString());
        }
        //得分 评价人数  图片 电影  名称 剧情 季数
        String ratePeopleStr = document.select("span[property=v:votes]").text();
        if(!StringUtils.isEmpty(ratePeopleStr)){
            Integer ratePeople =  Integer.parseInt(ratePeopleStr);
            movie.setEvaluateNumber(ratePeople);
        }


        String scoreStr = document.select("strong.rating_num").text();
        if(!StringUtils.isEmpty(ratePeopleStr)) {
            Float score =  Float.parseFloat(scoreStr);
            movie.setScore(score);
        }
        String  movieName = document.select("span[property=v:itemreviewed]").text();
        movie.setName(movieName);

        String  imgsrc = document.select("div#mainpic img").attr("src");
        movie.setPicture(imgsrc);

        String  descption = StringUtils.trimAllWhitespace(document.select("span[property=v:summary]").text());
        movie.setSynopsis(descption);

        return  movie;
    }

    /**
     * 从文本中获取想要的电影信息
     * @param document html的DOM对象
     * @return 电影中的制片国家/地区 / 语言: /又名
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

                if("".equals(text)||"/".equals(StringUtils.trimAllWhitespace(text))){
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
        //log.info("info {}",info);
        String[] array =  info.split("\\|");
        Map<String,String> movieInfo = new HashMap<>(5);

        if(array.length>0){
            movieInfo.put("filmmakingArea",array[0]);
        }

        if(array.length==FOUR){

            movieInfo.put("alias",array[3]);
        }

        if(array.length==THREE){
            movieInfo.put("alias",array[2]);
        }
        if(array.length==FIVE){
            movieInfo.put("alias",array[4]);
        }

        for(String msg : array){
            boolean flag = isInteger(msg);
            if(flag){
                movieInfo.put("number",msg);
                continue;
            }
            //语言 不太恰当
            if((msg.contains("语")||msg.contains("語") ||msg.contains("话") || msg.contains("无对白") || msg.toLowerCase().contains("english")|| msg.contains("方言")||msg.contains("英文"))
                    && !msg.contains("分钟")){
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
            }
        }
        return movieInfo;
    }


    public UrlVO findTempUrl(String tagName,Integer mark){
        ErrTempurlExample errTempurlExample = new ErrTempurlExample();
        errTempurlExample.createCriteria().andLabelEqualTo(tagName).andMarkEqualTo(mark);
        errTempurlExample.setOrderByClause("pageIndex ASC");

        List<ErrTempurl> list = errTempurlMapper.selectByExample(errTempurlExample);
        UrlVO urlVO = null;
        if(list != null && list.size()>0){
            ErrTempurl tempurl = list.get(0);
            try {
                urlVO = new UrlVO();
                urlVO.addUrl(tempurl.getUrl()).addType(tempurl.getType()).addSort(tempurl.getSort())
                        .addIntervalId(tempurl.getIntervalid());
                Integer pageIndex = tempurl.getPageindex();
                if(mark == MOVIEANDTV){
                    urlVO.addPageStart(pageIndex*Constant.pageSize);
                    urlVO.addPageLimit(Constant.pageSize);
                }
                if(mark == TAG || mark == CHART){
                    urlVO.addStart(pageIndex*Constant.pageSize);
                    urlVO.addLimit(Constant.pageSize);
                }
                urlVO.setPageIndex(pageIndex);
            } catch (Exception e) {
                e.printStackTrace();
            }
            deleteTempUrl(list.get(0).getId());
        }
        //执行删除操作


        return  urlVO;
    }

    private void deleteTempUrl(Integer id) {
        ErrTempurlExample errTempurlExample = new ErrTempurlExample();
        errTempurlExample.createCriteria().andIdEqualTo(id);
        errTempurlMapper.deleteByExample(errTempurlExample);

    }


    public boolean isSampleProxy(HttpHost proxy,HttpHost newProxy){
         return proxy.getHostName().equals(newProxy.getHostName())&&proxy.getPort()==newProxy.getPort()&&proxy.getSchemeName().equals(proxy.getSchemeName());
    }


    /**
     * 对于字符串信息超过存储长度的进行截取
     * @param info 要存储的信息
     * @param length 最大长度（已经去除空格）
     * @return
     */
    public  String cutOutChar(String info,Integer length){
         Integer realLength = info.length();
         if(realLength > length){
               String infotemp = info.substring(0,length);
              int temp = infotemp.lastIndexOf("/");
               info = infotemp.substring(0,temp);
         }
         return  info;

    }

}
