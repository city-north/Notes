# 方法校验

method validation

根据Java Bean验证规范，如果方法验证产生非空的约束违反集合，那么`javax.validation`应该抛出`ConstraintViolationException`来包装违规。规范本身没有提供任何抛出此异常的实现，相反，它详细描述了与侦听框架(如Spring)集成后应该遵循的行为。该规范提供了API，帮助手动执行方法和构造函数的验证(查看这个和这个示例)。

方法验证可以通过注册`MethodValidationPostProcessor`集成到Spring应用程序中。所有包含要验证的方法的目标类也应该注册为bean，并且应该使用Spring特定的注释`@ validate`进行注释。

```java
/**
 * 方法校验
 *
 * @author EricChen 2019/11/22 22:56
 */
@Configuration
@ComponentScan("cn.eccto.study.springframework.tutorials.methodValidation")
public class MethodValidationExample {

    @Bean
    public Validator validatorFactory() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }



    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(MethodValidationExample.class);

        ReportTask task = context.getBean(ReportTask.class);
        System.out.println("-- calling ReportTask#createReport() --");
        System.out.println("-- with invalid parameters --");
        try {
            String status = task.createReport("", LocalDateTime.now().minusMinutes(30));
            System.out.println(status);
        } catch (ConstraintViolationException e) {
            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                System.out.println(violation.getPropertyPath() + " " + violation.getMessage());
            }
        }
        System.out.println("-- with valid parameters but invalid return value --");
        try {
            String status = task.createReport("create reports", LocalDateTime.now().plusMinutes(30));
            System.out.println(status);
        } catch (ConstraintViolationException e) {
            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                System.out.println(violation.getPropertyPath() + " " + violation.getMessage());
            }
        }

        UserTask userTask = context.getBean(UserTask.class);
        System.out.println("-- calling UserTask#registerUser() --");
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

    public static class User {
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
}

```

```java
/**
 * description
 *
 * @author EricChen 2019/11/22 22:56
 */
@Validated
@Component
public class ReportTask {
    public @Pattern(regexp = "[0-3]") String createReport(@NotNull @Size(min = 3, max = 20) String name,
                                                          @NotNull @FutureOrPresent LocalDateTime startDate) {
        return "-1";
    }
}
```

```java
@Component
@Validated
public class UserTask {

    public void registerUser(@NotNull @Valid MethodValidationExample.User user){
        System.out.println("registering user: "+ user);
    }
}
```

