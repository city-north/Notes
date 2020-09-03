# Docker 安装 Redis


拉取镜像：`# docker pull redis:5.0`

启动容器：`# `
```
docker run -d -p 6379:6379 --name redis -v /hzero/data-server/redis/data:/data redis
```
运行 redis-clis
```
docker run -it --network some-network --rm redis redis-cli -h some-redis
```

格式化一下(仅仅是方便查看,运行命令用上面的):

```
docker run -d -p 6379:6379 
--name redis 
-v /hzero/data-server/redis/data:/data redis
redis-server --appendonly yes -- 开启持久化

```
例子：
```
docker run -d -p 6379:6379 --name redis -v /data/redis/data:/data redis redis-server --appendonly yes --
```


我的实践：
```
[root@192-168-17-12 redis]# pwd
/data/redis
[root@192-168-17-12 redis]# ls -l
total 12
drwxr-xr-x 2 root              root 4096 Aug  1 10:37 conf
drwxr-xr-x 2 systemd-bus-proxy root 4096 Aug  1 09:49 data
-rw-r--r-- 1 root              root  115 Aug  1 09:52 README.txt
```


```
docker run -d -p 6379:6379 --name redis -v /data/redis/data:/data -v /data/redis/conf/redis.conf:/usr/local/etc/redis/redis.conf redis --appendonly yes --

```
配置文件看Redis配置文件笔记






```
[数据地址]
/data/database/redis/data
[配置地址]
/data/database/redis/conf
[启动命令]
docker run -d -p 6379:6379 --name redis -v /data/database/redis/data:/data -v /data/database/redis/conf/redis.conf:/usr/local/etc/redis/redis.conf redis --appendonly yes --
```


## 主从复制