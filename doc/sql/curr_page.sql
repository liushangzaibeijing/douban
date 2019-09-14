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

 Date: 14/09/2019 18:40:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for curr_page
-- ----------------------------
DROP TABLE IF EXISTS `curr_page`;
CREATE TABLE `curr_page`  (
  `id` int(11) NOT NULL COMMENT '主键',
  `curr_page` int(5) NULL DEFAULT NULL COMMENT '正在爬取的当前页',
  `type` int(1) NULL DEFAULT NULL COMMENT ' 1歌手 2专辑 3歌曲',
  `message` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误的详细信息 之截取500',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of curr_page
-- ----------------------------
INSERT INTO `curr_page` VALUES (0, 280, 1, '003yZzC83sctbn_Tone Spliff null');

SET FOREIGN_KEY_CHECKS = 1;
