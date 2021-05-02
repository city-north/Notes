# 080-Docker容器资源限制

[TOC]

## 为什么要对容器资源限制

如果不对container的资源做限制，它就会无限制地使用物理机的资源，这样显然是不合适的。

### 查看资源情况

```
docker stats 2.2.2.1
```

###  内存限制

```
 --memory Memory limit
  如果不设置 --memory-swap，其大小和memory一样
   docker run -d --memory 100M --name tomcat1 tomcat
```

## CPU限制

```
 --cpu-shares 权重
```

```
docker run -d --cpu-shares 10 --name tomcat2 tomcat
```

## 图形化资源监控Scope

```
 sudo curl -L git.io/scope -o /usr/local/bin/scope
```

```
 sudo chmod a+x /usr/local/bin/scope
```

```
 scope launch 39.100.39.63
 # 停止scope 
 scope stop
 # 同时监控两台机器，在两台机器中分别执行如下命令 scope launch ip1 ip2
```



 

