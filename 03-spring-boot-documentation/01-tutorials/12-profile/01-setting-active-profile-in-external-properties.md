# Setting Active Profile in external properties

激活 profile

- 我们可以使用`spring.profiles.active`激活环境属性
- 命令行`--spring.profiles.active`

## Example

We are going to use spring.profiles.active, in application.properties, to specify the active profile.

```
@SpringBootConfiguration
public class ExampleMain {
  @Bean
  @Profile("in-memory")
  InventoryService inMemoryInventoryService() {
      return new InMemoryInventoryServiceImpl();
  }

  @Bean
  @Profile("production")
  InventoryService productionInventoryService() {
      return new InventoryServiceImpl();
  }

  public static void main(String[] args) throws InterruptedException {
      ApplicationContext context = SpringApplication.run(ExampleMain.class, args);
      InventoryService service = context.getBean(InventoryService.class);
      service.addStockItem("item1", 10, BigDecimal.valueOf(20.5));
  }
}
public class InMemoryInventoryServiceImpl implements InventoryService {
  @Override
  public void addStockItem(String itemName, int qty, BigDecimal unitPrice) {
      System.out.println("adding item in InMemoryInventoryServiceImpl: " + itemName);
  }
}
src/main/resources/application.properties
spring.profiles.active=in-memory
Output

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.4.RELEASE)

2017-07-12 11:04:20.199  INFO 15376 --- [mpleMain.main()] com.logicbig.example.ExampleMain         : Starting ExampleMain on JoeMsi with PID 15376 (D:\LogicBig\example-projects\spring-boot\boot-active-profile-example\target\classes started by Joe in D:\LogicBig\example-projects\spring-boot\boot-active-profile-example)
2017-07-12 11:04:20.201  INFO 15376 --- [mpleMain.main()] com.logicbig.example.ExampleMain         : The following profiles are active: in-memory
2017-07-12 11:04:20.375  INFO 15376 --- [mpleMain.main()] com.logicbig.example.ExampleMain         : Started ExampleMain in 0.367 seconds (JVM running for 4.038)
adding item in InMemoryInventoryServiceImpl: item1xxxxxxxxxx spring.profiles.active=in-memory
```