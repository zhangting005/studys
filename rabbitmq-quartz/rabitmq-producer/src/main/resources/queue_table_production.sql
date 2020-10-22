/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50634
Source Host           : localhost:3306
Source Database       : db_shiro

Target Server Type    : MYSQL
Target Server Version : 50634
File Encoding         : 65001

Date: 2017-10-06 16:05:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `queue_table_production`
-- ----------------------------
DROP TABLE IF EXISTS `queue_table_production`;
CREATE TABLE `queue_table_production` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `request` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `entry_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of queue_table_production
-- ----------------------------
INSERT INTO `queue_table_production` VALUES ('16', '{\"originOrderType\":0,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_SEND', '2017-09-13 23:03:05');
INSERT INTO `queue_table_production` VALUES ('17', '{\"originOrderType\":3,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_SEND', '2017-09-13 23:03:55');
INSERT INTO `queue_table_production` VALUES ('18', '{\"originOrderType\":4,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_SEND', '2017-09-13 23:04:16');
INSERT INTO `queue_table_production` VALUES ('19', '{\"originOrderType\":5,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_SEND', '2017-09-13 23:04:27');
INSERT INTO `queue_table_production` VALUES ('20', '{\"originOrderType\":6,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_SEND', '2017-09-13 23:04:36');
INSERT INTO `queue_table_production` VALUES ('21', '{\"originOrderType\":7,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_SEND', '2017-09-18 10:42:57');
INSERT INTO `queue_table_production` VALUES ('22', '{\"originOrderType\":8,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_SEND', '2017-09-18 10:43:13');
INSERT INTO `queue_table_production` VALUES ('23', '{\"originOrderType\":9,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_SEND', '2017-09-18 10:43:23');
INSERT INTO `queue_table_production` VALUES ('24', '{\"originOrderType\":10,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_SEND', '2017-09-18 10:43:32');
INSERT INTO `queue_table_production` VALUES ('25', '{\"originOrderType\":11,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_SEND', '2017-09-18 10:43:39');
INSERT INTO `queue_table_production` VALUES ('26', '{\"originOrderType\":12,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_SEND', '2017-09-18 10:43:45');
INSERT INTO `queue_table_production` VALUES ('27', '{\"originOrderType\":13,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_SEND', '2017-09-18 10:43:54');
INSERT INTO `queue_table_production` VALUES ('28', '{\"originOrderType\":14,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_SEND', '2017-09-18 10:44:01');
INSERT INTO `queue_table_production` VALUES ('29', '{\"originOrderType\":15,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_SEND', '2017-09-18 10:44:24');
INSERT INTO `queue_table_production` VALUES ('30', '{\"originOrderType\":16,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_SEND', '2017-09-18 10:44:32');
INSERT INTO `queue_table_production` VALUES ('31', '{\"originOrderType\":17,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_SEND', '2017-09-18 10:44:39');
INSERT INTO `queue_table_production` VALUES ('32', '{\"originOrderType\":18,\"originOrderNo\":1,\"originOrderQty\":1,\"newOrderNo\":1,\"newOrderQty\":1}', 'split', 'HAS_SEND', '2017-09-18 10:44:45');
