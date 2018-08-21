[返回目录](/README.md)

# Nginx配置文件详解

[官方文档](http://nginx.org/en/docs/ngx_core_module.html?&_ga=1.10035445.1509956953.1490042234#events)

文件名称：nginx.conf

位置：/usr/local/nginx/conf/etc/nginx 或者/usr/local/etc/nginx

完整文件如下：

```
#user  nobody;          #运行nginx的所属组和所有者
worker_processes  1;    #开启一个nginx工作进程，一般几个CP核心就写几

#error_log  logs/error.log;  指定nginx错误日志路径
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;  #pid路径


events {
    worker_connections  1024; # 一个进程能同时处理 1024 个请求
}


http {
    include       mime.types;
    default_type  application/octet-stream;

    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main; # 默认访问日志路径

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;  # keepalive 超时时间

    #gzip  on;

# 开始配置一个域名,一个 server 配置段一般对应一个域名
    server {
        listen       80; # 在本机所有 ip 上监听 80,也可以写为 192.168.1.202:80,这样的话,就只监听 192.168.1.202 上的 80 口
        server_name  localhost;  # 域名

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            root   html; # 站点根目录（程序目录）
            index  index.html index.htm; # 索引文件
        }

        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;# 定义错误页面,如果是 500 错误,则把站点根目录下的 50x.html 返回给用户

        location = /50x.html {   # 可以有多个 location
            root   html;  # 站点根目录（程序目录）
        }

        # proxy the PHP scripts to Apache listening on 127.0.0.1:80
        #
        #location ~ \.php$ {
        #    proxy_pass   http://127.0.0.1;
        #}

        # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
        #
        #location ~ \.php$ {
        #    root           html;
        #    fastcgi_pass   127.0.0.1:9000;
        #    fastcgi_index  index.php;
        #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
        #    include        fastcgi_params;
        #}

        # deny access to .htaccess files, if Apache's document root
        # concurs with nginx's one
        #
        #location ~ /\.ht {
        #    deny  all;
        #}
    }


    # another virtual host using mix of IP-, name-, and port-based configuration
    #
    #server {
    #    listen       8000;
    #    listen       somename:8080;
    #    server_name  somename  alias  another.alias;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}


    # HTTPS server
    #
    #server {
    #    listen       443 ssl;
    #    server_name  localhost;

    #    ssl_certificate      cert.pem;
    #    ssl_certificate_key  cert.key;

    #    ssl_session_cache    shared:SSL:1m;
    #    ssl_session_timeout  5m;

    #    ssl_ciphers  HIGH:!aNULL:!MD5;
    #    ssl_prefer_server_ciphers  on;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}

}
```

## 配置文件的结构

nginx由配置文件中指定的指令控制的模块组成。

指定分为**简单指令**和**块指令**。

* 简单指令

由空格分隔的名称和参数组成，并以";"结尾。

* 块指令

由括号 {和} 保卫的一组附加指令结束。

如果块指令可以在大括号内部有其他指令，则称为上下文（events/http/server/location）。

## 顶级指令

* events （一般链接处理）
* http （HTTP协议流量）
* mail（Mail协议流量）
* stream（TCP协议流量）

对于http协议流量，每个服务器指令控制对特定域或IP地址上的资源请求的处理。服务器上下人中国的一个或者多个位置上下文定义了如何处理特定的URL集合。

对于邮件和TCP流量（mail和stream），服务器指令各自控制到达特定TCP端口或UNIX套接字的流量处理。

备注：

```
user nobody; # a directive in the 'main' context

events {
    # configuration of connection processing
    # 连接过程的配置
}

http {

    # Configuration specific to HTTP and affecting all virtual servers
    # 配置指定HTTP，影响所有的虚拟节点

    server {
        # configuration of HTTP virtual server 1
        # 配置虚拟节点1

        location /one {
            # configuration for processing URIs with '/one'
            # 配置'/one' URL访问的端口
        }

        location /two {
            # configuration for processing URIs with '/two'
            # 配置'/two' URL访问的端口
        }
    }

    server {
        # configuration of HTTP virtual server 2
    }
}

stream {
    # Configuration specific to TCP and affecting all virtual servers

    server {
        # configuration of TCP virtual server 1 
    }
}
```

## 



