package com.xiu.crawling.douban.bean.vo;

import java.util.List;

/**
 * author   xieqx
 * createTime  2018/10/27
 * desc 一句话描述该功能
 */
public class Result {
    private String messsage;
    private Long code;
    private Object data;


    public String getMesssage() {
        return messsage;
    }

    public void setMesssage(String messsage) {
        this.messsage = messsage;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    public Result success(Object data){
        Result result = new Result();
        result.setCode(200L);
        result.setMesssage("获取信息成功");
        result.setData(data);
        return result;
    }

    public Result fail(String messsage){
        Result result = new Result();
        result.setCode(500L);
        result.setMesssage(messsage);
        return result;
    }
}
