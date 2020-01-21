/*
Navicat MySQL Data Transfer

Source Server         : MySQL
Source Server Version : 50715
Source Host           : localhost:3306
Source Database       : mutil-user-excel-database

Target Server Type    : MYSQL
Target Server Version : 50715
File Encoding         : 65001

Date: 2020-01-16 09:33:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(18) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `status` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
DROP TRIGGER IF EXISTS `user_id_trigger`;
DELIMITER ;;
CREATE TRIGGER `user_id_trigger` BEFORE INSERT ON `user` FOR EACH ROW set new.id =CONCAT("user",DATE_FORMAT(NOW(),'%Y%m%d%H%i%s'))
;;
DELIMITER ;
