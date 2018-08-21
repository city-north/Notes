[返回目录](/README.md)

# CentOS中安装Nginx

---

## 要求的安装环境

### gcc以及其他编译工具和库文件

使用命令：

```
yum -y install make zlib zlib-devel gcc-c++ libtool  openssl openssl-devel
```

* gcc

安装nginx需要先将官网下载的源码进行编译,编译依赖gcc环境。

* zlib

zlib库提供了许多种压缩和解压缩的方式，nginx使用zlib对http包的内容进行gzip，所以可以需要在linux上安装zlib库

* openssl

OpenSSL 是一个强大的安全套接字层密码库，囊括主要的密码算法、常用的密钥和证书封装管理功能及SSL协议，并提供丰富的应用程序供测试或其它目的使用。

nginx不仅支持http协议，还支持https（即在ssl协议上传输http），所以需要在linux安装openssl库。

### PCRE安装

PCRE\(Perl Compatible Regular Expressions\)是一个Perl库，包括 perl 兼容的正则表达式库。nginx的http模块使用pcre来解析正则表达式，所以需要在linux上安装pcre库。

```
[root@localhost ~]# cd /usr/local/src
[root@localhost src]# wget http://downloads.sourceforge.net/project/pcre/pcre/8.35/pcre-8.35.tar.gz
```

* 解压安装包：

```
[root@localhost src]# tar zxvf pcre-8.35.tar.gz
```

* 进入安装包目录：

```
[root@localhost src]# cd pcre-8.35
```

* 编译 安装

```
[root@localhost pcre-8.35]# ./configure
[root@localhost pcre-8.35]# make && make install
```

* 查看pcre版本

```
[root@localhost pcre-8.35]# pcre-config --version
```

## 安装Nginx

* 下载Nginx（或者将上传nginx安装包）

```
[root@localhost ~]# cd /usr/local/src
[root@localhost nginx-1.6.2]# wget http://nginx.org/download/nginx-1.6.2.tar.gz
```

* ## 解压安装包

```
[root@localhost nginx-1.6.2]# tar zxvf nginx-1.6.2.tar.gz
```

* 进入安装包目录

```
[root@localhost nginx-1.6.2]# cd nginx-1.6.2
```

* 编译，安装

配置相关参数可以使用./configure --help查询详细的参数，主要使用的参数如下：

```
./configure \
--prefix=设置快捷方式
--pid-path=/var/run/nginx/nginx.pid \
--lock-path=/var/lock/nginx.lock \
--error-log-path=/var/log/nginx/error.log \
--http-log-path=/var/log/nginx/access.log \
--with-http_gzip_static_module \
--http-client-body-temp-path=/var/temp/nginx/client \
--http-proxy-temp-path=/var/temp/nginx/proxy \
--http-fastcgi-temp-path=/var/temp/nginx/fastcgi \
--http-uwsgi-temp-path=/var/temp/nginx/uwsgi \
```

```
[root@localhost nginx-1.6.2]# ./configure --prefix=/usr/local/webserver/nginx --with-http_stub_status_module --with-http_ssl_module --with-pcre=/usr/local/src/pcre-8.35
```

```
[root@localhost nginx-1.6.2]# make
[root@localhost nginx-1.6.2]# make install
```

* 查看nginx版本

```
[root@localhost nginx-1.6.2]# /usr/local/webserver/nginx/sbin/nginx -v
```

## 配置Nginx

[nginx配置](https://gitee.com/Erichan/Nginx-Notes/blob/master/action/config.md)

## 命令

* 启动Nginx:

```
[root@localhost conf]# /usr/local/webserver/nginx/sbin/nginx
```

* 其他命令:

```
[root@localhost conf]# /usr/local/webserver/nginx/sbin/nginx -s reload            # 重新载入配置文件
[root@localhost conf]# /usr/local/webserver/nginx/sbin/nginx -s reopen            # 重启 Nginx
[root@localhost conf]# /usr/local/webserver/nginx/sbin/nginx -s stop              # 停止 Nginx
```

* 显示Nginx进程

```
ps aux|grep nginx
```



