package com.xiu.crawling.douban.proxypool.config;

import com.xiu.crawling.douban.proxypool.site.*;
import com.xiu.crawling.douban.proxypool.site.xicidaili.XicidailiProxyListPageParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tony on 2017/10/19.
 */
public class Constant {
    /**
     * 单个ip请求间隔，单位ms
     */
    public final static long TIME_INTERVAL = 1000;

    /**
     * 浏览器的请求头信息
     */
    public final static String[] userAgentArray = new String[]{
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36"
    };

    public final static String COL_NAME_JOB_LOG = "Job_Log";

    public final static String COL_NAME_SYS_SEQUENCE = "Sys_Sequence";

    public final static String COL_NAME_PROXY = "Proxy";

    public final static String COL_NAME_PROXY_RESOURCE = "Proxy_Resource";

    public final static String COL_NAME_RESOURCE_PLAN = "Resource_Plan";

    /**
     * 所有的代理对象
     */
    public final static Map<String, Class> proxyMap = new HashMap<>();

    static {
        //proxyMap.put("http://www.xicidaili.com/nn/1.html", XicidailiProxyListPageParser.class);
        //proxyMap.put("https://proxy.mimvp.com/", MimvpProxyListPageParser.class);
        //proxyMap.put("http://www.mogumiao.com/web", MogumiaoProxyListPageParser.class);
        //proxyMap.put("http://www.goubanjia.com/", GoubanjiaProxyListPageParser.class);
        //proxyMap.put("http://m.66ip.cn/2.html", M66ipProxyListPageParser.class);
        //proxyMap.put("http://www.data5u.com/", Data4uProxyListPageParser.class);
        //proxyMap.put("https://list.proxylistplus.com/Fresh-HTTP-Proxy-List-1", ProxyListPlusProxyListPageParser.class);
        //proxyMap.put("http://www.ip3366.net/", Ip3366ProxyListPageParser.class);//TODO gb2312如何处理？
        proxyMap.put("http://www.feilongip.com/", FeilongipProxyListPageParser.class);
        //proxyMap.put("http://proxydb.net/", ProxyDbProxyListPageParser.class);
        proxyMap.put("http://www.xiaohexia.cn/", XiaoHeXiaProxyListPageParser.class);
    }
}
