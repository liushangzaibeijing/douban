package com.xiu.crawling.douban.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JsonUtil {
    private static Logger log = LoggerFactory.getLogger(JsonUtil.class);


    static ObjectMapper mapper = new ObjectMapper();

    public static byte[] obj2byte(Object obj){
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            log.error("json 转换错误",e);
        }
        return null;
    }

    public static String obj2str(Object obj){
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("json 转换错误",e);
        }
        return null;
    }

    public static <T> T readValue(String content, Class<T> valueType){
        try {
            return  mapper.readValue(content, valueType);
        } catch (Exception e) {
            log.error("json 转换错误",e);
        }
        return null;
    }
}
