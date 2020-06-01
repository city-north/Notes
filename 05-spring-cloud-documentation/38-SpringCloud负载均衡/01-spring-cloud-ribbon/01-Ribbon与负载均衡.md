# Ribbon 与负载均衡

> Ribbon is a client-side load balancer that gives you a lot of control over the behavior of HTTP and TCP clients 
>
> > 本质上是通过拦截 RestTemplate 根据服务 ID 查找服务实例并使用轮询算法进行拦截修改 url 的机制

Ribbon 是一个客户端负载均衡器

少了它.服务就不能横向拓展,有悖于云原生 12 原则

Feign 和 Zuul 默认集成了 Ribbon

Spring Cloud Ribbon 是 SpringCloud 微服务体系弹性拓展的基础组件,与其他组件结合可以发挥强大的作用

- 丰富的负载均衡策略
- 重试机制
- 支持多协议异步与响应式模型
- 容错
- 缓存
- 批处理

等等特性让你在构建自己的微服务架构时游刃有余

## 负载均衡

负载均衡 Load Balance , 利用特定方式将流量分摊到多个操作单元上的一种手段,它对系统吞吐量与系统处理能力有质的提升

- 软负载 , Nginx 
- 硬负载, F5

或者

- 服务端负载均衡

> Nginx 和 F5 都属于集中式负载均衡,

- 客户端负载均衡

> 实例一般存储在 Eureka , Consul , Zookeeper ,etcd 这样的注册中心,此时的负载均衡器就是类似于 Ribbon 的 IPC (Inter-process Communication 进程间通讯)组件,因此,进程内负载均衡又叫客户端负载混合

<img src="assets/image-20200528123430064.png" alt="image-20200528123430064" style="zoom:50%;" />

