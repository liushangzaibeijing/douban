package com.xiu.crawling.douban.bean.vo;

import lombok.Getter;
import lombok.Setter;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.util.StringUtils;

/**
 * author   xieqx
 * createTime  2018/11/10
 * desc 拼接动态的url连接
 */
@Setter
@Getter
public class UrlVO {

    private String url;

    private String sort;

    private Integer type;

    /**
     * intervalId 格式 100:90
     */
    private String intervalId;



    /********************* 非持久化数据 ************************/
    private String baseUrl;

    private Integer pageLimit;

    private Integer pageStart;

    private Integer start;

    private Integer pageSize;

    private Integer limit;

    private Integer pageIndex;

    public UrlVO addUrl(String url){
        this.url = url;
        baseUrl = url;
        return  this;
    }

    public UrlVO addType(Integer type) throws Exception {
        if(StringUtils.isEmpty(baseUrl)){
            throw new Exception("base URL 不能为空");
        }
        if(type !=null){
            this.type = type;
            if(!baseUrl.contains("?")){
                baseUrl +="?type="+type;
            }else{
                baseUrl +="&type="+type;
            }

        }
        return  this;
    }

    public UrlVO addIntervalId(String intervalId) throws Exception {
        if(StringUtils.isEmpty(baseUrl)){
            throw new Exception("base URL 不能为空");
        }
        if(!StringUtils.isEmpty(intervalId)) {
            this.intervalId = intervalId;
            if(!baseUrl.contains("?")){
                baseUrl +="?interval_id="+intervalId;
            }else {
                baseUrl += "&interval_id=" + intervalId;
            }

        }
        return  this;
    }

    public UrlVO addStart(Integer start) throws Exception {
        if(StringUtils.isEmpty(baseUrl)){
            throw new Exception("base URL 不能为空");
        }
        if(start !=null) {
            this.start = start;
            if(!baseUrl.contains("?")){
                baseUrl +="?start="+start;
            }else {
                baseUrl += "&start=" + start;
            }

        }
        return  this;
    }

    public UrlVO addLimit(Integer limit) throws Exception {
        if(StringUtils.isEmpty(baseUrl)){
            throw new Exception("base URL 不能为空");
        }
        if(limit !=null) {
            this.limit = limit;
            if(!baseUrl.contains("?")){
                baseUrl +="?limit="+limit;
            }else {
                baseUrl += "&limit=" + limit;
            }
        }
        return  this;
    }

    public UrlVO addSort(String sort) throws Exception {
        if(StringUtils.isEmpty(baseUrl)){
            throw new Exception("base URL 不能为空");
        }
        if(!StringUtils.isEmpty(sort)) {
            this.sort = sort;
            if(!baseUrl.contains("?")){
                baseUrl +="?sort="+sort;
            }else {
                baseUrl += "&sort=" + sort;
            }
        }
        return  this;
    }

    public UrlVO addPageLimit(Integer pageSize)throws Exception {
        if(StringUtils.isEmpty(baseUrl)){
            throw new Exception("base URL 不能为空");
        }
        if(pageSize !=null) {
            this.pageSize = pageSize;
            if(!baseUrl.contains("?")){
                baseUrl +="?pageSize="+pageSize;
            }else {
                baseUrl += "&page_limit=" + pageSize;
            }
        }
        return  this;
    }

    public UrlVO addPageStart(Integer pageStart)throws Exception {
        if(StringUtils.isEmpty(baseUrl)){
            throw new Exception("base URL 不能为空");
        }
        if(pageStart !=null) {
            this.pageStart = pageStart;
            if(!baseUrl.contains("?")){
                baseUrl +="?pageSize="+pageSize;
            }else {
                baseUrl += "&page_start=" + pageStart;
            }
        }
        return  this;
    }


}
