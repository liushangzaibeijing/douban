package com.xiu.crawling.douban.mapper;

import com.xiu.crawling.douban.bean.UrlInfo;
import com.xiu.crawling.douban.bean.UrlInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UrlInfoMapper {
    long countByExample(UrlInfoExample example);

    int deleteByExample(UrlInfoExample example);

    int insert(UrlInfo record);

    int insertSelective(UrlInfo record);

    List<UrlInfo> selectByExample(UrlInfoExample example);

    int updateByExampleSelective(@Param("record") UrlInfo record, @Param("example") UrlInfoExample example);

    int updateByExample(@Param("record") UrlInfo record, @Param("example") UrlInfoExample example);
}