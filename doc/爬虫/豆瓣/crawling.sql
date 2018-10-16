/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : crawling

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-10-16 21:40:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(150) NOT NULL COMMENT '书籍名称',
  `author` varchar(100) NOT NULL COMMENT '作者',
  `translator` varchar(50) DEFAULT NULL COMMENT '译者',
  `publis_house` varchar(100) DEFAULT NULL COMMENT '出版社',
  `publication_date` varchar(12) DEFAULT NULL COMMENT '出版日期',
  `price` double(5,2) DEFAULT NULL,
  `score` double(5,1) DEFAULT NULL COMMENT '评分，保留一位小数',
  `evaluate_number` int(11) DEFAULT NULL COMMENT '评价人数',
  `picture` varchar(255) DEFAULT NULL COMMENT '图片路径',
  `descption` varchar(500) DEFAULT NULL COMMENT '描述',
  `tag` varchar(20) DEFAULT NULL COMMENT '书籍所属标签',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8 COMMENT='豆瓣书籍信息';

-- ----------------------------
-- Table structure for url_info
-- ----------------------------
DROP TABLE IF EXISTS `url_info`;
CREATE TABLE `url_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `url` varchar(255) NOT NULL COMMENT '爬取的url地址',
  `active` int(1) DEFAULT '0' COMMENT '爬取的url是否有效，0无效（已经爬取过，或者不需要爬取） 1 有效 可以进行爬取',
  `descption` varchar(255) DEFAULT NULL COMMENT '爬取的url的描述',
  `label` int(1) DEFAULT NULL COMMENT '层级 （0 是第一层，1是第二层）',
  `mark` int(5) DEFAULT NULL COMMENT '对于该url的标识 ，以后可能会有多个补充标识，和label唯一确定这个url的含义\r\n\r\n1 书籍 2音乐 3电影 ....待补充',
  PRIMARY KEY (`id`),
  UNIQUE KEY `url_index` (`url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=utf8 COMMENT='需要爬取的url地址';
