# 090-上传镜像-docker-push

[TOC]

可以使用docker push命令上传镜像到仓库，默认上传到Docker Hub官方仓库（需要登录）。命令格式为：

```
docker push NAME[:TAG] | [REGISTRY_HOST[:REGISTRY_PORT]/]NAME[:TAG]
```

￼

用户在Docker Hub网站注册后可以上传自制的镜像。例如用户user上传本地的test：latest镜像，可以先添加新的标签user/test：latest，然后用docker push命令上传镜像：

```
$ docker tag test:latest user/test:latest
$ docker push user/test:latest
The push refers to a repository [docker.io/user/test]
Sending image list
Please login prior to push:
Username:
Password:
Email:
```

第一次上传时，会提示输入登录信息或进行注册。