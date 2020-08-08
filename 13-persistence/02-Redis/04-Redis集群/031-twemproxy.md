# Twemproxy

<img src="../../../assets/image-20200321211807363.png" alt="image-20200321211807363" style="zoom:50%;" />

Twemproxy 的优点:比较稳定，可用性高。

不足:

-  出现故障不能自动转移，架构复杂，需要借助其他组件(LVS/HAProxy +
   Keepalived)实现 HA 
-  扩缩容需要修改配置，不能实现平滑地扩缩容(需要重新分布数据)。