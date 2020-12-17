## centos服务器之间相共享文件夹，并可以用win10连接共享文件夹

[TOC]

### 1、前言

前提：假设`A服务器`ip为：`192.168.6.101` ，`B服务器`ip为：`192.168.6.102`
现在要求把`A服务器`的`/share` 路径下的文件夹 共享到`B服务器`的`/share` 下。

`samba 是基于SMB协议（ServerMessage Block，信息服务块）的开源软件，samba也可以是SMB协议的商标。SMB是一种Linux、UNIX系统上可用于共享文件和打印机等资源的协议，这种协议是基于Client\Server型的协议，Client端可以通过SMB访问到Server（服务器）上的共享资源。当Windows是 Client，CentOS是服务器时，通过Samba就可以实现window访问Linux的资源，实现两个系统间的数据交互`。

### 2、安装配置

#### 2.1、A机器配置

~~~shell
[root@A /]# yum install samba -y
# 列出已下载的samba
[root@A /]# yum list samba
已加载插件：fastestmirror
Loading mirror speeds from cached hostfile
 * base: mirrors.aliyun.com
 * extras: mirrors.ustc.edu.cn
 * updates: mirrors.aliyun.com
已安装的软件包
samba.x86_64                                                                             4.10.16-7.el7_9
~~~

~~~shell
[root@A /]#yum list samba            //列出软件包的信息
[root@A /]#yum remove samba          //卸载软件包
[root@A /]#yum check-update samba    //检查是否有可更新的软件包
[root@A /]#yum update samba          //更新samba软件包
~~~

> 安装完成后，增加配置文件

~~~shell
[root@A /]# cd /etc/samba/
[root@A samba]# ll
总用量 24
-rw-r--r-- 1 root root    20 10月  1 01:50 lmhosts
-rw-r--r-- 1 root root   417 12月 16 13:57 smb.conf
-rw-r--r-- 1 root root 11327 10月  1 01:50 smb.conf.example
# 备份
[root@A samba]# cp smb.conf smb.conf.bak
[root@A samba]# ll
总用量 24
-rw-r--r-- 1 root root    20 10月  1 01:50 lmhosts
-rw-r--r-- 1 root root   417 12月 16 13:57 smb.conf
-rw-r--r-- 1 root root   706 10月  1 01:50 smb.conf.bak
-rw-r--r-- 1 root root 11327 10月  1 01:50 smb.conf.example
[root@A samba]# vim smb.conf
[global]
workgroup = MYGROUP
server string = Samba Server Version %v

log file = /var/log/samba/log.%m
max log size = 50

security = user
passdb backend = tdbsam

[homes]
comment = Home Directories
browseable = no
writable = yes

[share]                                           
comment = Share test
path = /share                          
valid users = test
directory mask = 775
writable = yes
browseable = yes

~~~

配置说明：

~~~shell
Samba的主配置文件为/etc/samba/smb.conf
主配置文件由两部分构成
    Global Settings 
　　该设置都是与Samba服务整体运行环境有关的选项，它的设置项目是针对所有共享资源的。
    Share Definitions 
　　该设置针对的是共享目录个别的设置，只对当前的共享资源起作用。

全局参数：
#==================Global Settings ===================
[global]

config file = /usr/local/samba/lib/smb.conf.%m
说明：config file可以让你使用另一个配置文件来覆盖缺省的配置文件。如果文件不存在，则该项无效。这个参数很有用，可以使得samba配置更灵活，可以让一台 samba服务器模拟多台不同配置的服务器。比如，你想让PC1（主机名）这台电脑在访问Samba Server时使用它自己的配置文件，那么先在/etc/samba/host/下为PC1配置一个名为smb.conf.pc1的文件，然后在 smb.conf中加入：config file = /etc/samba/host/smb.conf.%m。这样当PC1请求连接Samba Server时，smb.conf.%m就被替换成smb.conf.pc1。这样，对于PC1来说，它所使用的Samba服务就是由 smb.conf.pc1定义的，而其他机器访问Samba Server则还是应用smb.conf。

workgroup = WORKGROUP
说明：设定 Samba Server 所要加入的工作组或者域。

server string = Samba Server Version %v
说明：设定 Samba Server 的注释，可以是任何字符串，也可以不填。宏%v表示显示Samba的版本号。

netbios name = smbserver
说明：设置Samba Server的NetBIOS名称。如果不填，则默认会使用该服务器的DNS名称的第一部分。netbios name和workgroup名字不要设置成一样了。

interfaces = lo eth0 192.168.12.2/24 192.168.13.2/24
说明：设置Samba Server监听哪些网卡，可以写网卡名，也可以写该网卡的IP地址。

hosts allow = 127. 192.168.1. 192.168.10.1
说明：表示允许连接到Samba Server的客户端，多个参数以空格隔开。可以用一个IP表示，也可以用一个网段表示。hosts deny 与hosts allow 刚好相反。
例如：hosts allow=172.17.2.EXCEPT172.17.2.50
表示容许来自172.17.2.*的主机连接，但排除172.17.2.50
hosts allow=172.17.2.0/255.255.0.0
表示容许来自172.17.2.0/255.255.0.0子网中的所有主机连接
hosts allow=M1，M2
表示容许来自M1和M2两台计算机连接
hosts allow=@pega
表示容许来自pega网域的所有计算机连接

max connections = 0
说明：max connections用来指定连接Samba Server的最大连接数目。如果超出连接数目，则新的连接请求将被拒绝。0表示不限制。

deadtime = 0
说明：deadtime用来设置断掉一个没有打开任何文件的连接的时间。单位是分钟，0代表Samba Server不自动切断任何连接。

time server = yes/no
说明：time server用来设置让nmdb成为windows客户端的时间服务器。

log file = /var/log/samba/log.%m
说明：设置Samba Server日志文件的存储位置以及日志文件名称。在文件名后加个宏%m（主机名），表示对每台访问Samba Server的机器都单独记录一个日志文件。如果pc1、pc2访问过Samba Server，就会在/var/log/samba目录下留下log.pc1和log.pc2两个日志文件。

max log size = 50
说明：设置Samba Server日志文件的最大容量，单位为kB，0代表不限制。

security = user
说明：设置用户访问Samba Server的验证方式，一共有四种验证方式。
1. share：用户访问Samba Server不需要提供用户名和口令, 安全性能较低。
2. user：Samba Server共享目录只能被授权的用户访问,由Samba Server负责检查账号和密码的正确性。账号和密码要在本Samba Server中建立。
3. server：依靠其他Windows NT/2000或Samba Server来验证用户的账号和密码,是一种代理验证。此种安全模式下,系统管理员可以把所有的Windows用户和口令集中到一个NT系统上,使用 Windows NT进行Samba认证, 远程服务器可以自动认证全部用户和口令,如果认证失败,Samba将使用用户级安全模式作为替代的方式。
4. domain：域安全级别,使用主域控制器(PDC)来完成认证。

passdb backend = tdbsam
说明：passdb backend就是用户后台的意思。目前有三种后台：smbpasswd、tdbsam和ldapsam。sam应该是security account manager（安全账户管理）的简写。
1.smbpasswd：该方式是使用smb自己的工具smbpasswd来给系统用户（真实用户或者虚拟用户）设置一个Samba密码，客户端就用这个密码来访问Samba的资源。smbpasswd文件默认在/etc/samba目录下，不过有时候要手工建立该文件。
2.tdbsam： 该方式则是使用一个数据库文件来建立用户数据库。数据库文件叫passdb.tdb，默认在/etc/samba目录下。passdb.tdb用户数据库 可以使用smbpasswd –a来建立Samba用户，不过要建立的Samba用户必须先是系统用户。我们也可以使用pdbedit命令来建立Samba账户。pdbedit命令的 参数很多，我们列出几个主要的。
　　pdbedit –a username：新建Samba账户。
　　pdbedit –x username：删除Samba账户。
　　pdbedit –L：列出Samba用户列表，读取passdb.tdb数据库文件。
　　pdbedit –Lv：列出Samba用户列表的详细信息。
　　pdbedit –c “[D]” –u username：暂停该Samba用户的账号。
　　pdbedit –c “[]” –u username：恢复该Samba用户的账号。
3.ldapsam：该方式则是基于LDAP的账户管理方式来验证用户。首先要建立LDAP服务，然后设置“passdb backend = ldapsam:ldap://LDAP Server”

encrypt passwords = yes/no
说明：是否将认证密码加密。因为现在windows操作系统都是使用加密密码，所以一般要开启此项。不过配置文件默认已开启。

smb passwd file = /etc/samba/smbpasswd
说明：用来定义samba用户的密码文件。smbpasswd文件如果没有那就要手工新建。

username map = /etc/samba/smbusers
说明：用来定义用户名映射，比如可以将root换成administrator、admin等。不过要事先在smbusers文件中定义好。比如：root = administrator admin，这样就可以用administrator或admin这两个用户来代替root登陆Samba Server，更贴近windows用户的习惯。

guest account = nobody
说明：用来设置guest用户名。

socket options = TCP_NODELAY SO_RCVBUF=8192 SO_SNDBUF=8192
说明：用来设置服务器和客户端之间会话的Socket选项，可以优化传输速度。

domain master = yes/no
说明：设置Samba服务器是否要成为网域主浏览器，网域主浏览器可以管理跨子网域的浏览服务。

local master = yes/no
说明：local master用来指定Samba Server是否试图成为本地网域主浏览器。如果设为no，则永远不会成为本地网域主浏览器。但是即使设置为yes，也不等于该Samba Server就能成为主浏览器，还需要参加选举。

preferred master = yes/no
说明：设置Samba Server一开机就强迫进行主浏览器选举，可以提高Samba Server成为本地网域主浏览器的机会。如果该参数指定为yes时，最好把domain master也指定为yes。使用该参数时要注意：如果在本Samba Server所在的子网有其他的机器（不论是windows NT还是其他Samba Server）也指定为首要主浏览器时，那么这些机器将会因为争夺主浏览器而在网络上大发广播，影响网络性能。
如果同一个区域内有多台Samba Server，将上面三个参数设定在一台即可。

os level = 200
说明：设置samba服务器的os level。该参数决定Samba Server是否有机会成为本地网域的主浏览器。os level从0到255，winNT的os level是32，win95/98的os level是1。Windows 2000的os level是64。如果设置为0，则意味着Samba Server将失去浏览选择。如果想让Samba Server成为PDC，那么将它的os level值设大些。

domain logons = yes/no
说明：设置Samba Server是否要做为本地域控制器。主域控制器和备份域控制器都需要开启此项。

logon script = %u.bat
说明：当使用者用windows客户端登陆，那么Samba将提供一个登陆档。如果设置成%u.bat，那么就要为每个用户提供一个登陆档。如果人比较多， 那就比较麻烦。可以设置成一个具体的文件名，比如start.bat，那么用户登陆后都会去执行start.bat，而不用为每个用户设定一个登陆档了。 这个文件要放置在[netlogon]的path设置的目录路径下。

wins support = yes/no
说明：设置samba服务器是否提供wins服务。

wins server = wins服务器IP地址
说明：设置Samba Server是否使用别的wins服务器提供wins服务。

wins proxy = yes/no
说明：设置Samba Server是否开启wins代理服务。

dns proxy = yes/no
说明：设置Samba Server是否开启dns代理服务。

load printers = yes/no
说明：设置是否在启动Samba时就共享打印机。

printcap name = cups
说明：设置共享打印机的配置文件。

printing = cups
说明：设置Samba共享打印机的类型。现在支持的打印系统有：bsd, sysv, plp, lprng, aix, hpux, qnx

共享参数：
#================== Share Definitions ==================
[共享名]

comment = 任意字符串
说明：comment是对该共享的描述，可以是任意字符串。

path = 共享目录路径
说 明：path用来指定共享目录的路径。可以用%u、%m这样的宏来代替路径里的unix用户和客户机的Netbios名，用宏表示主要用于[homes] 共享域。例如：如果我们不打算用home段做为客户的共享，而是在/home/share/下为每个Linux用户以他的用户名建个目录，作为他的共享目 录，这样path就可以写成：path = /home/share/%u; 。用户在连接到这共享时具体的路径会被他的用户名代替，要注意这个用户名路径一定要存在，否则，客户机在访问时会找不到网络路径。同样，如果我们不是以用 户来划分目录，而是以客户机来划分目录，为网络上每台可以访问samba的机器都各自建个以它的netbios名的路径，作为不同机器的共享资源，就可以 这样写：path = /home/share/%m 。

browseable = yes/no
说明：browseable用来指定该共享是否可以浏览。

writable = yes/no
说明：writable用来指定该共享路径是否可写。

available = yes/no
说明：available用来指定该共享资源是否可用。

admin users = 该共享的管理者
说明：admin users用来指定该共享的管理员（对该共享具有完全控制权限）。在samba 3.0中，如果用户验证方式设置成“security=share”时，此项无效。
例如：admin users =david，sandy（多个用户中间用逗号隔开）。

valid users = 允许访问该共享的用户
说明：valid users用来指定允许访问该共享资源的用户。
例如：valid users = david，@dave，@tech（多个用户或者组中间用逗号隔开，如果要加入一个组就用“@组名”表示。）

invalid users = 禁止访问该共享的用户
说明：invalid users用来指定不允许访问该共享资源的用户。
例如：invalid users = root，@bob（多个用户或者组中间用逗号隔开。）

write list = 允许写入该共享的用户
说明：write list用来指定可以在该共享下写入文件的用户。
例如：write list = david，@dave

public = yes/no
说明：public用来指定该共享是否允许guest账户访问。

guest ok = yes/no
说明：意义同“public”。

几个特殊共享：
[homes]
comment = Home Directories
browseable = no
writable = yes
valid users = %S
; valid users = MYDOMAIN\%S

[printers]
comment = All Printers
path = /var/spool/samba
browseable = no
guest ok = no
writable = no
printable = yes

[netlogon]
comment = Network Logon Service
path = /var/lib/samba/netlogon
guest ok = yes
writable = no
share modes = no

[Profiles]
path = /var/lib/samba/profiles
browseable = no
guest ok = yes

Samba安装好后，使用testparm命令可以测试smb.conf配置是否正确。使用testparm –v命令可以详细的列出smb.conf支持的配置参数。
~~~

> 创建账户

~~~shell
[root@A samba]# useradd -d /share -s /sbin/nologin test 
# 赋予账号的共享密码 
[root@A samba]# smbpasswd -a test
New SMB password: 
Retype new SMB password:

#查看新建的用户
[root@A samba]# cat /etc/passwd
......
test:x:1000:1000::/share:/sbin/nologin
~~~

> 启动smb等服务

~~~shell
[root@A samba]# systemctl start smb.service
[root@A samba]# systemctl start nmb.service
[root@A samba]# systemctl enable smb.service
Created symlink from /etc/systemd/system/multi-user.target.wants/smb.service to /usr/lib/systemd/system/smb.service.
[root@A samba]# systemctl enable nmb.service
Created symlink from /etc/systemd/system/multi-user.target.wants/nmb.service to /usr/lib/systemd/system/nmb.service.

# 查看状态
[root@A share]# systemctl status nmb.service
● nmb.service - Samba NMB Daemon
   Loaded: loaded (/usr/lib/systemd/system/nmb.service; enabled; vendor preset: disabled)
   Active: active (running) since 三 2020-12-16 19:32:08 CST; 6min ago
     Docs: man:nmbd(8)
           man:samba(7)
           man:smb.conf(5)
 Main PID: 28757 (nmbd)
   Status: "nmbd: ready to serve connections..."
   Memory: 2.3M
   CGroup: /system.slice/nmb.service
           └─28757 /usr/sbin/nmbd --foreground --no-process-group
[root@A share]# systemctl status smb.service
● smb.service - Samba SMB Daemon
   Loaded: loaded (/usr/lib/systemd/system/smb.service; enabled; vendor preset: disabled)
   Active: active (running) since 三 2020-12-16 19:38:30 CST; 1min 26s ago
     Docs: man:smbd(8)
           man:samba(7)
           man:smb.conf(5)
 Main PID: 28840 (smbd)
   Status: "smbd: ready to serve connections..."
   Memory: 5.6M
   CGroup: /system.slice/smb.service
           ├─28840 /usr/sbin/smbd --foreground --no-process-group
           ├─28842 /usr/sbin/smbd --foreground --no-process-group
           ├─28843 /usr/sbin/smbd --foreground --no-process-group
           ├─28844 /usr/sbin/smbd --foreground --no-process-group
           ├─28849 /usr/sbin/smbd --foreground --no-process-group
           └─28850 /usr/sbin/smbd --foreground --no-process-group

12月 16 19:38:30 intranet-sso-uc systemd[1]: Starting Samba SMB Daemon...
12月 16 19:38:30 intranet-sso-uc smbd[28840]: [2020/12/16 19:38:30.777664,  0] ../../lib/util/become_daemon.c:136(daemon_ready)
12月 16 19:38:30 intranet-sso-uc smbd[28840]:   daemon_ready: daemon 'smbd' finished starting up and ready to serve connections
12月 16 19:38:30 intranet-sso-uc systemd[1]: Started Samba SMB Daemon.
~~~
#### 2.2、B机器配置

> 在B机器操作

~~~shell
[root@B /]# mkdir share
[root@B /]# yum install cifs-utils
# 挂载A服务器的共享文件到B服务器
[root@B /]# mount -t cifs -o username=test,password='test@2.' //192.168.6.101/share /share

# 开机自动挂载
[root@B /]# chmod +X /etc/rc.local
[root@B /]# vi /etc/rc.local
[root@B /]# mount -t cifs -o username=test,password='test@2.' //192.168.6.101/share /share
~~~

#### 2.3、测试

> A机器

~~~shell
[root@A share]# echo "test" > test.txt
[root@A share]# echo "test" > 2.txt
[root@A share]# ll
总用量 12
-rw-r--r-- 1 root root 2 12月 16 14:36 2.txt
-rw-r--r-- 1 root root 5 12月 16 14:35 test.txt
~~~

> B机器

~~~shell
[root@B share]# ll
总用量 12
-rw-r--r-- 1 root root 2 12月 16 14:36 2.txt
-rw-r--r-- 1 root root 5 12月 16 14:35 test.txt
~~~

测试AB机器文件夹共享成功；

> 在B机器写入一个文件试试

~~~shell
[root@B share]# echo 4 > 4.txt
-bash: 4.txt: 权限不够
~~~

却显示权限不够，我们之前是配置了写入权限了，那就有可能是samba server文件夹没有权限，那么进入A机器操作一下

~~~shell
[root@A /]# chmod 777 /share
~~~

再次测试后通过。

### 3、扩展 — Window10 连接 samba server

既然我们已经实现了AB两台centos机器之前的文件共享，那么如何在window台式机本地连接samba server，实现三台机器的文件同步呢？

> window + e 进入文件器，右键选择添加一个网络位置

![1608102629606](centos%E4%B8%A4%E5%8F%B0%E6%9C%BA%E5%99%A8%E5%A6%82%E4%BD%95%E5%AE%9E%E7%8E%B0%E6%96%87%E4%BB%B6%E5%A4%B9%E5%85%B1%E4%BA%AB.assets/1608102629606.png)

![1608102689506](centos%E4%B8%A4%E5%8F%B0%E6%9C%BA%E5%99%A8%E5%A6%82%E4%BD%95%E5%AE%9E%E7%8E%B0%E6%96%87%E4%BB%B6%E5%A4%B9%E5%85%B1%E4%BA%AB.assets/1608102689506.png)

> 然后输入之前新建的test账户密码，进入

![1608102766883](centos%E4%B8%A4%E5%8F%B0%E6%9C%BA%E5%99%A8%E5%A6%82%E4%BD%95%E5%AE%9E%E7%8E%B0%E6%96%87%E4%BB%B6%E5%A4%B9%E5%85%B1%E4%BA%AB.assets/1608102766883.png)

> window当前网络位置内新建文件夹 new

~~~shell
# A机器
[root@A share]# ll
总用量 990736
-rw-r--r-- 1 test test          0 12月 16 15:05 1.sh
-rw-r--r-- 1 root root          2 12月 16 14:36 22.txt
-rw-r--r-- 1 root root          2 12月 16 14:41 3.txt
-rw-r--r-- 1 test test          3 12月 16 14:55 8.txt
-rw-r--r-- 1 root root 1014496833 12月  1 11:16 docker.tar.gz
drwxrwxr-x 2 test test          6 12月 16 15:14 new
drwxrwxr-x 2 test test         22 12月 16 15:03 test
-rw-r--r-- 1 root root          5 12月 16 14:35 test.txt

# B机器
[root@B share]# ll
总用量 990736
-rw-r--r-- 1 test test          0 12月 16 15:05 1.sh
-rw-r--r-- 1 root root          2 12月 16 14:36 22.txt
-rw-r--r-- 1 root root          2 12月 16 14:41 3.txt
-rw-r--r-- 1 test test          3 12月 16 14:55 8.txt
-rw-r--r-- 1 root root 1014496833 12月  1 11:16 docker.tar.gz
drwxrwxr-x 2 test test          6 12月 16 15:14 new
drwxrwxr-x 2 test test         22 12月 16 15:03 test
-rw-r--r-- 1 root root          5 12月 16 14:35 test.txt
~~~

测试通过。

这样之后我们新增的文件，都会在三台机器内同步。

### 4、Docker搭建samba

上述操作已经完成，如果我们想到别的机器上再部署一个samba server，就需要按照上述步骤一个个操作，显得很是麻烦。

所以打算使用`Docker`搭建`samba`，后续迁移也会方便许多。

~~~shell
[root@A /]# docker search samba
NAME                              DESCRIPTION                                     STARS               OFFICIAL            AUTOMATED
dperson/samba                                                                     443                                     [OK]
svendowideit/samba                Sharing a Docker container's volume should b…   55                                      [OK]
nowsci/samba-domain               A well documented and tested Samba Active Di…   29                                      [OK]
servercontainers/samba            samba - (servercontainers/samba) (+ optional…   19                                      [OK]
elswork/samba                     Multi-Arch container of Samba for AMD & ARM …   17
appcontainers/samba               CentOS 6.6 Samba 4 Container - 282.2MB          13                                      [OK]
jenserat/samba-publicshare        Simple Docker image for publically sharing a…   12                                      [OK]
joebiellik/samba-server           Simple Samba server running on Alpine Linux …   10                                      [OK]
dreamcat4/samba                                                                   8                                       [OK]
instantlinux/samba-dc             Samba domain controller                         8                                       [OK]
sixeyed/samba                     Samba server, FROM dperson/samba                6                                       [OK]
gists/samba-server                Samba server based on alpine                    6                                       [OK]
pwntr/samba-alpine                Simple and lightweight Samba docker containe…   5                                       [OK]
timjdfletcher/samba-timemachine   Samba configured to run as a timemachine tar…   5
rsippl/samba-ad-dc                Samba 4 Active Directory Domain Controller      4                                       [OK]
andrespp/samba-ldap               Docker image for SAMBA with LDAP authenticat…   4                                       [OK]
willtho/samba-timemachine         Samba based Time Machine                        3                                       [OK]
nestyurkin/samba4timemachine      Latest Samba Server for support OSX High Sie…   2                                       [OK]
rootlogin/samba                   Samba smbd daemon                               1                                       [OK]
cptactionhank/samba                                                               1                                       [OK]
b32147/samba                      A dockerized instance of Samba on Apline        1                                       [OK]
znedw/samba                       Samba 4+ based on Alpine, designed to be run…   1
charlesmknox/samba                https://gitlab.com/charles-m-knox/samba-dock…   0
mediadepot/samba                  Samba container                                 0                                       [OK]
hivesolutions/samba               Simple stand-alone samba server.                0                                       [OK]
~~~

那我们就使用`Star`排名第一的 `dperson/samba`

> 新建目录 /docker/samba
>
> 当前目录下生成 sm.conf 配置文件

~~~xml
[global]
workgroup = MYGROUP
server string = Samba Server Version %v

log file = /var/log/samba/log.%m
max log size = 50

security = user
passdb backend = tdbsam

[homes]
comment = Home Directories
browseable = no
writable = yes

[share]
comment = Share test
path = /share
valid users = test
directory mask = 775
writable = yes
browseable = yes

~~~

> Dockerfilie

~~~dockerfile
# Alpine Linux
from dperson/samba
# 修改默认配置
COPY smb.conf /etc/samba
# 修改镜像源
COPY repositories /etc/apk/repositories
RUN mkdir /share \
    && chmod 777 /share \
    # 安装vim
    && apk add vim \
    # 安装ll
    && echo "alias ll='ls $LS_OPTIONS -l'" >> ~/.bashrc \
    && source ~/.bashrc 
~~~

> 生成镜像

~~~shell
[root@A samba]# docker build -t mysamba .
~~~

> 启动容器

~~~~shell
[root@A samba]# docker run -itd --name samba-test -p 139:139 -p 445:445 -v /share:/share -v /etc/localtime:/etc/localtime mysamba -u "test;test@2." -s "test;/share;yes;no;no;test;test;"

# 解释: 其中 -u "test;test@2." 是创建的用户密码
# -s "test;/share;yes;no;no;test;test;" 是编辑 sm.conf
# 等同于：
在sm.conf直接添加
[test]
   path = /share
   browsable = yes
   read only = no
   guest ok = no
   veto files = /.apdisk/.DS_Store/.TemporaryItems/.Trashes/desktop.ini/ehthumbs.db/Network Trash Folder/Temporary Items/Thumbs.db/
   delete veto files = yes
   valid users = test
   admin users = test
~~~~

> 测试

在window上网络增加映射网络驱动器。

![1608181717311](centos%E4%B8%A4%E5%8F%B0%E6%9C%BA%E5%99%A8%E5%A6%82%E4%BD%95%E5%AE%9E%E7%8E%B0%E6%96%87%E4%BB%B6%E5%A4%B9%E5%85%B1%E4%BA%AB.assets/1608181717311.png)

测试通过

![1608181813963](centos%E4%B8%A4%E5%8F%B0%E6%9C%BA%E5%99%A8%E5%A6%82%E4%BD%95%E5%AE%9E%E7%8E%B0%E6%96%87%E4%BB%B6%E5%A4%B9%E5%85%B1%E4%BA%AB.assets/1608181813963.png)

### 5、总结

上述有很多方式，去大家samba共享文件，docker方式是最方便迁移部署的方式。