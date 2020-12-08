## 使用Dockerfile安装JDK8

[TOC]

### 1、下载 jdk8

~~~shell
mkdir -p /docker/jdk
~~~

下载[jdk](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)，并上传到服务器目录==/docker/jdk==；

![1607338198704](docker%E5%AE%89%E8%A3%85%E8%87%AA%E5%AE%9A%E4%B9%89jdk%E9%95%9C%E5%83%8F.assets/1607338198704.png)



### 2、创建 Dockerfile

> 创建目录

~~~shell
cd /docker/jdk
# 生成 Dockerfile
touch Dockerfile
# 生成镜像脚本
touch setup.sh
# 启动容器脚本
touch start.sh
# 进入容器脚本
touch enter.sh
~~~

> /docker/jdk/Dockerfile

~~~dockerfile
# 基础镜像为centos
FROM centos

# 维护者
MAINTAINER ALEX

# 添加jdk压缩包至 /usr/local 目录，压缩包会自动解压，解压后目录名称为jdk1.8.0_271
ADD jdk-8u271-linux-x64.tar.gz /usr/local/env/jdk/

# 配置JAVA_HOME环境变量
ENV JAVA_HOME /usr/local/env/jdk/jdk1.8.0_271/

# 将JAVA_HOME/bin 添加至PATH环境变量
ENV PATH $JAVA_HOME/bin:$PATH

# 安装vim
RUN yum -y install vim

# 安装ll
RUN echo "alias ll='ls $LS_OPTIONS -l'" >> ~/.bashrc
RUN source ~/.bashrc

~~~

> setup.sh 执行脚本

~~~shell
docker build -t jdk:1.8 .
# . 代表着当前上下文
~~~

~~~shell
[root@ jdk]# sh setup.sh
Sending build context to Docker daemon  143.1MB
Step 1/8 : FROM centos
 ---> 0d120b6ccaa8
Step 2/8 : MAINTAINER ALEX
 ---> Using cache
 ---> c713e2db9476
Step 3/8 : ADD jdk-8u271-linux-x64.tar.gz /usr/local/env/jdk/
 ---> Using cache
 ---> 2b44078a4757
Step 4/8 : ENV JAVA_HOME /usr/local/env/jdk/jdk1.8.0_271/
 ---> Using cache
 ---> 518acf43cd37
Step 5/8 : ENV PATH $JAVA_HOME/bin:$PATH
 ---> Using cache
 ---> da8791cd64c3
Step 6/8 : RUN yum -y install vim
 ---> Using cache
 ---> a4aa6cec7edf
Step 7/8 : RUN echo "alias ll='ls $LS_OPTIONS -l'" >> ~/.bashrc
 ---> Using cache
 ---> b2eb2cb33a02
Step 8/8 : RUN source ~/.bashrc
 ---> Using cache
 ---> cd096ee7d56c
Successfully built cd096ee7d56c
Successfully tagged jdk:1.8
~~~

> 查看生成的JDK镜像

~~~shell
[root@ jdk]# docker images
REPOSITORY            TAG                 IMAGE ID            CREATED             SIZE
jdk                   1.8                 cd096ee7d56c        2 hours ago         628MB
~~~

> 运行容器 start.sh

~~~shell
docker stop jdk-test
docker rm jdk-test
docker run -itd --name jdk-test jdk:1.8
~~~

> 进入容器 enter.sh

~~~shell
docker exec -it jdk-test /bin/bash
~~~

> 进入容器后

~~~shell
[root@842e12c3e67b /]# java -version
java version "1.8.0_271"
Java(TM) SE Runtime Environment (build 1.8.0_271-b09)
Java HotSpot(TM) 64-Bit Server VM (build 25.271-b09, mixed mode)
~~~

到此安装完毕；



