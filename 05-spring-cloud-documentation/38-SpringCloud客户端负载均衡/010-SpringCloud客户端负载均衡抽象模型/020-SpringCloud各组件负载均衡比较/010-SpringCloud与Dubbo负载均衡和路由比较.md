# 010-SpringCloud与Dubbo负载均衡和路由比较

| 功能/框架        | Spring Cloud                                                | Apache Dubbo                                                 |
| ---------------- | ----------------------------------------------------------- | ------------------------------------------------------------ |
| 负载均衡         | ReactiveLoadBalancer(SCL)<br />IRule(Ribbon)                | LoadBalance                                                  |
| 路由             | ServerInstanceListSupplier(SCL)<br />ILoadBalancer (Ribbon) | Router                                                       |
| 容错机制         | ServerStats (Ribbon)<br />ILoadBalancer                     | Cluster                                                      |
| 服务实例刷新机制 | DiscoveryClient (SCL)<br />ServerListUpdater (Ribbon)       | NotifyListener                                               |
| 健康监控         | IPing (Ribbon)                                              | 不处理 (注册中心实现) , 可以依靠 fault tolerant 机制过滤不健康的实例 |

