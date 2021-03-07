# 050-Eureka服务端-接受服务心跳

[TOC]

## 一言蔽之

在Eureka Client完成服务注册之后，它需要定时向Eureka Server发送心跳请求(默认30秒一次)，维持自己在Eureka Server中租约的有效性。

## 接受服务心跳核心逻辑

Eureka Server处理心跳请求的核心逻辑位于AbstractInstanceRegistry#renew方法中。

renew方法是对Eureka Client位于注册表中的租约的续租操作，不像register方法需要服务实例信息，仅根据服务实例的服务名和服务实例id即可更新对应租约的有效时间。具体代码如下所示：

```java
// AbstractInstanceRegistry.java
public boolean renew(String appName, String id, boolean isReplication) {
    RENEW.increment(isReplication);
    // 根据appName获取服务集群的租约集合
    Map<String, Lease<InstanceInfo>> gMap = registry.get(appName);
    Lease<InstanceInfo> leaseToRenew = null;
    if (gMap != null) {
    leaseToRenew = gMap.get(id);
    }
    // 租约不存在，直接返回false
    if (leaseToRenew == null) {
        RENEW_NOT_FOUND.increment(isReplication);
        return false;
    } else {
        InstanceInfo instanceInfo = leaseToRenew.getHolder();
        if (instanceInfo != null) {
           // 根据覆盖状态规则得到服务实例的最终状态
            InstanceStatus overriddenInstanceStatus = this.getOverriddenInstanceStatus(instanceInfo, leaseToRenew, isReplication);
            if (overriddenInstanceStatus == InstanceStatus.UNKNOWN) {
                // 如果得到的服务实例最后状态是UNKNOWN，取消续约
								RENEW_NOT_FOUND.increment(isReplication);
                return false;
            }
            if (!instanceInfo.getStatus().equals(overriddenInstanceStatus)) {
                instanceInfo.setStatus(overriddenInstanceStatus);
            }
        }
        // 统计每分钟续租的次数，用于自我保护
        renewsLastMin.increment();
        // 更新租约中的有效时间
        leaseToRenew.renew();
        return true;
    }
}
```

在#renew方法中，不关注InstanceInfo，仅关注于租约本身以及租约的服务实例状态。

- 如果根据服务实例的appName和instanceInfoId查询出服务实例的租约，并且根据#getOverriddenInstanceStatus方法得到的instanceStatus不为InstanceStatus.UNKNOWN，那么更新租约中的有效时间，即更新租约Lease中的lastUpdateTimestamp，达到续约的目的；

- 如果租约不存在，那么返回续租失败的结果。