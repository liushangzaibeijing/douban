package com.xiu.crawling.douban.core;

import com.xiu.crawling.douban.bean.Book;
import com.xiu.crawling.douban.bean.BookExample;
import com.xiu.crawling.douban.bean.UrlInfo;
import com.xiu.crawling.douban.bean.UrlInfoExample;
import com.xiu.crawling.douban.mapper.BookMapper;
import com.xiu.crawling.douban.utils.HttpUtil;
import com.xiu.crawling.douban.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String result = HttpUtil.doGet(this.url);
        log.info(result);

        Document document = Jsoup.parseBodyFragment(result);

        String regex = "a[href~=/tag/\\S*]";
        Elements elements = document.select(regex);

        HashMap<String,UrlInfo> data = new HashMap<>();

        for(Element node:elements){

        }

        log.info("需要爬取得数据信息 {}", JsonUtil.obj2str(data));

        //遍历map
        for(Map.Entry entry:data.entrySet()){
            Book book = (Book) entry.getValue();
            //先查询是否重复，重复不进行插入操作
            BookExample example = new BookExample();
            example.createCriteria().andNameEqualTo("");
            List<Book> books = bookMapper.selectByExample(example);
            if(books==null || books.size()==0){
                bookMapper.insertSelective(book);
            }
        }

    }
}
