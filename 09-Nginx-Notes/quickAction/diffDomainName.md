[返回目录](/README.md)

# 通过不同的域名访问不同的主机

一台服务器运行多个网站，适用于不同的域名访问不同网站，但是只有一台服务器的情况。

```
server {
    listen       80;
    server_name  localhost;

    #charset koi8-r;

    #access_log  logs/host.access.log  main;

    location / {
        root   test1;
        index  index.html index.htm;
    }
}
server {
    listen       80;   
    server_name  localhost;

    #charset koi8-r;

    #access_log  logs/host.access.log  main;

    location / {
        root   test2;
        index  index.html index.htm;
    }
}
```



