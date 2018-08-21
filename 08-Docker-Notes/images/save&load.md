[返回目录](/README.md)

# 存储和载入镜像

### 存入镜像

语法：

```
sudo docker save -o documentserver.tar onlyoffice/documentserver:latest
```

### 载入镜像

语法：

```
sudo docker load --input documentserver.tar
或者
sudo docker load < documentserver.tar
```



