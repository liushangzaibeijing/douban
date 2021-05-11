package com.xiu.crawling.douban.mapper;

import com.xiu.crawling.douban.bean.BaiDuXueSuInfo;
import com.xiu.crawling.douban.bean.BaiDuXueSuInfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaiDuXueSuInfoMapper {
    int countByExample(BaiDuXueSuInfoExample example);

    int deleteByExample(BaiDuXueSuInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BaiDuXueSuInfo record);

    int insertSelective(BaiDuXueSuInfo record);

    List<BaiDuXueSuInfo> selectByExample(BaiDuXueSuInfoExample example);

    BaiDuXueSuInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BaiDuXueSuInfo record, @Param("example") BaiDuXueSuInfoExample example);

    int updateByExample(@Param("record") BaiDuXueSuInfo record, @Param("example") BaiDuXueSuInfoExample example);

    int updateByPrimaryKeySelective(BaiDuXueSuInfo record);

    int updateByPrimaryKey(BaiDuXueSuInfo record);
}