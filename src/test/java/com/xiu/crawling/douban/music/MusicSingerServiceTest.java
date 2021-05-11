package com.xiu.crawling.douban.music;

import com.xiu.crawling.douban.DoubanApplication;
import com.xiu.crawling.douban.core.service.impl.MusicSingerService;
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
public class MusicSingerServiceTest {
    /**
     * 歌手的附属信息
     */
    @Autowired
    MusicSingerService musicSingerService;

    //获取歌手信息
    @Test
    public void parseSigner(){
        musicSingerService.parseSigner();
    }
}
