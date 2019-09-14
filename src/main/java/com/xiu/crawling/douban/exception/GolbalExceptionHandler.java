package com.xiu.crawling.douban.exception;


import com.xiu.crawling.douban.cache.GlobalVariables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GolbalExceptionHandler {

     @Autowired
    GlobalVariables globalVariables;
    /**
     * @Author: gmy
     * @Description: 自定义异常捕获处理
     * @Date: 16:08 2018/5/30
     */
    @ExceptionHandler(value = CrawlingException.class)//MessageCenterException是自定义的一个异常
    public void messageCenterExceptionHandler(CrawlingException ex) {
        log.error("捕获到MessageCenterException异常", ex.getMessage());
        globalVariables.setSingerCrawlCurrent(ex.getCurrentPage(),ex.getType());
    }

    /**
     * @Author: gmy
     * @Description: 自定义异常捕获处理
     * @Date: 16:08 2018/5/30
     */
    @ExceptionHandler(value = Exception.class)//MessageCenterException是自定义的一个异常
    public void messageCenterExceptionHandler(Exception ex) {
        log.error("捕获到MessageCenterException异常", ex.getMessage());
    }
}

