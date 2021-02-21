# 030-Ribbon和SpringCloudLoadBalancer的对比

[TOC]

## 实现对比

| 功能/组件        | SpringCloud LoadBalancer                              | Ribbon                                                       |
| ---------------- | ----------------------------------------------------- | ------------------------------------------------------------ |
| 负载均衡         | ReactiveLoadBalancer<br />ServiceInstanceListSupplier | ILoadBalancer<br />IRule                                     |
| 服务实例         | ServiceInstance                                       | ServiceInstance<br />RibbonServer<br />Server<br />ServerIntrospector |
| 服务调用统计信息 | -                                                     | ServerStats<br />LoadBalancerStats                           |

