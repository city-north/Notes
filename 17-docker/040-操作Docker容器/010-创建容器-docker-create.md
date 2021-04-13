# 010-创建容器-docker-create

[TOC]

从现在开始，忘掉“臃肿”的虚拟机吧。对容器进行操作就跟直接操作应用一样简单、快速。Docker容器实在太轻量级了，用户可以随时创建或删除容器。

## 1.新建容器

可以使用docker create命令新建一个容器，例如：

```
$ docker create -it ubuntu:latest
af8f4f922dafee22c8fe6cd2ae11d16e25087d61f1b1fa55b36e94db7ef45178
$ docker ps -a
CONTAINER ID IMAGE COMMAND CREATED STATUS PORTS NAMES
af8f4f922daf ubuntu:latest "/bin/bash" 17 seconds ago Created silly_euler
```

使用docker create命令新建的容器处于停止状态，可以使用docker start命令来启动它。

Create命令和后续的run命令支持的选项都十分复杂，主要包括如下几大类：

- 与容器运行模式相关
- 与容器和环境配置相关
- 与容器资源限制和安全保护相关

## 1.create命令与容器运行模式相关的选项

| 选项                                                 | 说明                                                         |
| ---------------------------------------------------- | ------------------------------------------------------------ |
| `-a` , `--attach=[]`                                 | 是否绑定到标准输入、输出和错误                               |
| `-d`, `--detach=true|false`                          | 是否在后台运行容器，默认为否                                 |
| `--detach-keys=""`                                   | 从attach模式退出的快捷键                                     |
| `--entrypoint=""`                                    | 镜像存在入口命令时，覆盖为新的命令                           |
| `--expose=[]`                                        | 指定容器会暴露出来的端口或端口范围                           |
| `--group-add=[]`                                     | 运行容器的用户组                                             |
| `-i`,`--interactive=true|false`                      | 保持标准输入打开，默认为false                                |
| `--ipc=""`                                           | 容器IPC命名空间，可以其他容器或主机                          |
| `--isolation=default`                                | 容器使用的隔离机制                                           |
| `--log-driver="json-file"`                           | 指定容器的日志驱动类型，可以为：json-file、syslog、journald、gelf、fluentd、awslogs、splunk、etwlogs、gcplogs、none |
| `--log-opt=[]`                                       | 传递给日志驱动的选项                                         |
| `--net="bridge"`                                     | 指定容器的网络模式，包括bridge、none、其他容器内网络、host的网络或某个现有网络等 |
| `--net-alias=[]`                                     | 容器在网络中的别名                                           |
| `-p`,`--publish-all=true|false`                      | 通过NAT机制将容器标记暴露的端口自动映射到本地主机的临时端口  |
| `-p`,`--publish=[]`                                  | 指定如何映射到本地主机端口，例如-p 11234-12234:1234-2234     |
| `--pid=host`                                         | 容器的PID命名空间                                            |
| `--userns=""`                                        | 启用userns-remap时配置用户命名空间的模式                     |
| `-uts=host`                                          | 容器的UTS命名空间                                            |
| `-restart="no"`                                      | 容器的重启策略，包括no、on-failure[: max-retry]、always、unless-stopped等 |
| `--rm=true|false`                                    | 容器退出后是否自动删除，不能跟-d同时使用                     |
| `-t`,`--tty=true|false`                              | 是否分配一个伪终端，默认为false                              |
| `--tmpfs=[]`                                         | 挂载临时文件系统到容器                                       |
| `-v|--volume[=[[HOST-DIR:]COMTAONER-DIR[:OPTIONS]]]` | 挂载主机上的文件卷到容器内                                   |
| `--volunme-driver=""`                                | 挂载文件卷的驱动类型                                         |
| `--volume-from=[]`                                   | 从其他容器挂载卷                                             |
| `-w`,`--workdir=""`                                  | 容器内的默认工作目录                                         |

## 2.create命令与容器环境和配置相关的选项

| 选项                          | 说明                                                         |
| ----------------------------- | ------------------------------------------------------------ |
| `--add-host=[]`               | 在容器内添加一个主机名到IP地址的映射关系（通过/etc/hosts文件） |
| `--device=[]`                 | 映射物理机上的设备到容器内                                   |
| `--dns-search=[]`             | DNS搜索域                                                    |
| `--dns-opt=[]`                | 自定义的DNS选项                                              |
| `--dns`                       | 自定义的DNS服务器                                            |
| `-e`、`--env=[]`              | 指定容器内的环境变量                                         |
| `--env-file=[]`               | 从文件中读取环境变量到容器内                                 |
| `-h`, `--hostname=""`         | 指定容器内主机名                                             |
| `--ip=""`                     | 指定容器内的主机IPv4地址                                     |
| `--ip6=""`                    | 指定容器内的主机IPv6地址                                     |
| `--link=[<name or id>:alias]` | 链接到其他容器                                               |
| `--link-local-ip=[]`          | 容器的本地链接地址列表                                       |
| `--mac-address=""`            | 指定容器的Mac地址                                            |
| `--name=""`                   | 指定容器的别名                                               |

## 3.create命令与容器资源限制和安全保护相关的选项

| 选项                                         | 说明                                                         |
| -------------------------------------------- | ------------------------------------------------------------ |
| `-blkio-weight=10~1000`                      | 容器读写块设备的I/O性能权重，默认为0                         |
| `--blkio-weight-device=[DEVICE_NAME:WEIGHT]` | 指定各个块设备I/O性能权重                                    |
| `--cpu-shares=0`                             | 允许容器使用CPU资源的相对权重，默认一个容器能用满一个核的CPU |
| `--cap-add=[]`                               | 增加容器的Linux指定安全能力                                  |
| `--cap-drop=[]`                              | 移除容器的Linux指定安全能力                                  |
| `--cgroup-parent=""`                         | 容器cgroups限制的创建路径                                    |
| `--cidfile=""`                               | 指定容器的进程ID号写到文件                                   |
| `--cpu-period=0`                             | 限制容器在CFS调度器下的CPU占用时间片                         |
| `--cpuset-cpus=""`                           | 限制容器能使用哪些CPU核心                                    |
| `--cpuset-mems=""`                           | NUMA架构下使用哪些核心的内存                                 |
| `--cpu-quota=0`                              | 限制容器在CFS调度器下的CPU配额                               |
| `--device-read-bps=[]`                       | 挂载设备的读吞率（以bps为单位）限制                          |
| `--device-write-bps=[]`                      | 挂载设备的写吞率（以bps为单位）限制                          |
| `--device-read-iops=[]`                      | 挂载设备的读速率（以每秒i/o次数为单位）限制                  |
| `--device-write-iops=[]`                     | 挂载设备的写速率（以每秒i/o次数为单位）限制                  |
| `--health-cmd=""`                            | 指定检查容器健康状态的命令                                   |
| `--health-interval=0s`                       | 执行健康检查的间隔时间，单位可以为ms、s、m或h                |
| `--health-retries=int`                       | 健康检查失败重试次数，超过则认为不健康                       |
| `--health-start-period=0s`                   | 容器启动后进行健康检查的等待时间，单位可以为ms、s、m或h      |
| `--health-timeout=0s`                        | 健康检查的执行超时，单位可以为ms、s、m或h                    |
| `--no-healthcheck=true|false`                | 是否禁用健康检查                                             |
| `--init`                                     | 在容器中执行一个init进程，来负责响应信号和处理僵尸状态子进程 |
| `--kernel-memory=""`                         | 限制容器使用内核的内存大小，单位可以是b、k、m或g             |
| `-m`, `--memory=""`                          | 限制容器内应用使用的内存，单位可以是b、k、m或g               |
| `--memory-reservation=""`                    | 当系统中内存过低时，容器会被强制限制内存到给定值，默认情况下等于内存限制值 |
| `--memory-swap="LIMIT"`                      | 限制容器使用内存和交换区的总大小                             |
| `--oom-score-adj=""`                         | 调整容器的内存耗尽参数                                       |
| `--pids-limit=""`                            | 限制容器的pid个数                                            |
| `--privileged=true|false`                    | 是否给容器高权限，这意味着容器内应用将不受权限的限制，一般不推荐 |
| `--read-only=true|false`                     | 是否让容器内的文件系统只读                                   |
| `--security-opt=[]`                          | 指定一些安全参数，包括权限、安全能力、apparmor等             |
| `--stop-signal=SIGTERM`                      | 指定停止容器的系统信号                                       |
| `--shm-size=""`                              | /dev/shm的大小                                               |
| `--sig-proxy=true|false`                     | 是否代理收到的信号给应用，默认为true，不能代理SIGCHLD、SIGSTOP和SIGKILL信号 |
| `--memory-swappiness="0~100"`                | 调整容器的内存交换区参数                                     |
| `-u`,`--user=""`                             | 指定在容器内执行命令的用户信息                               |
| `--userns=""`                                | 指定用户命名空间                                             |
| `--ulimit=[]`                                | 通过ulimit来限制最大文件数、最大进程数等                     |

其他选项还包括：

- `-l`, `--label=[]`：以键值对方式指定容器的标签信息；
- `--label-file=[]`：从文件中读取标签信息。