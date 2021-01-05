## Docker搭建SVN服务器

[TOC]

### 1、前言

`Subversion(简称SVN)，是一个开放源代码的版本控制系统。可以存储代码文档等`

### 2、部署

> 搜索镜像
>
> 使用排名第2的elleflorio/svn-server镜像
>
> `这里面包含了 apache2(http server)、svn server 、svnadmin(PHP版本)代码仓库管理端，相比排名第一的garethflowers/svn-server，比较完整，可以使用http协议访问，否则只能使用svn协议访问。`

~~~shell
[root@AlexWong svn]# docker search svn
NAME                           DESCRIPTION                                     STARS     OFFICIAL   AUTOMATED
garethflowers/svn-server       A simple Subversion server, using `svnserve`.   64                   [OK]
elleflorio/svn-server          Lightweight Docker container running an SVN …   38
krisdavison/svn-server         A pre-configured SVN source control server.     26
......
~~~

> 新建容器
>
> /docker/svn/start.sh

~~~shell
# 新建目录
[root@AlexWong svn]# mkdir repo config svnadmin_data
[root@AlexWong svn]# pwd
/docker/volumes/svn
# 修改权限
[root@AlexWong svn]# sudo chmod -R a+w *
-----------------------------------------------------------
# 进入/docker/svn 目录
[root@AlexWong svn]# vim start.sh
docker stop svn-test
docker rm svn-test
docker run --restart always --name svn-test -d -p 3690:3690  -p 18080:80\
       -v /docker/volumes/svn:/tmp/svn elleflorio/svn-server
 [root@AlexWong svn]# sh start.sh
~~~

- `/docker/volumes/svn`为宿主机的文件目录，`/var/opt/svn`为容器内的文件目录
- `--restart always`命令可以实现容器在宿主机开机时自启动
- `-p 3690:3690表示将宿主机的3690端口映射到容器的3690端口，此端口为svn服务的默认端口，可以根据需要自行修改`
- `-p 18080:80表示将宿主机的18080端口映射到容器的80端口，此端口为apache服务的默认端口，可以根据需要自行修改`

> 进入容器

~~~shell
-----------------------------------1--------------------------------
# 先挂载临时目录
[root@AlexWong svn]# cat enter.sh
docker exec -it svn-test /bin/sh
[root@AlexWong svn]# sh enter.sh
# 查看仓库配置文件
/ # cat /etc/apache2/conf.d/dav_svn.conf
LoadModule dav_svn_module /usr/lib/apache2/mod_dav_svn.so
LoadModule authz_svn_module /usr/lib/apache2/mod_authz_svn.so
<Location /svn>
     DAV svn
     SVNParentPath /home/svn
     SVNListParentPath On
     AuthType Basic
     AuthName "Subversion Repository"
     AuthUserFile /etc/subversion/passwd
     AuthzSVNAccessFile /etc/subversion/subversion-access-control
     Require valid-user
--------------------------------------------------------------------
# 拷贝相关文件到tmp，同步到挂载点
/ # cp /etc/subversion/*  /tmp/svn/config
/ # cp /opt/svnadmin/data/* /tmp/svn/svnadmin_data
# 然后退出,此时相关配置文件已经同步到宿主机，退出更改start.sh
[root@AlexWong svn]# vim start.sh
# 3690是svn server的默认端口，80是apache的默认端口
docker stop svn-test
docker rm svn-test
docker run --restart always --name svn-test -d -p 3690:3690 -p 18080:80 \
       -v /docker/volumes/svn/repo:/home/svn \
       -v /docker/volumes/svn/config:/etc/subversion \
       -v /docker/volumes/svn/svnadmin_data:/opt/svnadmin/data elleflorio/svn-server
# 更改挂载点后，重启容器
[root@AlexWong svn]# sh start.sh
-----------------------------------2--------------------------------
# 进入容器，创建仓库
[root@AlexWong svn]# sh enter.sh

/ # mkdir -p /home/svn/myrep
/ # ls /home/svn/
myrep
/ # svnadmin create --pre-1.6-compatible /home/svn/myrep
-----------------------------------3--------------------------------
# 添加用户访问权限
/ # vi /etc/subversion/subversion-access-control
[groups]
[/]
* = r
admin = rw
# 添加admin的读写权限
-----------------------------------4--------------------------------
# 添加用户账号
/ # htpasswd -b /etc/subversion/passwd admin admin123
Adding password for user admin
# 退出
/ # exit
-----------------------------------5文件赋予权限--------------------------------
[root@AlexWong config]# sudo chmod -R a+w /docker/volumes/svn/config/*
[root@AlexWong config]# sudo chmod -R a+w /docker/volumes/svn/repo/*
[root@AlexWong config]# sudo chmod -R a+w /docker/volumes/svn/svnadmin_data/*
#不赋予权限，后面会有问题
-----------------------------------------------------------------------------

# 访问Apache HTTP Server
http://124.71.81.53:18080/
# 服务器需开放18080端口
~~~

![1609760623481](Docker%E6%90%AD%E5%BB%BAsvn.assets/1609760623481.png)

表示SVN服务正常启动。

在浏览器地址后面加上svn, 即http://<svn host>/svn，会弹出提示框输入用户名和密码，登录成功后出现如下界面

![1609760709507](Docker%E6%90%AD%E5%BB%BAsvn.assets/1609760709507.png)

![1609760727725](Docker%E6%90%AD%E5%BB%BAsvn.assets/1609760727725.png)

> 配置svnadmin(一个php写的svn管理工具)
>
> 访问 http://124.71.81.53:18080/svnadmin/

~~~
Error: Could not copy configuration file template. Require write permission (777) to "data" folder and all containing files.

#0 /opt/svnadmin/index.php(20): include_once()
#1 {main}
~~~

配置文件权限

~~~shell
[root@AlexWong svn]# chmod 777 /docker/volumes/svn/svnadmin_data/
~~~

然后刷新一下：

![1609761007648](Docker%E6%90%AD%E5%BB%BAsvn.assets/1609761007648.png)

按照 上面步骤里提到的/etc/apache2/conf.d/dav_svn.conf的内容去填写，并点击test按钮验证，如下图，然后点击Save Configuration

~~~shell
/ # cat /etc/apache2/conf.d/dav_svn.conf
LoadModule dav_svn_module /usr/lib/apache2/mod_dav_svn.so
LoadModule authz_svn_module /usr/lib/apache2/mod_authz_svn.so

<Location /svn>
     DAV svn
     SVNParentPath /home/svn
     SVNListParentPath On
     AuthType Basic
     AuthName "Subversion Repository"
     AuthUserFile /etc/subversion/passwd
     AuthzSVNAccessFile /etc/subversion/subversion-access-control
     Require valid-user

~~~

![1609761687439](Docker%E6%90%AD%E5%BB%BAsvn.assets/1609761687439.png)

然后保存配置，就可以使用admin登录了

![1609761771528](Docker%E6%90%AD%E5%BB%BAsvn.assets/1609761771528.png)

![1609761842234](Docker%E6%90%AD%E5%BB%BAsvn.assets/1609761842234.png)

![1609761869416](Docker%E6%90%AD%E5%BB%BAsvn.assets/1609761869416.png)

分配权限

![1609761928439](Docker%E6%90%AD%E5%BB%BAsvn.assets/1609761928439.png)

分配完之后，我们就可以在浏览器输入：http://124.71.81.53:18080/svn/project/访问

`使用TortoiseSVN访问：`

![1609762154561](Docker%E6%90%AD%E5%BB%BAsvn.assets/1609762154561.png)

`然后试下提交文件，提交时提示权限错误，检查是否遗漏了对repo目录下project的chmod的操作`

~~~shell
[root@AlexWong repo]# ll
total 8
drwxrwxrwx 6 root root 4096 Jan  4 19:34 myrep
drwxr-xr-x 6  100  101 4096 Jan  4 20:03 project
# 赋予权限
[root@AlexWong repo]# sudo chmod -R a+w *
[root@AlexWong repo]# ll
total 8
drwxrwxrwx 6 root root 4096 Jan  4 19:34 myrep
drwxrwxrwx 6  100  101 4096 Jan  4 20:03 project
~~~

![1609810714056](Docker%E6%90%AD%E5%BB%BAsvn.assets/1609810714056.png)

搭建成功。

### 3、总结

这是依赖于别人的镜像安装，后续可以从零开始创建自己的Docker镜像。后续有时间，就写一个从零开始Docker如何配置安装SVN。