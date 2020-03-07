package cn.eccto.study.java.concurrent.art.chapter04.connectionPoolDemo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 连接池测试
 * </p>
 *
 * @author EricChen 2020/03/07 22:04
 */
public class ConnectionPoolTest {
    private static ConnectionPool connectionPool = new ConnectionPool(10);
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    static CountDownLatch end;


    public static void main(String[] args) throws InterruptedException {
        // 线程数量，可以线程数量进行观察
        int threadCount = 50;
        end = new CountDownLatch(threadCount);
        int count = 20;
        AtomicInteger got = new AtomicInteger();
        AtomicInteger notGot = new AtomicInteger();
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new ConnectionRunner(count, got, notGot), "ConnectionRunnerThread");
            thread.start();
        }
        countDownLatch.countDown();
        end.await();
        System.out.println("total invoke: " + (threadCount * count));
        System.out.println("got connection:  " + got);
        System.out.println("not got connection " + notGot);

    }


    static class ConnectionRunner implements Runnable {
        private int count;
        private AtomicInteger got;
        private AtomicInteger notGot;


        public ConnectionRunner(int count, AtomicInteger got, AtomicInteger notGot) {
            this.count = count;
            this.got = got;
            this.notGot = notGot;
        }

        @Override
        public void run() {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (count > 0) {
                // 从线程池中获取连接，如果1000ms内无法获取到，将会返回null
                // 分别统计连接获取的数量got和未获取到的数量notGot
                try {
                    Connection connection = connectionPool.fetchConnection(1000);
                    if (connection == null) {
                        notGot.incrementAndGet();
                    } else {
                        try {
                            connection.createStatement();
                            connection.commit();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            connectionPool.releaseConnection(connection);
                            got.incrementAndGet();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    count--;
                }
            }
            end.countDown();
        }
    }


}
