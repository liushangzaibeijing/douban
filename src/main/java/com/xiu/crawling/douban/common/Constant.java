package com.xiu.crawling.douban.common;

/**
 * author   xieqx
 * createTime  2018/11/10
 * desc 常量
 */
public class Constant {

    /**
     * 电影标签
     */
    public final static String MOVIETAG = "https://movie.douban.com/j/search_tags?type="+MvTypeEnum.MOVIE.getCode();

    /**
     * 电视剧标签
     */
    public final static String TVTAG = "https://movie.douban.com/j/search_tags?type="+MvTypeEnum.TV.getCode();


    /**
     * 分类排行榜标签标签
     */
    public final static String CHART = "https://movie.douban.com/chart";


    /**
     * 排行榜获取 分类中获取
     */
    public final static String CHART_TOP = "https://movie.douban.com/j/chart/top_list";

    /**
     * 排行榜获取 数量
     */
    public final static String CHART_TOP_COUNT = "https://movie.douban.com/j/chart/top_list_count";


    /**
     * 排行榜获取 数量
     */
    public final static String TAG = "https://movie.douban.com/tag/";


    /**
     * 选择影视
     */
    public final static String SELECT_FILM = "https://movie.douban.com/j/new_search_subjects?sort=T&range=0,10&tags=";

    /**
     * 影视基础url
     */
    public final static String TV_BASE_URL = "https://movie.douban.com/j/search_subjects";


    /**
     * page
     */
    public final static Integer pageSize = 10;

}
