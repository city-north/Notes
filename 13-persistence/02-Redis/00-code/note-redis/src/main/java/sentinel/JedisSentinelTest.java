package sentinel;

import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: qingshan
 * @Date: 2019/9/24 14:50
 * @Description: 咕泡学院，只为更好的你
 */
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
