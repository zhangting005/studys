/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50634
Source Host           : localhost:3306
Source Database       : db_shiro

Target Server Type    : MYSQL
Target Server Version : 50634
File Encoding         : 65001

Date: 2017-10-06 16:05:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `schedule_job`
-- ----------------------------
DROP TABLE IF EXISTS `schedule_job`;
CREATE TABLE `schedule_job` (
  `jobid` varchar(20) NOT NULL DEFAULT '',
  `createtime` timestamp NULL DEFAULT NULL,
  `updatetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `jobname` varchar(255) DEFAULT NULL,
  `jobgroup` varchar(255) DEFAULT NULL,
  `jobpause` varchar(255) DEFAULT NULL,
  `jobstatus` varchar(255) DEFAULT NULL,
  `cronexpression` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `beanclass` varchar(255) DEFAULT NULL,
  `isconcurrent` varchar(255) DEFAULT NULL COMMENT '1',
  `springid` varchar(255) DEFAULT NULL,
  `methodname` varchar(255) NOT NULL,
  PRIMARY KEY (`jobid`),
  UNIQUE KEY `name_group` (`jobname`,`jobgroup`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of schedule_job
-- ----------------------------
INSERT INTO `schedule_job` VALUES ('1', null, '2017-09-18 13:57:31', 'test1', 'test', '0', '0', '0/20 * * * * ?', 'test1', 'com.sl.quartz.task.TaskTest', '0', null, 'run1');
INSERT INTO `schedule_job` VALUES ('2', '2014-04-25 16:52:17', '2017-09-18 13:57:03', 'test2', '1111', '0', '0', '0/5 * * * * ?', 'test2', 'com.sl.quartz.task.TaskTest', '0', '', 'run2');
INSERT INTO `schedule_job` VALUES ('3', '2016-01-05 10:16:35', '2017-09-18 13:57:04', 'test3', 'test', '0', '0', '0/2 * * * * ?', 'test3', 'com.sl.quartz.task.TaskTest', '0', '', 'run3');
INSERT INTO `schedule_job` VALUES ('4', '2017-09-17 21:49:30', '2017-09-19 09:37:40', 'test4', 'test4', '0', '0', '0/30 * * * * ?', 'QueueTableTask', 'com.sl.quartz.task.QueueTableTask', '0', '', 'task');
