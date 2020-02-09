/*
Navicat MySQL Data Transfer

Source Server         : MySQL
Source Server Version : 50715
Source Host           : localhost:3306
Source Database       : mutil-user-excel-database

Target Server Type    : MYSQL
Target Server Version : 50715
File Encoding         : 65001

Date: 2020-02-08 22:21:45
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for door_image
-- ----------------------------
DROP TABLE IF EXISTS `door_image`;
CREATE TABLE `door_image` (
  `name` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of door_image
-- ----------------------------
INSERT INTO `door_image` VALUES ('新建excel', 'http://192.168.149.133:8083/download/images/new.PNG', '随时随地新建excel');
INSERT INTO `door_image` VALUES ('编辑excel', 'http://192.168.149.133:8083/download/images/excel.PNG', '多人同时编辑excel，支持excel导出，邀请小伙伴一起来编辑吧');
INSERT INTO `door_image` VALUES ('打开excel', 'http://192.168.149.133:8083/download/images/open.PNG', '翻找以前的编辑的excel，数据不怕丢，可打开小伙伴分享的excel');
INSERT INTO `door_image` VALUES ('导入excel', 'http://192.168.149.133:8083/download/images/import.PNG', '随时随地导入excel,编辑分享两不误');
