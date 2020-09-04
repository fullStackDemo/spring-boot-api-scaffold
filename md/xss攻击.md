## XSS攻击

[TOC]

### 1、前言

什么是`XSS`呢？

跨站脚本攻击(Cross Site Scripting)，为了不和层叠样式表(Cascading Style Sheets, CSS)的缩写混淆，故将跨站脚本攻击缩写为XSS。恶意攻击者往Web页面里插入恶意Script代码，当用户浏览该页时，嵌入其中Web里面的Script代码会被执行，从而达到恶意攻击用户的目的。

XSS攻击其实就是针对web漏洞进行各种恶意脚本的注入，从而达到恶意的目的；

| 类型                             | 存储         | 插入位置   |
| -------------------------------- | ------------ | :--------- |
| Reflected XSS（反射型 XSS 攻击） | URL          | HTML       |
| Stored XSS（存储型 XSS 攻击）    | 数据库       | HTML       |
| DOM XSS                          | 数据库 、URL | javascript |
| JSONP XSS                        | 数据库、URL  | javascript |

### 2、细说XSS攻击类型

