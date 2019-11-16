# 资源注入(Resource Injection)

> 版权声明
>
> 此篇文章参考于[logicbig](https://www.logicbig.com/),引用其文字/代码/图片仅用于学习笔记,不用于商业用途

Spring 提供了一个标准的资源访问机制,接口 [ResourceLoader](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/core/io/ResourceLoader.html).

- ResourceLoader 是ApplicationContext 的一个超接口
- 我们可以通过 ApplicationContext 实例获取资源
- 我们可以在任何 bean 中注入`ResourceLoader`

```java
@Component
 public class MyBean{
    @Autowired
    private ResourceLoader resourceLoader;
      ......
 }
```

关于 ResourceLoader 的详细介绍我们可以参考笔记  [03-the-resource-loader.md](../../02-resources/03-the-resource-loader.md) 

## 如何加载 classpath/file 文件

This example shows how to read classpath resources obtained via resource location "classpath:someResource.txt" (we can also use file system resource i.e. "file:d:\\someResource.txt" same way) :

这个例子展示了如何去读取 classpath 下的资源,通过位置`classpath:someResource.txt`,我们也可以使用文件系统资源`file:d:\\someResource.txt`

```java
    ....
 Resource resource = resourceLoader.getResource("classpath:someResource.txt");
 File file = resource.getFile();
 String s = new String(Files.readAllBytes(file.toPath()));
 System.out.println(s);
    ....
```

## 如何加载url 资源

```java
    ....
 Resource resource = resourceLoader.getResource("url:http://www.example.com/");
 StringBuilder stringBuilder = new StringBuilder();
   try (BufferedReader reader = new BufferedReader(
                                new InputStreamReader(resource.getInputStream()))) {
                reader.lines().forEach(stringBuilder::append);
            }
   System.out.println(stringBuilder.toString());
    ....
        
```

## 通过`@Value`注解来注入资源

![img](assets/resource-injection.png)

Resource instance can be injected directly with @Value annotation

Resource 实例 可以直接使用`@Value`注解注入

```java
 public class MyBean {
    @Value("classpath:myResource.txt")
    private Resource myResource;
    ...
}
```



推荐使用这种方式注入资源

- 避免在业务逻辑里使用 SpringAPI
- 我们可以直接获取 Resource 的资源

---

我们为什么要避免在业务逻辑中使用 SpringAPI 呢,毕竟我们已经在代码中使用了 Spring 的注解

- 注解是元数据,即使我们在应用逻辑中使用了注解,我们依然可以在 Spring 容器外使用这个类
- 如果我们在应用逻辑中使用了 SpringAPI,那么相关对象就不能再 SpringContainer 外使用

---

## Resource 的实现类

参考笔记

- [02-built-in-resource-impl.md](../../02-resources/02-built-in-resource-impl.md) 