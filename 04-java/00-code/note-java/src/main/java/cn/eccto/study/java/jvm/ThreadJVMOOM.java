package cn.eccto.study.java.jvm;

/**
 * <p>
 * 创建线程导致内存溢出异常
 * 原因:
 * </p>
 *
 * @author EricChen 2020/05/02 17:51
 */
public class ThreadJVMOOM {

    private void dontStop() {
        while (true) {

        }
    }

    private void stackLeakByThread(){
        while (true) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dontStop();
                }
            }).start();
        }
    }

    public static void main(String[] args) {
        ThreadJVMOOM threadJVMOOM = new ThreadJVMOOM();
        threadJVMOOM.stackLeakByThread();
    }
}
