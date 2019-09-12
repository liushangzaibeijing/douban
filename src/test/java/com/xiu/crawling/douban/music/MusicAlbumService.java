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
import com.xiu.crawling.douban.mapper.AlbumMapper;
import com.xiu.crawling.douban.mapper.BusSingerMapper;
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
     * 歌手图片存放根路径
     */
    @Value("${singerPicBasePath}")
    String singerPicBasePath;

    /**
     * 专辑图片存放根路径
     */
    @Value("${albumPicBasePath}")
    String albumPicBasePath;

    /**
     * 歌曲资源根路径
     */
    @Value("${songResourceBasePath}")
    String songResourceBasePath;


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
                log.info("所有的歌手信息爬取");
                return;
            }
            //遍历歌手信息
            for(Singer singer:singers){
               String singerMid = singer.getSignerMid();
               String singerAblumListUrl = ConstantMusic.getAlbumList(singerMid);

               String result = HttpUtil.doGet(singerAblumListUrl);

               List<Album> albumList = parseAlbum(result);

            }
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
            JSONObject album = albumList.getJSONObject(0);

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
            //TODO  "singers": [
            //            {
            //              "singer_id": 1190986,
            //              "singer_mid": "003DBAjk2MMfhR",
            //              "singer_name": "BLACKPINK"
            //            }
            //          ]

            String singerMids = getSingersByJson(album, "singer_mid");
            albumBean.setSignerMid(singerMids);
            Date pubTime = getDateByJson(album, "pub_time");
            albumBean.setPubTime(pubTime);
            String score = getStringByJson(album, "score");
            albumBean.setScore(score);

            String albumHtmlUrl = getAlbumHtml(albumMid);
            String htmlResult = HttpUtil.doGet(albumHtmlUrl);
            Document document = Jsoup.parseBodyFragment(htmlResult);
            //从html页面中获取其他更加详细的数据信息
            parseAlbumOther(albumBean,document);

            albums.add(albumBean);
        }

        return albums;
    }

    private void parseAlbumOther(Album albumBean, Document document) {
        //TODO 图片
        //<li class="subject-item"
        Elements  picNode  = document.select("div.main.mod_data img#albumImg");

        String imgUrl = picNode.get(0).attr("src");
        //下载并存放

        imgUrl = "https"+imgUrl;
        String albumDir = generatorSaveDir(albumPicBasePath,albumBean.getSignerMid());
        String albumPicUrl = HttpUtil.doDown(imgUrl,null,albumDir,albumBean.getAlbumMid());

        albumBean.setAlbumPic(albumPicUrl);
        //TODO desc 描述


    }

    private String generatorSaveDir(String albumPicBasePath, String signerMid) {
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
        Date date = DateUtils.parseDate(value);
        return date;

    }

    public String getStringByJson(JSONObject jsonObject,String key){
        String value = jsonObject.getString(key);
        if(StringUtils.isEmpty(value)){
            return null;
        }
        return value;

    }


}
