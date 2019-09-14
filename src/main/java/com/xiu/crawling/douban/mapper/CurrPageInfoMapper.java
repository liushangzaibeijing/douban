package com.xiu.crawling.douban.mapper;

import com.xiu.crawling.douban.bean.CurrPageInfo;
import com.xiu.crawling.douban.bean.CurrPageInfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CurrPageInfoMapper {
    int countByExample(CurrPageInfoExample example);

    int deleteByExample(CurrPageInfoExample example);

    int insert(CurrPageInfo record);

    int insertSelective(CurrPageInfo record);

    List<CurrPageInfo> selectByExample(CurrPageInfoExample example);

    int updateByExampleSelective(@Param("record") CurrPageInfo record, @Param("example") CurrPageInfoExample example);

    int updateByExample(@Param("record") CurrPageInfo record, @Param("example") CurrPageInfoExample example);
}