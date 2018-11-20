package com.xiu.crawling.douban.common;


/**
 * 电影类型
 * Channel
 */
public enum MvTypeEnum {

    TV("tv","电视剧"),
    MOVIE("movie","电影"),
    VARIETY("variety","综艺"),
    COMIC("comic","动漫"),
    DOCUMENTARY ("documentary ","纪录片"),
    SHORTFILM("movie","短片");

    private String code;
    private String name;

    MvTypeEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static MvTypeEnum fromCode(String code) {
        for (MvTypeEnum type : MvTypeEnum.values()) {
            if (type.getCode().equals(code) ){
                return type;
            }
        }
        return null;
    }

    public static MvTypeEnum fromName(String name) {
        for (MvTypeEnum type : MvTypeEnum.values()) {
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
