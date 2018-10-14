package com.xiu.crawling.douban;

import com.xiu.crawling.douban.core.BookThreadTask;

/**
 * author   xieqx
 * createTime  2018/10/14
 * desc 一句话描述该功能
 */
public class Test {

    public static void main(String[] args){
        BookThreadTask task = new BookThreadTask("文学","https://book.douban.com/tag/文学",null);
        Thread thead = new Thread(task);
        thead.start();
    }
}
