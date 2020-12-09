# 014-Spring配置元信息有哪些

- Bean配置辕信息：通过媒介（Xml.Properties)解析成BeanDefintion
- IoC容器配置元信息：通过媒介（如Xml. Properties)控制IoC容器行为，比如注解驱动，AOP等等
- 外部化配置：通过资源抽象（Properties.YAML等）控制PropertySource
- SpringProfile ： 通过外部化配置，提供条件分支流程