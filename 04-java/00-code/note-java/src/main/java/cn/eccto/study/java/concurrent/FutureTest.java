package cn.eccto.study.java.concurrent;

import java.util.concurrent.*;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan 2020/07/27 23:33
 */
public class FutureTest {
    /**
     * ① 创建了一个单线程和一个队列元素个数为 1 的线程池,拒绝策略为 DiscardPolicy
     */
    private static final ThreadPoolExecutor executorService = new ThreadPoolExecutor(1,
            1,
            1L,
            TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(1),
            new ThreadPoolExecutor.DiscardPolicy());


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Future futureOne = null;
        Future future2 = null;
        //②添加任务 one. 并且这个任务会由 2 唯一的线程来执行,并打印后阻塞该线程 5 秒
        futureOne = executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("start runnable one");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        // ③ 想线程池提交了一个任务 two , 这个时候会把任务 two 放入阻塞队列
        future2 = executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("start runnable two");
            }
        });
        // ④ 向线程池提交任务 three , 由于队列已满,所以触发拒绝策略丢弃任务 Three
        Future futureThree = null;
        try {
            futureThree = executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("start runnable three");
                }
            });
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        System.out.println("task 1" + futureOne.get());
        System.out.println("task 2" + future2.get());
        System.out.println("task 3" + (futureThree == null ? null : futureThree.get()));

        executorService.shutdown();
    }
}
