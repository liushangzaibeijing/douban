package com.xiu.crawling.douban.core.service;

import org.apache.http.HttpHost;

/**
 * author   xieqx
 * createTime  2018/10/27
 * desc 代理相关服务接口
 */
public interface ProxyService {

    /**
     * 获取一个代理服务类 HttpPoxy
     */
    HttpHost findCanUseProxy();

    /**
     * 获取一个代理服务类 HttpPoxy
     */
    Boolean updateProxy(Boolean flag, HttpHost proxy);
}
