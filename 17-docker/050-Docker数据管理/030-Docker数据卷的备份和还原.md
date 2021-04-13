# 030-Docker数据卷的备份和还原

[TOC]

##  数据备份语法

```
docker run --volumes-from [container name] -v$(pwd):/backup ubuntu tar cvf/backup/backup.tar [container data volume]
```

例如:

```
docker run --valumes-from dvt5 -v ~/backup:/backup:wr --name dvt_10 ubantu tar cvf /backup/dvt5.tar /datavolume1
```

- `valumes-from` : 指定需要备份的容器的名字
- `-v` : 指定希望备份文件存放的位置
- `wr` : 读写方式
- `tar cvf` 压缩命令

数据还原语法
```
docker run --volumes-from [container name] -v$(pwd):/backup ubuntu tar xvf /backup/backup.tar [container data volume]

```

可以利用数据卷容器对其中的数据卷进行备份、恢复，以实现数据的迁移。下面介绍这两个操作。

## 1.备份

使用下面的命令来备份dbdata数据卷容器内的数据卷：

```
$ docker run --volumes-from dbdata -v $(pwd):/backup --name worker ubuntu tar cvf /backup/backup.tar /dbdata
```

￼
这个命令稍微有点复杂，具体分析一下。首先利用ubuntu镜像创建了一个容器worker。使用

```
--volumes-from dbdata 参数来让worker容器挂载dbdata容器的数据卷（即dbdata数据卷）；
```

使用

```
-v $(pwd):/backup 参数来挂载本地的当前目录到worker容器的/backup目录。
```


worker容器启动后，使用了`tar cvf/backup/backup.tar/dbdata`命令来将/dbdata下内容备份为容器内的/backup/backup.tar，即宿主主机当前目录下的backup.tar。

## 2.恢复

如果要将数据恢复到一个容器，可以按照下面的步骤操作。首先创建一个带有数据卷的容器dbdata2：￼

```
$ docker run -v /dbdata --name dbdata2 ubuntu /bin/bash
```

然后创建另一个新的容器，挂载dbdata2的容器，并使用untar解压备份文件到所挂载的容器卷中：

```
$ docker run --volumes-from dbdata2 -v $(pwd):/backup busybox tar xvf/backup/backup.tar
```

￼

