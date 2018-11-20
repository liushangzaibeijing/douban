package com.xiu.crawling.douban.common;


/**
 * 电影标签中的所有
 * Channel
 */
public enum MarkEnum {

    TV_MOVIE(3,"电视剧/电影"),
    RANK(4,"分类排序榜"),
    TAG(5,"标签");

    private int code;
    private String name;

    MarkEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public static MarkEnum fromCode(int code) {
        for (MarkEnum type : MarkEnum.values()) {
            if (type.getCode()== code ){
                return type;
            }
        }
        return null;
    }

    public static MarkEnum fromName(String name) {
        for (MarkEnum type : MarkEnum.values()) {
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
