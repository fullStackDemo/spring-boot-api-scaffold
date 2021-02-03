## Linux Sed命令

[TOC]

### 1、前言

*sed*是一种流编辑器，它是文本处理中非常好的工具，能够完美的配合正则表达式使用；

Sed主要用来自动编辑一个或多个文件，可以将数据行进行替换、删除、新增、选取等特定工作，简化对文件的反复操作，编写转换程序等；

处理结果保存在缓存区域，如果不使用重定向存储输出，不会直接修改文件。

>语法：
>
>```shell
>sed [-hnV][-e<script>][-f<script文件>][文本文件]
>```
>
>拆分如下：
>
>```shell
>sed的脚本格式：sed [options] -f scriptfile file(s);>
>```

~~~
 -e ：直接在命令行模式上进行sed动作编辑，此为默认选项;
 -f ：将sed的动作写在一个文件内，用–f filename 执行filename内的sed动作;
 -i ：直接修改文件内容;
 -n ：只打印模式匹配的行；
 -r ：支持扩展表达式;
~~~

> 常用指令

```
 a\ 在当前行下面插入文本
 i\ 在当前行上面插入文本
 c\ 把选定的行改为新的文本
 d 删除，删除选择的行
 D 删除模板块的第一行
 s 替换指定字符
 h 拷贝模板块的内容到内存中的缓冲区
 H 追加模板块的内容到内存中的缓冲区
 g 获得内存缓冲区的内容，并替代当前模板块中的文本
 G 获得内存缓冲区的内容，并追加到当前模板块文本的后面
 l 列表不能打印字符的清单
 n 读取下一个输入行，用下一个命令处理新的行而不是用第一个命令
 N 追加下一个输入行到模板块后面并在二者间嵌入一个新行，改变当前行号码
 p 打印模板块的行。 P(大写) 打印模板块的第一行
 q 退出Sed
 b lable 分支到脚本中带有标记的地方，如果分支不存在则分支到脚本的末尾
 r file 从file中读行
 t label if分支，从最后一行开始，条件一旦满足或者T，t命令，将导致分支到带有标号的命令处，或者到脚本的末尾
 T label 错误分支，从最后一行开始，一旦发生错误或者T，t命令，将导致分支到带有标号的命令处，或者到脚本的末尾
 w file 写并追加模板块到file末尾
 W file 写并追加模板块的第一行到file末尾
 ! 表示后面的命令对所有没有被选定的行发生作用
 = 打印当前行号
 # 把注释扩展到下一个换行符以前
```

> sed替换标记

```
 g 表示行内全面替换
 p 表示打印行
 w 表示把行写入一个文件
 x 表示互换模板块中的文本和缓冲区中的文本
 y 表示把一个字符翻译为另外的字符（但是不用于正则表达式）;
 \1 子串匹配标记
 & 已匹配字符串标记
```

> sed正则匹配规则

```
^ 匹配行开始，如：/^sed/匹配所有以sed开头的行; 
 $ 匹配行结束，如：/sed$/匹配所有以sed结尾的行; 
 . 匹配一个非换行符的任意字符，如：/s.d/匹配s后接一个任意字符，最后是d; 
 * 匹配0个或多个字符，如：/*sed/匹配所有模板是一个或多个空格后紧跟sed的行;  
 [] 匹配一个指定范围内的字符，如/[ss]ed/匹配sed和Sed;   
 [^] 匹配一个不在指定范围内的字符，如：/[^A-RT-Z]ed/匹配不包含A-R和T-Z的一个字母开头，紧跟ed的行;  
 \(..\) 匹配子串，保存匹配的字符，如s/\(love\)able/\1rs，loveable被替换成lovers;  
 & 保存搜索字符用来替换其他字符，如s/love/**&**/，love这成**love**;  
 \< 匹配单词的开始，如:/\ 
 \> 匹配单词的结束，如/love\>/匹配包含以love结尾的单词的行; 
 x\{m\} 重复字符x，m次，如：/0\{5\}/匹配包含5个0的行; 
 x\{m,\} 重复字符x，至少m次，如：/0\{5,\}/匹配至少有5个0的行; 
 x\{m,n\} 重复字符x，至少m次，不多于n次，如：/0\{5,10\}/匹配5~10个0的行;
```

### 2、实例

#### 2.1、替换

> `s命令`

~~~shell
[root@AlexWong script]# cat test.txt
daemonize yes
logfile ./logs/redis.log
daemonize yes
logfile ./logs/redis.log
# 替换
[root@AlexWong script]# sed 's/daemonize yes/daemonize no/' test.txt
daemonize no
logfile ./logs/redis.log
daemonize no
logfile ./logs/redis.log
# 并没有写入文件
[root@AlexWong script]# cat test.txt
daemonize yes
logfile ./logs/redis.log
# -i 直接修改当前文件。默认匹配规则-g，全局全部替换
[root@AlexWong script]# sed -i 's/daemonize yes/daemonize no/' test.txt
[root@AlexWong script]# cat test.txt
daemonize no
logfile ./logs/redis.log
daemonize no
logfile ./logs/redis.log
# 特殊字符时需转义
[root@AlexWong script]# sed -i 's/logfile \.\/logs\/redis.log/logfile \.\/logs\/mysql.log/' test.txt
[root@AlexWong script]# cat test.txt
daemonize no
logfile ./logs/mysql.log
daemonize no
logfile ./logs/mysql.log
~~~

> `-n选项和p命令`

~~~shell
# -n 和 p 一起使用时，只打印有更改的行，此时并没有写入test.txt
[root@AlexWong script]# sed -n 's/logfile \.\/logs\/mysql2.log/logfile \.\/logs\/mysql.log/p' test.txt
logfile ./logs/mysql.log
logfile ./logs/mysql.log
[root@AlexWong script]# cat test.txt
daemonize no
logfile ./logs/mysql2.log
daemonize no
logfile ./logs/mysql2.log
# -n 和 p, 再加上 -i 重置当前文件
[root@AlexWong script]# sed -ni 's/logfile \.\/logs\/mysql2.log/logfile \.\/logs\/mysql.log/p' test.txt
[root@AlexWong script]# cat test.txt
logfile ./logs/mysql.log
logfile ./logs/mysql.log

# 注意

# -i在前，加上-n 和 p, 不会重置当前文件，只修改匹配行
[root@AlexWong script]# cat test.txt
daemonize no
logfile ./logs/mysql.log
daemonize no
logfile ./logs/mysql.log
[root@AlexWong script]# sed -in 's/logfile \.\/logs\/mysql2.log/logfile \.\/logs\/mysql.log/p' test.txt
[root@AlexWong script]# cat test.txt
daemonize no
logfile ./logs/mysql.log
daemonize no
logfile ./logs/mysql.log
~~~

> `/Ng 命令`
>
> 从第N处匹配开始替换

~~~shell
[root@AlexWong script]# echo "testtesttest" | sed 's/test/TEST/1g'
TESTTESTTEST
[root@AlexWong script]# echo "testtesttest" | sed 's/test/TEST/2g'
testTESTTEST
[root@AlexWong script]# echo "testtesttest" | sed 's/test/TEST/3g'
testtestTEST
~~~

> 定界符
>
> 一般定界符为`|`，其实定界符可以是任何字符，只不过匹配项中如果有当前定界符，需要转义

~~~shell
# 定界符 |
[root@AlexWong script]# echo "testtesttest" | sed 's/test/TEST/3g'
testtestTEST
# 定界符 :
[root@AlexWong script]# echo "testtesttest" | sed 's:test:TEST:3g'
testtestTEST
# 定界符 0
[root@AlexWong script]# echo "testtesttest" | sed 's0test0TEST03g'
testtestTEST
# 定界符 0 出现在内部，需要转义
[root@AlexWong script]# echo "testtesttest" | sed 's0test0TEST\003g'
testtestTEST0
~~~



#### 2.2、删除

> `d命令`

~~~shell
# 源文件
[root@AlexWong script]# cat test.txt
daemonize no
logfile ./logs/mysql.log

daemonize no
logfile ./logs/mysql.log
# 删除空行
[root@AlexWong script]# sed -i '/^$/d' test.txt
daemonize no
logfile ./logs/mysql.log
daemonize no
logfile ./logs/mysql.log
# 删除文件的第2行
[root@AlexWong script]# sed '2d' test.txt
daemonize no
daemonize no
logfile ./logs/mysql.log
# 删除文件的第2行到末尾所有行
[root@AlexWong script]# sed '2,$d' test.txt
daemonize no
# 删除文件最后一行
[root@AlexWong script]# sed '$d' test.txt
daemonize no
logfile ./logs/mysql.log
daemonize no
# 删除文件中所有开头是logfile的行
[root@AlexWong script]# sed '/^logfile/'d test.txt
daemonize no
daemonize no

# 注意，以上案例除了删除空格其他均没有加 -i，所以原有文件并没有被修改，如有需要，请加 -i，这样会直接修改原文件
~~~

#### 2.3、匹配

> `已匹配字符串标记&`

~~~shell
# 使用 [&] 替换它，& 对应于之前所匹配到的单词
[root@AlexWong script]# echo test test | sed 's/\w\+/[&]/g'
[test] [test]
[root@AlexWong script]# cat test.txt
daemonize no
logfile ./logs/mysql.log
daemonize no
logfile ./logs/mysql.log
# 在匹配关键字后面加666
[root@AlexWong script]# sed 's/^logfile/&666/g' test.txt
daemonize no
logfile666 ./logs/mysql.log
daemonize no
logfile666 ./logs/mysql.log
# 在匹配行后面加666
[root@AlexWong script]# sed 's/^logfile.*/&666/g' test.txt
daemonize no
logfile ./logs/mysql.log666
daemonize no
logfile ./logs/mysql.log666

~~~

> `\1 子串匹配`

~~~shell
# 匹配到的第一个子串就标记为 \1
[root@AlexWong script]# sed 's/\(logfile\)/\1test/g' test.txt
daemonize no
logfiletest ./logs/mysql.log
daemonize no
logfiletest ./logs/mysql.log
# 只打印相关
[root@AlexWong script]# sed -n 's/\(logfile\)/\1test/pg' test.txt
logfiletest ./logs/mysql.log
logfiletest ./logs/mysql.log

# 依此类推匹配到的第二个结果就是 \2
[root@AlexWong script]# sed 's/\(logfile\) \(\.\/logs\)/\2 \1/g' test.txt
daemonize no
./logs logfile/mysql.log
daemonize no
./logs logfile/mysql.log
~~~

> `多表达式`

~~~shell
[root@AlexWong script]# sed 's/\(logfile\) \(\.\/logs\)/\2 \1/g;s/\(daemonize\)/\166666/g' test.txt
daemonize66666 no
./logs logfile/mysql.log
daemonize66666 no
./logs logfile/mysql.log

# sed '表达式' | sed '表达式'  等价于：  
# sed '表达式; 表达式'
~~~

> `逗号，选定行的范围`

~~~shell
# 打印匹配的多行
[root@AlexWong script]# sed -n "/logfile/,/yes/p" test.txt
logfile ./logs/mysql.log
daemonize yes
logfile ./logs/mysql.log
#打印从第3行开始到第一个包含以log开始的行之间的所有行
[root@AlexWong script]# cat test.txt
daemonize no
logfile ./logs/mysql.log
daemonize yes
logfile ./logs/mysql.log
[root@AlexWong script]# sed -n "3,/log/p" test.txt
daemonize yes
logfile ./logs/mysql.log
# 追加文本
[root@AlexWong script]# sed "3,/log/s/$/9999/" test.txt
daemonize no
logfile ./logs/mysql.log
daemonize yes9999
logfile ./logs/mysql.log9999
[root@AlexWong script]# sed "3,/log/s/$/9999,6666/" test.txt
daemonize no
logfile ./logs/mysql.log
daemonize yes9999,6666
logfile ./logs/mysql.log9999,6666
[root@AlexWong script]# sed "/no/,/log/s/$/9999,6666/" test.txt
daemonize no9999,6666
logfile ./logs/mysql.log9999,6666
daemonize yes
logfile ./logs/mysql.log
~~~

> `多点编辑：e命令`
>
> 同时执行多个表达式，上一个表达式的结果会对下一个表达式执行造成影响

~~~shell
[root@AlexWong script]# sed -e "s/yes/9999/" -e "s/log/6666/g" test.txt
daemonize no
6666file ./6666s/mysql.6666
daemonize 9999
6666file ./6666s/mysql.6666

# 测试前面的表达式会对后面造成影响
[root@AlexWong script]# sed -e "s/yes/9999/" -e "s/yes/6666/g" test.txt
daemonize no
logfile ./logs/mysql.log
daemonize 9999
logfile ./logs/mysql.log

# 多表达式1
[root@AlexWong script]# sed 's/\(logfile\) \(\.\/logs\)/\2 \1/g' test.txt  | sed 's/\(daemonize\)/\166666/g' test.txt
daemonize66666 no
logfile ./logs/mysql.log
daemonize66666 yes
logfile ./logs/mysql.log

# 多表达式2
[root@AlexWong script]# sed 's/\(logfile\) \(\.\/logs\)/\2 \1/g;s/\(daemonize\)/\166666/g' test.txt
daemonize66666 no
./logs logfile/mysql.log
daemonize66666 yes
./logs logfile/mysql.log

# 多表达式3
[root@AlexWong script]# sed -e 's/\(logfile\) \(\.\/logs\)/\2 \1/g' -e 's/\(daemonize\)/\166666/g' test.txt
daemonize66666 no
./logs logfile/mysql.log
daemonize66666 yes
./logs logfile/mysql.log

# 总结
# 多表达式2 = 多表达式3。执行顺序和表达式的顺序有关
# 多表达1属于组合多个表达式，上下表达式互不影响，执行以最后一个为准
~~~

#### 2.4、读写

> `从文件写入：r命令`
>
> 读取文件作为追加文本

~~~shell
[root@AlexWong script]# cat 2.txt
2222
[root@AlexWong script]# cat test.txt
daemonize no
logfile ./logs/mysql.log
daemonize yes
logfile ./logs/mysql.log
# 匹配，然后追加文本
[root@AlexWong script]# sed '/logfile/r 2.txt' test.txt
daemonize no
logfile ./logs/mysql.log
2222
daemonize yes
logfile ./logs/mysql.log
2222
~~~

> `追加（行下）：a\命令`

~~~shell
[root@AlexWong script]# sed '/logfile/a\6666' test.txt
daemonize no
logfile ./logs/mysql.log
6666
daemonize yes
logfile ./logs/mysql.log
6666
~~~

> `插入（行上）: i\命令`

~~~shell
[root@AlexWong script]# sed '/logfile/i\6666' test.txt
daemonize no
6666
logfile ./logs/mysql.log
daemonize yes
6666
logfile ./logs/mysql.log
~~~

> `下一行：n命令`
>
> 如果log被匹配，则移动到匹配行的下一行，替换这一行的mysql，变为8888，并打印该行

~~~shell
# 下一行没有匹配到内容
[root@AlexWong script]# sed "/log/{n; s/mysql/8888/;}" test.txt
daemonize no
logfile ./logs/mysql.log
daemonize yes
logfile ./logs/mysql.log

# 下一行匹配到内容
[root@AlexWong script]# sed "/yes/{n; s/mysql/8888/;}" test.txt
daemonize no
logfile ./logs/mysql.log
daemonize yes
logfile ./logs/8888.log
~~~

> `变形：y命令`
>
> 分散匹配 logfile变成 LOGFILE

~~~shell
[root@AlexWong script]# cat test.txt
daemonize no
logfile ./logs/mysql.log
daemonize yes
logfile ./logs/mysql.log
[root@AlexWong script]# sed "1,4y/logfile/LOGFILE/" test.txt
daEmOnIzE nO
LOGFILE ./LOGs/mysqL.LOG
daEmOnIzE yEs
LOGFILE ./LOGs/mysqL.LOG
~~~



