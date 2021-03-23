# 010-什么是SpringCloudSleuth

[TOC]

## 为什么要有Sleuth

Spring Cloud Sleuth为服务之间的调用提供链路追踪，

- 通过Sleuth可以很清楚地了解到一个服务请求经过了哪些服务，每个服务处理花费了多长，从而可以很方便地理清各微服务间的调用关系。

- Sleuth可以进行耗时分析，通过Sleuth可以很方便地了解到每个采样请求的耗时，从而分析出哪些服务调用比较耗时；

- Sleuth可以可视化错误，对于程序未捕捉的异常，可以通过集成Zipkin服务界面看到；

- 通过Sleuth还能对链路优化，对于调用比较频繁的服务实施一些优化措施。

## SpringCloudSleut特性

Spring Cloud Sleuth具有如下的特性：

- 提供对常见分布式跟踪数据模型的抽象：Traces、Spans(形成DAG)、注解和键值注解。Spring Cloud Sleuth虽然基于HTrace，但与Zipkin(Dapper)兼容。
- Sleuth记录了耗时操作的信息以辅助延时分析。通过使用Sleuth，可以定位应用中的耗时原因。
- 为了不写入太多的日志，以至于使生产环境的应用程序崩溃，Sleuth做了如下的工作：
  - 生成调用链的结构化数据。
  - 包括诸如自定义的织入层信息，比如HTTP。
  - 包括采样率配置以控制日志的容量。
  - 查询和可视化完全兼容Zipkin。
- 为Spring应用织入通用的组件，如Servlet过滤器、Zuul过滤器和OpenFeign客户端等等。
- Sleuth可以在进程之间传播上下文(也称为背包)。因此，如果在Span上设置了背包元素，它将通过HTTP或消息传递到下游，发送到其他进程。
- Spring Cloud Sleuth实现了OpenTracing的规范。OpenTracing通过提供平台无关、厂商无关的API，使得开发人员能够方便地添加(或更换)追踪系统的实现。

