package com.xiu.crawling.douban.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiu.crawling.douban.bean.Article;
import com.xiu.crawling.douban.bean.ArticleExample;
import com.xiu.crawling.douban.common.Constant;
import com.xiu.crawling.douban.core.service.CsdnService;
import com.xiu.crawling.douban.mapper.ArticleMapper;
import com.xiu.crawling.douban.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * author  Administrator
 * date   2018/10/18
 */
@Slf4j
@Service
public class CsdnServiceImpl implements CsdnService {


    @Resource
    private ArticleMapper articleMapper;

    public static Map<String,String> headerMap = new HashMap<>();
    public static List<String> commentList = new ArrayList<>();

    public static Random random = new Random();
    static{
        try {

            Properties properties = new Properties();
            ClassPathResource classPathResource = new ClassPathResource("csdn/submitHeader.properties");
            InputStream in = classPathResource.getInputStream();
            properties.load(in);
            for(String key:properties.stringPropertyNames()){
                headerMap.put(key,properties.getProperty(key));
            }
//            commentList.add("刚好有这个困惑，看了你的博客很有收获，望互访");
            commentList.add("大佬的文章简洁干练，是难得的好文！^◡^,望互访");
            commentList.add("不愧是大佬，太厉害了，学习，学习！^◡^，望莅临");
            commentList.add("好文收藏，很实用^◡^,望回访");
            commentList.add("写的真全，牛^◡^");
            commentList.add("你不上热榜 谁上^◡^");
            commentList.add("大佬出品，必属精品^◡^,望莅临");
            commentList.add("赞赞赞，你真是个小天才哦^◡^,望莅临");
            commentList.add("写的好，很nice,期待大佬回访！");
            commentList.add("写得好，欢迎回访");
            commentList.add("写得很不错，支持原创！");
            commentList.add("文章写得很详细，干货巨多，支持支持，欢迎回访！！！");
            commentList.add("我反手一个赞，正手一个赞");
            commentList.add("给大佬打call！");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mockComment(String url)  {
        if(StringUtils.isEmpty(url)){
            log.info("请求url不存在");
            return;
        }
        String result = HttpUtil.doGet(url);
        if(StringUtils.isEmpty(result)){
            log.info("获取的文章列表为空获取不到内容");
            return;
        }
        Document document = Jsoup.parse(result);
        Elements shownOffsetEle = document.select("ul#feedlist_id");
        if(shownOffsetEle.size()<=0){
            return;
        }
        String shownOffset = shownOffsetEle.get(0).attr("shown-offset");
        log.info("shownOffset:{}",shownOffset);
        //请求文章列表
        String category = url.substring(url.lastIndexOf("/")+1);

        //type=more&category=java&shown_offset=1627655913086375
        Map<String,Object> paramOne = new HashMap<>();
        paramOne.put("type","new");
        paramOne.put("category",category);
        paramOne.put("shown_offset",shownOffset);

        String articlesJson = HttpUtil.doGet(Constant.articleList, null, paramOne);

        log.info("博客文章列表：{}",articlesJson);
        JSONArray articleList = JSON.parseObject(articlesJson).getJSONArray("articles");

        for(int i=0;i<articleList.size();i++){

            JSONObject article = articleList.getJSONObject(i);
            String articleUrl = article.getString("url");
            String name = article.getString("title");
            String articleId = articleUrl.substring(articleUrl.lastIndexOf("/")+1);

            //查询是否存在 存在已经评论过 不进行评论类 否则 插入
            if(hasComment(buildArticle(article))){
                log.info("博客{}已经评论过",name);
                 continue;
            }
            //入库 如果已经评论过 则跳过
            //携带cookie 进入
            Map<String,Object> param = new HashMap<>(3);
            param.put("commentId","");
            log.info("随机评论"+i);
            param.put("content",commentList.get(i%commentList.size()));
            param.put("articleId",articleId);
            String response = HttpUtil.doPost(Constant.commentSubmit, param, headerMap);
            log.info("请求结果：{}",response);
            JSONObject jsonObject = JSON.parseObject(response);
            Integer code = jsonObject.getInteger("code");
            if(code != 200){
                break;
            }
            try {
                Thread.sleep(1000*60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean hasComment(Article article) {
        ArticleExample articleExample = new ArticleExample();
        articleExample.createCriteria().andArticleIdEqualTo(article.getArticleId());
        List<Article> articles = articleMapper.selectByExample(articleExample);
        if(articles== null || articles.size()==0){
            //保存
            articleMapper.insert(article);
            return false;
        }
        return true;

    }

    private Article buildArticle(JSONObject article) {
        String articleUrl = article.getString("url");
        String name = article.getString("title");

        String articleId = articleUrl.substring(articleUrl.lastIndexOf("/")+1);
        String bloggerId = articleUrl.substring("https://blog.csdn.net/".length()+1,articleUrl.indexOf("/article"));
        Article articleEntity = new Article();
        articleEntity.setName(name);
        articleEntity.setArticleId(articleId);
        articleEntity.setHref(articleUrl);
        articleEntity.setBloggerId(bloggerId);
        articleEntity.setCreateTime(new Date());
        return articleEntity;
    }
}
