# 070-Eureka服务端-服务下线

[TOC]

## 一言蔽之

Eureka Client在应用销毁时，会向Eureka Server发送服务下线请求，清除注册表中关于本应用的租约，避免无效的服务调用。

## 服务下线流程

在服务剔除的过程中，也是通过服务下线的逻辑完成对单个服务实例过期租约的清除工作。

服务下线的主要实现代码位于AbstractInstanceRegistry#internalCancel方法中，仅需要服务实例的服务名和服务实例id即可完成服务下线。

具体代码如下所示：

```java
// AbstractInstanceRegistry.java
@Override
public boolean cancel(String appName, String id, boolean isReplication) {
    return internalCancel(appName, id, isReplication);
}
protected boolean internalCancel(String appName, String id, boolean isReplication) {
    try {
        // 获取读锁，防止被其他线程进行修改
        read.lock();
        CANCEL.increment(isReplication);
        // 根据appName获取服务实例的集群
        Map〈String, Lease〈InstanceInfo〉〉 gMap = registry.get(appName);
        Lease〈InstanceInfo〉 leaseToCancel = null;
        // 移除服务实例的租约
        if (gMap != null) {
            leaseToCancel = gMap.remove(id);
        }
        // 将服务实例信息添加到最近下线服务实例统计队列
        synchronized (recentCanceledQueue) {
recentCanceledQueue.add(new Pair〈Long, String〉(System.currentTimeMillis(), appName + "(" + id + ")"));
        }
        // 租约不存在，返回false
        if (leaseToCancel == null) {
            CANCEL_NOT_FOUND.increment(isReplication);
            return false;
        } else {
           // 设置租约的下线时间
            leaseToCancel.cancel();
            InstanceInfo instanceInfo = leaseToCancel.getHolder();
            ...
            if (instanceInfo != null) {
                instanceInfo.setActionType(ActionType.DELETED);
                // 添加最近租约变更记录队列，标识ActionType为DELETED
                // 这将用于Eureka Client增量式获取注册表信息
							 recentlyChangedQueue.add(new RecentlyChangedItem(leaseToCancel));
                instanceInfo.setLastUpdatedTimestamp();
            }
            // 设置response缓存过期
            invalidateCache(appName, vip, svip);
            // 下线成功
            return true;
        }
    } finally {
        // 释放锁
        read.unlock();
    }
}
```

internalCancel方法与register方法的行为过程很类似，

- 首先通过registry根据服务名和服务实例id查询关于服务实例的租约Lease是否存在，统计最近请求下线的服务实例用于Eureka Server主页展示。
  - 如果租约不存在，返回下线失败；
  - 如果租约存在，从registry注册表中移除，设置租约的下线时间，同时在最近租约变更记录队列中添加新的下线记录，以用于Eureka Client的增量式获取注册表信息，最后设置repsonse缓存过期。

internalCancel方法中同样通过读锁保证registry注册表中数据的一致性，避免脏读。