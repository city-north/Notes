### [返回目录](/README.md)

# Location匹配原则

转载，[原文](https://segmentfault.com/a/1190000009651161)

## Nginx location 配置语法

```
    1. location [ = | ~ | ~* | ^~ ] uri { ... }
    2. location @name { ... }   
```

## location 配置可以有两种配置方法

```
1.前缀 + uri（字符串/正则表达式）
2.@ + name
```

### 前缀含义

```
    =  ：精确匹配（必须全部相等）
    ~  ：大小写敏感
    ~* ：忽略大小写
    ^~ ：只需匹配uri部分
    @  ：内部服务跳转
```

## Loaction基础

* location是在server块中配置
* 可以根据不同的URL使用不同的配置，处理不同的请求。
* location是有顺序的，会被第一个匹配的location处理



## 例子

## `=`精确匹配

            location = / {
                #规则
            }
            # 则匹配到 `http://www.example.com/` 这种请求。 

## `~`大小写敏感

```
        location ~ /Example/ {
                #规则
        }
        #请求示例
        #http://www.example.com/Example/  [成功]
        #http://www.example.com/example/  [失败]
```

## `~*`大小写忽略

```
    location ~* /Example/ {
                #规则
    }
    # 则会忽略 uri 部分的大小写
    #http://www.example.com/Example/  [成功]
    #http://www.example.com/example/  [成功]
```

## `^~`只匹配以 uri 开头

```
    location ^~ /img/ {
            #规则
    }
    #以 /img/ 开头的请求，都会匹配上
    #http://www.example.com/img/a.jpg   [成功]
    #http://www.example.com/img/b.mp4 [成功]
```

## `@`nginx内部跳转

```
   location /img/ {
        error_page 404 @img_err;
    }
    
    location @img_err {
        # 规则
    }
    #以 /img/ 开头的请求，如果链接的状态为 404。则会匹配到 @img_err 这条规则上。
```



