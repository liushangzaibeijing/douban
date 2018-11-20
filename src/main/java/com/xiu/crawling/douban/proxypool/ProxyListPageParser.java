package com.xiu.crawling.douban.proxypool;


import com.xiu.crawling.douban.proxypool.domain.Proxy;

import java.util.List;

/**
 * Created by tony on 2017/10/19.
 * 在代理网站中获取代理ip
 */
public interface ProxyListPageParser {

    /**
     * 是否只要匿名代理
     */
    boolean anonymousFlag = true;
    List<Proxy> parse(String content);
}
