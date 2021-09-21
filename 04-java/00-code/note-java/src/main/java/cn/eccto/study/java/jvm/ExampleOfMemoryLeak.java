package cn.eccto.study.java.jvm;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 模拟内存泄漏
 * </p>
 *
 * @author Jonathan 2020/04/28 19:57
 */
public class ExampleOfMemoryLeak {

    //声明缓存对象
    private static final Map map = new HashMap();

    public static void main(String[] args) {
        try {
            Thread.sleep(10000);//给打开 visualvm 时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //循环添加对象到缓存
        for (int i = 0; i < 1000000; i++) {
            TestMemory testMemory = new TestMemory();
            map.put("key" + i, testMemory);
        }
        System.out.println("first");
        //为dump出堆提供时间
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 1000000; i++) {
            TestMemory t = new TestMemory();
            map.put("key" + i, t);
        }
        System.out.println("second");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 3000000; i++) {
            TestMemory t = new TestMemory();
            map.put("key" + i, t);
        }
        System.out.println("third");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 4000000; i++) {
            TestMemory t = new TestMemory();
            map.put("key" + i, t);
        }
        System.out.println("forth");
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("qqqq");

    }
}
