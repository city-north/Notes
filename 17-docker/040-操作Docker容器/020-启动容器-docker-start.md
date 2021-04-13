# 020-启动容器-docker-start

[TOC]

## 启动容器

`docker 【container】 start`启动一个已经创建的容器(89为id开头两位)

`docker ps` 查看一个运行中的容器

```
$ docker start 89
89

sen@TR-PC MINGW64 /d/Program Files/Docker Toolbox
$ docker ps
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
89a89ea8db48        ubuntu:latest       "/bin/bash"         30 hours ago        Up 7 seconds                            optimistic_varahamihira
1234567
```

