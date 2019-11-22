# Spring 校验

Spring 官方文档笔记 [01-spring-validator.md](../../03-validation-data-binding-type-conversion/01-spring-validator.md) 

Spring 提供了两种方式去校验 bean

- Spring 的校验机制是实现 Validator 接口

- JSR 349/303 的注解校验机制

## Spring 校验实例

#### 使用 Spring 的校验机制进行校验

```java
/**
 * Spring 校验机制实例,
 * 1. 准备客户端 {@link ClientBean}, 实例 bean {@link Order}
 * 2. 自定义 {@link OrderValidator} 实现 Spring 的接口 {@link Validator}
 * 3. 在客户端 {@link ClientBean} 中写校验方法 {@link ClientBean#validateOrder},通过messageSource 获取配置文件中配置的错误信息
 * @author EricChen 2019/11/22 20:53
 */
public class ValidatorExample {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        Config.class);

        ClientBean bean = context.getBean(ClientBean.class);
        bean.processOrder();
    }

    @Configuration
    public static class Config {

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

    private static class ClientBean {
        @Autowired
        private Order order;

        private void processOrder() {
            if (validateOrder()) {
                System.out.println("processing " + order);
            }
        }

        private boolean validateOrder() {
            DataBinder dataBinder = new DataBinder(order);
            dataBinder.addValidators(new OrderValidator());
            dataBinder.validate();

            if (dataBinder.getBindingResult().hasErrors()) {
                ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
                messageSource.setBasename("messages/messages");
                System.out.println(messageSource.getMessage("order.invalid", null, Locale.US));
                dataBinder.getBindingResult().getAllErrors()
                        .stream()
                        .forEach(e -> System.out.println(messageSource.getMessage(e, Locale.US)));
                return false;
            }
            return true;
        }
    }

    private static class Order {
        private Date date;
        private BigDecimal price;

        public void setDate(Date date) {
            this.date = date;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public Date getDate() {
            return date;
        }

        public BigDecimal getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return "Order{'date='" + date + "', price=" + price + '}';
        }
    }

    /**
     * 自定义 {@link Validator}  ,通过使用 {@link ValidationUtils} 内的方法,
     */
    private static class OrderValidator implements Validator {

        @Override
        public boolean supports(Class<?> clazz) {
            return Order.class == clazz;
        }

        @Override
        public void validate(Object target, Errors errors) {
            ValidationUtils.rejectIfEmpty(errors, "date", "date.empty");
            ValidationUtils.rejectIfEmpty(errors, "price", "price.empty");
            Order order = (Order) target;
            if (order.getPrice() != null && order.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                errors.rejectValue("price", "price.invalid");
            }
        }
    }
}
```

#### 

#### 使用方法`ValidationUtils.invokeValidator`调用一个自定义的 Validator 

```java
/**
 * Spring 校验机制实例,
 * 使用 {@link ValidationUtils#invokeValidator}方法调用一个自定义的 Validator 
 * @author EricChen 2019/11/22 20:53
 */
public class ValidatorExample2 {
    public static void main (String[] args) {

        ResourceBundleMessageSource messageSource =
                new ResourceBundleMessageSource();
        messageSource.setBasename("messages/messages");

        Order order = new Order();
        order.setPrice(BigDecimal.ZERO);
        BeanPropertyBindingResult result = new BeanPropertyBindingResult(order, "order");
        ValidationUtils.invokeValidator(new OrderValidator(), order, result);
        result.getAllErrors().stream().
                forEach(e -> System.out.println(messageSource.getMessage(e, Locale.US)));
    }


    private static class Order {
        private LocalDate date;
        private BigDecimal price;

        public void setDate (LocalDate date) {
            this.date = date;
        }

        public void setPrice (BigDecimal price) {
            this.price = price;
        }

        public LocalDate getDate () {
            return date;
        }

        public BigDecimal getPrice () {
            return price;
        }

        @Override
        public String toString () {
            return "Order{'date='" + date + "', price=" + price + '}';
        }
    }

    /**
     * 自定义家
     */
    private static class OrderValidator implements Validator {

        @Override
        public boolean supports (Class<?> clazz) {
            return Order.class == clazz;
        }

        @Override
        public void validate (Object target, Errors errors) {
            ValidationUtils.rejectIfEmpty(errors, "date", "date.empty");
            ValidationUtils.rejectIfEmpty(errors, "price", "price.empty");
            Order order = (Order) target;
            if (order.getPrice() != null &&
                    order.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                errors.rejectValue("price", "price.invalid");
            }
        }
    }
}
```

#### 基于 JSR-349 注解方式进行校验

```java
/**
 * 基于 JSR-349 注解方式进行校验
 *
 * @author EricChen 2019/11/22 20:34
 */
public class ValidationJSR349Example {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        Config.class);

        ClientBean bean = context.getBean(ClientBean.class);
        bean.processOrder();
    }

    @Configuration
    public static class Config {

        @Bean
        public ClientBean clientBean() {
            return new ClientBean();
        }

        @Bean
        public Order order() {

            Order order = new Order();
            // order.setPrice(BigDecimal.TEN);
            order.setDate(new Date(System.currentTimeMillis() - 60000));
            return order;
        }

        @Bean
        public Validator validatorFactory() {
            return new LocalValidatorFactoryBean();
        }
    }

    private static class ClientBean {
        @Autowired
        private Order order;
        @Autowired
        Validator validator;

        private void processOrder() {
            if (validateOrder()) {
                System.out.println("processing " + order);
            }
        }

        private boolean validateOrder() {
            Locale.setDefault(Locale.US);
            Set<ConstraintViolation<Order>> c = validator.validate(order);
            if (c.size() > 0) {
                System.out.println("Order validation errors:");
                c.stream().map(v -> v.getMessage())
                        .forEach(System.out::println);
                return false;
            }
            return true;
        }
    }

    private static class Order {
        @NotNull(message = "{date.empty}")
        @Future(message = "{date.future}")
        private Date date;

        @NotNull(message = "{price.empty}")
        @DecimalMin(value = "0", inclusive = false, message = "{price.invalid}")
        private BigDecimal price;

        public void setDate(Date date) {
            this.date = date;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public Date getDate() {
            return date;
        }

        public BigDecimal getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return "Order{'date='" + date + "', price=" + price + '}';
        }
    }
}
```

#### 混合方式校验

```java
/**
 * 混合方式校验
 * Spring 校验机制实例,
 * 1. 准备客户端 {@link ValidationMixedExample.ClientBean}, 实例 bean {@link ValidationMixedExample.Order}
 * 2. 自定义 {@link ValidationMixedExample.OrderValidator} 实现 Spring 的接口 {@link Validator}
 * 3. 书写通用的校验方式 {@link GenericValidator} 获取所有 Spring 中注册的校验器实例,并循环找到符合要求的校验器进行校验
 * @author EricChen 2019/11/22 20:53
 */
public class ValidationMixedExample {



    public static void main (String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        ClientBean clientBean = context.getBean(ClientBean.class);
        clientBean.processOrder();
    }


    @Configuration
    public static class Config {

        @Bean
        public ClientBean clientBean () {
            return new ClientBean();
        }

        @Bean
        public Order order () {
            Order order = new Order();
            //  order.setPrice(BigDecimal.TEN);
            //  order.setDate(new Date(System.currentTimeMillis() + 100000));
            order.setCustomerId("111");
            return order;
        }

        @Bean
        public org.springframework.validation.Validator validatorFactory () {
            return new LocalValidatorFactoryBean();
        }

        @Bean
        public OrderValidator orderValidator () {
            return new OrderValidator();
        }

        @Bean
        public GenericValidator genericValidator () {
            return new GenericValidator();
        }
    }


    private static class ClientBean {
        @Autowired
        private Order order;

        @Autowired
        private GenericValidator genericValidator;

        private void processOrder () {
            if (genericValidator.validateObject(order)) {
                System.out.println("processing " + order);
            }
        }

        public Order getOrder () {
            return order;
        }
    }


    private static class Order {
        @NotNull(message = "{date.empty}")
        @Future(message = "{date.future}")
        private Date date;

        @NotNull(message = "{price.empty}")
        @DecimalMin(value = "0", inclusive = false, message = "{price.invalid}")
        private BigDecimal price;

        String customerId;

        public void setDate (Date date) {
            this.date = date;
        }

        public void setPrice (BigDecimal price) {
            this.price = price;
        }

        public Date getDate () {
            return date;
        }

        public BigDecimal getPrice () {
            return price;
        }

        public String getCustomerId () {
            return customerId;
        }

        public void setCustomerId (String customerId) {
            this.customerId = customerId;
        }

        @Override
        public String toString () {
            return "Order{ date=" + date + ", price=" + price +
                    ", customerId='" + customerId + "'}";
        }
    }

    private static class OrderValidator implements org.springframework.validation.Validator {

        @Override
        public boolean supports (Class<?> clazz) {
            return Order.class == clazz;
        }

        @Override
        public void validate (Object target, Errors errors) {
            ValidationUtils.rejectIfEmpty(errors,
                    "customerId", "customerId.empty");

            Order order = (Order) target;
            if (order.getCustomerId() != null) {
                Customer customer = getCustomerById(order.getCustomerId());
                if (customer == null) {
                    errors.reject("customer.id.invalid",
                            new Object[]{order.getCustomerId()},
                            "Customer id is not valid");
                }
            }
        }

        private Customer getCustomerById (String customerId) {
            //just for test returning null..
            // otherwise we have to use a backend data service here
            return null;
        }
    }

    private static class Customer {
    }

    private static class GenericValidator {

        @Autowired
        ApplicationContext context;

        /**
         * This is a framework style generic method which can
         * validate any object given that the corresponding
         * validator is register as bean.
         */
        public boolean validateObject (Object objectToValidate) {

            Map<String, Validator> validatorMap = context.getBeansOfType(Validator.class);
            if (validatorMap == null) {
                return true;
            }

            DataBinder binder = new DataBinder(objectToValidate);

            //in this example two validators are register OrderValidator
            // and LocalValidatorFactoryBean which will do JSR 349 validations.
            for (Validator validator : validatorMap.values()) {
                if (validator.supports(objectToValidate.getClass())) {
                    binder.addValidators(validator);
                }
            }
            binder.validate();
            BindingResult bindingResult = binder.getBindingResult();
            if (bindingResult.hasErrors()) {
                ResourceBundleMessageSource messageSource =
                        new ResourceBundleMessageSource();
                messageSource.setBasename("ValidationMessages");
                System.out.println(messageSource.getMessage("object.invalid",
                        new Object[]{objectToValidate.getClass().
                                getSimpleName()},
                        Locale.US));
                        bindingResult.getAllErrors()
                        .stream()
                        .map(e -> messageSource.getMessage(e,
                                Locale.US))
                        .forEach(System.out::println);
                return false;
            }

            return true;
        }

    }
}
```



