# SpringCloud父子容器抽象

> https://cloud.tencent.com/developer/article/1403379

容器大致分为三层，分别对应上面的三类：

- [BootStrap上下文](#BootStrap上下文)：由SpringCloud 监听器创建，用来初始化 SpringCloud 上下文，也是祖先容器。
- [SpringBoot上下文](#SpringBoot上下文)：由SpringBoot创建，也是项目中常用的Spring容器。
- [微服务配置上下文](#微服务配置上下文)：Feign和Ribbon配置类对应的上下文，由配置容器抽象工厂 NamedContextFactory 创建，用于容器隔离。

![image-20201011121842466](../../../assets/image-20201011121842466.png)

## BootStrap上下文

 [020-SpringCloudBootStrap上下文.md](020-SpringCloudBootStrap上下文.md) 

## SpringBoot上下文

 [010-SpringBoot上下文.md](../../../03-spring-boot-documentation/08-理解SpringApplication/010-SpringBoot上下文.md) 

## 微服务配置上下文