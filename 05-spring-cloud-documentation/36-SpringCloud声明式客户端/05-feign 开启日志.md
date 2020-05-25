# Feign Client 开启日志

Feign 为每一个 FeignClient 都提供了一个 feign.Logger 实例,可以在配置中开启日志,开启方式比较简单,分为两步

- application.yml或者 application.properties 中配置日志输出
- 通过 Java 代码在主程序入口类中配置日志 Bean

## 第一步

application.yml或者 application.properties 中配置日志输出

```
logging:
     level:
       cn.springcloud.book.feign.service.HelloFeignService: debug
```

通过 Java 代码在主程序入口类中配置日志 Bean

## 第二步

通过 Java 代码在主程序入口类中配置日志 Bean

```java
@Configuration
public class HelloFeignServiceConfig {

    /**
     *
     * Logger.Level 的具体级别如下：
         NONE：不记录任何信息
         BASIC：仅记录请求方法、URL以及响应状态码和执行时间
         HEADERS：除了记录 BASIC级别的信息外，还会记录请求和响应的头信息
         FULL：记录所有请求与响应的明细，包括头信息、请求体、元数据
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}

```

