[返回目录](/README.md)

# 上传镜像

### 上传镜像到DockerHub官方仓库

命令：

```
sudo docker push NAME[:TAG]
```

例子：用户user上传本地的test:latest镜像，可以先添加新的标签user/test:latest,然后用命令上传

```
sudo docker tag test:latest user/test:latest
sudo docker push user/test:latest
```



