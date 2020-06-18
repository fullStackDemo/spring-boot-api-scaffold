/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 18/06/2020 14:33:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for weather
-- ----------------------------
DROP TABLE IF EXISTS `weather`;
CREATE TABLE `weather`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名字',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '天气状态',
  `date` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '当前日期',
  `max` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最高气温',
  `min` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最低气温',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `date`(`date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of weather
-- ----------------------------
INSERT INTO `weather` VALUES (15, '今天', '中雨转小雨', '2020-06-18', '34/22℃', NULL, '2020-06-18 14:27:55');
INSERT INTO `weather` VALUES (16, '明天', '多云', '2020-06-19', '28/21℃', NULL, '2020-06-18 14:27:55');
INSERT INTO `weather` VALUES (17, '后天', '小雨', '2020-06-20', '26/21℃', NULL, '2020-06-18 14:27:55');
INSERT INTO `weather` VALUES (18, '周日', '大雨转暴雨', '2020-06-21', '24/21℃', NULL, '2020-06-18 14:27:55');
INSERT INTO `weather` VALUES (19, '周一', '小雨', '2020-06-22', '27/23℃', NULL, '2020-06-18 14:27:55');
INSERT INTO `weather` VALUES (20, '周二', '小雨转阴', '2020-06-23', '27/24℃', NULL, '2020-06-18 14:27:55');
INSERT INTO `weather` VALUES (21, '周三', '小雨转中雨', '2020-06-24', '32/24℃', NULL, '2020-06-18 14:27:55');

SET FOREIGN_KEY_CHECKS = 1;
