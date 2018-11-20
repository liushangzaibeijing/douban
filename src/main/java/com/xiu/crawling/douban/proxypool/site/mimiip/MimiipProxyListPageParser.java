package com.xiu.crawling.douban.proxypool.site.mimiip;

import com.xiu.crawling.douban.proxypool.ProxyListPageParser;
import com.xiu.crawling.douban.proxypool.config.Constant;
import com.xiu.crawling.douban.proxypool.domain.Proxy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2017/10/19.
 */
public class MimiipProxyListPageParser implements ProxyListPageParser {

    @Override
    public List<Proxy> parse(String hmtl) {
        Document document = Jsoup.parse(hmtl);
        Elements elements = document.select("table[class=list] tr");
        List<Proxy> proxyList = new ArrayList<>(elements.size());
        for (int i = 1; i < elements.size(); i++){
            String isAnonymous = elements.get(i).select("td:eq(3)").first().text();
            if(!anonymousFlag || isAnonymous.contains("匿")){
                String ip = elements.get(i).select("td:eq(0)").first().text();
                String port  = elements.get(i).select("td:eq(1)").first().text();
                String type = elements.get(i).select("td:eq(4)").first().text();
                proxyList.add(new Proxy(ip, Integer.valueOf(port), type, Constant.TIME_INTERVAL));
            }
        }
        return proxyList;
    }
}
