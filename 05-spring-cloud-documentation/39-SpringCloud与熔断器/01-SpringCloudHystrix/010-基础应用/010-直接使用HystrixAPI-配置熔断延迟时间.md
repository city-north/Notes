# 010-直接使用HystrixAPI-配置熔断延迟时间

[TOC]

## 配置延迟时间

Hystrix的配置信息通过 HystrixCommand.Setter 完成, 比如, 下面构造函数中的 Setter就是配置执行超时时间为1500ms. 如果执行时间超过了 1500ms, 就会调用 getFallback方法

```java
public class TimeoutRestCommand extends HystrixCommand<String> {

    private final RestTemplate restTemplate = new RestTemplate();

    private final int seconds;

    public TimeoutRestCommand(int seconds) {
        super(
HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("TimeoutRestExample"))
            .andCommandPropertiesDefaults(
                HystrixCommandProperties.Setter()
                    .withExecutionTimeoutInMilliseconds(1500)
                    //.withExecutionTimeoutInMilliseconds(100)
            )
        );
        this.seconds = seconds;
    }

    @Override
    protected String run() {
        String url = "http://httpbin.org/delay/" + seconds;
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

