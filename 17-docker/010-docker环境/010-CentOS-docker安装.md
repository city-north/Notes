# CentOS docker 安装

> 环境
>
> - CentOS 7.6

## 更新yum源

```
sudo yum update
yum -y install yum-utils
```

## 添加仓库

```
sudo yum-config-manager \
    --add-repo \
    https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
```

## 查看最新版本

如果之前安装了docker，需要卸载旧版本

```
yum list docker-ce --showduplicates | sort -r
```

## 安装Docker CE版本

```
yum install docker-ce -y
```

## 开机自启动

https://docs.docker.com/engine/install/linux-postinstall/

## 修改存储位置

```
# 关闭docker服务
systemctl stop docker.service

# 移动数据到新的目录
mv /var/lib/docker /export/docker

# 修改docker.service文件，使用-g参数指定存储位置

vi /usr/lib/systemd/system/docker.service  
ExecStart=/usr/bin/dockerd --graph /new-path/docker 

# reload配置文件 
systemctl daemon-reload 

# 重启docker 
systemctl restart docker.service

# 查看数据目录
docker info | grep Dir
```