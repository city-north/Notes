# 01-Jenkins启动脚本 

[TOC]

## 简介

启动 SpringBoot 项目的简单启动脚本



## DEMO

```java
#!/bin/sh
# @author: yang
# 普通单节点应用直接使用该脚本即可满足所有服务配合jenkins使用, 内存可根据服务器自行调整
# 多注册中心情况则参考注释, 打开eureka配置
# redis配置修改需要注意lock的redis

#JAR=app.jar
echo "starting $JAR shell"

echo ">>> kill -9 $(jps -ml | grep $JAR | awk '{print $1}')"
kill -9 $(jps -ml | grep $JAR | awk '{print $1}')

JAVA_OPTS=" -Xms512m -Xmx1024m $JAVA_OPTS "
#JAVA_OPTS="$JAVA_OPTS -Deureka.client.serviceUrl.defaultZone='http://node1:8000/eureka,http://node2:8000/eureka'"
#JAVA_OPTS="$JAVA_OPTS -Dspring.datasource.username='root'"
#JAVA_OPTS="$JAVA_OPTS -Dspring.datasource.password='hand1357hand'"
#JAVA_OPTS="$JAVA_OPTS -Dspring.redis.host='redis.hzero.org'"
#JAVA_OPTS="$JAVA_OPTS -Dspring.redis.password='123456'"
#JAVA_OPTS="$JAVA_OPTS -Dhzero.lock.single-server.address='redis.hzero.org'"
#JAVA_OPTS="$JAVA_OPTS -Dhzero.lock.single-server.password='123456'"


echo ">>> nohup java -jar $JAVA_OPTS $JAR >app.log &"
BUILD_ID=dontKillMe nohup java -jar $JAVA_OPTS $JAR >app.log &
```

