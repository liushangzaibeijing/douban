package com.xiu.crawling.douban.core;

import com.xiu.crawling.douban.bean.BaiDuXueSuInfo;
import com.xiu.crawling.douban.common.Constant;
import com.xiu.crawling.douban.mapper.BaiDuXueSuInfoMapper;
import com.xiu.crawling.douban.utils.HttpUtil;
import com.xiu.crawling.douban.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.concurrent.CountDownLatch;


/**
 * @author  xieqx
 * date   2018/10/13
 * desc 爬取
 */
@Slf4j
public class BaiDuXueSuThreadTask implements  Runnable{

    private static final Integer SLEEPTIM = 3000;

    private static final int MOVIEANDTV = 3;
    private static final int CHART = 4;
    private static final int TAG = 5;
    private static final int INTERVALID = 10;

    private static final String NOT_FOUND = "404";

    /**
     * 关键字
     */
    private String keyWord;



    /**
     * 持久化的电影对象
     */
    private BaiDuXueSuInfoMapper baiDuXueSuInfoMapper;


    /**
     * 并行操作
     */
    private CountDownLatch latch;

    /**
     * 用于存储可用的代理对象
     */
    private HttpHost proxy;

    public BaiDuXueSuThreadTask(String keyWord, BaiDuXueSuInfoMapper baiDuXueSuInfoMapper) {
        this.keyWord = keyWord;
        this.baiDuXueSuInfoMapper = baiDuXueSuInfoMapper;
    }

    /**
     * 支持首页列表 以及第几页数据的爬取操作
     */
    @Override
    public void run() {
        try {
            //开始爬取以及持久化
             crawlBaiDuXueSu(this.keyWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }


    /**
     * @param keyWord 标签名
     */
    private void crawlBaiDuXueSu(String keyWord) {
         int i = 0;

        while(true){
           try {
                 keyWord  = URLEncoder.encode(keyWord, "UTF-8");
                 String url = MessageFormat.format(Constant.BAIDU_XUESU_SEARCH, keyWord, i * Constant.pageSize);
                 //关键字请求
                 String result = HttpUtil.doGet(url);
                 //响应404不进行解析
                 if(result.equals("404")){
                    continue;
                 }
                 //解析信息
                 if(!StringUtils.isEmpty(result) || !result.equals(NOT_FOUND)){
                     Document document = Jsoup.parse(result);
                     parseBaiDuXueSu(document,i);
                 }
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }

    }




    public BaiDuXueSuInfo parseBaiDuXueSu(Document document,int index) {
        BaiDuXueSuInfo baiDuXueSuInfo =  new BaiDuXueSuInfo();

        int size = (index+1)*Constant.pageSize;
        int i = (index)*Constant.pageSize+1;
        for(;i <= size;i++){
            Elements elements =  document.select("div#"+i+" a");
            if(elements.size()==0){
                continue;
            }
            //获取第一个
            Element element = elements.get(0);
            //获取a标签的text和href属性
            String detailUrl = element.attr("href");
            String title = element.text();
            //论文名字
            baiDuXueSuInfo.setTitle(title);
            //被引用量
            Element citeElement =  document.select("div#"+i+" span.sc_time + span").get(0);
            String citeInfo = citeElement.text();
            if(!StringUtils.isEmpty(citeInfo)){
                String cited = citeInfo.split(":")[1].trim();
                baiDuXueSuInfo.setCited(cited);
            }

            crawlBaiDuXueSuDetail(detailUrl,baiDuXueSuInfo);
        }

        log.info("baiDuXueSuInfo info {}", JsonUtil.obj2str(baiDuXueSuInfo));
        return  baiDuXueSuInfo;
    }

    private void crawlBaiDuXueSuDetail(String detailUrl, BaiDuXueSuInfo baiDuXueSuInfo) {
        //关键字请求
        try {
            if(!detailUrl.contains("http")){
                detailUrl = "https:"+detailUrl;
            }
            String result = HttpUtil.doGet(detailUrl);
            if(!StringUtils.isEmpty(result) || !result.equals(NOT_FOUND)){
                Document document = Jsoup.parse(result);
                parseBaiDuXueSuDetail(document,baiDuXueSuInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseBaiDuXueSuDetail(Document document, BaiDuXueSuInfo baiDuXueSuInfo) {
        //作者
        Elements authors = document.select("div.author_wr div.author_text span");
        if(authors.size()!=0){
            StringBuffer authorList = new StringBuffer();
            for(int i=0;i<authors.size();i++){
                String author = authors.get(i).select("a").text().trim();
                authorList.append(author).append(",");
            }
            baiDuXueSuInfo.setAuthor(authorList.toString());
        }

        //摘要
        Elements abstractEle = document.select("div.abstract_wr p.abstract");
        if(abstractEle.size()!=0){
            String abstractText = abstractEle.text();
            baiDuXueSuInfo.setAbstractInfo(abstractText);
        }

        //关键词
        Elements kwEles = document.select("div.abstract_wr p.kw_main_s span");
        if(kwEles.size()!=0){
            StringBuffer kwList = new StringBuffer();
            for(int i=0;i<kwEles.size();i++){
                String kw = kwEles.get(i).select("a").text();
                kwList.append(kw).append(",");
            }
            baiDuXueSuInfo.setKeyword(kwList.toString());
        }

        // 年份

        // 会议/期刊名字。


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
