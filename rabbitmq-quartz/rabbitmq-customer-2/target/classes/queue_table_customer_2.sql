/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50634
Source Host           : localhost:3306
Source Database       : db_shiro

Target Server Type    : MYSQL
Target Server Version : 50634
File Encoding         : 65001

Date: 2017-10-06 16:05:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `queue_table_customer_2`
-- ----------------------------
DROP TABLE IF EXISTS `queue_table_customer_2`;
CREATE TABLE `queue_table_customer_2` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `request` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `entry_date` datetime DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2647 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of queue_table_customer_2
-- ----------------------------
INSERT INTO `queue_table_customer_2` VALUES ('2639', '{\"originOrderType\":3,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_HANDLE', '2017-09-13 23:03:55', null);
INSERT INTO `queue_table_customer_2` VALUES ('2640', '{\"originOrderType\":5,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_HANDLE', '2017-09-13 23:04:27', null);
INSERT INTO `queue_table_customer_2` VALUES ('2641', '{\"originOrderType\":7,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_HANDLE', '2017-09-18 10:42:57', null);
INSERT INTO `queue_table_customer_2` VALUES ('2642', '{\"originOrderType\":9,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_HANDLE', '2017-09-18 10:43:23', null);
INSERT INTO `queue_table_customer_2` VALUES ('2643', '{\"originOrderType\":11,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_HANDLE', '2017-09-18 10:43:39', null);
INSERT INTO `queue_table_customer_2` VALUES ('2644', '{\"originOrderType\":13,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_HANDLE', '2017-09-18 10:43:54', null);
INSERT INTO `queue_table_customer_2` VALUES ('2645', '{\"originOrderType\":15,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_HANDLE', '2017-09-18 10:44:24', null);
INSERT INTO `queue_table_customer_2` VALUES ('2646', '{\"originOrderType\":17,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_HANDLE', '2017-09-18 10:44:39', null);
