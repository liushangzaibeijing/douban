package com.xiu.crawling.douban.bean.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * author   xieqx
 * createTime  2018/11/10
 * desc 拼接动态的url连接
 */
@Setter
@Getter
public class UrlVO {
    //https://movie.douban.com/j/search_subjects?type=movie&tag=%E7%83%AD%E9%97%A8&sort=recommend&page_limit=100000000000&page_start=0

    //https://movie.douban.com/j/chart/top_list?type=11&interval_id=100%3A90&action=&start=0&limit=20
    //https://movie.douban.com/j/chart/top_list_count?type=11&interval_id=100%3A90

    private String baseUrl;

    private String sort;

    private String pageLimit;

    private String pageStart;

    //intervalId 格式 100:90
    private String intervalId;

    private String start;

    private String limit;


    public UrlVO addUrl(String url){
        this.baseUrl = url;
        return  this;
    }

    public UrlVO addIntervalId(String intervalId) throws Exception {
        if(StringUtils.isEmpty(baseUrl)){
            throw new Exception("base URL 不能为空");
        }

        baseUrl +="&intervalId="+intervalId;
        return  this;
    }

    public UrlVO addStart(Integer start) throws Exception {
        if(StringUtils.isEmpty(baseUrl)){
            throw new Exception("base URL 不能为空");
        }
        baseUrl +="&start="+start;
        return  this;
    }

    public UrlVO addLimit(Integer limit) throws Exception {
        if(StringUtils.isEmpty(baseUrl)){
            throw new Exception("base URL 不能为空");
        }
        baseUrl +="&limit="+limit;
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
