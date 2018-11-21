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
public class CrawlingMovieTest {

    @Test
    public void parseMovie(){
        String url = "https://movie.douban.com/subject/26694491/";
        String result = HttpUtil.doGet(url,null);


        MovieThreadTask task = new MovieThreadTask(null,null,null,null,null,null,null,null,null,null);
        Movie movie = task.parseMovie( Jsoup.parse(result),"");

        log.info("movie info {}",JsonUtil.obj2str(movie));
    }

    public Movie parseMovie(Document document, String tagName) {
        Movie movie =  new Movie();
        movie.setType(tagName);
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
        log.info("info {}",info);
        String[] array =  info.split("\\|");
        Map<String,String> movieInfo = new HashMap<>(5);

        if(array.length>0){
            movieInfo.put("filmmakingArea",array[0]);
        }

        if(array.length==4){

            movieInfo.put("alias",array[3]);
        }

        if(array.length==3){
            movieInfo.put("alias",array[2]);
        }
        if(array.length==5){
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
            }
        }
        return movieInfo;
    }


    @Test
    public void testAAA(){
        String result = "法国 / 德国 / 俄罗斯 / 立陶宛 / 荷兰 / 乌克兰 / 拉脱维亚";
        log.info("length :  {}",result.length());
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static boolean isCharterAndNum(String str) {
        Pattern pattern = Pattern.compile("^[0-9a-zA-Z]*$");
        return pattern.matcher(str).matches();
    }

    private Movie fillMovieInfo(Document document, Movie movie) {
        Elements elements =  document.select("#info > span");
        for(Element element : elements) {
            String text = element.text();
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
        //类型 v:genre 上映时间  v:initialReleaseDate 片长  v:runtime
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
        //得分 评价人数  图片 电影  名称 剧情 季数
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

        return  movie;
    }

}