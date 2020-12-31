## CentOS服务自启动

[TOC]

### 1、前言

有些服务比如`mysql、redis、samba、nginx等服务`，需要开机自启动，否则开机后，一个个操作也比较浪费时间。

### 2、操作

#### 2.1、编写脚本

注意：脚本执行的目录必须是绝对地址，否则找不到

~~~shell
[root@AlexWong docker]# vim init.sh
#!/bin/bash
# 机器重启时，服务需要自启动
# mysql
sh /docker/mysql/start.sh

# redis
sh /docker/redis/start.sh

# samba
sh /docker/samba/start.sh

# fastdfs
sh /docker/fastdfs/start.sh

# nginx
sh /docker/nginx/start.sh

~~~

#### 2.2、配置脚本

~~~shell
# 脚本移动到/etc/rc.d/init.d目录下
[root@AlexWong docker]# cp init.sh  /etc/rc.d/init.d
# 增加脚本的可执行权限
[root@AlexWong docker]# chmod +x  /etc/rc.d/init.d/init.sh
# 添加脚本到开机自动启动项目中
[root@AlexWong docker]# cd /etc/rc.d/init.d
[root@AlexWong init.d]# chkconfig --add init.sh
[root@AlexWong init.d]# chkconfig init.sh on
~~~

#### 2.3、问题

`在chkconfig添加自启动脚本，报错：`

~~~shell
[root@AlexWong init.d]# chkconfig --add init.sh
service init.sh does not support chkconfig
~~~

  添加下面两句到 #!/bin/bash 之后。

~~~shell
# chkconfig: 2345 10 90
# description: autostart server
~~~

  其中2345是默认启动级别，级别有0-6共7个级别。

　　等级0表示：表示关机 　　

　　等级1表示：单用户模式 　　

　　等级2表示：无网络连接的多用户命令行模式 　　

　　等级3表示：有网络连接的多用户命令行模式 　　

　　等级4表示：不可用 　　

　　等级5表示：带图形界面的多用户模式 　　

　　等级6表示：重新启动

​     10是启动优先级，90是停止优先级，优先级范围是0－100，数字越大，优先级越低。  

### 3、总结

> 所以最终脚本为：

~~~shell
[root@AlexWong init.d]# vim init.sh
#!/bin/bash
#chkconfig:2345 10 90
#description:autostart server

# 机器重启时，服务需要自启动

# mysql
sh /docker/mysql/start.sh

# redis
sh /docker/redis/start.sh

# samba
sh /docker/samba/start.sh

# fastdfs
sh /docker/fastdfs/start.sh

# nginx
sh /docker/nginx/start.sh
~~~

> 重启机器

~~~shell
[root@AlexWong ~]# docker ps
CONTAINER ID   IMAGE                COMMAND                  CREATED        STATUS                  PORTS                                                                   NAMES
242a11b8f1a2   mynginx   "/docker-entrypoint.…"   14 hours ago   Up 14 hours             80/tcp, 0.0.0.0:8090->8090/tcp                                            nginx-test
9e21d08f85bf   delron/fastdfs       "/usr/bin/start1.sh …"   14 hours ago   Up 14 hours             8080/tcp, 0.0.0.0:8898->8898/tcp, 22122/tcp, 0.0.0.0:25000->25000/tcp   storage-test
670c7924b2b0   delron/fastdfs       "/usr/bin/start1.sh …"   14 hours ago   Up 14 hours             8080/tcp, 8888/tcp, 23000/tcp, 0.0.0.0:22322->22322/tcp                 tracker-test
e2d783934fcb   mysamba   "/sbin/tini -- /usr/…"   14 hours ago   Up 14 hours (healthy)   0.0.0.0:139->139/tcp, 137-138/udp, 0.0.0.0:445->445/tcp                 samba-test
af8ee1fc0582   myredis   "docker-entrypoint.s…"   14 hours ago   Up 14 hours             0.0.0.0:6379->6379/tcp                                                  redis-test
9be53b84b7c1   mymysql   "docker-entrypoint.s…"   14 hours ago   Up 14 hours             0.0.0.0:3306->3306/tcp, 33060/tcp                                       mysql-test

~~~

`自启动测试通过`

