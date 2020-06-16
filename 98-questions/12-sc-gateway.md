# springCloudGateway

 [README.md](../05-spring-cloud-documentation/32-SpringCloud与网关中间件/02-spring-cloud-gateway/README.md) 

#### 是什么

> 实际上就是路由,断言和过滤器,实现各种各样网关的功能

分布式网关系统,提供了简单,统一高效的 API路由管理方式,基于它的断言和过滤器,来实现各种各样的需求,如安全,监控,埋点限流

#### 什么是断言? 有什么用

gateway 项目中的断言函数实际上是 Spring5.0中的 ServerWebExchange 机制

有了断言,我们可以轻易的对请求进行判断是否符合要求,常用的有

- 判断请求时间是否符合要求
- 判断 header 里参数是否符合要求
- 判断 host 是否符合要求
- 判断 query 参数是否符合要求
- 判断远程地址是否符合要求

如果失败则直接返回错误消息

#### Spring Cloud  中的 Filter 有哪些

分类主要有两种,他俩都是一个方法 `Mono filter()`,唯一的区别就是 GatewayFilter 继承了 `ShortcutConfigurable`接口

- Gateway Filter 

> 网关过滤器,应用于单个路由或者一个分组的路由上,相当于一个 Filter 过滤器,可以对访问的 URL 过滤,进行横切处理,应用场景包括超时,安全等

- Global Filter 全局过滤器

> 应用到所有的路由上

内置了一些 filter 

- AddRequestHeader 过滤器工厂

> 用于对匹配上的请求加上 header

- AddRequestParameter 过滤器

> 用于对匹配上的过滤器加上请求参数

- RewritePath 过滤器

> 重写 path 

- AddResponseHeader 过滤器

> 从网关返回的响应添加 Header

- StripPrefix 过滤器

> 去掉前缀,如 /demo/app/text 变为 /app/text

- Retry 过滤器

> 重试策略

- Hystrix 过滤器

> 服务降级

#### `ServerWebExchange`

### 权重路由

 [051-gateway-权重路由.md](../05-spring-cloud-documentation/32-SpringCloud与网关中间件/02-spring-cloud-gateway/051-gateway-权重路由.md) 

有时候我们想打一部分流量到 v1版本,打一部分流量到 v2 版本