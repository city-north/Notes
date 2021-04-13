# 060-删除容器-docker-rm

可以使用docker rm命令来删除处于终止或退出状态的容器，命令格式为

```
docker rm[-f|--force][-l|--link][-v|--volumes]CONTAINER[CONTAINER...]
```


主要支持的选项包括：

·-f，--force=false：是否强行终止并删除一个运行中的容器；
·-l，--link=false：删除容器的连接，但保留容器；
·-v，--volumes=false：删除容器挂载的数据卷。

例如，查看处于终止状态的容器，并删除：
￼

```
$ docker ps -aCONTAINER ID IMAGE COMMAND CREATED STATUS PORTS  NAMES
ce554267d7a4 ubuntu:latest "/bin/sh -c 'while t 3 minutes ago Exited (-1) 13 seconds ago determined_piked58050081fe3 ubuntu:latest "/bin/bash" About an hour ago Exited (0) About an hour ago berserk_brattaine812617b41f6 ubuntu:latest "echo 'hello! I am h 2 hours ago  Exited (0) 3 minutes ago
$ docker rm ce554267d7a4ce554267d7a4￼
```

默认情况下，docker rm命令只能删除处于终止或退出状态的容器，并不能删除还处于运行状态的容器。

如果要直接删除一个运行中的容器，可以添加-f参数。Docker会先发送SIGKILL信号给容器，终止其中的应用，之后强行删除，如下所示：

```
$ docker run -d ubuntu:14.04 /bin/sh -c "while true; do echo hello world; sleep 1; done"2aed76caf8292c7da6d24c3c7f3a81a135af942ed1707a79f85955217d4dd594

$ docker rm 2aeError response from daemon: You cannot remove a running container. Stop the container before attempting removal or use -f2016/07/03 09:02:24 Error: failed to remove one or more containers

$ docker rm -f 2ae2ae
￼
```



