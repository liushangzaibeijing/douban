package com.xiu.crawling.douban.bean;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MusicTagMapper {
    int countByExample(MusicTagExample example);

    int deleteByExample(MusicTagExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MusicTag record);

    int insertSelective(MusicTag record);

    List<MusicTag> selectByExample(MusicTagExample example);

    MusicTag selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MusicTag record, @Param("example") MusicTagExample example);

    int updateByExample(@Param("record") MusicTag record, @Param("example") MusicTagExample example);

    int updateByPrimaryKeySelective(MusicTag record);

    int updateByPrimaryKey(MusicTag record);
}