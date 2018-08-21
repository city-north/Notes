[返回目录](/README.md)

# 通过端口区分不同的主机

修改配置文件nginx.conf

```
server {
    listen       80;
    server_name  localhost;

    #charset koi8-r;

    #access_log  logs/host.access.log  main;

    location / {
        root   html;
        index  index.html index.htm;
    }
}
server {
    listen       81;   #监听81
    server_name  localhost;

    #charset koi8-r;

    #access_log  logs/host.access.log  main;

    location / {
        root   html81;
        index  index.html index.htm;
    }
}
```



