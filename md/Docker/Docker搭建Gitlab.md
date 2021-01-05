## Docker搭建Gitlab

[TOC]

### 1、前言

Gitlab也是版本管理

### 2、部署

> 创建数据卷文件目录

~~~shell
[root@AlexWong /]# mkdir -p /docker/volumesgitlab/{etc,log,opt}
~~~

> 创建容器
>
> start.sh

~~~shell
[root@AlexWong gitlab]# vim start.sh
docker stop gitlab-test
docker rm gitlab-test
docker run --restart always --name gitlab-test -d -p 9980:80 -p 9922:22 \
    -v /docker/volumes/gitlab/etc:/etc/gitlab \
    -v /docker/volumes/gitlab/log:/var/log/gitlab \
    -v /docker/volumes/gitlab/opt:/var/opt/gitlab \
    gitlab/gitlab-ce
[root@AlexWong gitlab]# sh start.sh
~~~

> 进入容器
>
> enter.sh

~~~shell
[root@AlexWong gitlab]# vim enter.sh
docker exec -it gitlab-test /bin/sh
[root@AlexWong gitlab]# sh enter.sh
~~~

> 修改gitlab.rb 

~~~shell
# gitlab访问地址
external_url 'http://124.71.181.253:9980'
# ssh主机ip
gitlab_rails['gitlab_ssh_host'] = '124.71.181.253'
# ssh连接端口
gitlab_rails['gitlab_shell_ssh_port'] = 9922
~~~



