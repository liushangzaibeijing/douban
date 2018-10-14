package com.xiu.crawling.douban.core;

import com.xiu.crawling.douban.bean.Book;
import com.xiu.crawling.douban.bean.BookExample;
import com.xiu.crawling.douban.bean.UrlInfo;
import com.xiu.crawling.douban.bean.UrlInfoExample;
import com.xiu.crawling.douban.mapper.BookMapper;
import com.xiu.crawling.douban.utils.HttpUtil;
import com.xiu.crawling.douban.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
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
     * 持久化的mapper对象
     */
    private BookMapper bookMapper;


    public BookThreadTask(String tagName, String url, BookMapper bookMapper) {
        this.tagName = tagName;
        this.url = url;
        this.bookMapper = bookMapper;
    }

    /**
     * 支持首页列表 以及第几页数据的爬取操作
     */
    @Override
    public void run() {
        //开始爬取以及持久化
        crawlBook(this.url);
        ///爬取结束后将该url的状态置为已经爬取过 模糊


    }

    private  void crawlBook(String url){
        String result = HttpUtil.doGet(this.url);
        log.info(result);
        Document document = Jsoup.parseBodyFragment(result);
        //解析书籍信息
        List<Book> books = parseBooks(document);

        //循环插入
        /*
        for(Book book:books){
            //先查询是否重复，重复不进行插入操作
            BookExample example = new BookExample();
            example.createCriteria().andNameEqualTo("");
            List<Book> bookList = bookMapper.selectByExample(example);
            if(books==null || books.size()==0){
                bookMapper.insertSelective(book);
            }
        }
        */
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
                //继续爬取
                crawlBook(apiUrl);
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

        List<Book> books = new ArrayList<>();
        for(Element node:elements){
            //获取图片
            Elements  picNode = node.select("div.pic img");
            String imgUrl = picNode.get(0).attr("src");

            //简介信息过少情况下 爬取到该页面进行爬取书籍简介信息
            String  contentUrl = node.select("div.pic a").attr("href");
            //获取书名
            String bookName = node.select("div.info.a").text();
            //评分
            Double score = Double.parseDouble(node.select("span.rating_nums").text());
            //评论人数
            String numStr = node.select("span.pl").text().replace("人评价","")
                    .replace("(","")
                    .replace(")","");
            Integer evaluateNumber = Integer.parseInt(numStr);

            //描述
            String descption = node.select("p").text();

            //去掉空格
            String data = node.select("div.pub").text().replaceAll(" ","");

            //从左到右 一次是 作者，译者， 出版社 出版日期，价格
            String[] array = data.split("/");

            String author = null;
            String translator = null;
            String publicHouse = null;
            Date publicDate = null;
            Double price = null;
            if(array.length==4){
                //没有译者信息（国产作品）
                author = array[0];
                publicHouse =array[1];
                publicDate = DateUtils.parseDate(array[2]);
                //使用正则匹配数字
                price = getNumberByRegex(array[3]);
            }
            if(array.length==5) {
                author = array[0];
                translator =array[1];
                publicHouse =array[2];
                publicDate = DateUtils.parseDate(array[3]);
                price = getNumberByRegex(array[4]);
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

            log.info("书籍信息：{}", JsonUtil.obj2str(book));

            books.add(book);
        }
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
            log.info("find {}", m.group(0));
            price = Double.parseDouble(m.group(0));
        }else{
            log.info("no find");
        }
        return price;
    }
}
