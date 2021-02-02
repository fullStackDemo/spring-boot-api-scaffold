## Spring Boot 多模块项目创建与配置

[TOC]

### 1、前言

如果一个项目由一个父模块和若干个子模块构成，每个模块都对应着一个pom.xml。它们之间通过继承相互关联，都是maven管理的，那么这种就是多模块项目。

多模块适用于一些比较大的项目，通过合理的模块拆分，实现代码的复用，便于维护和管理。

比如有一些是common、utils等子模块提供公共服务。

### 2、创建

#### 2.1、创建父模块

`左上角选择File->New->Project后，选择Spring Initializr，默认使用的Java版本是1.8`

![1612256443110](Spring%20Boot%20%E5%A4%9A%E6%A8%A1%E5%9D%97%E9%A1%B9%E7%9B%AE%E5%88%9B%E5%BB%BA%E4%B8%8E%E9%85%8D%E7%BD%AE.assets/1612256443110.png)

进入下一步，可以设置项目的一些基本信息，过程省略，该填写的就填写

![1612256676527](Spring%20Boot%20%E5%A4%9A%E6%A8%A1%E5%9D%97%E9%A1%B9%E7%9B%AE%E5%88%9B%E5%BB%BA%E4%B8%8E%E9%85%8D%E7%BD%AE.assets/1612256676527.png)

点击Next，进入下一个选择dependency的界面，作用是在pom中自动添加一些依赖，在项目初始化时就下载。这里我们不需要任何依赖。

`创建完成后，删除src、target等目录，父模块只做依赖管理，不需要编写代码。`

#### 2.2、创建子模块

`右键项目名，New->module`

![1612257209132](Spring%20Boot%20%E5%A4%9A%E6%A8%A1%E5%9D%97%E9%A1%B9%E7%9B%AE%E5%88%9B%E5%BB%BA%E4%B8%8E%E9%85%8D%E7%BD%AE.assets/1612257209132.png)

`创建过程同上，依次创建file-common、file-fastdfs-client、file-obs-client`

`删除父子模块中所有的mvnw、mvnw.cmd文件及.mvn文件夹`

![1612257499864](Spring%20Boot%20%E5%A4%9A%E6%A8%A1%E5%9D%97%E9%A1%B9%E7%9B%AE%E5%88%9B%E5%BB%BA%E4%B8%8E%E9%85%8D%E7%BD%AE.assets/1612257499864.png)

到此为止，简单的父模块和子模块创建完成。

