# Route Predicate Factories

> - **本文作者：**二当家的
> - **本文链接：** [2018/12/25/spring cloud gateway系列教程1——Route Predicate/](https://www.edjdhbb.com/2018/12/25/spring cloud gateway系列教程1——Route Predicate/)
> - **版权声明：** 本博客所有文章除特别声明外，均采用 [CC BY-NC-SA 3.0 CN](http://creativecommons.org/licenses/by-nc-sa/3.0/cn/) 许可协议。转载请注明出处！

Spring Cloud Gateway是使用Spring WebFlux的`HandlerMapping`作为匹配路由底层实现，本身已自带很多Route Predicate Factories，分别匹配不同的http请求属性，多个Route Predicate Factories也可以通过`and`进行逻辑合并匹配。

#### 1. After Route Predicate Factory

After Route Predicate Factory使用的是时间作为匹配规则，只要当前时间大于设定时间，路由才会匹配请求。
**application.yml**：

```
spring:
  cloud:
    gateway:
      routes:
      - id: after_route
        uri: http://www.google.com
        predicates:
        - After=2018-12-25T14:33:47.789+08:00
```

这个路由规则会在东8区的2018-12-25 14:33:47后，将请求都转跳到google。

#### 2. Before Route Predicate Factory

Before Route Predicate Factory也是使用时间作为匹配规则，只要当前时间小于设定时间，路由才会匹配请求。
**application.yml**：

```
spring:
  cloud:
    gateway:
      routes:
      - id: before_route
        uri: http://www.google.com
        predicates:
        - Before=2018-12-25T14:33:47.789+08:00
```

这个路由规则会在东8区的2018-12-25 14:33:47前，将请求都转跳到google。

#### 3. Between Route Predicate Factory

Between Route Predicate Factory也是使用两个时间作为匹配规则，只要当前时间大于第一个设定时间，并小于第二个设定时间，路由才会匹配请求。

**application.yml**：

```
spring:
  cloud:
    gateway:
      routes:
      - id: between_route
        uri: http://www.google.com
        predicates:
        - Between=2018-12-25T14:33:47.789+08:00, 2018-12-26T14:33:47.789+08:00
```

这个路由规则会在东8区的2018-12-25 14:33:47到2018-12-26 14:33:47之间，将请求都转跳到google。

#### 4. Cookie Route Predicate Factory

Cookie Route Predicate Factory使用的是cookie名字和正则表达式的value作为两个输入参数，请求的cookie需要匹配cookie名和符合其中value的正则。
**application.yml**：

```
spring:
  cloud:
    gateway:
      routes:
      - id: cookie_route
        uri: http://www.google.com
        predicates:
        - Cookie=cookiename, cookievalue
```

路由匹配请求存在cookie名为cookiename，cookie内容匹配cookievalue的，将请求转发到google。

#### 5. Header Route Predicate Factory

Header Route Predicate Factory，与Cookie Route Predicate Factory类似，也是两个参数，一个header的name，一个是正则匹配的value。
**application.yml**：

```
spring:
  cloud:
    gateway:
      routes:
      - id: header_route
        uri: http://www.google.com
        predicates:
        - Header=X-Request-Id, \d+
```

路由匹配存在名为`X-Request-Id`，内容为数字的header的请求，将请求转发到google。

#### 6. Host Route Predicate Factory

Host Route Predicate Factory使用的是host的列表作为参数，host使用Ant style匹配。
**application.yml**：

```
spring:
  cloud:
    gateway:
      routes:
      - id: host_route
        uri: http://www.google.com
        predicates:
        - Host=**.somehost.org,**.anotherhost.org
```

路由会匹配Host诸如：`www.somehost.org` 或 `beta.somehost.org`或`www.anotherhost.org`等请求。

#### 7. Method Route Predicate Factory

Method Route Predicate Factory是通过HTTP的method来匹配路由。
**application.yml**：

```
spring:
  cloud:
    gateway:
      routes:
      - id: method_route
        uri: http://www.google.com
        predicates:
        - Method=GET
```

路由会匹配到所有GET方法的请求。

#### 8. Path Route Predicate Factory

Path Route Predicate Factory使用的是path列表作为参数，使用Spring的`PathMatcher`匹配path，可以设置可选变量。
**application.yml**：

```
spring:
  cloud:
    gateway:
      routes:
      - id: host_route
        uri: http://www.google.com
        predicates:
        - Path=/foo/{segment},/bar/{segment}
```

上面路由可以匹配诸如：`/foo/1` 或 `/foo/bar` 或 `/bar/baz`等
其中的segment变量可以通过下面方式获取：

```
PathMatchInfo variables = exchange.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE);
Map<String, String> uriVariables = variables.getUriVariables();
String segment = uriVariables.get("segment");
```

在后续的GatewayFilter Factories就可以做对应的操作了。

#### 9. Query Route Predicate Factory

Query Route Predicate Factory可以通过一个或两个参数来匹配路由，一个是查询的name，一个是查询的正则value。
**application.yml**：

```
spring:
  cloud:
    gateway:
      routes:
      - id: query_route
        uri: http://www.google.com
        predicates:
        - Query=baz
```

路由会匹配所有包含`baz`查询参数的请求。
**application.yml**：

```
spring:
  cloud:
    gateway:
      routes:
      - id: query_route
        uri: http://www.google.com
        predicates:
        - Query=foo, ba.
```

路由会匹配所有包含`foo`，并且`foo`的内容为诸如：`bar`或`baz`等符合`ba.`正则规则的请求。

#### 10. RemoteAddr Route Predicate Factory

RemoteAddr Route Predicate Factory通过无类别域间路由(IPv4 or IPv6)列表匹配路由。
**application.yml**：

```
spring:
  cloud:
    gateway:
      routes:
      - id: remoteaddr_route
        uri: http://www.google.com
        predicates:
        - RemoteAddr=192.168.1.1/24
```

上面路由就会匹配RemoteAddr诸如`192.168.1.10`等请求。

##### 10.1 Modifying the way remote addresses are resolved

RemoteAddr Route Predicate Factory默认情况下，使用的是请求的remote address。但是如果Spring Cloud Gateway是部署在其他的代理后面的，如Nginx，则Spring Cloud Gateway获取请求的remote address是其他代理的ip，而不是真实客户端的ip。

考虑到这种情况，你可以自定义获取remote address的处理器`RemoteAddressResolver`。当然Spring Cloud Gateway也提供了基于X-Forwarded-For请求头的`XForwardedRemoteAddressResolver`。
*熟悉Http代理协议的，都知道X-Forwarded-For头信息做什么的，不熟悉的可以自己谷歌了解一下。*

`XForwardedRemoteAddressResolver`提供了两个静态方法获取它的实例：
`XForwardedRemoteAddressResolver::trustAll`得到的`RemoteAddressResolver`总是获取X-Forwarded-For的第一个ip地址作为remote address，这种方式就比较容易被伪装的请求欺骗，模拟请求很容易通过设置初始的`X-Forwarded-For`头信息，就可以欺骗到gateway。

`XForwardedRemoteAddressResolver::maxTrustedIndex`得到的`RemoteAddressResolver`则会在`X-Forwarded-For`信息里面，从右到左选择信任最多`maxTrustedIndex`个ip，因为`X-Forwarded-For`是越往右是越接近gateway的代理机器ip，所以是越往右的ip，信任度是越高的。
那么如果前面只是挡了一层Nginx的话，如果只需要Nginx前面客户端的ip，则`maxTrustedIndex`取1，就可以比较安全地获取真实客户端ip。

使用java的配置：
**GatewayConfig.java**：

```
RemoteAddressResolver resolver = XForwardedRemoteAddressResolver
    .maxTrustedIndex(1);

...

.route("direct-route",
    r -> r.remoteAddr("10.1.1.1", "10.10.1.1/24")
        .uri("http://www.google.com")
.route("proxied-route",
    r -> r.remoteAddr(resolver,  "10.10.1.1", "10.10.1.1/24")
        .uri("http://www.google.com")
)
```

