# GatewayFilter Factories

> - **本文作者：**二当家的
> - **本文链接：** [2019/01/01/spring cloud gateway系列教程2——GatewayFilter_上篇/](https://www.edjdhbb.com/2019/01/01/spring cloud gateway系列教程2——GatewayFilter_上篇/)
> - **版权声明：** 本博客所有文章除特别声明外，均采用 [CC BY-NC-SA 3.0 CN](http://creativecommons.org/licenses/by-nc-sa/3.0/cn/) 许可协议。转载请注明出处！

Route filters可以通过一些方式修改HTTP请求的输入和输出，针对某些特殊的场景，Spring Cloud Gateway已经内置了很多不同功能的GatewayFilter Factories。

下面就来通过例子逐一讲解这些GatewayFilter Factories。

## 1. AddRequestHeader GatewayFilter Factory

AddRequestHeader GatewayFilter Factory通过配置name和value可以增加请求的header。

**application.yml**：

```yaml
spring:
  cloud:
    gateway:
      routes:
      - id: add_request_header_route
        uri: http://www.google.com
        filters:
        - AddRequestHeader=X-Request-Foo, Bar
```

对匹配的请求，会额外添加`X-Request-Foo:Bar`的header。

![image-20191213124926755](assets/image-20191213124926755.png)

## 2. AddRequestParameter GatewayFilter Factory

AddRequestParameter GatewayFilter Factory通过配置name和value可以增加请求的参数

**application.yml**：

```
spring:
  cloud:
    gateway:
      routes:
      - id: add_request_parameter_route
        uri: http://www.google.com
        filters:
        - AddRequestParameter=foo, bar
```

对匹配的请求，会额外添加`foo=bar`的请求参数。

![image-20191213124629007](assets/image-20191213124629007.png)

## 3. AddResponseHeader GatewayFilter Factory

AddResponseHeader GatewayFilter Factory通过配置name和value可以增加响应的header。
**application.yml**：

```
spring:
  cloud:
    gateway:
      routes:
      - id: add_request_header_route
        uri: http://www.google.com
        filters:
        	- AddResponseHeader=MyResponseHeader, hello
```

对匹配的请求，响应返回时会额外添加`MyResponseHeader: hello`的header返回。

![image-20191213132922956](assets/image-20191213132922956.png)

#### 4. Hystrix GatewayFilter Factory

Hystrix是Netflix实现的断路器模式工具包，The Hystrix GatewayFilter就是将断路器使用在gateway的路由上，目的是保护你的服务避免级联故障，以及在下游失败时可以降级返回。

项目里面引入`spring-cloud-starter-netflix-hystrix`依赖，并提供`HystrixCommand`的名字，即可生效Hystrix GatewayFilter。
**application.yml**：

```yaml
spring:
  cloud:
    gateway:
      routes:
      - id: hystrix_route
        uri: http://www.google.com
        filters:
        - Hystrix=myCommandName
```

那么剩下的过滤器，就会包装在名为`myCommandName`的HystrixCommand中运行。