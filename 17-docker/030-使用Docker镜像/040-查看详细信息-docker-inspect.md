# 040-使用inspect命令查看详细信息

[TOC]

```java
$ docker inspect mysql:5.7.17
[
    {
        "Id": "sha256:9546ca122d3ac7a3853c0a0cca4074a65c418239bc1bd97009e1b26f5f1552c9",
        "RepoTags": [
            "mysql:5.7.17"
        ],
        "RepoDigests": [
            "mysql@sha256:49b7d6d8d45f8c3300cba056e8cdf36c714d99e0b40f7005b9e6e75e64ecdf7c"
        ],
        "Parent": "",
        "Comment": "",
        "Created": "2017-03-30T21:44:22.115799583Z",
        "Container": "3027895a032a030c31c5553f4bd57b146dfdb4d94366b3135df9d6918db7742f",
        "ContainerConfig": {
            "Hostname": "7e9ec6cde4d1",
            "Domainname": "",
            "User": "",
            "AttachStdin": false,
            "AttachStdout": false,
            "AttachStderr": false,
            "ExposedPorts": {
                "3306/tcp": {}
            },
            "Tty": false,
            "OpenStdin": false,
            "StdinOnce": false,
            "Env": [
                "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin",
                "GOSU_VERSION=1.7",
                "MYSQL_MAJOR=5.7",
                "MYSQL_VERSION=5.7.17-1debian8"
            ],
            "Cmd": [
                "/bin/sh",
                "-c",
                "#(nop) ",
                "CMD [\"mysqld\"]"
            ],
            "ArgsEscaped": true,
            "Image": "sha256:6dcf08ca93fa64390be16f1f314f2f790f6144dac6d3fb647b3391066730784a",
            "Volumes": {
                "/var/lib/mysql": {}
            },
            "WorkingDir": "",
            "Entrypoint": [
                "docker-entrypoint.sh"
            ],
            "OnBuild": [],
            "Labels": {}
        },
        "DockerVersion": "1.12.6",
        "Author": "",
        "Config": {
            "Hostname": "7e9ec6cde4d1",
            "Domainname": "",
            "User": "",
            "AttachStdin": false,
            "AttachStdout": false,
            "AttachStderr": false,
            "ExposedPorts": {
                "3306/tcp": {}
            },
            "Tty": false,
            "OpenStdin": false,
            "StdinOnce": false,
            "Env": [
                "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin",
                "GOSU_VERSION=1.7",
                "MYSQL_MAJOR=5.7",
                "MYSQL_VERSION=5.7.17-1debian8"
            ],
            "Cmd": [
                "mysqld"
            ],
            "ArgsEscaped": true,
            "Image": "sha256:6dcf08ca93fa64390be16f1f314f2f790f6144dac6d3fb647b3391066730784a",
            "Volumes": {
                "/var/lib/mysql": {}
            },
            "WorkingDir": "",
            "Entrypoint": [
                "docker-entrypoint.sh"
            ],
            "OnBuild": [],
            "Labels": {}
        },
        "Architecture": "amd64",
        "Os": "linux",
        "Size": 406971761,
        "VirtualSize": 406971761,
        "GraphDriver": {
            "Data": {
                "LowerDir": "/var/lib/docker/overlay2/e4d69884103d355852bf1453e45d7f524384e2cab104d27c3169438cab11cd20/diff:/var/lib/docker/overlay2/fc76a235c3e2b93c7cc3e91d5a695eab115c4c28d95d604cb059896b981a603b/diff:/var/lib/docker/overlay2/582475cf7b171505a560997ad4b54364295d7f160e8f8373482b15ad7ed5630a/diff:/var/lib/docker/overlay2/282ea0cbba5391fd2248c20e1996cc2e2555ecefbd5113f53b542d5c040ca470/diff:/var/lib/docker/overlay2/80763cb3820ca70c32a13a52ab86ae6b731147e71614e0002ec61877fbbb7335/diff:/var/lib/docker/overlay2/17463cf2380c99761ab1b017477da2bff91769e22c23cdf322945862e8a6b096/diff:/var/lib/docker/overlay2/daa71154b5364b926d4a52a369bc2cf140857810c1147f74fa32e00d3c008206/diff:/var/lib/docker/overlay2/14b243f1e757414743c6d06b977d548609854cbe77d6e7e1b0fa8fae754abc8f/diff:/var/lib/docker/overlay2/95bdb5c38b70b7492fe887cef08de906a1ac0b2acae76648fb3001443ec170c0/diff:/var/lib/docker/overlay2/0e8c29dd089b1e8a12f13313351f9849435412539a68b6ac87d6a9455b3926fe/diff",
                "MergedDir": "/var/lib/docker/overlay2/b896ab113291dce8a1270977dd2cedac07990470cb9def7c5585eac4d8f76437/merged",
                "UpperDir": "/var/lib/docker/overlay2/b896ab113291dce8a1270977dd2cedac07990470cb9def7c5585eac4d8f76437/diff",
                "WorkDir": "/var/lib/docker/overlay2/b896ab113291dce8a1270977dd2cedac07990470cb9def7c5585eac4d8f76437/work"
            },
            "Name": "overlay2"
        },
        "RootFS": {
            "Type": "layers",
            "Layers": [
                "sha256:5d6cbe0dbcf9a675e86aa0fbedf7ed8756d557c7468d6a7c64bde7fa9e029636",
                "sha256:435f2dfbd8847eb35f9fceabb191751c1380289c34c0e8a10e491c755d9d777b",
                "sha256:814d7b59f0cce1f89c83412a5c9ee289222eb347471a58d1fda0cff6b570b9e4",
                "sha256:aae399245bd06cd2507076b9b92c21c1312711082b0a2a968aeb27bffb0159df",
                "sha256:4b1c12540dec607a51016ea5d64616b2f51b45a4de77c95937d5169457d4012c",
                "sha256:4a89782c43f45fc01a03b7e88dae4b4e54b1deef0f474c2d7b697e0a18c5aa6e",
                "sha256:caddf82c1ece4699d215a06d70d2cb8f6d64866297c8770881e09d2fec032049",
                "sha256:ae3f87f5093bfbd798f5bfe2c69969d916d5db480ca2ca33683b895a04f3e40a",
                "sha256:58ba42f8a5fe20827ad111997430df5630e8fa5949758ff82148b1ff85daff90",
                "sha256:6a26b64d0b631619bba10bd0285239c08f6d43d49f85d13cc852c59b26ce4ee9",
                "sha256:6cb029cb3ff3a958eeefb362c89dd3ea1da5e2688ef242d2daf83490378ff5d9"
            ]
        },
        "Metadata": {
            "LastTagTime": "0001-01-01T00:00:00Z"
        }
    }
]
```

## 