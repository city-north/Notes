# 010-熔断器工厂-CircuitBreakerFactory-ReactiveCircuitBreakerFactory

[TOC]

## 熔断器工厂有什么用

CircuitBreakerFactory 用于创建 CurcuitBreaker熔断器

## CircuitBreakerFactory

```java
public abstract class CircuitBreakerFactory<CONF, CONFB extends ConfigBuilder<CONF>>
      extends AbstractCircuitBreakerFactory<CONF, CONFB> {
   public abstract CircuitBreaker create(String id);
}
```

- CONF 代表配置
- CONB 代表 ConfigBuilder接口

## 抽象通用实现

抽象实现封装了配置的容器

```java
public abstract class AbstractCircuitBreakerFactory<CONF, CONFB extends ConfigBuilder<CONF>> {

  //① 内部使用ConcurrentHashMap 保存配置信息
   private final ConcurrentHashMap<String, CONF> configurations = new ConcurrentHashMap<>();

  //②提供configure 方法 . 先通过configBuilder 抽象方法得到 ConfigBuilder , 再调用 Consumer接口对这个ConfigBuilder 做一些额外的处理, 最终构建出Config配置 , 并设置到 configurations
   public void configure(Consumer<CONFB> consumer, String... ids) {
      for (String id : ids) {
         CONFB builder = configBuilder(id);
         consumer.accept(builder);
         CONF conf = builder.build();
         getConfigurations().put(id, conf);
      }
   }

   protected ConcurrentHashMap<String, CONF> getConfigurations() {
      return configurations;
   }
	//③ 抽象方法, 通过一个ID得到ConfigBuilder
   protected abstract CONFB configBuilder(String id);

  //④抽象方法, 进行默认的配置, 一般 CircuitBreakerFactory 实现类内部都有默认的配置
  	// - 如果开发者不进行配置构造, 那么久使用默认的配置,,默认的配置通过该方法进行修改
   public abstract void configureDefault(Function<String, CONF> defaultConfiguration);

}
```