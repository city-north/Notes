

# 080-SpringBoot中的MessageSource实现

[TOC]

## SpringBoot为什么要新建MessageSourceBean?

- AbstractApplicationContext 的实现决定了MessageSource内建实现
- SpringBoot通过外部化配置简化MessageSource Bean的构建
- SpringBoot基于 BeanValidation校验非常普遍

## 自动装配类核心入口

org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration

```java
	@Bean
	public MessageSource messageSource() {
		MessageSourceProperties properties = messageSourceProperties();
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		if (StringUtils.hasText(properties.getBasename())) {
					messageSource.setBasenames(StringUtils.commaDelimitedListToStringArray(
					StringUtils.trimAllWhitespace(properties.getBasename())));
		}
		if (properties.getEncoding() != null) {
			messageSource.setDefaultEncoding(properties.getEncoding().name());
		}
		messageSource.setFallbackToSystemLocale(properties.isFallbackToSystemLocale());
		Duration cacheDuration = properties.getCacheDuration();
		if (cacheDuration != null) {
			messageSource.setCacheMillis(cacheDuration.toMillis());
		}
		messageSource.setAlwaysUseMessageFormat(properties.isAlwaysUseMessageFormat());
		messageSource.setUseCodeAsDefaultMessage(properties.isUseCodeAsDefaultMessage());
		return messageSource;
	}
```

