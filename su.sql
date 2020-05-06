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

 Date: 06/05/2020 16:34:18
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
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tab_department
-- ----------------------------
DROP TABLE IF EXISTS `tab_department`;
CREATE TABLE `tab_department`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dep_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '部门编码',
  `dep_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'dep_name',
  `parent_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '部门父级id',
  `state` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '状态:1,启用，0，停用',
  `remark` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `last_update_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `remark1` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '备用字段1',
  `remark2` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '备用字段2',
  `is_sale` char(1) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '是否销售部门   1-销售部门  0-非销售部门',
  `task` decimal(16, 2) NULL DEFAULT NULL COMMENT '当年目标',
  `last_task` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '往年目标  格式：json格式的字符串',
  `task_month` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '各月目标。用英文逗号隔开',
  `company_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '公司id',
  `type` varchar(16) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '部门类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tab_dept_rel
-- ----------------------------
DROP TABLE IF EXISTS `tab_dept_rel`;
CREATE TABLE `tab_dept_rel`  (
  `ancestor` int(0) NOT NULL COMMENT '祖先节点',
  `descendant` int(0) NOT NULL COMMENT '后代节点',
  PRIMARY KEY (`ancestor`, `descendant`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Fixed;

-- ----------------------------
-- Table structure for tab_permission
-- ----------------------------
DROP TABLE IF EXISTS `tab_permission`;
CREATE TABLE `tab_permission`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '资源名称',
  `type` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '资源类型：menu,button,auth',
  `url` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '访问url地址',
  `per_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '权限代码字符串/menu=>vue name',
  `clazz` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '样式',
  `parent_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '父结点id',
  `parent_ids` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '父结点id列表串',
  `sort_string` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '排序号',
  `available` char(1) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '是否可用,1：可用，0不可用',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `last_update_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tab_subscriber
-- ----------------------------
INSERT INTO `tab_subscriber` VALUES ('1964160321', '15580046120', 'fae0b27c451c728867a567e8c1bb4e53', 14, '0', 'C:/Users/admin/Pictures/头像上传8a389a28-5a74-401d-96c6-b264d6d56e025406ba0052143.jpg', '2020-04-30 11:40:37');
INSERT INTO `tab_subscriber` VALUES ('1964160347', '13873984611', '202cb962ac59075b964b07152d234b70', NULL, '0', 'C:/Users/admin/Pictures/头像上传//timg.jpg', '2020-05-02 13:19:50');
INSERT INTO `tab_subscriber` VALUES ('1973849957', '15580046123', 'd81f9c1be2e08964bf9f24b15f0e4900', NULL, '0', 'C:/Users/admin/Pictures/头像上传//timg.jpg', '2020-05-06 15:50:31');

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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
