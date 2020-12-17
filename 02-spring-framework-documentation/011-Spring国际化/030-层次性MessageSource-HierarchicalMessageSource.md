# 030-层次性MessageSource-HierarchicalMessageSource

[TOC]

## Spring层次性国际化接口

当自己解析不了的时候,调用父类的MessageSource去解析

- org.springframework.context.HierarchicalMessageSource

```java
public interface HierarchicalMessageSource extends MessageSource {

	void setParentMessageSource(@Nullable MessageSource parent);

	@Nullable
	MessageSource getParentMessageSource();
}
```

## 为什么要有层次性

当前找不到的时候,可以去parent里找,拓展了搜索路径,增强了弹性和代码复用