package com.xiu.crawling.douban.enums;

/**
 * 歌手信息的转换
 */
public enum ConvertMusicSignerEnum {
    FULL_NAME("中文名","fullName"),
    ENGLISG_NAME("外文名","englishName"),
    SHORT_NAME("别名","shortName"),
    CONTRACT("国籍","contractId"),
    PROFESSION("职业","profession"),
    SEX("歌手性别","sex"),
    MEMBERS("组合成员","members"),
    SCHOOL("歌手流派","school"),
    NATIVE_PLACE("籍贯","nativePlace"),
    BTIRTH_TIME("出生日期","birthTime"),
    NATION("民族","nation"),
    BROKER("经纪公司","broker"),
    START_TIME("出道时间","stratTime"),
    PIC_LOCAL("歌手图片 本地存储","picLocal"),
    PIC("歌手图片","pic"),
    HEIGTH("身高","heigth"),
    WEIGTH("体重","weigth"),
    HOBBY("爱好","hobby"),
    BLOOD_TYPE("血型","bloodType"),
    GRADUATE_SCHOOL("毕业院校","graduateSchool"),
    CONSTELLATION("星座","constellation"),
    LANGUAGE("语言","language"),
    FANS_NAME("粉丝名称","fansName"),
    IDOL_COLOR("应援色","idolColor"),
    SPECIALITY("特长","speciality"),
    //自己计算
    BIRTH_PLACE("出生地","birthPlace"),
    //自己计算
    INDEX("首字母","index"),
    //主要成就
    MAJOR_ACHIEVE("主要成就","majorAchieve"),
    //从艺历程
    ART_COURSE("从艺历程","artCourse"),

    //荣誉记录
    HONORARY_RECORDS("荣誉记录","honoraryRecords"),
    //社会活动
    SOCIAL_ACTIVITIES("社会活动","socialActivities");


    String key;
    String prop;

    ConvertMusicSignerEnum(String key, String prop) {
        this.key = key;
        this.prop = prop;
    }

    //根据code 获得描述
    public static String convertProp(String key){
        for(ConvertMusicSignerEnum signPlatform: ConvertMusicSignerEnum.values()){
            if(key.equals(signPlatform.key)){
                return signPlatform.prop;
            }
        }
        return null;
    }

    //根据名称获取 code
    public static String convertKey(String prop){
        for(ConvertMusicSignerEnum signPlatform: ConvertMusicSignerEnum.values()){
            if(prop.equals(signPlatform.prop)){
                return signPlatform.key;
            }
        }
        return  null;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
