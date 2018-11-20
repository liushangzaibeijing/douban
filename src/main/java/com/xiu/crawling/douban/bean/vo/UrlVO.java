package com.xiu.crawling.douban.bean.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * author   xieqx
 * createTime  2018/11/10
 * desc 一句话描述该功能
 */
@Setter
@Getter
public class UrlVO {
    //https://movie.douban.com/j/search_subjects?type=movie&tag=%E7%83%AD%E9%97%A8&sort=recommend&page_limit=100000000000&page_start=0

    private String baseUrl;

    private String sort;

    private String pageLimit;

    private String pageStart;


    public UrlVO addUrl(String url){
        this.baseUrl = url;
        return  this;
    }

    public UrlVO addSort(String sort) throws Exception {
        if(StringUtils.isEmpty(baseUrl)){
            throw new Exception("base URL 不能为空");
        }
        baseUrl +="&sort="+sort;
        return  this;
    }

    public UrlVO addPageLimit(int pageSize)throws Exception {
        if(StringUtils.isEmpty(baseUrl)){
            throw new Exception("base URL 不能为空");
        }
        baseUrl +="&page_limit="+pageSize;
        return  this;
    }

    public UrlVO addPageStart(int pageStart)throws Exception {
        if(StringUtils.isEmpty(baseUrl)){
            throw new Exception("base URL 不能为空");
        }
        baseUrl +="&page_start="+pageStart;
        return  this;
    }


}
