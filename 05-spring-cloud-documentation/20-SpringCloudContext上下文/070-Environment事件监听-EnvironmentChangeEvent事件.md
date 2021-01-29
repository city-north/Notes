

# 070-Environment事件监听-EnvironmentChangeEvent事件

[TOC]

Config Client应用程序会监听EnvironmentChangeEvent事件，当监听到一个EnvironmentChangeEvent时，它将持有一个被改变的键值对列表，应用程序使用这些值来：

- 重新绑定所有的@ConfigurationProperties的Bean实例，更新本地的配置属性。
- 为在logging.level.*的所有属性设置日志的等级。
- 一般来讲，Config Client默认不会使用轮询方法来监听Environment中的改变。

在Spring Cloud中，Spring Cloud Config Server使用Spring Cloud Bus将EnvironmentChangeEvent广播到所有的Config Client中，通知它们Environment发生变化。

### EnvironmentChangeEvent事件

EnvironmentChangeEvent是一个事件类，用于在Environment发生修改时发布事件。开发者可以通过访问/configprops端点(常规的Spring Boot Actuator端点)来验证这些更改是否绑定到@ConfigurationProperties的Bean实例上。

例如一个DataSource的最大连接数量在运行时被改变了(DataSource默认由Spring Boot创建，属于@ConfigurationProperties的Bean)并且动态增加容量，可以通过查看Config Client应用程序的/configprops端点来验证DataSource的最大连接池数量是否发生变化。
