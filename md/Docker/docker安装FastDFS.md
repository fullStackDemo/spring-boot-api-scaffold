## 如何使用Docker容器安装FastDFS

[TOC]



### 1、前言

之前有一篇文章，是在centos上从零搭建一个FastDFS分布式文件系统， 包括集群部署和java客户端连接。

这里就不再赘述相关理论知识；感兴趣，可进[传送门-如何从零搭建一个FastDFS分布式文件系统](https://blog.csdn.net/qq_26003101/article/details/111942403)；

通过以上文章可知，如果跨机器需要再部署一个FastDFS，似乎看起来有些许的麻烦，所以考虑使用Docker容器来部署一个FastDFS。

### 2、Docker部署

> 搜索已有的fastdfs镜像

~~~shell
[root@AlexWong fastdfs]# docker search fastdfs
NAME                           DESCRIPTION                                     STARS     OFFICIAL   AUTOMATED
season/fastdfs                 FastDFS                                         71
luhuiguo/fastdfs               FastDFS is an open source high performance d…   25                   [OK]
ygqygq2/fastdfs-nginx          整合了nginx的fastdfs                                24                   [OK]
morunchang/fastdfs             A FastDFS image                                 19
delron/fastdfs                                                                 12                 
......
~~~

列表按`starts`排列，我们这里选择`delron/fastdfs`，里面默认安装了`nginx`。

排名第一的`season/fastdfs`，最新版本默认没有安装==nginx==。

#### 2.1、 `安装tracker容器`

> `/docker/fastdfs/tracker/start.sh`
>
> 运行`tracker`跟踪服务，默认端口为`22122`，映射`/docker/volumes/fastdfs/tracker`以保留数据和日志

~~~shell
[root@AlexWong /]# mkdir -p /docker/fastdfs/tracker
[root@AlexWong /tracker]# cd /docker/fastdfs/tracker
# 创建宿主机器挂载点，保证容器数据可以持续化保存
[root@AlexWong /]# mkdir -p /docker/volumes/fastdfs/tracker
# 启动脚本
[root@AlexWong /tracker]# vim start.sh
docker stop tracker-test
docker rm tracker-test
# 运行tracker跟踪服务，默认端口为22122，映射`/docker/volumes/fastdfs/tracker`以保留数据和日志
docker run -itd --name tracker-test -p 22122:22122  \
	   -v /docker/volumes/fastdfs/tracker:/var/fdfs \
	   delron/fastdfs tracker
------------------------------------------------
# 启动tracker容器
[root@AlexWong /tracker]# sh start.sh
# 查看容器运行
[root@AlexWong tracker]# docker ps
CONTAINER ID   IMAGE                COMMAND                  CREATED          STATUS                PORTS                                                                   NAMES
407659b39e5e   delron/fastdfs       "/usr/bin/start1.sh …"   4 minutes ago    Up 4 minutes          8080/tcp, 8888/tcp, 23000/tcp, 0.0.0.0:22122->22122/tcp                 tracker-test
# 进入tracker容器
[root@AlexWong /tracker]# vim enter.sh
docker exec -it tracker-test /bin/bash
[root@AlexWong /tracker]# sh enter.sh
~~~

`22122`端口需要在公网安全组上开放端口。以便外部访问Tracker。如果无需外部访问可以不需要开放。

#### 2.2 、`安装storage容器`

> `/docker/fastdfs/storage/start.sh`
>
> 运行`storage`存储卷服务，默认端口为`23000`，映射`/docker/volumes/fastdfs/storage`以保留数据和日志
>
> storage中默认安装的nginx的端口是`8888`，位置在容器内`/usr/local/nginx/conf/nginx.conf`

~~~shell
[root@AlexWong /]# mkdir -p /docker/fastdfs/storage
[root@AlexWong /storage]# cd /docker/fastdfs/storage
# 创建宿主机器挂载点，保证容器数据可以持续化保存
[root@AlexWong /]# mkdir -p /docker/volumes/fastdfs/storage
# 启动脚本
[root@AlexWong /storage]# vim start.sh
docker stop storage-test
docker rm storage-test

# 运行storage存储服务, 绑定tracker跟踪服务的ip, 弹性公网IP 124.71.*.*(或者内网私有IP 192.168.*.*)
docker run -itd  --name storage-test -p 8888:8888 -p 23000:23000 \
       -e TRACKER_SERVER=弹性公网IP 124.71.*.*(或者内网私有IP 192.168.*.*):22122 \
       -v /docker/volumes/fastdfs/storage:/var/fdfs delron/fastdfs storage
------------------------------------------------
# 启动storage容器
[root@AlexWong /storage]# sh start.sh
# 查看容器运行
[root@AlexWong storage]# docker ps
CONTAINER ID   IMAGE                COMMAND                  CREATED          STATUS                PORTS                                                                   NAMES
407659b39e5e   delron/fastdfs       "/usr/bin/start1.sh …"   14 minutes ago   Up 14 minutes         8080/tcp, 8888/tcp, 23000/tcp, 0.0.0.0:22122->22122/tcp                 tracker-test
ead353c90fd5   delron/fastdfs       "/usr/bin/start1.sh …"   25 minutes ago   Up 25 minutes         8080/tcp, 0.0.0.0:8888->8888/tcp, 22122/tcp, 0.0.0.0:23000->23000/tcp   storage-test
# 进入storage容器
[root@AlexWong /storage]# vim enter.sh
docker exec -it storage-test /bin/bash
[root@AlexWong /storage]# sh enter.sh
[root@ead353c90fd5 nginx-1.12.2]# cat /usr/local/nginx/conf/nginx.conf
....
  server {
        listen       8888;
        server_name  localhost;
        location ~/group[0-9]/ {
            ngx_fastdfs_module;
        }
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root html;
        }
    }
.....
# 如果想更改nginx的端口，可以在这里修改

# 注意
# TRACKER_SERVER=内网私有IP 192.168.*.*:22122 时，外部无法测试访问strorage进行上传下载，只能在内部进行测试
# 这点在部署测试环境和生产环境时，要注意区分
~~~

`23000 8888`端口需要在公网安全组上开放端口。以便外部访问Storage。如果无需外部访问可以不需要开放。

#### 2.3、部署顺序

`先部署tracker，再部署storage`

所以可以写一个启动脚本

> `/docker/fastdfs/start.sh`

~~~shell
[root@AlexWong fastdfs]# pwd
/docker/fastdfs
[root@AlexWong fastdfs]# vim start.sh
sh ./tracker/start.sh
sh ./storage/start.sh
~~~

#### 2.4、Storage测试

进入Storage容器进行上传文件测试。

~~~shell
[root@AlexWong /storage]# sh enter.sh
[root@ead353c90fd5 nginx-1.12.2]# /usr/bin/fdfs_upload_file /etc/fdfs/client.conf README
group1/M00/00/00/rBEABl_r6uaAPq37AAAAMT6WPfM6698716
~~~

此时将文件已上传至文件系统，并在执行该语句后返回图片存储的URL。

所以此刻如果在已经开放了`8888`端口，就可以访问`http://你的服务器IP:8888/group1/M00/00/00/rBEABl_r6uaAPq37AAAAMT6WPfM6698716`下载这个文件

测试通过

> 我们退出容器，到宿主的挂载点查看

~~~shell
[root@AlexWong storage]# ll /docker/volumes/fastdfs/storage/data/00/00/
-rw-r--r-- 1 root root   49 Dec 30 10:50 rBEABl_r6uaAPq37AAAAMT6WPfM6698716
~~~

#### 2.5、总结

到此为止一个`fastDFS`分布式文件系统搭建完毕。至于关于 端口`22122、23000、8888是否要需要开放`，就要视情况而定，如果是生产环境，为了安全，只需要开放一个java客户端的端口和一个其他的nginx服务器端口，`22122、23000、8888`全部不开放。

> java client 相关配置

> 测试环境配置
>
> 这时候：22122、23000需要开发端口。
>
> 启动Stroage容器时，-e TRACKER_SERVER=弹性公网IP 124.71.0.0:22122

~~~properties
fastdfs.connect_timeout_in_seconds = 60
fastdfs.network_timeout_in_seconds = 60
fastdfs.charset = UTF-8
fastdfs.http_tracker_http_port = 8080
fastdfs.tracker_servers = 124.71.*.*:22122
~~~

> 生产环境配置
>
> 这时候：22122、23000不需要开发端口。
>
> 启动Stroage容器时，-e TRACKER_SERVER=内网网IP 192.168.0.0:22122

~~~properties
fastdfs.connect_timeout_in_seconds = 60
fastdfs.network_timeout_in_seconds = 60
fastdfs.charset = UTF-8
fastdfs.http_tracker_http_port = 8080
fastdfs.tracker_servers = 127.0.0.1:22122
~~~

