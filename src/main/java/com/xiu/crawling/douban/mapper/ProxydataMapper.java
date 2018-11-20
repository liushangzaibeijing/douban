package com.xiu.crawling.douban.mapper;

import com.xiu.crawling.douban.bean.Proxydata;
import com.xiu.crawling.douban.bean.ProxydataExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProxydataMapper {
    long countByExample(ProxydataExample example);

    int deleteByExample(ProxydataExample example);

    int insert(Proxydata record);

    int insertSelective(Proxydata record);

    List<Proxydata> selectByExample(ProxydataExample example);

    int updateByExampleSelective(@Param("record") Proxydata record, @Param("example") ProxydataExample example);

    int updateByExample(@Param("record") Proxydata record, @Param("example") ProxydataExample example);

    long canUserCounts();
}