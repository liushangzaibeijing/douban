package com.xiu.crawling.douban.music;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiu.crawling.douban.DoubanApplication;
import com.xiu.crawling.douban.core.service.impl.MusicSongService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DoubanApplication.class)
@WebAppConfiguration
public class MusicSongServiceTest {
    /**
     * 歌手信息
     */
    @Autowired
    MusicSongService musicSongService;

    //获取歌手信息
    @Test
    public void parseSongList(){
        musicSongService.parseSongList();
    }
    @Test
    public void parseSong(){
       String singerName = "周杰伦";
       musicSongService.parseSong(singerName);
    }
    @Test
    public void resetSong(){
        musicSongService.resetSong();
    }

    @Test
    public void queryMusicResource(){
        JSONArray singers = new JSONArray();
        JSONObject singer = new JSONObject();
        singer.put("id","4558");
        singers.add(0,singer);
        musicSongService.getMusicResocure("002b3VP635HgLn","告白气球",singers);
    }

}
