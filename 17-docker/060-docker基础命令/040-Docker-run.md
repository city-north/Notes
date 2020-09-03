# Run命令

```
docker run [OPTIONS] 镜像名:标签名

```
#### 创建容器 [OPTIONS] 常用的参数说明:

- `-i `表示交互式运行容器(就是创建容器后，马上会启动容器，并进入容器 )，通常与 -t
同时使用 。

- `-t` 启动后会进入其容器命令行, 通常与 -i 同时使用; 加入 -it 两个参数后，容器创建就能登录进
     去。即分配一个伪终端。
     
- `--name` 为创建的容器指定一个名称 。

- `-d` 创建一个守护式容器在后台运行，并返回容器ID;这样创建容器后不会自动登录容器，如果加 -i 参数，创建后就会运行容器。

- `-v` 表示目录映射, 格式为: -p 宿主机目录:容器目录

注意:最好做目录映射，在宿主机上做修改，然后共享到容器上。

- `-p` 表示端口映射，格式为: -p 宿主机端口:容器端口


## 例子
#### 一、创建一个容器并进入容器内部

```
docker run -it --name mycentos centos:7 /bin/bash
```

- 创建一个交互式容器并取名字为`mycentos`
- `/bin/bash`是 Linux 命令解析器,进入容器内部
- `exit` 退出 退出容器内的命令行,关闭容器
- `Ctrl + p + q ` 退出容器内的命令行,但不关闭容器

#### 二、创建一个后台运行的容器,挂在目录
语法:
```
docker run -id -v 宿主机目录:容器目录
```

```
docker run -id -v /opt:/opt --name mycentos3 centos:7

```
创建容器并挂在映射目录

```
sudo docker run -i -t -d -p 8765:80 --name ds --restart=always \
  -v /app/onlyoffice/DocumentServer/logs:/var/log/onlyoffice  \
  -v /app/onlyoffice/DocumentServer/data:/var/www/onlyoffice/Data  \
  -v /app/onlyoffice/DocumentServer/lib:/var/lib/onlyoffice \
  -v /app/onlyoffice/DocumentServer/db:/var/lib/postgresql  hand/documentserver:1.8
  
```
- `-v`挂在宿主的目录到容器的内部

#### 三 挂在宿主目录时使用只读模式
```
docker run -id -v /宿主机绝对路径目录:/容器内目录:ro --name 容器名 镜像名
```
例如
```
docker run -id -v /dataHost:/dataContainer:ro --name mycentos4 centos:7
```