# 010-Guarded Suspension (等待唤醒) 模式实现模板

[TOC]

## DEMO

 [010-Guarded Suspension模式：等待唤醒机制的规范实现.md](../../../04-java/03-Java并发编程/01-tutorials/032-并发设计模式2/010-Guarded Suspension模式：等待唤醒机制的规范实现.md) 

```java
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

/**
 * <p>
 * Guarded Suspension (等待唤醒) 模式实现模板 ,
 * 使用场景:
 * </p>
 * 1. 线程1 想要调用一个服务, 然后等待服务的响应才能继续
 * 2. 线程2 触发对象的改变,从而使得线程1 同步去获取到
 *
 * @author Jonathan Chen
 */
public class GuardedObject<T> {
    /**
     * 唤醒对象
     */
    private T guardedObject;

    private final Lock lock = new ReentrantLock();

    private final Condition done = lock.newCondition();
    /**
     * 超时时间
     */
    private final int timeout = 2;
    /**
     * 保存所有 GuardedObject
     */
    private final static Map<Object, GuardedObject> gos = new ConcurrentHashMap<>();


    /**
     * 静态方法创建 GuardedObject
     *
     * @param key 受保护对象的标识
     * @param <K> Key类型
     */
    public static <K, T> GuardedObject<T> create(K key) {
        GuardedObject<T> go = new GuardedObject<>();
        gos.put(key, go);
        return go;
    }

    /**
     * 触发事件
     */
    static <K, T> void fireEvent(K key, T obj) {
        GuardedObject<T> go = gos.remove(key);
        if (go != null) {
            go.onChanged(obj);
        }
    }

    /**
     * 获取受保护的对象
     *
     * @param predicate 谓语
     * @return 对象
     */
    private T get(Predicate<T> predicate) {
        lock.lock();
        try {
            //MESA 管程模型
            while (!predicate.test(guardedObject)) {
                done.await(timeout, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            //TODO handle Exception
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        // 返回非空的受保护对象
        return guardedObject;
    }

    /**
     * 通过外部触发保护对象的状态改变
     *
     * @param obj 唤醒的保护对象
     */
    public void onChanged(T obj) {
        lock.lock();
        try {
            this.guardedObject = obj;
            done.signalAll();
        } finally {
            lock.unlock();
        }
    }


    private static class Message {
    }

    private static class Test {

        /**
         * 处理浏览器请求
         *
         * @return
         */
        public static String handleWebReq() {
//            int id = 序号生成器.get();
//            创建一消息
//            Msg msg1 = new Msg(id, "{...}");
            // 创建 GuardedObject 实例
            GuardedObject<Message> go = GuardedObject.create("id1");
            // 发送消息
//            send(msg1);
            // 等待 MQ 消息
            Message r = go.get(t -> t != null);
            return "";
        }

        /**
         * 消息中间件回调
         *
         * @param msg
         */
        public static void onMessage(Message msg) {
            // 唤醒等待的线程
            GuardedObject.fireEvent("id1", msg);
        }

        public static void main(String[] args) {
            //浏览器发送请求,等待消息队列返回后才继续执行
            handleWebReq();
        }

    }
}
```

