package com.xiu.crawling.douban.core.service;

import com.xiu.crawling.douban.bean.ErrUrl;
import com.xiu.crawling.douban.mapper.ErrUrlMapper;
import com.xiu.crawling.douban.proxypool.job.ScheduleJobs;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author   xieqx
 * createTime  2018/11/14
 * desc 一句话描述该功能
 */
@Slf4j
public class AbstractThreadTask {
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;

    /**
     * 获取代理服务
     */
    public ProxyService proxyService;
    /**
     * 获取代理服务
     */
    public ErrUrlMapper errUrlMapper ;

    /**
     * 获取代理服务
     */
    public ScheduleJobs scheduleJobs;

    public AbstractThreadTask(ProxyService proxyService,ErrUrlMapper errUrlMapper,ScheduleJobs scheduleJobs){
        this.proxyService = proxyService;
        this.errUrlMapper = errUrlMapper;
        this.scheduleJobs = scheduleJobs;
    }

    /**
     * 获取字符中的浮点数
     * @param input 目标字符串
     * @return 浮点数信息
     */
    public Double getNumberByRegex(String input) {
        Double price = null;
        //使用正则匹配数字

        String regex = "(\\d+)(.?)(\\d*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(input);
        if(m.find()){
            //log.info("find {}", m.group(0));
            price = Double.parseDouble(m.group(0));
        }else{
            log.info("no find");
        }
        return price;
    }

    /**
     * 获取字符中的浮点数
     * @param input 目标字符串
     * @return 浮点数信息
     */
    public Integer getIntByRegex(String input) {
        Integer number = null;
        //使用正则匹配数字
        String regex = "(\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(input);
        if(m.find()){
            //log.info("find {}", m.group(0));
            number = Integer.parseInt(m.group(0));
        }else{
            log.info("no find");
        }
        return number;
    }


    /**
     * 获取字符中的浮点数
     * @param input 目标字符串
     * @return 浮点数信息
     */
    public Boolean isContainNumber(String input) {
        Integer number = null;
        //使用正则匹配数字
        String regex = "(\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(input);
        if(m.find()){
            return  true;
        }else{
            return false;
        }
    }


    public HttpHost checkProxy(String result, HttpHost proxy) {
        if (result.equals("403") || result.contains("<script>var d=[navigator.platform,navigator.userAgent,navigator.vendor].join(\"|\");")
                || result.contains("400 The plain HTTP request was sent to HTTPS port 400 Bad Request The plain HTTP request was sent to HTTPS port nginx")
                || result.contains("检测到有异常请求从你的 IP 发出")) {

            log.info("ip受到限制，重新换取ip");
            //先更新，再查询新的ip代理
            proxyService.updateProxy(false, proxy);
            //使用临时变量是为了让代理对象变为null;
            HttpHost newProxy = null;
            newProxy = proxyService.findCanUseProxy();

            if (newProxy == null) {
                log.info("代理ip 用完 提前启动爬取代理的任务");
                ScheduleJobs scheduleJobs = new ScheduleJobs();
                scheduleJobs.cronJob();
                //insertErrUrl(url, tagName, "代理ip 用完 暂时没有相关的解决办法");
                //break;
                newProxy = proxyService.findCanUseProxy();
            }

            return newProxy;
        }
        return proxy;
    }

    public void insertErrUrl(String url, String name, String message,String moduleName) {
        ErrUrl errUrl = new ErrUrl();
        errUrl.setModule(moduleName);
        errUrl.setErrorUrl(url);
        errUrl.setName(name);
        errUrl.setInfo(message);
        try {
            errUrlMapper.insert(errUrl);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static boolean isCharterAndNum(String str) {
        Pattern pattern = Pattern.compile("^[0-9a-zA-Z]*$");
        return pattern.matcher(str).matches();
    }

}
