[返回目录](/README.md)

# Commit从容器创建一个新的镜像

```
docker commit [OPTIONS] CONTAINER [REPOSITORY[:TAG]]
```

OPTIONS说明：

* **-a :**提交的镜像作者；

* **-c :**使用Dockerfile指令来创建镜像；

* **-m :**提交时的说明文字；

* **-p :**在commit时，将容器暂停。

```
docker commit 85af807f5e19 hand/documentserver:1.6
```

[导出](/container/import&export.md)



```

```

[返回目录](/README.md)

