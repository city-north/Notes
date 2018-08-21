[返回目录](/README.md)

# 安装Tomcat

## 概览

* 安装包 ：[apache-tomcat-8.0.53.tar.gz](http://mirrors.shu.edu.cn/apache/tomcat/tomcat-8/v8.5.32/bin/apache-tomcat-8.5.32.tar.gz)
* 安装位置：/usr/local/tomcat

## 创建目录

```
mkdir /usr/local/tomcat
cd /usr/local/tomcat
```

### 解压

```
tar -zxvf apache-tomcat-8.0.53.tar.gz
```

## 删除Webapp目录下的所有

```
rm -rf  apache-tomcat-8.0.53/webapps/*
```

## 修改端口

### 1.修改访问端口8080

```
vim apache-tomcat-8.0.53/conf/server.xml
```

输入`/8080`可快速查找到端口编辑处

### 2.修改ShutDown远程停服务端口8005

```
<Server port="8005" shutdown="SHUTDOWN">
```

### 3.修改AJP端口8009

```
<!-- Define an AJP 1.3 Connector on port 8009 -->
<Connector port="8009" protocol="AJP/1.3" redirectPort="8443" />
```

## 修改catalina.sh

```
export CATALINA_OPTS="-server -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dfile.encoding=utf-8"
```

修改内存设置，开始处添加

```
JAVA_OPTS='-Xms512m -Xmx2048m'
```

## 启动

```
cd /usr/local/tomcat/apache-tomcat-8.0.53/bin/
./startup.sh
```

[返回目录](/README.md)

