## docker安装mysql并挂载目录

[TOC]

### 1、下载镜像

~~~shell
[root /]# docker pull mysql
~~~

> 查看镜像

~~~shell
[root /]# docker images
REPOSITORY            TAG                 IMAGE ID            CREATED             SIZE
mysql                 latest              63921077800c        29 hours ago        674MB
~~~

### 2、运行容器

> start.sh

~~~shell
[root /]# docker stop mysql-test
[root /]# docker rm mysql-test
[root /]# docker run -itd --name mysql-test  -e MYSQL_ROOT_PASSWORD=123456 -p 3306:3306 mysql

# MYSQL_ROOT_PASSWORD 初始化密码为123456，建议使用复杂强度密码，这里只是为了测试
~~~
> 然后使用 `docker ps` 查看已运行容器：

~~~shell
[root /]#docker ps
[root /]# docker ps
CONTAINER ID        IMAGE                 COMMAND                  CREATED             STATUS              PORTS                               NAMES
9e5e665b3a36         mysql          "docker-entrypoint.s…"        3 hours ago         Up 3 hours          0.0.0.0:3306->3306/tcp, 33060/tcp   mysql-test
~~~

> 如果每次需要删除容器并删除镜像，需要`严格执行以下步骤`:

~~~shell
# 1、停止容器
[root /]#docker stop mysql-test
# 2、删除容器
[root /]#docker rm mysql-test
# 3、删除镜像
[root /]#docker rmi mysql
~~~

> 重启容器

~~~shell
[root /]#docker restart mysql-test
~~~

> 进入mysql容器

~~~shell
[root /]#docker exec -it mysql-test bash
~~~

> 登录mysql

~~~shell
root@9e5e665b3a36:/# mysql -uroot -p
Enter password:
# 输入密码 123456
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 10
Server version: 8.0.22 MySQL Community Server - GPL

Copyright (c) 2000, 2020, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> user mysql;
mysql> show databases;
~~~

> 使用root账户远程登录

![1607070909160](docker%E5%AE%89%E8%A3%85mysql%E5%B9%B6%E6%8C%82%E8%BD%BD%E7%9B%AE%E5%BD%95%20.assets/1607070909160.png)

### 3、挂载 Mounts

容器创建之后，最安全的做法是容器的数据，挂载在宿主机的目录下，这样修改数据都会和容器内同步。如果容器误删了，数据也不至于丢失。

> 新建目录

~~~shell
[root /]#mkdir -p /docker/volume/mysql/{data, conf}
# data 目录放置数据库数据
# conf 目录放置配置文件
~~~

~~~shell
# 从mysql-test容器内拷贝一份my.cnf
[root /] docker cp mysql-test:/etc/mysql/my.cnf /docker/volumes/mysql/conf
~~~

> 获取容器内对应目录位置

~~~shell
数据文件路径： /var/lib/mysql
配置文件路径：/etc/mysql/my.cnf
~~~

> 运行 start.sh

~~~shell
[root /]#sh start.sh
~~~

> start.sh

~~~shell
docker stop mysql-test
docker rm mysql-test
docker run -itd --name mysql-test -v /docker/volumes/mysql/data:/var/lib/mysql -v /docker/volumes/mysql/conf/my.cnf:/etc/mysql/my.cnf -e MYSQL_ROOT_PASSWORD=123456 -p 3306:3306 mysql
~~~

配置完毕，成功启动后，所有操作的数据库数据都会==同步宿主机器和容器==。

> 测试，新增test数据库

==宿主机器==：

~~~shell
[root@ /]# find /docker/volumes/mysql/data/test
/docker/volumes/mysql/data/test
~~~

==容器内==：

~~~shell
root@374c4a55019d:/# find /var/lib/mysql/test
/var/lib/mysql/test
~~~

==验证OK==。







