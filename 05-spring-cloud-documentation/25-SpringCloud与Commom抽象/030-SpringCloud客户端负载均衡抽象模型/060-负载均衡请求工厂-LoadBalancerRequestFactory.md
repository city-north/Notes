# 060-负载均衡请求工厂-LoadBalancerRequestFactory

[TOC]

## LoadBalancerRequestFactory主要作用

- LoadBalancerRequest 表示一次负载均衡请求, 被 LoadBalancerRequestFactory构造
- LoadBalancerRequestFactory 构造的实际上是包装类 ServiceRequestWrapper (内部基于 服务实例和请求信息构造出真正的URI)
- 然后根据 LoadBalancerRequestTransformer 做二次加工

## 调用中的位置

![image-20210219203722856](../../../assets/image-20210219203722856.png)

## LoadBalancerRequestFactory源码

```java
public class LoadBalancerRequestFactory {

	private LoadBalancerClient loadBalancer;

	private List<LoadBalancerRequestTransformer> transformers;

	public LoadBalancerRequestFactory(LoadBalancerClient loadBalancer,
			List<LoadBalancerRequestTransformer> transformers) {
		this.loadBalancer = loadBalancer;
		this.transformers = transformers;
	}

	public LoadBalancerRequestFactory(LoadBalancerClient loadBalancer) {
		this.loadBalancer = loadBalancer;
	}

	public LoadBalancerRequest<ClientHttpResponse> createRequest(
			final HttpRequest request, final byte[] body,
			final ClientHttpRequestExecution execution) {
		return instance -> {
      //最终使用的是包装器 ServiceRequestWrapper
			HttpRequest serviceRequest = new ServiceRequestWrapper(request, instance,
					this.loadBalancer);
			if (this.transformers != null) {
				for (LoadBalancerRequestTransformer transformer : this.transformers) {
          //使用 transfer 进行定制
					serviceRequest = transformer.transformRequest(serviceRequest,instance);
				}
			}
			return execution.execute(serviceRequest, body);
		};
	}

}
```

## LoadBalancerClient

 [050-客户端负载均衡器-LoadBalancerClient.md](050-客户端负载均衡器-LoadBalancerClient.md) 