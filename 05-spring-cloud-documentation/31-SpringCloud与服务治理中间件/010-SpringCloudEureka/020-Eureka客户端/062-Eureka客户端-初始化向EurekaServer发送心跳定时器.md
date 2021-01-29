# 062-Eureka客户端-初始化向EurekaServer发送心跳定时器

[TOC]

## 简介

HeartbeatThread同样继承了Runnable接口，该任务的作用是向Eureka Server发送心跳请求，维持Eureka Client在注册表中的租约。代码如下所示：

```java
// DiscoveryClient.java
private class HeartbeatThread implements Runnable {
    public void run() {
        if (renew()) {
lastSuccessfulHeartbeatTimestamp = System.currentTimeMillis();
        }
    }
}
```

HeartbeatThread主要逻辑代码位于#renew方法中，代码如下所示：

```java
//DiscovClient.java
boolean renew() {
    EurekaHttpResponse〈InstanceInfo〉 httpResponse;
    try {
        // 调用HTTP发送心跳到Eureka Server中维持租约
httpResponse = eurekaTransport.registrationClient.sendHeartBeat(instanceInfo.getAppName(), instanceInfo.getId(), instanceInfo, null);
        // Eureka Server中不存在该应用实例，
if (httpResponse.getStatusCode() == 404) {
            REREGISTER_COUNTER.increment();
            // 重新注册
            return register();
        }
        // 续约成功
        return httpResponse.getStatusCode() == 200;
    } catch (Throwable e) {
        return false;
    }
}
```

Eureka Server会根据续租提交的appName与instanceInfoId来更新注册表中的服务实例的租约。

- 当注册表中不存在该服务实例时，将返回404状态码，发送心跳请求的Eureka Client在接收到404状态后将会重新发起注册

- 如果续约成功，将会返回200状态码。

## 续租的方法

跟踪到AbstractJerseyEurekaHttpClient#sendHeartBeat方法中，可以发现服务续租调用的接口以及传递的参数，

续租的接口地址为`apps/${APP_NAME}/${INSTANCE_INFO_ID}`，HTTP方法为put，参数主要有status(当前服务的状态)、lastDirtyTimestamp(上次数据变化时间)以及overriddenStatus。

