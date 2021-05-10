package com.xiu.crawling.douban;

import com.alibaba.fastjson.JSONObject;
import com.xiu.crawling.douban.bean.Movie;
import com.xiu.crawling.douban.core.BookThreadTask;
import com.xiu.crawling.douban.utils.ChineseIndex;
import com.xiu.crawling.douban.utils.HttpUtil;
import com.xiu.crawling.douban.utils.JsonUtil;
import com.xiu.crawling.douban.utils.RandCookie;
import lombok.extern.slf4j.Slf4j;
import org.apache.xerces.impl.dv.util.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * author   xieqx
 * createTime  2018/10/14
 * desc 一句话描述该功能
 */
@Slf4j
public class Test {

    public static void main(String[] args){
        BookThreadTask task = new BookThreadTask("文学","https://book.douban.com/tag/文学",null,null,null,null,null);
        Thread thead = new Thread(task);
        thead.start();
    }

    @org.junit.Test
    public void test(){
      String str = "Understanding Your Users : A Practical Guide to User Requirements Methods, Tools, and Techniques (The Morgan Kaufmann Serie)";
      System.out.println(str.length());
    }

    @org.junit.Test
    public void testCookie(){
        RandCookie.getRandomCode(11);
    }


    @org.junit.Test
    public  void main() throws Exception {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, null, null);

        SSLSocketFactory factory = (SSLSocketFactory) context.getSocketFactory();
        SSLSocket socket = (SSLSocket) factory.createSocket();

        String[] protocols = socket.getSupportedProtocols();

        System.out.println("Supported Protocols: " + protocols.length);
        for (int i = 0; i < protocols.length; i++) {
            System.out.println(" " + protocols[i]);
        }

        protocols = socket.getEnabledProtocols();

        System.out.println("Enabled Protocols: " + protocols.length);
        for (int i = 0; i < protocols.length; i++) {
            System.out.println(" " + protocols[i]);
        }

    }


    @org.junit.Test
    public void testParseMovieHtml(){
        String[] arrayUrl = {"https://movie.douban.com/subject/30228909/"};
        for(String url:arrayUrl){
            parseMovie(url);
        }



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
                 String text = StringUtils.trimAllWhitespace(node.text());
                 if(text.equals("")||StringUtils.trimAllWhitespace(text).equals("/")){
                     continue;
                 }
                 textBuilder.append(text).append("|");
             }
             textBuilder.replace(textBuilder.length()-1,textBuilder.length(),"");
         }

         // 中国大陆|汉语普通话|36|45分钟|原来你还在这里电视剧版
         //截取 出来的长度 3  4 有集数
        String info = textBuilder.toString();
//         log.info("info {}",info);
        String[] array =  info.split("\\|");
        Map<String,String> movieInfo = new HashMap<>();
        if(array.length==4){
            movieInfo.put("filmmakingArea",array[0]);
            movieInfo.put("language",array[1]);
            movieInfo.put("number",array[2]);
            movieInfo.put("alias",array[3]);
        }

        if(array.length==3){
            movieInfo.put("filmmakingArea",array[0]);
            movieInfo.put("language",array[1]);
            movieInfo.put("alias",array[2]);
        }
        if(array.length==4){
            movieInfo.put("filmmakingArea",array[0]);
            movieInfo.put("language",array[1]);
            movieInfo.put("number",array[2]);
            movieInfo.put("runTime",array[3]);
        }
        if(array.length==5){
            movieInfo.put("filmmakingArea",array[0]);
            movieInfo.put("language",array[1]);
            movieInfo.put("number",array[2]);
            movieInfo.put("runTime",array[3]);
            movieInfo.put("alias",array[4]);
        }

        return movieInfo;
    }

    private Movie parseMovie(String url) {
        String result = null;
        try {
            result = HttpUtil.doGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Movie movie = null;
        if(!StringUtils.isEmpty(result)){
           Document document = Jsoup.parse(result);
           movie = new Movie();

           //获取导演 编剧主演的信息
           Elements elements =  document.select("#info > span");
           for(int i = 0; i<elements.size();i++) {
               String text = elements.get(i).text();
//               log.info(text);
               if (text.contains("导演")) {
                   movie.setDirector(StringUtils.trimAllWhitespace(text.split(":")[1]));
               }
               if (text.contains("主演")) {
                   movie.setLeadActor(StringUtils.trimAllWhitespace(text.split(":")[1]));
               }
               if (text.contains("编剧")) {
                   movie.setScreenWriter(StringUtils.trimAllWhitespace(text.split(":")[1]));
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


           }
        log.info("movie info {}", JsonUtil.obj2str(movie));
        return  movie;
    }


    @org.junit.Test
     public void testAAA(){
        String result = "波兰语 / 法语 / 德语 / 克罗地亚语 / 意大利语 / 俄语";
        log.info("length :  {}",result.length());
     }

     @org.junit.Test
    public void testUrlDecoder() throws UnsupportedEncodingException {
        String urldecoder = "https://u.y.qq.com/cgi-bin/musicu.fcg?-=getplaysongvkey509352456976849&g_tk=5381&loginUin=1374523006&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0&data=%7B%22req%22%3A%7B%22module%22%3A%22CDN.SrfCdnDispatchServer%22%2C%22method%22%3A%22GetCdnDispatch%22%2C%22param%22%3A%7B%22guid%22%3A%227254122593%22%2C%22calltype%22%3A0%2C%22userip%22%3A%22%22%7D%7D%2C%22req_0%22%3A%7B%22module%22%3A%22vkey.GetVkeyServer%22%2C%22method%22%3A%22CgiGetVkey%22%2C%22param%22%3A%7B%22guid%22%3A%227254122593%22%2C%22songmid%22%3A%5B%220039MnYb0qxYhV%22%5D%2C%22songtype%22%3A%5B0%5D%2C%22uin%22%3A%221374523006%22%2C%22loginflag%22%3A1%2C%22platform%22%3A%2220%22%7D%7D%2C%22comm%22%3A%7B%22uin%22%3A%221374523006%22%2C%22format%22%3A%22json%22%2C%22ct%22%3A24%2C%22cv%22%3A0%7D%7D";
        String url = URLDecoder.decode(urldecoder, "UTF-8");
        log.info("查询url:{}",url);

         String index = ChineseIndex.getFirstLetter("李荣浩");
         log.info("歌手首字母:{}",index.toUpperCase());
     }


     @org.junit.Test
     public void testParse() {

         String[] singerList = {"001fNHEf1SFEFN"};
         for (String id : singerList) {
             String url =
                     "https://c.y.qq.com/splcloud/fcgi-bin/fcg_get_singer_desc.fcg?singermid=004U5z7s4ZvRNq&utf8=1&outCharset=utf-8&format=xml&r="+new Date().getTime();
             /**
              Accept-Encoding: gzip, deflate, br
              Accept-Language: zh-CN,zh;q=0.9
              */
             Map<String, String> headers = new HashMap<>();
             //         headers.put("Host","c.y.qq.com");
             //         headers.put("Connection","keep-alive");
             //         headers.put("Sec-Fetch-Mode","cors");
             //         headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36");
             //         headers.put("Accept","same-origin");
             headers.put("Referer", "https://c.y.qq.com/xhr_proxy_utf8.html");
             //         headers.put("Cookie","pgv_pvi=6434184192; RK=A+y0Ekf4c+; ptcz=1298d2ea8ab28c3f1ed5b5072d2d5474aa1ac0292e5148c93d8d3dd74207c8c4; tvfe_boss_uuid=ec09c7142a8a1107; ts_uid=883180610; pgv_pvid=4956931700; pgv_info=ssid=s7000389619; pgv_si=s4030064640; userAction=1; ts_last=y.qq.com/portal/singer_list.html; yqq_stat=0");


             String result = HttpUtil.doGetByHeader(url, headers);
             log.info("歌手id 请求结果信息：{}", result);
         }


     }


     //解析歌词
    @org.junit.Test
    public void parseLyric(){
        String url = "https://c.y.qq.com/lyric/fcgi-bin/fcg_query_lyric_new.fcg?-=MusicJsonCallback_lrc&pcachetime="+new Date().getTime()+"&songmid=001a69Fl3h6bpK&g_tk=5381&loginUin=1374523006&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0";
                    url =  "https://c.y.qq.com/lyric/fcgi-bin/fcg_query_lyric_new.fcg?-=MusicJsonCallback_lrc&pcachetime=1568472549423&songmid=001rBM4W0oL0CXXr&g_tk=5381&loginUin=1374523006&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0";
        String lyric = getLyric(url);
        if (lyric == null) return;

        byte[] decode = Base64.decode(lyric);

        String result2 = new String (decode);

        log.info("歌词信息：{}",result2);


    }

    private String getLyric(String url) {
        Map<String,String> headers = new HashMap<>();
        headers.put("Referer"," https://y.qq.com/portal/player.html");
        String result = HttpUtil.doGetByHeader(url,headers);
        log.info(result);

        JSONObject lyricJson = JSONObject.parseObject(result);
        String lyric = lyricJson.getString("lyric");
        if(StringUtils.isEmpty(lyric)){
            return null;
        }
        return lyric;
    }


    @org.junit.Test
    public void testAutomicInt(){
        AtomicInteger atomicInteger = new AtomicInteger(1);

        log.info("get(): {}",atomicInteger.get());
        log.info("getAndIncrement(): {}",atomicInteger.getAndIncrement());
        log.error("incrementAndGet(): {}",atomicInteger.incrementAndGet());



    }


    @org.junit.Test
    public void testVipDown(){
        Date time = new Date();
        Long timeL = time.getTime();
        String url = "https://ws.y.qq.com/cgi-bin/musics.fcg?pcachetime="+timeL;
        Map<String,String> headers = new HashMap<>();
        headers.put("Mask","qGHc2HKDjY0BEj5N0/PlMrUSEhSBdLv/ovQbcI0xhmvOx+cUTeOYBVHLd1qaePgUGO3UDf7ndmDndenurQ6saASKxbxkKl7T4DibJsi0jJQ=");
        headers.put("Sign","RVFVRlNUUEZRS1dUZLuqtfDDYQuhDYG2E+KntRqMpR8=");

        String json = "{\"comm\":{\"ct\":\"19\",\"cv\":\"1712\",\"patch\":\"118\",\"uin\":\"1374523006\",\"wid\":\"7585820891367763367\"},\"queryvkey\":{\"method\":\"CgiGetEVkey\",\"module\":\"vkey.GetEVkeyServer\",\"param\":{\"checklimit\":0,\"ctx\":1,\"downloadfrom\":0,\"filename\":[\"F0M0002qU5aY3Qu24y.mflac\",\"F0M0002MXZNu1GToOk.mflac\"],\"guid\":\"ABB0D1BD7886FB31112EE64C48EEFC32\",\"musicfile\":[\"F0M0002qU5aY3Qu24y.mflac\",\"F0M0002MXZNu1GToOk.mflac\"],\"nettype\":\"\",\"referer\":\"y.qq.com\",\"scene\":0,\"songmid\":[\"002qU5aY3Qu24y\",\"002MXZNu1GToOk\"],\"songtype\":[1,1],\"uin\":\"1374523006\"}}}";

        String result  = HttpUtil.doPostSSL(url, json);
        log.info("result:{}",result);

    }
}
