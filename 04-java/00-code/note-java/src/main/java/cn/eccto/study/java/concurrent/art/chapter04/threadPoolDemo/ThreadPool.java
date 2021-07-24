package cn.eccto.study.java.concurrent.art.chapter04.threadPoolDemo;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan 2020/03/07 23:07
 */
public interface ThreadPool<T extends Thread> {

    /**
     * 执行 job
     *
     * @param job 任务
     */
    void execute(T job);

    /**
     * 关闭线程池
     */
    void shutdown();

    /**
     * 添加工作者
     *
     * @param num 工作者个数
     */
    void addWorker(int num);


    /**
     * 减少工作者个数
     *
     * @param num 工作者个数
     */
    void removeWorker(int num);

    /**
     * @return 正在等待的 job 的大小
     */
    int getJobSize();
}
