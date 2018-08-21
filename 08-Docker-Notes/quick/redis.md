[返回目录](/README.md)

# 安装Redis

* 拉取指定版本

```
docker pull  redis:3.2
```

* 运行reids

```
docker run -p 6379:6379 -v $PWD/data:/data  -d --restart=always redis:3.2 redis-server --appendonly yes
```

* 参数

```
-p 6379:6379 : 将容器的6379端口映射到主机的6379端口

-v $PWD/data:/data : 将主机中当前目录下的data挂载到容器的/data

redis-server --appendonly yes : 在容器执行redis-server启动命令，并打开redis持久化配置
```

* 开启端口

```
firewall-cmd --zone=public --add-port=6379/tcp --permanent
```



