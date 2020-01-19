/*
Navicat MySQL Data Transfer

Source Server         : MySQL
Source Server Version : 50715
Source Host           : localhost:3306
Source Database       : mutil-user-excel-database

Target Server Type    : MYSQL
Target Server Version : 50715
File Encoding         : 65001

Date: 2020-01-16 09:34:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for excel
-- ----------------------------
DROP TABLE IF EXISTS `excel`;
CREATE TABLE `excel` (
  `id` varchar(100) NOT NULL COMMENT 'excel的id，由创建人邮箱和当前时间生成',
  `name` varchar(255) DEFAULT '未命名' COMMENT 'excel名',
  `create_date` date NOT NULL COMMENT 'excel创建时间',
  `last_modify_date` datetime NOT NULL COMMENT 'excel最后修改时间',
  `json` mediumtext NOT NULL COMMENT 'excel的json对象字符串',
  `creater_id` varchar(255) NOT NULL COMMENT 'excel创建者',
  `status` char(1) DEFAULT '1' COMMENT 'excel的状态 0:删除，1:正常',
  `file_describe` varchar(255) DEFAULT NULL COMMENT '文件描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
DROP TRIGGER IF EXISTS `excel_last_modify_date_trigger`;
DELIMITER ;;
CREATE TRIGGER `excel_last_modify_date_trigger` BEFORE UPDATE ON `excel` FOR EACH ROW set new.last_modify_date =DATE_FORMAT(NOW(),'%Y%m%d%H%i%s')
;;
DELIMITER ;
