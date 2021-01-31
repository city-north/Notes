# 060-Eureka客户端-初始化定时任务

[TOC]

## 一言蔽之

为了监控Eureka Client应用信息和状态的变化，Eureka Client设置了一个按需注册定时器，定时检查应用信息或者状态的变化，并在发生变化时向Eureka Server重新注册，避免注册表中的本服务实例信息不可用。

## 为什么需要这个定时任务

很明显，服务注册应该是一个持续的过程，Eureka Client通过定时发送心跳的方式与Eureka Server进行通信，维持自己在Server注册表上的租约。

同时Eureka Server注册表中的服务实例信息是动态变化的，为了保持Eureka Client与Eureka Server的注册表信息的一致性，Eureka Client需要定时向Eureka Server拉取注册表信息并更新本地缓存。

- 为了监控Eureka Client应用信息和状态的变化，Eureka Client设置了一个按需注册定时器，定时检查应用信息或者状态的变化，并在发生变化时向Eureka Server重新注册，避免注册表中的本服务实例信息不可用。

## 初始化定时器的具体实现

**一共初始化了三个定时器任务**

在DiscoveryClient#initScheduledTasks方法中初始化了三个定时器任务，

- 一个用于向Eureka Server拉取注册表信息刷新本地缓存
- 一个用于向Eureka Server发送心跳
- 一个用于进行按需注册的操作。

## 初始化代码

```java
// DiscoveryClient.java
private void initScheduledTasks() {
  	//判断开关
    if (clientConfig.shouldFetchRegistry()) {
        // 注册表缓存刷新定时器
        // 获取配置文件中刷新间隔，默认为30s，可以通过eureka.client.registry-fetch-interval-seconds进行设置
        int registryFetchIntervalSeconds = clientConfig.getRegistryFetchIntervalSeconds();
        int expBackOffBound = clientConfig.getCacheRefreshExecutorExponentialBackOffBound(); 
      	scheduler.schedule(new TimedSupervisorTask("cacheRefresh", scheduler, cacheRefreshExecutor,
                registryFetchIntervalSeconds, TimeUnit.SECONDS, expBackOffBound, new CacheRefreshThread()
            ),
            registryFetchIntervalSeconds, TimeUnit.SECONDS);
    }
    if (clientConfig.shouldRegisterWithEureka()) {
        // 发送心跳定时器，默认30秒发送一次心跳
        int renewalIntervalInSecs = instanceInfo.getLeaseInfo().getRenewalIntervalInSecs();
        int expBackOffBound = clientConfig.getHeartbeatExecutorExponentialBackOffBound();
       // 心跳定时器
        scheduler.schedule(
            new TimedSupervisorTask("heartbeat", scheduler, heartbeatExecutor,
                renewalIntervalInSecs,
                TimeUnit.SECONDS, expBackOffBound, new HeartbeatThread()
            ),
            renewalIntervalInSecs, TimeUnit.SECONDS);
        	// 按需注册定时器 // InstanceInfo replicator
          ...
}
```

## 定时器1-拉取注册表信息刷新本地缓存

 [061-Eureka客户端-初始化拉取注册表信息定时器.md](061-Eureka客户端-初始化拉取注册表信息定时器.md) 

## 定时器2:发送心跳

 [062-Eureka客户端-初始化向EurekaServer发送心跳定时器.md](062-Eureka客户端-初始化向EurekaServer发送心跳定时器.md) 

## 定时器3:按需注册定时器

 [063-Eureka客户端-初始化按需注册定时器.md](063-Eureka客户端-初始化按需注册定时器.md) 

## 缓存刷新定时任务与发送心跳定时任务

在DiscoveryClient#initScheduledTasks方法中，通过ScheduledExecutorService#schedule的方式提交缓存刷新任务和发送心跳任务，任务执行的方式为延时执行并且不循环，这两个任务的定时循环逻辑由TimedSupervisorTask提供实现。TimedSupervisorTask继承了TimerTask，提供执行定时任务的功能。它在run方法中定义执行定时任务的逻辑。具体代码如下所示：

```java
//com.netflix.discovery.TimedSupervisorTask#TimedSupervisorTask
public class TimedSupervisorTask extends TimerTask {
    ...
    public void run() {
        Future future = null;
        try {
            // 执行任务
            future = executor.submit(task);
						threadPoolLevelGauge.set((long) executor.getActiveCount());
            // 等待任务执行结果
            future.get(timeoutMillis, TimeUnit.MILLISECONDS);
            // 执行完成，设置下次任务执行频率(时间间隔)
            delay.set(timeoutMillis);
            threadPoolLevelGauge.set((long) executor.getActiveCount());
        } catch (TimeoutException e) {
            // 执行任务超时
            timeoutCounter.increment();
            // 设置下次任务执行频率(时间间隔)
            long currentDelay = delay.get();
            long newDelay = Math.min(maxDelay, currentDelay * 2);
            delay.compareAndSet(currentDelay, newDelay);
        } catch (RejectedExecutionException e) {
            // 执行任务被拒绝
            // 统计被拒绝次数
            rejectedCounter.increment();
        } catch (Throwable e) {
            // 其他的异常
            // 统计异常次数
            throwableCounter.increment();
        } finally {
            // 取消未结束的任务
            if (future != null) {
                future.cancel(true);
            }
            // 如果定时任务服务未关闭，定义下一次任务
            if (!scheduler.isShutdown()) {
                scheduler.schedule(this, delay.get(), TimeUnit.MILLISECONDS);
            }
        }
    }
}
```

run方法中存在以下的任务调度过程：

- scheduler初始化并延迟执行TimedSupervisorTask；
- TimedSupervisorTask将task提交executor中执行，task和executor在初始化TimedSupervisorTask时传入：
  - 若task正常执行，TimedSupervisorTask将自己提交到scheduler，延迟delay时间后再次执行；
  - 若task执行超时，计算新的delay时间(不超过maxDelay)，TimedSupervisorTask将自己提交到scheduler，延迟delay时间后再次执行；




￼



