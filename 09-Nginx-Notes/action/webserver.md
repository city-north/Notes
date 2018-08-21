[返回目录](/README.md)

# 配置Nginx作为Web服务器

## 配置文件可以定义什么

1. 定义它处理哪些URL以及如何处理这些URL上的资源的HTTP请求。
2. 定义了一组控制对特定域或IP地址的请求的处理的虚拟服务器

## Nginx配置文件可以定义

1. 配置HTTP流量的每个虚拟服务器定义了成为位置的特殊配置实例，它们控制特定URL集合的处理。
2. 配置每个位置定义自己的映射到此位置的请求发生的情况。每个位置都可以代理请求或返回一个文件，
3. 配置URL，以便将请求重定向到另一个位置或虚拟服务。
4. 配置返回特定的错误代码
5. 配置特定的页面对应每个错误代码

```
#user  nobody;
worker_processes  1;

#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       mime.types;
    default_type  application/octet-stream;

    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;

    server {
        listen       80;
        server_name  localhost;
        #charset koi8-r;
        #access_log  logs/host.access.log  main;
        location / {
            root   html;
            index  index.html index.htm;
        }
        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
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
}
```

## 具体配置

* [设置虚拟服务器（server ）](/action/webserver/virtualServer.md)
* [配置位置（server-&gt;location）](/action/webserver/location.md)
* 使用变量
* 返回特定状态码
* 重写请求中的URI
* 重写HTTP响应
* 处理错误



