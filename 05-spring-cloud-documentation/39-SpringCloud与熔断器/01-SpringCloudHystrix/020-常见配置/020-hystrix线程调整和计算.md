# 020-hystrix线程调整和计算

[TOC]

## 配置总结

- 默认超时时间 为 1000ms, 如果业务明显超过 1000ms,根据自己的业务进行修改
- 线程池默认为 10, 如果你知道确实要使用更多可以调整
- 金丝雀发布
- 在生产环境中运行超过 24 小时
- 如果系统有告警或者监控,那么可以依靠它们捕捉问题
- 运行 24 小时候,通过延迟百分位和流量来计算有意义的最低满足值
- 在生产或者测试环境实时修改值,通过仪表盘监控
- 如果断路器产生变化和影响,则需要再次确认这个配置

Threadpool 的大小计算推荐

```
每秒请求的峰值 * 99%的延迟百分比(请求响应时间) + 预留缓冲值
30 * 0.2s = 6 + 预留4 = 10
```

### 常用配置

在真实的应用过程中,一般会对超时时间,线程池大小,信号量等进行修改,具体要结合业务进行分析

默认的 Hystrix 超时时间为 1 秒,但是在实际的运用过程中,发现 1秒比较短,应该设为 5-10 s , 对于一些同步文件上传等业务则会更长,如果配置了 Ribbon 的事件,其超时时间也需要和 Ribbon 的事件配合使用,一般情况下 Ribbon 的事件应短语 Hystrix 超时时间

```
hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds # 全局请求超时时间,推荐 10s
```

```java
hystrix:
  threadpool:
    default:
      coreSize: 20  #全局默认核心线程池大小
      maximumSize: 50 # 全局默认核心最大线程池大小,默认是 10
      maxQueueSize: -1 #
      allowMaximumSizeToDivergeFromCoreSize: true # 该属性允许配置 coreSIze 和 maximumSize生效,默认为 false
  command:
    default:
      execution:
        timeout:
          enabled: false
        isolation:
          thread:
            interruptOnTimeout: false 
            timeoutInMilliseconds: 15000 
```



