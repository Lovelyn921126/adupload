/*
Navicat MySQL Data Transfer

Source Server         : 广告上传mysql_正式
Source Server Version : 50711
Source Host           : 10.16.65.37:3132
Source Database       : glht_adupload

Target Server Type    : MYSQL
Target Server Version : 50711
File Encoding         : 65001

Date: 2017-07-04 11:56:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for advert_channel
-- ----------------------------
DROP TABLE IF EXISTS `advert_channel`;
CREATE TABLE `advert_channel` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(100) NOT NULL COMMENT '频道名字',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否删除（1=删除，0=未删除）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8 COMMENT='广告频道管理表-业务表';

-- ----------------------------
-- Table structure for advert_comment
-- ----------------------------
DROP TABLE IF EXISTS `advert_comment`;
CREATE TABLE `advert_comment` (
  `id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '字段id',
  `panoid` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '文件id，对应file表里的source_id',
  `message` varchar(40) CHARACTER SET utf8mb4 NOT NULL COMMENT '说一说内容',
  `userid` bigint(20) NOT NULL COMMENT '用户通行证ID',
  `avatar` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户头像地址',
  `ath` float(6,2) NOT NULL COMMENT '页面点击横坐标',
  `atv` float(6,2) NOT NULL COMMENT '页面点击纵坐标',
  `scene` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '全景/航拍的场景',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否删除（1=删除，0=未删除）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`,`panoid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='360广告说一说管理表-业务表';

-- ----------------------------
-- Table structure for advert_file
-- ----------------------------
DROP TABLE IF EXISTS `advert_file`;
CREATE TABLE `advert_file` (
  `id` varchar(32) NOT NULL COMMENT '字段id',
  `source_id` varchar(32) NOT NULL COMMENT '文件id，正常情况下是唯一的.重构前的数据有一些可能重复',
  `project_name` varchar(500) NOT NULL COMMENT '项目名字',
  `source_url` varchar(500) NOT NULL COMMENT '文件链接地址',
  `source_extension` varchar(10) NOT NULL COMMENT '文件扩展名',
  `source_size` double(10,1) NOT NULL COMMENT '文件大小',
  `source_width` int(11) DEFAULT NULL COMMENT '文件宽(px)',
  `source_height` int(11) DEFAULT NULL COMMENT '文件高(px)',
  `service_type` tinyint(4) NOT NULL COMMENT '文件类型(1:普通广告；2:360全景)',
  `city_id` int(10) NOT NULL COMMENT '对应的城市id',
  `location_id` bigint(20) DEFAULT NULL COMMENT '对应的广告位id',
  `channel_id` bigint(20) NOT NULL COMMENT '频道id',
  `acquire_info_way` tinyint(4) DEFAULT NULL COMMENT '数据来源渠道(1:接口；2:用户直接上传；-1:无定义)',
  `upload_user_id` varchar(32) NOT NULL COMMENT '上传者id，本地系统内的ID',
  `upload_username` varchar(100) DEFAULT NULL COMMENT '上传者邮箱',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否删除（1=删除，0=未删除）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`,`source_id`),
  KEY `IX_AdverFile_SourceIdAndIsDelete` (`source_id`,`is_delete`) USING BTREE,
  KEY `IX_AdverFile_CreateTimeAndServiceTypeAndProjectNameAndUseName` (`create_time`,`service_type`,`project_name`,`upload_username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='广告文件表-业务表-普通zip，普通图片，360zip，360图片数据都在这';

-- ----------------------------
-- Table structure for advert_location
-- ----------------------------
DROP TABLE IF EXISTS `advert_location`;
CREATE TABLE `advert_location` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(100) NOT NULL COMMENT '名字',
  `channel_id` bigint(20) DEFAULT NULL COMMENT '对应的频道id',
  `size` double(10,1) DEFAULT NULL COMMENT '大小',
  `units` varchar(4) DEFAULT NULL COMMENT '单位',
  `width` int(20) DEFAULT NULL COMMENT '宽',
  `height` int(20) DEFAULT NULL COMMENT '高',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否删除（1=删除，0=未删除）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8 COMMENT='广告位管理表-业务表-广告位属于频道子数据';

-- ----------------------------
-- Table structure for sys_city
-- ----------------------------
DROP TABLE IF EXISTS `sys_city`;
CREATE TABLE `sys_city` (
  `city_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '城市编号',
  `name` varchar(20) NOT NULL COMMENT '城市名称',
  `code` varchar(20) NOT NULL COMMENT '城市简写',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否删除（1=删除，0=未删除）',
  PRIMARY KEY (`city_id`)
) ENGINE=InnoDB AUTO_INCREMENT=252 DEFAULT CHARSET=utf8 COMMENT='城市管理标-系统表';

-- ----------------------------
-- Table structure for sys_group
-- ----------------------------
DROP TABLE IF EXISTS `sys_group`;
CREATE TABLE `sys_group` (
  `group_id` int(10) NOT NULL COMMENT '集团ID',
  `parent_id` int(10) DEFAULT NULL COMMENT '父集团ID，一级菜单为0',
  `name` varchar(50) DEFAULT NULL COMMENT '集团名称',
  `order_num` int(11) DEFAULT NULL COMMENT '显示顺序（越大越在上）',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否删除（1=删除，0=未删除）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='集团信息-业务表';

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) DEFAULT NULL COMMENT '请求参数',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19826 DEFAULT CHARSET=utf8 COMMENT='日志表-系统表-登录、操作日志';

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` varchar(32) NOT NULL COMMENT '菜单ID',
  `parent_id` varchar(32) NOT NULL DEFAULT '' COMMENT '父菜单ID，一级菜单为空（程序里对应32个0）',
  `menu_name` varchar(50) NOT NULL DEFAULT '' COMMENT '当前菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` tinyint(1) DEFAULT NULL COMMENT '类型 0：目录(文件夹) 1：菜单(子页面) 2：按钮(页面内操作)',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否删除（1=删除，0=未删除）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统菜单-系统表';

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` varchar(32) NOT NULL COMMENT '角色ID',
  `parent_id` varchar(32) NOT NULL DEFAULT '' COMMENT '父角色ID，一级菜单为空（程序里对应32个0）',
  `role_name` varchar(50) NOT NULL DEFAULT '' COMMENT '当前角色名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建者ID',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否删除（1=删除，0=未删除）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统角色-系统表';

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` varchar(32) NOT NULL COMMENT '关系ID',
  `role_id` varchar(32) NOT NULL COMMENT '角色ID',
  `menu_id` varchar(32) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与菜单对应关系表-系统表';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `username` varchar(100) NOT NULL COMMENT '用户名(邮箱)',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `name` varchar(50) NOT NULL COMMENT '用户名字(真实姓名)',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机号',
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '账户类型（\r\n(默认)0:需要在OA入口登录；\r\n(少量)1:不经过OA;\r\n(少量)3:超级用户）',
  `group_id` int(10) DEFAULT NULL COMMENT '集团编号',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建者ID',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否删除（1=删除，0=未删除）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统用户表-系统表';

-- ----------------------------
-- Table structure for sys_user_city
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_city`;
CREATE TABLE `sys_user_city` (
  `id` varchar(32) NOT NULL COMMENT '关系ID',
  `city_id` int(11) NOT NULL COMMENT '城市ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统用户与权限城市对应关系表-系统表';

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` varchar(32) NOT NULL COMMENT '关系ID',
  `role_id` varchar(32) NOT NULL COMMENT '角色ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与角色对应关系表-系统表';
