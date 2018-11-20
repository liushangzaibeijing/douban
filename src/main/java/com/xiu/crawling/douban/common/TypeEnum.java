package com.xiu.crawling.douban.common;


/**
 * 通道信息
 * Channel
 */
public enum TypeEnum {

    BOOK(1,"书籍"),
    MOVIE(2,"电影"),
    TV(4,"电视剧"),
    MUSIC(3,"音乐");

    private int code;
    private String name;

    TypeEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public static TypeEnum fromCode(int code) {
        for (TypeEnum type : TypeEnum.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

    public static TypeEnum fromName(String name) {
        for (TypeEnum type : TypeEnum.values()) {
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
