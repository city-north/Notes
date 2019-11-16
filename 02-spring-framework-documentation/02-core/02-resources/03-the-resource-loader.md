# ResourceLoader

相关实战

-  [40-resource-injection.md](../00-tutorials/05-resource-handling/40-resource-injection.md) 

这个接口的实现类用来创建一个 `Resource`

```java
public interface ResourceLoader {

    Resource getResource(String location);

}
```

所有的`ApplicationContext` 的实现类都实现了这个接口,

- 如果没有指定前缀会自动将返回适合于该特定应用程序上下文中的资源类型

例如，假设下面的代码片段是针对`ClassPathXmlApplicationContext`实例执行的:

```java
Resource template = ctx.getResource("some/resource/path/myTemplate.txt");
```

那么就会返回一个`ClassPathResource`

如果是一个`FileSystemXmlApplicationContext`的`ctx`那么就会返回一个`FileSystemResource`

如果是一个`WebApplicationContext`的`ctx`那么就会返回一个`ServletContextResource`

- 指定前缀来获取相应的 Resource 实现类

`Resource template = ctx.getResource("classpath:some/resource/path/myTemplate.txt");`

上面获取的是`ClassPathResource`

同样的

- 指定前缀来获取相应的 Resource 实现类

返回`UrlResource`的用法

`Resource template = ctx.getResource("file:///some/resource/path/myTemplate.txt");`

- 指定前缀来获取相应的 Resource 实现类

`Resource template = ctx.getResource("https://myhost.com/resource/path/myTemplate.txt");`

返回`UrlResource`的用法

下面是获取的策略

| Prefix     | Example                          | Explanation                                                  |
| :--------- | :------------------------------- | :----------------------------------------------------------- |
| classpath: | `classpath:com/myapp/config.xml` | Loaded from the classpath.                                   |
| file:      | `file:///data/config.xml`        | Loaded as a `URL` from the filesystem. See also [`FileSystemResource`Caveats](https://docs.spring.io/spring/docs/5.1.7.RELEASE/spring-framework-reference/core.html#resources-filesystemresource-caveats). |
| http:      | `https://myserver/logo.png`      | Loaded as a `URL`.                                           |
| (none)     | `/data/config.xml`               | Depends on the underlying `ApplicationContext`.              |



## 