# Docker 数据卷的备份和还原

[TOC]

##  数据备份方法

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

数据还原
```
docker run --volumes-from [container name] -v$(pwd):/backup ubuntu tar xvf /backup/backup.tar [container data volume]

```