package com.xiu.crawling.douban.common;


/**
 * 通道信息
 * Channel
 */
public enum SortEnum {

    /*
    recommend 热度 time 热度 rank 评论
     */
    RECOMMEND("recommend","热度"),
    TIME("time","热度"),
    RANK("rank","评论");


    private String code;
    private String name;

    SortEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static SortEnum fromCode(String code) {
        for (SortEnum type : SortEnum.values()) {
            if (type.getCode().equals(code) ){
                return type;
            }
        }
        return null;
    }

    public static SortEnum fromName(String name) {
        for (SortEnum type : SortEnum.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
