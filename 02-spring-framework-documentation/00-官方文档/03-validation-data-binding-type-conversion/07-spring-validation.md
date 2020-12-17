## Spring  校验机制(Spring Validation)

Spring 核心模块提供两种方法校验 bean 的合法性:

- 使用 Spring 校验机制,通过实现`org.springframework.validation.Validator`接口
- 使用JSR 349/303 注解的校验  [Java EE Bean validation tutorial](https://www.logicbig.com/tutorials/java-ee-tutorial/bean-validation.html)



## JSR-303 bean 校验 API

Spring 全面支持 Bean Validation API, 它包含了对 JSR-303和 JSR-349 Bean 校验的全面支持,你可以在你的 sping 应用中随时注入`javax.validation.ValidatorFactory`和`javax.validation.Validator`.

下面实例显示了一个 JSR-303 注解标注的校验方式

```java
public class PersonForm {

    @NotNull
    @Size(max=64)
    private String name;

    @Min(0)
    private int age;
}
```

当JSR-303验证器验证该类的实例时，将强制执行这些约束。

你可以将检验器集成到你的项目中

你可以使用`LocalValidatorFactoryBean`配置成 Spring 默认的 校验器,如下:

```xml
<bean id="validator"
    class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean"/>
```

例子中的代码,使用了默认的引导机制,听了 JSR-303 或者 JSR-349 的提供者,例如`Hibernate Validator`,我们可以在代码中注入校验器:

```java
import javax.validation.Validator;

@Service
public class MyService {

    @Autowired
    private Validator validator;
}
```

如果你需要 Spring 校验 API ,那么你可以注入另一个`org.springframework.validation.Validator`

```java
import org.springframework.validation.Validator;

@Service
public class MyService {

    @Autowired
    private Validator validator;
}
```

## 配置自定义约束(Configurating Custom Constraints )

自定义约束的方式:

- 自定义`Validator`实现`javax.validation.ConstraintValidator`

- 使用`@Constraint`,其中数据`validatedBy`属性设置为自定义 `Validator`

```java
import javax.validation.ConstraintValidator;

public class MyConstraintValidator implements ConstraintValidator<MyConstraint,String> {

    @Autowired;
    private Foo aDependency;

    // ...
}
```



```java
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=MyConstraintValidator.class)
public @interface MyConstraint {
}
```



## 例子

```java
/**
 * Spring 自定义校验器实例
 * 1. 新建待校验的实体类{@link Order}
 * 2. 新建需要使用到校验的客户端 bean {@link ClientBean}
 * 3. 配置校验器 {@link OrderValidator}
 * 4. 配置配置类 {@link Config}
 *
 * @author EricChen 2019/11/04 19:29
 */
public class ValidatorExample {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        ClientBean bean = context.getBean(ClientBean.class);
        bean.processOrder();
    }

}

```

#### 1.新建待校验的实体类Order

```java
public class Order {
    private Date date;
    private BigDecimal price;
}
```

#### 2. 新建需要使用到校验的客户端 bean {@link ClientBean}

```java
public class ClientBean {
    @Autowired
    private Order order;

    public void processOrder() {
        if (validateOrder()) {
            System.out.println("processing " + order);
        }
    }

    private boolean validateOrder() {
        DataBinder dataBinder = new DataBinder(order);
        dataBinder.addValidators(new OrderValidator());
        dataBinder.validate();

        if (dataBinder.getBindingResult().hasErrors()) {
            ResourceBundleMessageSource messageSource =
                    new ResourceBundleMessageSource();
            //使用 messageSource
            messageSource.setBasename("messages/messages");
            messageSource.setDefaultEncoding("UTF-8");
            System.out.println(messageSource.getMessage("order.invalid", null, Locale.SIMPLIFIED_CHINESE));
            dataBinder.getBindingResult().getAllErrors().stream().
                    forEach(e -> System.out.println(messageSource
                            .getMessage(e, Locale.SIMPLIFIED_CHINESE)));
            return false;
        }
        return true;
    }
}
```

3. 配置校验器 {@link OrderValidator}

```java
public class OrderValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Order.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "date", "date.empty");
        ValidationUtils.rejectIfEmpty(errors, "price", "price.empty");
        Order order = (Order) target;
        if (order.getPrice() != null &&
                order.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.rejectValue("price", "price.invalid");
        }
    }
}
```

4. 配置配置类 {@link Config}

```java
@Configuration
public class Config {

    @Bean
    public ClientBean clientBean() {
        return new ClientBean();
    }

    @Bean
    public Order order() {
        //in real scenario this order should be coming from user interface
        Order order = new Order();
        order.setPrice(BigDecimal.ZERO);
        return order;
    }
}
```

## JSR 349/303 基于注解的校验

```java
/**
 * JSR 标准注解的数据的数据校验
 * 1. 新建待校验的实体类{@link Order}
 * 2. 新建需要使用到校验的客户端 bean {@link cn.eccto.study.springframework.validation.spr.ClientBean}
 * 3. 配置配置类 {@link cn.eccto.study.springframework.validation.spr.Config}
 *
 * @author EricChen 2019/11/04 20:09
 */
public class JSRValidatorExample {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        ClientBean bean = context.getBean(ClientBean.class);
        bean.processOrder();
    }

}

```

#### 1. 新建待校验的实体类{@link Order}

```
public class Order {
    @NotNull(message = "date.empty")
    @Future(message = "date.future")
    private Date date;

    @NotNull(message = "price.empty")
    @DecimalMin(value = "0", inclusive = false, message = "price.invalid")
    private BigDecimal price;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
```

#### 2. 新建需要使用到校验的客户端 bean {@link cn.eccto.study.springframework.validation.spr.ClientBean}

```java
public class ClientBean {
    @Autowired
    private Order order;

    public void processOrder() {
        if (validateOrder()) {
            System.out.println("processing " + order);
        }
    }

    private boolean validateOrder() {
        DataBinder dataBinder = new DataBinder(order);
        dataBinder.addValidators(new OrderValidator());
        dataBinder.validate();

        if (dataBinder.getBindingResult().hasErrors()) {
            ResourceBundleMessageSource messageSource =
                    new ResourceBundleMessageSource();
            //使用 messageSource
            messageSource.setBasename("messages/messages");
            messageSource.setDefaultEncoding("UTF-8");
            System.out.println(messageSource.getMessage("order.invalid", null, Locale.SIMPLIFIED_CHINESE));
            dataBinder.getBindingResult().getAllErrors().stream().
                    forEach(e -> System.out.println(messageSource
                            .getMessage(e, Locale.SIMPLIFIED_CHINESE)));
            return false;
        }
        return true;
    }
}

```

#### 3. 配置配置类 {@link cn.eccto.study.springframework.validation.spr.Config}

```java
public class Order {
    @NotNull(message = "date.empty")
    @Future(message = "date.future")
    private Date date;

    @NotNull(message = "price.empty")
    @DecimalMin(value = "0", inclusive = false, message = "price.invalid")
    private BigDecimal price;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
```

