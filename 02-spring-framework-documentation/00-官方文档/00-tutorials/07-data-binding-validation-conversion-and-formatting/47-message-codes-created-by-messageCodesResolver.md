# MessageCodesRsolver 创建的消息 Code和他们对错误信息的决议

#### Message codes created by MessageCodesRsolver and their resolution to error message

在 Spring 中我们常常调用一些方法如`Errors#register()`或者`ValidationUtils`里的方法

- 潜在的`Errors`的实现类会注册一些错误代码,还有一些附加的 code

`MessageCodeResolver`接口就是负责将这些消息 Code 进行建造,默认的实现类是`DefaultMessageCodeResolver`

在下面的实例中,我们会理解这些附加的 code 是如何创建的并映射到像一个的错误信息的

# 代码实例



```java
/**
 * 本例展示了如何使用通过简单的自定义{@link Validator} 以及使用{@link ValidationUtils} 调用自定义校验器的方式
 *
 *
 * @author EricChen 2019/11/22 21:19
 */
@Configuration
public class ErrorCodeExample {

    @Bean
    OrderValidator orderValidator() {
        return new OrderValidator();
    }


    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ErrorCodeExample.class);
        Order order = new Order();
        Validator bean = context.getBean(Validator.class);
        BeanPropertyBindingResult result = new BeanPropertyBindingResult(order, Order.class.getName());
        ValidationUtils.invokeValidator(bean, order, result);
        result.getAllErrors().stream().forEach(
                objectError ->
                        System.out.println(Arrays.toString(objectError.getCodes()))
        );
    }


    /**
     * 测试实体类
     */
    private static class Order {
        private LocalDate orderDate;
        private BigDecimal orderPrice;

        public LocalDate getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(LocalDate orderDate) {
            this.orderDate = orderDate;
        }

        public BigDecimal getOrderPrice() {
            return orderPrice;
        }

        public void setOrderPrice(BigDecimal orderPrice) {
            this.orderPrice = orderPrice;
        }
    }

    class OrderValidator implements Validator {

        @Override
        public boolean supports(Class<?> clazz) {
            return clazz.equals(Order.class);
        }

        @Override
        public void validate(Object target, Errors errors) {
            ValidationUtils.rejectIfEmpty(errors, "orderDate", "error.date.empty", "Date cannot be empty");
            ValidationUtils.rejectIfEmpty(errors, "orderPrice", "error.price.empty", "Price cannot be empty");
            Order order = (Order) target;
            if (order.getOrderPrice() != null && order.getOrderPrice().compareTo(BigDecimal.ZERO) <= 0) {
                errors.rejectValue("orderPrice", "price.invalid", "Price must be greater than 0");
            }
        }
    }

}

```

输出:

```java
[error.date.empty.cn.eccto.study.springframework.tutorials.messageResolver.ErrorCodeExample$Order.orderDate, error.date.empty.orderDate, error.date.empty.java.time.LocalDate, error.date.empty]
[error.price.empty.cn.eccto.study.springframework.tutorials.messageResolver.ErrorCodeExample$Order.orderPrice, error.price.empty.orderPrice, error.price.empty.java.math.BigDecimal, error.price.empty]
```

- 可以看出除了常规的我们在代码中编码的错误 code 之外,还有一些code 跟上了类的全路径名

下面介绍我们如何将错误 code 映射成错误消息

## 自定义 DefaultMessageCodesResolver

默认情况下,Spring`DefaultMessageCodesResolver` 会使用`Format.PREFIX_ERROR_CODE`(将用户提供的代码[上例中的 `orderDate`],在下面的代码中,我们将改为`Format.POSTFIX_ERROR_CODE`

```java
    /**
     * 使用 {@link DefaultMessageCodesResolver.Format#POSTFIX_ERROR_CODE} 模式拼接全类名以及客户端自定义的 CODE
     */
    public static void runWithPostfixFormat(AnnotationConfigApplicationContext context) {
        Order order = new Order();
        order.setOrderPrice(BigDecimal.ZERO);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(order, Order.class.getName());
        DefaultMessageCodesResolver messageCodesResolver = (DefaultMessageCodesResolver) bindingResult.getMessageCodesResolver();
        messageCodesResolver.setMessageCodeFormatter(DefaultMessageCodesResolver.Format.POSTFIX_ERROR_CODE);
        OrderValidator validator = context.getBean(OrderValidator.class);
        ValidationUtils.invokeValidator(validator, order, bindingResult);
        bindingResult.getAllErrors().forEach(e -> System.out.println(Arrays.toString(e.getCodes())));
    }

```

输出

```
[cn.eccto.study.springframework.tutorials.messageResolver.ErrorCodeExample$Order.orderDate.error.date.empty, orderDate.error.date.empty, java.time.LocalDate.error.date.empty, error.date.empty]
[cn.eccto.study.springframework.tutorials.messageResolver.ErrorCodeExample$Order.orderPrice.price.invalid, orderPrice.price.invalid, java.math.BigDecimal.price.invalid, price.invalid]
```

## 使用 context 获取 errorcode 对应的消息

```java
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/messages");
        return messageSource;
    }

/**
     * 使用 上线文对象获取Messaage ,因为 {@link AnnotationConfigApplicationContext} 实现了 {@link MessageSource}, 需要注册 MessageSource 实例即可完成 code 与 message的转换
     */
    public static void runMessageCodesResolver(AnnotationConfigApplicationContext context) {
        Order order = new Order();
        order.setOrderPrice(BigDecimal.ZERO);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(order, Order.class.getName());
        DefaultMessageCodesResolver messageCodesResolver = (DefaultMessageCodesResolver) bindingResult.getMessageCodesResolver();
        messageCodesResolver.setMessageCodeFormatter(DefaultMessageCodesResolver.Format.POSTFIX_ERROR_CODE);
        OrderValidator validator = context.getBean(OrderValidator.class);
        ValidationUtils.invokeValidator(validator, order, bindingResult);
        bindingResult.getAllErrors().forEach(e -> System.out.println(context.getMessage(e, Locale.SIMPLIFIED_CHINESE)));
    }

```

输出

```java
Date cannot be empty
Order Price must be zero or less.
```

