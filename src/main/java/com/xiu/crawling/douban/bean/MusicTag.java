package com.xiu.crawling.douban.bean;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2019-09-03
 */
public class MusicTag {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 对应的key
     */
    private String type;

    /**
     * 标签
     */
    private String name;

    /**
     * 父级标签
     */
    private Integer parent_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }
}