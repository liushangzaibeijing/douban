package com.xiu.crawling.douban.proxypool.site.coderbusy;


import com.xiu.crawling.douban.proxypool.ProxyListPageParser;
import com.xiu.crawling.douban.proxypool.config.Constant;
import com.xiu.crawling.douban.proxypool.domain.Proxy;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析目标：https://proxy.coderbusy.com/classical/anonymous-type/highanonymous.aspx?page=1
 */
@Slf4j
public class CoderBusyProxyListPageParser implements ProxyListPageParser {
    private Logger log = LoggerFactory.getLogger(CoderBusyProxyListPageParser.class);
    @Override
    public List<Proxy> parse(String content) {
        Document document = Jsoup.parse(content);
        Elements elements = document.select("div[class='table-responsive'] table[class='table'] tbody tr");
        List<Proxy> proxyList = new ArrayList<>(elements.size());
        for (Element element : elements){
            String ip = element.select("td:eq(0)").first().text();
            String port  = element.select("td:eq(2)").first().text();
            String type = element.select("td:eq(5)").first().text();
            String isAnonymous = element.select("td:eq(7)").first().text();
            System.out.println("ip:"+ip);
            log.debug("parse result = "+type+"://"+ip+":"+port+"  "+isAnonymous);
            if(!anonymousFlag || isAnonymous.contains("匿")){
                proxyList.add(new Proxy(ip, Integer.valueOf(port), type, Constant.TIME_INTERVAL));
            }
        }
        return proxyList;
    }
}
