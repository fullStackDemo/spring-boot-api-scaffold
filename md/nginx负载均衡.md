## nginx负载均衡

[TOC]

## 1、前言

什么是负载均衡？

当我们把应用部署在单个机器时，如果用户访问量越来越大时，服务器压力就会越来越大，大到超过自身承受能力时，服务器的服务就会崩溃，用户访问就会不正常。为了避免服务器崩溃，让用户有更好的体验，我们通过负载均衡的方式来分担服务器压力。

当我们把工程部署在多个服务器时，这就变成了服务器集群部署。

当用户访问网站时，先访问一个web服务器，然后把请求通过TCP servers均匀分发到其他服务器，减少大量的集中的访问。如此以来，用户的每次访问，都会保证服务器集群中的每个服务器压力趋于平衡，分担了服务器压力，避免了服务器崩溃的情况。

**负载均衡是用反向代理的原理实现的。**

## 2、实现

> 普通单机反向代理如下
>
> 假设机器IP为： 118.224.178.5

~~~nginx
server {
  listen       80;
  server_name  localhost;

  location / {
      root   /home/test/;
      try_files $uri /index.html;
      index  index.html index.htm;
      autoindex on;
  }

  location ~ ^/(api) {
    proxy_pass http://118.224.6.196:8280;
  }
}
~~~

~~~mermaid
graph LR
客户端访问 --> 118.224.178.5
118.224.178.5 --> 客户端访问
118.224.178.5 --/api路径的请求转发请求到--> http://192.168.66.68:8280
http://192.168.66.68:8280 --> 118.224.178.5
~~~

> 负载均衡：
>
> 分发请求到多个服务器，到达均衡的目的

~~~mermaid
graph LR
客户端访问 --> 118.224.178.5
118.224.178.5 --> 客户端访问
118.224.178.5 --/api路径的请求转发请求到--> backend
backend --> 118.224.178.5
backend --分发请求--> 118.224.6.196服务器1
backend --分发请求--> 118.224.6.181服务器2
~~~

~~~nginx
# 负载均衡服务
upstream backend {
  server 118.224.6.196:8280;
  server 118.224.6.181:8280;
}

server {
  listen       80;
  server_name  localhost;

  location / {
      root   /home/test/;
      try_files $uri /index.html;
      index  index.html index.htm;
      autoindex on;
  }

  location ~ ^/(api) {
    add_header backendIP $upstream_addr;
    add_header backendCode $upstream_status;
    add_header Cache-Control no-store;
    proxy_pass http://backend;
  }
}
~~~

> 假设：
>
> nginx服务器0：118.224.178.5
>
> server机器1：118.224.6.196
>
> server机器2：118.224.6.181

我有一个nginx的web服务器，是一个验证码如图：

![1598265373940](nginx%E8%B4%9F%E8%BD%BD%E5%9D%87%E8%A1%A1.assets/1598265373940.png)

我在机器1和机器2，部署同一个server应用；

当我关闭server1时，server2依然可以正常访问。

![1598266022123](nginx%E8%B4%9F%E8%BD%BD%E5%9D%87%E8%A1%A1.assets/1598266022123.png)

当我关闭server2时，server1依然可以正常访问。

![1598265979844](nginx%E8%B4%9F%E8%BD%BD%E5%9D%87%E8%A1%A1.assets/1598265979844.png)

当我关闭server1，同时关闭server2时，客户端不可以正常访问。

![1598266048323](nginx%E8%B4%9F%E8%BD%BD%E5%9D%87%E8%A1%A1.assets/1598266048323.png)

以上为默认方式，每个请求**按时间顺序逐一分配**到不同的后端服务器，如果后端服务器服务挂了，能自动连接其他服务；

## 3、其他方式

> weight
> 指定轮询几率，weight和访问比率成正比，用于后端服务器性能不均的情况,
>
> 该配置存在一个问题：已经登录后，如果服务转发到其他服务器，登录的session信息会丢失

~~~nginx
# 负载均衡服务
upstream backend {
  server 192.168.6.196:8280 weight=3;
  server 192.168.6.181:8280 weight=7;
}
~~~

![1598266734763](nginx%E8%B4%9F%E8%BD%BD%E5%9D%87%E8%A1%A1.assets/1598266734763.png)

![1598266767652](nginx%E8%B4%9F%E8%BD%BD%E5%9D%87%E8%A1%A1.assets/1598266767652.png)

获取验证码是在server1机器上，验证验证码却在server2机器上，验证码存在server1的session中，所以有很大几率会登录不成功。

这种情况下，我们可以采用ip_hash指令解决这个问题，如果客户已经访问了某个服务器，当用户再次访问时，会将该请求通过哈希算法，自动定位到该服务器；

~~~nginx
# 负载均衡服务
upstream backend {
  server 192.168.6.196:8280;
  server 192.168.6.181:8280;
  ip_hash;
}
~~~





