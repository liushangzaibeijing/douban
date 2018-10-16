package com.xiu.crawling.douban.core;

import com.xiu.crawling.douban.bean.Book;
import com.xiu.crawling.douban.bean.BookExample;
import com.xiu.crawling.douban.bean.UrlInfo;
import com.xiu.crawling.douban.bean.UrlInfoExample;
import com.xiu.crawling.douban.common.ActiveEnum;
import com.xiu.crawling.douban.mapper.BookMapper;
import com.xiu.crawling.douban.mapper.UrlInfoMapper;
import com.xiu.crawling.douban.utils.HttpUtil;
import com.xiu.crawling.douban.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author  xieqx
 * date   2018/10/13
 * desc 读取书籍信息的子线程 每一个分类tag 用一个线程进行读取
 */
@Slf4j
public class BookThreadTask implements  Runnable{
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
     * 持久化的url对象
     */
    private UrlInfoMapper urlInfoMapper;

    /**
     * 并行操作
     */
    private CountDownLatch latch;


    public BookThreadTask(String tagName, String url, BookMapper bookMapper,UrlInfoMapper urlInfoMapper) {
        this.tagName = tagName;
        this.url = url;
        this.bookMapper = bookMapper;
        this.urlInfoMapper = urlInfoMapper;
    }
    public BookThreadTask(String tagName, String url, BookMapper bookMapper,UrlInfoMapper urlInfoMapper,CountDownLatch countDownLatch) {
        this.tagName = tagName;
        this.url = url;
        this.bookMapper = bookMapper;
        this.urlInfoMapper = urlInfoMapper;
        this.latch = countDownLatch;
    }



    /**
     * 支持首页列表 以及第几页数据的爬取操作
     */
    @Override
    public void run() {
        try {
            //开始爬取以及持久化
            crawlBook(this.url);
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
     * @param url
     */
    private void endCrawl(String tagName,String url) {
        UrlInfoExample urlInfoExample = new UrlInfoExample();
        urlInfoExample.createCriteria().andUrlEqualTo(url);

        UrlInfo urlInfo = new UrlInfo();
        urlInfo.setActive(ActiveEnum.NO_ACTIVE.getCode());
        Integer count = urlInfoMapper.updateByExampleSelective(urlInfo,urlInfoExample);
        if(count!=null){
            log.info("{} 标签下的书籍信息爬取完毕",tagName);
        }
    }

    /**
     * 爬取书籍信息
     * @param url
     */
    private  void crawlBook(String url){
        String result = HttpUtil.doGet(url);
        //log.info(result);
        Document document = Jsoup.parseBodyFragment(result);
        //解析书籍信息
        List<Book> books = parseBooks(document);

        //循环插入
        if(books!=null && books.size()>0) {
            for (Book book : books) {
                //先查询是否重复，重复不进行插入操作
                BookExample example = new BookExample();
                example.createCriteria().andNameEqualTo(book.getName());
                List<Book> bookList = bookMapper.selectByExample(example);
                if (bookList == null || bookList.size() == 0) {
                    bookMapper.insertSelective(book);
                }
            }
        }
        //默认只有二十个每页，所以需要获取到总的记录条数，循环改变start去获取

        //获取后一页的url信息 如果没有说明爬取书籍中已经没有了相关的信息 不需要重构url进行爬取信息
        //GET /tag/文学?start=40&type=T HTTP/1.1
        /**
         <div class="paginator">
         <span class="prev">
         <link rel="prev" href="/tag/美术?start=20&amp;type=T">
         <a href="/tag/美术?start=20&amp;type=T">&lt;前页</a>
         </span>
         <a href="/tag/美术?start=0&amp;type=T">1</a>
         */
        if(books!=null&&books.size()!=0){
            String apiUrl = document.select("div.paginator span.next link").attr("href");
            //书籍中的标签列表 可能没有基础的url地址 所以需要进行添加操作 baseURL
            if(!apiUrl.contains("https://book.douban.com")){
                apiUrl = "https://book.douban.com"+apiUrl;
                log.info(apiUrl);
                //
                /*
                try {
                    //Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                */
                crawlBook(apiUrl);
            }else{
                return ;
            }
        }
    }


    /**
     * 解析书籍信 从文档中获取书籍信息
     * @param document
     * @return
     */
    private List<Book> parseBooks(Document document) {
        //<li class="subject-item"
        Elements elements = document.getElementsByClass("subject-item");
        if(elements.size()==0){
            return null;
        }

        List<Book> books = new ArrayList<>();
        for(Element node:elements){
            //获取图片
            Elements  picNode = node.select("div.pic img");
            String imgUrl = picNode.get(0).attr("src");

            //简介信息过少情况下 爬取到该页面进行爬取书籍简介信息
            String  contentUrl = node.select("div.pic a").attr("href");
            //获取书名 info
            String bookName = node.select("div.info a[title]").text();
            //评分
            String scoreS = node.select("span.rating_nums").text();
            Double score = null;
            try {
                if(!StringUtils.isEmpty(scoreS)){
                    score = Double.parseDouble(scoreS);
                }
            }catch (Exception e){
                log.info("分数为非法字符 ｛｝ bookName: {}",scoreS,bookName);
            }
            //评论人数
            Integer evaluateNumber = getIntByRegex(node.select("span.pl").text());

            //描述
            String descption = node.select("p").text();

            //去掉空格
            String data = node.select("div.pub").text().replaceAll(" ","");

            //从左到右 一次是 作者，译者， 出版社 出版日期，价格
            String[] array = data.split("/");

            String author = null;
            String translator = null;
            String publicHouse = null;
            String publicDate = null;
            Double price = null;
            if(array.length==3){
                //没有publicHouse 或者没有出版时间
                author = array[0];
                //如何判断是否是出版社，还是出版时间 如果全为数字则为
                publicDate = array[1];
                //使用正则匹配数字
                price = getNumberByRegex(array[2]);
            }

            if(array.length==4){
                //没有译者信息（国产作品） // 作者，译者， 出版社 出版日期，价格
                author = array[0];
                publicHouse =array[1];
                publicDate = array[2];
                //使用正则匹配数字
                price = getNumberByRegex(array[3]);
            }
            if(array.length==5) {
                author = array[0];
                translator =array[1];
                publicHouse =array[2];
                publicDate = array[3];
                price = getNumberByRegex(array[4]);
            }
            //包含6个数组的 可能包含两个作者 且使用了/分隔
            if(array.length==6) {
                author = array[0]+"、"+array[1];
                translator =array[2];
                publicHouse =array[3];
                publicDate = array[4];
                price = getNumberByRegex(array[5]);
            }
            if(array.length > 6){
                author = Arrays.asList(array).toString();
            }
            Book book = new Book();
            book.setName(bookName);
            book.setAuthor(author);
            book.setTranslator(translator);
            book.setPublisHouse(publicHouse);
            book.setPublicationDate(publicDate);
            book.setEvaluateNumber(evaluateNumber);
            book.setPrice(price);
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

    /**
     * 获取字符中的浮点数
     * @param input 目标字符串
     * @return 浮点数信息
     */
    private Double getNumberByRegex(String input) {
        Double price = null;
        //使用正则匹配数字

        String regex = "(\\d+)(.?)(\\d*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(input);
        if(m.find()){
            //log.info("find {}", m.group(0));
            price = Double.parseDouble(m.group(0));
        }else{
            log.info("no find");
        }
        return price;
    }

    /**
     * 获取字符中的浮点数
     * @param input 目标字符串
     * @return 浮点数信息
     */
    private Integer getIntByRegex(String input) {
        Integer number = null;
        //使用正则匹配数字
        String regex = "(\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(input);
        if(m.find()){
            //log.info("find {}", m.group(0));
            number = Integer.parseInt(m.group(0));
        }else{
            log.info("no find");
        }
        return number;
    }
}
