/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50634
Source Host           : localhost:3306
Source Database       : db_shiro

Target Server Type    : MYSQL
Target Server Version : 50634
File Encoding         : 65001

Date: 2017-10-06 16:04:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `queue_table_customer`
-- ----------------------------
DROP TABLE IF EXISTS `queue_table_customer`;
CREATE TABLE `queue_table_customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `request` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `entry_date` datetime DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2658 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of queue_table_customer
-- ----------------------------
INSERT INTO `queue_table_customer` VALUES ('2647', '{\"originOrderType\":0,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'NOT_HANDLE', '2017-09-13 23:03:05', '重新尝试1次');
INSERT INTO `queue_table_customer` VALUES ('2648', '{\"originOrderType\":0,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'NOT_HANDLE', '2017-09-13 23:03:05', '重新尝试2次');
INSERT INTO `queue_table_customer` VALUES ('2649', '{\"originOrderType\":0,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_HANDLE', '2017-09-13 23:03:05', null);
INSERT INTO `queue_table_customer` VALUES ('2650', '{\"originOrderType\":4,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_HANDLE', '2017-09-13 23:04:16', null);
INSERT INTO `queue_table_customer` VALUES ('2651', '{\"originOrderType\":6,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_HANDLE', '2017-09-13 23:04:36', null);
INSERT INTO `queue_table_customer` VALUES ('2652', '{\"originOrderType\":8,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_HANDLE', '2017-09-18 10:43:13', null);
INSERT INTO `queue_table_customer` VALUES ('2653', '{\"originOrderType\":10,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_HANDLE', '2017-09-18 10:43:32', null);
INSERT INTO `queue_table_customer` VALUES ('2654', '{\"originOrderType\":12,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_HANDLE', '2017-09-18 10:43:45', null);
INSERT INTO `queue_table_customer` VALUES ('2655', '{\"originOrderType\":14,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_HANDLE', '2017-09-18 10:44:01', null);
INSERT INTO `queue_table_customer` VALUES ('2656', '{\"originOrderType\":16,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_HANDLE', '2017-09-18 10:44:32', null);
INSERT INTO `queue_table_customer` VALUES ('2657', '{\"originOrderType\":18,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_HANDLE', '2017-09-18 10:44:45', null);
