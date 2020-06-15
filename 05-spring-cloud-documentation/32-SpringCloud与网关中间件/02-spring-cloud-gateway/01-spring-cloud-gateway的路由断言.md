# SC gatway 的路由断言

<img src="../../../assets/image-20200615123602357.png" alt="image-20200615123602357" style="zoom:50%;" />

SpringCloud Gateway 路由匹配功能以

- Spring WebFlux 中的 Handler Mapping 为基础实现的
- 路由断言工厂组成的
- 过滤器

## 什么是断言工厂,为什么要有断言工厂

当 HttpRequest 请求进入 Spring Cloud Gateway 的时候,网关中的路由断言工厂会根据配置的路由规则,对 Http Request 请求进行断言匹配

- 成功则进入下一步处理
- 失败则直接返回错误信息

为什么要有?

对请求的合法性先进行 true 或者 false的校验, 主要是组织不合法的请求

## 断言工厂

- After 路由断言工厂

  > 断言请求时间必须在规定的时间之后

- Before 路由断言工厂

  > 断言请求时间必须在规定的时间之前

- Between 路由断言工厂

  > 断言请求时间必须在固定的范围之内

- Cookile 路由断言工厂

  > 断言请求 cookile 中带的值

- Header路由断言工厂

  > 断言路由 header 中带的值

- Host 路由断言工厂

  > 断言请求中的 host 是给定的规则

- Method 路由断言工厂

  > 断言请求类型,例如 GET 或者 POST

- Query路由断言工厂

  > 断言请求参数是否和配置的一致

- RemoteAddr 断言工厂

  > 断言请求的远程 ip地址是否是规定的规则

### After 路由断言工厂

