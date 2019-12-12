# Type safe binding of nested objects with @ConfigurationProperties 

使用 `@ConfigurationProperties `注解绑定内部类对象

## 代码实例

```java
/**
 * 读取配置文件 application-type-safe.properties 和 application-type-safe.yml
 *
 * @author EricChen 2019/12/11 22:07
 */
@Data
@Component
@ConfigurationProperties("nest")
public class MyAppNestProperties {
    private List<String> adminEmails;
    boolean sendEmailOnErrors;
    boolean exitOnErrors;
    private int refreshRate;
    private OrderScreenProperties orderScreenProperties;
    private CustomerScreenProperties customerScreenProperties;
    private MainScreenProperties mainScreenProperties;
}
```

```java
@Data
public class OrderScreenProperties {
    private String title;
    private String itemLabel;
    private String qtyLabel;
}
```

```java
@Data
public class CustomerScreenProperties {
    private String title;
    private String nameLabel;
    private String phoneLabel;
}
```

```java
@Data
public class MainScreenProperties {
    private int refreshRate;
    private int width;
    private int height;
}
```

```java
@SpringBootApplication
public class TypeSafePropertyBindingExample {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(TypeSafePropertyBindingExample.class)
                .profiles("type-safe")
                .run(args);
        MyAppProperties appProperties = context.getBean(MyAppProperties.class);
        System.out.println(nestProperties);
    }

}
```

配置文件

#### src/main/resources/application.properties

```properties
nest.main-screen-properties.refresh-rate=15
nest.main-screen-properties.width=600
nest.main-screen-properties.height=400
```

#### src/main/resources/application.yml

```yaml
nest:
  send-email-on-errors: true
  exit-on-errors: true
  order-screen-properties:
    title:      Order Placement
    item-label: Item
    qty-label:  Qunatity
  customer-screen-properties:
    title:       Customer Profile
    name-label:  Customer Name
    phone-label: Contact Number
```

#### 输出

```
MyAppNestProperties(adminEmails=null, sendEmailOnErrors=true, exitOnErrors=true, refreshRate=0, orderScreenProperties=OrderScreenProperties(title=Order Placement, itemLabel=Item, qtyLabel=Qunatity), customerScreenProperties=CustomerScreenProperties(title=Customer Profile, nameLabel=Customer Name, phoneLabel=Contact Number), mainScreenProperties=MainScreenProperties(refreshRate=15, width=600, height=400))
```

