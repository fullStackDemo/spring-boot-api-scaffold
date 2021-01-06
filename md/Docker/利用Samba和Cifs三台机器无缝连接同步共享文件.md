## 利用SAMBA和CIFS三台机器无缝连接同步共享文件

[TOC]

SMB（Server Message Block）又称CIFS(Common Internet File System),一种应用层网络传输协议，由微软开发，主要功能是使网络上的机器能够共享计算机文件、打印机、串行端口和通讯等资源。它也提供认证的进程间通讯技能。它主要用在Windows的机器上。

CIFS是由microsoft在SMB的基础上发展，扩展到Internet上的协议。他和具体的OS无关，在unix上安装samba后可使用CIFS.它使程序可以访问远程Internet计算机上的文件并要求此计算机的服务。CIFS 使用客户/服务器模式。客户程序请求远在服务器上的服务器程序为它提供服务。服务器获得请求并返回响应。

CIFS是公共的或开放的SMB协议版本，并由Microsoft使用。SMB协议现在是局域网上用于服务器文件访问和打印的协议。像SMB协议一样，CIFS在高层运行，而不像TCP/IP协议那样运行在底层。CIFS可以看做是应用程序协议如文件传输协议和超文本传输协议的一个实现。

这里不再赘述，如何安装SAMBA协议安装Samba server，[请参考：Samba：centos服务器之间相互共享文件夹，可以用win10连接共享文件夹，并可以使用Docker部署](https://blog.csdn.net/qq_26003101/article/details/111279960)

`假设我们有三台机器：`

`A：192.168.6.101`

`B：192.168.6.102`

`C：192.168.6.103`

`A、B、C均安装了Samba，所以ABC均可以被访问。`

`共享文件目录都是 /share。`

> 在B机器上利用CIFS挂载A机器的目录

~~~shell
[root@B /]# mkdir share
[root@B /]# yum install cifs-utils
[root@B /]# mount -t cifs //192.168.6.101/share /share -o username=test,password='test@2.',domain=DOMAIN,vers=2.0 

# 查看挂载情况
[root@B /]# df -h
...
//192.168.6.101/share    20G  4.1G   16G   21% /share
~~~

> 在C机器上利用CIFS挂载A机器的目录
>
> 或者挂载B机器的目录

~~~shell
[root@C /]# mkdir share
[root@C /]# yum install cifs-utils
[root@C /]# mount -t cifs //192.168.6.101/share /share -o username=test,password='test@2.',domain=DOMAIN,vers=2.0 

# 查看挂载情况
[root@B /]# df -h
...
//192.168.6.101/share    20G  4.1G   16G   21% /share
~~~

挂载成功后，重启samba server;

然后，现在开始，三个机器的`/share`目录无论新建删除，都会同步。