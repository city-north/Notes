package cn.eccto.study.java.concurrent.art.chapter04;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/03/06 18:15
 */
public class MultiThread {
    public static void main(String[] args) {
        // 获取Java线程管理MXBean
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        // 不需要获取同步的monitor和synchronizer信息，仅仅获取线程和线程堆栈信息
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        // 遍历线程信息，仅打印线程ID和线程名称信息
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println("[" + threadInfo.getThreadId() + "] " + threadInfo.getThreadName());
        }
    }
}
//[7] JDWP Command Reader
//[6] JDWP Event Helper Thread
//[5] JDWP Transport Listener: dt_socket
//[4] Signal Dispatcher
//[3] Finalizer
//[2] Reference Handler
//[1] main