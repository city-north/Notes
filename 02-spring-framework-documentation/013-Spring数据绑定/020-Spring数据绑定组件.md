# 020-Spring数据绑定组件

[TOC]

## 标准组件

- org.springframework.validation.DataBinder

## Web组件

- org.springframework.web.bind.WebDataBinder
- org.springframework.web.bind.ServletRequestDataBinder
- org.springframework.web.bind.support.WebRequestDataBinder
- org.springframework.web.bind.support.WebExchangeDataBinder

## DataBinder

### DataBinder的核心属性

| 属性                 | 说明                                                     |
| -------------------- | -------------------------------------------------------- |
| target               | 目标关联Bean                                             |
| objectName           | 目标Bean名称                                             |
| bindingResult        | 属性绑定结果                                             |
| typeConverter        | 类型转换器<br />利用JavaBean中的PropertiesEditor来实现的 |
| conversionService    | 类型转化服务<br />使用Spring自己的转换器来实现的         |
| messageCodesReslover | 校验错误文案Code处理器                                   |
| validators           | 关联的BeanValidator实例集合                              |

### DataBinder的绑定方法

bind方法将 PropertyValues(key-value)内容映射到uanlianBean(target)中的属性上

```java
	public void bind(PropertyValues pvs) {
		...
	}
```

- 假设PropertyValues中包含"name =小马哥" 的键值对
- 同时Bean对象User中存在name属性,当bind方法执行时
- User中的name属性将被绑定为小马哥

### 

