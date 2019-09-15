package com.xiu.crawling.douban.music;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.github.pagehelper.PageHelper;
import com.xiu.crawling.douban.DoubanApplication;
import com.xiu.crawling.douban.bean.*;
import com.xiu.crawling.douban.bean.dto.SongInfoResult;
import com.xiu.crawling.douban.bean.dto.Songlist;
import com.xiu.crawling.douban.common.ConstantMusic;
import com.xiu.crawling.douban.core.service.ProxyService;
import com.xiu.crawling.douban.enums.MusicTypeEnum;
import com.xiu.crawling.douban.mapper.AlbumMapper;
import com.xiu.crawling.douban.mapper.BusSingerMapper;
import com.xiu.crawling.douban.mapper.CurrPageMapper;
import com.xiu.crawling.douban.mapper.SingerMapper;
import com.xiu.crawling.douban.utils.HttpUtil;
import com.xiu.crawling.douban.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.utils.DateUtils;
import org.assertj.core.util.DateUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.xiu.crawling.douban.common.Constant.pageSize;
import static com.xiu.crawling.douban.common.ConstantMusic.getAlbumHtml;

/**
 * 歌曲专辑信息爬取
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DoubanApplication.class)
@WebAppConfiguration
public class MusicAlbumService {
    /**
     * 歌手信息
     */
    @Autowired
    AlbumMapper albumMapper;


    /**
     * 歌手信息 业务
     */
    @Autowired
    BusSingerMapper busSingerMapper;

    /**
     * 歌手信息 业务
     */
    @Autowired
    SingerMapper singerMapper;

    /**
     * 专辑图片存放根路径
     */
    @Value("${albumPicBasePath}")
    String albumPicBasePath;


    @Autowired
    CurrPageMapper currPageMapper;

    //获取专辑信息
    @Test
    public void parseAlbumList(){
        //getAlbumList
        Integer total = busSingerMapper.selectCountSinger();

        Integer pageCount = total%ConstantMusic.songpageSize==0
                ?total/ConstantMusic.songpageSize:total/ConstantMusic.songpageSize+1;

        for(int singerIndex =0;singerIndex<pageCount;singerIndex++){
            PageHelper.startPage(singerIndex,ConstantMusic.songpageSize);
            //分页获取歌手
            SingerExample singerExample = new SingerExample();
            List<Singer> singers = singerMapper.selectByExample(singerExample);

            if(singers==null && singers.size()==0){
                log.info("所有的歌手信息爬取完成");
                return;
            }
            //遍历歌手信息
            for(Singer singer:singers){
                log.info("歌手 {} 的专辑信息爬取开始",singer.getFullName());
               String singerMid = singer.getSignerMid();

               Integer totalPage = getAlbumToTalPage(singerMid);

               for(int albumIndex = 0; albumIndex<totalPage;albumIndex++){
                   try{
                       String singerAblumListUrl = ConstantMusic.getAlbumList(singerMid,albumIndex);
                       String result = HttpUtil.doGet(singerAblumListUrl);
                       List<Album> albumList = parseAlbum(result);
                       log.info("下载的专辑信息:{}",JsonUtil.obj2str(albumList));
                       insertAlbumBatch(albumList);
                   }catch (Exception ex){
                       Integer id = singer.getId();
                       updateCurrPageInfo( id, MusicTypeEnum.ABLUM.getCode(),
                               singer.getFullName()+"_"+ex.getMessage());

                   }


                }
                log.info("歌手 {} 的专辑信息爬取完成",singer.getFullName());
                crawAblumOver(singer.getId());
            }
        }


    }

    /**
     * 标记该歌手对应的专辑信息已经爬取完毕
     * @param singerId
     */
    private void crawAblumOver(Integer singerId) {
        //更新歌手信息
        Singer singerUpdate = new Singer();
        singerUpdate.setId(singerId);
        singerUpdate.setIsOver(1);
        singerMapper.updateByPrimaryKey(singerUpdate);
    }

    private Integer getAlbumToTalPage(String singerMid) {
        String singerAblumListUrl = ConstantMusic.getAlbumList(singerMid,1);

        String result = HttpUtil.doGet(singerAblumListUrl);
        JSONObject albumListStr = JSONObject.parseObject(result);
        //获取专辑列表信息
        Integer total = albumListStr.getJSONObject("singerAlbum").getJSONObject("data").getInteger("total");
        return  total%ConstantMusic.albumSize==0
                ?total/ConstantMusic.albumSize:total/ConstantMusic.albumSize+1;
    }

    /**
     * 批量插入专辑信息
     * @param albumList 专辑信息集合对象
     */
    private void insertAlbumBatch(List<Album> albumList) {
        for (Album album : albumList) {
            albumMapper.insert(album);
        }
    }

    /**
     * 从json中获取专辑信息
     * @param result
     * @return
     */
    private List<Album> parseAlbum(String result) {
        List<Album> albums = new ArrayList<>();
        JSONObject albumListStr = JSONObject.parseObject(result);
        //获取专辑列表信息
        JSONArray albumList = albumListStr.getJSONObject("singerAlbum").getJSONObject("data").getJSONArray("list");
        //获取专辑列表
        for(int i=0;i<albumList.size();i++){
            Album albumBean = new Album();
            JSONObject album = albumList.getJSONObject(i);

            Integer albumid = album.getInteger("albumid");
            albumBean.setAlbumId(albumid);
            String albumMid = getStringByJson(album,"album_mid");
            albumBean.setAlbumMid(albumMid);
            String albumName =  getStringByJson(album,"album_name");
            albumBean.setAlbumName(albumName);
            String albumtype =  getStringByJson(album,"albumtype");
            albumBean.setAlbumType(albumtype);
            String companyName = getStringByJson( album.getJSONObject("company"),"company_name");
            albumBean.setCompanyName(companyName);
            String lan = getStringByJson(album, "lan");
            albumBean.setLan(lan);
            String singerMids = getSingersByJson(album, "singers");
            albumBean.setSignerMid(singerMids);
            Date pubTime = getDateByJson(album, "pub_time");
            albumBean.setPubTime(pubTime);
            String score = getStringByJson(album, "score");
            albumBean.setScore(score);

            String albumHtmlUrl = getAlbumHtml(albumMid);

            String htmlResult = HttpUtil.doGet(albumHtmlUrl);
//            log.info("htmlResult:{}",htmlResult);
            Document document = Jsoup.parseBodyFragment(htmlResult);
            //从html页面中获取其他更加详细的数据信息
            parseAlbumOther(albumBean,document);
            albums.add(albumBean);
        }
        return albums;
    }

    /**
     * 从对应专辑的html 获取图片和desc信息
     * @param albumBean
     * @param document
     */
    private void parseAlbumOther(Album albumBean, Document document) {
        //TODO 图片
        //<li class="subject-item"
        Elements  picNode  = document.select("img#albumImg");

        String imgUrl = picNode.get(0).attr("src");
        //下载并存放

        imgUrl = "http:"+imgUrl;

        log.info("专辑图片：{}",imgUrl);
        String albumDir = generatorSaveDir(albumPicBasePath,albumBean.getSignerMid());
        String albumPicUrl = HttpUtil.doDown(imgUrl,null,albumDir,albumBean.getAlbumMid()+".jpg");
         //album_desc
        albumBean.setAlbumPic(albumPicUrl);
        //TODO desc 描述
        Elements  descNode  = document.select("div#album_desc .about__cont p");
        String desc = descNode.text();
        albumBean.setDescption(desc);

    }

    private String generatorSaveDir(String albumPicBasePath, String signerMid) {
        signerMid = signerMid.replaceAll(",","_");
      return albumPicBasePath+"/"+signerMid+"/";
    }

    private String getSingersByJson(JSONObject album, String key) {
        StringBuffer stringBuffer = new StringBuffer();
        JSONArray singers =  album.getJSONArray(key);
        for(int i=0;i<singers.size();i++){
            JSONObject singer =  singers.getJSONObject(i);
            String singer_mid = getStringByJson(singer, "singer_mid");
            if(singer_mid!=null){
                stringBuffer.append(singer_mid).append(",");
            }
        }
        String singerMids = stringBuffer.substring(0, stringBuffer.length() - 1);
        return singerMids;
    }

    private Date getDateByJson(JSONObject album, String key) {
        String value = album.getString(key);
        if(StringUtils.isEmpty(value)){
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public String getStringByJson(JSONObject jsonObject,String key){
        String value = jsonObject.getString(key);
        if(StringUtils.isEmpty(value)){
            return null;
        }
        return value;

    }


    /**
     * 保存中断导致的信息
     * @param currentPage
     * @param type
     * @param errorMsg
     */
    private void updateCurrPageInfo(Integer currentPage, Integer type, String errorMsg) {
        CurrPage currPage = new CurrPage();
        currPage.setCurrPage(currentPage);
        log.info("爬取专辑信息出现的问题");
        currPage.setType(type);
        errorMsg = errorMsg.length()>500?errorMsg.substring(0,500):errorMsg;
        currPage.setMessage(errorMsg);
        currPageMapper.insert(currPage);
    }

}
