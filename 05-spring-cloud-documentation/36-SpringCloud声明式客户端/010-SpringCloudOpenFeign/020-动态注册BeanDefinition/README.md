# 020-动态注册BeanDefinition

[TOC]

OpenFeign可以通过多种方式进行自定义配置，配置的变化会导致接口类初始化时使用不同的Bean实例，从而控制OpenFeign的相关行为，比如说网络请求的编解码、压缩和日志处理。

可以说，了解OpenFeign配置和实例初始化的流程与原理对于我们学习和使用OpenFeign有着至关重要的作用，**而且Spring Cloud的所有项目的配置和实例初始化过程的原理基本相同，**

**了解了OpenFeign的原理，就可以触类旁通，一通百通了。**

