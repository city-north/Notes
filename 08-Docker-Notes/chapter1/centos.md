[返回首页](/README.md)

# CentOS

Docker 支持CentOS6以后的版本。

使用ContOS6系统可使用EPEL库安装Docker

```
sudo yum install -y http://mirrors.yun-idc.com/epel/6/i386/epel-release-6-8.noarch.rpm
sudo yum install -y docker-io
```

ContOS7

```
sudo yum install -y docker
```

启动docker服务

```
service docker start
```



