## Curl命令汇总

[TOC]

### 1、前言

`curl 是常用的命令行工具，用来请求 Web 服务器。curl = client + url 客户端向服务器发送URL请求。`

### 2、Usage

#### -X

> `-X POST`
>
> 参数指定 HTTP 请求的方法
>

~~~shell
curl -X POST http://192.168.66.66:9002/api/test/curl
~~~

#### -d

> `-d`
>
> 参数用于发送 POST 请求的数据体
>
> 使用-d时，HTTP 请求会自动加上标头`Content-Type : application/x-www-form-urlencoded`。会自动将请求转为 POST 方法，因此可以省略`-X POST`
>

~~~shell
curl -d "name=test&age=12" http://192.168.66.66:9002/api/test/curl
or
curl -d "name=test" -d "age=12" http://192.168.66.66:9002/api/test/curl
~~~

> -d 也可以读取本地文本
>
> 新建文本 data.txt 
>
> name=test&age=12

~~~shell
curl -d "@data.txt" http://192.168.66.66:9002/api/test/curl
~~~

####  -e

> -e
>
> `-e`参数用来设置 HTTP 的标头`Referer`，表示请求的来源

~~~shell
curl -e "http://test.com" -X POST http://192.168.66.66:9002/api/test/curl
or
curl -H "Referer:http://test.com" -X POST http://192.168.66.66:9002/api/test/curl
~~~

#### -F

> -F
>
> HTTP 请求自动加上标头`Content-Type: multipart/form-data; 

~~~shell
curl -F "file=@data.png;type=image/png;filename=1.png" http://192.168.66.66:9002/api/test/curl

# type 可以自定义类型
# filename 可以自定义名字
~~~

向服务器发送一个文件data.png，服务器接收的时候改名为1.png

#### -H

> -H 
>
> 参数添加 HTTP 请求的标头

~~~shell
curl -H "Content-Type: application/json" -X POST http://192.168.66.66:9002/api/test/curl
~~~

#### -i

> -i
>
> 参数打印出服务器回应的 HTTP 标头

~~~shell
curl -i -X POST http://192.168.66.66:9002/api/test/curl
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 12 Jan 2021 05:54:31 GMT

{"cookie":"[null, null]","params":"[name=null, age=null]","headers":"[Content-Type:null, Referer:null]"}
~~~

收到服务器回应后，先输出服务器回应的标头，然后空一行，再输出接口的数据

#### -I

> -I
>
> 参数打印出服务器回应的 HTTP 标头

~~~shell
curl -I -X POST http://192.168.66.66:9002/api/test/curl
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 12 Jan 2021 05:54:31 GMT
~~~

输出服务器对 HEAD 请求的回应

#### -o

> -o
>
> 输出内容到本地文件

~~~shell
curl -o 1.txt -X POST http://192.168.66.66:9002/api/test/curl
cat 1.txt
{"cookie":"[null, null]","params":"[name=null, age=null]","headers":"[Content-Type:null, Referer:null]"}
~~~

#### -O

> -O
>
> `-O`参数将服务器回应保存成文件，并将 URL 的最后部分当作文件名

~~~shell
curl -O -X POST http://192.168.66.66:9002/api/test/curl
# 保存为curl文件
cat curl
{"cookie":"[null, null]","params":"[name=null, age=null]","headers":"[Content-Type:null, Referer:null]"}
~~~

#### -k

> 参数指定跳过 SSL 检测
>
> 检查服务器的 SSL 证书是否正确

#### --limit-rate

> 用来限制 HTTP 请求和回应的带宽，模拟慢网速的环境

~~~shell
curl --limit-rate 200k -X POST http://192.168.66.66:9002/api/test/curl
~~~

#### -G

>参数用来构造 URL 的查询字符串

~~~shell
 curl -d "name=test&age=12" -G http://192.168.66.66:9002/api/test/curl
 # 等同于
 curl -d "name=test&age=12" -X GET http://192.168.66.66:9002/api/test/curl
~~~

### 3、测试接口源码

~~~java
package com.alex.web.controller;

import com.alex.common.utils.HttpUtils;
import com.alex.common.utils.UUIDUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex
 */
@RestController
@RequestMapping("api/test")
public class TestController {

    @PostMapping("curl")
    public Object postCurl(String name, String age, MultipartFile file) {
        HttpServletRequest request = HttpUtils.getRequest();
        System.out.println("------cookies------");
        Map<String, Object> data = new HashMap<>(2);
        if (request != null) {
            // cookies
            Cookie[] cookies = request.getCookies();
            String[] cookieArr = new String[2];
            if (cookies != null) {
                int cookeIndex = 0;
                for (Cookie cookie : cookies) {
                    System.out.println(cookie.getName() + ":" + cookie.getValue());
                    cookieArr[cookeIndex] = cookie.getName() + ":" + cookie.getValue();
                    cookeIndex++;
                }
            }
            data.put("cookie", Arrays.toString(cookieArr));

            // params
            System.out.println("------params------");
            String[] params = new String[2];
            System.out.println("name=" + request.getParameter("name"));
            System.out.println("age=" + request.getParameter("age"));
            params[0] = "name=" + request.getParameter("name");
            params[1] = "age=" + request.getParameter("age");
            data.put("params", Arrays.toString(params));

            // headers
            System.out.println("------headers------");
            String[] headersArr = new String[2];
            String[] headers = new String[]{"Content-Type", "Referer"};
            for (int i = 0; i < headers.length; i++) {
                headersArr[i] = headers[i] + ":" + request.getHeader(headers[i]);
            }
            data.put("headers", Arrays.toString(headersArr));
        }

        if (file != null) {
            data.put("fileName", file.getOriginalFilename());
            data.put("fileType", file.getContentType());
        }

        return data;
    }
}
~~~

