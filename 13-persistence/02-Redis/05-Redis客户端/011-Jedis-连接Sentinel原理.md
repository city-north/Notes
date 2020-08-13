## Jedis 连接Sentinel 原理

我们在使用Jedis连接Sentinel的时候，配置的全部是哨兵的地址，Sentinel 是如何返回可用的master 地址的呢？

#### 使用方式

```java
public class JedisSentinelTest {
    private static JedisSentinelPool pool;

    private static JedisSentinelPool createJedisPool() {
        // master的名字是sentinel.conf配置文件里面的名称
        String masterName = "redis-master";
        Set<String> sentinels = new HashSet<String>();
        sentinels.add("192.168.8.203:26379");
        sentinels.add("192.168.8.204:26379");
        sentinels.add("192.168.8.205:26379");
        pool = new JedisSentinelPool(masterName, sentinels);
        return pool;
    }

    public static void main(String[] args) {
        JedisSentinelPool pool = createJedisPool();
        pool.getResource().set("qingshan", "qq"+System.currentTimeMillis());
        System.out.println(pool.getResource().get("qingshan"));
    }
}

```

从代码中可以看出， 我们使用`JedisSentinelPool` 构造了一个Sentinel 连接池

```java
pool = new JedisSentinelPool(masterName, sentinels);
```

最终调用JedisSentinelPool中的初始化方法

```java
//redis.clients.jedis.JedisSentinelPool#JedisSentinelPool    
HostAndPort master = initSentinels(sentinels, masterName);
```

具体实现如下

- 遍历所有Sentinel 节点，根据masterName 进行获取

```java
private HostAndPort initSentinels(Set<String> sentinels, final String masterName) {

    HostAndPort master = null;
    boolean sentinelAvailable = false;

    for (String sentinel : sentinels) {
      final HostAndPort hap = HostAndPort.parseString(sentinel);
      Jedis jedis = null;
      try {
        jedis = new Jedis(hap.getHost(), hap.getPort());
        List<String> masterAddr = jedis.sentinelGetMasterAddrByName(masterName);
        // connected to sentinel...
        sentinelAvailable = true;
        master = toHostAndPort(masterAddr);
          
        break;
      } catch (JedisException e) {
			//
      }
    }
// 到这里，如果 master 为 null，则说明有两种情况，一种是所有的 sentinels 节点都 down 掉了，一种是 master 节点没有被存活的 sentinels 监控到
    for (String sentinel : sentinels) {
      final HostAndPort hap = HostAndPort.parseString(sentinel);
      MasterListener masterListener = new MasterListener(masterName, hap.getHost(), hap.getPort());
      // whether MasterListener threads are alive or not, process can be stopped
      masterListener.setDaemon(true);
      masterListeners.add(masterListener);
      masterListener.start();
    }

    return master;
  }
```

