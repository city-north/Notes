[返回目录](/README.md)

# 安装MySQL

## 使用docker hub镜像在线安装

* 拉下来镜像

```
docker pull mysql:5.7
```

* 运行镜像

```
 docker run \
--net=host \
--restart=always \
--privileged=true \
-v /usr/docker_dat/mysql/data:/var/lib/mysql \
--name mysql \
-p 3306:3306 \
-e MYSQL_ROOT_PASSWORD=root \
-v /etc/localtime:/etc/localtime:ro \
-d mysql:5.7 --lower_case_table_names=1 --character_set_server=utf8 --max_connections=500
```

* 参数说明

```
--restart=always 跟随docker启动  
--privileged=true 容器root用户享有主机root用户权限  
-v 映射主机路径到容器  
-e MYSQL_ROOT_PASSWORD=root 设置root用户密码  
-d 后台启动  
--lower_case_table_names=1 设置表名参数名等忽略大小写 
-v /etc/localtime:/etc/localtime:ro   设置容器的时间与宿主机同步
```



