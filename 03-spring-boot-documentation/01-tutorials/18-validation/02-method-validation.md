# 方法校验

方法校验的特性是有 [Bean Validation Specification](https://www.logicbig.com/tutorials/java-ee-tutorial/bean-validation.html)提供的,classpath 下如果有实现类,例如 hibernate-validator).Spring 就会自动添加这些类启动

In Spring Boot we even do not have to register `MethodValidationPostProcessor` or `LocalValidatorFactoryBean` beans (as compared to core Spring [validation setup](https://www.logicbig.com/tutorials/spring-framework/spring-core/method-validation.html)).

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter</artifactId>
</dependency>
<dependency>
   <groupId>org.hibernate.validator</groupId>
   <artifactId>hibernate-validator</artifactId>
</dependency>
```



```java
@SpringBootApplication
public class ExampleMain {

    public static void main(String[] args) {
      ApplicationContext ac =  SpringApplication.run(ExampleMain.class);
        UserTask userTask = ac.getBean(UserTask.class);
        User user = new User();
        user.setEmail("tony-example.com");
        try {
            userTask.registerUser(user);
        } catch (ConstraintViolationException e) {
            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                System.out.println(violation.getPropertyPath() + " " + violation.getMessage());
            }
        }
    }
}
```

```java

public class User {
    @NotEmpty
    private String name;
    @Email
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
```

```java
@Component
@Validated
public class UserTask {

    public void registerUser(@NotNull @Valid User user) {
        System.out.println("registering user: " + user);
    }
}
```

