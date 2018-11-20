package com.xiu.crawling.douban.proxypool.task;

import com.google.common.base.Preconditions;
import com.xiu.crawling.douban.proxypool.ProxyListPageParser;
import com.xiu.crawling.douban.proxypool.ProxyPool;
import com.xiu.crawling.douban.proxypool.domain.Page;
import com.xiu.crawling.douban.proxypool.domain.Proxy;
import com.xiu.crawling.douban.proxypool.http.HttpManager;
import com.xiu.crawling.douban.proxypool.site.ProxyListPageParserFactory;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by tony on 2017/10/25.
 */
public class ProxyPageCallable implements Callable<List<Proxy>>{
    private Logger log = LoggerFactory.getLogger(ProxyPageCallable.class);
    protected String url;

    public ProxyPageCallable(String url){
        this.url = url;
    }

    @Override
    public List<Proxy> call() throws Exception {

        long requestStartTime = System.currentTimeMillis();

        try {
            //获取代理ip对象的页面html
            Page page = HttpManager.get().getWebPage(url);
            int status = page.getStatusCode();
            long requestEndTime = System.currentTimeMillis();

            StringBuilder sb = new StringBuilder();
            sb.append(Thread.currentThread().getName()).append(" ")
                    .append("  ,executing request ").append(page.getUrl()).append(" ,response statusCode:").append(status)
                    .append("  ,request cost time:").append(requestEndTime - requestStartTime).append("ms");

            String logStr = sb.toString();

            if(status == HttpStatus.SC_OK){
                log.info("Success: "+logStr);
                return handle(page);
            } else if (status>=400){ // http请求没有成功，尝试使用代理抓取数据源的策略

                Proxy proxy = null;

                for (int i=0; i<3; i++) {

                    proxy = ProxyPool.getProxy(); // 从代理池中获取数据

                    if (proxy!=null && HttpManager.get().checkProxy(proxy.toHttpHost())) { // 代理可用的情况下

                        requestStartTime = System.currentTimeMillis();
                        page = HttpManager.get().getWebPage(url,proxy);
                        status = page.getStatusCode();
                        requestEndTime = System.currentTimeMillis();

                        sb = new StringBuilder();
                        sb.append(Thread.currentThread().getName()).append(" ")
                                .append("  ,executing request ").append(page.getUrl()).append(" ,response statusCode:").append(status)
                                .append("  ,request cost time:").append(requestEndTime - requestStartTime).append("ms");

                        logStr = sb.toString();

                        if (status == HttpStatus.SC_OK) {

                            log.info("Success: "+logStr);
                            return handle(page);
                        } else {

                            log.info("Failure: "+logStr);
                        }
                    }
                }

            } else {

                log.info("Failure: "+logStr);
            }

        } catch (IOException e) {
            log.info("IOException: e="+e.getMessage());
        }

        return new ArrayList<Proxy>();
    }

    /**
     * 将下载的proxy放入代理池
     * @param page
     */
    private List<Proxy> handle(Page page){

        //if (page == null || Preconditions.isBlank(page.getHtml())){
          if (page == null || StringUtils.isEmpty(page.getHtml())){
            return new ArrayList<Proxy>();
        }

        List<Proxy> result = new ArrayList<>();

        ProxyListPageParser parser = ProxyListPageParserFactory.getProxyListPageParser(ProxyPool.proxyMap.get(url));
        if (parser!=null) {

            List<Proxy> proxyList = parser.parse(page.getHtml());
            //if(Preconditions.isNotBlank(proxyList)) {
            if(proxyList!=null && proxyList.size()>0) {
                    for(Proxy p : proxyList){
                        if (!ProxyPool.proxyList.contains(p)) {
                            result.add(p);
                        }
                    }
                }
            }
        return result;
    }
}
