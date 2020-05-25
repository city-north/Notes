# Feign 的基础原理

```
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FeignClient {
	@AliasFor("name")
	String value() default "";

	@AliasFor("value")
	String name() default "";
	String qualifier() default "";
	String url() default "";
	boolean decode404() default false;
	Class<?>[] configuration() default {};
	Class<?> fallback() default void.class;
	Class<?> fallbackFactory() default void.class;
	String path() default "";
	boolean primary() default true;
}

```

- `@Target(ElementType.TYPE)`元注解,标识 FeignClient 注解的作用目标在接口上
- `name` : 指定 FeignClient 的名称,如果项目上使用了 Ribbon,name 属性会作为微服务的名称,用于服务发现
- `url`: url 一般用于调试,可以手动指定`@feignClient` 调用的地址
- decode404 : 当发生 404 错误时,如果该字段为 true, 会调用 decoder 进行解码,否则抛出 FeignException
- `configuration`: Feign 配置类,可以自定义 Feign 的 Encoder , Decoder , LogLevel , Contract 
- `fallback` :定义容错的处理类,当调用远程接口失败或者超时时,会调用对应皆苦的容错逻辑,fallback 指定的类必须实现@FeignClient 标记的接口
- `fallbackFactory `工厂类, 用于生成 fallback 类示例, 通过这个属性我们可以实现每个接口通用的容错逻辑,减少重复代码
- `path` 定义当前 FeignClient 的统一前缀

`