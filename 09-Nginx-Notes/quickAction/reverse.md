[返回目录](/README.md)

# 通过URL反向代理

实例：

* 输入www.eric.com跳转8080端口
* 输入www.chen.com跳转8081端口

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
        server_name  www.chen.com;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            proxy_pass http://localhost:8081;
            index  index.html index.htm;
        }

    }

    server {
        listen       80;
        server_name  www.eric.com;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            proxy_pass http://localhost:8080;
            index  index.html index.htm;
        }

    }

}
```

实例2：

* 输入www.eric.com跳转8080端口
* 输入www.eric.com/chen跳转8081端口

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


    #access_log  logs/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;

    server {
        listen       80;
        server_name  www.eric.com;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;


        location ^~ /chen/ {

            proxy_pass http://127.0.0.1:8081/;
            index  index.html index.htm;
        }
    location / {
            proxy_pass http://localhost:8080;
            index  index.html index.htm;
        }

    }

    server {
        listen       80;
        server_name  www.chen.com;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;


        location / {
            proxy_pass http://localhost:8081;
            index  index.html index.htm;
        }

    }





}
```

## Location配置

[location配置解析](/quickAction/location.md)

