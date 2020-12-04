## docker安装nginx并挂载目录

[TOC]

### 1、下载镜像

~~~shell
[root /]# docker pull nginx
~~~

> 查看镜像

~~~shell
[root /]# docker images
REPOSITORY            TAG                 IMAGE ID            CREATED             SIZE
nginx    latest              63921077800c        29 hours ago        674MB
~~~



### 2、运行容器

> start.sh

~~~shell
[root /]# docker stop nginx-test
[root /]# docker rm nginx-test
[root /]# docker run --name nginx-test -d -p 9080:80 nginx
~~~
> 然后使用 `docker ps` 查看已运行容器：

~~~shell
[root /]#docker ps
[root /]# docker ps
CONTAINER ID        IMAGE                 COMMAND                  CREATED             STATUS              PORTS                               NAMES
4c98d10c8d80        nginx    "/docker-entrypoint.…"   1 hours ago         Up 1 hours          0.0.0.0:9080->80/tcp                nginx-test
~~~

> 如果每次需要删除容器并删除镜像，需要`严格执行以下步骤`:

~~~shell
# 1、停止容器
[root /]#docker stop nginx-test
# 2、删除容器
[root /]#docker rm nginx-test
# 3、删除镜像
[root /]#docker rmi nginx
~~~

> 重启容器

~~~shell
[root /]#docker restart nginx-test
~~~

> 进入容器

~~~shell
[root /]#docker exec -it nginx-test bash
~~~

### 3、挂载 Mounts

容器创建之后，最安全的做法是容器的数据，挂载在宿主机的目录下，这样修改数据都会和容器内同步。如果容器误删了，数据也不至于丢失。

> 新建目录

~~~shell
[root /]#mkdir -p /docker/volume/nginx/{conf,logs,html}

# conf 目录放置配置文件
# logs 目录放置日志
# html 目录放置静态资源
~~~

> conf目录下新增默认nginx.conf  和 conf.d文件夹

~~~nginx
# user  nobody;
worker_processes  4;

#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       mime.types;
    default_type  application/octet-stream;

    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    gzip  on;


    include ./conf.d/*.conf;

}
~~~

`include ./conf.d/*.conf;` 代表所有的配置文件将放在conf.d文件中；

> 新建一个default.conf

~~~shell
server {
    listen   80;
    server_name  localhost;

    location / {
   		root html;
   		index index.html;
    }
}
~~~

> 进入到html目录中

~~~shell
[root /html]# echo "test" > index.html 
~~~

> 获取容器内对应目录位置

~~~shell
html文件路径： /usr/share/nginx/html
配置文件路径：/etc/nginx/nginx.conf
日志存放路径：/var/log/nginx
~~~

> 运行 start.sh

~~~shell
[root /]#sh start.sh
~~~

> start.sh

~~~shell
docker stop nginx-test
docker rm nginx-test
docker run --name nginx-test -d -p 9999:80 -v /docker/volumes/nginx/conf/nginx.conf:/etc/nginx/nginx.conf -v /docker/volumes/nginx/html:/usr/share/nginx/html -v /docker/volumes/nginx/logs:/var/log/nginx nginx
~~~

容器重启以后，访问==9999==端口，就可以看到test了

![1607069354501](docker%E5%AE%89%E8%A3%85nginx%E5%B9%B6%E6%8C%82%E8%BD%BD%E7%9B%AE%E5%BD%95.assets/1607069354501.png)





