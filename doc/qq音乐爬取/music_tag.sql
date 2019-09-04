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

 Date: 04/09/2019 19:47:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for music_tag
-- ----------------------------
DROP TABLE IF EXISTS `music_tag`;
CREATE TABLE `music_tag`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对应的key',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '父级标签',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of music_tag
-- ----------------------------
INSERT INTO `music_tag` VALUES (1, '0', '所属区域', NULL);
INSERT INTO `music_tag` VALUES (2, '-100', '全部', 1);
INSERT INTO `music_tag` VALUES (3, '200', '内地', 1);
INSERT INTO `music_tag` VALUES (4, '2', '港台', 1);
INSERT INTO `music_tag` VALUES (5, '5', '欧美', 1);
INSERT INTO `music_tag` VALUES (6, '4', '日本', 1);
INSERT INTO `music_tag` VALUES (7, '3', '韩国', 1);
INSERT INTO `music_tag` VALUES (8, '6', '其他', 1);
INSERT INTO `music_tag` VALUES (9, '0', '性别', NULL);
INSERT INTO `music_tag` VALUES (10, '-100', '全部', 9);
INSERT INTO `music_tag` VALUES (11, '1', '流行', 9);
INSERT INTO `music_tag` VALUES (12, '6', '嘻哈', 9);
INSERT INTO `music_tag` VALUES (13, '2', '摇滚', 9);
INSERT INTO `music_tag` VALUES (14, '4', '电子', 9);
INSERT INTO `music_tag` VALUES (15, '3', '民谣', 9);
INSERT INTO `music_tag` VALUES (16, '8', 'R&B', 9);
INSERT INTO `music_tag` VALUES (17, '10', '民歌', 9);
INSERT INTO `music_tag` VALUES (18, '9', '轻音乐', 9);
INSERT INTO `music_tag` VALUES (19, '5', '爵士', 9);
INSERT INTO `music_tag` VALUES (20, '14', '古典', 9);
INSERT INTO `music_tag` VALUES (21, '25', '乡村', 9);
INSERT INTO `music_tag` VALUES (22, '20', '蓝调', 9);
INSERT INTO `music_tag` VALUES (23, '0', '歌手姓名首字母', NULL);
INSERT INTO `music_tag` VALUES (24, '-100', '热门', 23);
INSERT INTO `music_tag` VALUES (25, '1', 'A', 23);
INSERT INTO `music_tag` VALUES (26, '2', 'B', 23);
INSERT INTO `music_tag` VALUES (27, '3', 'C', 23);
INSERT INTO `music_tag` VALUES (28, '4', 'D', 23);
INSERT INTO `music_tag` VALUES (29, '5', 'E', 23);
INSERT INTO `music_tag` VALUES (30, '6', 'F', 23);
INSERT INTO `music_tag` VALUES (31, '7', 'G', 23);
INSERT INTO `music_tag` VALUES (32, '8', 'H', 23);
INSERT INTO `music_tag` VALUES (33, '9', 'I', 23);
INSERT INTO `music_tag` VALUES (34, '10', 'J', 23);
INSERT INTO `music_tag` VALUES (35, '11', 'K', 23);
INSERT INTO `music_tag` VALUES (36, '12', 'L', 23);
INSERT INTO `music_tag` VALUES (37, '13', 'M', 23);
INSERT INTO `music_tag` VALUES (38, '14', 'N', 23);
INSERT INTO `music_tag` VALUES (39, '15', 'O', 23);
INSERT INTO `music_tag` VALUES (40, '16', 'P', 23);
INSERT INTO `music_tag` VALUES (41, '17', 'Q', 23);
INSERT INTO `music_tag` VALUES (42, '18', 'R', 23);
INSERT INTO `music_tag` VALUES (43, '19', 'S', 23);
INSERT INTO `music_tag` VALUES (44, '20', 'T', 23);
INSERT INTO `music_tag` VALUES (45, '21', 'U', 23);
INSERT INTO `music_tag` VALUES (46, '22', 'V', 23);
INSERT INTO `music_tag` VALUES (47, '23', 'W', 23);
INSERT INTO `music_tag` VALUES (48, '24', 'X', 23);
INSERT INTO `music_tag` VALUES (49, '25', 'Y', 23);
INSERT INTO `music_tag` VALUES (50, '26', 'Z', 23);
INSERT INTO `music_tag` VALUES (51, '27', '#', 23);

SET FOREIGN_KEY_CHECKS = 1;
