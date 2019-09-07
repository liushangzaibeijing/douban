package com.xiu.crawling.douban.bean;

/**
 * 歌手其他信息 荣誉记录 个人经历 感情生活等
 * 
 * @author wcyong
 * 
 * @date 2019-09-06
 */
public class SingerOther {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 歌手主键
     */
    private Integer singerId;

    /**
     * 社会活动、个人生活
     */
    private String type;

    /**
     * 具体的内容很大
     */
    private String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSingerId() {
        return singerId;
    }

    public void setSingerId(Integer singerId) {
        this.singerId = singerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }
}