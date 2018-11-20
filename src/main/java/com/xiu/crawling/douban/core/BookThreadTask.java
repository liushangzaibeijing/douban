package com.xiu.crawling.douban.core;

import com.xiu.crawling.douban.bean.*;
import com.xiu.crawling.douban.bean.vo.Result;
import com.xiu.crawling.douban.common.ActiveEnum;
import com.xiu.crawling.douban.core.service.AbstractThreadTask;
import com.xiu.crawling.douban.core.service.ProxyService;
import com.xiu.crawling.douban.mapper.BookMapper;
import com.xiu.crawling.douban.mapper.ErrUrlMapper;
import com.xiu.crawling.douban.mapper.UrlInfoMapper;
import com.xiu.crawling.douban.proxypool.job.ScheduleJobs;
import com.xiu.crawling.douban.utils.HttpUtil;
import com.xiu.crawling.douban.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * @author  xieqx
 * date   2018/10/13
 * desc 读取书籍信息的子线程 每一个分类tag 用一个线程进行读取
 */
@Slf4j
public class BookThreadTask extends AbstractThreadTask implements  Runnable{
    private  static final String baseUrl = "https://book.douban.com";
    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 标签的url
     */
    private String url;

    /**
     * 持久化的book对象
     */
    private BookMapper bookMapper;

    /**
     * 存储url信息
     */
    private UrlInfoMapper urlInfoMapper;

    /**
     * 并行操作
     */
    private CountDownLatch latch;

    /**
     *用于存储可用的代理对象
     */
    private HttpHost proxy;


    public BookThreadTask(String tagName, String url, BookMapper bookMapper,UrlInfoMapper urlInfoMapper,ErrUrlMapper errUrlMapper,ProxyService proxyService,ScheduleJobs scheduleJobs) {
        super(proxyService,errUrlMapper,scheduleJobs);
        this.tagName = tagName;
        this.url = url;
        this.bookMapper = bookMapper;
        this.urlInfoMapper = urlInfoMapper;

    }

    public BookThreadTask(String tagName, String url, BookMapper bookMapper, UrlInfoMapper urlInfoMapper, ErrUrlMapper errUrlMapper, ProxyService proxyService,CountDownLatch latch,ScheduleJobs scheduleJobs) {
        super(proxyService,errUrlMapper,scheduleJobs);
        this.tagName = tagName;
        this.url = url;
        this.bookMapper = bookMapper;
        this.urlInfoMapper = urlInfoMapper;
        this.latch = latch;
    }

    /**
     * 支持首页列表 以及第几页数据的爬取操作
     */
    @Override
    public void run() {
        try {
            //开始爬取以及持久化
            crawlBook(this.tagName,this.url);
            //爬取结束后将该url的状态置为已经爬取过 模糊查询
            endCrawl(this.tagName, this.url);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            //该并行线程结束
            latch.countDown();
        }

    }

    /**
     * 设置爬取的url信息状态为已经爬取完成
     * @param url url地址
     */
    private void endCrawl(String tagName,String url) {
        UrlInfoExample urlInfoExample = new UrlInfoExample();
        urlInfoExample.createCriteria().andUrlEqualTo(url);

        UrlInfo urlInfo = new UrlInfo();
        urlInfo.setActive(ActiveEnum.NO_ACTIVE.getCode());
        Integer count = urlInfoMapper.updateByExampleSelective(urlInfo,urlInfoExample);
        if(count > 0){
            log.info("{} 标签下的书籍信息爬取完毕",tagName);
        }
    }

    /**
     * 爬取书籍信息
     * @param url url地址
     */
    private  void crawlBook(String tagName,String url){
        proxy = proxyService.findCanUseProxy();
        String result = null;
        while(true) {
            //获取HttpHost 从
            result = HttpUtil.doGet(url,proxy);

            proxy = checkProxy(result,proxy);
            if(proxy==null){
                break;
            }
        }
        Document document = Jsoup.parseBodyFragment(result);
        List<Book> books = null;
        String name = null;
        try {
            //解析书籍信息
            books = parseBooks(document);
            if (books != null && books.size() > 0) {
                for (Book book : books) {
                    //先查询是否重复，重复不进行插入操作
                    BookExample example = new BookExample();
                    example.createCriteria().andNameEqualTo(book.getName());
                    List<Book> bookList = bookMapper.selectByExample(example);
                    if (bookList == null || bookList.size() == 0) {
                        name = book.getName();
                        bookMapper.insertSelective(book);

                    }
                }
            }
        } catch (Exception e) {
            log.info("{} 模块下的 {} 中的 {} 爬取出错，请重试", "书籍", name, url);
            insertErrUrl(url, name, e.getMessage(),"书籍");
        }

            //默认只有二十个每页，所以需要获取到总的记录条数，循环改变start去获取

            //获取后一页的url信息 如果没有说明爬取书籍中已经没有了相关的信息 不需要重构url进行爬取信息
            //GET /tag/文学?start=40&type=T HTTP/1.1
            if (books != null && books.size() != 0) {
                String apiUrl = document.select("div.paginator span.next link").attr("href");
                //书籍中的标签列表 可能没有基础的url地址 所以需要进行添加操作 baseURL
                if (!apiUrl.contains(baseUrl)) {
                    apiUrl = baseUrl + apiUrl;
                    log.info(apiUrl);

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    crawlBook(tagName, apiUrl);
                }
            }
    }


    /**
     * 解析书籍信 从文档中获取书籍信息
     * @param document html的DOM对象
     * @return Book 集合对象
     */
    private List<Book> parseBooks(Document document) throws  Exception{
        //<li class="subject-item"
        Elements elements = document.getElementsByClass("subject-item");
        //先判断是否为一些限制ip的脚本
        if(elements.size()==0){
            log.info("请求信息：{}",document.text());
            return null;
        }
        List<Book> books = new ArrayList<>();
        for(Element node:elements){
            //获取图片
            Elements  picNode = node.select("div.pic img");
            String imgUrl = picNode.get(0).attr("src");

            //简介信息过少情况下 爬取到该页面进行爬取书籍简介信息String  contentUrl = node.select("div.pic a").attr("href");//获取书名 info
            String bookName = node.select("div.info a[title]").text();
            //评分
            String scoreS = node.select("span.rating_nums").text();
            Double score = null;
            if(!StringUtils.isEmpty(scoreS)){
                score = Double.parseDouble(scoreS);
            }
            //评论人数
            Integer evaluateNumber = getIntByRegex(node.select("span.pl").text());
            //描述
            String descption = node.select("p").text();
            //去掉空格
            String data = node.select("div.pub").text().replaceAll(" ","");
            //从左到右 一次是 作者，译者， 出版社 出版日期，价格
            String[] array = data.split("/");
            Book book = fillBookByArray(array);
            book.setName(bookName);
            book.setEvaluateNumber(evaluateNumber);
            book.setScore(score);
            book.setPicture(imgUrl);
            book.setDescption(descption);
            //book.setTag
            book.setTag(tagName);

            books.add(book);
        }
        log.info("书籍信息：{}", JsonUtil.obj2str(books));
        return books;
    }

    private Book fillBookByArray(String[] array) {
        Book book = new Book();

        String translator = null;
        String publicHouse = null;
        String publicDate = null;
        String price = null;
        String author = null;

        if(array.length==ONE ||array.length>SIX){
            //没有publicHouse 或者没有出版时间
            author = array[0];
         }
        if(array.length==THREE){
            //没有publicHouse 或者没有出版时间
            author = array[0];
            //如何判断是否是出版社，还是出版时间 如果含有数字则为出版时间，否则是出版社
            //isContainNumber
            boolean flag = isContainNumber(array[1]);
            if(flag){
                publicDate = array[1];
            }else{
                publicHouse = array[1];
            }
            //使用正则匹配数字
            price = array[2];
        }
        if(array.length==FOUR){
            //没有译者信息（国产作品） // 作者，译者， 出版社 出版日期，价格
            author = array[0];
            publicHouse =array[1];
            publicDate = array[2];
            //使用正则匹配数字
            price = array[3];
        }
        if(array.length==FIVE) {
            author = array[0];
            translator =array[1];
            publicHouse =array[2];
            publicDate = array[3];
            price = array[4];
        }
        //包含6个数组的 可能包含两个作者 且使用了/分隔
        if(array.length==SIX) {
            author = array[0]+"、"+array[1];
            translator =array[2];
            publicHouse =array[3];
            publicDate = array[4];
            price = array[5];
        }

        book.setAuthor(author);
        book.setTranslator(translator);
        book.setPublisHouse(publicHouse);
        book.setPublicationDate(publicDate);
        book.setPrice(price);
        return book;
    }


}
