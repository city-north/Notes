package cn.eccto.study.java.concurrent.art.chapter08;

import java.util.concurrent.ExecutorService;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/04/05 23:26
 */
public class MonitorThreadPoolExecutorTest implements Runnable {
    private static ExecutorService es = MonitorThreadPoolExecutor.newCachedThreadPool();

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 100; i++) {
            es.execute(new MonitorThreadPoolExecutorTest());
        }
        es.shutdown();
    }
}
