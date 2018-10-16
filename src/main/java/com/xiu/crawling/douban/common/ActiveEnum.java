package com.xiu.crawling.douban.common;


/**
 * 通道信息
 * Channel
 */
public enum ActiveEnum {

    ACTIVE(1,"有效"),
    NO_ACTIVE(0,"无效");

    private int code;
    private String name;

    ActiveEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public static ActiveEnum fromCode(int code) {
        for (ActiveEnum type : ActiveEnum.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

    public static ActiveEnum fromName(String name) {
        for (ActiveEnum type : ActiveEnum.values()) {
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
