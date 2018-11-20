package com.xiu.crawling.douban.proxypool.site;

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
 * https://proxy.mimvp.com/
 */
public class MimvpProxyListPageParser implements ProxyListPageParser {
    private Logger log = LoggerFactory.getLogger(MimvpProxyListPageParser.class);
    @Override
    public List<Proxy> parse(String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.select("div[id=free_freelist_open] table tbody tr");
        List<Proxy> proxyList = new ArrayList<>(elements.size());
        for (Element element : elements){
            String ip = element.select("td:eq(1)").first().text();
            String port  = element.select("td:eq(2)").first().text();  //TODO 端口号是一张图片，要解析成字符串
            String type = element.select("td:eq(3)").first().text();
            String isAnonymous = element.select("td:eq(4)").first().text();
            log.debug("parse result = "+type+"://"+ip+":"+port+"  "+isAnonymous);
            if(!anonymousFlag || isAnonymous.contains("匿")){
                proxyList.add(new Proxy(ip, Integer.valueOf(port), type, Constant.TIME_INTERVAL));
            }
        }
        return proxyList;
    }
}
