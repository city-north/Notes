# 050-终止容器-docker-stop

[TOC]

可以使用docker stop来终止一个运行中的容器。该命令的格式为

```
docker stop[-t|--time[=10]][CONTAINER...]
```


首先向容器发送SIGTERM信号，等待一段超时时间（默认为10秒）后，再发送SIGKILL信号来终止容器：

```
￼$ docker stop ce5ce5
```

## ￼注意

docker kill命令会直接发送SIGKILL信号来强行终止容器。

此外，当Docker容器中指定的应用终结时，容器也会自动终止。例如对于上一节中只启动了一个终端的容器，用户通过exit命令或Ctrl+d来退出终端时，所创建的容器立刻终止，处于stopped状态。

可以用docker ps-qa命令看到所有容器的ID。例如：￼

```
$ docker ps -qace554267d7a4d58050081fe3e812617b41f6
```


处于终止状态的容器，可以通过docker start命令来重新启动：

```
$ docker start ce5ce5
$ docker ps
CONTAINER ID IMAGE COMMAND CREATED STATUS PORTS NAMES
ce554267d7a4 ubuntu:latest "/bin/sh -c 'while t 4 minutes ago Up 5 seconds determined_pike
```

此外，docker restart命令会将一个运行态的容器先终止，然后再重新启动它：

```
$ docker restart ce5ce5
$ docker ps
CONTAINER ID IMAGE COMMAND CREATED STATUS PORTS NAMESce554267d7a4 ubuntu:latest "/bin/sh -c 'while t 5 minutes ago Up 14 seconds determined_pike
```

