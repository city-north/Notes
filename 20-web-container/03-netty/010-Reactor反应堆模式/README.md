# 010-Reactor反应堆模式

[TOC]

## Reactor反应器模式为何如此重要

到目前为止，高性能网络编程都绕不开反应器模式

- Web服务器Nginx
- Redis
- Netty

## Reactor反应器模式简介

反应器模式由Reactor反应器线程、Handler处理器两大角色组成

- Reactor反应器模式的职责： 负责响应IO事件， 并且分发到Handler处理器
- Handlers处理器的职责：非阻塞的执行业务处理逻辑

