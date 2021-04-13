# 020-查看镜像信息-docker-images

[TOC]

## 1.使用images命令列出镜像

使用docker images命令可以列出本地主机上已有镜像的基本信息。

```
$ docker images
REPOSITORY TAG IMAGE ID CREATED SIZE 
ubuntu 16.04 2fa927b5cdd3 2 weeks ago 
122 MBubuntu latest 
2fa927b5cdd3 2 weeks ago 
122 MBubuntu 14.04 
8f1bd21bd25c 2 weeks ago 188 MB
```

在列出的信息中，可以看到以下几个字段信息。

```
-来自于哪个仓库，比如ubuntu仓库用来保存ubuntu系列的基础镜像；
-镜像的标签信息，比如14.04、latest用来标注不同的版本信息。标签只是标记，并不能标识镜像内容；
-镜像的ID（唯一标识镜像），如ubuntu：latest和ubuntu：16.04镜像的ID都是2fa927b5cdd3，说明它们目前实际上指向同一个镜像；
-创建时间，说明镜像最后的更新时间；
-镜像大小，优秀的镜像往往体积都较小。
```

其中镜像的ID信息十分重要，它唯一标识了镜像。

在使用镜像ID的时候，一般可以使用该ID的前若干个字符组成的可区分串来替代完整的ID。

TAG信息用来标记来自同一个仓库的不同镜像。例如ubuntu仓库中有多个镜像，通过TAG信息来区分发行版本，包括10.04、12.04、12.10、13.04、14.04、16.04等标签。

镜像大小信息只是表示该镜像的逻辑体积大小，实际上由于相同的镜像层本地只会存储一份，物理上占用的存储空间会小于各镜像的逻辑体积之和。

- images子命令主要支持如下选项，用户可以自行进行尝试。
- -a，--all=true|false：列出所有的镜像文件（包括临时文件），默认为否；
- --digests=true|false：列出镜像的数字摘要值，默认为否；
- -f，--filter=[]：过滤列出的镜像，如dangling=true只显示没有被使用的镜像；也可指定带有特定标注的镜像等；
- --format="TEMPLATE"：控制输出格式，如.ID代表ID信息，.Repository代表仓库信息等；
- --no-trunc=true|false：对输出结果中太长的部分是否进行截断，如镜像的ID信息，默认为是；
- -q，--quiet=true|false：仅输出ID信息，默认为否。

其中，对输出结果进行控制的选项如-f，--filter=[]、--no-trunc=true|false、-q，--quiet=true|false等，大部分子命令都支持。

