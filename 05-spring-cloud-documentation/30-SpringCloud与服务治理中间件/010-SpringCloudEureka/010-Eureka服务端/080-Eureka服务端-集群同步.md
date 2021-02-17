# 080-Eureka服务端-集群同步

[TOC]

## 一言蔽之

如果Eureka Server是通过集群的方式进行部署，那么为了维护整个集群中Eureka Server注册表数据的一致性，势必需要一个机制同步Eureka Server集群中的注册表数据。

Eureka Server集群同步包含两个部分，

- 一部分是Eureka Server在启动过程中从它的peer节点中拉取注册表信息，并将这些服务实例的信息注册到本地注册表中；
- 另一部分是Eureka Server每次对本地注册表进行操作时，同时会将操作同步到它的peer节点中，达到集群注册表数据统一的目的。

## Eureka Server初始化本地注册表信息

在Eureka Server启动的过程中，会从它的peer节点中拉取注册表来初始化本地注册表，这部分主要通过PeerAwareInstanceRegistry#syncUp方法完成，它将从可能存在的peer节点中，拉取peer节点中的注册表信息，并将其中的服务实例信息注册到本地注册表中，如下所示：

```java
// PeerAwareInstanceRegistry.java
public int syncUp() {
    // 从临近的peer中复制整个注册表int count = 0;
    // 如果获取不到，线程等待
    for (int i = 0; ((i 〈 serverConfig.getRegistrySyncRetries()) &amp;&amp; (count == 0));
        i++) {
        if (i 〉 0) {
            try {
                Thread.sleep(serverConfig.getRegistrySyncRetryWaitMs());
            } catch (InterruptedException e) {
                break;
            }
        }
        // 获取所有的服务实例
        Applications apps = eurekaClient.getApplications();
        for (Application app : apps.getRegisteredApplications()) {
            for (InstanceInfo instance : app.getInstances()) {
                try {
                    // 判断是否可注册，主要用于AWS环境下进行，若部署在其他的环境，直接返回true
                    if (isRegisterable(instance)) {
                        // 注册到自身的注册表中
                        register(instance, instance.getLeaseInfo().getDurationInSecs(), true);
                        count++;
                    }
                } catch (Throwable t) {
                    logger.error("During DS init copy", t);
                }
            }
        }
    }
    return count;
}
```

Eureka Server也是一个Eureka Client，在启动的时候也会进行DiscoveryClient的初始化，会从其对应的Eureka Server中拉取全量的注册表信息。

## 循环遍历Peer节点中的注册表,注册到本地服务表

在Eureka Server集群部署的情况下，Eureka Server从它的peer节点中拉取到注册表信息后，将遍历这个Applications，将所有的服务实例通过AbstractRegistry#register方法注册到自身注册表中。

在初始化本地注册表时，Eureka Server并不会接受来自Eureka Client的通信请求(如注册、或者获取注册表信息等请求)。在同步注册表信息结束后会通过PeerAwareInstanceRegistryImpl#openForTraffic方法允许该Server接受流量。代码如下所示：

```java
//PeerAwareInstanceRegistryImpl.java
@Override
public boolean cancel(final String appName, final String id, final boolean isReplication) {
    if (super.cancel(appName, id, isReplication)) {
        // 同步下线状态
        replicateToPeers(Action.Cancel, appName, id, null, null, isReplication);
        ...
    }
    ...
}
public void register(final InstanceInfo info, final boolean isReplication) {
    int leaseDuration = Lease.DEFAULT_DURATION_IN_SECS;
    if (info.getLeaseInfo() != null &amp;&amp; info.getLeaseInfo().getDurationInSecs() 〉 0) {
            leaseDuration = info.getLeaseInfo().getDurationInSecs();
    }
    super.register(info, leaseDuration, isReplication);
    // 同步注册状态
    replicateToPeers(Action.Register, info.getAppName(), info.getId(), info, null, isReplication);
}
public boolean renew(final String appName, final String id, final boolean isReplication) {
    if (super.renew(appName, id, isReplication)) {
        // 同步续约状态
        replicateToPeers(Action.Heartbeat, appName, id, null, null, isReplication);
        return true;
    }
    return false;
}
```

同步的操作主要有：

```
public enum Action {
    Heartbeat, Register, Cancel, StatusUpdate, DeleteStatusOverride;
    ...
}
```

对此需要关注replicateToPeers方法，它将遍历Eureka Server中peer节点，向每个peer节点发送同步请求。代码如下所示：

```java
//PeerAwareInstanceRegistryImpl.java
private void replicateToPeers(Action action, String appName, String id,
                              InstanceInfo info /* optional */,
                              InstanceStatus newStatus /* optional */, boolean isReplication) {
    Stopwatch tracer = action.getTimer().start();
    try {
        if (isReplication) {
            numberOfReplicationsLastMin.increment();
        }
        // 如果peer集群为空，或者这本来就是复制操作，那么就不再复制，防止造成循环复制
        if (peerEurekaNodes == Collections.EMPTY_LIST || isReplication) {
            return;
        }
        // 向peer集群中的每一个peer进行同步
        for (final PeerEurekaNode node : peerEurekaNodes.getPeerEurekaNodes()) {
            // 如果peer节点是自身的话，不进行同步复制
            if (peerEurekaNodes.isThisMyUrl(node.getServiceUrl())) {
                continue;
            }
            // 根据Action调用不同的同步请求
            replicateInstanceActionsToPeers(action, appName, id, info, newStatus, node);
        }
    } finally {
        tracer.stop();
    }
}
```



PeerEurekaNode代表一个可同步共享数据的Eureka Server。在PeerEurekaNode中，具有register、cancel、heartbeat和statusUpdate等诸多用于向peer节点同步注册表信息的操作。
在replicateInstanceActionsToPeers方法中将根据action的不同，调用PeerEurekaNode的不同方法进行同步复制，代码如下所示：

```java
//PeerAwareInstanceRegistryImpl.java
private void replicateInstanceActionsToPeers(Action action, String appName,
    String id, InstanceInfo info, InstanceStatus newStatus, PeerEurekaNode node) {
    try {
        InstanceInfo infoFromRegistry = null;
        CurrentRequestVersion.set(Version.V2);
        switch (action) {
            case Cancel:
            // 同步下线
                node.cancel(appName, id);
                break;
            case Heartbeat:
                InstanceStatus overriddenStatus = overriddenInstanceStatusMap.get(id);
                infoFromRegistry = getInstanceByAppAndId(appName, id, false);
                // 同步心跳
                node.heartbeat(appName, id, infoFromRegistry, overriddenStatus, false);
                break;
            case Register:
                // 同步注册
                node.register(info);
                break;
            case StatusUpdate:
                infoFromRegistry = getInstanceByAppAndId(appName, id, false);
                // 同步状态更新
                node.statusUpdate(appName, id, newStatus, infoFromRegistry);
                break;
            case DeleteStatusOverride:
                infoFromRegistry = getInstanceByAppAndId(appName, id, false);
                node.deleteStatusOverride(appName, id, infoFromRegistry);
                break;
        }
    } catch (Throwable t) {
        logger.error("Cannot replicate information to {} for action {}", node.getServiceUrl(), action.name(), t);
}
```

PeerEurekaNode中的每一个同步复制都是通过批任务流的方式进行操作，同一时间段内相同服务实例的相同操作将使用相同的任务编号，在进行同步复制的时候根据任务编号合并操作，减少同步操作的数量和网络消耗，但是同时也造成同步复制的延时性，不满足CAP中的C(强一致性)。

通过Eureka Server在启动过程中初始化本地注册表信息和Eureka Server集群间的同步复制操作，最终达到了集群中Eureka Server注册表信息一致的目的。