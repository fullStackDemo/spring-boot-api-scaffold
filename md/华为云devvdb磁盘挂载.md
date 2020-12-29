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

