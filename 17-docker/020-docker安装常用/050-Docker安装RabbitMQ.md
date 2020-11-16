# Docker 安装 RabbitMQ

## 方式一

1. 拉取RabbitMQ镜像（带managment）

```
docker pull rabbitmq:3.7.17-management
```

2. 创建docker网络（让容器可以和主机通信）

```
docker network create rabbitmqnet
```

3. 创建三个容器，端口分别是 5673 5674 5675 ，管理端口是 15673 15674 15675

```
docker run -d \
 --name=rabbitmq1 \
 -p 5673:5672 \
 -p 15673:15672 \
 -e RABBITMQ_NODENAME=rabbitmq1 \
 -e RABBITMQ_ERLANG_COOKIE='GUPAOEDUFORBETTERYOU' \
 -h rabbitmq1 \
 --net=rabbitmqnet \
 rabbitmq:management
```

```java
docker run -d \
 --name=rabbitmq2 \
 -p 5674:5672 \
 -p 15674:15672 \
 -e RABBITMQ_NODENAME=rabbitmq1 \
 -e RABBITMQ_ERLANG_COOKIE='GUPAOEDUFORBETTERYOU' \
 -h rabbitmq2 \
 --net=rabbitmqnet \
 rabbitmq:management
```

```d
docker run -d \
 --name=rabbitmq3 \
 -p 5675:5672 \
 -p 15675:15672 \
 -e RABBITMQ_NODENAME=rabbitmq1 \
 -e RABBITMQ_ERLANG_COOKIE='GUPAOEDUFORBETTERYOU' \
 -h rabbitmq3 \
 --net=rabbitmqnet \
 rabbitmq:management
```

4. 后两个节点作为内存节点加入集群

```
docker exec -it rabbitmq2 /bin/bash
rabbitmqctl stop_app
rabbitmqctl reset
rabbitmqctl join_cluster --ram rabbitmq1@rabbitmq1
rabbitmqctl start_app
docker exec -it rabbitmq3 /bin/bash
rabbitmqctl stop_app
rabbitmqctl reset
rabbitmqctl join_cluster --ram rabbitmq1@rabbitmq1
rabbitmqctl start_app
```

访问：

```
http://127.0.0.1:15673/
guest/guest登录
```



