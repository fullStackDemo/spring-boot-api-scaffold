## Docker-compose

[TOC]

### 1、前言

Docker-Compose项目是Docker官方的开源项目，负责实现对Docker容器进行统一编排。

可以统一对同一个项目所需要的多个容器进行管理发布，避免了一个个去发布的繁琐过程。

一个工程当中可包含多个服务，每个服务中定义了容器运行的镜像，参数，依赖。一个服务当中可包括多个容器实例。

`比如：现在有一个项目使用，包括nginx、spring boot 和 mysql`。

一般部署：分别部署三个容器，nginx容器，jdk容器部署spring boot的jar包和mysql容器。然后每次更新时再一个个容器进行更新发布。

有了docker-compose后，就不必再一个个进行管理。

Compose允许用户通过一个单独的docker-compose.yml模板文件（YAML 格式）来定义一组相关联的应用容器为一个项目。

> 常用命令

~~~shell
# FILE指定Compose模板文件，默认为docker-compose.yml
docker-compose -f FILENAME
# 指定项目名称，默认使用当前所在目录为项目
docker-compose -f projectName
# 运行服务容器
docker-compose up
# 后台运行服务容器
docker-compose up -d
# 列出项目中所有的容器
docker-compose ps
# 启动存在的容器
docker-compose start
# 停止正在运行的容器
docker-compose stop
# 用移除所有容器以及网络相关
docker-compose down
# 查看服务容器的输出
docker-compose logs
# 重启项目中的服务
docker-compose restart
...
~~~



### 2、安装

~~~shell
[root@ /]# sudo curl -L https://github.com/docker/compose/releases/download/1.16.1/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100   633  100   633    0     0    846      0 --:--:-- --:--:-- --:--:--   846
100 8648k  100 8648k    0     0  13449      0  0:10:58  0:10:58 --:--:-- 11572
[root@ /]# # sudo chmod +x /usr/local/bin/docker-compose
[root@ /]# # docker-compose --version
docker-compose version 1.16.1, build 6d1ac21
~~~

如果访问太慢，可以换：

~~~shell
sudo curl -L https://get.daocloud.io/docker/compose/releases/download/1.25.1/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
~~~

### 3、配置

> 文件目录结构

~~~shell
[root@ test]# pwd
/home/test
[root@ test]# tree
.
├── docker-compose.yml
├── mysql
│   └── data
│       ├── auto.cnf
│       ├── ...
│       ├── sys
│       │   └── sys_config.ibd
│       ├── test
│       │   └── sys_user.ibd
│       ├── undo_001
│       ├── undo_002
│       └── testdb
│           ├── ...
├── nginx
│   ├── conf
│   │   ├── conf.d
│   │   │   ├── default.conf
│   │   │   └── test.conf
│   │   └── nginx.conf
│   ├── html
│   │   ├── dist.tar.gz
│   │   ├── favicon.ico
│   │   ├── index.html
│   │   ├── report.html
│   └── logs
│       ├── access.log
│       └── error.log
├── server
│   ├── logs
│   ├── service.sh
│   └── test-01.jar
~~~

`Docker-Compose标准模板文件应该包含version、services、networks 三大部分，最关键的是services和networks两个部分`

> 初次配置

~~~yaml
version: "3"
services:
  # WEB
  web:
    # 引用的镜像
    image: nginx
    # 映射端口, 宿主机：容器内
    ports:
      - 8090:8090
    # 挂载目录, 宿主机：容器内
    volumes:
      - /home/test/nginx/conf/nginx.conf:/etc/nginx/nginx.conf
      - /home/test/nginx/conf/conf.d:/etc/nginx/conf.d
      - /home/test/nginx/html:/home/web
      - /home/test/nginx/logs:/var/log/nginx
    # 链接到其它服务中的容器，服务名称Container-name:服务别名Alias
    # 比如nginx proxy_pass需要转发到server的8080，则写 proxy_pass http://serverHost:8080;
    links:
      - "server:serverHost"
    # 设置网络模式, 引用同一个network
    networks:
      - backend
    # 解决容器的依赖、启动先后的问题，优先启动mysql、server
    depends_on:
      - mysql
      - server

  # 数据库
  mysql:
    # 镜像
    image: mysql
    # 映射端口, 宿主机：容器内
    ports:
      - 3306:3306
    # 挂载目录, 宿主机：容器内
    volumes:
      - /home/test/mysql/data:/var/lib/mysql
    # 设置网络模式, 引用同一个network
    networks:
      - backend

  # server
  server:
    # 引用的镜像
    image: ascdc/jdk8
    # 映射端口, 宿主机：容器内
    ports:
      - 8080:8080
    # 挂载目录, 宿主机：容器内
    volumes:
      - /home/test/server/service.sh:/home/service.sh
      - /home/test/server/logs:/home/logs
      - /home/test/server/test-01.jar:/home/test-01.jar
    # 总是重启
    restart: "always"
    # 启动容器后执行的命令
    entrypoint: java -jar /home/test-01.jar
    # 链接到其它服务中的容器，服务名称Container-name:服务别名Alias
    # 比如server用到mysql3306端口，则数据地址写jdbc:mysql://mysql:3306/testdb
    links:
      - mysql:mysql
    # 设置网络模式, 引用同一个network
    networks:
      - backend
    # 等同于docker run -it, 否则执行完会自动退出
    stdin_open: true
    tty: true

# 设置网络模式
networks:
  backend:
    driver: bridge
~~~

> 需要注意的是容器之间的相互连接：

`比如nginx proxy_pass需要转发到server的8080，则写 proxy_pass http://serverHost:8080`

~~~nginx
server {
    listen       8090;
    server_name  localhost;
    
    location / {
    	try_files $uri $uri/ @router;
        root /home/web;
        index index.html;
    }

    location @router {
        rewrite ^.*$ /index.html last;
    }

    location ~ ^/(api) {
        # 注意这里
	   proxy_pass http://serverHost:8080;
    }
}
~~~



`比如server用到mysql3306端口，则数据地址写jdbc:mysql://mysql:3306/testdb`

~~~properties
spring.datasource.primarydatasource.url=jdbc:mysql://mysql:3306/testdb?serverTimezone=GMT%2B8&useUnicode=true&nullCatalogMeansCurrent=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true
~~~

`由于depends_on的设定，我们的三个容器的顺序将是mysql—>server—>web。`

~~~shell
[root@ home]# docker-compose -p test up
Creating network "test_backend" with driver "bridge"
Creating test_mysql_1 ...
Creating test_mysql_1 ... done
Creating test_server_1 ...
Creating test_server_1 ... done
Creating test_web_1 ...
Creating test_web_1 ... done
....
[root@ home]# docker-compose ps
         Name                       Command               State                 Ports
---------------------------------------------------------------------------------------------------
test_mysql_1    docker-entrypoint.sh mysqld      Up      0.0.0.0:3306->3306/tcp, 33060/tcp
test_server_1   java -jar /home/test-0 ...       Up      0.0.0.0:8080->8080/tcp
test_web_1      /docker-entrypoint.sh ngin ...   Up      80/tcp, 0.0.0.0:8090->8090/tcp
# 到此为止说明启动成功
# 网络
[root@ home]# docker network ls
NETWORK ID          NAME                     DRIVER              SCOPE
3b5aa26dba14        test_backfont  			bridge              local
~~~

### 4、优化

`server服务端应用：`

所有的应用代码应该都是无状态，不应该牵扯太多外部配置。

每次都在配置文件里写明`jdbc:mysql://mysql:3306/testdb`，比较麻烦，`可以考虑增加全局变量spring.datasource.host`

~~~properties
spring.datasource.host=127.0.0.1
spring.datasource.primarydatasource.url=jdbc:mysql://${spring.datasource.host}:3306/testdb
~~~

然后再`docker run -e spring.datasource.host=mysql`

~~~yaml
  # server
  server:
    # 引用的镜像
    image: ascdc/jdk8
    # 映射端口, 宿主机：容器内
    ports:
      - 8080:8080
    # 环境变量
    # -e spring.datasource.host=mysql
    environment:
      spring.datasource.host: mysql
    # 挂载目录, 宿主机：容器内
    volumes:
      - /home/test/server/service.sh:/home/service.sh
      - /home/test/server/logs:/home/logs
      - /home/test/server/test-01.jar:/home/test-01.jar
    # 总是重启
    restart: "always"
    # 启动容器后执行的命令
    entrypoint: java -jar /home/test-01.jar
    # 链接到其它服务中的容器，服务名称Container-name:服务别名Alias
    # 比如server用到mysql3306端口，则数据地址写jdbc:mysql://mysql:3306/testdb
    #links:
    #  - mysql:mysql
    # 设置网络模式, 引用同一个network
    networks:
      - backend
    # 等同于docker run -it, 否则执行完会自动退出
    stdin_open: true
    tty: true
~~~

外层增加环境变量后，会覆盖默认变量。这样代码就可以原有的方式进行编码，更加灵活。部署也会变得更加灵活。

