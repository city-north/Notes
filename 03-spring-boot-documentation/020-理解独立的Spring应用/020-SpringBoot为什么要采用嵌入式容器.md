# SpringBoot为什么要采用嵌入式容器



## 目录

[TOC]

## 一言蔽之

SpringBoot采用嵌入式容器的和核心是想要掌握整个应用的生命周期,在SpringBoot之前, Spring一直是没有掌握的

## 为什么之前没有掌握

SpringBoot之前的应用,都是作为胶水用来连接其他组件,最终这些应用被打成WAR包,部署到Servlet容器中,**Spring容器的初始化**要依赖容器的回调机制

- SpringWeb中的 **ContextLoaderListener** , 前者 ServletContext 生命周期构建 web Root Spring 上下文
- SpringWeb MVC 中的 **DispatcherServlet**  , 使用Servlet 生命周期创建Spring应用上下文

这两种都是属于被动的回调,没有完整应用的主导权

## 怎么掌握的

Spring使用嵌入式容器方式启动后,嵌入式容器就成为了应用的一部分,容器就属于Spring上下文中的组件Beans, 这些组件和其他组件均由其他自动装配的特性组成SpringBean 定义(BeanDefinition),随着Spring应用上下文启动而注册并初始化

其核心驱动是 SpringApplication

