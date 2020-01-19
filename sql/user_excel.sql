/*
Navicat MySQL Data Transfer

Source Server         : MySQL
Source Server Version : 50715
Source Host           : localhost:3306
Source Database       : mutil-user-excel-database

Target Server Type    : MYSQL
Target Server Version : 50715
File Encoding         : 65001

Date: 2020-01-16 09:34:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user_excel
-- ----------------------------
DROP TABLE IF EXISTS `user_excel`;
CREATE TABLE `user_excel` (
  `user_id` varchar(255) DEFAULT NULL,
  `excel_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
