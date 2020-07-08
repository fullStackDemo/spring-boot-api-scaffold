#!/bin/bash

AppName=test-0.0.1-SNAPSHOT.jar

#盐值变量
JASYPT_PASSWORD="1qaz2wsx3edc"
# 加密方式是
JASYPT_ALGORITHM="PBEWithMD5AndTripleDES"

#JVM参数
JVM_OPTS="-Djasypt.encryptor.password=${JASYPT_PASSWORD} -Djasypt.encryptor.algorithm=${JASYPT_ALGORITHM} -Dname=$AppName -Duser.timezone=Asia/Shanghai -Xms2048M -Xmx2048M -XX:PermSize=512M -XX:MaxPermSize=1024M -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps  -XX:+PrintGCDetails -XX:NewRatio=1 -XX:SurvivorRatio=30 -XX:+UseParallelGC -XX:+UseParallelOldGC"
APP_HOME=$(pwd)
LOG_PATH=$APP_HOME/logs/$AppName.log

if [ "$1" = "" ]; then
  echo -e "\033[0;31m 未输入操作名 \033[0m  \033[0;34m {start|stop|restart|status} \033[0m"
  exit 1
fi

if [ "$AppName" = "" ]; then
  echo -e "\033[0;31m 未输入应用名 \033[0m"
  exit 1
fi

function start() {
  PID=$(ps -ef | grep java | grep $AppName | grep -v grep | awk '{print $2}')

  if [ x"$PID" != x"" ]; then
    echo "$AppName is running..."
  else
    nohup java -jar $JVM_OPTS $AppName /dev/null >$LOG_PATH 2>&1 &
    echo "Start $AppName success..."
  fi
}

function stop() {
  echo "Stop $AppName"

  PID=""
  query() {
    PID=$(ps -ef | grep java | grep $AppName | grep -v grep | awk '{print $2}')
  }

  query
  if [ x"$PID" != x"" ]; then
    kill -TERM $PID
    echo "$AppName (pid:$PID) exiting..."
    while [ x"$PID" != x"" ]; do
      sleep 1
      query
    done
    echo "$AppName exited."
  else
    echo "$AppName already stopped."
  fi
}

function restart() {
  stop
  sleep 2
  start
}

function status() {
  PID=$(ps -ef | grep java | grep $AppName | grep -v grep | wc -l)
  if [ $PID != 0 ]; then
    echo "$AppName is running..."
  else
    echo "$AppName is not running..."
  fi
}

case $1 in
start)
  start
  ;;
stop)
  stop
  ;;
restart)
  restart
  ;;
status)
  status
  ;;
*) ;;

esac
