## 云服务器无法访问开放IP和关闭防火墙后Docker出现iptables问题

[TOC]

### 1、问题描述

之前购买过云服务器后，不仅加了安全组，也开启了防火墙。

> 安全组 VS 防火墙

安全组作用于虚拟机网卡，而防火墙则是在VPC路由器上，保护整个VPC；
安全组是分布式部署的，在每个计算节点上都会存在，而防火墙是集中式，把VPC路由器变成了防火墙；
安全组是白名单机制，仅支持允许策略，防火墙可以自定义规则行为是允许还是拒绝；
安全组规则根据配置顺序来定优先级，防火墙则可以自定义优先级；
安全组规则的匹配内容包括源IP、源端口、协议，防火墙则包括五元组以及TCP flag、ICMP Type、报文状态。

> 碰到的问题如下：

举例端口 `61504`：

首先开放安全组中的`61504`端口，不限制任何IP访问。

当使用docker容器启动java工程使用 `-p 61504:61504`来映射IP，这个时候外网访问是通的。

当直接把`java`工程包部署在宿主机时，外网却无法访问。

### 2、解决

#### 2.1、关闭防火墙

> 关闭防火墙

~~~shell
[root@AlexWong fastdfs]# systemctl stop firewalld
[root@AlexWong fastdfs]# telnet 124.71.81.53 61504
Trying 124.71.81.53...
Connected to 124.71.81.53.
~~~

但是关闭防火墙后，Docker部署的FastDFS应用又出现以下错误

~~~shell
# 启动container的时候出现iptables: No chain/target/match by that name
348f6ed9d3be2ea8983ca428481fbc4be05c5b4e9c58882e7ffafb488ebbae82
docker: Error response from daemon: driver failed programming external connectivity on endpoint storage-test (ae14346ea9dd60809350abf969ef5453dd8815cf0ca30a0a8dedaf5c53a535c9):  (iptables failed: iptables --wait -t nat -A DOCKER -p tcp -d 0/0 --dport 23000 -j DNAT --to-destination 172.17.0.2:23000 ! -i docker0: iptables: No chain/target/match by that name.
 (exit status 1))
~~~

` iptables: No chain/target/match by that name，是跟iptables有关`

`大概原因是刚开始时是启动了防火墙，然后docker server启动的时候，docker netword是需要操作iptable chain对容器进行网络配置。后来我们关闭了防火墙，docker network无法对新container进行网络配置，然后就会报这个错误`

一般，Docker安装完成后，将默认在宿主机系统上增加一些iptables规则，以用于Docker容器和容器之间以及和外界的通信

所以我们考虑重启Docker或者重启机器。

~~~shell
[root@AlexWong fastdfs]# systemctl restart docker
~~~

> 重启后，启动容器不会报错
>
> 但是又出现了Storage的以下错误

> 错误1：

~~~shell
[root@AlexWong fastdfs]# docker logs storage-test
ngx_http_fastdfs_set pid=9
try to start the storage node...
tail: cannot open '/var/fdfs/logs/storaged.log' for reading: No such file or directory

# 这种情况需要先删除老的在后台的容器
[root@AlexWong fastdfs]# docker ps -a
CONTAINER ID   IMAGE                COMMAND                  CREATED         STATUS                       PORTS                                                     NAMES
b04029446be0   delron/fastdfs       "/usr/bin/start1.sh …"   8 minutes ago   Exited (1) 8 minutes ago                                                               storage-test
[root@AlexWong fastdfs]# docker rm storage-test
~~~
> 错误2：
~~~shell
[2020-12-30 07:15:31] ERROR - file: tracker_proto.c, line: 48, server: 124.71.81.53:22122, response status 22 != 0
[2020-12-30 07:15:31] ERROR - file: storage_ip_changed_dealer.c, line: 114, tracker server 124.71.81.53:22122, recv data fail or response status != 0, errno: 22, error info: Invalid argument
~~~

`这时候考虑也许是历史storage和tracker的问题，所以对两个挂载点文件夹里的文件都全部删除。然后重启了就好了`。

#### 2.2、开启防火墙

安全组合防火墙是相辅相成，不一定是开了安全组，就一定不能开放防火墙。所以我们测试同时开启是怎么配置，才能不影响访问。

> 再次开启防火墙

~~~shell
# 开启防火墙
[root@AlexWong fastdfs]# systemctl start firewalld
# 查看开放的端口
[root@AlexWong fastdfs]# firewall-cmd --list-ports
# 开放端口(修改后需要重启防火墙方可生效)
[root@AlexWong fastdfs]# firewall-cmd --add-port=61504/tcp --permanent
success
# 重启防火墙(每次修改都要重启)
[root@AlexWong fastdfs]# firewall-cmd --reload
success
# 查看开放的端口
[root@AlexWong fastdfs]# firewall-cmd --list-ports
61504/tcp

~~~

不过一般情况，开启安全组后，不需要再开启服务器内部的防火墙。

#### 2.3、扩展知识

服务器有两种防火墙

- iptables
- firewalld

这两个防火墙只能开一个，也可以两个都不开，不开就是全部支持访问的意思。`我这台服务器默认开启firewalld`

> iptables

~~~shell
# 先查看有没有安装 
systemctl status iptables.service

# 安装iptables:
yum install iptables

# 安装iptables-services:
yum install iptables-services

# 开启防火墙：
systemctl start iptables.service

# 关闭防火墙： 
systemctl stop iptables.service

# 查看防火墙状态： 
systemctl status iptables.service

# 设置开机启动：
systemctl enable iptables.service

# 禁用开机启动：
systemctl disable iptables.service

# 查看filter表的几条链规则(INPUT链可以看出开放了哪些端口)： 
iptables -L -n

# 清除防火墙所有规则：
iptables -F
iptables -X
iptables -Z

# 给INPUT链添加规则（开放8080端口）： 
iptables -I INPUT -p tcp --dport 8080 -j ACCEPT

# 查找规则所在行号： 
iptables -L INPUT --line-numbers -n

# 根据行号删除过滤规则（关闭8080端口）： 
iptables -D INPUT 1
~~~

> firewall

~~~~shell
# 查看防火墙状态： 
systemctl status firewalld

# 开启防火墙： 
systemctl start firewalld

# 关闭防火墙：
systemctl stop firewalld

# 设置开机启动：
systemctl enable firewalld

# 禁用开机启动：
systemctl disable firewalld

# 重启防火墙（每次修改都要重启）：
firewall-cmd --reload

# 开放端口（修改后需要重启防火墙方可生效）：
firewall-cmd --add-port=80/tcp –permanent

# 关闭端口：
firewall-cmd --remove-port=8080/tcp --permanent

# 查看开放的端口： 
firewall-cmd --list-ports
~~~~

### 3、疑问

`为什么Docker network不需要被防火墙firewall的规则限制，可以直接越过直接访问成功。而宿主机部署的服务，却需要开启防火墙端口呢？`

查阅了一些资料，下面说法还是比较靠谱的

> 这不是docker绕过系统防火墙操作iptables，原因是docker网络是基于宿主机的，本身就只支持iptables。像firewalld之类的是后来在iptables上封装的，所以docker的网络规则会直接无视非iptables的防火墙，docker想做容器的网络操作也只能这么搞，他没法去动iptables这种系统防火墙，只能自己添加一个DOCKER规则。默认每次启动都会改掉防火墙。

~~~shell
[root@AlexWong fastdfs]# iptables --list
Chain INPUT (policy ACCEPT)
target     prot opt source               destination

Chain FORWARD (policy DROP)
target     prot opt source               destination
DOCKER-USER  all  --  anywhere             anywhere
DOCKER-ISOLATION-STAGE-1  all  --  anywhere             anywhere
ACCEPT     all  --  anywhere             anywhere             ctstate RELATED,ESTABLISHED
DOCKER     all  --  anywhere             anywhere
ACCEPT     all  --  anywhere             anywhere
ACCEPT     all  --  anywhere             anywhere

Chain OUTPUT (policy ACCEPT)
target     prot opt source               destination

Chain DOCKER (1 references)
target     prot opt source               destination
ACCEPT     tcp  --  anywhere             172.17.0.2           tcp dpt:22122
ACCEPT     tcp  --  anywhere             172.17.0.3           tcp dpt:inovaport1
ACCEPT     tcp  --  anywhere             172.17.0.3           tcp dpt:ddi-tcp-1

Chain DOCKER-ISOLATION-STAGE-1 (1 references)
target     prot opt source               destination
DOCKER-ISOLATION-STAGE-2  all  --  anywhere             anywhere
RETURN     all  --  anywhere             anywhere

Chain DOCKER-ISOLATION-STAGE-2 (1 references)
target     prot opt source               destination
DROP       all  --  anywhere             anywhere
RETURN     all  --  anywhere             anywhere

Chain DOCKER-USER (1 references)
target     prot opt source               destination
RETURN     all  --  anywhere             anywhere
~~~

Docker走的是iptables中的Chain DOCKER。

~~~shell
[root@AlexWong fastdfs]# iptables -L DOCKER
Chain DOCKER (1 references)
target     prot opt source               destination
ACCEPT     tcp  --  anywhere             172.17.0.2           tcp dpt:22122
ACCEPT     tcp  --  anywhere             172.17.0.3           tcp dpt:inovaport1
ACCEPT     tcp  --  anywhere             172.17.0.3           tcp dpt:ddi-tcp-1

~~~

`不过这个规则通过firewalld是看不到的，较新版本的redhat中加入firewalld主要是为了引入区域概念、简化操作和易于理解，说白了就是用于解放iptables冗长的命令。
可docker却是通过iptables来完成自身功能的，本质上firewalld是建立在iptables之上的一个应用。
所以：docker并不是绕过了防火墙，只是应为它往iptables里写了规则，你在firewalld里看不到而已。`

稍后详细了解下firewalld和iptables的区别。



