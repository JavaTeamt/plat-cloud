/*
 Navicat Premium Data Transfer

 Source Server         : czkj
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : su

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 11/05/2020 10:57:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tab_accitem
-- ----------------------------
DROP TABLE IF EXISTS `tab_accitem`;
CREATE TABLE `tab_accitem`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '账本id',
  `statuss` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '状态 0=无效 1=有效',
  `describe` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '账本说明',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tab_account
-- ----------------------------
DROP TABLE IF EXISTS `tab_account`;
CREATE TABLE `tab_account`  (
  `seq` decimal(20, 0) NOT NULL COMMENT '账本流水号',
  `subid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户id',
  `accitemid` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '账本id',
  `amount` decimal(20, 0) NULL DEFAULT NULL COMMENT '账本金额',
  `cratetime` date NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`seq`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tab_customer
-- ----------------------------
DROP TABLE IF EXISTS `tab_customer`;
CREATE TABLE `tab_customer`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '序列号',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '姓名',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '性别 1=男 2=女',
  `certtype` char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '身份证',
  `certid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '身份证号',
  `custidentify` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '实名认证标识,0=未实名认证,1=已实名认证',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '手机号',
  `createtime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tab_customer
-- ----------------------------
INSERT INTO `tab_customer` VALUES (1, '孙敏', '1', '居民身份证', '430522199610305895', '1', '15580046122', '2020-05-07 00:00:00');
INSERT INTO `tab_customer` VALUES (2, '烟给杨', '1', '居民身份证', '430566200510305895', '1', '15580046188', '2020-05-07 00:00:00');

-- ----------------------------
-- Table structure for tab_permission
-- ----------------------------
DROP TABLE IF EXISTS `tab_permission`;
CREATE TABLE `tab_permission`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '资源名称',
  `available` char(1) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '是否可用,1：可用，0不可用',
  `remark` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `last_update_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tab_permission
-- ----------------------------
INSERT INTO `tab_permission` VALUES (1, '超级管理员', '1', '拥有所有权限', '2020-05-12 16:45:00', '2020-05-08 07:54:32');
INSERT INTO `tab_permission` VALUES (2, 'test', '1', NULL, '2020-05-07 14:47:33', NULL);
INSERT INTO `tab_permission` VALUES (3, 'test3', '1', NULL, '2020-05-13 09:56:43', '2020-05-08 09:56:49');
INSERT INTO `tab_permission` VALUES (4, 'test5', '1', '用于测试', '2020-05-11 09:21:12', NULL);

-- ----------------------------
-- Table structure for tab_permission_url
-- ----------------------------
DROP TABLE IF EXISTS `tab_permission_url`;
CREATE TABLE `tab_permission_url`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'url',
  `per_id` int(0) NOT NULL COMMENT '权限id',
  `available` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '可用标识',
  `remark` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `last_update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tab_permission_url
-- ----------------------------
INSERT INTO `tab_permission_url` VALUES (3, 'www.cn', 2, '1', 'test', '2020-05-07 15:13:32', '2020-05-07 15:13:32');
INSERT INTO `tab_permission_url` VALUES (4, 'localhost:8080', 1, '1', NULL, '2020-05-08 01:48:46', '2020-05-08 01:48:46');
INSERT INTO `tab_permission_url` VALUES (5, 'localhost:8811', 1, '1', NULL, '2020-05-21 01:48:46', '2020-05-08 01:48:46');

-- ----------------------------
-- Table structure for tab_role
-- ----------------------------
DROP TABLE IF EXISTS `tab_role`;
CREATE TABLE `tab_role`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '角色代码',
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '角色名称',
  `available` char(1) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '是否可用,1：可用，0不可用',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `last_update_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tab_role
-- ----------------------------
INSERT INTO `tab_role` VALUES (1, 'ROLE', 'ROLE', '1', '2020-05-12 18:21:42', '2020-05-20 18:21:44');
INSERT INTO `tab_role` VALUES (2, 'TESTCODE', 'test1', '1', '2020-05-07 00:00:00', '2020-05-07 16:12:31');
INSERT INTO `tab_role` VALUES (3, 'TEST2', 'TEST', '1', '2020-05-08 05:58:28', NULL);
INSERT INTO `tab_role` VALUES (5, 'xxxx', 'baba权限', '1', '2020-05-08 14:15:45', '2020-05-08 14:25:49');

-- ----------------------------
-- Table structure for tab_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `tab_role_permission`;
CREATE TABLE `tab_role_permission`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sys_role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '角色id',
  `sys_permission_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '权限id',
  `create_time` datetime(0) NOT NULL COMMENT '创建日期',
  `last_update_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tab_role_permission
-- ----------------------------
INSERT INTO `tab_role_permission` VALUES (1, '1', '1', '2020-05-20 18:21:59', '2020-05-19 18:22:01');
INSERT INTO `tab_role_permission` VALUES (2, '2', '1', '2020-05-07 15:37:44', NULL);
INSERT INTO `tab_role_permission` VALUES (3, '2', '2', '2020-05-07 15:37:44', NULL);
INSERT INTO `tab_role_permission` VALUES (6, '5', '3', '2020-05-08 14:25:49', '2020-05-08 14:25:49');
INSERT INTO `tab_role_permission` VALUES (7, '5', '2', '2020-05-08 14:25:49', '2020-05-08 14:25:49');

-- ----------------------------
-- Table structure for tab_subscriber
-- ----------------------------
DROP TABLE IF EXISTS `tab_subscriber`;
CREATE TABLE `tab_subscriber`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '手机号',
  `password` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '密码',
  `custid` bigint(0) NULL DEFAULT NULL COMMENT '客户id',
  `loginstatus` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '登录状态 0=未登录 1=已登录',
  `headimg` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '头像路径',
  `createtime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tab_subscriber
-- ----------------------------
INSERT INTO `tab_subscriber` VALUES ('1973849930', '15550046100', 'df6d2338b2b8fce1ec2f6dda0a630eb0', NULL, '1', 'C:/Users/admin/Pictures/头像上传/29f48d8d-049d-430b-a1a7-65b99ea6af8d.jpg', '2020-05-08 13:38:36');
INSERT INTO `tab_subscriber` VALUES ('1973849950', '15550046120', '202cb962ac59075b964b07152d234b70', NULL, '1', 'C:/Users/admin/Pictures/头像上传/timg.jpg', '2020-05-07 07:33:44');
INSERT INTO `tab_subscriber` VALUES ('1973849951', '15580046188', '202cb962ac59075b964b07152d234b70', 2, '0', 'C:/Users/admin/Pictures/头像上传//timg.jpg', '2020-05-07 04:08:44');
INSERT INTO `tab_subscriber` VALUES ('1973849962', '10987654321', '202cb962ac59075b964b07152d234b70', NULL, '0', '传//timg.jpg', '2020-05-06 18:38:36');
INSERT INTO `tab_subscriber` VALUES ('1973849967', '15387391598', 'df6d2338b2b8fce1ec2f6dda0a630eb0', NULL, '0', 'C:/Users/admin/Pictures/头像上传/timg.jpg', '2020-05-08 13:37:14');
INSERT INTO `tab_subscriber` VALUES ('1973849976', '15387391587', 'df6d2338b2b8fce1ec2f6dda0a630eb0', NULL, '0', 'C:/Users/admin/Pictures/头像上传/timg.jpg', '2020-05-08 13:34:11');

-- ----------------------------
-- Table structure for tab_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tab_user_role`;
CREATE TABLE `tab_user_role`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sys_user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '用户id',
  `sys_role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '角色id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `last_update_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tab_user_role
-- ----------------------------
INSERT INTO `tab_user_role` VALUES (1, '1973849950', '1', '2020-05-07 07:33:44', '2020-05-07 07:33:44');
INSERT INTO `tab_user_role` VALUES (2, '1973849950', '2', '2020-05-07 07:33:44', '2020-05-07 07:33:44');
INSERT INTO `tab_user_role` VALUES (11, '1973849951', '1', '2020-05-07 00:00:00', '2020-05-07 08:20:22');
INSERT INTO `tab_user_role` VALUES (12, '1973849951', '2', '2020-05-07 00:00:00', '2020-05-07 08:20:22');

SET FOREIGN_KEY_CHECKS = 1;
