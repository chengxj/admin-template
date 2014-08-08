/*
Navicat MySQL Data Transfer

Source Server         : 3308
Source Server Version : 50610
Source Host           : localhost:3308
Source Database       : demo

Target Server Type    : MYSQL
Target Server Version : 50610
File Encoding         : 65001

Date: 2014-08-08 17:32:28
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `company_config`
-- ----------------------------
DROP TABLE IF EXISTS `company_config`;
CREATE TABLE `company_config` (
  `CONFIG_ID` int(11) NOT NULL,
  `COMPANY_ID` int(11) DEFAULT NULL,
  `CONFIG_KEY` varchar(16) NOT NULL,
  `CONFIG_VALUE` varchar(16) NOT NULL,
  `DESCRIPTION` varchar(128) DEFAULT NULL,
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`CONFIG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='如果COMPANY_ID=-1,则表示是全局配置项';

-- ----------------------------
-- Records of company_config
-- ----------------------------

-- ----------------------------
-- Table structure for `i18n_message`
-- ----------------------------
DROP TABLE IF EXISTS `i18n_message`;
CREATE TABLE `i18n_message` (
  `I18N_ID` int(11) NOT NULL,
  `I18N_KEY` varchar(64) NOT NULL,
  `I18N_VALUE_EN` varchar(64) NOT NULL,
  `I18N_VALUE_ZH_CN` varchar(64) NOT NULL,
  `I18N_VALUE_ZH_TW` varchar(64) NOT NULL,
  `IS_ROOT` tinyint(1) DEFAULT NULL,
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`I18N_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of i18n_message
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_company`
-- ----------------------------
DROP TABLE IF EXISTS `sys_company`;
CREATE TABLE `sys_company` (
  `COMPANY_ID` int(11) NOT NULL,
  `COMPANY_NAME` varchar(64) NOT NULL,
  `COMPANY_CODE` varchar(8) NOT NULL,
  `IS_DEL` tinyint(1) NOT NULL,
  PRIMARY KEY (`COMPANY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_company
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_dict`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `DICT_CODE` varchar(32) NOT NULL,
  `DICT_NAME` varchar(32) NOT NULL,
  `PARENT_CODE` varchar(32) NOT NULL DEFAULT '-1',
  `SORTED` int(11) NOT NULL DEFAULT '9999',
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`DICT_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_job`
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job` (
  `JOB_ID` int(11) NOT NULL,
  `JOB_NAME` varchar(32) NOT NULL,
  `CLAZZ_NAME` varchar(64) NOT NULL,
  `CRON` varchar(32) DEFAULT NULL,
  `ENABLED` tinyint(1) NOT NULL,
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`JOB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_job
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `MENU_ID` int(11) NOT NULL,
  `PARENT_ID` int(11) NOT NULL DEFAULT '-1',
  `MENU_NAME` varchar(32) NOT NULL,
  `MENU_TYPE` varchar(32) DEFAULT NULL,
  `MENU_PATH` varchar(128) DEFAULT NULL,
  `SORTED` int(11) DEFAULT '9999',
  `ICON` varchar(32) DEFAULT NULL,
  `PERMISSION` varchar(64) DEFAULT NULL,
  `IS_ROOT` tinyint(1) DEFAULT NULL,
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`MENU_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单类型：菜单、按钮\r\n系统中把菜单看作是路由和资源的集合';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_menu_res`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu_res`;
CREATE TABLE `sys_menu_res` (
  `MENU_RES_ID` int(11) NOT NULL,
  `RESOURCE_ID` int(11) DEFAULT NULL,
  `MENU_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`MENU_RES_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单资源表，用户拥有菜单权限则拥有了相应的rest权限';

-- ----------------------------
-- Records of sys_menu_res
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_menu_route`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu_route`;
CREATE TABLE `sys_menu_route` (
  `MENU_ROUTE_ID` int(11) NOT NULL,
  `ROUTE_ID` int(11) DEFAULT NULL,
  `MENU_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`MENU_ROUTE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色拥有菜单的权限，则代表拥有路由的权限';

-- ----------------------------
-- Records of sys_menu_route
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_resource`
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource` (
  `RESOURCE_ID` int(11) NOT NULL,
  `URL` varchar(128) NOT NULL,
  `METHOD` varchar(32) NOT NULL,
  `RESOURCE_NAME` varchar(64) DEFAULT NULL,
  `PERMISSION` varchar(64) DEFAULT NULL,
  `IS_ROOT` tinyint(1) NOT NULL,
  `AUTH_TYPE` varchar(32) NOT NULL,
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`RESOURCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_resource
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `ROLE_ID` int(11) NOT NULL,
  `ROLE_NAME` varchar(32) NOT NULL,
  `ROLE_CODE` varchar(32) NOT NULL,
  `IS_ROOT` tinyint(1) DEFAULT NULL,
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `ROLE_MENU_ID` int(11) NOT NULL,
  `ROLE_ID` int(11) DEFAULT NULL,
  `MENU_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ROLE_MENU_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_role_res`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_res`;
CREATE TABLE `sys_role_res` (
  `ROLE_RES_ID` int(11) NOT NULL,
  `RESOURCE_ID` int(11) DEFAULT NULL,
  `ROLE_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ROLE_RES_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_res
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_role_route`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_route`;
CREATE TABLE `sys_role_route` (
  `ROLE_ROUTE_ID` int(11) NOT NULL,
  `ROUTE_ID` int(11) DEFAULT NULL,
  `ROLE_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ROLE_ROUTE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_route
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_route`
-- ----------------------------
DROP TABLE IF EXISTS `sys_route`;
CREATE TABLE `sys_route` (
  `ROUTE_ID` int(11) NOT NULL,
  `NAME` varchar(32) NOT NULL,
  `URL` varchar(128) NOT NULL,
  `IS_ROOT` tinyint(1) DEFAULT NULL,
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ROUTE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_route
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `USER_ID` int(11) NOT NULL,
  `USERNAME` varchar(16) NOT NULL,
  `PASSWORD` varchar(64) NOT NULL,
  `FULL_NAME` varchar(32) NOT NULL,
  `EMAIL` varchar(64) DEFAULT NULL,
  `BIRTHDAY` date DEFAULT NULL,
  `SALT` varchar(64) DEFAULT NULL,
  `ENABLED` tinyint(1) DEFAULT NULL,
  `IS_ROOT` tinyint(1) DEFAULT NULL,
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_user_profile`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_profile`;
CREATE TABLE `sys_user_profile` (
  `PROFILE_ID` int(11) NOT NULL,
  `USER_ID` int(11) DEFAULT NULL,
  `LANGUAGE` varchar(16) NOT NULL,
  PRIMARY KEY (`PROFILE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_profile
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `USER_ROLE_ID` int(11) NOT NULL,
  `USER_ID` int(11) DEFAULT NULL,
  `ROLE_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`USER_ROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------

-- ----------------------------
-- Table structure for `task`
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `TASK_ID` int(11) NOT NULL,
  `COMPANY_ID` int(11) DEFAULT NULL,
  `TASK_NAME` varchar(32) DEFAULT NULL,
  `ASSIGNEE` varchar(32) DEFAULT NULL,
  `ASSIGNEE_ID` int(11) DEFAULT NULL,
  `ASSIGNEE_TIME` decimal(14,0) DEFAULT NULL,
  `COMMENT` varchar(128) DEFAULT NULL,
  `TRACKER_ID` int(11) DEFAULT NULL,
  `TRACKER` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`TASK_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='状态：未领取、进行中、完成\r\n类型：提交订单、分配任务、出库、领取保管箱、出库押运、客户签收、使用完毕、交还、';

-- ----------------------------
-- Records of task
-- ----------------------------

-- ----------------------------
-- Table structure for `test_table`
-- ----------------------------
DROP TABLE IF EXISTS `test_table`;
CREATE TABLE `test_table` (
  `TEST_CODE` varchar(32) NOT NULL,
  `DICT_NAME` varchar(32) NOT NULL,
  `PARENT_CODE` varchar(32) NOT NULL DEFAULT '-1',
  `SORTED` int(11) NOT NULL DEFAULT '9999',
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`TEST_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of test_table
-- ----------------------------

-- ----------------------------
-- Table structure for `test2_table`
-- ----------------------------
DROP TABLE IF EXISTS `test2_table`;
CREATE TABLE `test2_table` (
  `TEST_ID` varchar(32) NOT NULL,
  `TEST_CODE2` varchar(32) NOT NULL,
  `DICT_NAME` varchar(32) NOT NULL,
  `PARENT_CODE` varchar(32) NOT NULL DEFAULT '-1',
  `SORTED` int(11) NOT NULL DEFAULT '9999',
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`TEST_CODE2`,`TEST_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of test2_table
-- ----------------------------
