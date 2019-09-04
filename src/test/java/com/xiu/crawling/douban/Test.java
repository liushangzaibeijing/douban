package com.xiu.crawling.douban;

import com.xiu.crawling.douban.bean.Movie;
import com.xiu.crawling.douban.bean.dto.Data;
import com.xiu.crawling.douban.core.BookThreadTask;
import com.xiu.crawling.douban.proxypool.domain.Page;
import com.xiu.crawling.douban.utils.HttpUtil;
import com.xiu.crawling.douban.utils.JsonUtil;
import com.xiu.crawling.douban.utils.RandCookie;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
         log.info("info {}",info);
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
        String result = HttpUtil.doGet(url,null);
        Movie movie = null;
        if(!StringUtils.isEmpty(result)){
           Document document = Jsoup.parse(result);
           movie = new Movie();

           //获取导演 编剧主演的信息
           Elements elements =  document.select("#info > span");
           for(int i = 0; i<elements.size();i++) {
               String text = elements.get(i).text();
               log.info(text);
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
        String urldecoder = "https://u.y.qq.com/cgi-bin/musicu.fcg?-=getUCGI08264852810353918&g_tk=5381&loginUin=1374523006&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0&data=%7B%22comm%22%3A%7B%22ct%22%3A24%2C%22cv%22%3A0%7D%2C%22singer%22%3A%7B%22method%22%3A%22get_singer_detail_info%22%2C%22param%22%3A%7B%22sort%22%3A5%2C%22singermid%22%3A%22001fNHEf1SFEFN%22%2C%22sin%22%3A0%2C%22num%22%3A10%7D%2C%22module%22%3A%22music.web_singer_info_svr%22%7D%7D";
        String url = URLDecoder.decode(urldecoder, "UTF-8");
        log.info("查询url:{}",url);

     }


     @org.junit.Test
     public void testParse(){
        String url =
                "https://c.y.qq.com/splcloud/fcgi-bin/fcg_get_singer_desc.fcg?singermid=004Be55m1SJaLk&utf8=1&outCharset=utf-8&format=xml&r=1567573980893";

         Long time = 1567573446498L;
        Date date =  new Date(time);
        String result = HttpUtil.doGet(url);
        log.info("请求结果信息：{}",result);
     }




}
