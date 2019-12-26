# [HATEOAS support](https://cloud.spring.io/spring-cloud-static/spring-cloud-openfeign/2.2.0.RELEASE/reference/html/#hateoas-support)

Spring provides some APIs to create REST representations that follow the [HATEOAS](https://en.wikipedia.org/wiki/HATEOAS) principle, [Spring Hateoas](https://spring.io/projects/spring-hateoas) and [Spring Data REST](https://spring.io/projects/spring-data-rest).

If your project use the `org.springframework.boot:spring-boot-starter-hateoas` starter or the `org.springframework.boot:spring-boot-starter-data-rest` starter, Feign HATEOAS support is enabled by default.

When HATEOAS support is enabled, Feign clients are allowed to serialize and deserialize HATEOAS representation models: [EntityModel](https://docs.spring.io/spring-hateoas/docs/1.0.0.M1/apidocs/org/springframework/hateoas/EntityModel.html), [CollectionModel](https://docs.spring.io/spring-hateoas/docs/1.0.0.M1/apidocs/org/springframework/hateoas/CollectionModel.html) and [PagedModel](https://docs.spring.io/spring-hateoas/docs/1.0.0.M1/apidocs/org/springframework/hateoas/PagedModel.html).

```java
@FeignClient("demo")
public interface DemoTemplate {

    @GetMapping(path = "/stores")
    CollectionModel<Store> getStores();
}
```

