# 060-Eureka服务端-服务剔除

[TOC]

## 一言蔽之

如果Eureka Client在注册后，既没有续约，也没有下线(服务崩溃或者网络异常等原因)，那么服务的状态就处于不可知的状态，不能保证能够从该服务实例中获取到回馈，所以需要服务剔除AbstractInstanceRegistry#evict方法定时清理这些不稳定的服务，该方法会批量将注册表中所有过期租约剔除。实现代码如下所示：

```java
// AbstractInstanceRegistry.java
@Override
public void evict() {
    evict(0l);
}
public void evict(long additionalLeaseMs) {
    // 自我保护相关，如果出现该状态，不允许剔除服务
    if (!isLeaseExpirationEnabled()) {
        return;
    };
    }
    // 遍历注册表register，一次性获取所有的过期租约
    List〈Lease〈InstanceInfo〉〉 expiredLeases = new ArrayList〈〉();
    for (Entry〈String, Map〈String, Lease〈InstanceInfo〉〉〉 groupEntry : registry.
        entrySet()) {
        Map〈String, Lease〈InstanceInfo〉〉 leaseMap = groupEntry.getValue();
        if (leaseMap != null) {
            for (Entry〈String, Lease〈InstanceInfo〉〉 leaseEntry : leaseMap.
                entrySet()) {
                Lease〈InstanceInfo〉 lease = leaseEntry.getValue();
                // 1
                if (lease.isExpired(additionalLeaseMs) &amp;&amp; lease.getHolder() != null) {
                    expiredLeases.add(lease);
                }
            }
        }
    }
    // 计算最大允许剔除的租约的数量，获取注册表租约总数
    int registrySize = (int) getLocalRegistrySize();
    // 计算注册表租约的阀值，与自我保护相关
    int registrySizeThreshold = (int) (registrySize * serverConfig.getRenewalPercentThreshold());
    int evictionLimit = registrySize - registrySizeThreshold;
    // 计算剔除租约的数量
    int toEvict = Math.min(expiredLeases.size(), evictionLimit);
    if (toEvict 〉 0) {
        Random random = new Random(System.currentTimeMillis());
        // 逐个随机剔除
        for (int i = 0; i 〈 toEvict; i++) {
            int next = i + random.nextInt(expiredLeases.size() - i);
            Collections.swap(expiredLeases, i, next);
            Lease〈InstanceInfo〉 lease = expiredLeases.get(i);
            String appName = lease.getHolder().getAppName();
            String id = lease.getHolder().getId();
            EXPIRED.increment();
            // 逐个剔除
            internalCancel(appName, id, false);
        }
    }
}
```

服务剔除将会遍历registry注册表，找出其中所有的过期租约，然后根据配置文件中续租百分比阀值和当前注册表的租约总数量计算出最大允许的剔除租约的数量(当前注册表中租约总数量减去当前注册表租约阀值)，分批次剔除过期的服务实例租约。

- 对过期的服务实例租约调用AbstractInstanceRegistry#internalCancel服务下线的方法将其从注册表中清除掉。

服务剔除#evict方法中有很多限制，都是为了保证Eureka Server的可用性：

- 自我保护时期不能进行服务剔除操作。
- 过期操作是分批进行。
- 服务剔除是随机逐个剔除，剔除均匀分布在所有应用中，防止在同一时间内同一服务集群中的服务全部过期被剔除，以致大量剔除发生时，在未进行自我保护前促使了程序的崩溃。

## 服务剔除是一个定时的任务

服务剔除是一个定时的任务，所以AbstractInstanceRegistry中定义了一个EvictionTask用于定时执行服务剔除，默认为60秒一次。服务剔除的定时任务一般在AbstractInstanceRegistry初始化结束后进行，按照执行频率evictionIntervalTimerInMs的设定，定时剔除过期的服务实例租约。

## 自我保护机制

自我保护机制主要在Eureka Client和Eureka Server之间存在网络分区的情况下发挥保护作用，在服务器端和客户端都有对应实现。假设在某种特定的情况下(如网络故障)，Eureka Client和Eureka Server无法进行通信，此时Eureka Client无法向Eureka Server发起注册和续约请求，Eureka Server中就可能因注册表中的服务实例租约出现大量过期而面临被剔除的危险，然而此时的Eureka Client可能是处于健康状态的(可接受服务访问)，如果直接将注册表中大量过期的服务实例租约剔除显然是不合理的。

针对这种情况，Eureka设计了“自我保护机制”。

在Eureka Server处，如果出现大量的服务实例过期被剔除的现象，那么该Server节点将进入自我保护模式，保护注册表中的信息不再被剔除，在通信稳定后再退出该模式；在Eureka Client处，如果向Eureka Server注册失败，将快速超时并尝试与其他的Eureka Server进行通信。

“自我保护机制”的设计大大提高了Eureka的可用性。