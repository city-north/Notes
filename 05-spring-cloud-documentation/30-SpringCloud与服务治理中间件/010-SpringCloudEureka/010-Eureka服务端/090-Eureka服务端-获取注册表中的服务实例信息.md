# 090-Eureka服务端-获取注册表中的服务实例信息

[TOC]

## 一言蔽之

Eureka Server中获取注册表的服务实例信息主要通过两个方法实现：

- `AbstractInstanceRegistry#getApplicationsFromMultipleRegions`从多地区获取全量注册表数据，
- `AbstractInstanceRegistry#getApplicationDeltasFromMultipleRegions`从多地区获取增量式注册表数据

## 从多地区获取全量注册表数据getApplicationsFromMultipleRegions

getApplicationsFromMultipleRegions方法将会从多个地区中获取全量注册表信息，并封装成Applications返回，实现代码如下所示：

```java
//AbstractInstanceRegistry.java
public Applications getApplicationsFromMultipleRegions(String[] remoteRegions) {
    boolean includeRemoteRegion = null != remoteRegions && remoteRegions.length != 0;
    Applications apps = new Applications();
    apps.setVersion(1L);
    // 从本地registry获取所有的服务实例信息InstanceInfo
    for (Entry<String, Map<String, Lease<InstanceInfo>>> entry : registry.entrySet()) {
        Application app = null;
        if (entry.getValue() != null) {
            for (Entry<String, Lease<InstanceInfo>> stringLeaseEntry : entry.
                getValue().entrySet()) {
                Lease<InstanceInfo> lease = stringLeaseEntry.getValue();
                if (app == null) {
                    app = new Application(lease.getHolder().getAppName());
                }
                app.addInstance(decorateInstanceInfo(lease));
            }
        }
        if (app != null) {
            apps.addApplication(app);
        }
    }
    if (includeRemoteRegion) {
        // 获取远程Region中的Eureka Server中的注册表信息
        ...
    }
    apps.setAppsHashCode(apps.getReconcileHashCode());
    return apps;
}
```

它首先会将本地注册表registry中的所有服务实例信息提取出来封装到Applications中，再根据是否需要拉取远程Region中的注册表信息，将远程Region的Eureka Server注册表中的服务实例信息添加到Applications中。最后将封装了全量注册表信息的Applications返回给Client。

## 从多地区获取增量式注册表数据getApplicationDeltasFromMultipleRegions

getApplicationDeltasFromMultipleRegions方法将会从多个地区中获取增量式注册表信息，并封装成Applications返回，实现代码如下所示：

```java
//AbstractInstanceRegistry.java
public Applications getApplicationDeltasFromMultipleRegions(String[] remoteRegions) {
    if (null == remoteRegions) {
        remoteRegions = allKnownRemoteRegions; // null means all remote regions.
    }
    boolean includeRemoteRegion = remoteRegions.length != 0;
    Applications apps = new Applications();
    apps.setVersion(responseCache.getVersionDeltaWithRegions().get());
    Map<String, Application> applicationInstancesMap = new HashMap<String, Application>();
    try {
        write.lock();// 开启写锁
        // 遍历recentlyChangedQueue队列获取最近变化的服务实例信息InstanceInfo
        Iterator<RecentlyChangedItem> iter = this.recentlyChangedQueue.iterator();
        while (iter.hasNext()) {
            //...()) {
            //...
        }
        if (includeRemoteRegion) {
            // 获取远程Region中的Eureka Server的增量式注册表信息
           ...
        } finally {
        write.unlock();
    }
    // 计算应用集合一致性哈希码，用以在Eureka Client拉取时进行对比
    apps.setAppsHashCode(apps.getReconcileHashCode());
    return apps;
}
```

获取增量式注册表信息将会从recentlyChangedQueue中获取最近变化的服务实例信息。recentlyChangedQueue中统计了近3分钟内进行注册、修改和剔除的服务实例信息，在服务注册AbstractInstanceRegistry#registry、接受心跳请求AbstractInstanceRegistry#renew和服务下线

`AbstractInstanceRegistry#internalCancel`等方法中均可见到recentlyChangedQueue对这些服务实例进行登记，用于记录增量式注册表信息。`#getApplicationsFromMultipleRegions`方法同样提供了从远程Region的Eureka Server获取增量式注册表信息的能力。