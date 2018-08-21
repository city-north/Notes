[返回目录](/README.md)

## 删除镜像

查看所有正在运行得容器

```
docker ps -all
```

删除容器

```
docker rm [container ID]
```

删除镜像：

```
sudo docker rmi [IMAGE ID]
```

强制删除命令：

```
sudo docker rmi -f [IMAGE ID]
```



