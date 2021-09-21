# 010-Nacos配置中心基本使用

[TOC]

## Nacos配置的基本使用

#### **Data ID**的命名规范

语法

```
${prefix}-${profile}.${file-extension}
```

命名规则

```
${spring.application.name}-${spring.profiles.active}.${file-extension}
```

例如 

```java
eforce-platform-test.yml  //eforce-platform应用的测试环境
```



