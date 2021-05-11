package com.xiu.crawling.douban.music;

import com.xiu.crawling.douban.DoubanApplication;
import com.xiu.crawling.douban.core.service.impl.MusicAlbumService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * 歌曲专辑信息爬取
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DoubanApplication.class)
@WebAppConfiguration
public class MusicAlbumServiceTest {
    /**
     * 歌手信息
     */
    @Autowired
    MusicAlbumService musicAlbumService;


    //获取专辑信息
    @Test
    public void parseAlbumList(){
        musicAlbumService.parseAlbumList();
    }
}
