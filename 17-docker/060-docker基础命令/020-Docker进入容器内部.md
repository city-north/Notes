# Docker进入容器内部

```
docker exec -it 容器 ID|容器名称 /bin/bash
```
参数
- `-d` , `--detach` :在容器中后台执行名
- `--detach-keys=""`:指定将容器切回后台的按键
- `-e`,`--env=[]`:指定环境变量列表
- `-i`,`--interactive=true|false`:打开标准输入接受用户名命令,默认为 false
- `-t`,`--tty=true|false`:分配伪终端,默认为 false
- `-u`,`--user==""` :执行命令的用户名或者 ID


例如:
```
docker exec -it mycentos2 /bin/bash
```
## 宿主与容器互相拷贝文件

```
docker cp 要拷贝的宿主主机文件或者目录 容器名称:容器文件或者目录
```

例如:
拷贝`/usr/share/fonts` 文件夹到`/tmp/onlyoffice` 文件夹
```
docker cp  8cd25f0f9a3f:/usr/share/fonts /tmp/onlyoffice

```

## 查看容器内部细节
```
[root@localhost ~]# docker inspect mycentos1
[
    {
        "Id": "cef11d30463dc06a3573aa62d4b3d766cbe1d31558e3dc1a1e8b5bae921f6ffa",
        "Created": "2019-05-23T06:06:41.706731183Z",
        "Path": "/bin/bash",
        "Args": [],
        "State": {
            "Status": "exited",
            "Running": false,
            "Paused": false,
            "Restarting": false,
            "OOMKilled": false,
            "Dead": false,
            "Pid": 0,
            "ExitCode": 127,
            "Error": "",
            "StartedAt": "2019-05-23T06:06:43.855416737Z",
            "FinishedAt": "2019-05-23T06:09:25.246562866Z"
        },
        "Image": "sha256:9f38484d220fa527b1fb19747638497179500a1bed8bf0498eb788229229e6e1",
        "ResolvConfPath": "/var/lib/docker/containers/cef11d30463dc06a3573aa62d4b3d766cbe1d31558e3dc1a1e8b5bae921f6ffa/resolv.conf",
        "HostnamePath": "/var/lib/docker/containers/cef11d30463dc06a3573aa62d4b3d766cbe1d31558e3dc1a1e8b5bae921f6ffa/hostname",
        "HostsPath": "/var/lib/docker/containers/cef11d30463dc06a3573aa62d4b3d766cbe1d31558e3dc1a1e8b5bae921f6ffa/hosts",
        "LogPath": "/var/lib/docker/containers/cef11d30463dc06a3573aa62d4b3d766cbe1d31558e3dc1a1e8b5bae921f6ffa/cef11d30463dc06a3573aa62d4b3d766cbe1d31558e3dc1a1e8b5bae921f6ffa-json.log",
        "Name": "/mycentos1",
        "RestartCount": 0,
        "Driver": "overlay2",
        "Platform": "linux",
        "MountLabel": "",
        "ProcessLabel": "",
        "AppArmorProfile": "",
        "ExecIDs": null,
        "HostConfig": {
            "Binds": null,
            "ContainerIDFile": "",
            "LogConfig": {
                "Type": "json-file",
                "Config": {}
            },
            "NetworkMode": "default",
            "PortBindings": {},
            "RestartPolicy": {
                "Name": "no",
                "MaximumRetryCount": 0
            },
            "AutoRemove": false,
            "VolumeDriver": "",
            "VolumesFrom": null,
            "CapAdd": null,
            "CapDrop": null,
            "Dns": [],
            "DnsOptions": [],
            "DnsSearch": [],
            "ExtraHosts": null,
            "GroupAdd": null,
            "IpcMode": "shareable",
            "Cgroup": "",
            "Links": null,
            "OomScoreAdj": 0,
            "PidMode": "",
            "Privileged": false,
            "PublishAllPorts": false,
            "ReadonlyRootfs": false,
            "SecurityOpt": null,
            "UTSMode": "",
            "UsernsMode": "",
            "ShmSize": 67108864,
            "Runtime": "runc",
            "ConsoleSize": [
                0,
                0
            ],
            "Isolation": "",
            "CpuShares": 0,
            "Memory": 0,
            "NanoCpus": 0,
            "CgroupParent": "",
            "BlkioWeight": 0,
            "BlkioWeightDevice": [],
            "BlkioDeviceReadBps": null,
            "BlkioDeviceWriteBps": null,
            "BlkioDeviceReadIOps": null,
            "BlkioDeviceWriteIOps": null,
            "CpuPeriod": 0,
            "CpuQuota": 0,
            "CpuRealtimePeriod": 0,
            "CpuRealtimeRuntime": 0,
            "CpusetCpus": "",
            "CpusetMems": "",
            "Devices": [],
            "DeviceCgroupRules": null,
            "DiskQuota": 0,
            "KernelMemory": 0,
            "MemoryReservation": 0,
            "MemorySwap": 0,
            "MemorySwappiness": null,
            "OomKillDisable": false,
            "PidsLimit": 0,
            "Ulimits": null,
            "CpuCount": 0,
            "CpuPercent": 0,
            "IOMaximumIOps": 0,
            "IOMaximumBandwidth": 0,
            "MaskedPaths": [
                "/proc/asound",
                "/proc/acpi",
                "/proc/kcore",
                "/proc/keys",
                "/proc/latency_stats",
                "/proc/timer_list",
                "/proc/timer_stats",
                "/proc/sched_debug",
                "/proc/scsi",
                "/sys/firmware"
            ],
            "ReadonlyPaths": [
                "/proc/bus",
                "/proc/fs",
                "/proc/irq",
                "/proc/sys",
                "/proc/sysrq-trigger"
            ]
        },
        "GraphDriver": {
            "Data": {
                "LowerDir": "/var/lib/docker/overlay2/b8f43b338c171eae572b93b253b1912299cd74721799baefca8a41868387e5f0-init/diff:/var/lib/docker/overlay2/3467891851d46ca4ae4d8290694898dbafdcb0aa1ea7d8756050ac8c7dde61d3/diff",
                "MergedDir": "/var/lib/docker/overlay2/b8f43b338c171eae572b93b253b1912299cd74721799baefca8a41868387e5f0/merged",
                "UpperDir": "/var/lib/docker/overlay2/b8f43b338c171eae572b93b253b1912299cd74721799baefca8a41868387e5f0/diff",
                "WorkDir": "/var/lib/docker/overlay2/b8f43b338c171eae572b93b253b1912299cd74721799baefca8a41868387e5f0/work"
            },
            "Name": "overlay2"
        },
        "Mounts": [],
        "Config": {
            "Hostname": "cef11d30463d",
            "Domainname": "",
            "User": "",
            "AttachStdin": true,
            "AttachStdout": true,
            "AttachStderr": true,
            "Tty": true,
            "OpenStdin": true,
            "StdinOnce": true,
            "Env": [
                "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"
            ],
            "Cmd": [
                "/bin/bash"
            ],
            "Image": "centos:7",
            "Volumes": null,
            "WorkingDir": "",
            "Entrypoint": null,
            "OnBuild": null,
            "Labels": {
                "org.label-schema.build-date": "20190305",
                "org.label-schema.license": "GPLv2",
                "org.label-schema.name": "CentOS Base Image",
                "org.label-schema.schema-version": "1.0",
                "org.label-schema.vendor": "CentOS"
            }
        },
        "NetworkSettings": {
            "Bridge": "",
            "SandboxID": "a0e4b8aa6601e1a910ede54aa21b951d0a66f5527a6b34ddb5018bc05bcf75e7",
            "HairpinMode": false,
            "LinkLocalIPv6Address": "",
            "LinkLocalIPv6PrefixLen": 0,
            "Ports": {},
            "SandboxKey": "/var/run/docker/netns/a0e4b8aa6601",
            "SecondaryIPAddresses": null,
            "SecondaryIPv6Addresses": null,
            "EndpointID": "",
            "Gateway": "",
            "GlobalIPv6Address": "",
            "GlobalIPv6PrefixLen": 0,
            "IPAddress": "",
            "IPPrefixLen": 0,
            "IPv6Gateway": "",
            "MacAddress": "",
            "Networks": {
                "bridge": {
                    "IPAMConfig": null,
                    "Links": null,
                    "Aliases": null,
                    "NetworkID": "9adead8696e37df9091d86896f91d784c1b672722398372b66842cbe6361a642",
                    "EndpointID": "",
                    "Gateway": "",
                    "IPAddress": "",
                    "IPPrefixLen": 0,
                    "IPv6Gateway": "",
                    "GlobalIPv6Address": "",
                    "GlobalIPv6PrefixLen": 0,
                    "MacAddress": "",
                    "DriverOpts": null
                }
            }
        }
    }
]

```