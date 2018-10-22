package com.xiu.crawling.douban.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * author   xieqx
 * createTime  2018/10/22
 * desc 一句话描述该功能
 */
@Slf4j
public class RandCookie {

    /**
     * 生成随机码值，包含数字、大小写字母
     * @author 郑明亮
     * @param number 生成的随机码位数
     * @return
     */
    public static String getRandomCode(int number){
        String codeNum = "";
        int [] code = new int[3];
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            int num = random.nextInt(10) + 48;
            int uppercase = random.nextInt(26) + 65;
            int lowercase = random.nextInt(26) + 97;
            code[0] = num;
            code[1] = uppercase;
            code[2] = lowercase;
            codeNum+=(char)code[random.nextInt(3)];
        }

        log.info("生成的11位数cookie：{}",codeNum);
        return codeNum;
    }

}
