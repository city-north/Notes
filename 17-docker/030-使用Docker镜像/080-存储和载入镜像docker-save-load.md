# 080-存储和载入镜像docker-save-load

[TOC]

用户可以使用docker save和docker load命令来存出和载入镜像。

## 1.存出镜像

如果要导出镜像到本地文件，可以使用docker save命令。例如，导出本地的ubuntu：14.04镜像为文件ubuntu_14.04.tar，如下所示：

```
$ docker images
REPOSITORY TAG IMAGE ID CREATED VIRTUAL SIZE
ubuntu 14.04 8f1bd21bd25c 2 weeks ago 188 MB...
$ docker save -o ubuntu_14.04.tar ubuntu:14.04
```

之后，用户就可以通过复制ubuntu_14.04.tar文件将该镜像分享给他人。

## 2.载入镜像

可以使用docker load将导出的tar文件再导入到本地镜像库，例如从文件ubuntu_14.04.tar导入镜像到本地镜像列表，如下所示：

```
$ docker load --input ubuntu_14.04.tar
```

或：

```
$ docker load < ubuntu_14.04.tar
```

这将导入镜像及其相关的元数据信息（包括标签等）。导入成功后，可以使用docker images命令进行查看。

