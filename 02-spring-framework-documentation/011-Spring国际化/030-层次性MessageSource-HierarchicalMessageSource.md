# 030-层次性MessageSource-HierarchicalMessageSource

[TOC]

## Spring层次性国际化接口

- org.springframework.context.HierarchicalMessageSource

```java
/**
 * Sub-interface of MessageSource to be implemented by objects that
 * can resolve messages hierarchically.
 */
public interface HierarchicalMessageSource extends MessageSource {

	/**
	 * Set the parent that will be used to try to resolve messages
	 * that this object can't resolve.
	 * @param parent the parent MessageSource that will be used to
	 * resolve messages that this object can't resolve.
	 * May be {@code null}, in which case no further resolution is possible.
	 */
	void setParentMessageSource(@Nullable MessageSource parent);

	/**
	 * Return the parent of this MessageSource, or {@code null} if none.
	 */
	@Nullable
	MessageSource getParentMessageSource();

}
```

## 为什么要有层次性

当前找不到的时候,可以去parent里找,拓展了搜索路径,增强了弹性和代码复用