# Docker 安装 RabbitMQ

## 方式一
创建镜像(默认用户名密码),远程连接端口5672，管理系统访问端口15672，默认用户名: guest ，密
码也是 guest

```
docker run -id --name=rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:management
```
## 方式二
启动镜像(设置用户名和密码)

```
docker run -id --name=mxg_rabbitmq2 -e RABBITMQ_DEFAULT_USER=username -e
RABBITMQ_DEFAULT_PASS=password -p 5672:5672 -p 15672:15672 rabbitmq:management

```
格式化一下:

```
docker run -id 
--name=rabbitmq2 
-e RABBITMQ_DEFAULT_USER=username 
-e RABBITMQ_DEFAULT_PASS=password 
-p 5672:5672 
-p 15672:15672 
rabbitmq:management
```

访问管理界面的地址是 http://[宿主机IP]:15672 , 如:http://192.168.10.11:15672，默认 guest 用户，密码 也是 guest。