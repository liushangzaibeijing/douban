package com.xiu.crawling.douban.music;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiu.crawling.douban.DoubanApplication;
import com.xiu.crawling.douban.bean.Singer;
import com.xiu.crawling.douban.bean.SingerExample;
import com.xiu.crawling.douban.bean.dto.SongInfoResult;
import com.xiu.crawling.douban.bean.dto.Songlist;
import com.xiu.crawling.douban.common.ConstantMusic;
import com.xiu.crawling.douban.core.service.ProxyService;
import com.xiu.crawling.douban.mapper.SingerMapper;
import com.xiu.crawling.douban.utils.HttpUtil;
import com.xiu.crawling.douban.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DoubanApplication.class)
@WebAppConfiguration
public class MusicSongService {
    /**
     * 歌手信息
     */
    @Autowired
    SingerMapper singerMapper;

    /**
     * 代理服务
     */
    @Autowired
    ProxyService proxyService;
    //获取歌手信息
    @Test
    public void parseSongList(){
        //获取一个歌手的mid
        SingerExample singerExample = new SingerExample();
        SingerExample.Criteria criteria = singerExample.createCriteria();
        criteria.andFullNameEqualTo("周杰伦");
        List<Singer> singers = singerMapper.selectByExample(singerExample);

        if(singers==null && singers.size()==0){
            return;
        }

        Singer singer = singers.get(0);

        String signerMid = singer.getSignerMid();

        //根据歌手 mid 获取歌曲信息 注意分页的情况
        String songListUrl = ConstantMusic.getMusicSongListDetailUrl(signerMid,1);

        String result = HttpUtil.doGet(songListUrl);


        //转换为jsonObje 获取其中的key

        JSONObject songInfoJsonObj = JSONObject.parseObject(result);
        String songResult = songInfoJsonObj.getJSONObject("singer").getJSONObject("data").toString();
        log.info("查询的歌曲信息为：{}",songResult);
        SongInfoResult  songInfoResult = JsonUtil.readValue(songResult,SongInfoResult.class);

        List<Songlist> songlist = songInfoResult.getSonglist();
        HttpHost proxy = null;
        for (Songlist song : songlist){

            //TODO 获取歌曲其他信息存储

            String songMid = song.getMid();

            String vKeyUrl = ConstantMusic.getMusicSongVKey(songMid);

            //Referer: https://y.qq.com/portal/player.html
            Map<String, String> headers = generatorHeader();
            String vkeyInfo = HttpUtil.doGetByHeader(vKeyUrl,headers);
            log.info("获取的key的信息：{}",vkeyInfo);
            JSONObject vkeyInfoJson = JSONObject.parseObject(vkeyInfo);

            String userIp = vkeyInfoJson.getJSONObject("req").getJSONObject("data").getString("userip");

            log.info("用户登录的ip地址：{}  爬取的歌曲名称为：{}",userIp,song.getName());
            //获取到vkey
            JSONArray midurlinfos = vkeyInfoJson.getJSONObject("req_0").getJSONObject("data").getJSONArray("midurlinfo");

            for(int i=0;i<midurlinfos.size();i++){
                //获取purl
                JSONObject  midurlinfo = midurlinfos.getJSONObject(i);
                String purl = midurlinfo.getString("purl");
                //下载音乐
                String songDownUrl = ConstantMusic.getSongDownUrl(purl);


                //下载操作
                HttpUtil.doDown(songDownUrl,proxy,"/home/nas/music/",songMid);
            }



        }

//
//
//        //根据歌曲的mid 获取key 根据key 下载音乐

    }

    private Map<String,String> generatorHeader(){
        Map<String,String> headers = new HashMap<>();
        headers.put("Host","u.y.qq.com");
        headers.put("Connection","keep-alive");
        headers.put("Accept","application/json, text/javascript, */*; q=0.01");

        headers.put("Origin","https://y.qq.com");
        headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36");
        headers.put("Sec-Fetch-Mode","cors");
        headers.put("Sec-Fetch-Site","same-site");
        headers.put("Referer","https://y.qq.com/portal/player.html");
        headers.put("Accept-Encoding","gzip, deflate, br");
        headers.put("Accept-Language","zh-CN,zh;q=0.9");
        //Cookie: pgv_pvid=7254122593; ts_uid=9331586008; pgv_pvi=456002560; userAction=1; pgv_info=ssid=s9413784599; pgv_si=s8151438336; _qpsvr_localtk=0.8080819634502663; RK=p3y0AkfoOc; ptcz=3fb8aa0d886440ee67c42caa783c4cadb4aa436d23f9d48938662ddb2e1353b9; player_exist=1; qqmusic_fromtag=66; psrf_musickey_createtime=1567844478; psrf_qqopenid=A3AE48579EAF7EC52D358D530F7675B7; psrf_qqaccess_token=FCB5930F18F1624E052ABBD48AF0ABF2; psrf_access_token_expiresAt=1575620478; psrf_qqrefresh_token=075A5E5517FA4733C135F09F94D56E2A; psrf_qqunionid=75F11F8D868EEC6DA64B1A2EE28C125A; uin=1374523006; qm_keyst=Q_H_L_2u7Qpr50e4zispB5i44NYaaT64vNCpyZHjeWZDpqva04J-DYqbgRPphNwNgPkY3; yqq_stat=0; ts_last=y.qq.com/portal/player.html; yplayer_open=1; yq_index=0
        return headers;
    }

}
