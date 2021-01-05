## 华为云/dev/vdb磁盘挂载

[TOC]

### 1、前言

新买的服务器发现看不到数据盘，查了文档发现原来是没有挂载。

### 2、步骤

> 检查磁盘挂载

~~~shell
[root@AlexWong /]# fdisk -l
Disk /dev/vda: 40 GiB, 42949672960 bytes, 83886080 sectors
Units: sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disklabel type: dos
Disk identifier: 0x5d6e4b6b

Device     Boot Start      End  Sectors Size Id Type
/dev/vda1  *     2048 83886079 83884032  40G 83 Linux


Disk /dev/vdb: 100 GiB, 107374182400 bytes, 209715200 sectors
Units: sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
~~~

~~~shell
[root@AlexWong /]# df -h
Filesystem      Size  Used Avail Use% Mounted on
devtmpfs        1.9G     0  1.9G   0% /dev
tmpfs           1.9G     0  1.9G   0% /dev/shm
tmpfs           1.9G  8.5M  1.9G   1% /run
tmpfs           1.9G     0  1.9G   0% /sys/fs/cgroup
/dev/vda1        40G  2.3G   35G   7% /
tmpfs           374M     0  374M   0% /run/user/0
~~~

`Disk /dev/vda：磁盘1，已挂载`

`Disk /dev/vdb：磁盘2，未挂载`

> 磁盘分区
>
> 100GB数据硬盘待分区

~~~markdown
fdisk /dev/vdb
参数说明：
n：新建分区
  p: 主分区
  e: 扩展分区
上面可以默认回车，最后输入w （write）将操作写入磁盘
~~~

~~~shell
[root@AlexWong /]# fdisk /dev/vdb

Welcome to fdisk (util-linux 2.32.1).
Changes will remain in memory only, until you decide to write them.
Be careful before using the write command.

Device does not contain a recognized partition table.
Created a new DOS disklabel with disk identifier 0xdb040091.

Command (m for help): m

Help:

  DOS (MBR)
   a   toggle a bootable flag
   b   edit nested BSD disklabel
   c   toggle the dos compatibility flag

  Generic
   d   delete a partition
   F   list free unpartitioned space
   l   list known partition types
   n   add a new partition
   p   print the partition table
   t   change a partition type
   v   verify the partition table
   i   print information about a partition

  Misc
   m   print this menu
   u   change display/entry units
   x   extra functionality (experts only)

  Script
   I   load disk layout from sfdisk script file
   O   dump disk layout to sfdisk script file

  Save & Exit
   w   write table to disk and exit
   q   quit without saving changes

  Create a new label
   g   create a new empty GPT partition table
   G   create a new empty SGI (IRIX) partition table
   o   create a new empty DOS partition table
   s   create a new empty Sun partition table


Command (m for help): n
Partition type
   p   primary (0 primary, 0 extended, 4 free)
   e   extended (container for logical partitions)
Select (default p): p
Partition number (1-4, default 1): 1
First sector (2048-209715199, default 2048):
Last sector, +sectors or +size{K,M,G,T,P} (2048-209715199, default 209715199):

Created a new partition 1 of type 'Linux' and of size 100 GiB.

Command (m for help): w
The partition table has been altered.
Calling ioctl() to re-read partition table.
Syncing disks.
~~~

> 格式化刚划分的磁盘，格式成ext4格式

~~~shell
[root@AlexWong /]# mkfs.ext4 /dev/vdb
~~~

> 挂载文件目录

~~~shell
# 新建文件夹
[root@AlexWong /]# mkdir data
[root@AlexWong /]# echo "/dev/vdb /gps ext4 defaults 0 0" >> /etc/fstab
~~~

> 重新加载fstable，并查看磁盘是否加载成功

~~~shell
[root@AlexWong /]# mount -a
[root@AlexWong /]# df -h
[root@AlexWong /]# df -h
Filesystem      Size  Used Avail Use% Mounted on
devtmpfs        1.9G     0  1.9G   0% /dev
tmpfs           1.9G     0  1.9G   0% /dev/shm
tmpfs           1.9G  8.5M  1.9G   1% /run
tmpfs           1.9G     0  1.9G   0% /sys/fs/cgroup
/dev/vda1        40G  2.3G   35G   7% /
tmpfs           374M     0  374M   0% /run/user/0
/dev/vdb         98G   61M   93G   1% /data
~~~

挂载成功。

### 3、分多个主区

`100G分三个，一个20G，另外两个40G`

```shell
Units = sectors of 1 * 512 = 512 bytes
sectors：
20GB = 20 * 1024 * 1024 * 2 = 41943040
40GB = 40 * 1024 * 1024 * 2 = 83886080
40GB = 40 * 1024 * 1024 * 2 = 83886080
```

~~~shell
[root@AlexWong /]# fdisk -l
Disk /dev/vda: 40 GiB, 42949672960 bytes, 83886080 sectors
Units: sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disklabel type: dos
Disk identifier: 0x5d6e4b6b

Device     Boot Start      End  Sectors Size Id Type
/dev/vda1  *     2048 83886079 83884032  40G 83 Linux


Disk /dev/vdb: 100 GiB, 107374182400 bytes, 209715200 sectors
Units: sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
----------------------------------20GB----------------------------------------
# 分第一个20G，分配给/share
#以新挂载的数据盘/dev/vdb为例
[root@AlexWong /]# fdisk /dev/vdb
# 输入“n”，按“Enter”，开始新建分区
Command (m for help): n
Partition type
   p   primary (0 primary, 0 extended, 4 free)
   e   extended (container for logical partitions)
# 表示磁盘有两种分区类型：
# “p”表示主分区
# “e”表示扩展分区
# 磁盘使用MBR分区形式，最多可以创建4个主分区，或者3个主分区加1个扩展分区，扩展分区不可以直接使用，需要划分成若干个逻辑分区才可以使用
# 磁盘使用GPT分区形式时，没有主分区、扩展分区以及逻辑分区之分
# 以创建一个主要分区为例，输入“p”，按“Enter”，开始创建一个主分区
Select (default p): p
# “Partition number”表示主分区编号，可以选择1-4
Partition number (1-4, default 1): 1
# “First sector”表示起始磁柱值，可以选择2048-209715199，默认为2048
First sector (2048-209715199, default 2048): 2048
# “Last sector”表示截止磁柱值，可以选择2048-209715199，默认为209715199
Last sector, +sectors or +size{K,M,G,T,P} (41943040-209715199, default 209715199): 41943040
# 表示20GB的分区已经建立成功
Created a new partition 1 of type 'Linux' and of size 20 GiB.

# 输入“p”，按“Enter”，查看新建分区的详细信息
Command (m for help): p
Disk /dev/vdb: 100 GiB, 107374182400 bytes, 209715200 sectors
Units: sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disklabel type: dos
Disk identifier: 0xb2fef9e3

Device     Boot Start      End  Sectors Size Id Type
/dev/vdb1        2048 41943040 41940993  20G 83 Linux

# 输入“w”，按“Enter”，将分区结果写入分区表中
Command (m for help): w
The partition table has been altered.
Syncing disks.
# 分区创建完成
----------------------------------40GB----------------------------------------
# 开始创建40GB的分区
[root@AlexWong /]# fdisk /dev/vdb

Welcome to fdisk (util-linux 2.32.1).
Changes will remain in memory only, until you decide to write them.
Be careful before using the write command.


Command (m for help): n
Partition type
   p   primary (1 primary, 0 extended, 3 free)
   e   extended (container for logical partitions)
Select (default p): p
Partition number (2-4, default 2): 2
First sector (41943041-209715199, default 41945088):
# Last sector = 41945088 + 83886080 = 125831168 区间值
Last sector, +sectors or +size{K,M,G,T,P} (41945088-209715199, default 209715199): 125831168

Created a new partition 2 of type 'Linux' and of size 40 GiB.

Command (m for help): p
Disk /dev/vdb: 100 GiB, 107374182400 bytes, 209715200 sectors
Units: sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disklabel type: dos
Disk identifier: 0xb2fef9e3

Device     Boot    Start       End  Sectors Size Id Type
/dev/vdb1           2048  41943040 41940993  20G 83 Linux
/dev/vdb2       41945088 125831168 83886081  40G 83 Linux

Command (m for help): w
The partition table has been altered.
Syncing disks.
----------------------------------40GB----------------------------------------
# 开始创建最后一个40G分区
[root@AlexWong /]# fdisk /dev/vdb

Welcome to fdisk (util-linux 2.32.1).
Changes will remain in memory only, until you decide to write them.
Be careful before using the write command.


Command (m for help): n
Partition type
   p   primary (2 primary, 0 extended, 2 free)
   e   extended (container for logical partitions)
Select (default p): p
Partition number (3,4, default 3): 3
# 最后一个硬盘，sector默认enter即可
First sector (41943041-209715199, default 125833216):
Last sector, +sectors or +size{K,M,G,T,P} (125833216-209715199, default 209715199):

Created a new partition 3 of type 'Linux' and of size 40 GiB.

Command (m for help): p
Disk /dev/vdb: 100 GiB, 107374182400 bytes, 209715200 sectors
Units: sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disklabel type: dos
Disk identifier: 0xb2fef9e3

Device     Boot     Start       End  Sectors Size Id Type
/dev/vdb1            2048  41943040 41940993  20G 83 Linux
/dev/vdb2        41945088 125831168 83886081  40G 83 Linux
/dev/vdb3       125833216 209715199 83881984  40G 83 Linux

Command (m for help): w
The partition table has been altered.
Syncing disks.
~~~

~~~shell
# 执行以下命令，将新的分区表变更同步至操作系统。
[root@AlexWong /]# partprobe
# 执行以下命令，将新建分区文件系统设为系统所需格式。
# mkfs -t 文件系统格式 /dev/vdb1
[root@AlexWong /]# mkfs -t ext4 /dev/vdb1
mke2fs 1.45.6 (20-Mar-2020)
/dev/vdb1 is apparently in use by the system; will not make a filesystem here!
# 报错，系统已占用
[root@AlexWong /]# cat /proc/partitions
major minor  #blocks  name
 253        0   41943040 vda
 253        1   41942016 vda1
 253       16  104857600 vdb
 253       17   20970496 vdb1
 253       18   41943040 vdb2
 253       19   41940992 vdb3
# 上面可以看到dm工具确实在用
[root@AlexWong /]# dmsetup remove_all
[root@AlexWong /]# dmsetup status
No devices found
# 此时对vdb1进行格式化操作
[root@AlexWong /]# mkfs.ext4 /dev/vdb1
[root@AlexWong /]# mkfs.ext4 /dev/vdb2
[root@AlexWong /]# mkfs.ext4 /dev/vdb3

----------------------------------------------------------
此时发生了重大事故，我重启了机器，发现无法登陆。原因就是
我之前已经把/dev/vdb格式化并挂载过。此时再分区才导致如此的问题。
我把/dev/vdb /home ext4 defaults 0 0
写入了/etc/fstab，重启后无法SSH登陆。
之后把/dev/vdb重新分区，却我忘记删除/etc/fstab的自动挂载。
UUID=f1e9feb2-4a28-4c57-a03b-3a732954b724 /    ext4    defaults        1 1
#/dev/vdb /home ext4 defaults 0 0
#/dev/vdb1 /home ext4 defaults 0 0
----------------------------------------------------------
# 挂载硬盘
[root@AlexWong /]# mount /dev/vdb1 /share
[root@AlexWong /]# mount /dev/vdb2 /docker
[root@AlexWong /]# mount /dev/vdb3 /project
[root@AlexWong /]# df -h
Filesystem      Size  Used Avail Use% Mounted on
devtmpfs        1.9G     0  1.9G   0% /dev
tmpfs           1.9G     0  1.9G   0% /dev/shm
tmpfs           1.9G  9.0M  1.9G   1% /run
tmpfs           1.9G     0  1.9G   0% /sys/fs/cgroup
/dev/vda1        40G   17G   21G  44% /
/dev/vdb1        20G   45M   19G   1% /share
/dev/vdb2        40G   49M   38G   1% /docker
/dev/vdb3        40G   49M   38G   1% /project

----------------------------------------------------------
# 把挂载的分区写入/etc/fstab，这样在开机的时候能自动挂载
# 建议使用UUID的方式挂载
[root@AlexWong /]# blkid
/dev/vda1: UUID="f1e9feb2-4a28-4c57-a03b-3a732954b724" BLOCK_SIZE="4096" TYPE="ext4" PARTUUID="5d6e4b6b-01"
/dev/vdb1: UUID="85c07779-b50b-4397-8b4f-5fc608cd027b" BLOCK_SIZE="4096" TYPE="ext4" PARTUUID="b2fef9e3-01"
/dev/vdb2: UUID="245b254d-00c4-4666-9adb-a0fffca46a7e" BLOCK_SIZE="4096" TYPE="ext4" PARTUUID="b2fef9e3-02"
/dev/vdb3: UUID="6420d65e-7946-49ea-b87c-90c25dc790a6" BLOCK_SIZE="4096" TYPE="ext4" PARTUUID="b2fef9e3-03"
# 0 不检测硬盘, 根分区是1，数据盘是0
[root@AlexWong /]# echo "UUID=85c07779-b50b-4397-8b4f-5fc608cd027b /share ext4 defaults 0 0" >> /etc/fstab
[root@AlexWong /]# echo "UUID=245b254d-00c4-4666-9adb-a0fffca46a7e /docker ext4 defaults 0 0" >> /etc/fstab
[root@AlexWong /]# echo "UUID=6420d65e-7946-49ea-b87c-90c25dc790a6 /project ext4 defaults 0 0" >> /etc/fstab
[root@AlexWong /]# cat /etc/fstab
# /etc/fstab
# Created by anaconda on Sat Dec 12 11:50:01 2020
#
# Accessible filesystems, by reference, are maintained under '/dev/disk/'.
# See man pages fstab(5), findfs(8), mount(8) and/or blkid(8) for more info.
#
# After editing this file, run 'systemctl daemon-reload' to update systemd
# units generated from this file.
#
UUID=f1e9feb2-4a28-4c57-a03b-3a732954b724 /                       ext4    defaults        1 1
UUID=85c07779-b50b-4397-8b4f-5fc608cd027b /share                  ext4    defaults        0 0
UUID=245b254d-00c4-4666-9adb-a0fffca46a7e /docker                 ext4    defaults        0 0
UUID=6420d65e-7946-49ea-b87c-90c25dc790a6 /project                ext4    defaults        0 0
----------------------------------------------------------
# 发现一个问题，挂载硬盘后，原有文件丢失
[root@AlexWong docker]# ll
total 20
drwx------ 2 root root 16384 Jan  5 15:59 lost+found
drwxr-xr-x 4 root root  4096 Jan  5 17:54 volumes
[root@AlexWong docker]# umount /dev/vdb2 /docker
umount: /docker: target is busy.
umount: /docker: target is busy.
# 被占用
[root@AlexWong docker]# umount -l /dev/vdb2 /docker
# 移除挂载后，重新退出文件目录，再次进入，文件还在
~~~

