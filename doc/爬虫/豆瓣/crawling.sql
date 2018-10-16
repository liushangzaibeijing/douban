/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : crawling

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-10-13 17:16:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(150) NOT NULL COMMENT '书籍名称',
  `author` varchar(50) NOT NULL COMMENT '作者',
  `translator` varchar(50) DEFAULT NULL COMMENT '译者',
  `publis_house` varchar(100) NOT NULL COMMENT '出版社',
  `publication_date` varchar(12)  COMMENT '出版日期',
  `price` double(5,2) NOT NULL,
  `score` double(5,1) DEFAULT NULL COMMENT '评分，保留一位小数',
  `evaluate_number` int(11) DEFAULT NULL COMMENT '评价人数',
  `picture` varchar(255) DEFAULT NULL COMMENT '图片路径',
  `descption` varchar(500) DEFAULT NULL COMMENT '描述',
  `tag` varchar(20) DEFAULT NULL COMMENT '书籍所属标签',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='豆瓣书籍信息';

-- ----------------------------
-- Records of book
-- ----------------------------

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
) ENGINE=InnoDB AUTO_INCREMENT=478 DEFAULT CHARSET=utf8 COMMENT='需要爬取的url地址';

-- ----------------------------
-- Records of url_info
-- ----------------------------
INSERT INTO `url_info` VALUES ('1', 'https://www.douban.com/group/explore?tag=动漫', '0', '动漫', null, null);
INSERT INTO `url_info` VALUES ('2', 'https://www.douban.com/group/explore?tag=塔罗', '0', '塔罗', null, null);
INSERT INTO `url_info` VALUES ('3', 'https://www.douban.com/group/362263/', '0', '对象太他妈难找了党-单身凸^-^凸', null, null);
INSERT INTO `url_info` VALUES ('4', 'https://music.douban.com/programme/1332482', '0', '', null, null);
INSERT INTO `url_info` VALUES ('5', 'https://movie.douban.com/subject/30140571/', '0', '嗝嗝老师', null, null);
INSERT INTO `url_info` VALUES ('6', 'https://movie.douban.com/tag/励志', '0', '励志', null, null);
INSERT INTO `url_info` VALUES ('7', 'https://www.douban.com/update/topic/%E6%88%91%E9%80%89%E6%97%A9%E9%A4%90?dcm=douban&dcs=anonymous-home-topic', '0', '我的豆瓣收藏夹里有什么', null, null);
INSERT INTO `url_info` VALUES ('8', 'https://movie.douban.com/subject/26794854/cinema/', '0', '选座购票', null, null);
INSERT INTO `url_info` VALUES ('9', 'https://book.douban.com/tag/营销', '0', '营销', null, null);
INSERT INTO `url_info` VALUES ('10', 'https://read.douban.com/ebooks/', '0', '更多', null, null);
INSERT INTO `url_info` VALUES ('11', 'https://movie.douban.com/subject/26725678/', '0', '解除好友2：暗网', null, null);
INSERT INTO `url_info` VALUES ('12', 'https://www.douban.com/partner/', '0', '豆瓣广告', null, null);
INSERT INTO `url_info` VALUES ('13', 'https://www.douban.com/location/', '0', '更多', null, null);
INSERT INTO `url_info` VALUES ('14', 'https://www.douban.com/gallery/topic/27292/?from=hot_topic_anony_sns', '0', '当我们谈论环境保护时', null, null);
INSERT INTO `url_info` VALUES ('15', 'https://video.douban.com/column/10018/', '0', '瓣嘴  · · · · · ·', null, null);
INSERT INTO `url_info` VALUES ('16', 'https://read.douban.com/ebook/53171779', '0', '到床上去', null, null);
INSERT INTO `url_info` VALUES ('17', 'https://m.douban.com/page/np9j55o?dt_time_source=douban-web_anonymous', '0', '用文字掌控工作——21天职场写作特训营', null, null);
INSERT INTO `url_info` VALUES ('18', 'https://movie.douban.com/tag/黑色幽默', '0', '黑色幽默', null, null);
INSERT INTO `url_info` VALUES ('19', 'https://www.douban.com/group/explore?tag=传媒', '0', '传媒', null, null);
INSERT INTO `url_info` VALUES ('20', 'https://www.douban.com/group/402725/', '0', '记事本圆梦小组', null, null);
INSERT INTO `url_info` VALUES ('21', 'https://www.douban.com/group/explore?tag=心理学', '0', '心理学', null, null);
INSERT INTO `url_info` VALUES ('22', 'https://book.douban.com/tag/漫画', '0', '漫画', null, null);
INSERT INTO `url_info` VALUES ('23', 'https://book.douban.com/tag/企业史', '0', '企业史', null, null);
INSERT INTO `url_info` VALUES ('24', 'https://music.douban.com/subject/30274394/', '0', '画外音', null, null);
INSERT INTO `url_info` VALUES ('25', 'https://www.douban.com/event/30794723/', '0', '真的爱你—致敬BEYOND·黄家驹25周年纪念演唱会', null, null);
INSERT INTO `url_info` VALUES ('26', 'https://music.douban.com/programmes/', '0', '更多', null, null);
INSERT INTO `url_info` VALUES ('27', 'https://help.douban.com/?app=main', '0', '帮助中心', null, null);
INSERT INTO `url_info` VALUES ('28', 'https://www.douban.com/group/shafake/', '0', '沙发客', null, null);
INSERT INTO `url_info` VALUES ('29', 'https://www.douban.com/group/83759/', '0', '我们就是要做衣服给自己穿', null, null);
INSERT INTO `url_info` VALUES ('30', 'https://book.douban.com/tag/?view=type#经管', '0', '更多', null, null);
INSERT INTO `url_info` VALUES ('31', 'https://book.douban.com/tag/推理', '0', '推理', null, null);
INSERT INTO `url_info` VALUES ('32', 'https://www.douban.com/group/explore?tag=摄影', '0', '摄影', null, null);
INSERT INTO `url_info` VALUES ('33', 'https://www.douban.com/group/explore?tag=品牌', '0', '品牌', null, null);
INSERT INTO `url_info` VALUES ('34', 'https://www.douban.com/group/explore?tag=设计', '0', '设计', null, null);
INSERT INTO `url_info` VALUES ('35', 'https://movie.douban.com/tag/', '0', '分类', null, null);
INSERT INTO `url_info` VALUES ('36', 'https://book.douban.com/tag/', '0', '分类浏览', null, null);
INSERT INTO `url_info` VALUES ('37', 'https://www.douban.com/group/explore?tag=建筑', '0', '建筑', null, null);
INSERT INTO `url_info` VALUES ('38', 'https://book.douban.com/tag/交互设计', '0', '交互设计', null, null);
INSERT INTO `url_info` VALUES ('39', 'https://music.douban.com/subject/30312719/', '0', '换句话说', null, null);
INSERT INTO `url_info` VALUES ('40', 'https://www.douban.com/event/31263949/', '0', '【福利】2018“ONE NIGHT 给小孩”公益演唱会', null, null);
INSERT INTO `url_info` VALUES ('41', 'https://www.douban.com/note/625092950/', '0', '“我结婚前一天，我爸抽搐地在我奶奶家哭了好久，不说话，就是哭” | 父亲的50个脆弱瞬间', null, null);
INSERT INTO `url_info` VALUES ('42', 'https://movie.douban.com/tag/战争', '0', '战争', null, null);
INSERT INTO `url_info` VALUES ('43', 'https://book.douban.com/tag/家居', '0', '家居', null, null);
INSERT INTO `url_info` VALUES ('44', 'https://music.douban.com/programme/31530993', '0', '', null, null);
INSERT INTO `url_info` VALUES ('45', 'https://www.douban.com/group/explore?tag=社会', '0', '社会»', null, null);
INSERT INTO `url_info` VALUES ('46', 'https://book.douban.com/tag/绘本', '0', '绘本', null, null);
INSERT INTO `url_info` VALUES ('47', 'https://book.douban.com/tag/?view=type#文学', '0', '更多', null, null);
INSERT INTO `url_info` VALUES ('48', 'https://www.douban.com/event/30853704/', '0', '2018 山姆·史密斯“痛快感受”北京演唱会', null, null);
INSERT INTO `url_info` VALUES ('49', 'https://www.douban.com/gallery/topic/26973/?from=hot_topic_anony_sns', '0', '2018年金马奖竞争力分析', null, null);
INSERT INTO `url_info` VALUES ('50', 'https://www.douban.com/location/beijing/events/week-1408', '0', '美食', null, null);
INSERT INTO `url_info` VALUES ('51', 'https://www.douban.com/location/beijing/events/week-film', '0', '电影»', null, null);
INSERT INTO `url_info` VALUES ('52', 'https://www.douban.com/location/beijing/events/week-1409', '0', '派对', null, null);
INSERT INTO `url_info` VALUES ('53', 'https://www.douban.com/group/explore?tag=恋爱', '0', '恋爱', null, null);
INSERT INTO `url_info` VALUES ('54', 'https://book.douban.com/tag/?view=type#科技', '0', '更多', null, null);
INSERT INTO `url_info` VALUES ('55', 'https://movie.douban.com/subject/30201003/', '0', '寄宿学校', null, null);
INSERT INTO `url_info` VALUES ('56', 'https://www.douban.com/group/explore?tag=励志', '0', '励志', null, null);
INSERT INTO `url_info` VALUES ('57', 'https://www.douban.com/photos/album/1623281100/', '0', '吃货日记《美味博物馆》', null, null);
INSERT INTO `url_info` VALUES ('58', 'https://movie.douban.com/tag/惊悚', '0', '惊悚', null, null);
INSERT INTO `url_info` VALUES ('59', 'https://m.douban.com/time/column/111?dt_time_source=douban-web_anonymous', '0', '黑泽明与杨德昌——焦雄屏电影大师手册', null, null);
INSERT INTO `url_info` VALUES ('60', 'https://book.douban.com/tag/小说', '0', '小说', null, null);
INSERT INTO `url_info` VALUES ('61', 'https://www.douban.com/location/beijing/events/week-1401', '0', '生活', null, null);
INSERT INTO `url_info` VALUES ('62', 'https://music.douban.com/subject/30319056/', '0', 'WARNING', null, null);
INSERT INTO `url_info` VALUES ('63', 'https://www.douban.com/location/beijing/events/week-1402', '0', '集市', null, null);
INSERT INTO `url_info` VALUES ('64', 'https://market.douban.com/campaign/notebook?dcm=douban&dcs=anonymous-home-spu', '0', '布面精装本', null, null);
INSERT INTO `url_info` VALUES ('65', 'https://www.douban.com/location/beijing/events/week-1403', '0', '摄影', null, null);
INSERT INTO `url_info` VALUES ('66', 'https://www.douban.com/location/beijing/events/week-1404', '0', '外语', null, null);
INSERT INTO `url_info` VALUES ('67', 'https://video.douban.com/column/10008/', '0', '观影会客厅  · · · · · ·', null, null);
INSERT INTO `url_info` VALUES ('68', 'https://movie.douban.com/subject/27140071/cinema/', '0', '选座购票', null, null);
INSERT INTO `url_info` VALUES ('69', 'https://www.douban.com/location/beijing/events/week-1405', '0', '桌游', null, null);
INSERT INTO `url_info` VALUES ('70', 'https://www.douban.com/location/beijing/events/week-1406', '0', '夜店', null, null);
INSERT INTO `url_info` VALUES ('71', 'https://movie.douban.com/subject/27072795/', '0', '幸福的拉扎罗', null, null);
INSERT INTO `url_info` VALUES ('72', 'https://www.douban.com/location/beijing/events/week-1407', '0', '交友', null, null);
INSERT INTO `url_info` VALUES ('73', 'https://www.douban.com/group/explore?tag=化妆', '0', '化妆', null, null);
INSERT INTO `url_info` VALUES ('74', 'https://m.douban.com/page/3z5hv0n?dt_time_source=douban-web_anonymous', '0', '30天掌握讲故事的技能——写作成长营第4期', null, null);
INSERT INTO `url_info` VALUES ('75', 'https://music.douban.com', '0', '音乐', null, null);
INSERT INTO `url_info` VALUES ('76', 'https://movie.douban.com/subject/27092785/', '0', '李茶的姑妈', null, null);
INSERT INTO `url_info` VALUES ('77', 'https://book.douban.com/tag/经济学', '0', '经济学', null, null);
INSERT INTO `url_info` VALUES ('78', 'https://music.douban.com/artists/royalty/', '0', '金羊毛计划', null, null);
INSERT INTO `url_info` VALUES ('79', 'https://market.douban.com?dcs=anonymous-home-more-skus&dcm=douban', '0', '更多', null, null);
INSERT INTO `url_info` VALUES ('80', 'https://movie.douban.com/tag/青春', '0', '青春', null, null);
INSERT INTO `url_info` VALUES ('81', 'https://movie.douban.com/subject/26683421/', '0', '特工', null, null);
INSERT INTO `url_info` VALUES ('82', 'https://book.douban.com/tag/编程', '0', '编程', null, null);
INSERT INTO `url_info` VALUES ('83', 'https://www.douban.com/group/explore?tag=二手', '0', '二手', null, null);
INSERT INTO `url_info` VALUES ('84', 'https://music.douban.com/subject/30271277/', '0', 'TRENCH', null, null);
INSERT INTO `url_info` VALUES ('85', 'https://market.douban.com/campaign/collection?dcm=douban&dcs=anonymous-home-spu', '0', '豆瓣收藏夹', null, null);
INSERT INTO `url_info` VALUES ('86', 'https://book.douban.com/tag/设计', '0', '设计', null, null);
INSERT INTO `url_info` VALUES ('87', 'https://book.douban.com/tag/港台', '0', '港台', null, null);
INSERT INTO `url_info` VALUES ('88', 'https://movie.douban.com/tag/美国', '0', '美国', null, null);
INSERT INTO `url_info` VALUES ('89', 'https://www.douban.com/group/explore?tag=心情', '0', '心情', null, null);
INSERT INTO `url_info` VALUES ('90', 'https://read.douban.com/reader/ebook/57218487/', '0', '免费试读', null, null);
INSERT INTO `url_info` VALUES ('91', 'https://movie.douban.com/subject/27092785/cinema/', '0', '选座购票', null, null);
INSERT INTO `url_info` VALUES ('92', 'https://www.douban.com/group/explore?tag=怪癖', '0', '怪癖', null, null);
INSERT INTO `url_info` VALUES ('93', 'https://music.douban.com/programme/659925', '0', '', null, null);
INSERT INTO `url_info` VALUES ('94', 'https://www.douban.com/group/explore?tag=桌游', '0', '桌游', null, null);
INSERT INTO `url_info` VALUES ('95', 'https://www.douban.com/photos/album/127582785/', '0', '武汉的樱花', null, null);
INSERT INTO `url_info` VALUES ('96', 'https://www.douban.com/location/drama/', '0', '舞台剧', null, null);
INSERT INTO `url_info` VALUES ('97', 'https://www.douban.com/group/explore?tag=软件', '0', '软件', null, null);
INSERT INTO `url_info` VALUES ('98', 'https://book.douban.com/tag/互联网', '0', '互联网', null, null);
INSERT INTO `url_info` VALUES ('99', 'https://www.douban.com/about?policy=disclaimer', '0', '免责声明', null, null);
INSERT INTO `url_info` VALUES ('100', 'https://movie.douban.com/subject/26425063/cinema/', '0', '选座购票', null, null);
INSERT INTO `url_info` VALUES ('101', 'https://site.douban.com/blakeyoung/', '0', 'BlakeYoung', null, null);
INSERT INTO `url_info` VALUES ('102', 'https://book.douban.com/tag/童话', '0', '童话', null, null);
INSERT INTO `url_info` VALUES ('103', 'https://www.douban.com/group/explore?tag=八卦', '0', '八卦', null, null);
INSERT INTO `url_info` VALUES ('104', 'https://movie.douban.com/tag/剧情', '0', '剧情', null, null);
INSERT INTO `url_info` VALUES ('105', 'https://book.douban.com/tag/商业', '0', '商业', null, null);
INSERT INTO `url_info` VALUES ('106', 'https://music.douban.com/subject/30336706/', '0', 'Tha Carter V', null, null);
INSERT INTO `url_info` VALUES ('107', 'https://www.douban.com/doubanapp/app?channel=nimingye', '0', '下载豆瓣 App', null, null);
INSERT INTO `url_info` VALUES ('108', 'https://www.douban.com/group/explore?tag=创业', '0', '创业', null, null);
INSERT INTO `url_info` VALUES ('109', 'https://book.douban.com/tag/建筑', '0', '建筑', null, null);
INSERT INTO `url_info` VALUES ('110', 'https://movie.douban.com/tag/恐怖', '0', '恐怖', null, null);
INSERT INTO `url_info` VALUES ('111', 'https://www.douban.com/accounts/connect/wechat/?from=douban-web-anony-home', '0', '微信登录', null, null);
INSERT INTO `url_info` VALUES ('112', 'https://movie.douban.com/chart', '0', '更多', null, null);
INSERT INTO `url_info` VALUES ('113', 'https://www.douban.com/location/beijing/events/week-party', '0', '聚会»', null, null);
INSERT INTO `url_info` VALUES ('114', 'https://movie.douban.com/subject/27003543/', '0', '与神同行2：因与缘', null, null);
INSERT INTO `url_info` VALUES ('115', 'https://book.douban.com/review/best/', '0', '书评', null, null);
INSERT INTO `url_info` VALUES ('116', 'https://www.douban.com/group/explore?tag=健康', '0', '健康', null, null);
INSERT INTO `url_info` VALUES ('117', 'https://book.douban.com/tag/名著', '0', '名著', null, null);
INSERT INTO `url_info` VALUES ('118', 'https://movie.douban.com/tag/文艺', '0', '文艺', null, null);
INSERT INTO `url_info` VALUES ('119', 'https://read.douban.com/reader/ebook/48080876/', '0', '免费试读', null, null);
INSERT INTO `url_info` VALUES ('120', 'https://www.douban.com/note/673644060/', '0', '在我最自暴自弃的时候，是音乐给了我出口', null, null);
INSERT INTO `url_info` VALUES ('121', 'https://www.douban.com/group/explore?tag=手工', '0', '手工', null, null);
INSERT INTO `url_info` VALUES ('122', 'https://www.douban.com/group/explore?tag=情感', '0', '情感»', null, null);
INSERT INTO `url_info` VALUES ('123', 'https://www.douban.com/group/explore?tag=旅行', '0', '旅行', null, null);
INSERT INTO `url_info` VALUES ('124', 'https://m.douban.com/time/column/100?dt_time_source=douban-web_anonymous', '0', '花知道答案——中日名师插花课', null, null);
INSERT INTO `url_info` VALUES ('125', 'https://www.douban.com/explore/', '0', '更多', null, null);
INSERT INTO `url_info` VALUES ('126', 'https://book.douban.com/tag/理财', '0', '理财', null, null);
INSERT INTO `url_info` VALUES ('127', 'https://movie.douban.com/showtimes/', '0', '更多', null, null);
INSERT INTO `url_info` VALUES ('128', 'https://www.douban.com/group/explore?tag=留学', '0', '留学', null, null);
INSERT INTO `url_info` VALUES ('129', 'https://book.douban.com/tag/金融', '0', '金融', null, null);
INSERT INTO `url_info` VALUES ('130', 'https://movie.douban.com/tag/日本', '0', '日本', null, null);
INSERT INTO `url_info` VALUES ('131', 'https://www.douban.com/group/explore?tag=发泄', '0', '发泄', null, null);
INSERT INTO `url_info` VALUES ('132', 'https://music.douban.com/programme/1302579', '0', '', null, null);
INSERT INTO `url_info` VALUES ('133', 'https://movie.douban.com/subject/26794854/', '0', '帝企鹅日记2：...', null, null);
INSERT INTO `url_info` VALUES ('134', 'https://m.douban.com/time/column/76?dt_time_source=douban-web_anonymous', '0', '52倍人生——戴锦华大师电影课', null, null);
INSERT INTO `url_info` VALUES ('135', 'https://movie.douban.com/subject/25881500/', '0', '玛雅蜜蜂历险记...', null, null);
INSERT INTO `url_info` VALUES ('136', 'https://movie.douban.com', '0', '电影', null, null);
INSERT INTO `url_info` VALUES ('137', 'https://market.douban.com/campaign/calendar2018?dcm=douban&dcs=anonymous-home-spu', '0', '豆瓣电影日历2018', null, null);
INSERT INTO `url_info` VALUES ('138', 'https://www.douban.com/group/explore?tag=美食', '0', '美食', null, null);
INSERT INTO `url_info` VALUES ('139', 'https://www.douban.com/group/explore?tag=纹身', '0', '纹身', null, null);
INSERT INTO `url_info` VALUES ('140', 'https://www.douban.com/note/628510374/', '0', '日本七夕：从传说到现代', null, null);
INSERT INTO `url_info` VALUES ('141', 'https://book.douban.com', '0', '读书', null, null);
INSERT INTO `url_info` VALUES ('142', 'https://book.douban.com/tag/日本文学', '0', '日本文学', null, null);
INSERT INTO `url_info` VALUES ('143', 'https://www.douban.com/group/explore?tag=淘宝', '0', '淘宝', null, null);
INSERT INTO `url_info` VALUES ('144', 'https://market.douban.com/campaign/canvasbag?dcm=douban&dcs=anonymous-home-spu', '0', '帆布包·早餐系列', null, null);
INSERT INTO `url_info` VALUES ('145', 'https://www.douban.com/group/explore?tag=育儿', '0', '育儿', null, null);
INSERT INTO `url_info` VALUES ('146', 'https://www.douban.com/group/explore?tag=戏曲', '0', '戏曲', null, null);
INSERT INTO `url_info` VALUES ('147', 'https://www.douban.com/about', '0', '关于豆瓣', null, null);
INSERT INTO `url_info` VALUES ('148', 'https://www.douban.com/location/beijing/events/week-salon', '0', '讲座', null, null);
INSERT INTO `url_info` VALUES ('149', 'https://m.douban.com/time/column/83?dt_time_source=douban-web_anonymous', '0', '哲学闪耀时——不一样的西方哲学史', null, null);
INSERT INTO `url_info` VALUES ('150', 'https://read.douban.com/ebook/56368197', '0', '你活在人间，便...', null, null);
INSERT INTO `url_info` VALUES ('151', 'https://www.douban.com/group/explore?tag=宠物', '0', '宠物', null, null);
INSERT INTO `url_info` VALUES ('152', 'https://site.douban.com/zhuxin/', '0', '朱子芥', null, null);
INSERT INTO `url_info` VALUES ('153', 'https://www.douban.com/group/topic/109121959?dcm=douban&dcs=anonymous-home-group', '0', '【组规】emmmm...这是豆瓣豆品官方小组组规', null, null);
INSERT INTO `url_info` VALUES ('154', 'https://m.douban.com/time/column/98?dt_time_source=douban-web_anonymous', '0', '美剧大爆炸——创作、脑洞与文化密码', null, null);
INSERT INTO `url_info` VALUES ('155', 'https://book.douban.com/subject/30270219/', '0', '恶的科学', null, null);
INSERT INTO `url_info` VALUES ('156', 'https://movie.douban.com/subject/26649627/cinema/', '0', '选座购票', null, null);
INSERT INTO `url_info` VALUES ('157', 'https://movie.douban.com/tag/动画', '0', '动画', null, null);
INSERT INTO `url_info` VALUES ('158', 'https://www.douban.com/group/explore?tag=音乐', '0', '音乐', null, null);
INSERT INTO `url_info` VALUES ('159', 'https://www.douban.com/gallery/topic/27427/?from=hot_topic_anony_sns', '0', '给你看我的手帐吧', null, null);
INSERT INTO `url_info` VALUES ('160', 'https://www.douban.com/group/explore?tag=哲学', '0', '哲学', null, null);
INSERT INTO `url_info` VALUES ('161', 'https://site.douban.com/kulu/', '0', '梁晓雪', null, null);
INSERT INTO `url_info` VALUES ('162', 'https://movie.douban.com/nowplaying/', '0', '影讯&购票', null, null);
INSERT INTO `url_info` VALUES ('163', 'https://www.douban.com/group/explore?tag=文学', '0', '文学', null, null);
INSERT INTO `url_info` VALUES ('164', 'https://movie.douban.com/subject/26649627/', '0', '超能泰坦', null, null);
INSERT INTO `url_info` VALUES ('165', 'https://movie.douban.com/subject/27621727/', '0', '遗传厄运', null, null);
INSERT INTO `url_info` VALUES ('166', 'https://book.douban.com/tag/算法', '0', '算法', null, null);
INSERT INTO `url_info` VALUES ('167', 'https://movie.douban.com/subject/27140071/', '0', '找到你', null, null);
INSERT INTO `url_info` VALUES ('168', 'https://www.douban.com/group/explore?tag=笑话', '0', '笑话', null, null);
INSERT INTO `url_info` VALUES ('169', 'https://www.douban.com/note/624450483/', '0', '在这里，爸爸变成了离开水的鱼', null, null);
INSERT INTO `url_info` VALUES ('170', 'https://www.douban.com/group/explore?tag=LES', '0', 'LES', null, null);
INSERT INTO `url_info` VALUES ('171', 'https://music.douban.com/review/latest/', '0', '乐评', null, null);
INSERT INTO `url_info` VALUES ('172', 'https://www.douban.com/accounts/connect/sina_weibo/?from=douban-web-anony-home', '0', '微博登录', null, null);
INSERT INTO `url_info` VALUES ('173', 'https://book.douban.com/tag/?view=type#文化', '0', '更多', null, null);
INSERT INTO `url_info` VALUES ('174', 'https://movie.douban.com/tag/中国大陆', '0', '中国大陆', null, null);
INSERT INTO `url_info` VALUES ('175', 'https://www.douban.com/group/explore?tag=游戏', '0', '游戏', null, null);
INSERT INTO `url_info` VALUES ('176', 'https://www.douban.com/location/beijing/events/week-travel', '0', '旅行', null, null);
INSERT INTO `url_info` VALUES ('177', 'https://www.douban.com/group/explore?tag=运动', '0', '运动', null, null);
INSERT INTO `url_info` VALUES ('178', 'https://movie.douban.com/explore', '0', '选电影', null, null);
INSERT INTO `url_info` VALUES ('179', 'https://movie.douban.com/tag/动作', '0', '动作', null, null);
INSERT INTO `url_info` VALUES ('180', 'https://www.douban.com/group/explore?tag=文具', '0', '文具', null, null);
INSERT INTO `url_info` VALUES ('181', 'https://www.douban.com/group/explore?tag=购物', '0', '购物»', null, null);
INSERT INTO `url_info` VALUES ('182', 'https://book.douban.com/tag/奇幻', '0', '奇幻', null, null);
INSERT INTO `url_info` VALUES ('183', 'https://video.douban.com?dt_time_source=douban-web_anonymous', '0', '视频', null, null);
INSERT INTO `url_info` VALUES ('184', 'https://movie.douban.com/subject/26411377/', '0', '你的婚礼', null, null);
INSERT INTO `url_info` VALUES ('185', 'https://book.douban.com/tag/灵修', '0', '灵修', null, null);
INSERT INTO `url_info` VALUES ('186', 'https://movie.douban.com/tag/香港', '0', '香港', null, null);
INSERT INTO `url_info` VALUES ('187', 'https://www.douban.com/group/explore?tag=公益', '0', '公益', null, null);
INSERT INTO `url_info` VALUES ('188', 'https://book.douban.com/writers/', '0', '作者', null, null);
INSERT INTO `url_info` VALUES ('189', 'https://www.douban.com/group/407193/', '0', '帮帮组™加入这个组就有人帮你了！', null, null);
INSERT INTO `url_info` VALUES ('190', 'https://artist.douban.com/abilu/', '0', '阿比鹿音乐奖', null, null);
INSERT INTO `url_info` VALUES ('191', 'https://www.douban.com/group/explore?tag=自然', '0', '自然', null, null);
INSERT INTO `url_info` VALUES ('192', 'https://www.douban.com/group/explore?tag=护肤', '0', '护肤', null, null);
INSERT INTO `url_info` VALUES ('193', 'https://music.douban.com/programme/267112', '0', '', null, null);
INSERT INTO `url_info` VALUES ('194', 'https://www.douban.com/group/explore?tag=团购', '0', '团购', null, null);
INSERT INTO `url_info` VALUES ('195', 'https://book.douban.com/tag/散文', '0', '散文', null, null);
INSERT INTO `url_info` VALUES ('196', 'https://www.douban.com/event/31269143/', '0', '南征北战2018北京演唱会', null, null);
INSERT INTO `url_info` VALUES ('197', 'https://movie.douban.com/tag/喜剧', '0', '喜剧', null, null);
INSERT INTO `url_info` VALUES ('198', 'https://www.douban.com/about?topic=licence', '0', '0110418', null, null);
INSERT INTO `url_info` VALUES ('199', 'https://www.douban.com/group/explore?tag=学术', '0', '学术»', null, null);
INSERT INTO `url_info` VALUES ('200', 'https://book.douban.com/tag/管理', '0', '管理', null, null);
INSERT INTO `url_info` VALUES ('201', 'https://www.douban.com/group/explore?tag=汽车', '0', '汽车', null, null);
INSERT INTO `url_info` VALUES ('202', 'https://www.douban.com/note/680758842/', '0', '别去羡慕那些大作家，他们曾经和你一样不想动笔', null, null);
INSERT INTO `url_info` VALUES ('203', 'https://movie.douban.com/subject/26925317/', '0', '动物世界', null, null);
INSERT INTO `url_info` VALUES ('204', 'https://www.douban.com/group/explore?tag=影视', '0', '影视', null, null);
INSERT INTO `url_info` VALUES ('205', 'https://book.douban.com/tag/职场', '0', '职场', null, null);
INSERT INTO `url_info` VALUES ('206', 'https://www.douban.com/group/topic/120464785?dcm=douban&dcs=anonymous-home-group', '0', '「豆瓣市集」升级为「豆瓣豆品」', null, null);
INSERT INTO `url_info` VALUES ('207', 'https://www.douban.com/photos/album/153740994/', '0', '对生活热爱如昨。', null, null);
INSERT INTO `url_info` VALUES ('208', 'https://book.douban.com/tag/教育', '0', '教育', null, null);
INSERT INTO `url_info` VALUES ('209', 'https://www.douban.com/group/', '0', '小组', null, null);
INSERT INTO `url_info` VALUES ('210', 'https://book.douban.com/tag/随笔', '0', '随笔', null, null);
INSERT INTO `url_info` VALUES ('211', 'https://movie.douban.com/tag/悬疑', '0', '悬疑', null, null);
INSERT INTO `url_info` VALUES ('212', 'https://www.douban.com/group/explore?tag=GAY', '0', 'GAY', null, null);
INSERT INTO `url_info` VALUES ('213', 'https://www.douban.com/photos/album/69354869/', '0', '以此纪念汶川大地震', null, null);
INSERT INTO `url_info` VALUES ('214', 'https://www.douban.com/group/explore?tag=宗教', '0', '宗教', null, null);
INSERT INTO `url_info` VALUES ('215', 'https://movie.douban.com/tv/', '0', '电视剧', null, null);
INSERT INTO `url_info` VALUES ('216', 'https://movie.douban.com/tag/中国', '0', '中国', null, null);
INSERT INTO `url_info` VALUES ('217', 'https://www.douban.com/group/explore?tag=兴趣', '0', '兴趣»', null, null);
INSERT INTO `url_info` VALUES ('218', 'https://www.douban.com/note/628477228/', '0', '【手绘游记】滇西南之行-享受宁静的夏日时光', null, null);
INSERT INTO `url_info` VALUES ('219', 'https://book.douban.com/latest', '0', '更多', null, null);
INSERT INTO `url_info` VALUES ('220', 'https://www.douban.com/group/explore?tag=闲聊', '0', '闲聊»', null, null);
INSERT INTO `url_info` VALUES ('221', 'https://book.douban.com/tag/诗歌', '0', '诗歌', null, null);
INSERT INTO `url_info` VALUES ('222', 'https://www.douban.com/group/explore?tag=数码', '0', '数码', null, null);
INSERT INTO `url_info` VALUES ('223', 'https://www.douban.com/group/echofans/', '0', '我爱三毛', null, null);
INSERT INTO `url_info` VALUES ('224', 'https://movie.douban.com/tag/?view=type', '0', '更多', null, null);
INSERT INTO `url_info` VALUES ('225', 'https://music.douban.com#new1', '0', '更多', null, null);
INSERT INTO `url_info` VALUES ('226', 'https://site.douban.com/liwangnian/', '0', '黎忘年', null, null);
INSERT INTO `url_info` VALUES ('227', 'https://www.douban.com/group/explore?tag=出国', '0', '出国', null, null);
INSERT INTO `url_info` VALUES ('228', 'https://www.douban.com/location/beijing/events/week-exhibition', '0', '展览', null, null);
INSERT INTO `url_info` VALUES ('229', 'https://book.douban.com/tag/言情', '0', '言情', null, null);
INSERT INTO `url_info` VALUES ('230', 'https://artist.douban.com/app', '0', '豆瓣音乐人', null, null);
INSERT INTO `url_info` VALUES ('231', 'https://book.douban.com/tag/科普', '0', '科普', null, null);
INSERT INTO `url_info` VALUES ('232', 'https://www.douban.com/group/explore?tag=租房', '0', '租房', null, null);
INSERT INTO `url_info` VALUES ('233', 'https://www.douban.com/location/beijing/events/week-1004', '0', '音乐节', null, null);
INSERT INTO `url_info` VALUES ('234', 'https://book.douban.com/tag/音乐', '0', '音乐', null, null);
INSERT INTO `url_info` VALUES ('235', 'https://www.douban.com/group/explore?tag=求职', '0', '求职', null, null);
INSERT INTO `url_info` VALUES ('236', 'https://www.douban.com/location/beijing/events/week-1801', '0', '主题放映', null, null);
INSERT INTO `url_info` VALUES ('237', 'https://read.douban.com/ebook/48080876', '0', '飞鸟集', null, null);
INSERT INTO `url_info` VALUES ('238', 'https://www.douban.com/location/beijing/events/week-1802', '0', '影展', null, null);
INSERT INTO `url_info` VALUES ('239', 'https://www.douban.com/location/beijing/events/week-1803', '0', '影院活动', null, null);
INSERT INTO `url_info` VALUES ('240', 'https://www.douban.com/doubanapp/', '0', '移动应用', null, null);
INSERT INTO `url_info` VALUES ('241', 'https://www.douban.com/about?topic=contactus', '0', '联系我们', null, null);
INSERT INTO `url_info` VALUES ('242', 'https://img3.doubanio.com/f/shire/80d71f876c40a3ecdfde2fe2afe3b1983a2cac64/pics/licence/publication2018.png', '0', '新出发京批字第直160029号', null, null);
INSERT INTO `url_info` VALUES ('243', 'https://www.douban.com/group/cuthair/', '0', '自己给自己剪头发', null, null);
INSERT INTO `url_info` VALUES ('244', 'https://www.douban.com/group/explore?tag=考试', '0', '考试', null, null);
INSERT INTO `url_info` VALUES ('245', 'https://book.douban.com/tag/哲学', '0', '哲学', null, null);
INSERT INTO `url_info` VALUES ('246', 'https://www.douban.com/group/170177/', '0', '这辈子一定要做几件疯狂的事', null, null);
INSERT INTO `url_info` VALUES ('247', 'https://music.douban.com/topics/', '0', '专题', null, null);
INSERT INTO `url_info` VALUES ('248', 'https://www.douban.com/group/explore?tag=直播', '0', '直播', null, null);
INSERT INTO `url_info` VALUES ('249', 'https://www.douban.com/group/liucixin/', '0', '刘慈欣', null, null);
INSERT INTO `url_info` VALUES ('250', 'https://movie.douban.com/subject/4864908/cinema/', '0', '选座购票', null, null);
INSERT INTO `url_info` VALUES ('251', 'https://movie.douban.com/tag/德国', '0', '德国', null, null);
INSERT INTO `url_info` VALUES ('252', 'https://www.douban.com/gallery/topic/24401/?from=hot_topic_anony_sns', '0', '你最喜欢的关于月亮的诗词歌赋', null, null);
INSERT INTO `url_info` VALUES ('253', 'https://movie.douban.com/tag/英国', '0', '英国', null, null);
INSERT INTO `url_info` VALUES ('254', 'https://movie.douban.com/subject/30140571/cinema/', '0', '选座购票', null, null);
INSERT INTO `url_info` VALUES ('255', 'https://www.douban.com/location/beijing/events/week-sports', '0', '运动', null, null);
INSERT INTO `url_info` VALUES ('256', 'https://www.douban.com/location/beijing/events/week-music', '0', '音乐»', null, null);
INSERT INTO `url_info` VALUES ('257', 'https://site.douban.com/rc/', '0', '养鸡', null, null);
INSERT INTO `url_info` VALUES ('258', 'https://www.douban.com/group/explore?tag=星座', '0', '星座', null, null);
INSERT INTO `url_info` VALUES ('259', 'https://www.douban.com/wetware/', '0', '潮潮豆瓣音乐周', null, null);
INSERT INTO `url_info` VALUES ('260', 'https://read.douban.com/reader/ebook/57541373/', '0', '免费试读', null, null);
INSERT INTO `url_info` VALUES ('261', 'https://www.douban.com/group/explore?tag=社科', '0', '社科', null, null);
INSERT INTO `url_info` VALUES ('262', 'https://book.douban.com/tag/传记', '0', '传记', null, null);
INSERT INTO `url_info` VALUES ('263', 'https://m.douban.com/time/column/85?dt_time_source=douban-web_anonymous', '0', '黑镜人生——网络生活的传播学肖像', null, null);
INSERT INTO `url_info` VALUES ('264', 'https://movie.douban.com/tag/韩国', '0', '韩国', null, null);
INSERT INTO `url_info` VALUES ('265', 'https://movie.douban.com/trailers', '0', '预告片', null, null);
INSERT INTO `url_info` VALUES ('266', 'https://book.douban.com/tag/?view=type#生活', '0', '更多', null, null);
INSERT INTO `url_info` VALUES ('267', 'https://movie.douban.com/tag/科幻', '0', '科幻', null, null);
INSERT INTO `url_info` VALUES ('268', 'https://music.douban.com/programme/17543587', '0', '', null, null);
INSERT INTO `url_info` VALUES ('269', 'https://book.douban.com/tag/武侠', '0', '武侠', null, null);
INSERT INTO `url_info` VALUES ('270', 'https://book.douban.com/tag/通信', '0', '通信', null, null);
INSERT INTO `url_info` VALUES ('271', 'https://www.douban.com/group/explore?tag=服饰', '0', '服饰', null, null);
INSERT INTO `url_info` VALUES ('272', 'https://www.douban.com/location/beijing/events/week-1103', '0', '舞剧', null, null);
INSERT INTO `url_info` VALUES ('273', 'https://www.douban.com', '0', '豆瓣', null, null);
INSERT INTO `url_info` VALUES ('274', 'https://read.douban.com/reader/ebook/57630114/', '0', '免费试读', null, null);
INSERT INTO `url_info` VALUES ('275', 'https://www.douban.com/location/beijing/events/week-1104', '0', '歌剧', null, null);
INSERT INTO `url_info` VALUES ('276', 'https://www.douban.com/group/explore?tag=国学', '0', '国学', null, null);
INSERT INTO `url_info` VALUES ('277', 'https://www.douban.com/location/beijing/events/week-1105', '0', '戏曲', null, null);
INSERT INTO `url_info` VALUES ('278', 'https://www.douban.com/location/beijing/events/week-1106', '0', '其他', null, null);
INSERT INTO `url_info` VALUES ('279', 'https://music.douban.com/subject/30339920/', '0', 'NEW KIDS : THE FINAL', null, null);
INSERT INTO `url_info` VALUES ('280', 'https://www.douban.com/group/explore?tag=硬件', '0', '硬件', null, null);
INSERT INTO `url_info` VALUES ('281', 'https://movie.douban.com/tag/法国', '0', '法国', null, null);
INSERT INTO `url_info` VALUES ('282', 'https://www.douban.com/hnypt/variformcyst.py', '0', '', null, null);
INSERT INTO `url_info` VALUES ('283', 'https://www.douban.com/group/explore?tag=人文', '0', '人文', null, null);
INSERT INTO `url_info` VALUES ('284', 'https://music.douban.com/subject/30335559/', '0', '你說之後會找我', null, null);
INSERT INTO `url_info` VALUES ('285', 'https://book.douban.com/cart', '0', '购书单', null, null);
INSERT INTO `url_info` VALUES ('286', 'https://movie.douban.com/askmatrix/hot_questions/all', '0', '问答', null, null);
INSERT INTO `url_info` VALUES ('287', 'https://m.douban.com/time/column/99?dt_time_source=douban-web_anonymous', '0', '古典音乐的奇幻之旅——从入门到上瘾的108堂课', null, null);
INSERT INTO `url_info` VALUES ('288', 'https://www.douban.com/group/20618/', '0', '西双版纳', null, null);
INSERT INTO `url_info` VALUES ('289', 'https://book.douban.com/tag/健康', '0', '健康', null, null);
INSERT INTO `url_info` VALUES ('290', 'https://www.douban.com/jobs', '0', '在豆瓣工作', null, null);
INSERT INTO `url_info` VALUES ('291', 'https://www.douban.com/group/explore?tag=生活', '0', '生活»', null, null);
INSERT INTO `url_info` VALUES ('292', 'https://www.douban.com/group/explore?tag=艺术', '0', '艺术»', null, null);
INSERT INTO `url_info` VALUES ('293', 'https://m.douban.com/time/column/101?dt_time_source=douban-web_anonymous', '0', '花鸟鱼虫的生活意见——博物君的自然笔记', null, null);
INSERT INTO `url_info` VALUES ('294', 'https://movie.douban.com/review/best/', '0', '影评', null, null);
INSERT INTO `url_info` VALUES ('295', 'https://read.douban.com?dcn=entry&dcs=book-nav&dcm=douban', '0', '阅读', null, null);
INSERT INTO `url_info` VALUES ('296', 'https://www.douban.com/group/dashan/', '0', '搭讪学', null, null);
INSERT INTO `url_info` VALUES ('297', 'https://book.douban.com/subject/30288491/', '0', '海面之下', null, null);
INSERT INTO `url_info` VALUES ('298', 'https://www.douban.com/gallery/topic/27479/?from=hot_topic_anony_sns', '0', '有哪些值得关注的新生代演员？', null, null);
INSERT INTO `url_info` VALUES ('299', 'https://www.douban.com/note/625255572/', '0', '少时并不知，跟父亲自在相处的时光，并非我们想象的无边无际', null, null);
INSERT INTO `url_info` VALUES ('300', 'https://www.douban.com/note/673111446/', '0', '韩松落：《阳光灿烂的日子》和《教父》里，为什么都有它出现？', null, null);
INSERT INTO `url_info` VALUES ('301', 'https://book.douban.com/tag/电影', '0', '电影', null, null);
INSERT INTO `url_info` VALUES ('302', 'https://www.douban.com/location/beijing/events/week-commonweal', '0', '公益', null, null);
INSERT INTO `url_info` VALUES ('303', 'https://book.douban.com/tag/股票', '0', '股票', null, null);
INSERT INTO `url_info` VALUES ('304', 'https://book.douban.com/subject/30295861/', '0', '算法霸权', null, null);
INSERT INTO `url_info` VALUES ('305', 'https://book.douban.com/tag/?view=type#流行', '0', '更多', null, null);
INSERT INTO `url_info` VALUES ('306', 'https://www.douban.com/gallery/topic/22450/?from=hot_topic_anony_sns', '0', '你很长时间都走不出来的一部电影', null, null);
INSERT INTO `url_info` VALUES ('307', 'https://beijing.douban.com/hosts', '0', '主办方', null, null);
INSERT INTO `url_info` VALUES ('308', 'https://read.douban.com/reader/ebook/53745549/', '0', '免费试读', null, null);
INSERT INTO `url_info` VALUES ('309', 'https://market.douban.com?dcs=anonymous-home-sidenav&dcm=douban', '0', '豆品', null, null);
INSERT INTO `url_info` VALUES ('310', 'https://www.douban.com/location/beijing/events/week-1001', '0', '小型现场', null, null);
INSERT INTO `url_info` VALUES ('311', 'https://www.douban.com/location/beijing/events/week-all', '0', '其他»', null, null);
INSERT INTO `url_info` VALUES ('312', 'https://book.douban.com/tag/科幻', '0', '科幻', null, null);
INSERT INTO `url_info` VALUES ('313', 'https://www.douban.com/location/beijing/events/week-1002', '0', '音乐会', null, null);
INSERT INTO `url_info` VALUES ('314', 'https://www.douban.com/group/explore?tag=展览', '0', '展览', null, null);
INSERT INTO `url_info` VALUES ('315', 'https://www.douban.com/group/explore?tag=雕塑', '0', '雕塑', null, null);
INSERT INTO `url_info` VALUES ('316', 'https://www.douban.com/location/beijing/events/week-1003', '0', '演唱会', null, null);
INSERT INTO `url_info` VALUES ('317', 'https://www.douban.com/note/691557420/', '0', '来自僧侣的声音', null, null);
INSERT INTO `url_info` VALUES ('318', 'https://movie.douban.com/tag/纪录片', '0', '纪录片', null, null);
INSERT INTO `url_info` VALUES ('319', 'https://book.douban.com/tag/回忆录', '0', '回忆录', null, null);
INSERT INTO `url_info` VALUES ('320', 'https://movie.douban.com/subject/26425063/', '0', '无双', null, null);
INSERT INTO `url_info` VALUES ('321', 'https://www.douban.com/accounts/resetpassword', '0', '帮助', null, null);
INSERT INTO `url_info` VALUES ('322', 'https://www.douban.com/group/explore?tag=互联网', '0', '互联网', null, null);
INSERT INTO `url_info` VALUES ('323', 'https://music.douban.com/tag/', '0', '分类浏览', null, null);
INSERT INTO `url_info` VALUES ('324', 'https://movie.douban.com/subject/25882296/', '0', '狄仁杰之四大天王', null, null);
INSERT INTO `url_info` VALUES ('325', 'https://read.douban.com/reader/ebook/53171779/', '0', '免费试读', null, null);
INSERT INTO `url_info` VALUES ('326', 'https://book.douban.com/tag/神经网络', '0', '神经网络', null, null);
INSERT INTO `url_info` VALUES ('327', 'https://movie.douban.com/tag/经典', '0', '经典', null, null);
INSERT INTO `url_info` VALUES ('328', 'https://read.douban.com/ebook/53745549', '0', '罪恶的枷锁 Ⅰ...', null, null);
INSERT INTO `url_info` VALUES ('329', 'https://read.douban.com/reader/ebook/56368197/', '0', '免费试读', null, null);
INSERT INTO `url_info` VALUES ('330', 'https://movie.douban.com/tag/台湾', '0', '台湾', null, null);
INSERT INTO `url_info` VALUES ('331', 'https://market.douban.com?utm_campaign=anonymous_top_nav&utm_source=douban&utm_medium=pc_web', '0', '豆瓣豆品', null, null);
INSERT INTO `url_info` VALUES ('332', 'https://www.douban.com/group/explore?tag=曲艺', '0', '曲艺', null, null);
INSERT INTO `url_info` VALUES ('333', 'https://book.douban.com/tag/励志', '0', '励志', null, null);
INSERT INTO `url_info` VALUES ('334', 'https://movie.douban.com/tag/犯罪', '0', '犯罪', null, null);
INSERT INTO `url_info` VALUES ('335', 'https://www.douban.com/group/explore?tag=家庭', '0', '家庭', null, null);
INSERT INTO `url_info` VALUES ('336', 'https://book.douban.com/tag/历史', '0', '历史', null, null);
INSERT INTO `url_info` VALUES ('337', 'https://www.douban.com/location/beijing/events/week-drama', '0', '戏剧»', null, null);
INSERT INTO `url_info` VALUES ('338', 'https://www.douban.com/group/topic/123170768?dcm=douban&dcs=anonymous-home-group', '0', '2019豆瓣电影日历即将上线，参与转发活动得新品豆瓣日历', null, null);
INSERT INTO `url_info` VALUES ('339', 'https://beijing.douban.com/events', '0', '近期活动', null, null);
INSERT INTO `url_info` VALUES ('340', 'https://movie.douban.com/subject/25881500/cinema/', '0', '选座购票', null, null);
INSERT INTO `url_info` VALUES ('341', 'https://www.douban.com/group/explore?tag=语言', '0', '语言', null, null);
INSERT INTO `url_info` VALUES ('342', 'https://movie.douban.com/subject/4864908/', '0', '影', null, null);
INSERT INTO `url_info` VALUES ('343', 'https://read.douban.com/app/', '0', '豆瓣阅读', null, null);
INSERT INTO `url_info` VALUES ('344', 'https://time.douban.com?dt_time_source=douban-web_anonymous', '0', '更多', null, null);
INSERT INTO `url_info` VALUES ('345', 'https://book.douban.com/tag/?view=type', '0', '更多', null, null);
INSERT INTO `url_info` VALUES ('346', 'https://music.douban.com/chart', '0', '排行榜', null, null);
INSERT INTO `url_info` VALUES ('347', 'https://www.douban.com/note/625170352/', '0', '爸，节日快乐', null, null);
INSERT INTO `url_info` VALUES ('348', 'https://www.douban.com/group/explore?tag=舞蹈', '0', '舞蹈', null, null);
INSERT INTO `url_info` VALUES ('349', 'https://www.douban.com/group/explore?tag=美容', '0', '美容', null, null);
INSERT INTO `url_info` VALUES ('350', 'https://book.douban.com/tag/旅行', '0', '旅行', null, null);
INSERT INTO `url_info` VALUES ('351', 'https://www.douban.com/group/explore?tag=吐槽', '0', '吐槽', null, null);
INSERT INTO `url_info` VALUES ('352', 'https://book.douban.com/tag/青春', '0', '青春', null, null);
INSERT INTO `url_info` VALUES ('353', 'https://book.douban.com/tag/美食', '0', '美食', null, null);
INSERT INTO `url_info` VALUES ('354', 'https://music.douban.com/artists/', '0', '更多', null, null);
INSERT INTO `url_info` VALUES ('355', 'https://music.douban.com/subject/30318520/', '0', '绽放的绽放的绽放', null, null);
INSERT INTO `url_info` VALUES ('356', 'https://movie.douban.com/subject/30146756/', '0', '风语咒', null, null);
INSERT INTO `url_info` VALUES ('357', 'https://www.douban.com/location/beijing/events/week-1101', '0', '话剧', null, null);
INSERT INTO `url_info` VALUES ('358', 'https://www.douban.com/group/588598?dcs=anonymous-home-more-shops&dcm=douban#hot-shop-wrapper', '0', '更多', null, null);
INSERT INTO `url_info` VALUES ('359', 'https://www.douban.com/location/beijing/events/week-1102', '0', '音乐剧', null, null);
INSERT INTO `url_info` VALUES ('360', 'https://movie.douban.com/tag/爱情', '0', '爱情', null, null);
INSERT INTO `url_info` VALUES ('361', 'https://movie.douban.com/tag/搞笑', '0', '搞笑', null, null);
INSERT INTO `url_info` VALUES ('362', 'https://www.douban.com/group/explore?tag=理财', '0', '理财', null, null);
INSERT INTO `url_info` VALUES ('363', 'https://time.douban.com/?dt_time_source=douban-web_anonymous_index_top_nav', '0', '豆瓣时间', null, null);
INSERT INTO `url_info` VALUES ('364', 'https://www.douban.com/group/explore', '0', '更多', null, null);
INSERT INTO `url_info` VALUES ('366', 'https://book.douban.com/tag/张小娴', '1', '张小娴', null, null);
INSERT INTO `url_info` VALUES ('367', 'https://book.douban.com/tag/中国历史', '1', '中国历史', null, null);
INSERT INTO `url_info` VALUES ('368', 'https://book.douban.com/tag/策划', '1', '策划', null, null);
INSERT INTO `url_info` VALUES ('369', 'https://book.douban.com/tag/经典', '1', '经典', null, null);
INSERT INTO `url_info` VALUES ('370', 'https://book.douban.com/tag/两性', '1', '两性', null, null);
INSERT INTO `url_info` VALUES ('371', 'https://book.douban.com/tag/杂文', '1', '杂文', null, null);
INSERT INTO `url_info` VALUES ('372', 'https://book.douban.com/tag/外国文学', '1', '外国文学', null, null);
INSERT INTO `url_info` VALUES ('373', 'https://book.douban.com/tag/日本漫画', '1', '日本漫画', null, null);
INSERT INTO `url_info` VALUES ('374', 'https://book.douban.com/tag/艺术史', '1', '艺术史', null, null);
INSERT INTO `url_info` VALUES ('375', 'https://book.douban.com/tag/交互', '1', '交互', null, null);
INSERT INTO `url_info` VALUES ('376', 'https://book.douban.com/tag/米兰·昆德拉', '1', '米兰·昆德拉', null, null);
INSERT INTO `url_info` VALUES ('377', 'https://book.douban.com/tag/文化', '1', '文化', null, null);
INSERT INTO `url_info` VALUES ('378', 'https://book.douban.com/tag/成长', '1', '成长', null, null);
INSERT INTO `url_info` VALUES ('379', 'https://book.douban.com/tag/青春文学', '1', '青春文学', null, null);
INSERT INTO `url_info` VALUES ('380', 'https://book.douban.com/tag/游记', '1', '游记', null, null);
INSERT INTO `url_info` VALUES ('381', 'https://book.douban.com/tag/心理学', '1', '心理学', null, null);
INSERT INTO `url_info` VALUES ('382', 'https://book.douban.com/tag/思想', '1', '思想', null, null);
INSERT INTO `url_info` VALUES ('383', 'https://book.douban.com/tag/鲁迅', '1', '鲁迅', null, null);
INSERT INTO `url_info` VALUES ('384', 'https://book.douban.com/tag/美术', '1', '美术', null, null);
INSERT INTO `url_info` VALUES ('385', 'https://book.douban.com/tag/政治', '1', '政治', null, null);
INSERT INTO `url_info` VALUES ('386', 'https://book.douban.com/tag/杜拉斯', '1', '杜拉斯', null, null);
INSERT INTO `url_info` VALUES ('387', 'https://book.douban.com/tag/经济', '1', '经济', null, null);
INSERT INTO `url_info` VALUES ('388', 'https://book.douban.com/tag/创业', '1', '创业', null, null);
INSERT INTO `url_info` VALUES ('389', 'https://book.douban.com/tag/网络小说', '1', '网络小说', null, null);
INSERT INTO `url_info` VALUES ('390', 'https://book.douban.com/tag/佛教', '1', '佛教', null, null);
INSERT INTO `url_info` VALUES ('391', 'https://book.douban.com/tag/悬疑', '1', '悬疑', null, null);
INSERT INTO `url_info` VALUES ('392', 'https://book.douban.com/tag/人际关系', '1', '人际关系', null, null);
INSERT INTO `url_info` VALUES ('393', 'https://book.douban.com/tag/绘画', '1', '绘画', null, null);
INSERT INTO `url_info` VALUES ('394', 'https://book.douban.com/tag/东野圭吾', '1', '东野圭吾', null, null);
INSERT INTO `url_info` VALUES ('395', 'https://book.douban.com/tag/郭敬明', '1', '郭敬明', null, null);
INSERT INTO `url_info` VALUES ('396', 'https://book.douban.com/tag/投资', '1', '投资', null, null);
INSERT INTO `url_info` VALUES ('397', 'https://book.douban.com/tag/军事', '1', '军事', null, null);
INSERT INTO `url_info` VALUES ('398', 'https://book.douban.com/tag/茨威格', '1', '茨威格', null, null);
INSERT INTO `url_info` VALUES ('399', 'https://book.douban.com/tag/儿童文学', '1', '儿童文学', null, null);
INSERT INTO `url_info` VALUES ('400', 'https://book.douban.com/tag/UCD', '1', 'UCD', null, null);
INSERT INTO `url_info` VALUES ('401', 'https://book.douban.com/tag/文学', '1', '文学', null, null);
INSERT INTO `url_info` VALUES ('402', 'https://book.douban.com/tag/摄影', '1', '摄影', null, null);
INSERT INTO `url_info` VALUES ('403', 'https://book.douban.com/tag/广告', '1', '广告', null, null);
INSERT INTO `url_info` VALUES ('404', 'https://book.douban.com/tag/古典文学', '1', '古典文学', null, null);
INSERT INTO `url_info` VALUES ('405', 'https://book.douban.com/tag/张悦然', '1', '张悦然', null, null);
INSERT INTO `url_info` VALUES ('406', 'https://book.douban.com/tag/幾米', '1', '幾米', null, null);
INSERT INTO `url_info` VALUES ('407', 'https://book.douban.com/tag/J.K.罗琳', '1', 'J.K.罗琳', null, null);
INSERT INTO `url_info` VALUES ('408', 'https://book.douban.com/tag/科学', '1', '科学', null, null);
INSERT INTO `url_info` VALUES ('409', 'https://book.douban.com/tag/爱情', '1', '爱情', null, null);
INSERT INTO `url_info` VALUES ('410', 'https://book.douban.com/tag/当代文学', '1', '当代文学', null, null);
INSERT INTO `url_info` VALUES ('411', 'https://book.douban.com/tag/落落', '1', '落落', null, null);
INSERT INTO `url_info` VALUES ('412', 'https://book.douban.com/tag/UE', '1', 'UE', null, null);
INSERT INTO `url_info` VALUES ('413', 'https://book.douban.com/tag/社会', '1', '社会', null, null);
INSERT INTO `url_info` VALUES ('414', 'https://book.douban.com/tag/魔幻', '1', '魔幻', null, null);
INSERT INTO `url_info` VALUES ('415', 'https://book.douban.com/tag/宗教', '1', '宗教', null, null);
INSERT INTO `url_info` VALUES ('416', 'https://book.douban.com/tag/心理', '1', '心理', null, null);
INSERT INTO `url_info` VALUES ('417', 'https://book.douban.com/tag/轻小说', '1', '轻小说', null, null);
INSERT INTO `url_info` VALUES ('418', 'https://book.douban.com/tag/几米', '1', '几米', null, null);
INSERT INTO `url_info` VALUES ('419', 'https://book.douban.com/tag/村上春树', '1', '村上春树', null, null);
INSERT INTO `url_info` VALUES ('420', 'https://book.douban.com/tag/西方哲学', '1', '西方哲学', null, null);
INSERT INTO `url_info` VALUES ('421', 'https://book.douban.com/tag/情感', '1', '情感', null, null);
INSERT INTO `url_info` VALUES ('422', 'https://book.douban.com/tag/手工', '1', '手工', null, null);
INSERT INTO `url_info` VALUES ('423', 'https://book.douban.com/tag/金庸', '1', '金庸', null, null);
INSERT INTO `url_info` VALUES ('424', 'https://book.douban.com/tag/韩寒', '1', '韩寒', null, null);
INSERT INTO `url_info` VALUES ('425', 'https://book.douban.com/tag/生活', '1', '生活', null, null);
INSERT INTO `url_info` VALUES ('426', 'https://book.douban.com/tag/人物传记', '1', '人物传记', null, null);
INSERT INTO `url_info` VALUES ('427', 'https://book.douban.com/tag/养生', '1', '养生', null, null);
INSERT INTO `url_info` VALUES ('428', 'https://book.douban.com/tag/自助游', '1', '自助游', null, null);
INSERT INTO `url_info` VALUES ('429', 'https://book.douban.com/tag/国学', '1', '国学', null, null);
INSERT INTO `url_info` VALUES ('430', 'https://book.douban.com/tag/科幻小说', '1', '科幻小说', null, null);
INSERT INTO `url_info` VALUES ('431', 'https://book.douban.com/tag/耽美', '1', '耽美', null, null);
INSERT INTO `url_info` VALUES ('432', 'https://book.douban.com/tag/高木直子', '1', '高木直子', null, null);
INSERT INTO `url_info` VALUES ('433', 'https://book.douban.com/tag/校园', '1', '校园', null, null);
INSERT INTO `url_info` VALUES ('434', 'https://book.douban.com/tag/穿越', '1', '穿越', null, null);
INSERT INTO `url_info` VALUES ('435', 'https://book.douban.com/tag/三毛', '1', '三毛', null, null);
INSERT INTO `url_info` VALUES ('436', 'https://book.douban.com/tag/艺术', '1', '艺术', null, null);
INSERT INTO `url_info` VALUES ('437', 'https://book.douban.com/tag/数学', '1', '数学', null, null);
INSERT INTO `url_info` VALUES ('438', 'https://book.douban.com/tag/用户体验', '1', '用户体验', null, null);
INSERT INTO `url_info` VALUES ('439', 'https://book.douban.com/tag/自由主义', '1', '自由主义', null, null);
INSERT INTO `url_info` VALUES ('440', 'https://book.douban.com/tag/程序', '1', '程序', null, null);
INSERT INTO `url_info` VALUES ('441', 'https://book.douban.com/tag/安妮宝贝', '1', '安妮宝贝', null, null);
INSERT INTO `url_info` VALUES ('442', 'https://book.douban.com/tag/科技', '1', '科技', null, null);
INSERT INTO `url_info` VALUES ('443', 'https://book.douban.com/tag/诗词', '1', '诗词', null, null);
INSERT INTO `url_info` VALUES ('444', 'https://book.douban.com/tag/中国文学', '1', '中国文学', null, null);
INSERT INTO `url_info` VALUES ('445', 'https://book.douban.com/tag/考古', '1', '考古', null, null);
INSERT INTO `url_info` VALUES ('446', 'https://book.douban.com/tag/沧月', '1', '沧月', null, null);
INSERT INTO `url_info` VALUES ('447', 'https://book.douban.com/tag/外国名著', '1', '外国名著', null, null);
INSERT INTO `url_info` VALUES ('448', 'https://book.douban.com/tag/推理小说', '1', '推理小说', null, null);
INSERT INTO `url_info` VALUES ('449', 'https://book.douban.com/tag/亦舒', '1', '亦舒', null, null);
INSERT INTO `url_info` VALUES ('450', 'https://book.douban.com/tag/政治学', '1', '政治学', null, null);
INSERT INTO `url_info` VALUES ('451', 'https://book.douban.com/tag/古龙', '1', '古龙', null, null);
INSERT INTO `url_info` VALUES ('452', 'https://book.douban.com/tag/社会学', '1', '社会学', null, null);
INSERT INTO `url_info` VALUES ('453', 'https://book.douban.com/tag/钱钟书', '1', '钱钟书', null, null);
INSERT INTO `url_info` VALUES ('454', 'https://book.douban.com/tag/戏剧', '1', '戏剧', null, null);
INSERT INTO `url_info` VALUES ('455', 'https://book.douban.com/tag/web', '1', 'web', null, null);
INSERT INTO `url_info` VALUES ('456', 'https://book.douban.com/tag/阿加莎·克里斯蒂', '1', '阿加莎·克里斯蒂', null, null);
INSERT INTO `url_info` VALUES ('457', 'https://book.douban.com/tag/近代史', '1', '近代史', null, null);
INSERT INTO `url_info` VALUES ('458', 'https://book.douban.com/tag/人文', '1', '人文', null, null);
INSERT INTO `url_info` VALUES ('459', 'https://book.douban.com/tag/王小波', '1', '王小波', null, null);
INSERT INTO `url_info` VALUES ('460', 'https://book.douban.com/tag/二战', '1', '二战', null, null);
INSERT INTO `url_info` VALUES ('461', 'https://book.douban.com/tag/女性', '1', '女性', null, null);
INSERT INTO `url_info` VALUES ('462', 'https://book.douban.com/tag/余华', '1', '余华', null, null);
INSERT INTO `url_info` VALUES ('463', 'https://book.douban.com/tag/张爱玲', '1', '张爱玲', null, null);
INSERT INTO `url_info` VALUES ('464', 'https://book.douban.com/tag/文学?start=120&type=T', '1', '7', null, null);
INSERT INTO `url_info` VALUES ('465', 'https://book.douban.com/tag/文学?start=80&type=T', '1', '5', null, null);
INSERT INTO `url_info` VALUES ('466', 'https://book.douban.com/tag/%E6%96%87%E5%AD%A6?type=S', '1', '按评价排序', null, null);
INSERT INTO `url_info` VALUES ('467', 'https://book.douban.com/tag/文学?start=100&type=T', '1', '6', null, null);
INSERT INTO `url_info` VALUES ('468', 'https://book.douban.com/tag/文学?start=60&type=T', '1', '4', null, null);
INSERT INTO `url_info` VALUES ('469', 'https://book.douban.com/tag/%E6%96%87%E5%AD%A6?type=R', '1', '按出版日期排序', null, null);
INSERT INTO `url_info` VALUES ('470', 'https://book.douban.com/tag/文学?start=20&type=T', '1', '后页>', null, null);
INSERT INTO `url_info` VALUES ('471', 'https://book.douban.com/tag/文学?start=160&type=T', '1', '9', null, null);
INSERT INTO `url_info` VALUES ('472', 'https://book.douban.com/tag/纸版', '1', '纸版', null, null);
INSERT INTO `url_info` VALUES ('473', 'https://book.douban.com/tag/文学?start=1940&type=T', '1', '98', null, null);
INSERT INTO `url_info` VALUES ('474', 'https://book.douban.com/tag/中国', '1', '中国', null, null);
INSERT INTO `url_info` VALUES ('475', 'https://book.douban.com/tag/文学?start=140&type=T', '1', '8', null, null);
INSERT INTO `url_info` VALUES ('476', 'https://book.douban.com/tag/文学?start=1960&type=T', '1', '99', null, null);
INSERT INTO `url_info` VALUES ('477', 'https://book.douban.com/tag/文学?start=40&type=T', '1', '3', null, null);
