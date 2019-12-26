# Feign 对 @QueryMap 的支持

OpenFeign 的`@QueryMap`注解提供了 POJO 作为 map 使用的功能,但是默认的 OpenFeign 的 QueryMap he Spring 不兼容因为 缺少 value 熟悉感

SpringCloud OpenFeign 提供了一个相同的注解`@SpringQueryMap`,用来将一个 POJO桌和 Map 参数作为查询参数 map

例如:`Params`类里定义了查询参数`param1`和`param2`

```java
// Params.java
public class Params {
    private String param1;
    private String param2;

    // [Getters and setters omitted for brevity]
}
```

The following feign client uses the `Params` class by using the `@SpringQueryMap` annotation:

```java
@FeignClient("demo")
public interface DemoTemplate {

    @GetMapping(path = "/demo")
    String demoEndpoint(@SpringQueryMap Params params);
}
```

If you need more control over the generated query parameter map, you can implement a custom `QueryMapEncoder` bean.

