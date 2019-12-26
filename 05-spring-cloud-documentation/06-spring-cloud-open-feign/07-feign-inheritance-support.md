# [Feign Inheritance Support](https://cloud.spring.io/spring-cloud-static/spring-cloud-openfeign/2.2.0.RELEASE/reference/html/#spring-cloud-feign-inheritance)

Feign通过单继承接口支持样板api。这允许将通用操作分组到方便的基本接口中。

#### UserService.java

```java
public interface UserService {

    @RequestMapping(method = RequestMethod.GET, value ="/users/{id}")
    User getUser(@PathVariable("id") long id);
}
```

#### UserResource.java

```java
@RestController
public class UserResource implements UserService {

}
```

UserClient.java

```java
package project.user;

@FeignClient("users")
public interface UserClient extends UserService {

}
```

> 通常不建议在服务器和客户机之间共享接口。它引入了紧密耦合，并且在当前的形式下也不能与Spring MVC一起工作(方法参数映射不是继承的)。