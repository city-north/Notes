# Adding active profiles unconditionally by using spring.profiles.include property

根据条件`spring.profiles.include`引入 profiles

- `spring.profiles.include`属性

# Example

#### src/main/resources/application.properties

```
spring.profiles.include=remote,live
spring.main.banner-mode=off
```

## Profile related services

```
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

```
@SpringBootApplication
public class ExampleMain {

  @Autowired
  private InventoryService inventoryService;
  @Autowired
  private MsgService msgService;
  @Autowired
  private OrderService orderService;

  @PostConstruct
  private void postConstruct(){
      inventoryService.addStockItem("item1", 10, BigDecimal.valueOf(20.5));
      msgService.sendMessage("item added");
      orderService.placeOrder("item1", 5);
  }

  public static void main(String[] args) {
      SpringApplication.run(ExampleMain.class, args);
  }
}
```

## Output

```
D:\example-projects\spring-boot\boot-spring-profiles-include-property>mvn -q -Dspring.profiles.active=dev spring-boot:run
adding item in InMemoryInventoryServiceImpl: item1
sending live message: item added
placing order to remote server item:item1, qty: 5
D:\example-projects\spring-boot\boot-spring-profiles-include-property>mvn -q -Dspring.profiles.active=prod spring-boot:run
adding item in ProductionInventoryServiceImpl: item1
sending live message: item added
placing order to remote server item:item1, qty: 5
```