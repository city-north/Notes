# 040-Redis分布式方案-RedisCluster

[TOC]

## 什么是Redis集群

Redis集群是Redis提供的分布式数据库防范, 集群通过分片 sharding 来进行数据共享, 并提供复制和故障转移功能

Redis Cluster 是在 Redis 3.0 的版本正式推出的，用来解决分布式的需求，同时也可以实现高可用。跟 Codis 不一样，它是去中心化的，客户端可以连接到任意一个可用节点。

## RedisCluster的槽

RedisCluster 有16384个槽, 每个节点负责其中一部分槽位, 槽位的信息存储于每个节点中, 它不像Codis, 不需要另外的分布式存储空间来存储节点槽位信息

- 当客户端连接到集群时, 也会得到一份集群的槽位配置信息,
- 当客户端向要查找某个key的时候, 可以直接定位目标的节点

## 如何纠正客户端槽位信息差异

客户端为了可以直接定位某个具体的key所在的节点, 需要缓存槽位相关的信息, 这样才可以准确地快速定位到相应的节点, 

同时因为可能会存在客户端与服务器存储槽位的信息不一致的情况, 需要一个纠正机制

## RedisCluster图示

Redis Cluster 可以看成是由多个 Redis 实例组成的数据集合。客户端不需要关注数 据的子集到底存储在哪个节点，只需要关注这个集合整体。

以 3 主 3 从为例，节点之间两两交互，共享数据分片、节点状态等信息。





<img src="../../../../assets/image-20200321212539947.png" alt="image-20200321212539947" style="zoom: 50%;">

## 数据分片有几个关键的问题需要解决

- 数据怎么相对均匀地分片 
- 客户端怎么访问到相应的节点和数据 
- 重新分片的过程，怎么保证正常服务

## 集群命令

| 类型     | 命令                                                         |
| -------- | ------------------------------------------------------------ |
| 集群     | cluster info :打印集群的信息<br/>cluster nodes :列出集群当前已知的所有节点(node)，以及这些节点的相关信息。 |
| 节点     | cluster meet <ip> <port> :将 ip 和 port 所指定的节点添加到集群当中，让它成为集群的一份子。 cluster forget <node_id> :从集群中移除 node_id 指定的节点(保证空槽道)。<br/>cluster replicate <node_id> :将当前节点设置为 node_id 指定的节点的从节点。<br/>cluster saveconfig :将节点的配置文件保存到硬盘里面。 |
| 槽(slot) | cluster addslots <slot> [slot ...] :将一个或多个槽(slot)指派(assign)给当前节点。<br/>cluster delslots <slot> [slot ...] :移除一个或多个槽对当前节点的指派。<br/>cluster flushslots :移除指派给当前节点的所有槽，让当前节点变成一个没有指派任何槽的节点。 cluster setslot <slot> node <node_id> :将槽 slot 指派给 node_id 指定的节点，如果槽已经指派给另一个 节点，那么先让另一个节点删除该槽>，然后再进行指派。<br/>cluster setslot <slot> migrating <node_id> :将本节点的槽 slot 迁移到 node_id 指定的节点中。<br/>cluster setslot <slot> importing <node_id> :从 node_id 指定的节点中导入槽 slot 到本节点。<br/>cluster setslot <slot> stable :取消对槽 slot 的导入(import)或者迁移(migrate)。 |
| 键       | cluster keyslot <key> :计算键 key 应该被放置在哪个槽上。<br/>cluster countkeysinslot <slot> :返回槽 slot 目前包含的键值对数量。 cluster getkeysinslot <slot> <count> :返回 count 个 slot 槽中的键 |