# Jedis连接Cluster原理

> 问题：使用 Jedis 连接 Cluster 的时候，我们只需要连接到任意一个或者多个 redis group 中的实例地址，那我们是怎么获取到需要操作的 Redis Master 实例的？

## 值得注意的是

- 如何存储 slot 和 Redis 连接池的关系。 

## DEMO

```java
public static void main(String[] args) throws IOException {
        // 不管是连主备，还是连几台机器都是一样的效果
        HostAndPort hp4 = new HostAndPort("192.168.8.207",7294);
        HostAndPort hp5 = new HostAndPort("192.168.8.207",7295);
        HostAndPort hp6 = new HostAndPort("192.168.8.207",7296);

        Set nodes = new HashSet<HostAndPort>();
        nodes.add(hp4);
        nodes.add(hp5);
        nodes.add(hp6);

        JedisCluster cluster = new JedisCluster(nodes);
        cluster.set("gupao:cluster", "qingshan2673");
        System.out.println(cluster.get("gupao:cluster"));;
        cluster.close();
    }
```

当使用构造方法进行构造时,循环遍历 startNodes 

```java
//redis.clients.jedis.JedisClusterConnectionHandler#initializeSlotsCache 
private void initializeSlotsCache(Set<HostAndPort> startNodes, GenericObjectPoolConfig poolConfig, String password) {
    //
    for (HostAndPort hostAndPort : startNodes) {
        // 获取一个 Jedis 实例
      Jedis jedis = new Jedis(hostAndPort.getHost(), hostAndPort.getPort());
      if (password != null) {
        jedis.auth(password);
      }
      try {
          // 获取 Redis 节点和 Slot 虚拟槽
        cache.discoverClusterNodesAndSlots(jedis);
        break;
      } catch (JedisConnectionException e) {
          ...
      }
    }
  }
```

- 程序启动初始化集群环境，读取配置文件中的节点配置，无论是主从，无论多少 个，只拿第一个，获取 redis 连接实例（后面有个 break）。

- 用获取的 redis 连接实例执行 clusterSlots ()方法，实际执行 redis 服务端 cluster slots 命令，获取虚拟槽信息。 

```java
//从方法名可以看出，这个方法时用来发现集群节点和槽位信息的  
public void discoverClusterNodesAndSlots(Jedis jedis) {
    //加写锁
    w.lock();

    try {
      reset();
        //获取所有的槽位信息
      List<Object> slots = jedis.clusterSlots();

      for (Object slotInfoObj : slots) {
          //遍历槽位信息
        List<Object> slotInfo = (List<Object>) slotInfoObj;

          //如果小于2 就忽略，证明数据不完整 ②
        if (slotInfo.size() <= MASTER_NODE_INDEX) {
          continue;
        }
		//代码③ getAssignedSlotArray 来获取所有的槽点值
        List<Integer> slotNums = getAssignedSlotArray(slotInfo);

        // hostInfos
        int size = slotInfo.size();
        for (int i = MASTER_NODE_INDEX; i < size; i++) {
          List<Object> hostInfos = (List<Object>) slotInfo.get(i);
          if (hostInfos.size() <= 0) {
            continue;
          }
		//代码 ④ 生成一 个 HostAndPort 对象。
          HostAndPort targetNode = generateHostAndPort(hostInfos);
            //据 HostAndPort 解析出 ip:port 的 key 值，再根据 key 从缓存中查询对应的 jedisPool 实例。如果没有 jedisPool 实例，就创建 JedisPool 实例，最后放入缓存中。nodeKey 和 nodePool 的关系
          setupNodeIfNotExist(targetNode);
            //把 slot 和 jedisPool 缓存起来（16384 个），key 是 slot 下标，value 是连接池
          if (i == MASTER_NODE_INDEX) {
            assignSlotsToNode(slotNums, targetNode);
          }
        }
      }
    } finally {
      w.unlock();
    }
  }

```

- 代码②判断了数组的长度不能小于2 ， 是因为集合在存储的时候，采用了特定位置存储特定信息的方式 [long, long, List, List]
  - 第一个和第二个元素是 该节点负责槽点的起始位置
  - 第三个元素是主节点的信息
  - 第四个元素为主节点对应的从节点的信息
    - 基本信息又是一个数组
      - 第一个元素是host的信息
      - 第二个元素为post信息
      - 第三个元素是唯一id

- 代码③，获取有关节点的槽点信息后，调用 getAssignedSlotArray(slotinfo)来获取所有的槽点值
- 代码④ ，再获取主节点的地址信息，调用 generateHostAndPort(hostInfo)方法，生成一 个 HostAndPort 对象。

- 再根据节点地址信息来设置节点对应的 JedisPool，即设置 Map<String, JedisPool> nodes 的值。 

接下来判断若此时节点信息为主节点信息时，则调用 assignSlotsToNodes 方法，设置每个槽点值对应的连接池，即设置 Map<Integer, JedisPool> slots 的值。

## 从集群环境存取值的方式

从集群环境存取值： 

1、把 key 作为参数，执行 CRC16 算法，获取 key 对应的 slot 值。 

2、通过该 slot 值，去 slots 的 map 集合中获取 jedisPool 实例。 

3、通过 jedisPool 实例获取 jedis 实例，最终完成 redis 数据存取工作。