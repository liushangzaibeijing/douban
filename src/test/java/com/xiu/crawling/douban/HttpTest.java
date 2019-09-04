package com.xiu.crawling.douban;

import com.github.pagehelper.PageHelper;
import com.xiu.crawling.douban.bean.Book;
import com.xiu.crawling.douban.bean.ErrUrl;
import com.xiu.crawling.douban.bean.UrlInfo;
import com.xiu.crawling.douban.bean.UrlInfoExample;
import com.xiu.crawling.douban.common.MarkEnum;
import com.xiu.crawling.douban.core.BookThreadTask;
import com.xiu.crawling.douban.core.service.CrawlingService;
import com.xiu.crawling.douban.core.service.ProxyService;
import com.xiu.crawling.douban.mapper.BookMapper;
import com.xiu.crawling.douban.mapper.ErrUrlMapper;
import com.xiu.crawling.douban.mapper.UrlInfoMapper;
import com.xiu.crawling.douban.proxypool.http.HttpManager;
import com.xiu.crawling.douban.utils.HttpUtil;
import com.xiu.crawling.douban.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author  Administrator
 * date   2018/10/12
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DoubanApplication.class)
@WebAppConfiguration
public class HttpTest {
    @Autowired
    private CrawlingService crawlingService;
    @Autowired
    private UrlInfoMapper urlInfoMapper;
    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private ErrUrlMapper errUrlMapper;

    @Autowired
    private ProxyService proxyService;
    /**
     * 爬去豆瓣首页的数据
     */
    @Test
    public void testDoubanIndex(){
       String result =  HttpManager.get().getResponseStr("https://www.douban.com/");

        Document document = Jsoup.parseBodyFragment(result);

        String regex = "a[href~=https://\\S*.douban.*]";
        Elements elements = document.select(regex);

        HashMap<String,UrlInfo> data = new HashMap<>();

        for(Element node:elements){
            String url = node.attr("href");
            String desc = node.text();
            UrlInfo urlInfo = new UrlInfo();
            if(url!=null){
               urlInfo.setUrl(url);
                if(desc!=null){
            }
                urlInfo.setDescption(desc);
            }
            data.put(url,urlInfo);
        }

        log.info("需要爬取得数据信息 {}",JsonUtil.obj2str(data));

        //遍历map
        for(Map.Entry entry:data.entrySet()){
            UrlInfo urlInfo = (UrlInfo) entry.getValue();

            urlInfoMapper.insertSelective(urlInfo);
        }
    }


    /**
     * 爬去豆瓣读书模块的url
     */
    @Test
    public void testDoubanBook() {
        String result = HttpManager.get().getResponseStr("https://book.douban.com/");

        log.info(result);


        Document document = Jsoup.parseBodyFragment(result);

        String regex = "a[href~=https://\\S*.douban.*]";
        Elements elements = document.select(regex);

        HashMap<String,UrlInfo> data = new HashMap<>();

        for(Element node:elements){
            String url = node.attr("href");
            String desc = node.text();
            UrlInfo urlInfo = new UrlInfo();
            if(url!=null){
                urlInfo.setUrl(url);
                if(desc!=null){
                }
                urlInfo.setDescption(desc);
            }
            data.put(url,urlInfo);
        }

        log.info("需要爬取得数据信息 {}",JsonUtil.obj2str(data));

        //遍历map
        for(Map.Entry entry:data.entrySet()){
            UrlInfo urlInfo = (UrlInfo) entry.getValue();

            urlInfoMapper.insertSelective(urlInfo);
        }

    }


    /**
     * 爬去豆瓣读书模块的url
     */
    @Test
    public void testDoubanBookTagList() {
        String result = HttpManager.get().getResponseStr("https://book.douban.com/tag/?view=type&icn=index-sorttags-hot");
        log.info(result);

        Document document = Jsoup.parseBodyFragment(result);

        String regex = "a[href~=/tag/\\S*]";
        Elements elements = document.select(regex);

        HashMap<String,UrlInfo> data = new HashMap<>();

        for(Element node:elements){
            String bookUrl = node.attr("href");
            //书籍中的标签列表 可能没有基础的url地址 所以需要进行添加操作 baseURL
            if(!bookUrl.contains("https://book.douban.com")){
                bookUrl = "https://book.douban.com"+bookUrl;
            }
            //TODO 需要过滤掉相关的不合法的请求url 这里先进行简单的实现（以后会有相关的过滤请求的借口来进行处理）
            //https://book.douban.com/tag/?view=cloud
            if(bookUrl.contains("https://book.douban.com/tag/?")){
                //不符合的请求直接进行下次循环
                continue;
            }

            String desc = node.text();
            UrlInfo urlInfo = new UrlInfo();
            if(bookUrl!=null){
                urlInfo.setUrl(bookUrl);
                if(desc!=null){
                }
                urlInfo.setDescption(desc);
            }
            urlInfo.setActive(1);
            data.put(bookUrl,urlInfo);
        }

        log.info("需要爬取得数据信息 {}",JsonUtil.obj2str(data));

        //遍历map
        for(Map.Entry entry:data.entrySet()){
            UrlInfo urlInfo = (UrlInfo) entry.getValue();
            //先查询是否重复，重复不进行插入操作
            UrlInfoExample example = new UrlInfoExample();
            example.createCriteria().andUrlEqualTo(urlInfo.getUrl());
            List<UrlInfo> urlInfos = urlInfoMapper.selectByExample(example);
            if(urlInfos==null || urlInfos.size()==0){
                urlInfoMapper.insertSelective(urlInfo);
            }
        }

    }

    /**
     <div class="pic">
     <a class="nbg" href="https://book.douban.com/subject/6082808/" onclick="moreurl(this,{i:'0',query:'',subject_id:'6082808',from:'book_subject_search'})">
     <img class="" src="https://img3.doubanio.com/view/subject/m/public/s6384944.jpg" width="90">
     </a>
     </div>
     //书名等信息

     <div class="info">
     <h2 class="">
     <a href="https://book.douban.com/subject/3259440/" title="白夜行" onclick="moreurl(this,{i:'0',query:'',subject_id:'3259440',from:'book_subject_search'})">
     白夜行
     </a>
     </h2>
     <div class="pub">
     [日] 东野圭吾 / 刘姿君 / 南海出版公司 / 2008-9 / 29.80元
     </div>
     <div class="star clearfix">
     <span class="allstar45"></span>
     <span class="rating_nums">9.1</span>
     <span class="pl">
     (233024人评价)
     </span>
     </div>
     <p>“只希望能手牵手在太阳下散步”，这个象征故事内核的绝望念想，有如一个美丽的幌子，随着无数凌乱、压抑、悲凉的故事片段像纪录片一样一一还原：没有痴痴相思，没有海... </p>
     <div class="ft">
     <div class="collect-info">
     </div>
     <div class="cart-actions">
     </div>
     </div>
     </div>
     */
    /**
     * 爬去豆瓣读书模块的url 具体分页的书籍信息
     */
    @Test
    public void testDoubanBookInfoList() {
        String result = HttpManager.get().getResponseStr("https://book.douban.com/tag/文学");
        log.info(result);

        Document document = Jsoup.parseBodyFragment(result);

        //<li class="subject-item"
        Elements elements = document.getElementsByClass("subject-item");

        List<Book> books = new ArrayList<>();
        for(Element node:elements){
            //获取图片
            Elements  picNode = node.select("div.pic img");

            String imgUrl = picNode.get(0).attr("src");

            //简介信息过少情况下 爬取到该页面进行爬取书籍简介信息
            String  contentUrl = node.select("div.pic a").attr("href");

            String bookName = node.select("div.info.a").text();
            //去掉空格
            String data = node.select("div.pub").text().replaceAll(" ","");
            //String data = node.select("div.pub").text().trim();

            //从左到右 一次是 作者，译者， 出版社 出版日期，价格
            String[] array = data.split("/");


            //评分
            Double score = Double.parseDouble(node.select("span.rating_nums").text());
            //评论人数
            String numStr = node.select("span.pl").text().replace("人评价","")
                        .replace("(","")
                        .replace(")","");
            Integer evaluateNumber = Integer.parseInt(numStr);

            //描述
            String descption = node.select("p").text();

            String author = null;
            String translator = null;
            String publicHouse = null;
            String publicDate = null;
            String price = null;

            if(array.length==4){
                //没有译者信息（国产作品）
                author = array[0];
                publicHouse =array[1];
                publicDate = array[2];
                //使用正则匹配数字
                price =  array[3];
            }
            if(array.length==5) {
                author = array[0];
                translator =array[1];
                publicHouse =array[2];
                publicDate =array[3];
                price =  array[4];
            }
            Book book = new Book();
            book.setName(bookName);
            book.setAuthor(author);
            book.setTranslator(translator);
            book.setPublisHouse(publicHouse);
            book.setPublicationDate(publicDate);
            book.setPrice(price);
            book.setScore(score);
            book.setPicture(imgUrl);
            book.setDescption(descption);

            log.info("书籍信息：{}",JsonUtil.obj2str(book));

            books.add(book);

        }

        //log.info("书籍信息：{}",JsonUtil.obj2str(books));

    }

    @Test
    public void testBookTask(){
        BookThreadTask task = new BookThreadTask("文学","https://book.douban.com/tag/文学",bookMapper,urlInfoMapper,errUrlMapper,null,null);
        Thread thead = new Thread(task);
        thead.start();
    }


    /**
     * 测试插入数据出错后，捕获错误处理的时候将该数据存储
     */
    @Test
    public void testInsertAfterException() {

        ErrUrl errUrl = new ErrUrl();
        errUrl.setId(1);
        errUrl.setName("两性认知");
        errUrl.setErrorUrl("htttp://wwww.test.com");
        errUrl.setInfo("测试书籍信息");
        errUrl.setModule("书籍");

        try {
            errUrlMapper.insert(errUrl);
        }catch (Exception e){
            ErrUrl errUrlT = new ErrUrl();
            errUrlT.setName("两性认知");
            errUrlT.setErrorUrl("htttp://wwww.test.com");
            errUrlT.setInfo("爬取该信息出现错误");
            errUrlT.setModule("书籍");
            errUrlMapper.insert(errUrlT);
        }




    }


    private Double getNumberByRegex(String input) {
        Double price = null;
        //使用正则匹配数字

        String regex = "(\\d+)(.?)(\\d*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(input);
        if(m.find()){
            log.info("find {}", m.group(0));
            price = Double.parseDouble(m.group(0));
        }else{
            log.info("no find");
        }
        return price;
    }


    @Test
    public void testHttpProxy() throws IOException {
         HttpHost proxy = proxyService.findCanUseProxy();
        CloseableHttpClient httpClient= HttpClients.createDefault();// 创建httpClient实例
      HttpGet httpGet=new HttpGet("https://www.taobao.com/"); // 创建httpget实例
        RequestConfig requestConfig=RequestConfig.custom().setProxy(proxy).build();
        httpGet.setConfig(requestConfig);
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
        CloseableHttpResponse response=httpClient.execute(httpGet);// 执行http get请求
        HttpEntity entity=response.getEntity(); // 获取返回实体
        System.out.println("网页内容："+ entity);// 获取网页内容
        response.close();// response关闭
         httpClient.close();// httpClient关闭

    }

    @Test
    public void testCrawlingMovie() throws IOException {
        try {
            crawlingService.crawlingMovie   ();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrawlingMovieInfo() throws IOException {
        PageHelper.startPage(1,5);
        UrlInfoExample urlInfoExample = new UrlInfoExample();
        urlInfoExample.createCriteria().andMarkEqualTo(MarkEnum.TV_MOVIE.getCode());
        List<UrlInfo> urlInfos = urlInfoMapper.selectByExample(urlInfoExample);

        log.info("urlinfos {}",JsonUtil.obj2str(urlInfos));

    }

    @Test
    public void testCanUserProxy() throws IOException {
        HttpHost proxy = proxyService.findCanUseProxy();
        String result = HttpUtil.doGet("https://movie.douban.com/j/search_subjects?type=movie&tag=热门",null);

        log.info("result {}",result);


    }

}
