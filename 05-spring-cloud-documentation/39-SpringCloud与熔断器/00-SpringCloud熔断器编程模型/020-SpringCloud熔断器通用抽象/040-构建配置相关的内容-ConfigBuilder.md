# 040-构建配置相关的内容-ConfigBuilder

[TOC]

## 一言蔽之

构建配置相关内容, 比如, 配置时间窗口, 错误阈值

```java
public interface ConfigBuilder<CONF> {

   CONF build();

}
```

ConifgBuilder 用于构建一个配置 , 这里的 CONF 表示 Config配置

比如

- Alibaba Sentinel 的实现 SentinelConfigBuilder
- Netflix 的实现 AbstractHystrixConfigBuilder

