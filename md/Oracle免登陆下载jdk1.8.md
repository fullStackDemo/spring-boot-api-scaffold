## 免登陆Oracle下载jdk

现在每次去oracle下载jdk, 都需要登录，并勾选同意协议

![1611200386639](Oracle%E5%85%8D%E7%99%BB%E9%99%86%E4%B8%8B%E8%BD%BDjdk1.8.assets/1611200386639.png)

![1611206424800](Oracle%E5%85%8D%E7%99%BB%E9%99%86%E4%B8%8B%E8%BD%BDjdk1.8.assets/1611206424800.png)

比较麻烦，

右键要下载的版本，然后复制链接如下：

`以jdk-8u281-linux-i586.tar.gz为例：`

~~~shell
https://download.oracle.com/otn/java/jdk/8u281-b09/89d678f2be164786b292527658ca1605/jdk-8u281-linux-i586.tar.gz

# 需要把otn改成otn-pub
~~~

~~~shell
[root@ script]# wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "https://download.oracle.com/otn-pub/java/jdk/8u281-b09/89d678f2be164786b292527658ca1605/jdk-8u281-linux-x64.tar.gz"
~~~

