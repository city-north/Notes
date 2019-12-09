# 使用自动化配置

`@EnableAutoConfiguration`注解添加后:

- 自动扫描 configuration 类里对 bean 的声明并装载
- 配置,调用组件工具(如内嵌的 tomcat)
- 这个机制基于 classpath 下 jar 包的依赖,这些 jar 包往往通过 starter 提供,
- Starters 和 autoconfiguration 互相依赖

## 原理

待补充