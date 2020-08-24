## 1、前言

`什么是负载均衡？`

当我们把应用部署在单个机器时，如果用户访问量越来越大时，服务器压力就会越来越大，大到超过自身承受能力时，服务器的服务就会崩溃，用户访问就会不正常。为了避免服务器崩溃，让用户有更好的体验，我们通过负载均衡的方式来分担服务器压力。

`当我们把工程部署在多个服务器时，这就变成了服务器集群部署`。

当用户访问网站时，先访问一个`web服务器，然后把请求通过TCP servers均匀分发到其他服务器，减少大量的集中的访问。如此以来，用户的每次访问，都会保证服务器集群中的每个服务器压力趋于平衡，分担了服务器压力，避免了服务器崩溃的情况`。


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

我有一个`nginx的web服务器`，是一个验证码如图：

![](assets/b8ffd1d8435c449293505dad51616a81~tplv-k3u1fbpfcp-zoom-1.image)

我在`机器1和机器2，部署同一个server应用`；

`当我关闭server1时，server2依然可以正常访问`。

![](assets/56467796f72e490d988e97b2d168f6b4~tplv-k3u1fbpfcp-zoom-1.image)

`当我关闭server2时，server1依然可以正常访问`。

![](assets/7e286b8dd5b24fccb42abb13aea29830~tplv-k3u1fbpfcp-zoom-1.image)

`当我关闭server1，同时关闭server2时，客户端不可以正常访问`。

![](assets/81a6d4ccef404f59a205272838bb1c0f~tplv-k3u1fbpfcp-zoom-1.image)

以上为`默认方式，每个请求按时间顺序逐一分配到不同的后端服务器`，如果后端服务器服务挂了，能自动连接其他服务；

参数有：

| 参数         | 描述                                                         |
| ------------ | ------------------------------------------------------------ |
| fail_timeout | 与max_fails结合使用                                          |
| max_fails    | 设置在fail_timeout参数设置的时间内最大失败次数，如果在这个时间内，所有针对该服务器的请求都失败了，那么认为该服务器会被认为是停机了 |
| fail_time    | 服务器会被认为停机的时间长度,默认为10s。                     |
| backup       | 标记该服务器为备用服务器。当主服务器停止时，请求会被发送到它这里。 |
| down         | 标记服务器永久停机了。                                       |

## 3、其他方式

> weight
> 指定轮询几率，weight和访问比率成正比，用于后端服务器性能不均的情况,
>
> 该配置存在一个问题：已经登录后，如果服务转发到其他服务器，登录的session信息会丢失

~~~nginx
# 负载均衡服务
upstream backend {
  server 118.224.6.196:8280 weight=3;
  server 118.224.6.181:8280 weight=7;
}
~~~

![](assets/21f6b26b06924d25bdf0163b802bd81b~tplv-k3u1fbpfcp-zoom-1.image)

![](assets/4f8b71e637bb468a8cfc72e8a567ad90~tplv-k3u1fbpfcp-zoom-1.image)

`获取验证码是在server1机器上，验证验证码却在server2机器上，验证码存在server1的session中，所以有很大几率会登录不成功`。

这种情况下，我们可以采用`ip_hash`指令解决这个问题，如果客户已经访问了某个服务器，当用户再次访问时，会将该请求通过哈希算法，自动定位到该服务器；

> `ip_hash`

~~~nginx
# 负载均衡服务
upstream backend {
  server 118.224.6.196:8280;
  server 118.224.6.181:8280;
  ip_hash;
}
~~~

> fair(第三方插件)
>
> 这种方式根据后端服务器的响应时间进行分配，响应快的优先分配请求

~~~nginx
# 负载均衡服务
upstream backend {
  server 118.224.6.196:8280;
  server 118.224.6.181:8280;
  fair;
}
~~~

> url  hash(第三方插件)
>
> 此种方式和ip_hash比较类似，根据url的hash值进行分配，将url分配到同一个后端服务器，当服务器存在缓存时比较有效

~~~nginx
# 负载均衡服务
upstream backend {
  server 118.224.6.196:8280;
  server 118.224.6.181:8280;
  hash $request_uri;
  hash_method crc32;
}
~~~



## 4、总结

以上只是其中几个方式，这里只是简单说明，有时间再补充。