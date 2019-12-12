# Connecting Remote Production Database (MySql)

```
spring.datasource.url 
spring.datasource.username
spring.datasource.password
spring.datasource.driver-class-name
```

## Example Boot application

#### pom.xml

```
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
   <groupId>mysql</groupId>
   <artifactId>mysql-connector-java</artifactId>
</dependency>
```

#### src/main/resources/application.properties

```
spring.datasource.url=jdbc:mysql://localhost:3306/my_schema
spring.datasource.username=root
spring.datasource.password=1234
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```