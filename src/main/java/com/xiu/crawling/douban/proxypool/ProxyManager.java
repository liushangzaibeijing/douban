package com.xiu.crawling.douban.proxypool;

import com.xiu.crawling.douban.proxypool.domain.Proxy;
import com.xiu.crawling.douban.proxypool.http.HttpManager;
import com.xiu.crawling.douban.proxypool.task.ProxyPageCallable;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import org.apache.http.HttpHost;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by tony on 2017/10/25.
 */
public class ProxyManager {
    private Logger log = LoggerFactory.getLogger(ProxyPageCallable.class);
    private ProxyManager() {
    }

    public static ProxyManager get() {
        return Holder.MANAGER;
    }

    private static class Holder {
        private static final ProxyManager MANAGER = new ProxyManager();
    }

    /**
     * 抓取代理，成功的代理存放到ProxyPool中
     */
    public void start() {

        Flowable.fromIterable(ProxyPool.proxyMap.keySet())
                .parallel(ProxyPool.proxyMap.size())
                .map(new Function<String, List<Proxy>>() {
                    @Override
                    public List<Proxy> apply(String s) throws Exception {

                        try {
                            return new ProxyPageCallable(s).call();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return new ArrayList<Proxy>();
                    }
                })
                .flatMap(new Function<List<Proxy>, Publisher<Proxy>>() {
                    @Override
                    public Publisher<Proxy> apply(List<Proxy> proxies) throws Exception {
                        //if (Preconditions.isNotBlank(proxies)) {
                        if (proxies!=null && proxies.size()>0) {
                            List<Proxy> result = proxies
                                    .stream()
                                    .parallel()
                                    .filter(new Predicate<Proxy>() {
                                        @Override
                                        public boolean test(Proxy proxy) {
                                            HttpHost httpHost = new HttpHost(proxy.getIp(), proxy.getPort(), proxy.getType());
                                            boolean result = HttpManager.get().checkProxy(httpHost);
                                            if(result){
                                                log.info("checkProxy " + proxy.getProxyStr() +", "+result);
                                            }
                                            return result;
                                        }
                                    }).collect(Collectors.toList());

                            return Flowable.fromIterable(result);
                        }

                        return Flowable.empty();
                    }
                })
                .runOn(Schedulers.io())
                .sequential()
                .subscribe(new Consumer<Proxy>() {
                    @Override
                    public void accept(Proxy proxy) throws Exception {

                        if (proxy!=null) {
                            log.info("accept " + proxy.getProxyStr());
                            proxy.setLastSuccessfulTime(System.currentTimeMillis());
                            ProxyPool.proxyList.add(proxy);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        log.error("ProxyManager is error: "+throwable.getMessage());
                    }
                });
    }
}
