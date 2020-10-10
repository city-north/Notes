## 程序员自定义

#### 全局策略设置

```
@Configuration
public class TestConfiguration {

	@Bean
	public IRule ribbonRule() {
		return new RandomRule();//设置为随机
	}
}
```

#### 基于注解的策略设置

```
public @interface AvoidScan {

}
```

```
@RibbonClient(name = "client-a", configuration = TestConfiguration.class)
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = {AvoidScan.class})})
```

- client 的的策略使用的是`TestConfiguration`定义的
- ComponentScan 排除`AvoidScan`注解标注的类

#### 基于配置文件的配置

```
client-a:
  ribbon:
    ConnectTimeout: 3000 #全局请求连接的超时时间,默认 5 秒
    ReadTimeout: 60000 #全局超时是时间
    MaxAutoRetries: 0 #对第一次请求的服务的重试次数.如果要开启,需要保证服务的幂等性 ,注意
    MaxAutoRetriesNextServer: 1 #要重试的下一个服务的最大数量（不包括第一个服务）
    OkToRetryOnAllOperations: true #对所有操作请求都进行充实
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule#ribbon:
eager-load: #饥饿加载
  enabled: true
  clients: client-a, client-b, client-c
    
```

