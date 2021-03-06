# 050-Redis-一致性哈希

---

[TOC]

## 什么是一致性Hash

当我们在做数据库分库分表或者是分布式缓存时，不可避免的都会遇到一个问题:

如何将数据均匀的分散到各个节点中，并且尽量的在加减节点时能使**受影响的数据最少**。

- hash取模
- 一致性hash

## hash后取模

将传入的 Key 按照

```
index = hash(key) % N
```

来计算出需要存放的节点。其中 hash 函数是一个将字符串转换为正整数的哈希映射方法，N 就是节点的数量。

这样可以满足数据的均匀分配，但是这个算法的**容错性**和扩展性都较差。

**比如增加或删除了一个节点时，所有的 Key 都需要重新计算，显然这样成本较高，为此需要一个算法满足分布均匀同时也要有良好的容错性和拓展性。**

**数据需要重新分布。** 

所以我们需要一致性hash

## 我们为什么需要一致性Hash

**一致性哈希解决了动态增减节点时，所有数据都需要重新分布的问题，它只会影响 到下一个相邻的节点，对其他节点没有影响。**

## 一致性哈希详细介绍



把所有的哈希值空间组织成一个虚拟的圆环(哈希环)，整个空间按顺时针方向组 织。因为是环形空间，0 和 2^32-1 是重叠的。

假设我们有四台机器要哈希环来实现映射(分布数据)，我们先根据机器的名称或 者 IP 计算哈希值，然后分布到哈希环中(红色圆圈)。

<img src="../../../../assets/image-20200322203528925.png" alt="image-20200322203528925" style="zoom: 67%;" />

现在有 4 条数据或者 4 个访问请求，对 key 计算后，得到哈希环中的位置(绿色圆圈)。沿哈希环顺时针找到的第一个 Node，就是数据存储的节点。

<img src="../../../../assets/image-20200322203546174.png" alt="image-20200322203546174" style="zoom:67%;" />

在这种情况下，新增了一个 Node5 节点，不影响数据的分布。

<img src="../../../../assets/image-20200322203611227.png" alt="image-20200322203611227" style="zoom:67%;" />

删除了一个节点 Node4，只影响相邻的一个节点。

<img src="../../../../assets/image-20200322203622386.png" alt="image-20200322203622386" style="zoom:67%;" />



谷歌的 MurmurHash 就是一致性哈希算法。在分布式系统中，负载均衡、分库分表 等场景中都有应用。

**一致性哈希解决了动态增减节点时，所有数据都需要重新分布的问题，它只会影响 到下一个相邻的节点，对其他节点没有影响。**

但是这样的一致性哈希算法有一个缺点，因为节点不一定是均匀地分布的，特别是 在节点数比较少的情况下，所以数据不能得到均匀分布。解决这个问题的办法是引入虚 拟节点(Virtual Node)。

比如:2 个节点，5 条数据，只有 1 条分布到 Node2，4 条分布到 Node1，不均匀。

<img src="../../../../assets/image-20200322203652964.png" alt="image-20200322203652964" style="zoom:67%;" />

Node1 设置了两个虚拟节点，Node2 也设置了两个虚拟节点(虚线圆圈)。

这时候有 3 条数据分布到 Node1，1 条数据分布到 Node2。

<img src="../../../../assets/image-20200322203708152.png" alt="image-20200322203708152" style="zoom:67%;" />

## 一致性hash实现

- Jedis中的实现
- 其他实现

#### Jedis中的实现

关键代码

```java
//在getResource()中，获取了一个Jedis实例。
//它最终调用了redis.clients.util.Sharded类的initialize()方法。
jedis = jedisPool.getResource();
for(int i=0; i<100; i++){
    jedis.set("k"+i, ""+i);
}
for(int i=0; i<100; i++){
    //获取
    Client client = jedis.getShard("k"+i).getClient();
    System.out.println("取到值："+jedis.get("k"+i)+"，"+"当前key位于：" + client.getHost() + ":" + client.getPort());
}
```

最终 ， getResource()方法会初始化一个树 

```
//redis.clients.util.Sharded#initialize

private void initialize(List<S> shards) {
// 创建一个红黑树
nodes = new TreeMap<Long, S>();
// 把所有Redis节点放到红黑树中
for (int i = 0; i != shards.size(); ++i) {
	final S shardInfo = shards.get(i);
	// 为每个Redis节点创建160个虚拟节点，放到红黑树中
	if (shardInfo.getName() == null) 
		for (int n = 0; n < 160 * shardInfo.getWeight(); n++) {
			nodes.put(this.algo.hash("SHARD-" + i + "-NODE-" + n), shardInfo);
	}
else 
	for (int n = 0; n < 160 * shardInfo.getWeight(); n++) {
		// 对名字计算哈希（MurmurHash），名称格式 SHARD-0-NODE-0
		nodes.put(this.algo.hash(shardInfo.getName() + "*" + shardInfo.getWeight() + n), shardInfo);
}

// 添加到map中，键为ShardInfo，值为redis实例
resources.put(shardInfo, shardInfo.createResource());
	}
}
```

在jedis.getShard(“k”+i).getClient()获取到真正的客户端。

```java
public S getShardInfo(byte[] key) {
    // 获取比当前key的哈希值要大的红黑树的子集
    SortedMap<Long, S> tail = nodes.tailMap(algo.hash(key));
    if (tail.isEmpty()) {
        // 没有比它大的了，直接从nodes中取出
        return nodes.get(nodes.firstKey());
    }
    // 返回第一个比它大的JedisShardInfo
    return tail.get(tail.firstKey());
}
```

