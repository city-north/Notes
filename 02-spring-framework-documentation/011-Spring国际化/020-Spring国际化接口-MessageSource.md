# 020-Spring国际化接口-MessageSource

[TOC]

## 核心接口MessageSource

```java
org.springframework.context.MessageSource
```

Spring提供的一个策略策略接口用于解析消息，支持此类消息的参数化和国际化。

提供了两种开箱即用的实现:

- org.springframework.context.support.ResourceBundleMessageSource
- org.springframework.context.support.ReloadableResourceBundleMessageSource

## MessageSource接口

```java
public interface MessageSource {
   @Nullable
   String getMessage(String code, @Nullable Object[] args, @Nullable String defaultMessage, Locale locale);

   String getMessage(String code, @Nullable Object[] args, Locale locale) throws NoSuchMessageException;

   String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException;
}
```

里面有三个方法,可以看到核心的三个参数

- 文案模板编码(code)
- 文案模板参数(args) : 一个参数数组，用于填充消息内的参数(参数看起来像`"{0}", "{1,date}", "{2,time}"`)，如果没有参数，则为空。
- defaultMessage—查找失败时返回的默认消息
- 区域(Locale) : 执行查找的区域设置

##### MessageSourceResolvable 是三个参数的聚合

```java
@FunctionalInterface
public interface MessageSourceResolvable {
	@Nullable
	String[] getCodes();

	@Nullable
	default Object[] getArguments() {
		return null;
	}
	@Nullable
	default String getDefaultMessage() {
		return null;
	}
}
```





