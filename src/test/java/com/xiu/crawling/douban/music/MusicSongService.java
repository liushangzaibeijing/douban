package com.xiu.crawling.douban.music;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.xiu.crawling.douban.DoubanApplication;
import com.xiu.crawling.douban.bean.Singer;
import com.xiu.crawling.douban.bean.SingerExample;
import com.xiu.crawling.douban.bean.Song;
import com.xiu.crawling.douban.bean.dto.SongInfoResult;
import com.xiu.crawling.douban.bean.dto.Songlist;
import com.xiu.crawling.douban.common.ConstantMusic;
import com.xiu.crawling.douban.core.service.ProxyService;
import com.xiu.crawling.douban.mapper.BusSingerMapper;
import com.xiu.crawling.douban.mapper.SingerMapper;
import com.xiu.crawling.douban.utils.HttpUtil;
import com.xiu.crawling.douban.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xiu.crawling.douban.common.Constant.pageSize;

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
     * 歌手信息 业务
     */
    @Autowired
    BusSingerMapper busSingerMapper;

    /**
     * 代理服务
     */
    @Autowired
    ProxyService proxyService;

    /**
     * 歌曲资源根路径
     */
    @Value("${songResourceBasePath}")
    String songResourceBasePath;

    //获取歌手信息
    @Test
    public void parseSongList(){
        Integer total = busSingerMapper.selectCountSinger();

        Integer pageCount = total%ConstantMusic.songpageSize==0
                ?total/ConstantMusic.songpageSize:total/ConstantMusic.songpageSize+1;

        for(int singerIndex=0;singerIndex<pageCount;singerIndex++){
            PageHelper.startPage(singerIndex,pageSize);
            //分页获取歌手
            SingerExample singerExample = new SingerExample();
            singerExample.createCriteria().andIsoverEqualTo(0);
            List<Singer> singers = singerMapper.selectByExample(singerExample);

            if(singers==null && singers.size()==0){
                log.info("所有的歌手信息爬取");
                return;
            }
            for(Singer singer:singers){
                Integer pageNum = getSongListPage(singer.getSignerMid());

                for(int songIndex = 0;songIndex<pageNum;songIndex++){
                    SongInfoResult songInfoResult = getSongInfoResult(singer.getSignerMid(),songIndex+1);
                    List<Songlist> songlist = songInfoResult.getSonglist();
                    HttpHost proxy = null;
                    for (Songlist songvo : songlist){
                        //TODO 获取歌曲其他信息存储
                        String songMid = songvo.getMid();
                        String vKeyUrl = ConstantMusic.getMusicSongVKey(songMid);
                        Map<String, String> headers = generatorHeader();
                        String vkeyInfo = HttpUtil.doGetByHeader(vKeyUrl,headers);
                        log.info("获取的key的信息：{}",vkeyInfo);
                        JSONObject vkeyInfoJson = JSONObject.parseObject(vkeyInfo);

                        String userIp = vkeyInfoJson.getJSONObject("req").getJSONObject("data").getString("userip");

                        log.info("用户登录的ip地址：{}  爬取的歌曲名称为：{}",userIp,songvo.getName());
                        //获取到vkey
                        JSONArray midurlinfos = vkeyInfoJson.getJSONObject("req_0").getJSONObject("data").getJSONArray("midurlinfo");

                        if(midurlinfos!=null&&midurlinfos.size()!=0){
                            JSONObject  midurlinfo = midurlinfos.getJSONObject(0);
                            String purl = midurlinfo.getString("purl");
                            //下载音乐
                            String songDownUrl = ConstantMusic.getSongDownUrl(purl);

                            log.info("下载的url:{}",songDownUrl);
                            //HttpHost canUseProxy = proxyService.findCanUseProxy();
                            //下载操作
                            HttpUtil.doDown(songDownUrl,null,"/home/nas/music/",songMid+".m4a");

                        }
                        Song song = new Song();

                        song.setSongId(songvo.getId());
                        song.setSongMid(songvo.getMid());
                        song.setSongName(songvo.getName());
                        song.setSongType(String.valueOf(songvo.getType()));
                        song.setAlbumId(songvo.getAlbum().getMid());
                        StringBuilder singerMids = new StringBuilder();
                        for(com.xiu.crawling.douban.bean.dto.Singer singerDto:songvo.getSinger()){
                            singerMids.append(singerDto.getId()+",");
                        }
                        String  singerMid = singerMids.substring(0,singerMids.length()-1);
                        song.setSongMid(singerMid);
                        song.setTimePublic(song.getTimePublic());
                        //该字段目前可以不需要song.setSongAttr();


                    }
                }
            }
        }
    }

    /**
     * 获取歌手对应歌曲的总页数 按照每页10
     * @param signerMid 歌曲在qq音乐的唯一标识
     * @return
     */
    private Integer getSongListPage(String  signerMid) {
        SongInfoResult songInfoResult = getSongInfoResult(signerMid, 1);
        //总歌曲数目
        int total_song = songInfoResult.getTotal_song();
        int pageNume = total_song/ConstantMusic.songpageSize;
        return total_song%ConstantMusic.songpageSize==0?pageNume:pageNume+1;
    }

    /**
     * 获取歌手信息
     * @param signerMid 歌曲在qq音乐的唯一标识
     * @param currentPage 当前页码
     * @return
     */
    private SongInfoResult getSongInfoResult(String  signerMid, Integer currentPage) {
        //根据歌手 mid 获取歌曲信息 注意分页的情况
        String songListUrl = ConstantMusic.getMusicSongListDetailUrl(signerMid,currentPage);
        log.info("歌手对应的歌曲信息：{}",songListUrl);
        String result = HttpUtil.doGet(songListUrl);

        //转换为jsonObje 获取其中的key
        JSONObject songInfoJsonObj = JSONObject.parseObject(result);
        String songResult = songInfoJsonObj.getJSONObject("singer").getJSONObject("data").toString();
        log.info("查询的歌曲信息为：{}",songResult);
        SongInfoResult  songInfoResult = JsonUtil.readValue(songResult,SongInfoResult.class);
        return songInfoResult;
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
