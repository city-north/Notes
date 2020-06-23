package cn.eccto.study.java.concurrent.create;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/06/23 21:18
 */
public class MyCallable implements Callable<String> {
    private String name;

    public MyCallable(String name) {
        this.name = name;
    }

    @Override
    public String call() throws Exception {
        return name;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //step 2
        ExecutorService pool = Executors.newFixedThreadPool(5);
        //step 3
        List<Future> list = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            Callable callable = new MyCallable(i+"Ec");
            final Future submit = pool.submit(callable);
            list.add(submit);
        }
//        Thread.sleep(1000);
        pool.shutdown();
        for (Future future : list) {
            System.out.println(future.get());
        }

    }
}
