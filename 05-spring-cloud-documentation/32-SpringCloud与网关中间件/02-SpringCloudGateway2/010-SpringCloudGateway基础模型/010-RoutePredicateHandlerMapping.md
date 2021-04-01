# 010-SpringCloudGateway基础模型

[TOC]

## SpringCloud模型简介

Spring Cloud Gateway 基于 reactive 响应式模型, 这是一个非阻塞

## SpringCloud 如何做

SpringCloud Gateway 定义了 RoutePredicateHandlerMapping 这个 HandlerMapping 接口实现类(该接口时WebFlux定义的一个处理流出量请求的处理器) 用于处理请求的映射关系, 得到的结果被 HandlerAdapter 处理

```java
@Override
protected Mono<?> getHandlerInternal(ServerWebExchange exchange) {
   // don't handle requests on the management port if set
   if (managmentPort != null && exchange.getRequest().getURI().getPort() == managmentPort.intValue()) {
     //①
      return Mono.empty();
   }
   exchange.getAttributes().put(GATEWAY_HANDLER_MAPPER_ATTR, getSimpleName());

   return lookupRoute(exchange) // ②
         // .log("route-predicate-handler-mapping", Level.FINER) //name this
         .flatMap((Function<Route, Mono<?>>) r -> { // ③
            exchange.getAttributes().remove(GATEWAY_PREDICATE_ROUTE_ATTR);
            if (logger.isDebugEnabled()) {
               logger.debug("Mapping [" + getExchangeDesc(exchange) + "] to " + r);
            }

            exchange.getAttributes().put(GATEWAY_ROUTE_ATTR, r);
            return Mono.just(webHandler);
         }).switchIfEmpty(Mono.empty().then(Mono.fromRunnable(() -> {
            exchange.getAttributes().remove(GATEWAY_PREDICATE_ROUTE_ATTR);
            if (logger.isTraceEnabled()) {
               logger.trace("No RouteDefinition found for [" + getExchangeDesc(exchange) + "]");
            }
         })));
}
```

- ①应用开启了management 功能 (SpringCloud Actuator) 若端口与应用不一致, 则不对请求进行处理

- ②根据请求信息, 通过lookup方法啊找到合适的Route信息
- ③ 通过faltMap将Route 信息存储在请求属性中, 为后续 FilteringWebHandler 的执行准备数据
- 