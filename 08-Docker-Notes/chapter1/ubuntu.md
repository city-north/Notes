[返回首页](/README.md)

# Ubuntu

## 14.04以上版本

安装Ubantu自带包

```
sudo apt-get update
sudo apt-get install -y docker.io
sudo ln -sf /usr/bin/docker.io /usr/local/bin/docker
sudo sed -i '$acomplete -F_docker docker' /etc/bash_completion.d/docker.io
```

安装最新包

```
//卸载旧版本
sudo apt-get remove docker docker-engine docker-ce docker.io
//更新索引
sudo apt-get update
//使apt可以通过HTTPS使用存储库
sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common
//添加Docker官方的GPG密钥：
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
//设置stable存储库
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
//更新索引
sudo apt-get update
//安装最新版的Docker CE
sudo apt-get install -y docker-ce
```

安装指定版本：

```
apt-cache madison docker-ce
//安装指定版本
sudo apt-get install docker-ce=<VERSION>
```

## Ubuntu 14.04一下的版本

```
sudo apt-get update
sudo apt-get insatll -y linux-image-generic-lts-raring linux-headers-generic-lts-raring
sudo reboot
----
重启后重复Ubantu14.04系统安装步骤
```

## 验证docker

查看Docker服务是否启动

```
sudo systemctl status docker
```

若未启动，则启动docker服务

`sudo systemctl start docker`

## 配置镜像加速器

通过修改daemon配置文件`/etc/docker/daemon.json`来使用加速器：

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

```

```



