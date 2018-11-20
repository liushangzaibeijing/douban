package com.xiu.crawling.douban.proxypool.job;

import com.xiu.crawling.douban.bean.Proxydata;
import com.xiu.crawling.douban.bean.ProxydataExample;
import com.xiu.crawling.douban.mapper.ProxydataMapper;
import com.xiu.crawling.douban.proxypool.ProxyManager;
import com.xiu.crawling.douban.proxypool.ProxyPool;
import com.xiu.crawling.douban.proxypool.config.Constant;
import com.xiu.crawling.douban.proxypool.domain.Proxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tony on 2017/11/22.
 */
@Component
@Slf4j
public class ScheduleJobs {


    public final static int JOB_STATUS_INIT = 0;
    public final static int JOB_STATUS_RUNNING = 1;
    public final static int JOB_STATUS_STOPPED = 2;

    protected AtomicInteger stat = new AtomicInteger(JOB_STATUS_INIT);

    /**
     * 代理ip 对象
     */
    @Autowired
    ProxydataMapper proxydataMapper;

    /*
      代理对象的核心处理
     */
    ProxyManager proxyManager = ProxyManager.get();

    /**
     * 每隔几个小时跑一次任务
     */
    //@Scheduled(cron="${cronJob.schedule}")
    @Scheduled(fixedRate=1000*60*5)
    public void cronJob() {

        //1.检查job的状态
        checkRunningStat();

        log.info("Job Start...");

        //2.获取目标网页的Url
        ///ProxyPool.proxyMap = proxyDao.getProxyMap();

        //3.如果数据库里没取到，用默认内置的
        if(ProxyPool.proxyMap ==null || ProxyPool.proxyMap.size()==0) {
            log.info("Job proxyDao.getProxyMap() is empty");
            ProxyPool.proxyMap = Constant.proxyMap;
        }

        //4.每次跑job先清空缓存中的内容
        //if (cacheManager.getCache("proxys")!=null) {
            //cacheManager.getCache("proxys").clear();
        //}

        //5.创建一个日志对象，用于存储job的每次工作记录
        /*
        JobLog jobLog = new JobLog();
        jobLog.setJobName("ScheduleJobs.cronJob");
        jobLog.setStartTime(JodaUtils.formatDateTime(new Date()));
        */
        //6.跑任务之前先清空proxyList中上一次job留下的proxy数据，
        ProxyPool.proxyList.clear();

        //7.从数据库中选取10个代理作为种子代理，遇到http 503时使用代理来抓数据
        // ProxyPool.addProxyList(getProxyList(proxyDao.takeRandomTenProxy()));
        log.info("Job ProxyPool.proxyList size = "+ProxyPool.proxyList.size());
        //8.正式开始，爬代理数据
        proxyManager.start();

        //9.爬完以后，把数据转换为ProxyData并存到数据库
        CopyOnWriteArrayList<Proxydata> list = getProxyDataList(ProxyPool.proxyList);
        log.info("Job ProxyData list size = "+list.size());
        //if (Preconditions.isNotBlank(list)) {
        if (list!=null && list.size()>0) {
            // 10. list的数量<=15时，不删除数据库里的老数据
            if (list.size()>40) {
                proxydataMapper.deleteByExample(new ProxydataExample());
                log.info("Job after deleteAll");
            }

            //11. 然后再进行插入新的proxy
            for (Proxydata p:list) {
                //if(!p.getType().equals("https")){
                    p.setCanuse(1);
                    proxydataMapper.insert(p);
                //}

            }
            log.info("Job save count = "+list.size());

            //jobLog.setResultDesc(String.format("success save count = %s", list.size()));
            //jobLog.setEndTime(JodaUtils.formatDateTime(new Date()));
            //commonDao.saveJobLog(jobLog);

        } else {
            log.info("Job proxyList is empty...");
        }

        //12. 设置job状态为停止
        stop();

        log.info("Job End...");
    }

    private void checkRunningStat() {
        while (true) {

            int statNow = getJobStatus();

            //如果已经在运行了，就抛出异常，结束循环
            if (statNow == JOB_STATUS_RUNNING) {
                throw new IllegalStateException("Job is already running!");
            }

            //如果还没在运行，就设置为运行状态，结束循环
            if (stat.compareAndSet(statNow, JOB_STATUS_RUNNING)) {
                break;
            }
        }
    }

    public int  getJobStatus() {

        return stat.get();
    }

    public void stop() {
        //状态从JOB_STATUS_RUNNING更新为JOB_STATUS_STOPPED，代表停止job
        stat.compareAndSet(JOB_STATUS_RUNNING, JOB_STATUS_STOPPED);
    }

    private List<Proxy> getProxyList(List<Proxydata> list) {
        List<Proxy> resultList = new ArrayList<>();

        Proxy proxy = null;
        for(Proxydata proxyData : list) {
            proxy = new Proxy();
            proxy.setType(proxyData.getType());
            proxy.setIp(proxyData.getIp());
            proxy.setPort(proxyData.getPort());

            resultList.add(proxy);
        }

        return resultList;
    }

    private CopyOnWriteArrayList<Proxydata> getProxyDataList(List<Proxy> list) {
        CopyOnWriteArrayList<Proxydata> resultList = new CopyOnWriteArrayList<>();

        Proxydata proxyData = null;
        for(Proxy proxy : list) {
            proxyData = new Proxydata();
            proxyData.setType(proxy.getType());
            proxyData.setIp(proxy.getIp());
            proxyData.setPort(proxy.getPort());

            resultList.add(proxyData);
        }

        return resultList;
    }
}
