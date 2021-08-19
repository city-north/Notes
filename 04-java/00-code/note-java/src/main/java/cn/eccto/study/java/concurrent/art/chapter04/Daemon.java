package cn.eccto.study.java.concurrent.art.chapter04;

/**
 * <p>
 * 守护线程
 * </p>
 *
 * @author Jonathan 2020/03/07 15:59
 */
public class Daemon {
    public static void main(String[] args) {
        Thread thread = new Thread(new DaemonRunner());
        thread.setDaemon(true);
        thread.start();
    }
    static class DaemonRunner implements Runnable {
        @Override
        public void run() {
            try {
                SleepUtils.second(100);
            } finally {
                System.out.println("DaemonThread finally run.");
            }
        }
    }
}
