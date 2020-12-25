# 011-数据绑定-DataBinder中类型转换的实现

## 数据绑定和PropertyEditor的关系

![image-20201224124446547](../../assets/image-20201224124446547.png)

我们可以看出DataBinder是实现了PropertyEditorRegistry 接口的,这个接口涵盖了属性编辑器的注册中心,也就是说实际上我们用到的DataBinder类就是一个PropertyEditor的注册中心

### PropertyEditorRegistry源码

```java
public interface PropertyEditorRegistry {
    //	注册自定义的编辑器
    void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor);

    //注册自定义的编辑器
    void registerCustomEditor(@Nullable Class<?> requiredType, @Nullable String propertyPath, PropertyEditor propertyEditor);

    //根据类型和路径查找属性编辑器
    @Nullable
PropertyEditor findCustomEditor(@Nullable Class<?> requiredType, @Nullable String propertyPath);
}
```

### TypeConverter源码

另外，DataBinder不仅仅是一个PropertyEditorRegistry， 它还是一个TypeConverter, 

TypeConverter 通常情况下会结合PropertyEditorRegistry一起使用

```java
/**
 * Interface that defines type conversion methods. Typically (but not necessarily)
 * implemented in conjunction with the {@link PropertyEditorRegistry} interface.
 */
public interface TypeConverter {
    //能转换就转换
	@Nullable
	<T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType) throws TypeMismatchException;

	@Nullable
	<T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType,
			@Nullable MethodParameter methodParam) throws TypeMismatchException;
	@Nullable
	<T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType, @Nullable Field field)
			throws TypeMismatchException;
	@Nullable
	default <T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType,
			@Nullable TypeDescriptor typeDescriptor) throws TypeMismatchException {

		throw new UnsupportedOperationException("TypeDescriptor resolution not supported");
	}

}

```

## 