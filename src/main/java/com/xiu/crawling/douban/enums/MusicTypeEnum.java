package com.xiu.crawling.douban.enums;

public enum MusicTypeEnum {
     SINGER(1,"歌手"),
     ABLUM(2,"专辑"),
     SONG(3,"歌曲");

    MusicTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
    Integer code;
    String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }
}

