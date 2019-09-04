/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : localhost:3306
 Source Schema         : utopia

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 04/09/2019 19:47:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for singer
-- ----------------------------
DROP TABLE IF EXISTS `singer`;
CREATE TABLE `singer`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `signer_mid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'qq音乐唯一标识',
  `full_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '歌手全名',
  `english_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '英文名',
  `short_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '歌手简称（别名 多个）',
  `contract_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '国籍',
  `city_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属城市',
  `area_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属区域',
  `profession` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职业',
  `sex` int(2) NULL DEFAULT NULL COMMENT '歌手性别',
  `members` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组合成员 （歌手id 多个以，分隔）',
  `school` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '歌手流派',
  `index` int(2) NULL DEFAULT NULL COMMENT '歌手首字母',
  `desc` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '简介',
  `nation` int(2) NULL DEFAULT NULL COMMENT '民族',
  `broker` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经纪公司',
  `strat_time` datetime(0) NULL DEFAULT NULL COMMENT '出道时间',
  `pic` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '歌手图片',
  `pic_local` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '歌手图片 本地存储',
  `heigth` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身高',
  `weigth` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '体重',
  `blood_type` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '血型',
  `graduate_school` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '毕业学校',
  `constellation` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '星座',
  `language` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '语言',
  `fans_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '粉丝名称',
  ` idol_color` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应援色',
  `speciality` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '特长',
  `major_achieve` int(11) NULL DEFAULT NULL COMMENT '主要成就',
  `art_course` int(11) NULL DEFAULT NULL COMMENT '从艺历程 (关联表)',
  `honorary_records` int(11) NULL DEFAULT NULL COMMENT '荣誉记录 (关联表)',
  `social_activities` int(11) NULL DEFAULT NULL COMMENT '社会活动 (关联表)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
