# 050-使用history命令查看镜像历史

[TOC]

既然镜像文件由多个层组成，那么怎么知道各个层的内容具体是什么呢？这时候可以使用history子命令，该命令将列出各层的创建信息。
例如，查看ubuntu：14.04镜像的创建过程，可以使用如下命令：

```
$ docker history ubuntu:14.04
IMAGE CREATED CREATED BY SIZE COMMENT
8f1bd21bd25c 2 weeks ago /bin/sh -c #(nop) CMD ["/bin/bash"] 0 B
<missing> 2 weeks ago /bin/sh -c sed -i 's/^#\s*\(deb.*universe\)$/ 1.895 kB
<missing> 2 weeks ago /bin/sh -c rm -rf /var/lib/apt/lists/* 0 B
<missing> 2 weeks ago /bin/sh -c set -xe && echo '#!/bin/sh' > /u 194.5 kB
<missing> 2 weeks ago /bin/sh -c #(nop) ADD file:aca501360d0937bc49 187.8 MB
```

注意过长的命令被自动截断了，可以使用前面提到的--no-trunc选项来输出完整命令。

