## Docker Compose

[TOC]

### 1、前言

前面我们使用`Docker`容器去部署的时候，比如一个正常的网站项目，包括`nginx、mysql、redis和java工程`，这些关联的应用需要一个个部署，显得异常繁琐，维护起来也是很麻烦。

使用 `Docker Compose` 可以轻松、高效的管理容器，它是一个用于定义和运行多容器 Docker 的应用程序工具。

通过 Compose，您可以使用 YML 文件来配置应用程序需要的所有服务。然后，使用一个命令，就可以从 YML 文件配置中创建并启动所有服务。

### 2、安装

~~~shell
# 下载
[root@AlexWong /]# sudo curl -L https://github.com/docker/compose/releases/download/1.21.2/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
# 添加可执行权限
[root@AlexWong /]# sudo chmod +x /usr/local/bin/docker-compose
# 查看版本
[root@AlexWong /]# docker-compose --version
docker-compose version 1.21.2, build a133471
~~~

### 3、部署

