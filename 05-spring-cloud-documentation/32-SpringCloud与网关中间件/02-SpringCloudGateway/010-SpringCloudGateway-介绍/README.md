# 010-SpringCloudGateway介绍

[TOC]

## 简介

SpringCloud Gateway 定义了一个 RoutePredicateHandlerMapping 类,  实际上是一个HandlerMapping 接口实现类, WebFlux定义的一个处理流量请求的处理器, 用于处理请求的映射关系, 得到的结果被HandlerAdaper 处理