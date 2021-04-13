# 070-导入和导出容器-docker-export-import

[TOC]

导出容器是指导出一个已经创建的容器到一个文件，不管此时这个容器是否处于运行状态，可以使用docker export命令，该命令的格式为docker export[-o|--output[=""]]CONTAINER。其中，可以通过-o选项来指定导出的tar文件名，也可以直接通过重定向来实现。
首先查看所有的容器，如下所示：

```
$ docker ps -a
CONTAINER ID IMAGE COMMAND CREATED STATUS PORTS NAMES
ce554267d7a4 ubuntu:latest "/bin/sh -c 'while t" 3 minutes ago  Exited (-1) 13 seconds ago determined_piked58050081fe3 ubuntu:latest "/bin/bash" About an hour ago Exited (0) About an hour ago berserk_brattaine812617b41f6 ubuntu:latest "echo 'hello! I am h" 2 hours ago  Exited (0) 3 minutes ago silly_leakey
￼
```

分别导出ce554267d7a4容器和e812617b41f6容器到文件test_for_run.tar文件和test_for_stop.tar文件：
￼

```
$ docker export -o test_for_run.tar ce5
$ lstest_for_run.tar
$ docker export e81 >test_for_stop.tar$ lstest_for_run.tar test_for_stop.tar
```

￼之后，可将导出的tar文件传输到其他机器上，然后再通过导入命令导入到系统中，从而实现容器的迁移。

## 2.导入容器

导出的文件又可以使用docker import命令导入变成镜像，该命令格式为：

```
docker import [-c|--change[=[]]] [-m|--message[=MESSAGE]] file|URL|-[REPOSITORY [:TAG]]
```


用户可以通过`-c，--change=[] `选项在导入的同时执行对容器进行修改的Dockerfile指令。
下面将导出的test_for_run.tar文件导入到系统中：

```
$ docker import test_for_run.tar - test/ubuntu:v1.0
$ docker images
REPOSITORY TAG IMAGE ID CREATED VIRTUAL SIZE
```

￼


