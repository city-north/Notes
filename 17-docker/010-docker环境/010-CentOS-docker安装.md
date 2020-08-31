# CentOS docker 安装

> 环境
>
> - CentOS 7.6

## 更新yum源

```
sudo yum update
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

## 