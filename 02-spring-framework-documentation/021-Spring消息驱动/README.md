# 021-Spring消息驱动

spring生态中与消息有关的3个项目分别是

- spring-messaging 模块
- Spring Integration
- Spring Cloud Stream项目

## 什么是SpringMessaging模块

Spring Messaging 模块是定义消息编程模型的基础模块, 内部定义了

- 消息 (Message)
- 消息通道(MessageChannel)
- 消息处理器(MessageHandler)
- 消息映射注解(@MessageMapping)
- 处理器注解(@Handler)

## 什么是Spring Integration

Spring Integration 在 Spring Messaging 的基础上根据 Enterprise Integer Pattern (企业继承模式) 内消息部分的功能抽象了更多消息的概念

- MessageDispatcher(消息分发器)
- Transformer(消息转换器)
- Aggregator(消息聚合器)

## 什么是Spring Cloud Stream项目

Spring Cloud Stream 在 Spring intergration的基础上提出了Binder, Binding 等概念让开发者能够通过Bean 的注入以及相关注解, 就能够轻松完成消息的发送和订阅