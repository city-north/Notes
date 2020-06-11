# Hystrix

## 是什么

> hystrix 是一个延迟和容错库,主要用来服务降级,防止级联故障等
>
> 五种情况会触发 fallback
>
> - failure 执行失败,抛错
> - timeout 执行超时
> - short_circuited 断路器打开
> - thread_pool_rejected 线程池拒绝
> - semaphore_rejected 信号量拒绝



HyStrix 是一个延迟和容错库,旨在隔离远程系统,服务,和第三方库,组织级联故障,在负载的分布式系统中实现恢复能力

## 解决什么问题

在多系统和微服务的情况下,需要一种机制来处理延迟和故障,并保护整个系统处于可用稳定状态

![image-20200602124233616](assets/image-20200602124233616.png)

