# NULL安全性

由于 Java 没有提供让你解析 null 安全的方式,所以 Spring 框架提供了下面的注释让你去声明你的 API 或者字段是否为空:

主要注释集中在`org.springframework.lang`包下

- [`@Nullable`](https://docs.spring.io/spring-framework/docs/5.2.1.RELEASE/javadoc-api/org/springframework/lang/Nullable.html): 指定的参数,返回值或者字段值可以为 `null`
- [`@NonNull`](https://docs.spring.io/spring-framework/docs/5.2.1.RELEASE/javadoc-api/org/springframework/lang/NonNull.html):指定参数,返回值以及字段不能为 null,(如果使用了`@NonNullApi`或者`@NonNullFields`则不需要再添加) 
- [`@NonNullApi`](https://docs.spring.io/spring-framework/docs/5.2.1.RELEASE/javadoc-api/org/springframework/lang/NonNullApi.html): 在包级别声明非null作为参数和返回值的默认语义的注释。
- [`@NonNullFields`](https://docs.spring.io/spring-framework/docs/5.2.1.RELEASE/javadoc-api/org/springframework/lang/NonNullFields.html): 在包级别声明非null作为字段默认语义的注释。

IDE 、编译器会读取这个注释,并提示你进行空指针检查

