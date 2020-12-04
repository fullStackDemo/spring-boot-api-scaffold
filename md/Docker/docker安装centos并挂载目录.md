## docker安装centos并挂载目录

[TOC]

### 1、下载镜像

~~~shell
[root /]# docker pull centos
~~~

> 查看镜像

~~~shell
[root /]# docker images
REPOSITORY            TAG                 IMAGE ID            CREATED             SIZE
centos			    latest              63921077800c        29 hours ago        674MB
~~~



### 2、运行容器

> start.sh

~~~shell
[root /]# docker stop centos-test
[root /]# docker rm centos-test
[root /]# docker run -itd --name centos-test -p 9090:8080 centos
~~~
> 然后使用 `docker ps` 查看已运行容器：

~~~shell
[root /]#docker ps
[root /]# docker ps
CONTAINER ID        IMAGE                 COMMAND                  CREATED             STATUS              PORTS                               NAMES
bf8b4ec71684        master2011/mycentos   "/bin/bash"              8 hours ago         Up 8 hours          0.0.0.0:9090->8080/tcp              centos-test
~~~

> 如果每次需要删除容器并删除镜像，需要`严格执行以下步骤`:

~~~shell
# 1、停止容器
[root /]#docker stop centos-test
# 2、删除容器
[root /]#docker rm centos-test
# 3、删除镜像
[root /]#docker rmi centos
~~~

> 重启容器

~~~shell
[root /]#docker restart centos-test
~~~

> 进入容器

~~~shell
[root /]#docker exec -it centos-test bash
~~~

### 3、挂载 Mounts

容器创建之后，最安全的做法是容器的数据，挂载在宿主机的目录下，这样修改数据都会和容器内同步。如果容器误删了，数据也不至于丢失。

> 新建目录

~~~shell
[root /]#mkdir -p /home/project
# 该目录放置要运行的项目包
~~~

> 生成一个文本

~~~shell
[root /]# echo "test" > /home/project/test.txt
~~~

> 运行 start.sh

~~~shell
[root /]#sh start.sh
~~~

> start.sh

~~~shell
docker stop centos-test
docker rm centos-test
docker run -itd --name centos-test -p 9090:8080 -v /home/project:/home/project  centos
~~~

验证

==宿主机器==：

```shell
[root@ /]# find /home/project/test.txt
/home/project/test.txt
```

==容器内==：

```shell
root@c62ede406b6e:/# find /home/project/test.txt
/home/project/test.txt
```

==验证OK==。





