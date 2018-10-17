package com.xiu.crawling.douban.mapper;

import com.xiu.crawling.douban.bean.ErrUrl;
import com.xiu.crawling.douban.bean.ErrUrlExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ErrUrlMapper {
    long countByExample(ErrUrlExample example);

    int deleteByExample(ErrUrlExample example);

    int insert(ErrUrl record);

    int insertSelective(ErrUrl record);

    List<ErrUrl> selectByExample(ErrUrlExample example);

    int updateByExampleSelective(@Param("record") ErrUrl record, @Param("example") ErrUrlExample example);

    int updateByExample(@Param("record") ErrUrl record, @Param("example") ErrUrlExample example);
}