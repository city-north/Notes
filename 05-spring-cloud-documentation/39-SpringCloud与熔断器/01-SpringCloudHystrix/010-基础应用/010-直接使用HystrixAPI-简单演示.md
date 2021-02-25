# 010-直接使用HystrixAPI-简单演示

[TOC]

## 一言蔽之



## HystrixCommand

HystrixCommand 是 Hystrix对外暴露的一个抽象类, 器内部定义 Hystrix熔断器的常规操作, 两个最重要的方法是

- run() : 用于执行业务逻辑, 当发生错误或者熔断的时候会执行getFallBack
- getFallBack

## 实例

HelloWorldCommand 用于创建一个

```java
public class HelloWorldCommand extends HystrixCommand<String> {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String code;
		
    public HelloWorldCommand(String code) {
        super(HystrixCommandGroupKey.Factory.asKey("HelloWorldExample"));
        this.code = code;
    }

    @Override
    protected String run() {
        String url = "http://httpbin.org/status/" + code;
        System.out.println("start to curl: " + url);
        restTemplate.getForObject(url, String.class);
        return "Request success";
    }

    @Override
    protected String getFallback() {
        return "Request failed";
    }
}
```

## 测试类

```java
@Bean
CommandLineRunner commandLineRunner() {
  return new CommandLineRunner() 
    // 执行命令
    @Override
    public void run(String... args) throws Exception {
    HelloWorldCommand helloWorld1 = new HelloWorldCommand("200");
    HelloWorldCommand helloWorld2 = new HelloWorldCommand("500");
    System.err.println(helloWorld1.execute() + " and Circuit Breaker is " + (helloWorld1.isCircuitBreakerOpen() ? "open": "closed"));
    System.err.println(helloWorld2.execute() + " and Circuit Breaker is " + (helloWorld2.isCircuitBreakerOpen() ? "open": "closed"));
    System.err.println("================");
    int num = 1;
    while (num <= 10) {
      TimeoutRestCommand command = new TimeoutRestCommand(num);
      System.err.println("Execute " + num + ": " + command.execute() + " and Circuit Breaker is " + (
        command.isCircuitBreakerOpen() ? "open" : "closed"));
      num++;
    }
    System.err.println("================");
    num = 1;
    while (num <= 15) {
      CircuitBreakerRestCommand command = new CircuitBreakerRestCommand("500");
      System.err.println("Execute " + num + ": " + command.execute() + " and Circuit Breaker is " + (
        command.isCircuitBreakerOpen() ? "open" : "closed"));
      num++;
    }
    Thread.sleep(3000L);
    CircuitBreakerRestCommand command = new CircuitBreakerRestCommand("200");
    System.err.println("Execute " + num + ": " + command.execute() + " and Circuit Breaker is " + (
      command.isCircuitBreakerOpen() ? "open" : "closed"));
  }
};
}
```

## 执行类

```
@Profile("raw")
@SpringBootApplication
public class HystrixCircuitBreakerDemo {

    public static void main(String[] args) {
        new SpringApplicationBuilder(HystrixCircuitBreakerDemo.class)
            .properties("spring.profiles.active=raw").web(WebApplicationType.NONE)
            .run(args);
    }
}
```

## 执行结果

- 前两次进行了run方法的调用, 在第二次调用失败的情况下会执行getFallback 方法, 这两次调用的熔断器都出于 Closed状态
- 

```
start to curl: http://httpbin.org/status/200
Request success and Circuit Breaker is closed
start to curl: http://httpbin.org/status/500
start to curl: http://httpbin.org/delay/1
Request failed and Circuit Breaker is closed
================
Execute 1: Request success and Circuit Breaker is closed
start to curl: http://httpbin.org/delay/2
Execute 2: Request failed and Circuit Breaker is closed
start to curl: http://httpbin.org/delay/3
Execute 3: Request failed and Circuit Breaker is closed
start to curl: http://httpbin.org/delay/4
Execute 4: Request failed and Circuit Breaker is closed
start to curl: http://httpbin.org/delay/5
Execute 5: Request failed and Circuit Breaker is closed
start to curl: http://httpbin.org/delay/6
Execute 6: Request failed and Circuit Breaker is closed
start to curl: http://httpbin.org/delay/7
Execute 7: Request failed and Circuit Breaker is closed
start to curl: http://httpbin.org/delay/8
Execute 8: Request failed and Circuit Breaker is closed
start to curl: http://httpbin.org/delay/9
Execute 9: Request failed and Circuit Breaker is closed
start to curl: http://httpbin.org/delay/10
Execute 10: Request failed and Circuit Breaker is closed
================
start to curl: http://httpbin.org/status/500
Execute 1: Request failed and Circuit Breaker is closed
start to curl: http://httpbin.org/status/500
Execute 2: Request failed and Circuit Breaker is closed
start to curl: http://httpbin.org/status/500
Execute 3: Request failed and Circuit Breaker is closed
start to curl: http://httpbin.org/status/500
Execute 4: Request failed and Circuit Breaker is closed
start to curl: http://httpbin.org/status/500
Execute 5: Request failed and Circuit Breaker is closed
start to curl: http://httpbin.org/status/500
Execute 6: Request failed and Circuit Breaker is closed
start to curl: http://httpbin.org/status/500
Execute 7: Request failed and Circuit Breaker is closed
start to curl: http://httpbin.org/status/500
Execute 8: Request failed and Circuit Breaker is closed
start to curl: http://httpbin.org/status/500
Execute 9: Request failed and Circuit Breaker is closed
start to curl: http://httpbin.org/status/500
Execute 10: Request failed and Circuit Breaker is closed
start to curl: http://httpbin.org/status/500
Execute 11: Request failed and Circuit Breaker is open
Execute 12: Request failed and Circuit Breaker is open
Execute 13: Request failed and Circuit Breaker is open
Execute 14: Request failed and Circuit Breaker is open
Execute 15: Request failed and Circuit Breaker is open
start to curl: http://httpbin.org/status/200
Execute 16: Request success and Circuit Breaker is closed

Process finished with exit code 0

```

## 