# 040-RestTemplate失败重试

[TOC]

## 失败重试机制

负载均衡的RestTemplate可以添加失败重试机制。默认情况下，失败重试机制是关闭的，

- 启用方式是将Spring Retry添加到应用程序的类路径中。

还可以设置spring.cloud.loadbalancer.retry.enabled=false禁止类路径中Spring retry的重试逻辑。

如果想要添加一个或者多个RetryListener到重试请求中，可以创建一个类型为LoadBalancedRetryListenerFactory的Bean，用来返回将要用于重试机制的RetryListener的列表，如下代码所示：

```java
@Configuration
public class RryListenerConfiguration {
  @Bean
  LoadBalancedRetryListenerFactory retryListenerFactory() {
    return new LoadBalancedRetryListenerFactory() {
      @Override
      public RetryListener[] createRetryListeners(String service) {
        return new RetryListener[]{new RetryListener() {
          @Override
          // 重试开始前的工作
          public 〈T, E extends Throwable〉 boolean open(RetryContext context, RetryCallback〈T, E〉 callback) {
            return true;
          }
          // 重试结束后的工作
          @Override
          public 〈T, E extends Throwable〉 void close(RetryContext context, RetryCallback〈T, E〉 callback, Throwable throwable) {
          }
          // 重试出错后的工作
          @Override
          public 〈T, E extends Throwable〉 void onError(RetryContext context, RetryCallback〈T, E〉 callback, Throwable throwable) {
          }
        }};
      }
    };
  }
}
```



其中，自定义配置类中定义了生成LoadBalancedRetryListenerFactory实例的@Bean方法，该工厂类的createRetryListeners方法会生成一个RetryListener实例，用于进行网络请求的重试

