package com.xiu.crawling.douban;

import com.xiu.crawling.douban.bean.MusicTag;
import com.xiu.crawling.douban.bean.dto.Tag;
import com.xiu.crawling.douban.bean.dto.Tags;
import com.xiu.crawling.douban.mapper.MusicTagMapper;
import com.xiu.crawling.douban.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * author  Administrator
 * date   2018/10/12
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DoubanApplication.class)
@WebAppConfiguration
public class CrawlingMusicTest {

    @Autowired
    MusicTagMapper musicTagMapper;

    @Test
    public void parseTags(){
        String json = "{\"area\":[{\"id\":-100,\"name\":\"全部\"},{\"id\":200,\"name\":\"内地\"},{\"id\":2,\"name\":\"港台\"},{\"id\":5,\"name\":\"欧美\"},{\"id\":4,\"name\":\"日本\"},{\"id\":3,\"name\":\"韩国\"},{\"id\":6,\"name\":\"其他\"}],\"genre\":[{\"id\":-100,\"name\":\"全部\"},{\"id\":1,\"name\":\"流行\"},{\"id\":6,\"name\":\"嘻哈\"},{\"id\":2,\"name\":\"摇滚\"},{\"id\":4,\"name\":\"电子\"},{\"id\":3,\"name\":\"民谣\"},{\"id\":8,\"name\":\"R&B\"},{\"id\":10,\"name\":\"民歌\"},{\"id\":9,\"name\":\"轻音乐\"},{\"id\":5,\"name\":\"爵士\"},{\"id\":14,\"name\":\"古典\"},{\"id\":25,\"name\":\"乡村\"},{\"id\":20,\"name\":\"蓝调\"}],\"index\":[{\"id\":-100,\"name\":\"热门\"},{\"id\":1,\"name\":\"A\"},{\"id\":2,\"name\":\"B\"},{\"id\":3,\"name\":\"C\"},{\"id\":4,\"name\":\"D\"},{\"id\":5,\"name\":\"E\"},{\"id\":6,\"name\":\"F\"},{\"id\":7,\"name\":\"G\"},{\"id\":8,\"name\":\"H\"},{\"id\":9,\"name\":\"I\"},{\"id\":10,\"name\":\"J\"},{\"id\":11,\"name\":\"K\"},{\"id\":12,\"name\":\"L\"},{\"id\":13,\"name\":\"M\"},{\"id\":14,\"name\":\"N\"},{\"id\":15,\"name\":\"O\"},{\"id\":16,\"name\":\"P\"},{\"id\":17,\"name\":\"Q\"},{\"id\":18,\"name\":\"R\"},{\"id\":19,\"name\":\"S\"},{\"id\":20,\"name\":\"T\"},{\"id\":21,\"name\":\"U\"},{\"id\":22,\"name\":\"V\"},{\"id\":23,\"name\":\"W\"},{\"id\":24,\"name\":\"X\"},{\"id\":25,\"name\":\"Y\"},{\"id\":26,\"name\":\"Z\"},{\"id\":27,\"name\":\"#\"}],\"sex\":[{\"id\":-100,\"name\":\"全部\"},{\"id\":0,\"name\":\"男\"},{\"id\":1,\"name\":\"女\"},{\"id\":2,\"name\":\"组合\"}]}";

        Tags musicTag = JsonUtil.readValue(json, Tags.class);

        insertTags(musicTag.getArea(),"所属区域");
        insertTags(musicTag.getGenre(),"性别");
        insertTags(musicTag.getIndex(),"歌手姓名首字母");

    }

    private <T extends Tag> void insertTags(List<T> tList,String parentname) {
        MusicTag parentAreaTag = new MusicTag();
        parentAreaTag.setType("0");
        parentAreaTag.setName(parentname);

        //插入数据
        musicTagMapper.insert(parentAreaTag);

        for(T t:tList){
            MusicTag childTag = new MusicTag();
            childTag.setName(t.getName());
            childTag.setType(String.valueOf(t.getId()));
            childTag.setParent_id(parentAreaTag.getId());
            musicTagMapper.insert(childTag);
        }
    }


}