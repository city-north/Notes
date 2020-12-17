# 基于注解编程

## 目录

- [Spring注解编程历史](010-Spring注解编程历史.md) 

## Spring注解汇总

- [配置类注解](#配置类注解)

### 配置类注解

| 注解名称       | 说明                                                         |
| -------------- | ------------------------------------------------------------ |
| @Configuration | 把一个类作为IoC容器，它的某个方法头上如果注册了@Bean,就会作为这个Spring容器中的Bean |
| @ComponentScan | 在配置类上添加ComponentScan 注解，该注解默认会扫描该类所在包下所有的配置类，相当于xml中的`<context:compoent-scan>` |
| @Scope         | 用于指定Bean的作用域                                         |
| @Lazy          | 表示延迟加载                                                 |
| @Conditional   | Spring4开始提供，它是作用是按照一定的条件进行判断，满足条件的给容器注册Bean |
| @PostConstruct | 用于指定初始化方法（用在方法上）                             |
| @PreDestory    | 用于指定销毁方法                                             |
| @DependsOn     | 定义Bean初始化以及销毁的顺序                                 |

### 赋值类组件

| 注解名称   | 说明     |
| ---------- | -------- |
| @Component | 泛化组件 |
|            |          |
|            |          |
|            |          |
|            |          |
|            |          |
|            |          |
|            |          |