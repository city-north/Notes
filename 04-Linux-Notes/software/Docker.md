## CentOS7 安装docker

* 卸载老版本docker以及相关依赖

```
  sudo yum remove docker docker-common container-selinux docker-selinux docker-engine
```

* 【可跳过】安装 yum-utils，它提供了 yum-config-manager，可用来管理yum源

```
  sudo yum install -y yum-utils
```

* 添加yum源

```
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
```

* 更新索引

```
sudo yum makecache fast
```

* 安装 docker-ce

```
sudo yum install docker-ce
```

* 启动 docker

```
sudo systemctl start docker
```

* 验证是否安装成功

```
sudo docker info
```

* 开机自启动

```
sudo systemctl enable docker
```

* 如果需要加速器【阿里云】

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

## 移植documentServer

* 创建onlyoffice 存储地址

```
sudo mkdir -p "/app/onlyoffice/DocumentServer/data";
sudo mkdir -p "/app/onlyoffice/DocumentServer/logs";
```

* 创建onlyoffice桥接网络

```
sudo docker network create --driver bridge onlyoffice
```

* 安装DocumentServer服务器

```
sudo docker run --net onlyoffice -i -t -d --restart=always --name onlyoffice-document-server \
    -v /app/onlyoffice/DocumentServer/logs:/var/log/onlyoffice  \
    -v /app/onlyoffice/DocumentServer/data:/var/www/onlyoffice/Data  \
    -v /app/onlyoffice/DocumentServer/lib:/var/lib/onlyoffice \
    -v /app/onlyoffice/DocumentServer/db:/var/lib/postgresql \
    onlyoffice/documentserver
```

* 查看documentServer服务器ip

```
docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' onlyoffice-document-server
```

或者

```
EricChen>docker network ls
NETWORK ID          NAME                DRIVER              SCOPE
a987030bfe4d        bridge              bridge              local
5e637bd1a635        host                host                local
e7e9a6255dc2        none                null                local
5f8f14e2b50f        onlyoffice          bridge              local

docker network inspect onlyoffice
```



