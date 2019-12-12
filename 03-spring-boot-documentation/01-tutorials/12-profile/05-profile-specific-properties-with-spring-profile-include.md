# Spring Boot - Profile Specific Properties with spring.profiles.include

使用`spring.profiles.include`以内指定的 Profile 属性

- `spring.profile.include`总是加载，不管选择什么active Profile文件。
- `spring.profile.include`的属性会覆盖默认值
- 激活 prifile 文件 会覆盖`spring.profile.include`

# Example

#### src/main/resources/application.properties

```
app.window.width=500
app.window.height=400
app.refresh.rate=30
spring.profiles.include=throttling,db
spring.main.banner-mode=off
```

#### src/main/resources/application-dev.properties

```
app.window.height=300
db.connect.url=jdbc:mysql://localhost:3306/my_schema
```

#### src/main/resources/application-prod.properties

```
app.window.width=600
app.window.height=700
```

#### src/main/resources/application-throttling.properties

```
app.refresh.rate=60
```

#### src/main/resources/application-db.properties

```
db.connect.url=jdbc:mysql://712.22.4333.121/app-schema
db.pool.size=10
```

## Example client

```
@Component
public class ClientBean {
  @Value("${app.window.width}")
  private int width;
  @Value("${app.window.height}")
  private int height;
  @Value("${app.refresh.rate}")
  private int refreshRate;
  @Value("${db.connect.url}")
  private String dbConnectionUrl;
  @Value("${db.pool.size}")
  private int dbConnectionPoolSize;

  @PostConstruct
  private void postConstruct() {
      System.out.printf("width= %s%n", width);
      System.out.printf("height= %s%n", height);
      System.out.printf("refresh rate= %s%n", refreshRate);
      System.out.printf("db connect url= %s%n", dbConnectionUrl);
      System.out.printf("db connection pool size= %s%n", dbConnectionPoolSize);
  }
}
```

## Main class

We are setting active profile programmatically:

```
@SpringBootApplication
public class ExampleMain {

  public static void main(String[] args) {
      SpringApplication sa = new SpringApplication(ExampleMain.class);
      //programmatically setting active profile
      ConfigurableEnvironment env = new StandardEnvironment();
      env.setActiveProfiles("dev");
      sa.setEnvironment(env);

      sa.run(args);
  }
}
width= 500
height= 300
refresh rate= 60
db connect url= jdbc:mysql://localhost:3306/my_schema
db connection pool size= 10
```

- width = 500 is from application.properties (default). It is not used in any other profile
- height = 300 is from dev (active profile). It overrides height=400 (default)
- refresh rate= 60 is from throttling (include profile). It overrides refresh rate=30 (default)
- db connect url= jdbc:mysql://localhost:3306/my_schema id from dev (active). It overrides db (include profile) one.
- db connection pool size= 10 is from db (include profile). It is not specified anywhere else.