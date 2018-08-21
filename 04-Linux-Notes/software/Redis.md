[返回目录](/README.md)

# Redis安装

* Redis版本：3.2.9   （[redis-3.2.9.tar.gz](http://download.redis.io/releases/redis-3.2.9.tar.gz)）
* 配置文件位置： /etc/redis/6379.conf
* redis-server位置：/usr/local/bin/redis-server

## 开始安装

* 安装

```
yum install gcc-c++
```

* 解压，编译

```
tar -zxvf redis-3.2.9.tar.gz
cd redis-3.2.9
make
```

* 安装

```
cd src/
make install
```

* 修改配置文件redis.conf中daemonize为yes,确保守护进程开启。

```
vim /root/redis-3.2.9/redis.conf
```

* 复制redis.conf到/etc/redis/6379.conf

  ```
  mkdir /etc/redis/
  cp /root/redis-3.2.9/redis.conf /etc/redis/6379.conf
  ```

* 修改启动脚本

  ```
  vim /root/redis-3.2.9/utils/redis_init_script
  ```

* \#在启动脚本开头\#!/bin/sh下添加如下两行注释以修改其运行级别

```
# chkconfig:   2345 90 10
# description:  Redis is a persistent key-value database
```

* 将启动脚本复制到/etc/init.d目录下

```
cp /root/redis-3.2.9/utils/redis_init_script  /etc/init.d/redisd
```

* 开机自启

```
chkconfig redisd on
```

* 测试

```
#打开服务
service redisd start

cd /usr/local/bin/
redis-cli
127.0.0.1:6379> ping
PONG


#关闭服务
service redisd stop

# 重启服务器
reboot

ps -ef |grep redis
```

* 远程连接解决方案

* 修改配置文件

```
vim /etc/redis/6379.conf
```

* 两个属性:

`注释掉bind 127.0.0.1可以使所有的ip访问redis`

`protected-mode no`

* bind属性：绑定多个端口

例如：bind 127.0.0.1 172.16.1.192   //绑定多个端口用空格

```

```

[返回目录](/README.md)

