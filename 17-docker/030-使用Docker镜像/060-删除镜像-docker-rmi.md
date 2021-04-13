# 060-删除镜像-docker-rmi

[TOC]

## 1.使用标签删除镜像

使用docker rmi命令可以删除镜像，命令格式为

```
docker rmi IMAGE[IMAGE...]
```

其中IMAGE可以为标签或ID。

例如，要删除掉myubuntu：latest镜像，可以使用如下命令：

```
$ docker rmi myubuntu:latestUntagged: myubuntu:latest
```

￼
读者可能会担心，本地的ubuntu：latest镜像是否会受此命令的影响。无需担心，当同一个镜像拥有多个标签的时候，docker rmi命令只是删除该镜像多个标签中的指定标签而已，并不影响镜像文件。因此上述操作相当于只是删除了镜像2fa927b5cdd3的一个标签而已。

为保险起见，再次查看本地的镜像，发现ubuntu：latest镜像（准确地说是2fa927b5cdd3镜像）仍然存在：
￼

```
$ docker imagesREPOSITORY TAG IMAGE ID CREATED SIZE
ubuntu 16.04 2fa927b5cdd3 2 weeks ago 122 MBubuntu latest 2fa927b5cdd3 2 weeks ago 122 MBubuntu 14.04 8f1bd21bd25c 2 weeks ago 188 MB
```

但当镜像只剩下一个标签的时候就要小心了，此时再使用docker rmi命令会彻底删除镜像。
例如删除标签为ubuntu：14.04的镜像，由于该镜像没有额外的标签指向它，执行docker rmi命令，可以看出它会删除这个镜像文件的所有层：

```java
$ docker rmi ubuntu:14.04 Untagged: ubuntu:14.04Deleted: sha256:8f1bd21bd25c3fb1d4b00b7936a73a0664f932e11406c48a0ef19d82fd0b7342Deleted: sha256:8ea3b9ba4dd9d448d1ca3ca7afa8989d033532c11050f5e129d267be8de9c1b4Deleted: sha256:7db5fb90eb6ffb6b5418f76dde5f685601fad200a8f4698432ebf8ba80757576Deleted: sha256:19a7e879151723856fb640449481c65c55fc9e186405dd74ae6919f88eccce75Deleted: sha256:c357a3f74f16f61c2cc78dbb0ae1ff8c8f4fa79be9388db38a87c7d8010b2fe4Deleted: sha256:a7e1c363defb1f80633f3688e945754fc4c8f1543f07114befb5e0175d569f4c
```

## ￼2.使用镜像ID删除镜像

当使用docker rmi命令，并且后面跟上镜像的ID（也可以是能进行区分的部分ID串前缀）时，会先尝试删除所有指向该镜像的标签，然后删除该镜像文件本身。

注意，当有该镜像创建的容器存在时，镜像文件默认是无法被删除的，例如，先利用ubuntu：14.04镜像创建一个简单的容器来输出一段话：

```
$ docker run ubuntu:14.04 echo 'hello! I am here!'hello! I am here!
```

使用docker ps-a命令可以看到本机上存在的所有容器：
可以看到，后台存在一个退出状态的容器，是刚基于ubuntu：14.04镜像创建的。
￼

```
$ docker ps -a
CONTAINER ID IMAGE COMMAND CREATED STATUS PORTS NAMES
a21c0840213e ubuntu:14.04 "echo 'hello! I am he" About a minute ago Exited (0) About a minute ago romantic_euler
```

￼
试图删除该镜像，Docker会提示有容器正在运行，无法删除：

```
$ docker rmi ubuntu:14.04Error response from daemon: conflict: unable to remove repository reference "ubuntu: 14.04" (must force) - container a21c0840213e is using its referenced image 8f1bd21bd25c
```

如果要想强行删除镜像，可以使用-f参数。

```
$ docker rmi -f ubuntu:14.04Untagged: ubuntu:14.04Deleted: sha256:8f1bd21bd25c3fb1d4b00b7936a73a0664f932e11406c48a0ef19d82fd0b7342
```

￼
注意，通常并不推荐使用-f参数来强制删除一个存在容器依赖的镜像。正确的做法是，先删除依赖该镜像的所有容器，再来删除镜像。首先删除容器a21c0840213e：

```
$ docker rm a21c0840213e
```

再使用ID来删除镜像，此时会正常打印出删除的各层信息：

```
Untagged: ubuntu:14.04Deleted: sha256:8f1bd21bd25c3fb1d4b00b7936a73a0664f932e11406c48a0ef19d82fd0b7342Deleted: sha256:8ea3b9ba4dd9d448d1ca3ca7afa8989d033532c11050f5e129d267be8de9c1b4Deleted: sha256:7db5fb90eb6ffb6b5418f76dde5f685601fad200a8f4698432ebf8ba80757576Deleted: sha256:19a7e879151723856fb640449481c65c55fc9e186405dd74ae6919f88eccce75Deleted: sha256:c357a3f74f16f61c2cc78dbb0ae1ff8c8f4fa79be9388db38a87c7d8010b2fe4Deleted: sha256:a7e1c363defb1f80633f3688e945754fc4c8f1543f07114befb5e0175d569f4c
```

￼
