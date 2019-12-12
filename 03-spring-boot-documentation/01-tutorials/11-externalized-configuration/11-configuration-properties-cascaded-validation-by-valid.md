# `@ConfigurationProperties `Cascaded Validation by using `@Valid`

在上个例子中,我们看到如何使用 JSR303/349 校验注解来校验` @ConfigurationProperties`,本例我们

- 如何使用`@Valid`注解校验嵌套(级联)的配置属性

# Example

```java
/**
 * 如何使用`@Valid`注解校验嵌套(级联)的配置属性
 *
 * @author EricChen 2019/12/12 10:47
 */
@SpringBootApplication
public class PropertyValidateExample {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(PropertyValidateExample.class)
                .profiles("property-validate")
                .web(WebApplicationType.NONE)
                .run();
        MyAppProperties bean = context.getBean(MyAppProperties.class);
        System.out.println(bean);
    }

}
```

```java
@Data
@Component
@ConfigurationProperties("pv")
@Validated
public class MyAppProperties {

    @NotNull
    @Valid
    private MainScreenProperties mainScreenProperties;

    @NotNull
    @Valid
    private PhoneNumber adminContactNumber;
}
```

```java
@Data
public class PhoneNumber {
    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}")
    private String value;
    @Pattern(regexp = "(?i)cell|house|work")
    private String type;
}
```

```java
@Data
public class MainScreenProperties {
    @Min(1)
    private int refreshRate;
    @Min(50)
    @Max(1000)
    private int width;
    @Min(50)
    @Max(600)
    private int height;
}
```

输出

```
***************************
APPLICATION FAILED TO START
***************************

Description:

Binding to target org.springframework.boot.context.properties.bind.BindException: Failed to bind properties under 'pv' to cn.eccto.study.sb.propertiesvalidate.MyAppProperties failed:

    Property: pv.adminContactNumber.value
    Value: null
    Reason: must match "\d{3}-\d{3}-\d{4}"

    Property: pv.adminContactNumber.type
    Value: null
    Reason: must match "(?i)cell|house|work"

    Property: pv.mainScreenProperties.width
    Value: null
    Reason: must be greater than or equal to 50
```