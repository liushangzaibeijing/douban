package com.xiu.crawling.douban.mapper;

import com.xiu.crawling.douban.bean.ErrTempurl;
import com.xiu.crawling.douban.bean.ErrTempurlExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ErrTempurlMapper {
    long countByExample(ErrTempurlExample example);

    int deleteByExample(ErrTempurlExample example);

    int insert(ErrTempurl record);

    int insertSelective(ErrTempurl record);

    List<ErrTempurl> selectByExample(ErrTempurlExample example);

    int updateByExampleSelective(@Param("record") ErrTempurl record, @Param("example") ErrTempurlExample example);

    int updateByExample(@Param("record") ErrTempurl record, @Param("example") ErrTempurlExample example);
}