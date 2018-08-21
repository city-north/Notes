[返回目录](/README.md)

# 创建容器

## 只创建容器

命令：

```
//创建
sudo create -it ubuntu:latest
```

创建后容器处于停止状态，使用以下命令启动它

```
sudo docker start [容器ID]
```

### 新建并启动容器

命令：

```
sudo docker run -t -i ubuntu:14.04 /bin/bash
root@af8bae53bdd3:/#
```

* -t选项让Docker分配一个伪终端（pseudo-tty）并绑定到容器的标准输入上，-i则让容器的标准输入保持打

利用Docker run创建并启动容器时，Docker在后台运行得标准操作包括：

* 检查本地是否存在指定镜像，不存在就从共有仓库中下载
* 利用镜像创建并启动一个容器
* 分配一个文件系统，并在只读的镜像层外面挂载一层可读写层
* 从宿主主机配置的网桥接口中桥接一个虚拟接口道容器中去
* 从地址池配置一个IP地址给容器
* 执行用户指定的应用程序
* 执行完毕后容器被终止

### 守护态运行

Docker容器在后台以**守护态\(Daemonized\)**形式运行，用户可以通过添加-d参数来实现**。**

```
sudo docker run -i -t -d -p 8888:80 --restart=always \
    -v /app/onlyoffice/DocumentServer/logs:/var/log/onlyoffice  \
    -v /app/onlyoffice/DocumentServer/data:/var/www/onlyoffice/Data  \
    -v /app/onlyoffice/DocumentServer/lib:/var/lib/onlyoffice \
    -v /app/onlyoffice/DocumentServer/db:/var/lib/postgresql  onlyoffice/documentserver
```

* **-a stdin:**指定标准输入输出内容类型，可选 STDIN/STDOUT/STDERR 三项；

* **-d:**后台运行容器，并返回容器ID；

* **-i:**以交互模式运行容器，通常与 -t 同时使用；

* **-t:**为容器重新分配一个伪输入终端，通常与 -i 同时使用；

* **--name="nginx-lb":**为容器指定一个名称；

* **--dns 8.8.8.8:**指定容器使用的DNS服务器，默认和宿主一致；

* **--dns-search example.com:**指定容器DNS搜索域名，默认和宿主一致；

* **-h "mars":**指定容器的hostname；

* **-e username="ritchie":**设置环境变量；

* **--env-file=\[\]:**从指定文件读入环境变量；

* **--cpuset="0-2" or --cpuset="0,1,2":**绑定容器到指定CPU运行；

* **-m :**设置容器使用内存最大值；

* **--net="bridge":**指定容器的网络连接类型，支持 bridge/host/none/container:四种类型；

* **--link=\[\]:**添加链接到另一个容器；

* **--expose=\[\]:**开放一个端口或一组端口；

#### 实例

使用docker镜像nginx:latest以后台模式启动一个容器,并将容器命名为mynginx

```
docker run --name mynginx -d nginx:latest
```

使用镜像nginx:latest以后台模式启动一个容器,并将容器的80端口映射到主机随机端口。

```
docker run -P -d nginx:latest
```

使用镜像nginx:latest以后台模式启动一个容器,将容器的80端口映射到主机的80端口,主机的目录/data映射到容器的/data。

```
docker run -p 80:80 -v /data:/data -d nginx:latest
```

使用镜像nginx:latest以交互模式启动一个容器,在容器内执行/bin/bash命令。

```
runoob@runoob:~$ docker run -it nginx:latest /bin/bash
root@b8573233d675:/# 
```

[实例原文](http://www.runoob.com/docker/docker-run-command.html)

