[返回目录](/README.md)

# 获取镜像

镜像是Docker运行容器的前提

pull命令格式：

```
sudo docker pull NAME[:TAG]
```

如果不指定TAG，就默认选择latest标签。

```
sudo docker pull ubuntu
```

指定版本：

```
sudo docker pull ubuntu:14.04
```

指定其他注册服务器仓库：例如DockerPool社区：

```
sudo docker pull dl.dockerpool.com:5000/ubuntu
```



