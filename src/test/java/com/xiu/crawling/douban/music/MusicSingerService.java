package com.xiu.crawling.douban.music;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiu.crawling.douban.DoubanApplication;
import com.xiu.crawling.douban.bean.Singer;
import com.xiu.crawling.douban.bean.SingerOther;
import com.xiu.crawling.douban.common.ConstantMusic;
import com.xiu.crawling.douban.enums.ConvertMusicSignerEnum;
import com.xiu.crawling.douban.mapper.SingerMapper;
import com.xiu.crawling.douban.mapper.SingerOtherMapper;
import com.xiu.crawling.douban.utils.ChineseIndex;
import com.xiu.crawling.douban.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DoubanApplication.class)
@WebAppConfiguration
public class MusicSingerService {
    /**
     * 歌手的附属信息
     */
    @Autowired
    SingerOtherMapper singerOtherMapper;

    /**
     * 歌手信息
     */
    @Autowired
    SingerMapper singerMapper;
    //获取歌手信息
    @Test
    public void parseSigner(){
        String url = ConstantMusic.getMusicSignerListUrl(1);

        String result = HttpUtil.doGet(url);
        log.info("result:{}",result);
        //出现频繁的调用导致的 ip受限
        JSONObject resultJsonObj = JSONObject.parseObject(result);

        //歌手列表信息
        JSONArray singerlist = resultJsonObj.getJSONObject("singerList").getJSONObject("data").getJSONArray("singerlist");
        for(int i =0;i<singerlist.size();i++){
            JSONObject singerJson = singerlist.getJSONObject(i);
            //歌手id
            Integer signerId = singerJson.getInteger("singer_id");
            //歌手mid
            String signerMid = singerJson.getString("singer_mid");
            //singer_pic 歌手图片
            String singerPic = singerJson.getString("singer_pic");

            //获取歌手信息
            String detailUrl = ConstantMusic.getMusicSignerDetailUrl(signerMid);

            Map<String, String> headers = new HashMap<>();
            headers.put("Referer", "https://c.y.qq.com/xhr_proxy_utf8.html");
            String signerDetailXml = HttpUtil.doGetByHeader(detailUrl,headers);
            //出现频繁的调用导致的 ip受限
            Singer singer = new Singer();
            singer.setSignerId(signerId);
            singer.setSignerMid(signerMid);
            singer.setPic(singerPic);
            singer = parseSingerINfoXml(signerDetailXml,singer);
            log.info("歌手{}的个人信息爬取完成",singer.getFullName());
            singer.setSignerMid(signerMid);
            singer.setPic(singerPic);
            singerMapper.insert(singer);

        }

        log.info("歌手信息：{}",result);

    }

    /**
     * 解析xml形式的歌手信息
     * @param signerDetailXml
     * @return 歌手信息对象 用于入库
     */
    private Singer parseSingerINfoXml(String signerDetailXml,Singer singer)  {
        ByteArrayInputStream is = new ByteArrayInputStream(signerDetailXml.getBytes());
        SAXReader sax = new SAXReader();
        try {
            org.dom4j.Document document = sax.read(is);
            Element rootElement = document.getRootElement();
            Node info = rootElement.selectSingleNode("//data/info");
            //歌手简介
            String desc = info.selectSingleNode("//desc").getStringValue();
            singer.setDescption(desc);
            //歌手基础信息
            List<Node> itemNodes = info.selectNodes("basic/item");

            //歌手其他信息
            List<Node> otherItemNodes = info.selectNodes("other/item");
            itemNodes.addAll(otherItemNodes);
            for(Node node:itemNodes){
                Element keyEle = (Element) node.selectSingleNode("key");
                String key = keyEle.getData().toString();
                log.info("歌手信息对应的key:{} 解析开始",key);
                Element valueEle = (Element) node.selectSingleNode("value");
                String value = valueEle.getData().toString();
                //首字母
                if(key.equals(ConvertMusicSignerEnum.FULL_NAME.getKey())){
                    //转换contryId
                    singer.setFirstLetter(ChineseIndex.getFirstLetter(key));
                }

                if (initSingerOther(singer.getId(), key, value)){
                    log.info("歌手信息对应的key:{} 解析完成",key);
                    continue;
                }

                String prop = ConvertMusicSignerEnum.convertProp(key);
                if(StringUtils.isEmpty(prop)){
                    log.info("歌手信息对应的key:{} 解析完成",key);
                    continue;
                }
                Field field = singer.getClass().getDeclaredField(prop);
                field.setAccessible(true);
                field.set(singer,value);
                field.setAccessible(false);
                log.info("歌手信息对应的key:{} 解析完成",key);
            }

        } catch (DocumentException e) {
            log.error("xml类型的字符串转换为Document出错");
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            log.error("java 反射获取字段错误");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            log.error("java 反射非法访问私有变量错误");
            e.printStackTrace();
        }
        return singer;
    }

    /**
     * 插入歌手的其他信息
     * @param id signer_id
     * @param key 主要成就 演绎经历等
     * @param value  主要值
     * @return
     */
    private boolean initSingerOther(Integer id, String key, String value) {
        //MAJOR_ACHIEVE("主要成就","majorAchieve"),
        if(key.equals(ConvertMusicSignerEnum.MAJOR_ACHIEVE.getKey())){
            //转换contryId
            insertSingerOther(value,key,id);
            return true;
        }

        //从艺历程
        if(key.equals(ConvertMusicSignerEnum.ART_COURSE.getKey())){
            //转换contryId
            insertSingerOther(value,key,id);
            return true;
        }

        //荣誉记录
        if(key.equals(ConvertMusicSignerEnum.HONORARY_RECORDS.getKey())){
            //转换contryId
            insertSingerOther(value,key,id);
            return true;
        }
        //社会活动
        if(key.equals(ConvertMusicSignerEnum.SOCIAL_ACTIVITIES.getKey())){
            //转换contryId
            insertSingerOther(value,key,id);
            return true;
        }
        if(key.equals("代表作品")|| key.equals("精通语言")
                ||key.equals("所属唱片公司")
                ||key.equals("粤语拼音")
                ||key.equals("个人生活")){
            return true;
        }

        return false;
    }

    private void insertSingerOther(String value, String key, Integer id) {
        SingerOther singerOther = new SingerOther();
        singerOther.setType(key);
        singerOther.setValue(value);
        singerOther.setSingerId(id);

        singerOtherMapper.insert(singerOther);

    }

}
