# 010-SpringBoot和SpringCloud中的事件

# 150-SpringBoot和SpringCloud中的事件

[toc]

## SpringBoot中的事件处理

| 事件类型                            | 发生时机                              |
| ----------------------------------- | ------------------------------------- |
| ApplicationStartingEvent            | 当SpringBoot应用已经启动的时候        |
| ApplicationStartedEvent             | 当SpringBoot应用已启动时              |
| ApplicationEnvironmentPreparedEvent | 当SpringBoot Evironment实例已经准备时 |
| ApplicatonPreparedEvent             | 当SpingBoot应用预备时                 |
| ApplicationReadyEvent               | 当SpringBoot应用完全可用时            |
| ApplicationFailedEvent              | 当SPringBoot应用启动失败时            |

## SpringCloud中的事件

| 事件类型                   | 发生时机                          |
| -------------------------- | --------------------------------- |
| EnvironmentChangeEvent     | 当Environment配置属性发生变化时   |
| HeartbeatEvent             | 当DiscoveryClient客户端发送心跳时 |
| InstancePreRegsteredEvent  | 当服务实例注册前                  |
| InstanceRegisteredEvent    | 当服务实例注册后                  |
| RefreshEvent               | 当RefreshEndpoint被调用时         |
| RefreshScopeRefreshedEvent | 当RefreshScope Bean刷新后         |

