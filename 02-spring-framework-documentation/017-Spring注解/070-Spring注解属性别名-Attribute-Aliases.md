# 070-Spring注解属性别名-Attribute-Aliases

[toc]

## 什么Spring中的属性别名

一个属性别名的意思是，注解A上的属性传递到注解B上的属性，标注在注解A上的属性的效果和标注在B上是一致的。

- 显式别名

  ​	在一个注解中，使用@AliaseFor互相标注的注解是显式别名

- 隐式别名

  - 如果一个注解上有两个或者更多属性被声明成显性，并覆盖掉相同的元信息上面的@AliasFor（层次中的覆盖）

- 传递性的显式别名

  - 给定两个或者

## 显式别名实例

在一个注解中，使用@AliaseFor互相标注的注解是显式别名

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Repeatable(ComponentScans.class)
public @interface ComponentScan {

	@AliasFor("basePackages")
	String[] value() default {};


	@AliasFor("value")
	String[] basePackages() default {};
}
```

value 和 basePackages 本质上是一样的

## 隐式别名实例

如果一个注解上有两个或者更多属性被声明成显性，并覆盖掉相同的元信息上面的@AliasFor（层次中的覆盖）

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = {
		@Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {


	@AliasFor(annotation = ComponentScan.class, attribute = "basePackages")
	String[] scanBasePackages() default {};

```

scanBasePackages 是 ComponentScan 的basePackages 属性的别名