package cluster;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ClusterTest {
    public static void main(String[] args) throws IOException {
        // 不管是连主备，还是连几台机器都是一样的效果
/*        HostAndPort hp1 = new HostAndPort("192.168.8.207",7291);
        HostAndPort hp2 = new HostAndPort("192.168.8.207",7292);
        HostAndPort hp3 = new HostAndPort("192.168.8.207",7293);*/
        HostAndPort hp4 = new HostAndPort("192.168.8.207",7294);
        HostAndPort hp5 = new HostAndPort("192.168.8.207",7295);
        HostAndPort hp6 = new HostAndPort("192.168.8.207",7296);

        Set nodes = new HashSet<HostAndPort>();
/*        nodes.add(hp1);
        nodes.add(hp2);
        nodes.add(hp3);*/
        nodes.add(hp4);
        nodes.add(hp5);
        nodes.add(hp6);

        JedisCluster cluster = new JedisCluster(nodes);
        cluster.set("gupao:cluster", "qingshan2673");
        System.out.println(cluster.get("gupao:cluster"));;
        cluster.close();
    }
}
