# 010-DockerFile-指令说明

[TOC]

## 命令列表

| 序号 | 分类     | 指令        | 说明                               |
| ---- | -------- | ----------- | ---------------------------------- |
| 1    | 配置指令 | ARG         | 定义创建镜像过程中的变量           |
| 2    | 配置指令 | FROM        | 指定所创建镜像的基础镜像           |
| 3    | 配置指令 | LABEL       | 为生成的镜像添加元数据标签信息     |
| 4    | 配置指令 | EXPOSE      | 声明镜像内服务监听的端口           |
| 5    | 配置指令 | ENV         | 指定环境变量                       |
| 6    | 配置指令 | ENTRYPOINT  | 指定镜像的默认入口命令             |
| 7    | 配置指令 | VOLUME      | 创建一个数据卷挂载点               |
| 8    | 配置指令 | USER        | 指定运行容器时的用户名或者 UID     |
| 9    | 配置指令 | WORKDIR     | 配置工作目录                       |
| 10   | 配置指令 | ONBUILD     | 创建自镜像时指定自动执行的操作执行 |
| 11   | 配置指令 | STOPSIGNAL  | 指定退出的信号值                   |
| 12   | 配置指令 | HEALTHCHECK | 配置所启动容器如何进行健康检查     |
| 13   | 配置指令 | SHELL       | 指定默认 shell 类型                |
| 14   | 操作指令 | RUN         | 运行指定命令                       |
| 15   | 操作指令 | CMD         | 启动容器时指定默认执行的命令       |
| 16   | 操作指令 | ADD         | 添加内容到镜像                     |
| 17   | 操作指令 | COPY        | 复制内容到镜像                     |



## 1 ARG

定义创建镜像过程中使用的变量

语法:
```
ARG <name> [=<default value>]
```

在执行`docker build`的时候,可以通过`-build-arg[=]`来为变量赋值.

Docker 内置的创建变量

- `HTTP_PROXY`
- `HTTPS_PROXY`
- `FTP_PROXY`
- `NO_PROXY`

## 2 FROM
指定所创建镜像的基础镜像

例如:
```
ARG VERSION=9.3
FROM debian:${VERSION}

```

## 3 LABEL
为生成的镜像添加元数据标签信息
例如:

```
LABEL name="CentOS Base Image" \
    vendor="CentOS" \
    license="GPLv2" \
    build-date="2016-06-02"

```

## 4 EXPOSE
声明镜像内服务监听的端口

例如
```
EXPOSE 22 80 8843
```
注意该指令只是起到声明作用,并不会自动完成端口映射
如果要映射端口,应该在启动容器的时候使用`-p HOST_PORT:CONTAINER_PORT`的方式来指定

## 5 ENV
指定环境变量,在镜像生成过程中会被后续 RUN 指令使用,在镜像启动的容器中也会存在
例如:
```
ENV LANG=en_US.UTF-8 LANGUAGE=en_US:en LC_ALL=en_US.UTF-8 DEBIAN_FRONTEND=noninteractive

```


## 6 ENTRYPOINT
指定镜像的默认入口命令,该入口命令会在启动容器的时候作为根命令执行

支持两种格式:

```
ENTRYPOINT["executable","param1","param2"]  #exec 调用执行
ENTRYPOINT command param1 param2            #shell 中执行
```

每个 DcokerFile 中只能有一个`ENTRYPOINT`,当指定多个时,只有最后一个起效.
在运行时,可以被`--entrypoint`参数覆盖掉
例如

`docker run --entrypoint`

例如`OnlyOffice`中的最后一句
`ENTRYPOINT /app/onlyoffice/run-document-server.sh`

`ENTRYPOINT` 和 `CMD`组合使用

```
ENTRYPOINT ["/usr/sbin/nginx"]
CMD ["-g"]
```

## 7 VALUME
创建一个数据卷挂载点
格式为 `VOLUME["/data"]`
运行容器的时候可以从本地主机或者其他容器挂载数据卷,一般来说用来存放数据库和需要保持的数据等.

例如 `ONLYOFFICE`中的
`VOLUME /var/log/onlyoffice /var/lib/onlyoffice /var/www/onlyoffice/Data /var/lib/postgresql /usr/share/fonts/truetype/custom`

## 8 USER
指定运行容器时的用户名或者 UID

当服务不需要管理员权限时,可以通过该命令指定运行用户,并且可以在 

DockerFile 中创建所需要的用户,例如:
```
RUN groupadd -r postgres && useradd  --no-log-init -r -g postgres postgres

```

## 9 WORKDIR
为后续的`RUN`/`CMD`/`ENTRYPOINT`指令配置工作目录

格式为 `WORKDIR /path/to/workdir` 

## 10 ONBUILD
指定当基于所生成镜像创建子镜像时,会自动执行的操作指令

格式为`ONBUILD [INSTRUCTION]`

例如,使用如下 Dockerfile 创建父镜像 ParentImage,指定 `ONBUILD`指令:

```
#Dockerfile for ParentImage
[...]
ONBUILD ADD . /app/src
ONBUILD RUN /usr/local/bin/python-build --dir /app/src
[...]
```

那么,我们使用`docker build ` ParentImage 创建一个 ChildImage 的时候这样引用:
```
FORM ParentImage
```

那么会
```
# 在 构建 ChildImage 的时候自动的运行下面的语句
ADD . /app/src
RUN /usr/local/bin/python-build --dir /app/src

```
典型应用,自动编译/检查等操作的基础镜像时会十分有用

## 11 STOPSIGNAL
指定所创建镜像启动的容器接收到退出的信号值
`STOPSIGNAL signal`

## 12 HEALTHCHECK
配置所启动容器如何进行健康检查

语法:
```
#根据所执行命令返回值是否为 0 来判断

HEALTHCHECK [OPTIONS] CMD command
-interval=DURATION 过多久检查一次,默认是 30s
-timeout=DURATION 每次检查等待结果的超时 默认是 30s
-retries=N 如果失败了,重试几次才能最终确定失败 默认是 3

# 禁止基础进项中的健康检查

HEALTHCHECK NONE
```

## 13 SHELL
指定其他命令使用 shell 的时候的默认 shell 类型

```
SHELL ["executable",parameters]

默认为:["/bin/sh","-c"]
```

值得注意的是

对于 Windows 系统,Shell 路径中的使用了`\`作为分隔符,建议在 Dockerfile 开头添加`#escape ='`来执行转移符

## 14 RUN
运行指定命令
```
RUN<commond>
默认在 shell 终端中运行命令, 即/bin/sh -c 
或者
RUN ["executable","param1","param2"]
后者会被解析成 JSON 数组,因此要双引号,使用 exec 执行,不会启动 shell 环境
```
例如 onlyoffice中的
```
RUN echo "#!/bin/sh\nexit 0" > /usr/sbin/policy-rc.d && \
    apt-get -y update && \
    apt-get -yq install wget apt-transport-https curl locales && \
    apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 0x8320ca65cb2de8e5 && \
    locale-gen en_US.UTF-8 && \
    curl -sL https://deb.nodesource.com/setup_8.x | bash - && \
    apt-get -y update && \
    apt-get -yq install \
    service supervisor stop && \
    service nginx stop && \
    rm -rf /var/lib/apt/lists/*

```

## 15 CMD
指定启动容器时默认执行的命令
三种格式

```
CMD ["executable","param1","param2"]
#相当于执行 executable param1 param2
```
```
CMD command param1 param2
# 在默认的 Shell 中执行,提供给需要交互的应用

```
```
CMD ["param1","param2"]
#提供给 ENTRYPOINT 的默认参数
```
值得注意的是:
- 每个 Dockerfile 只能有一条 CMD命令,如果指定多条只会执行最后一套
- 如果用户启动容器的时候手动指定了运行的命令(作为 run 的参数),会覆盖掉 CMD 指定的命令

## 16 ADD
添加内容到镜像
复制指定的`<src>` 路径下的内容到容器中的`<dest>` 路径下
```
ADD <src> <dest>
```

- `<src>` 可以是 Dockerfile 所在目录的一个相对路径,也可以是 URL或者 tar 文件(自动解压为目录)
-` <desy>`可以是镜像内绝对路径,或者相对工作目录(WORKDIR 的相对路径)
- 支持正则:`ADD *.c /code/`


## 17 COPY
复制内容到镜像
复制制指定的`<src>` 路径下的内容到容器中的`<dest>` 路径下
```
ADD <src> <dest>
```
- `COPY` 和 `ADD `类似,当使用本地目录作为源目录的时候,推荐使用 `COPY`
- 支持正则:`ADD *.c /code/`