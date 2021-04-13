# 010-获取镜像

[TOC]

镜像是运行容器的前提, 官方的 Docker Hub网站已经提供了数十万个镜像供大家下载

## docker pull命令

可以使用docker pull命令直接从Docker Hub镜像源来下载镜像。

该命令的格式为

```
docker pull NAME[：TAG]
```

其中，

- NAME是镜像仓库的名称（用来区分镜像）
- TAG是镜像的标签（往往用来表示版本信息）

通常情况下，描述一个镜像需要包括“名称+标签”信息。

## 实例

```
docker pull nginx:1.19
```

## 从非官方的仓库下载镜像

如果从非官方仓库下载镜像,则需要在仓库名称前指定完整的仓库地址。例如从网易蜂巢的镜像源来下载ubuntu：14.04镜像，可以使用如下命令，此时下载的镜像名称为hub.c.163.com/public/ubuntu：14.04：

```
$ docker pull hub.c.163.com/public/ubuntu:14.04
```

pull子命令支持的选项主要包括：

```java
-a，--all-tags=true|false：是否获取仓库中的所有镜像，默认为否。
```

下载镜像到本地后，即可随时使用该镜像了，例如利用该镜像创建一个容器，在其中运行bash应用，执行ping localhost命令：

```shell
$ docker run -it ubuntu:14.04 bashroot@9c74026df12a:/# ping localhostPING localhost (127.0.0.1) 56(84) bytes of data.64 bytes from localhost (127.0.0.1): icmp_seq=1 ttl=64 time=0.058 ms64 bytes from localhost (127.0.0.1): icmp_seq=2 ttl=64 time=0.023 ms64 bytes from localhost (127.0.0.1): icmp_seq=3 ttl=64 time=0.018 ms^C--- localhost ping statistics ---3 packets transmitted, 3 received, 0% packet loss, time 1999msrtt min/avg/max/mdev = 0.018/0.033/0.058/0.017 msroot@9c74026df12a:/# exitexit
```



