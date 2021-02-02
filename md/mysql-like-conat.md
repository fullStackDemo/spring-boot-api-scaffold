~~~mysql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `age` int(11) NULL DEFAULT NULL,
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (8, 'test1飞龙', 11, '2021-01-29 09:52:58');
INSERT INTO `user` VALUES (9, 'test2梦魇', 12, '2021-01-29 09:52:41');
INSERT INTO `user` VALUES (10, 'test3龙狼', 13, '2021-01-29 09:52:34');
INSERT INTO `user` VALUES (11, 'test4狼人', 14, '2021-01-29 09:53:09');
INSERT INTO `user` VALUES (12, 'test5龙文', 15, '2021-01-29 09:53:04');
INSERT INTO `user` VALUES (13, 'test1', 11, '2021-01-29 09:53:12');
INSERT INTO `user` VALUES (14, 'test2', 12, '2021-01-29 09:53:12');
INSERT INTO `user` VALUES (15, 'test3', 13, '2021-01-29 09:53:12');
INSERT INTO `user` VALUES (16, 'test4', 14, '2021-01-29 09:53:12');
INSERT INTO `user` VALUES (17, 'test5', 15, '2021-01-29 09:53:12');

SET FOREIGN_KEY_CHECKS = 1;
~~~

> `like '%关键字%' `
>
> %：表示任意0个或多个字符。可匹配任意类型和长度的字符，有些情况下若是中文，请使用两个百 分号（%%）表示

~~~mysql
SELECT
	* 
FROM
	`user` 
WHERE
`name` LIKE '%2%' 
OR age LIKE '%3%'

-----------------------------------
# 查询name中带2或者age带3的数据
id	name	 age	create_time
9	test2梦魇	12	   2021-01-29 09:52:41
10	test3龙狼	13	   2021-01-29 09:52:34
14	test2	 12	    2021-01-29 09:53:12
15	test3	 13		2021-01-29 09:53:12

~~~

~~~mysql
# 扩展写法 LiKE CONCAT
如果多个字段或判断，写起来不是很方便, 如果是同一个关键字，可以使用 LiKE CONCAT
SELECT
	* 
FROM
	`user` 
WHERE
	CONCAT( NAME, age ) LIKE CONCAT( '%', '2', '%' )

#结果：
id	name	age	create_time
9	test2梦魇	12	2021-01-29 09:52:41
14	test2	12	2021-01-29 09:53:12
--------------------------------------------------------------------------
# 请注意，以上写法，如果字段中存在null，则无法正确匹配
# 更新id=14的age=null
UPDATE `user` SET age=NULL WHERE name='test2';
SELECT
	* 
FROM
	`user` 
WHERE
	CONCAT( NAME, age ) LIKE CONCAT( '%', '2', '%' )
#结果：
id	name	age	create_time
9	test2梦魇	12	2021-01-29 09:52:41
--------------------------------------------------------------------------
# 解决方案1
# 增加 IFNULL
SELECT
	* 
FROM
	`user` 
WHERE
	CONCAT( IFNULL(name, ''), IFNULL(age, '') ) LIKE CONCAT( '%', '2', '%' )
#结果：
id	name	  age	   create_time
9	test2梦魇	 12	  2021-01-29 09:52:41
14	test2		   2021-01-29 10:34:36
--------------------------------------------------------------------------
# 解决方案2
SELECT
	* 
FROM
	`user` 
WHERE
	CONCAT( `name` ) LIKE CONCAT( '%', '2', '%' )
	or
	CONCAT( age) LIKE CONCAT( '%', '2', '%' )
	
#结果：
id	name	  age	   create_time
9	test2梦魇	 12	  2021-01-29 09:52:41
14	test2		   2021-01-29 10:34:36
~~~

> `like '%关键字%关键字%' `
>
> 同时匹配多个关键字

~~~mysql
SELECT
	* 
FROM
	`user` 
WHERE
	`name` LIKE '%2%梦%'
	
# 结果
id	name	age	create_time
9	test2梦魇	12	2021-01-29 09:52:41
~~~

>`_： 表示任意单个字符。匹配单个任意字符，限制表达式的字符长度`

~~~mysql
# 1个_
SELECT
	* 
FROM
	`user` 
WHERE
	`name` LIKE 'test_'
	
id	name	age	create_time
13	test1	11	2021-01-29 09:53:12
14	test2		2021-01-29 10:34:36
15	test3	13	2021-01-29 09:53:12
16	test4	14	2021-01-29 09:53:12
17	test5	15	2021-01-29 11:20:54
--------------------------------------------
# 3个_
SELECT
	* 
FROM
	`user` 
WHERE
	`name` LIKE 'test___'
	
id	name	age	create_time
8	test1飞龙	11	2021-01-29 09:52:58
9	test2梦魇	12	2021-01-29 09:52:41
10	test3龙狼	13	2021-01-29 09:52:34
11	test4狼人	14	2021-01-29 09:53:09
12	test5龙文	15	2021-01-29 09:53:04

~~~



