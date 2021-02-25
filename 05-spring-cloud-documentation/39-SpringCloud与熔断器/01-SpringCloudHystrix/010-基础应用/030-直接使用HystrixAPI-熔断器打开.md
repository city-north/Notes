# 030-直接使用HystrixAPI-熔断器打开

[TOC]

## 一言蔽之



这个配置表示

- 10s (可以通过withMetricsRollingStatisticalWindowInMilliseconds 进行修改, 默认是10s) 内 
- 如果至少有10个请求
- 10%的异常比例 , 则触触发熔断器进入Open状态
- 3000s 表示从Open状态进入 Half-open状态的时间

```java
public class CircuitBreakerRestCommand extends HystrixCommand<String> {
    private final RestTemplate restTemplate = new RestTemplate();
    private String code;
    public CircuitBreakerRestCommand(String code) {
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CBRestExample"))
            .andCommandPropertiesDefaults(
                HystrixCommandProperties.Setter()
              //设置异常比例, 10%
                    .withCircuitBreakerErrorThresholdPercentage(10)
              //至少有10个请求
                    .withCircuitBreakerRequestVolumeThreshold(10)
              // 3000ms表示从Open状态进入Half-Open状态
                    .withCircuitBreakerSleepWindowInMilliseconds(3000)
                    //.withMetricsRollingStatisticalWindowInMilliseconds(1000)
            )
        );
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