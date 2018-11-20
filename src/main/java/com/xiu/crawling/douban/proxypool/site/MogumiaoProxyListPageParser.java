package com.xiu.crawling.douban.proxypool.site;

import com.xiu.crawling.douban.proxypool.ProxyListPageParser;
import com.xiu.crawling.douban.proxypool.config.Constant;
import com.xiu.crawling.douban.proxypool.domain.Proxy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * http://www.mogumiao.com/web
 */
public class MogumiaoProxyListPageParser implements ProxyListPageParser {
    private Logger log = LoggerFactory.getLogger(MogumiaoProxyListPageParser.class);
    @Override
    public List<Proxy> parse(String html) {
        Document document = Jsoup.parse(html);  //TODO 获取的html关键内容被base64了
        Elements elements = document.select("tr[class=el-table__row]");
        List<Proxy> proxyList = new ArrayList<>(elements.size());
        for (Element element : elements){
            String ip = element.select("td:eq(0)").first().text();
            String port  = element.select("td:eq(1)").first().text();
            String type = element.select("td:eq(2)").first().text();
            type = type.contains("//") ? "HTTPS" : type;
            String isAnonymous = element.select("td:eq(3)").first().text();
            log.debug("parse result = "+type+"://"+ip+":"+port+"  "+isAnonymous);
            if(!anonymousFlag || isAnonymous.contains("匿")){
                proxyList.add(new Proxy(ip, Integer.valueOf(port), type, Constant.TIME_INTERVAL));
            }
        }
        return proxyList;
    }
}
