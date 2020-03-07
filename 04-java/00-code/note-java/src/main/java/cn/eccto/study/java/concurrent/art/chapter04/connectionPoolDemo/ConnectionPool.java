package cn.eccto.study.java.concurrent.art.chapter04.connectionPoolDemo;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * <p>
 * 连接池
 * </p>
 *
 * @author EricChen 2020/03/07 21:36
 */
public class ConnectionPool {

    private LinkedList<Connection> pool = new LinkedList<>();

    public ConnectionPool(int initialSize) {
        if (initialSize > 0) {
            for (int i = 0; i < initialSize; i++) {
                pool.add(ConnectionDriver.createConnection());
            }
        }
    }

    /**
     * 将数据源连接释放
     */
    public void releaseConnection(Connection connection) {
        if (connection == null) {
            return;
        }
        synchronized (pool) {
            //连接释放后需要进行通知,这样其他消费者就能感知到连接池中已经归还了一个链接
            pool.addLast(connection);
            pool.notifyAll();
        }
    }

    /**
     * 获取连接
     *
     * @param mills 等待超时时间
     * @return 连接
     */
    public Connection fetchConnection(long mills) throws InterruptedException {
        synchronized (pool) {
            if (mills < 0) {
                while (pool.isEmpty()) {
                    pool.wait();
                }
                return pool.removeFirst();
            } else {
                long future = System.currentTimeMillis() + mills;
                long remaining = mills;
                while (pool.isEmpty() && remaining > 0) {
                    pool.wait(remaining);
                    remaining = future - System.currentTimeMillis();
                }
                Connection result = null;
                if (!pool.isEmpty()) {
                    result = pool.removeFirst();
                }
                return result;
            }
        }
    }

}
