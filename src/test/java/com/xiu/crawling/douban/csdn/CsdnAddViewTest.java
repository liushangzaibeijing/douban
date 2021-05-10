package com.xiu.crawling.douban.csdn;

import com.github.pagehelper.PageHelper;
import com.xiu.crawling.douban.DoubanApplication;
import com.xiu.crawling.douban.bean.Movie;
import com.xiu.crawling.douban.bean.UrlInfo;
import com.xiu.crawling.douban.bean.UrlInfoExample;
import com.xiu.crawling.douban.common.ActiveEnum;
import com.xiu.crawling.douban.core.BookThreadTask;
import com.xiu.crawling.douban.core.MovieThreadTask;
import com.xiu.crawling.douban.core.service.ProxyService;
import com.xiu.crawling.douban.mapper.BookMapper;
import com.xiu.crawling.douban.mapper.ErrUrlMapper;
import com.xiu.crawling.douban.mapper.UrlInfoMapper;
import com.xiu.crawling.douban.proxypool.ProxyListPageParser;
import com.xiu.crawling.douban.proxypool.config.Constant;
import com.xiu.crawling.douban.proxypool.domain.Page;
import com.xiu.crawling.douban.proxypool.domain.Proxy;
import com.xiu.crawling.douban.proxypool.http.HttpManager;
import com.xiu.crawling.douban.proxypool.site.M66ipProxyListPageParser;
import com.xiu.crawling.douban.utils.HttpUtil;
import com.xiu.crawling.douban.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * author  Administrator
 * date   2021/04/17
 */
@Slf4j
public class CsdnAddViewTest {

    @Test
    public void testAddViewNum() throws IllegalAccessException, InstantiationException, IOException {

        String urlDetail = "https://blog.csdn.net/liushangzaibeijing/article/details/115771358";

        List<Proxy> proxies = new ArrayList<>();
        long requestStartTime = System.currentTimeMillis();

        for(Map.Entry<String,Class>  proxyData:Constant.proxyMap.entrySet()){
            String url = proxyData.getKey();
            Class value = proxyData.getValue();

            ProxyListPageParser pageParser = (ProxyListPageParser)value.newInstance();
            for(int i=0;i<100;i++){
                url +="?stype=1&page="+i+1;
                Page page = HttpManager.get().getWebPage(url);
                int status = page.getStatusCode();
                long requestEndTime = System.currentTimeMillis();

                StringBuilder sb = new StringBuilder();
                sb.append(Thread.currentThread().getName()).append(" ")
                        .append("  ,executing request ").append(page.getUrl()).append(" ,response statusCode:").append(status)
                        .append("  ,request cost time:").append(requestEndTime - requestStartTime).append("ms");

                String logStr = sb.toString();

                if(status == HttpStatus.SC_OK) {
                    log.info("Success: " + logStr);
                    List<Proxy> parse = pageParser.parse(page.getHtml());
                    proxies.addAll(parse);
                }
            }

        }

         for(Proxy proxy:proxies){
             HttpHost host = new HttpHost(proxy.getIp(),proxy.getPort());
             boolean result = HttpManager.get().checkProxy(host);
             if(result){
                 try{
                     HttpUtil.doGet(urlDetail,host);
                 }catch (Exception e){
                     continue;
                 }
             }

         }
    }

    @Test
    public void testAddViewNumTwo() {

        String urlDetail = "https://blog.csdn.net/liushangzaibeijing/article/details/115771358";


        HttpHost host = new HttpHost("220.179.219.157",8888);
        boolean result = HttpManager.get().checkProxy(host);
        try{
            HttpUtil.doGet(urlDetail,host);
        }catch (Exception e){
           log.info(e.getMessage());
        }
    }
}