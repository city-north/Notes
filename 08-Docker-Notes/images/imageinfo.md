[返回目录](/README.md)

# 查看镜像信息

命令：

```
sudo docker images
```

例子：

```
>docker images
REPOSITORY                  TAG                 IMAGE ID            CREATED             SIZE
onlyoffice/documentserver   5.1.4.22            9a77d093202e        5 days ago          1.76GB
onlyoffice/documentserver   latest              9a77d093202e        5 days ago          1.76GB
docker4w/nsenter-dockerd    latest              cae870735e91        7 months ago        187kB
```

| 属性 | 含义 |
| :--- | :--- |
| REPOSITORY | 仓库名 |
| TAG | 标签 |
| IMAGE ID | 镜像的唯一ID |
| CREATED | 创建时间 |
| SIZE | 镜像大小 |

## 查看Docker 某项具体信息

指定镜像ID是，通常使用该ID的前若干个字符：

命令：

```
sudo docker inspect -f {{".Architecture"}} cae
```

例子：

```
>sudo docker inspect -f {{".Architecture"}} cae
amd64
```

## 查看Docker具体信息

命令：

```
sudo docker inspect [IMAGE ID]
```

例子：

```
>docker inspect 9a77d093202e
[
    {
        "Id": "sha256:9a77d093202efc544b8cdcddb3cf937bf0167132d6004042cec6ae7fe8b1cac6",
        "RepoTags": [
            "onlyoffice/documentserver:5.1.4.22",
            "onlyoffice/documentserver:latest"
        ],
        "RepoDigests": [
            "onlyoffice/documentserver@sha256:88881510e6b698d0b824d76d18da9ed186d59883c90c58c61ecc74bfb4592baa"
        ],
        "Parent": "",
        "Comment": "",
        "Created": "2018-05-22T13:35:04.313842177Z",
        "Container": "e84e8e24de7b6a205b0cefdc68d61d8a082957197da5b723e7372e5fb5149e61",
        "ContainerConfig": {
            "Hostname": "e84e8e24de7b",
            "Domainname": "",
            "User": "",
            "AttachStdin": false,
            "AttachStdout": false,
            "AttachStderr": false,
            "ExposedPorts": {
                "443/tcp": {},
                "80/tcp": {}
            },
            "Tty": false,
            "OpenStdin": false,
            "StdinOnce": false,
            "Env": [
                "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin",
                "LANG=en_US.UTF-8",
                "LANGUAGE=en_US:en",
                "LC_ALL=en_US.UTF-8",
                "DEBIAN_FRONTEND=noninteractive"
            ],
            "Cmd": [
                "/bin/sh",
                "-c",
                "#(nop) ",
                "CMD [\"/bin/sh\" \"-c\" \"bash -C '/app/onlyoffice/run-document-server.sh';'bash'\"]"
            ],
            "ArgsEscaped": true,
            "Image": "sha256:2be699369dc1cfe0bef06a0b0eb590fc6f04a2e38bd73bbba841c1ecb1305430",
            "Volumes": {
                "/etc/onlyoffice": {},
                "/usr/share/fonts/truetype/custom": {},
                "/var/lib/onlyoffice": {},
                "/var/lib/postgresql": {},
                "/var/log/onlyoffice": {},
                "/var/www/onlyoffice/Data": {}
            },
            "WorkingDir": "",
            "Entrypoint": null,
            "OnBuild": null,
            "Labels": {
                "maintainer": "Ascensio System SIA <support@onlyoffice.com>"
            }
        },
        "DockerVersion": "18.03.0-ce",
        "Author": "",
        "Config": {
            "Hostname": "",
            "Domainname": "",
            "User": "",
            "AttachStdin": false,
            "AttachStdout": false,
            "AttachStderr": false,
            "ExposedPorts": {
                "443/tcp": {},
                "80/tcp": {}
            },
            "Tty": false,
            "OpenStdin": false,
            "StdinOnce": false,
            "Env": [
                "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin",
                "LANG=en_US.UTF-8",
                "LANGUAGE=en_US:en",
                "LC_ALL=en_US.UTF-8",
                "DEBIAN_FRONTEND=noninteractive"
            ],
            "Cmd": [
                "/bin/sh",
                "-c",
                "bash -C '/app/onlyoffice/run-document-server.sh';'bash'"
            ],
            "ArgsEscaped": true,
            "Image": "sha256:2be699369dc1cfe0bef06a0b0eb590fc6f04a2e38bd73bbba841c1ecb1305430",
            "Volumes": {
                "/etc/onlyoffice": {},
                "/usr/share/fonts/truetype/custom": {},
                "/var/lib/onlyoffice": {},
                "/var/lib/postgresql": {},
                "/var/log/onlyoffice": {},
                "/var/www/onlyoffice/Data": {}
            },
            "WorkingDir": "",
            "Entrypoint": null,
            "OnBuild": null,
            "Labels": {
                "maintainer": "Ascensio System SIA <support@onlyoffice.com>"
            }
        },
        "Architecture": "amd64",
        "Os": "linux",
        "Size": 1756736182,
        "VirtualSize": 1756736182,
        "GraphDriver": {
            "Data": {
                "LowerDir": "/var/lib/docker/overlay2/a989287d327597427f89557b11ccf7286e5216c1ff8b6db7a83b6e72877643d8/diff:/var/lib/docker/overlay2/995eb86332b4ab195b48249d6fe7a6fbb809427477a62c304e54ae03dc8e6c83/diff:/var/lib/docker/overlay2/c91112ed0ceac7c15dbce28b6b027a1c79ed720d5afba9ea278a9a4f2dd4e7ee/diff:/var/lib/docker/overlay2/4c6bfbe374351543f7bb11ec5f0fdd3497a97d3b48df1bda17fe67b3fc5d0199/diff:/var/lib/docker/overlay2/c0c6b212d8b6d5ecaff40bb84c50eb5cb82d697a7a5be07fa538cbda3b4ab99e/diff:/var/lib/docker/overlay2/0162d34c276dab0d16e23cbd88f46d7918e30b50a295075022c9448cc4417332/diff:/var/lib/docker/overlay2/c4f2c8669962025dc1c2ec10cd1e574a7c00c0e7e7bd6367fddc3753778186e7/diff:/var/lib/docker/overlay2/29f946c5c7ce5c7ab480f8af0ee09de167ec417ea66e4b8cfc8612a631849e98/diff",
                "MergedDir": "/var/lib/docker/overlay2/b9c2135d21fff8a618ae923407ea3fcf2ee23572d65cf31d102206050bef2c2d/merged",
                "UpperDir": "/var/lib/docker/overlay2/b9c2135d21fff8a618ae923407ea3fcf2ee23572d65cf31d102206050bef2c2d/diff",
                "WorkDir": "/var/lib/docker/overlay2/b9c2135d21fff8a618ae923407ea3fcf2ee23572d65cf31d102206050bef2c2d/work"
            },
            "Name": "overlay2"
        },
        "RootFS": {
            "Type": "layers",
            "Layers": [
                "sha256:c8aa3ff3c3d351787cc5f84d960870fad16c9615aab7aa47ab343906fc8cfc24",
                "sha256:82718dbf791d95622b5edeedbfbc6d0f01d406d4035a30a416d51d2c7e193486",
                "sha256:3a0404adc8bdbcf7cfc28ca3a3e703d6db86f4e11853664085f00add69ccb3d7",
                "sha256:cd7b4cc1c2dd51a75939212a7b5c278eb899ac4330b9482038f72f7e6feba975",
                "sha256:bf3d982208f5bad81239ff690e81ad90e28c3cd81634b079cb938757d2435902",
                "sha256:078bdde3df68d8032f78b684a36b07db178513b5790b44ce927ac3f500850ddb",
                "sha256:d2de856c0e0bd529315ba5c1d669c3e0e44a43133739da3dabe3b68840987546",
                "sha256:71c17eafa76bcce2cd5ae5f99d58e2be71415b4bb84e5e95c7302b6ee874a74e",
                "sha256:26ba63daee444199cb104ef4d02c4360cd30fe4ab77f8ef18f5b716c5628873a"
            ]
        },
        "Metadata": {
            "LastTagTime": "0001-01-01T00:00:00Z"
        }
    }
]
```



