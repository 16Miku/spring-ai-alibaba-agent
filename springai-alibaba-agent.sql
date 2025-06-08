/*
 Navicat Premium Data Transfer

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:3306
 Source Schema         : springai-alibaba-agent

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 08/06/2025 09:15:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for items
-- ----------------------------
DROP TABLE IF EXISTS `items`;
CREATE TABLE `items`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `price` decimal(10, 2) NOT NULL COMMENT '商品价格',
  `stock` int(0) NOT NULL DEFAULT 0 COMMENT '库存数量',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of items
-- ----------------------------
INSERT INTO `items` VALUES (1, '笔记本电脑', 5999.00, 93, '2025-05-11 16:23:24', '2025-05-12 21:27:09');
INSERT INTO `items` VALUES (2, '智能手机', 3499.50, 200, '2025-05-11 16:23:24', '2025-05-11 16:23:24');
INSERT INTO `items` VALUES (3, '蓝牙耳机', 499.00, 500, '2025-05-11 16:23:24', '2025-05-11 16:23:24');

-- ----------------------------
-- Table structure for order_items
-- ----------------------------
DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '订单项ID',
  `order_id` bigint(0) NOT NULL COMMENT '所属订单ID',
  `item_id` bigint(0) NOT NULL COMMENT '商品ID',
  `item_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称（下单时的名称，防止商品改名影响历史订单）',
  `item_price` decimal(10, 2) NOT NULL COMMENT '商品价格（下单时的价格）',
  `quantity` int(0) NOT NULL COMMENT '购买数量',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_id`(`order_id`) USING BTREE,
  INDEX `item_id`(`item_id`) USING BTREE,
  CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`item_id`) REFERENCES `items` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_items
-- ----------------------------
INSERT INTO `order_items` VALUES (1, 1, 1, '笔记本电脑', 5999.00, 1, '2025-05-11 16:23:24', '2025-05-11 16:23:24');
INSERT INTO `order_items` VALUES (2, 1, 3, '蓝牙耳机', 499.00, 1, '2025-05-11 16:23:24', '2025-05-11 16:23:24');
INSERT INTO `order_items` VALUES (3, 2, 2, '智能手机', 3499.50, 1, '2025-05-11 16:23:24', '2025-05-11 16:23:24');
INSERT INTO `order_items` VALUES (4, 3, 1, '笔记本电脑', 5999.00, 1, '2025-05-12 13:46:28', '2025-05-12 13:46:28');
INSERT INTO `order_items` VALUES (5, 4, 1, '笔记本电脑', 5999.00, 2, '2025-05-12 14:43:12', '2025-05-12 14:43:12');
INSERT INTO `order_items` VALUES (6, 5, 1, '笔记本电脑', 5999.00, 2, '2025-05-12 20:50:41', '2025-05-12 20:50:41');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `user_id` bigint(0) NOT NULL COMMENT '下单用户ID',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单总金额',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单状态（如: PENDING, PAID, SHIPPED, COMPLETED, CANCELLED）',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1, 1, 6498.00, 'PENDING', '2025-05-11 16:23:24', '2025-05-11 16:23:24');
INSERT INTO `orders` VALUES (2, 2, 3499.50, 'PAID', '2025-05-11 16:23:24', '2025-05-11 16:23:24');
INSERT INTO `orders` VALUES (3, 1, 5999.00, 'PENDING', '2025-05-12 13:46:28', '2025-05-12 13:46:28');
INSERT INTO `orders` VALUES (4, 1, 11998.00, 'PAID', '2025-05-12 14:43:12', '2025-05-12 14:43:16');
INSERT INTO `orders` VALUES (5, 1, 11998.00, 'PAID', '2025-05-12 20:50:41', '2025-05-12 21:06:35');

-- ----------------------------
-- Table structure for payments
-- ----------------------------
DROP TABLE IF EXISTS `payments`;
CREATE TABLE `payments`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '支付ID',
  `order_id` bigint(0) NOT NULL COMMENT '关联订单ID（一个订单通常只有一个支付记录）',
  `amount` decimal(10, 2) NOT NULL COMMENT '支付金额',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '支付状态（如: PENDING, SUCCESS, FAILED, REFUNDED）',
  `payment_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付方式（如: ALIPAY, WECHAT_PAY, CREDIT_CARD）',
  `transaction_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付平台交易ID',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_id`(`order_id`) USING BTREE,
  UNIQUE INDEX `transaction_id`(`transaction_id`) USING BTREE,
  CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '支付表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payments
-- ----------------------------
INSERT INTO `payments` VALUES (1, 1, 6498.00, 'PENDING', NULL, NULL, '2025-05-11 16:23:24', '2025-05-11 16:23:24');
INSERT INTO `payments` VALUES (2, 2, 3499.50, 'SUCCESS', 'ALIPAY', 'TXN1234567890', '2025-05-11 16:23:24', '2025-05-11 16:23:24');
INSERT INTO `payments` VALUES (3, 4, 11998.00, 'COMPLETED', '信用卡', 'txn_12345', '2025-05-12 14:43:18', '2025-05-12 14:43:23');
INSERT INTO `payments` VALUES (4, 5, 11998.00, '已支付', '信用卡', 'TXN9876543210', '2025-05-12 21:12:26', '2025-05-12 21:23:43');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（通常是加密后的）',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  UNIQUE INDEX `email`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'updatedTestUser', 'newSecurePassword456', 'updated.test@example.com', '2025-05-11 16:23:24', '2025-05-12 14:43:05');
INSERT INTO `users` VALUES (2, 'lisi', 'hashed_password_2', 'lisi@example.com', '2025-05-11 16:23:24', '2025-05-11 16:23:24');
INSERT INTO `users` VALUES (21, 'Miku', '123456', 'miku@example.com', '2025-05-12 20:23:25', '2025-05-12 20:29:32');
INSERT INTO `users` VALUES (22, 'TestUser', 'testPassword123', 'test.user@example.com', '2025-05-12 21:26:56', '2025-05-12 21:26:56');
INSERT INTO `users` VALUES (23, '赛文奥特曼', 'hashed_password_for_赛文奥特曼', 'ultra_seven@example.com', '2025-05-14 19:42:17', '2025-05-14 19:42:17');
INSERT INTO `users` VALUES (24, '贝利亚奥特曼', 'hashed_password_for_贝利亚奥特曼', 'belia_ultraman@example.com', '2025-05-14 19:45:09', '2025-05-14 19:45:09');

SET FOREIGN_KEY_CHECKS = 1;
