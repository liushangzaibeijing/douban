package com.xiu.crawling.douban.core.service.impl;

import com.xiu.crawling.douban.bean.Proxydata;
import com.xiu.crawling.douban.bean.ProxydataExample;
import com.xiu.crawling.douban.common.Constant;
import com.xiu.crawling.douban.core.service.ProxyService;
import com.xiu.crawling.douban.mapper.ProxydataMapper;
import com.xiu.crawling.douban.proxypool.http.HttpManager;
import com.xiu.crawling.douban.proxypool.job.ScheduleJobs;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * author   xieqx
 * createTime  2018/10/27
 * desc 代理服务的实现类
 */
@Slf4j
@Service
@Transactional
public class ProxyServiceImpl implements ProxyService{
    private static final int PAGESIZE = 10;
    @Autowired
    private ProxydataMapper proxydataMapper;

    @Autowired
    private ScheduleJobs scheduleJobs;
    @Override
    public HttpHost findCanUseProxy() {
       //获取可以使用的代理记录  分页查询

        List<HttpHost> proxyList = new ArrayList<>();
        int i =0;
        //有可能会陷入死循环 需要返回
        while (true){
            List<Proxydata> proxydatas = null;
            //proxydatas = proxydataMapper.selectByExample(proxydataExample);
            Map<String,Object> param = new HashMap<>();

            param.put("start",i*Constant.pageSize);
            param.put("limit", Constant.pageSize);
            param.put("canUse",1);
            proxydatas =proxydataMapper.selectProxyListNoCache(param);

            if(proxydatas==null || proxydatas.size()<=0){
                 break;
            }
            if(proxyList.size()>=10){
                break;
            }
            //检查代理是否可用
            for(Proxydata data : proxydatas){
                HttpHost proxy = proxyData2HttpHost(data);

                if(HttpManager.get().checkProxy(proxy)){
                    proxyList.add(proxy);
                }else{
                    updateProxy(false,proxy);
                }
            }
            i++;
        }

        int size = proxyList.size();
        if(size<=0){
            //开启代理池ip获取
            scheduleJobs.cronJob();

        }

        return proxyList.get(new Random().nextInt(size));

    }


    @Override
    public Boolean updateProxy(Boolean flag, HttpHost proxy) {
       int canUse = flag?1:0;
       ProxydataExample proxydataExample = new ProxydataExample();
       proxydataExample.createCriteria().andIpEqualTo(proxy.getHostName()).andPortEqualTo(proxy.getPort());
       Proxydata proxydata = new Proxydata();
       proxydata.setCanuse(canUse);
       return  proxydataMapper.updateByExampleSelective(proxydata,proxydataExample)==1?true:false;
    }


    //更新ip数据不可用


    private HttpHost proxyData2HttpHost(Proxydata proxydata){
        HttpHost proxy = new HttpHost(proxydata.getIp(),proxydata.getPort(),proxydata.getType());
        return  proxy;
    }

}
