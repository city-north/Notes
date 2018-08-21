[返回目录](/README.md)

[返回上级目录](/action/webserver.md)

# 设置虚拟服务器

虚拟服务器由http上下文的服务器指令定义：

```
http {
    include       mime.types;
    default_type  application/octet-stream;
       '"$http_user_agent" "$http_x_forwarded_for"';
    sendfile        on;
    keepalive_timeout  65;

    server {
        listen       80; 

        server_name  localhost;

        #charset koi8-r;  #字符集

        #access_log  logs/host.access.log  main; 

    }
}
```

## server指令

`server`配置块通常包括一个`listen`指令，用于指定服务器侦听请求的IP地址和端口\(或Unix域套接字和路径\)。IPv4和IPv6地址均被接受。

例如：监听IP地址127.0.0.1和端口8080的服务器

```
server {
    listen 127.0.0.1:8080;
    # The rest of server configuration
}
```

* 如果省略端口，则监听80端口
* 如果省略了地址，则监听所有地址
* 如果没有包含listen指令，则“标准”端口为80/tcp，“default”端口为8000/tcp，具体取决取决于超级用户权限
* 如果有多个服务器与请求的IP地址和端口相匹配，则根据server\_name测试请求的主机头域

server\_name的参数可以是完整的名称，也可以是通配符或者正则表达式

```
server{
    listen 80;
    server_name example.org www.example.org;
}
```

搜索顺序：

* 确切的名称（完整准确的名称）
* 以星号开头的最长通配符，例如 \*.example.org
* 以星号结尾的最长通配符，如：mail.\*
* 第一个匹配正则表达式（按照出现在配置文件中的顺序）

如果主机头字段与服务器名称不匹配，则Nginx将会请求路由到请求到达端口的默认服务器。默认服务器是nginx.conf文件中列出的第一个服务器

一个完整的例子：

```
server {
    listen       80;
    server_name vhost1.com www.vhost1.com;
    index index.html index.html;
    root  /data/www/vhost1;
    access_log  /var/log/vhost1.com.log;
}

server {
    listen       80;
    server_name vhost2.com www.vhost2.com;
    index index.html index.html;
    root  /data/www/vhost2;
    access_log  /var/log/vhost2.com.log;
}
```

* vhost1.com网站的主目录在`/data/www/vhost1`
* vhost2.com网站的主目录在`/data/www/vhost2`



