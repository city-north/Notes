## 使用@Primary 注解

> 版权声明
>
> 此篇文章参考于[logicbig](https://www.logicbig.com/),引用其文字/代码/图片仅用于学习笔记,不用于商业用途

我们可以从之前的笔记 [07-inject-bean-by-name-and-using-qualifier.md](07-inject-bean-by-name-and-using-qualifier.md) ,中了解到,Spring在一个类型有多个实例的情况下,通常使用`@Qualifier`去指定一个 bean,以解决模糊匹配问题,有两个替代方案:

- 如果主动装配类型是` Autowire.BY_TYPE`,我们就不能使用`@Qualifier`因为我们不知道用户定义的注入点是使用`@Autowired`和`@Inject`
- 如果我们选做 bean 的选择(例如解决模糊性),在配置的时候解决而不是在 bean 的开发阶段

解决办法就是使用`@Primary`注解,相当于指定了一个优先注入

> @Primary indicates that a particular bean should be given preference when multiple beans are candidates to be autowired to a single-valued dependency. If exactly one 'primary' bean exists among the candidates, it will be the autowired value.

## 代码实例

```java
@Configuration
public class AppConfig {

    @Bean(autowire = Autowire.BY_TYPE)
    public OrderService orderService() {
        return new OrderService();
    }


    @Bean
    public Dao daoA() {
        return new DaoA();
    }

    @Primary //使用注解后将不报错
    @Bean
    public Dao daoB() {
        return new DaoB();
    }

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        OrderService orderService = context.getBean(OrderService.class);
        orderService.placeOrder("122");

    }
}
```

#### OrderService

```java
public class OrderService {

    private Dao dao;

    public void placeOrder(String orderId) {
        System.out.println("placing order " + orderId);
        dao.saveOrder(orderId);
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }
}
```

