## mysql8.0版本限制IP远程连接

[TOC]

### 1、前言

`mysql`的连接，为了安全，一般都需要限制IP登录，无论是内网IP端，或者某些固定的IP；接下来让我们说下 如何限制IP登录；

我们这里使用的`Mysql8.0版本`，其他版本的命令或许有所不同

### 2、操作

#### 2.1  服务器登录mysql

~~~shell
[root@10 /]# mysql -u root -p
~~~

![1596418580189](mysql8.0%E9%99%90%E5%88%B6IP.assets/1596418580189.png)

> 输入你的密码

![1596418719833](mysql8.0%E9%99%90%E5%88%B6IP.assets/1596418719833.png)

> 切换到mysql库

~~~shell
mysql> use mysql;
~~~

![1596418778440](mysql8.0%E9%99%90%E5%88%B6IP.assets/1596418778440.png)

> 查看用户

~~~shell
mysql> select user,host from user;
~~~

![1596418858553](mysql8.0%E9%99%90%E5%88%B6IP.assets/1596418858553.png)

这里可以看到所有用户。

~~~mysql
user  host
root  localhost  		   # 代表只能本机连接，不能远程连接
root  %         		   # 代表允许所有IP远程连接
root  192.168.66.31         # 代表允许IP：192.168.66.31 远程连接
root  192.168.66.32         # 代表允许IP：192.168.66.32 远程连接
~~~

以上已经说明了所有设置登录账户权限的场景；

#### 2.2 允许所有人连接

~~~mysql
# 更新host为'%'
UPDATE `user` SET `Host`='%' WHERE `user`='root' AND `Host`='localhost';

# Mysql 8 赋权： *.*代表所有库权限，test.* 代表test库的权限
GRANT ALL ON *.* to 'root'@'%';

# 更新配置并使之生效
flush privileges;
~~~

其他Mysql版本：

~~~mysql
# 更新host为'%'
UPDATE `user` SET `Host`='%' WHERE `user`='root' AND `Host`='localhost';

# Mysql5.7 赋权
GRANT ALL PRIVILEGES ON *.* to 'root'@'%' IDENTIFIED by '密码' WITH GRANT OPTION;

# 更新配置并使之生效
flush privileges;
~~~

