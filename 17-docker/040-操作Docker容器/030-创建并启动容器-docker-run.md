# 030-创建并启动容器-docker-run

[TOC]

## 新建并启动容器

`docker [container] run` 等价于

- 先执行docker [container] create 
- 再执行docker [container] start

例如：下面的命令输出一个“hello world”，之后容器自动停止

```
$ docker run ubuntu /bin/echo "hello world"
hello world
12
```

这和本地直接执行`/bin/echo 'hello world'`相比几乎感觉不出任何区别

docker [container] run 来创建并启动容器时，Docker在后台运行的标准包括：

- 检查本地是否存在指定的镜像，不存在就从共有仓库下载；
- 利用镜像创建一个容器，并启动该容器；
- 分配一个文件系统给容器，并在只读的镜像层外面挂载一层可写层；
- 从宿主主机配置的网桥接口中桥接一个虚拟接口到容器中去；
- 从网桥的地址池配置一个IP地址给容器；
- 执行用户指定的应用程序；
- 执行完毕后容器被自动终止。

下面的命令启动一个bash终端，允许用户进行交互：

```
$ docker run -it ubuntu:18.04 /bin/bash
root@....:/#
12
```

其中，`-t` 选项让Docker分配一个伪终端并绑定到容器的标准输出上，`-i` 则让容器的标准输入保持打开。更多的命令选项可以通过 `man docker-run` 命令来查看。

对于所创建的bash容器，当用户使用exit命令退出bash进程后，容器也会自动退出。这是因为对于容器来说，当其中的应用退出后，容器的使命完成，也就没有继续运行的必要了。

可以通过

```
docker container wait CONTAINER [CONTAINER…] 
```

子命令来等待容器退出，并打印退出返回结果。

某些时候，执行`docker [container] run` 时候因为命令无法正常执行，容器会出错直接退出，此时可以查看退出的错误代码。

默认情况下，错误代码包括：

- 125：Docker deamon 执行出错，例如指定了不支持的Docker命令参数；
- 126：所指定命令无法执行，例如权限出错；
- 127：容器内命令无法找到。

命令执行后出错，会默认返回命令的退出错误码。

## 守护态运行

更多时候，需要让Docker容器在后台以守护态形式运行。此时，可以通过添加`-d`参数来实现。

容器启动后会返回一个唯一的id，也可以通过`docker ps` 或 `docker container ls` 命令来查看容器信息

## 查看容器输出

要获取容器的输出信息，可以通过`docker [container] logs` 命令。

该命令支持的选项包括：

- `-details`：打印详细信息；
- `-f`，`-follow`：持续保持输出；
- `-since string`：输出从某个时间开始的日志；
- `-tail string`：输出最近的若干日志；
- `-t`，`-timestamps`：显示时间戳信息；
- `-until string`：输出某个时间之前的日志。

例如，查看某容器的输出可以使用如下命令：

```
$ docker logs ......
```


## 例子

#### 一、创建一个容器并进入容器内部

```
docker run -it --name mycentos centos:7 /bin/bash
```

- 创建一个交互式容器并取名字为`mycentos`
- `/bin/bash`是 Linux 命令解析器,进入容器内部
- `exit` 退出 退出容器内的命令行,关闭容器
- `Ctrl + p + q ` 退出容器内的命令行,但不关闭容器

#### 二、创建一个后台运行的容器,挂在目录

语法:

```
docker run -id -v 宿主机目录:容器目录
```

```
docker run -id -v /opt:/opt --name mycentos3 centos:7

```

创建容器并挂在映射目录

```
sudo docker run -i -t -d -p 8765:80 --name ds --restart=always \
  -v /app/onlyoffice/DocumentServer/logs:/var/log/onlyoffice  \
  -v /app/onlyoffice/DocumentServer/data:/var/www/onlyoffice/Data  \
  -v /app/onlyoffice/DocumentServer/lib:/var/lib/onlyoffice \
  -v /app/onlyoffice/DocumentServer/db:/var/lib/postgresql  hand/documentserver:1.8
  
```

- `-v`挂在宿主的目录到容器的内部

#### 三 挂在宿主目录时使用只读模式

```
docker run -id -v /宿主机绝对路径目录:/容器内目录:ro --name 容器名 镜像名
```

例如

```
docker run -id -v /dataHost:/dataContainer:ro --name mycentos4 centos:7
```