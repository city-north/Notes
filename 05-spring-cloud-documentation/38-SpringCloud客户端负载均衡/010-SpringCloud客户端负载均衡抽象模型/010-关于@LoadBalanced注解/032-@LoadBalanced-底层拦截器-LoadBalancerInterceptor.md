# 032@LoadBalanced-底层拦截器-LoadBalancerInterceptor

[TOC]

## LoadBalancerInterceptor注册时机

org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration 中

- 当不存在RetryTemplate时加载

```java
@Configuration
@ConditionalOnMissingClass("org.springframework.retry.support.RetryTemplate")
static class LoadBalancerInterceptorConfig {
   @Bean
   public LoadBalancerInterceptor ribbonInterceptor(
         LoadBalancerClient loadBalancerClient,
         LoadBalancerRequestFactory requestFactory) {
      return new LoadBalancerInterceptor(loadBalancerClient, requestFactory);
   }

   @Bean
   @ConditionalOnMissingBean
   public RestTemplateCustomizer restTemplateCustomizer(
         final LoadBalancerInterceptor loadBalancerInterceptor) {
      return restTemplate -> {
               List<ClientHttpRequestInterceptor> list = new ArrayList<>(
                       restTemplate.getInterceptors());
        //将 LoadBalancerInterceptor 加入到RestTemplate中
               list.add(loadBalancerInterceptor);
               restTemplate.setInterceptors(list);
           };
   }
}
```

## LoadBalancerInterceptor拦截器的作用

@LoadBalancer 本质上在修饰的 RestTemplate 上添加了一系列拦截器 LoadBalancerInterceptor

org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor

```java
public class LoadBalancerInterceptor implements ClientHttpRequestInterceptor {

   private LoadBalancerClient loadBalancer;

   private LoadBalancerRequestFactory requestFactory;

   public LoadBalancerInterceptor(LoadBalancerClient loadBalancer,
         LoadBalancerRequestFactory requestFactory) {
      this.loadBalancer = loadBalancer;
      this.requestFactory = requestFactory;
   }

   public LoadBalancerInterceptor(LoadBalancerClient loadBalancer) {
      // for backwards compatibility
      this(loadBalancer, new LoadBalancerRequestFactory(loadBalancer));
   }

   @Override
   public ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
         final ClientHttpRequestExecution execution) throws IOException {
      final URI originalUri = request.getURI(); // ②
      String serviceName = originalUri.getHost();
      Assert.state(serviceName != null,
            "Request URI does not contain a valid hostname: " + originalUri);
      return this.loadBalancer.execute(serviceName,  //③
            this.requestFactory.createRequest(request, body, execution));
   }

}
```

- LoadBalancerInterceptor 的构造方法需要 LoadBalancerClient 和  LoadBalancerRequestFactory

  -  [050-客户端负载均衡器-LoadBalancerClient.md](050-客户端负载均衡器-LoadBalancerClient.md)  根据负载均衡请求(LoadBalancerRequest) 和服务名进行真正的服务调用
  -  [060-负载均衡请求工厂-LoadBalancerRequestFactory.md](060-负载均衡请求工厂-LoadBalancerRequestFactory.md)  负责构造负载均衡请求(LoadBalancerRequest) 构造过程中会使用 (LoadBalancerRequestTransformer) 对请求进行一些自定义的转换操作 (默认没有实现)

- ② 服务名使用 URI中的 host信息

- ③ 使用 LoadBalancerClient 负载均衡器做真正的服务调用

  

