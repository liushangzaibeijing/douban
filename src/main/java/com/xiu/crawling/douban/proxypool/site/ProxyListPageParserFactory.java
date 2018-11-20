package com.xiu.crawling.douban.proxypool.site;



import com.xiu.crawling.douban.proxypool.ProxyListPageParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tony on 2017/10/19.
 * 获取代理页面的解析对象工厂类
 */
public class ProxyListPageParserFactory {

    private static Map<String, ProxyListPageParser> map = new HashMap<>();

    /**
     * 创建代理网页的解析对象实例
     * @param clazz
     * @return
     */
    public static ProxyListPageParser getProxyListPageParser(Class clazz){

        String parserName = clazz.getSimpleName();
        if (map.containsKey(parserName)){
            return map.get(parserName);
        } else {
            try {
                ProxyListPageParser parser = (ProxyListPageParser) clazz.newInstance();
                parserName = clazz.getSimpleName();
                map.put(parserName, parser);
                return parser;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
