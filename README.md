netstat -aon|findstr "9001"


钉钉SDK注入:

进入到工程目录下：

新建lib, 放置钉钉SDK

1、第一个


mvn install:install-file -DgroupId=com.taobao.sdk -DartifactId=dingtalk -Dversion=1.0.0 -Dfile=lib/dingtalk-sdk-java/taobao-sdk-java-auto_1479188381469-20190704.jar -Dpackaging=jar -DgeneratePom=true

和下面一一对应

<dependency>
    <groupId>com.taobao.sdk</groupId>
    <artifactId>dingtalk</artifactId>
    <version>1.0.0</version>
</dependency>

2、第二个


mvn install:install-file -DgroupId=com.taobao.sdk -DartifactId=dingtalk-source -Dversion=1.0.0 -Dfile=lib/ding talk-sdk-java/taobao-sdk-java-auto_1479188381469-20190704-source.jar -Dpackaging=jar -DgeneratePom=true

和下面一一对应

<dependency>
    <groupId>com.taobao.sdk</groupId>
    <artifactId>dingtalk-source</artifactId>
    <version>1.0.0</version>
</dependency>