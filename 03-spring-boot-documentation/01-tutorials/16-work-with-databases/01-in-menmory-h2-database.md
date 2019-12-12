# In Memory H2 database with JdbcTemplate Example

## 数据库的自动配置

SpringBoot 默认内置H2,HSQL,和Derby数据库

- 我们不需要提供 URL 或者是 DataSource,我们只要引入相关的依赖就可以直接使用嵌入式数据库

## Auto Configuration of JdbcTemplate

`JdbcTemplate` and `NamedParameterJdbcTemplate` classes are auto-configured and we can inject them directly using `@Autowire` i.e.

```
@Autowire
private JdbcTemplate jdbcTemplate;
```

Instead of creating an instance ourselves like we do with [plain Spring jdbc application](https://www.logicbig.com/tutorials/spring-framework/spring-data-access-with-jdbc/jdbc-template-example.html), i.e.

```
@Autowire
private DataSource dataSource;
 .....
JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
```

## Example

 [h2Example](../../00-code/notes-spring-boot/03-spring-boot-feature-example/src/main/java/cn/eccto/study/sb/h2) 

常用配置

```properties
##数据库连接设置
spring.datasource.url = jdbc:h2:mem:dbtest  #配置h2数据库的连接地址
spring.datasource.username = sa  #配置数据库用户名
spring.datasource.password = sa  #配置数据库密码
#配置JDBC Driver
spring.datasource.driverClassName =org.h2.Driver
##数据初始化设置
#进行该配置后，每次启动程序，程序都会运行resources/db/schema.sql文件，对数据库的结构进行操作。
spring.datasource.schema=classpath:h2/schema.sql
#进行该配置后，每次启动程序，程序都会运行resources/db/data.sql文件，对数据库的数据操作。
spring.datasource.data=classpath:h2/data.sql
##h2 web console设置
spring.datasource.platform=h2  #表明使用的数据库平台是h2
# 进行该配置后，h2 web consloe就可以在远程访问了。否则只能在本机访问。
spring.h2.console.settings.web-allow-others=true
spring.h2.console.path=/h2  #进行该配置，你就可以通过YOUR_URL/h2访问h2 web consloe。YOUR_URL是你程序的访问URl。
#进行该配置，程序开启时就会启动h2 web consloe。当然这是默认的，如果你不想在启动程序时启动h2 web consloe，那么就设置为false。
spring.h2.console.enabled=true
```