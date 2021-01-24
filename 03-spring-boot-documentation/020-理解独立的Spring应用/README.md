# 020-理解独立的Spring应用

[TOC]

## 为什么说SpringBoot是一个独立的Spring应用

SpringBoot通过自定义maven插件[spring-boot-maven-pligin] 插件, 重写了 maven 的 repackage  . 打成了一个独立的jar 包或者war包 

- jar 包或者 war 包都可以直接用java -jar 执行
- 使用特定的打包结构, 将依赖的jar包放入 BOOT-INF/lib 包下 

