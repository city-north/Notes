# 异常机制和处理

Hystrix 的异常处理中,有 5 种错误的情况下会被 fallback 截获,从而触发 fallback ,这些情况是

- FAILURE 执行失败,抛出异常
- TIMEOUT 执行超时
- SHORT_CIRCUITED 断路器打开
- THREAD_POOL_REJECTED : 线程池拒绝
- SEMAPHORE_REJECTED : 信号量拒绝

不会熔断,不会触发 fallback 的情况

- BAD_REQUEST 

> 会抛出 HystrixBadRequestException 这种异常一般对应的是由非法参数或者一些非系统异常引起的,对于这类可以根据相应创建对应的异常进行异常封装或者直接处理

