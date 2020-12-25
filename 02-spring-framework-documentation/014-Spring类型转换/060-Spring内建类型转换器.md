# 060-Spring内建类型转换器

[TOC]

## Spring内建实现

| 转换场景            | 实现类所在包名（package)                     |
| ------------------- | -------------------------------------------- |
| 日期/时间相关       | org.springframework.format.datatime          |
| Java8 日期/时间相关 | org.springframework.format.datatime.standard |
| 通用实现            | org.springframework.core.convert.support     |

## StringToArrayConverter-String转Array方式

我们可以看到类描述符是final class ,这意味着它不给你拓展而且是包内可见

```java
final class StringToArrayConverter implements ConditionalGenericConverter {

	private final ConversionService conversionService;


	public StringToArrayConverter(ConversionService conversionService) {
		this.conversionService = conversionService;
	}


	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, Object[].class));
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return ConversionUtils.canConvertElements(sourceType, targetType.getElementTypeDescriptor(),
				this.conversionService);
	}

	@Override
	@Nullable
	public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (source == null) {
			return null;
		}
		String string = (String) source;
		String[] fields = StringUtils.commaDelimitedListToStringArray(string);
		TypeDescriptor targetElementType = targetType.getElementTypeDescriptor();
		Assert.state(targetElementType != null, "No target element type");
		Object target = Array.newInstance(targetElementType.getType(), fields.length);
		for (int i = 0; i < fields.length; i++) {
			String sourceElement = fields[i];
			Object targetElement = this.conversionService.convert(sourceElement.trim(), sourceType, targetElementType);
			Array.set(target, i, targetElement);
		}
		return target;
	}

}

```

