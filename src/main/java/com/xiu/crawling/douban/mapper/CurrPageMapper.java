package com.xiu.crawling.douban.mapper;

import com.xiu.crawling.douban.bean.CurrPage;
import com.xiu.crawling.douban.bean.CurrPageExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CurrPageMapper {
    int countByExample(CurrPageExample example);

    int deleteByExample(CurrPageExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CurrPage record);

    int insertSelective(CurrPage record);

    List<CurrPage> selectByExample(CurrPageExample example);

    CurrPage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CurrPage record, @Param("example") CurrPageExample example);

    int updateByExample(@Param("record") CurrPage record, @Param("example") CurrPageExample example);

    int updateByPrimaryKeySelective(CurrPage record);

    int updateByPrimaryKey(CurrPage record);
}