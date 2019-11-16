# 隐式构造器注入(implicit constructor injection)

> 版权声明
>
> 此篇文章参考于[logicbig](https://www.logicbig.com/),引用其文字/代码/图片仅用于学习笔记,不用于商业用途

Spring 4.3 以后,如果在构造方法中指定额 `bean`,没有必要去添加`@Autowired` 注解

#### 客户端

可以看到没有使用`@Autowired`注解

```java
@Component
public class OrderServiceClient {

    private OrderService orderService;

    //@Autowired is no longer required in Spring 4.3 and later.
    public OrderServiceClient (OrderService orderService) {
        this.orderService = orderService;
    }

    public void showPendingOrderDetails () {
        System.out.println(orderService.getOrderDetails("100"));
    }
}
```

#### 实现类

```java
@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public String getOrderDetails(String orderId) {
        return "Order details from impl 1, for order id=" + orderId;
    }
}
```

#### 接口

```java
public interface OrderService {
    String getOrderDetails(String orderId);
}
```

#### 测试类

```java
public class ScopeExample {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Config.class);

        UserRegistrationBean registrationBean = context.getBean(UserRegistrationBean.class);

        while (true) {
            System.out.printf("[registration bean instance: %s]\n", System.identityHashCode(registrationBean));
            System.out.println("Enter new user. Enter exit to terminate");
            registerUser(registrationBean);
            registrationBean = context.getBean(UserRegistrationBean.class);
        }
    }

    private static void registerUser(UserRegistrationBean registrationBean) {

        UserInfo userInfo = new UserInfo();
        registrationBean.setUserInfo(userInfo);

        Map<String, String> errors = null;

        while (errors == null || errors.size() > 0) {
            if (errors != null) {
                System.out.println("Errors : " + errors.values() + "\n");
                System.out.println("Please enter exit to terminate");
            }

            if (errors == null || errors.containsKey(UserRegistrationBean.KEY_EMAIL)) {
                userInfo.setEmail(getUserInput("Enter Email"));
            }
            if (errors == null || errors.containsKey(UserRegistrationBean.KEY_PASSWORD)) {
                userInfo.setPassword(getUserInput("Enter Password"));
            }

            errors = registrationBean.validate();
        }
        registrationBean.register();
    }

    public static String getUserInput(String instruction) {
        System.out.print(instruction + ">");
        String s = scanner.nextLine();
        if ("exit".equals(s)) {
            System.exit(0);
        }
        return s;

    }
}

```

