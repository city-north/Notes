# Programmatically Setting Profiles

编程时设置 prifule

We can programmatically set active profiles by calling `SpringApplication.setAdditionalProfiles() `.

We can also set activate profiles by using `ConfigurableEnvironment.setActiveProfiles(...)`.

# Example

#### src/main/resources/application.properties

```
spring.main.banner-mode=off
```

## Environment related beans

```java
public interface InventoryService {
    void addStockItem(String itemName, int qty, BigDecimal unitPrice);

    @Service
    @Profile("dev")
    class InMemoryInventoryServiceImpl implements InventoryService {
        @Override
        public void addStockItem(String itemName, int qty, BigDecimal unitPrice) {
            System.out.println("adding item in InMemoryInventoryServiceImpl: " + itemName);
        }
    }

    @Service
    @Profile("prod")
    class ProductionInventoryServiceImpl implements InventoryService {
        @Override
        public void addStockItem(String itemName, int qty, BigDecimal unitPrice) {
            System.out.println("adding item in ProductionInventoryServiceImpl: " + itemName);
        }
    }
}
public interface MsgService {
    void sendMessage(String msg);

    @Service
    @Profile("live")
    class MqMsgService implements MsgService {

        @Override
        public void sendMessage(String msg) {
            System.out.println("sending live message: " + msg);
        }
    }

    @Service
    @Profile("test")
    class TestMsgService implements MsgService {

        @Override
        public void sendMessage(String msg) {
            System.out.println("sending message to test receiver: " + msg);
        }
    }
}
public interface OrderService {
    void placeOrder(String itemName, int qty);

    @Service
    @Profile("remote")
    class RemoteOrderService implements OrderService{
        @Override
        public void placeOrder(String itemName, int qty) {
            System.out.printf("placing order to remote server item:%s, qty: %s%n", itemName, qty);
        }
    }

    @Service
    @Profile("test")
    class TestOrderService implements OrderService{
        @Override
        public void placeOrder(String itemName, int qty) {
            System.out.printf("placing order to test server item:%s, qty: %s%n", itemName, qty);
        }
    }
}
```

## Main class

```java
@SpringBootApplication
public class ExampleMain {


  public static void main(String[] args) {
      System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "prod");
      SpringApplication sa = new SpringApplication(ExampleMain.class);
      sa.setAdditionalProfiles("remote","live");
      sa.run(args);
  }
}
adding item in ProductionInventoryServiceImpl: item1
sending live message: item added
placing order to remote server item:item1, qty: 5
```

Following example shows how to use `StandardEnvironment` to set active profiles:

```java
@SpringBootApplication
public class ExampleMain2 {

  public static void main(String[] args) {
      ConfigurableEnvironment environment = new StandardEnvironment();
      environment.setActiveProfiles("dev");

      SpringApplication sa = new SpringApplication(ExampleMain2.class);
      sa.setEnvironment(environment);
      sa.setAdditionalProfiles("remote","live");
      sa.run(args);
  }
}
adding item in InMemoryInventoryServiceImpl: item1
sending live message: item added
placing order to remote server item:item1, qty: 5
```

