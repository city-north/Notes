## 系统要求

* **CPU:**dual core**2 GHz**or better

* **RAM:2 GB**or more

* **HDD:**at least**40 GB**of free space

* **Additional Requirements:**at least**2 GB**of swap

* **OS:amd64**Linux distribution with kernel version**3.10**or later

* **Docker:**version**1.10**or later

## 导入documentServer

* 创建onlyoffice 存储地址

```
sudo mkdir -p "/app/onlyoffice/DocumentServer/data";
sudo mkdir -p "/app/onlyoffice/DocumentServer/logs";
```

* 安装DocumentServer服务器

```
sudo docker load < 存储位置/hand-documentserver-1.5.tar
```

* 运行DocumentServer

```
sudo docker run -i -t -d -p 8765:80 --restart=always \
  -v /app/onlyoffice/DocumentServer/logs:/var/log/onlyoffice  \
  -v /app/onlyoffice/DocumentServer/data:/var/www/onlyoffice/Data  \
  -v /app/onlyoffice/DocumentServer/lib:/var/lib/onlyoffice \
  -v /app/onlyoffice/DocumentServer/db:/var/lib/postgresql  hand/documentserver:1.5
```

* 配置系统中config.propertis



