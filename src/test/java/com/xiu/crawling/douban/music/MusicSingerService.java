package com.xiu.crawling.douban.music;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiu.crawling.douban.DoubanApplication;
import com.xiu.crawling.douban.bean.*;
import com.xiu.crawling.douban.cache.GlobalVariables;
import com.xiu.crawling.douban.common.ConstantMusic;
import com.xiu.crawling.douban.enums.ConvertMusicSignerEnum;
import com.xiu.crawling.douban.enums.MusicTypeEnum;
import com.xiu.crawling.douban.exception.CrawlingException;
import com.xiu.crawling.douban.mapper.CurrPageInfoMapper;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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

    /**
     * 存储中断页码信息
     */
    @Autowired
    CurrPageInfoMapper currPageInfoMapper;
    /**
     * 歌手图片存放根路径
     */
    @Value("${singerPicBasePath}")
    String singerPicBasePath;
    //获取歌手信息
    @Test
    public void parseSigner(){
        Integer currentPage = getSingerCurPage();
        do{
            log.info("页码：{}",currentPage);
            String url = ConstantMusic.getMusicSignerListUrl(currentPage++);
            String result = HttpUtil.doGet(url);
            log.info("result:{}",result);
            //出现频繁的调用导致的 ip受限
            JSONObject resultJsonObj = JSONObject.parseObject(result);

            //歌手列表信息
            JSONArray singerlist = resultJsonObj.getJSONObject("singerList").getJSONObject("data").getJSONArray("singerlist");
            for(int i =0;i<singerlist.size();i++){
                JSONObject singerJson = singerlist.getJSONObject(i);
                //歌手
                String singerMid = singerJson.getString("singer_mid");
                String singerName = singerJson.getString("singer_name");
                try {
                    if (isExisterSinger(singerMid)) {
                        continue;
                    }
                    log.info("歌手{}的个人信息爬取开始", singerName);
                    Singer singer = getSinger(singerJson,singerName);

                    if(singer!=null){
                        //插入歌手信息
                        singerMapper.insert(singer);
                    }
                    log.info("歌手{}的个人信息爬取完成", singerName);
                } catch(Exception e){
                    e.printStackTrace();
                    log.error("{}-{} 爬取的过程中出现异常，异常信息如下：{}",singerMid,singerName,e.getMessage());
                    //保存出错信息
                    updateCurrPageInfo(currentPage,MusicTypeEnum.SINGER.getCode(),singerMid+"_"+singerName+" "+e.getMessage());

                }
                log.info("歌手信息：{}",result);
            }
        }while(true);


    }

    /**
     * 保存中断导致的信息
     * @param currentPage
     * @param type
     * @param errorMsg
     */
    private void updateCurrPageInfo(Integer currentPage, Integer type, String errorMsg) {
        CurrPageInfoExample currPageInfoExample = new CurrPageInfoExample();
        currPageInfoExample.createCriteria().andTypeEqualTo(type);

        CurrPageInfo currPageInfo = new CurrPageInfo();
        currPageInfo.setCurrPage(currentPage);
        errorMsg = errorMsg.length()>500?errorMsg.substring(0,500):errorMsg;
        currPageInfo.setMessage(errorMsg);
        currPageInfoMapper.updateByExampleSelective(currPageInfo,currPageInfoExample);
    }

    private Integer getSingerCurPage() {
        CurrPageInfoExample currPageInfoExample = new CurrPageInfoExample();
        currPageInfoExample.createCriteria().andTypeEqualTo(MusicTypeEnum.SINGER.getCode());
        List<CurrPageInfo> currPageInfos = currPageInfoMapper.selectByExample(currPageInfoExample);
        return currPageInfos.get(0).getCurrPage();
    }

    /**
     * 根据singerMId 判断歌手信息是否已经爬取完成
     * @param singerMid
     * @return
     */
    private boolean isExisterSinger(String singerMid) {
        SingerExample query = new SingerExample();
        query.createCriteria().andSignerMidEqualTo(singerMid);
        List<Singer> singers = singerMapper.selectByExample(query);
        if(singers==null|| singers.size()==0){
            return false;
        }
        return true;
    }

    private Singer getSinger(JSONObject singerJson,String singerName) {
        //歌手mid
        String signerMid = singerJson.getString("singer_mid");
        Singer singer = new Singer();
        //歌手id
        Integer signerId = singerJson.getInteger("singer_id");

        //singer_pic 歌手图片
        String singerPic = singerJson.getString("singer_pic");

        //下载图片并进行保存
        String savePathDir = generatorSaveDir(singerPicBasePath);
        String picLocal = HttpUtil.doDown(singerPic,null,savePathDir,signerMid+".jpg");
        //出现频繁的调用导致的 ip受限

        singer.setSignerId(signerId);
        singer.setPic(singerPic);
        singer.setPicLocal(picLocal);

        //获取歌手信息
        String detailUrl = ConstantMusic.getMusicSignerDetailUrl(signerMid);
        Map<String, String> headers = new HashMap<>();
        headers.put("Referer", "https://c.y.qq.com/xhr_proxy_utf8.html");
        String signerDetailXml = HttpUtil.doGetByHeader(detailUrl,headers);

        singer.setSignerMid(signerMid);
        singer = parseSingerINfoXml(signerDetailXml,singer);
        singer.setFullName(singerName);
        return singer;
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
            if(info==null){
                return singer;
            }
            //歌手基础信息
            List<Node> itemNodes = info.selectNodes("basic/item");
            if(itemNodes.isEmpty()){
                itemNodes = new ArrayList<>();
            }
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
                    singer.setFirstLetter(ChineseIndex.getFirstLetter(value));
                }

                if (initSingerOther(singer.getSignerMid(), key, value)){
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
            //歌手简介
            Node descNode = info.selectSingleNode("//desc");
            if(descNode!=null){
                String desc = descNode.getStringValue();
                singer.setDescption(desc);
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
    private boolean initSingerOther(String id, String key, String value) {
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

    private void insertSingerOther(String value, String key, String singerMid) {
        SingerOther singerOther = new SingerOther();
        singerOther.setType(key);
        singerOther.setValue(value);
        singerOther.setSingerId(singerMid);

        singerOtherMapper.insert(singerOther);

    }


    private String generatorSaveDir(String albumPicBasePath) {
        return albumPicBasePath+"/";
    }

}
