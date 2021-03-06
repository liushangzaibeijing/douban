package com.xiu.crawling.douban;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.xiu.crawling.douban.mapper")
//执行任务调度
@EnableScheduling
public class DoubanApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoubanApplication.class, args);
	}
}
