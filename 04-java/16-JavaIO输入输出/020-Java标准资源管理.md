## 020-Java标准资源管理

[TOC]

- 简单实现
  - 实现URLStreamHandler并放置在 sun.net.www.protocol.${protocol}.Handler 包下
- 自定义实现
  - 实现URLStreamHandler
  - 添加-Djava.protocol.handler.pkgs启动参数,指向URLStreamHandler实现类的包下
- 高级实现
  - 实现 URLSTreamHandlerFactory并传递到URL

## 