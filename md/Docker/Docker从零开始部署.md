## Docker 从零部署应用

[TOC]

### 1、Docker 安装

> 使用官方安装脚本自动安装

~~~shell
curl -fsSL https://get.docker.com | bash -s docker --mirror Aliyun
~~~

> 启用Docker

~~~shell
[root@test2 /]# sudo systemctl start docker
~~~

> 重启docker服务

~~~shell
[root@test2 /]# sudo systemctl restart docker
~~~

> 停止docker服务

~~~shell
[root@test2 /]# sudo systemctl stop docker
~~~

> 查看状态

~~~shell
[root@test2 /]# service docker status
~~~

### 2、Docker 使用

#### 2.1、centos

Docker 允许你在容器内运行应用程序， 使用 **docker run** 命令来在容器内运行一个应用程序

> 启动 ubuntu 容器

~~~shell
[root@test2 /]# docker run -itd --name centos-test ubuntu /bin/bash
~~~

各个参数解析：

- **docker:** Docker 的二进制执行文件。
- **run:** 与前面的 docker 组合来运行一个容器。
- **centos** 指定要运行的镜像，Docker 首先从本地主机上查找镜像是否存在，如果不存在，Docker 就会从镜像仓库 Docker Hub 下载公共镜像。
- **/bin/bash:** 运行交互式的容器
- **-t:** 在新容器内指定一个伪终端或终端。
- **-i:** 允许你对容器内的标准输入 (STDIN) 进行交互。
- **-d:** 后台模式运行
- **--name**: 命令当前容器，后续可以通过指定名字运行操作容器

> 查看所有容器

~~~shell
[root@test2 ubuntu]# docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                  NAMES
36a66ae36ff5        centos        "/bin/bash"              26 minutes ago      Up 26 minutes                              centos-test

~~~
输出详情介绍：

**CONTAINER ID:** 容器 ID。

**IMAGE:** 使用的镜像。

**COMMAND:** 启动容器时运行的命令。

**CREATED:** 容器的创建时间。

**STATUS:** 容器状态。

状态有7种：

- created（已创建）
- restarting（重启中）
- running 或 Up（运行中）
- removing（迁移中）
- paused（暂停）
- exited（停止）
- dead（死亡）

**PORTS:** 容器的端口信息和使用的连接类型（tcp\udp）。

**NAMES:** 自动分配的容器名称

> 进入 ubuntu 容器

~~~shell
[root@test2 ubuntu]# docker exec -it centos-test /bin/bash
~~~

输入 **exit** 退出容器；

> 封装脚本如下

> /docker/centos/start.sh

~~~sh
# 重启，先停止容器，才能删除，然后新建容器，也可以直接 docker restart centos-test 直接重启
docker stop centos-test
docker rm centos-test
docker run -itd --name centos-test centos /bin/bash
~~~

> /docker/centos/enter.sh

~~~sh
# PID=$(docker ps | awk '/centos*/ {print $1}')
# echo $PID
# docker exec -it $PID /bin/bash
docker exec -it centos-test /bin/bash
~~~

#### 2.2、nginx

> 安装镜像

~~~shell
[root@test2 /]# docker pull nginx:latest
~~~

> 查看镜像

~~~shell
[root@test2 /]# docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
nginx               latest              bc9a0695f571        29 hours ago        133MB
ubuntu              latest               9b9cb95443b5        4 years ago         137MB
~~~

> 运行容器

~~~shell
[root@test2 /]# docker run --name nginx-test -d -p 9080:80 nginx
~~~

参数说明：

- **--name nginx-test**：容器名称。
- **-p 8080:80**： 端口进行映射，将本地 8080 端口映射到容器内部的 80 端口。
- **-d nginx**： 设置容器在在后台一直运行。

![1606370178250](Docker%E4%BB%8E%E9%9B%B6%E5%BC%80%E5%A7%8B%E9%83%A8%E7%BD%B2.assets/1606370178250.png)