# 020-Hystrix常见配置

[TOC]

## Execution:执行相关

| 配置项                                                       | 作用                                                         | 默认值         |
| ------------------------------------------------------------ | ------------------------------------------------------------ | -------------- |
| [`execution.isolation.strategy`](https://github.com/Netflix/Hystrix/wiki/Configuration#execution.isolation.strategy) | 隔离策略:<br />1. 线程池隔离(THREAD)<br />2. 信号量隔离 (SEMAPHORE) | 线程池(THREAD) |
| [`execution.isolation.thread.timeoutInMilliseconds`](https://github.com/Netflix/Hystrix/wiki/Configuration#execution.isolation.thread.timeoutInMilliseconds) | 执行超时时间, 超时后进入fallback逻辑                         | 1000(ms)       |
| [`execution.timeout.enabled`](https://github.com/Netflix/Hystrix/wiki/Configuration#execution.timeout.enabled) | 超时时间是否生效                                             | true           |
| [`execution.isolation.thread.interruptOnTimeout`](https://github.com/Netflix/Hystrix/wiki/Configuration#execution.isolation.thread.interruptOnTimeout) | HystrixCommand 执行超时后, 是否可以被打断                    | true           |
| [`execution.isolation.thread.interruptOnCancel`](https://github.com/Netflix/Hystrix/wiki/Configuration#execution.isolation.thread.interruptOnCancel) | HystrixCommand 执行被取消时是否可以被打断                    | 10 QPS         |
| [`execution.isolation.semaphore.maxConcurrentRequests`](https://github.com/Netflix/Hystrix/wiki/Configuration#execution.isolation.semaphore.maxConcurrentRequests) | HystrixCommand.getFallback()方法请求最大请求数               | 10(请求数)     |
|                                                              |                                                              |                |

## [Fallback](https://github.com/Netflix/Hystrix/wiki/Configuration#CommandFallback)

| 配置项                                                       | 作用                                     | 默认值 |
| ------------------------------------------------------------ | ---------------------------------------- | ------ |
| [`fallback.isolation.semaphore.maxConcurrentRequests`](https://github.com/Netflix/Hystrix/wiki/Configuration#fallback.isolation.semaphore.maxConcurrentRequests) | HystrixCommand.getFallback()方法是否生效 | true   |
| [`fallback.enabled`](https://github.com/Netflix/Hystrix/wiki/Configuration#fallback.enabled) | 熔断功能是否开启                         | true   |

## [Circuit Breaker](https://github.com/Netflix/Hystrix/wiki/Configuration#CommandCircuitBreaker)

| 配置项                                                       | 作用                                                         |            |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ---------- |
| [`circuitBreaker.enabled`](https://github.com/Netflix/Hystrix/wiki/Configuration#circuitBreaker.enabled) | 是否启用                                                     | true       |
| [`circuitBreaker.requestVolumeThreshold`](https://github.com/Netflix/Hystrix/wiki/Configuration#circuitBreaker.requestVolumeThreshold) | 时间窗口内最小能触发熔断的请求数. (如果时间窗口内没有达到最小请求数, 即使异常比例达标, 也不会出触发熔断效果) | 20         |
| [`circuitBreaker.sleepWindowInMilliseconds`](https://github.com/Netflix/Hystrix/wiki/Configuration#circuitBreaker.sleepWindowInMilliseconds) | 熔断器从Open状态进入 Half-Open状态的事件                     | 5000ms     |
| [`circuitBreaker.errorThresholdPercentage`](https://github.com/Netflix/Hystrix/wiki/Configuration#circuitBreaker.errorThresholdPercentage) | 异常比例, 时间窗口内最小能                                   | 50(百分比) |
| [`circuitBreaker.forceOpen`](https://github.com/Netflix/Hystrix/wiki/Configuration#circuitBreaker.forceOpen) | 断路器是否被强制打开, 如打开所有带额请求都会被拒绝           | false      |
| [`circuitBreaker.forceClosed`](https://github.com/Netflix/Hystrix/wiki/Configuration#circuitBreaker.forceClosed) | 熔断器是否强制关闭, 如果关闭, 达到异常比例的请求都会被拒绝   | false      |

## [Metrics](https://github.com/Netflix/Hystrix/wiki/Configuration#CommandMetrics)

| 配置项                                        | 作用                                                         |         |
| --------------------------------------------- | ------------------------------------------------------------ | ------- |
| metrics.rollingStats.timeInMilliseconds       | 请求统计时间窗口                                             | 10000ms |
| metrics.rollingStats.numBuckets               | 时间窗口内的桶的个数, 默认10s, 10s内分10个桶, 每个桶记录着<br />调用成功(Success) <br />调用失败(Failure)<br />超时(Timeout)<br />拒绝(Rejection) 次数 | 10个    |
| metrics.rollingPercentile.enabled             | 是否统计响应时间百分比<br />Dashboard会展示这些百分比统计数据 | true    |
| metrics.rollingPercentile.timeInMilliseconds  | 统计响应时间百分比时间窗口                                   | 6       |
| metrics.rollingPercentile.numBuckets          | 统计响应时间百分比时滑动窗口要划分的桶个数                   |         |
| metrics.rollingPercentile.bucketSize          | 统计响应时间百分比时,每个滑动窗口桶内保存的最大请求数, 桶内的请求超出这个值后, 会覆盖最前面保存的数据 | 100     |
| metrics.healthSnapshot.intervalInMilliseconds | 用来设计采集影响断路器状态的健康快照(请求的成功, 失败百分比)的间隔时间 | 500     |
|                                               |                                                              |         |

## [Request Context](https://github.com/Netflix/Hystrix/wiki/Configuration#CommandRequestContext)

| 配置项                                                       | 作用                                                         |      |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ---- |
| [`requestCache.enabled`](https://github.com/Netflix/Hystrix/wiki/Configuration#requestCache.enabled) | 是否开启缓存, 下一个具有想用key的请求直接从缓存中取出结果, 减少请求开销<br />缓存的key通过 HystrixCommand.getCacheKey方法获取 | true |
| [`requestLog.enabled`](https://github.com/Netflix/Hystrix/wiki/Configuration#requestLog.enabled) | 请求日志                                                     | true |
|                                                              |                                                              |      |

## [Thread Pool Properties](https://github.com/Netflix/Hystrix/wiki/Configuration#ThreadPool)

| 配置项                                                       | 作用                                                         |       |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ----- |
| [`coreSize`](https://github.com/Netflix/Hystrix/wiki/Configuration#coreSize) |                                                              |       |
| [`maximumSize`](https://github.com/Netflix/Hystrix/wiki/Configuration#maximumSize) | 线程池最大线程数                                             | 10    |
| [`maxQueueSize`](https://github.com/Netflix/Hystrix/wiki/Configuration#maxQueueSize) | 线程池队列长度                                               | 10    |
| [`queueSizeRejectionThreshold`](https://github.com/Netflix/Hystrix/wiki/Configuration#queueSizeRejectionThreshold) | 队列拒绝阈值, <br />如果设置改制,线程池的队列长度无效(maxQueueSize参数等于-1,该配置无效) | 5     |
| [`keepAliveTimeMinutes`](https://github.com/Netflix/Hystrix/wiki/Configuration#keepAliveTimeMinutes) | 核心线程树小于最大最大线程数时, 那么多出来的线程存活时间     | 1min  |
| [`allowMaximumSizeToDivergeFromCoreSize`](https://github.com/Netflix/Hystrix/wiki/Configuration#allowMaximumSizeToDivergeFromCoreSize) | 设置成true吗, 当最大线程比核心线程数小时, 最大线程数会用核心数 | false |

