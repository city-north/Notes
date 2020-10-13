# Spring 核心特性

- IoC容器
- Spring事件(Events)
- 资源管理(Resources)
- 国际化(i18n)
- 校验(validation)
- 数据绑定(Data Binding)
- 类型转换(Type Conversion)
- Spring 表达式(Spring Express Language)
- 面向切面编程

## Spring事件(Events)

Spring的事件是基于Java标准事件来进行拓展的

## 资源管理(Resources)

## 数据绑定(Data Binding)

简单来说就是外面的一些属性和Java的POJO的 setter 和getter绑定

## Web技术(Web)

- Web Servlet 技术栈 (Spring1 - Spring5 唯一支持)
  - Spring MVC
  - WebSocket
- Web Reactive 技术栈(Spring 5开始支持)
  - Spring WebFlux
  - WebClient (将HttpClient的同步方式改为异步)
  - WebSocket

SpringMVC 和Spring的WebFlux 在注解方面是一样的,只是底层的实现不一样,传统的SpringMVC 是需要Servlet引擎来支撑的

Reactive 通常情况下是 Netty 的 webServer ,当然也可以用Servlet引擎来进行实现



# Spring技术整合

- 