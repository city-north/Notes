package cn.eccto.study.java.concurrent.future;

import java.util.concurrent.*;

/**
 * description
 *
 * @author chen 2022/02/22 20:59
 */
public class FutureDemo {

    ExecutorService executor = Executors.newFixedThreadPool(10);
    ArchiveSearcher searcher = (target) -> {
        System.out.println("搜索目标中: " + target);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "搜索到了";
    };

    public static void main(String[] args) throws InterruptedException {
        FutureDemo futureDemo = new FutureDemo();
        futureDemo.showSearch("123");
    }

    public void showSearch(final String target) throws InterruptedException {
        Future<String> future = executor.submit(() -> searcher.search(target));
        displayOtherThings(); // do other things while searching
        try {
            String s = future.get();// use future
            System.out.println(s);
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }
        executor.shutdown();
    }

    private void displayOtherThings() {
        System.out.println("异步做其他事儿");
    }
}

interface ArchiveSearcher {
    String search(String target);
}