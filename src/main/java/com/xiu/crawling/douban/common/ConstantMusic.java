package com.xiu.crawling.douban.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * author   xieqx
 * createTime  2018/11/10
 * desc qq音乐相关常量
 */
public class ConstantMusic {

    private static Integer signerSize = 80;

    /**
     * 歌手列表标签
     */
    public final static String MUSIC_SIGNER_LIST ="https://u.y.qq.com/cgi-bin/musicu.fcg?-=getUCGI8991517745080486&g_tk=1370299737&loginUin=1169704579&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0&";

    /**
     * 歌手详细信息
     */
    public final static String MUSIC_SIGNER_DETAIL = "https://c.y.qq.com/splcloud/fcgi-bin/fcg_get_singer_desc.fcg";

    /**
     * 歌手对应的歌曲列表信息
     */
    public final static String MUSIC_SONG_LIST = "https://u.y.qq.com/cgi-bin/musicu.fcg?-=getUCGI048295277528706215&g_tk=1370299737&loginUin=1169704579&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0";


    /**
     * 获取歌手的vkey
     */
    public final static String MUSIC_SONG_VKEY = "https://u.y.qq.com/cgi-bin/musicu.fcg?-=getplaysongvkey509352456976849&g_tk=5381&loginUin=1374523006&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0";


    /**
     * 获取歌手列表url
     * @return
     */
    public static String getMusicSignerListUrl(Integer currentPage ){
      String data = "data=%7B%22comm%22%3A%7B%22ct%22%3A24%2C%22cv%22%3A0%7D%2C%22singerList%22%3A%7B%22module%22%3A%22Music.SingerListServer%22%2C%22method%22%3A%22get_singer_list%22%2C%22param%22%3A%7B%22area%22%3A-100" +
              "%2C%22sex%22%3A-100%2C%22genre%22%3A-100%2C%22index%22%3A-100%2C%22sin%22%3A"+((currentPage-1)*signerSize)+"%2C%22cur_page%22%3A"+currentPage+"%7D%7D%7D";
       return MUSIC_SIGNER_LIST+data;
    }



    /**
     * 获取单个歌手详细信息url
     * @return
     */
    public static String getMusicSignerDetailUrl(String signerId){
        Date time = new Date();
        String data = "?singermid="+signerId+"&utf8=1&outCharset=utf-8&format=xml&r="+time.getTime();
        return MUSIC_SIGNER_DETAIL+data;
    }


    /**
     * 获取歌手的所有歌曲的url
     * @return
     */
    public static String getMusicSongListDetailUrl(String singerMid,Integer currentPage){
        Integer pageSize = 10;
        String data = "&data=%7B%22comm%22%3A%7B%22ct%22%3A24%2C%22cv%22%3A0%7D%2C%22singer%22%3A%7B%22method%22%3A%22get_singer_detail_info%22%2C%22param%22%3A%7B%22sort%22%3A5%2C%22singermid%22%3A%22"+singerMid+"%22%2C%22sin%22%3A"+(currentPage-1)+"%2C%22num%22%3A"+pageSize+"%7D%2C%22module%22%3A%22music.web_singer_info_svr%22%7D%7D";
        return MUSIC_SONG_LIST+data;
    }


    /**
     * 获取歌手的所有歌曲的url
     * @return
     */
    public static String getMusicSongVKey(String singerMid){
        String data = "&data=%7B%22req%22%3A%7B%22module%22%3A%22CDN.SrfCdnDispatchServer%22%2C%22method%22%3A%22GetCdnDispatch%22%2C%22param%22%3A%7B%22guid%22%3A%227254122593%22%2C%22calltype%22%3A0%2C%22userip%22%3A%22%22%7D%7D%2C%22req_0%22%3A%7B%22module%22%3A%22vkey.GetVkeyServer%22%2C%22method%22%3A%22CgiGetVkey%22%2C%22param%22%3A%7B%22guid%22%3A%227254122593%22%2C%22songmid%22%3A%5B%22"+singerMid+"%22%5D%2C%22songtype%22%3A%5B0%5D%2C%22uin%22%3A%221374523006%22%2C%22loginflag%22%3A1%2C%22platform%22%3A%2220%22%7D%7D%2C%22comm%22%3A%7B%22uin%22%3A%221374523006%22%2C%22format%22%3A%22json%22%2C%22ct%22%3A24%2C%22cv%22%3A0%7D%7D";
        return MUSIC_SONG_VKEY+data;
    }


    /**
     * 获取歌曲的下载
     * @return
     */
    public static String getSongDownUrl(String purl){
        Date  time = new Date();
        String url = "http://isure.stream.qqmusic.qq.com/"+purl;
        //String url = "http://ws.stream.qqmusic.qq.com/"+songMid+".m4a?guid=7254122593&vkey="+vkey+"&uin=3710&fromtag=3&r="+time.getTime();
        //String url = "http://dl.stream.qqmusic.qq.com/"+songMid+".m4a?guid=9545253443&vkey="+vkey+"&uin=0&fromtag=66";
        return url;
    }



}
