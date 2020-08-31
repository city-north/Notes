# Docker添加镜像加速

#### 注意:分享给别人的时候应该使用链接分享

https://cr.console.aliyun.com/cn-hangzhou/instances/mirrors

## CentOS/Ubuntu
针对Docker客户端版本大于 1.10.0 的用户

您可以通过修改daemon配置文件/etc/docker/daemon.json来使用加速器

```
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://ttfqqexg.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```

## Mac

对于10.10.3以下的用户 推荐使用Docker Toolbox

Mac安装文件：http://mirrors.aliyun.com/docker-toolbox/mac/docker-toolbox/

对于10.10.3以上的用户 推荐使用Docker for Mac

Mac安装文件：http://mirrors.aliyun.com/docker-toolbox/mac/docker-for-mac/

#### 配置镜像加速器
针对安装了Docker Toolbox的用户，您可以参考以下配置步骤：
创建一台安装有Docker环境的Linux虚拟机，指定机器名称为default，同时配置Docker加速器地址。

docker-machine create --engine-registry-mirror=https://ttfqqexg.mirror.aliyuncs.com -d virtualbox default
查看机器的环境配置，并配置到本地，并通过Docker客户端访问Docker服务。

```
docker-machine env default
eval "$(docker-machine env default)"
docker info
```
针对安装了Docker for Mac的用户，您可以参考以下配置步骤：
右键点击桌面顶栏的 docker 图标，选择 Preferences ，在 Daemon 标签（Docker 17.03 之前版本为 Advanced 标签）下的 Registry mirrors 列表中将

https://ttfqqexg.mirror.aliyuncs.com加到"registry-mirrors"的数组里，点击 Apply & Restart按钮，等待Docker重启并应用配置的镜像加速器。



## Windows

1. 安装／升级Docker客户端
对于Windows 10以下的用户，推荐使用Docker Toolbox
Windows安装文件：http://mirrors.aliyun.com/docker-toolbox/windows/docker-toolbox/
对于Windows 10以上的用户 推荐使用Docker for Windows

Windows安装文件：http://mirrors.aliyun.com/docker-toolbox/windows/docker-for-windows/

2. 配置镜像加速器
针对安装了Docker Toolbox的用户，您可以参考以下配置步骤：
创建一台安装有Docker环境的Linux虚拟机，指定机器名称为default，同时配置Docker加速器地址。

docker-machine create --engine-registry-mirror=https://ttfqqexg.mirror.aliyuncs.com -d virtualbox default
查看机器的环境配置，并配置到本地，并通过Docker客户端访问Docker服务。
```
docker-machine env default
eval "$(docker-machine env default)"
docker info
```
针对安装了Docker for Windows的用户，您可以参考以下配置步骤：
在系统右下角托盘图标内右键菜单选择 Settings，打开配置窗口后左侧导航菜单选择 Docker Daemon。编辑窗口内的JSON串，填写下方加速器地址：
```
{
  "registry-mirrors": ["https://ttfqqexg.mirror.aliyuncs.com"]
}
```

编辑完成后点击 Apply 保存按钮，等待Docker重启并应用配置的镜像加速器。

注意
Docker for Windows 和 Docker Toolbox互不兼容，如果同时安装两者的话，需要使用hyperv的参数启动。
docker-machine create --engine-registry-mirror=https://ttfqqexg.mirror.aliyuncs.com -d hyperv default
Docker for Windows 有两种运行模式，一种运行Windows相关容器，一种运行传统的Linux容器。同一时间只能选择一种模式运行。