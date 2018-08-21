[返回目录](/README.md)

# Java 安装

## 安装环境

操作系统：Oracle Linux 7.3

JDK版本：Java SE Development Kit 8u171（[jdk-8u171-linux-x64.tar.gz](http://download.oracle.com/otn-pub/java/jdk/8u171-b11/512cd62ec5174c3487ac17c61aaa89e8/jdk-8u171-linux-x64.tar.gz)）

安装位置：/usr/java

## 开始安装

* 上传，解压

```
tar  -zxvf  jdk-8u171-linux-x64.tar.gz
```

* 移动到/usr/java目录

```
mkdir /usr/java
mv jdk1.8.0_171/ /usr/java
```

* 修改环境变量,使用vim（yum install vim  ）

```
vim /etc/profile
```

* 文档末尾添加

```
export JAVA_HOME=/usr/java/jdk1.8.0_171
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib:$CLASSPATH
export JAVA_PATH=${JAVA_HOME}/bin:${JRE_HOME}/bin
export PATH=$PATH:${JAVA_PATH}
```

* 使配置生效

```
source /etc/profile
```

* 测试

```
[root@localhost java]# javac
用法: javac <options> <source files>
....
[root@localhost java]# java -version
java version "1.8.0_171"
Java(TM) SE Runtime Environment (build 1.8.0_171-b11)
Java HotSpot(TM) 64-Bit Server VM (build 25.171-b11, mixed mode)
[root@localhost java]# echo $PATH
/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/root/bin:/usr/java/jdk1.8.0_171/bin:/usr/java/jdk1.8.0_171/jre/bin
```

[返回目录](/README.md)

