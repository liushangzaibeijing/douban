package com.xiu.crawling.douban.mapper;

import com.xiu.crawling.douban.bean.SingerOther;
import com.xiu.crawling.douban.bean.SingerOtherExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SingerOtherMapper {
    int countByExample(SingerOtherExample example);

    int deleteByExample(SingerOtherExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SingerOther record);

    int insertSelective(SingerOther record);

    List<SingerOther> selectByExample(SingerOtherExample example);

    SingerOther selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SingerOther record, @Param("example") SingerOtherExample example);

    int updateByExample(@Param("record") SingerOther record, @Param("example") SingerOtherExample example);

    int updateByPrimaryKeySelective(SingerOther record);

    int updateByPrimaryKey(SingerOther record);
}