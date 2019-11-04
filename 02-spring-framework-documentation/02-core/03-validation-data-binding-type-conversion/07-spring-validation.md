## Spring  校验机制(Spring Validation)

Spring 核心模块提供两种方法校验 bean 的合法性:

- 使用 Spring 校验机制,通过实现`org.springframework.validation.Validator`接口

- 使用JSR 349/303 注解的校验  [Java EE Bean validation tutorial](https://www.logicbig.com/tutorials/java-ee-tutorial/bean-validation.html)

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

