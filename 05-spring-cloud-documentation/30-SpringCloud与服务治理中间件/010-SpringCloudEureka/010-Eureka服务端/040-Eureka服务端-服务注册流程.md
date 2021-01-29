# 040-Eureka服务端-服务注册流程

[TOC]

## 一言蔽之

- Eureka Client在发起服务注册时会将自身的服务实例元数据封装在InstanceInfo中，然后将InstanceInfo发送到Eureka Server。

- Eureka Server在接收到Eureka Client发送的InstanceInfo后将会尝试将其放到本地注册表中以供其他Eureka Client进行服务发现。

## 客户端注册

 [050-Eureka客户端-服务注册.md](../020-Eureka客户端/050-Eureka客户端-服务注册.md) 

## 服务注册的核心URL

<img src="../../../../assets/image-20210129161735471.png" alt="image-20210129161735471" style="zoom: 67%;" />

## 服务注册的核心逻辑

服务注册的主要实现位于AbstractInstanceRegistry#registry方法中，代码如下所示：

```java
//AbstractInstanceRegistry.java
public void register(InstanceInfo registrant, int leaseDuration, boolean isReplication) {
    try {
        // 获取读锁
        read.lock();
        // 这里的registry是ConcurrentHashMap<String, Map<String, Lease<InstanceInfo>>>
        // registry，根据appName对服务实例集群进行分类
        Map<String, Lease<InstanceInfo>> gMap = registry.get(registrant.getAppName());
        REGISTER.increment(isReplication);
        if (gMap == null) {
            final ConcurrentHashMap<String, Lease<InstanceInfo>> gNewMap = new ConcurrentHashMap<String, Lease<InstanceInfo>>();
        // 这里有一个比较严谨的操作，防止在添加新的服务实例集群租约时把已有的其他线程添加的集群
          // 租约覆盖掉，如果存在该键值，直接返回已存在的值；否则添加该键值对，返回null
            gMap = registry.putIfAbsent(registrant.getAppName(), gNewMap);
            if (gMap == null) {
                gMap = gNewMap;
            }
        //根据instanceId获取实例的租约
        Lease<InstanceInfo> existingLease = gMap.get(registrant.getId());
        if (existingLease != null && (existingLease.getHolder() != null)) {
            Long existingLastDirtyTimestamp = existingLease.getHolder().getLastDirtyTimestamp();
            Long registrationLastDirtyTimestamp = registrant.getLastDirtyTimestamp();
            // 如果该实例的租约已经存在，比较最后更新时间戳的大小，取最大值的注册信息为有效
            if (existingLastDirtyTimestamp > registrationLastDirtyTimestamp) {
                registrant = existingLease.getHolder();
            }
          ....
```

在register中，服务实例的InstanceInfo保存在Lease中，Lease在AbstractInstanceRegistry中统一通过ConcurrentHashMap保存在内存中。

在服务注册过程中，会先获取一个读锁，防止其他线程对registry注册表进行数据操作，避免数据的不一致。然后从resgitry查询对应的InstanceInfo租约是否已经存在注册表中，根据appName划分服务集群，使用InstanceId唯一标记服务实例。

- 如果租约存在，比较两个租约中的InstanceInfo的最后更新时间lastDirtyTimestamp，保留时间戳大的服务实例信息InstanceInfo。

- 如果租约不存在，意味这是一次全新的服务注册，将会进行自我保护的统计，创建新的租约保存InstanceInfo。

接着将租约放到resgitry注册表中。

之后将进行一系列缓存操作并根据覆盖状态规则设置服务实例的状态，缓存操作包括将InstanceInfo加入用于统计Eureka Client增量式获取注册表信息的recentlyChangedQueue和失效responseCache中对应的缓存。最后设置服务实例租约的上线时间用于计算租约的有效时间，释放读锁并完成服务注册。代码如下所示：

```java
// AbstractInstanceRegistry.java
        } else {
            // 如果租约不存在，这是一个新的注册实例
            synchronized (lock) {
            if (this.expectedNumberOfRenewsPerMin > 0) {
            // 自我保护机制
                    this.expectedNumberOfRenewsPerMin = this.expectedNumberOfRenewsPerMin + 2;
                    this.numberOfRenewsPerMinThreshold =
                    (int) (this.expectedNumberOfRenewsPerMin * serverConfig.getRenewalPercentThreshold());
              }
          }
        }
        // 创建新的租约
        Lease<InstanceInfo> lease = new Lease<InstanceInfo>(registrant, leaseDuration);
        if (existingLease != null) {) {
            // 如果租约存在，继承租约的服务上线初始时间
            lease.setServiceUpTimestamp(existingLease.getServiceUpTimestamp());
        }
        // 保存租约
        gMap.put(registrant.getId(), lease);
        // 添加最近注册队列
        // private final CircularQueue<Pair<Long, String>> recentRegisteredQueue
        // 用来统计最近注册服务实例的数据
        synchronized (recentRegisteredQueue) {
        recentRegisteredQueue.add(new Pair<Long, String>(
            System.currentTimeMillis(),
            registrant.getAppName() + "(" + registrant.getId() + ")"));
        }
        ...
        // 根据覆盖状态规则得到服务实例的最终状态，并设置服务实例的当前状态
        InstanceStatus overriddenInstanceStatus = getOverriddenInstanceStatus(registrant, existingLease, isReplication);
        registrant.setStatusWithoutDirty(overriddenInstanceStatus);
        // 如果服务实例状态为UP，设置租约的服务上线时间，只有第一次设置有效
        if (InstanceStatus.UP.equals(registrant.getStatus())) {
            lease.serviceUp();
        }
        registrant.setActionType(ActionType.ADDED);
        // 添加最近租约变更记录队列，标识ActionType为ADDED
        // 这将用于Eureka Client增量式获取注册表信息
        // private ConcurrentLinkedQueue<RecentlyChangedItem>
        recentlyChangedQueue
        recentlyChangedQueue.add(new RecentlyChangedItem(lease));
        // 设置服务实例信息更新时间
        registrant.setLastUpdatedTimestamp();
        // 设置response缓存过期，这将用于Eureka Client全量获取注册表信息
        invalidateCache(registrant.getAppName(), registrant.getVIPAddress(), registrant. getSecureVipAddress());
    } finally {
        // 释放锁read.unlock();
    }
}

```

## 注册中心的数据结构

```java
private final ConcurrentHashMap<String, Map<String, Lease<InstanceInfo>>> registry = new ConcurrentHashMap<String, Map<String, Lease<InstanceInfo>>>();
```



