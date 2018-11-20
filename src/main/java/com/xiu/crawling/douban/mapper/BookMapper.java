package com.xiu.crawling.douban.mapper;

import java.util.List;

import com.xiu.crawling.douban.bean.Book;
import com.xiu.crawling.douban.bean.BookExample;
import org.apache.ibatis.annotations.Param;

/**
 * 书籍mapper
 */
public interface BookMapper {
    long countByExample(BookExample example);

    int deleteByExample(BookExample example);

    int insert(Book record);

    int insertSelective(Book record);

    List<Book> selectByExample(BookExample example);

    int updateByExampleSelective(@Param("record") Book record, @Param("example") BookExample example);

    int updateByExample(@Param("record") Book record, @Param("example") BookExample example);
}