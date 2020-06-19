#!/bin/bash
echo Starting application 
echo 'start sleep 10s...'

nohup java -jar /home/know/knowledge-app-3.2.jar > /home/know/knowledge-app-3.2.log 2>&1 &

#如果去掉以下延时,就会被jenkins杀掉子进程
sleep 10s
echo 'end sleep...'