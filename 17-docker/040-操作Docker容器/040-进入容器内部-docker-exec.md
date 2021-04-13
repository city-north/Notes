# 040-进入容器内部-docker-exec

[TOC]

## 命令格式

```
docker exec -it 容器 ID|容器名称 /bin/bash
```
参数
- `-d` , `--detach` :在容器中后台执行名
- `--detach-keys=""`:指定将容器切回后台的按键
- `-e`,`--env=[]`:指定环境变量列表
- `-i`,`--interactive=true|false`:打开标准输入接受用户名命令,默认为 false
- `-t`,`--tty=true|false`:分配伪终端,默认为 false
- `-u`,`--user==""` :执行命令的用户名或者 ID


例如:
```
docker exec -it mycentos2 /bin/bash
```
## 宿主与容器互相拷贝文件

```
docker cp 要拷贝的宿主主机文件或者目录 容器名称:容器文件或者目录
```

例如:
拷贝`/usr/share/fonts` 文件夹到`/tmp/onlyoffice` 文件夹
```
docker cp  8cd25f0f9a3f:/usr/share/fonts /tmp/onlyoffice
```
