[返回目录](/README.md)

# 创建镜像

1. 基于已有镜像创建

2. 基于本地模板导入

3. 基于Dockerfile创建

## 1.基于已有镜像创建

命令：

```
docker commit [OPTIONS] CONTAINER [REPOSITORY[:TAG]]
```

* -a , --author=""作者信息
* -m, --message=""提交信息
* -p, --pause=true 提交时暂停容器信息

```
sudo docker commit -m "Added a new file" -a "Docker Newbee" a925cb40b3f0 onlyoffice/documentserver:5.5.5
```

## 2.基于本地模板导入

* 下载模板。推荐OpenVZ提供的模板，下载地址（[http://openvz.org/Download/templates/precreated](http://openvz.org/Download/templates/precreated)）
* 使用命令导入：

```
sudo cat ubuntu-14.04-x86_64-minimal.tar.gz |docker import - ubuntu:14.04
```

* 使用命令查看新导入的镜像：

```
sudo docker images
```

## 3.基于Dockerfile创建

## 



