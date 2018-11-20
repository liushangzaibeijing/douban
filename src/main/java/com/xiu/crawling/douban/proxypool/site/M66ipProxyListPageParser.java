package com.xiu.crawling.douban.proxypool.site;

import com.xiu.crawling.douban.proxypool.ProxyListPageParser;
import com.xiu.crawling.douban.proxypool.config.Constant;
import com.xiu.crawling.douban.proxypool.domain.Proxy;
import com.xiu.crawling.douban.proxypool.site.xicidaili.XicidailiProxyListPageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * http://m.66ip.cn/1.html
 */
public class M66ipProxyListPageParser implements ProxyListPageParser {
    private Logger log = LoggerFactory.getLogger(XicidailiProxyListPageParser.class);
    @Override
    public List<Proxy> parse(String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.select("table tr:gt(1)");
        List<Proxy> proxyList = new ArrayList<>(elements.size());
        for (Element element : elements){
            String ip = element.select("td:eq(0)").first().text();
            String port  = element.select("td:eq(1)").first().text();
            String isAnonymous = element.select("td:eq(3)").first().text();
            log.debug("parse result = http://"+ip+":"+port+"  "+isAnonymous);
            if(!anonymousFlag || isAnonymous.contains("匿")){
                proxyList.add(new Proxy(ip, Integer.valueOf(port), "http", Constant.TIME_INTERVAL));
            }
        }
        return proxyList;
    }
}
