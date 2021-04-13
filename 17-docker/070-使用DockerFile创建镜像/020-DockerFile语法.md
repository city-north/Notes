# 020-DockerFile语法

[TOC]

https://github.com/CentOS/sig-cloud-instance-images/blob/f32666d2af356ed6835942ed753a4970e18bca94/docker/Dockerfile

```
FROM scratch # 基础镜像，scratch相当于java中的Object
ADD centos-7-x86_64-docker.tar.xz /  #  centos
LABEL org.label-schema.schema-version="1.0" \
    org.label-schema.name="CentOS Base Image" \
    org.label-schema.vendor="CentOS" \
    org.label-schema.license="GPLv2" \
    org.label-schema.build-date="20190305"
# 标签说明
CMD ["/bin/bash"] # 默认执行的命令，创建运行容器时最后会加上 /bin/bash, # 所以创建容器时，可不加 /bin/bash ,即如下:
# docker run -it --name=mycentos0 centos:7
# 如果加了，则在后面采用我们自己加的命令执行/bin/bash
```

### Dockerfile 语法规则
1. 每条指令的
1. 执行顺序按
1. `#`用于注释
1. 每条指令都会创建一个新的镜像层，并对镜像进行提交


### Dockerfile 执行流程
1. Docker 从基础镜像运行一个容器
1. 执行每一条指定并对容器作出修改
1. 执行类似 docker commit 的操作提交一个新的镜像层
1. docker 再基于刚提交的镜像运行一个新容器
1. 执行 Dockerfile 中的下一条指令直到所有指令都执行完成

### 自己写一个基于 CentOS7 ,配置Java 环境
创建一个 DockerFile
文件路径:
```
[root@localhost mydocker]# ll
total 190428
-rw-r--r--. 1 root root       332 May 23 16:52 Dockerfile
-rw-r--r--. 1 root root 194990602 May 23 16:31 jdk-8u211-linux-x64.tar.gz
```

```
#来自基础镜像
FROM centos:7
#指定镜像创建者信息
MAINTAINER EricChen
#切换工作目录 /usr/local
WORKDIR /usr/local
#创建一个存放jdk的路径
RUN mkdir /usr/local/java
#将jdk压缩包复制并解压到容器中/usr/local/java
ADD jdk-8u211-linux-x64.tar.gz /usr/local/java

#配置java环境变量
ENV JAVA_HOME /usr/local/java/jdk1.8.0_211
ENV JRE_HOME $JAVA_HOME/jre
ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JRE_HOME/lib:$CLASSPATH
ENV PATH $JAVA_HOME/bin:$PATH
CMD ["/bin/bash"]

```

### 构建语法
`docker build [-f 指定Dockerfile所在路径与文件名] -t 生成的镜像名:标签名 .`

注意后边的 空格 和点 . 不要省略, . 表示当前目录
示例: 生成镜像名为jdk，标签为1.8

`docker build -t jdk:1.8 .`

