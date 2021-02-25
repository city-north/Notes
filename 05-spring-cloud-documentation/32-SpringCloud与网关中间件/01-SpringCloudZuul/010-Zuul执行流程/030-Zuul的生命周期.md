# 030-Zuul的生命周期

[TOC]

## Zuul的生命周期

- pre , 在 zuul 按照规则路由到下一级服务之前执行,可以加一些预处理,如认证,鉴权,限流等,都应该考虑这类 Filter
- route , zuul路由的执行者, 是 AppacheHttpClient 或者 Netfilx Ribbon 构建和发送原始 Http 请求的地方,目前已经支持 OKHttp
- post ,  这类 Filter 是在原服务返回结果或者异常信息发生之后执行的,如果需要对返回信息进行一定的处理,则在这里处理
- error,  整个生命周期如果发生异常,进入 error Filter

## 



## 