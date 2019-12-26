# Feign Logging

将为创建的每个Feign客户端创建一个日志程序。默认情况下，日志程序的名称是用于创建Feign客户端的接口的完整类名。Feign日志只对调试级别有响应。

```yaml
logging.level.project.user.UserClient: DEBUG

```

The `Logger.Level` object that you may configure per client, tells Feign how much to log. Choices are:

- `NONE`, No logging (**DEFAULT**).
- `BASIC`, Log only the request method and URL and the response status code and execution time.
- `HEADERS`, Log the basic information along with request and response headers.
- `FULL`, Log the headers, body, and metadata for both requests and responses.

For example, the following would set the `Logger.Level` to `FULL`:

```java
@Configuration
public class FooConfiguration {
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
```

