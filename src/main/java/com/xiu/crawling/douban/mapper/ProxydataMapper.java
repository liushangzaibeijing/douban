package com.xiu.crawling.douban.mapper;

import com.xiu.crawling.douban.bean.Proxydata;
import com.xiu.crawling.douban.bean.ProxydataExample;
import java.util.List;
import java.util.Map;

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

    /**
     * 查询可用的没有缓存的代理对象列表
     * @param param
     * @return
     */
    List<Proxydata> selectProxyListNoCache(Map<String, Object> param);
}