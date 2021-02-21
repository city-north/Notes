# 040-服务实例选择器-ServiceInstanceChooser

[TOC]

## ServiceInstanceChooser语义

服务实例选择器, 根据服务名称获取一个服务实例 (ServiceInstance)

```java
org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser
```

## ServiceInstanceChooser

使用 ServiceInstanceChooser 并注入可以根据 serviceName 随机获取一个服务实例

```java
//实现该类来选择一个服务器用于发送请求
public interface ServiceInstanceChooser {
    /**
     * 根据serviceId从服务器列表中选择一个ServiceInstance
     **/
    ServiceInstance choose(String serviceId);
}
```

## 自定义ServiceInstanceChooser

```java
public class RandomServiceInstanceChooser implements ServiceInstanceChooser {

    private final DiscoveryClient discoveryClient;

    private final Random random;

    public RandomServiceInstanceChooser(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
        random = new Random();
    }

    @Override
    public ServiceInstance choose(String serviceId) {
        List<ServiceInstance> serviceInstanceList =
            discoveryClient.getInstances(serviceId);
      //随机
        return serviceInstanceList.get(random.nextInt(serviceInstanceList.size()));
    }
}
```

```java
@GetMapping("/customChooser")
public String customChooser() {
    ServiceInstance serviceInstance = randomServiceInstanceChooser.choose(serviceName);
    return normalRestTemplate.getForObject(
        "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/", String.class);
}
```

