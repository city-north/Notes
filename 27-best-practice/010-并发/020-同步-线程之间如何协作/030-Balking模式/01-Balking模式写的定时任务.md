# 01-Balking模式写的定时任务

[TOC]

## Balking 模式

 [020-Balking模式：再谈线程安全的单例模式.md](../../../04-java/03-Java并发编程/01-tutorials/032-并发设计模式2/020-Balking模式：再谈线程安全的单例模式.md) 

上一篇文章中，我们提到可以用“多线程版本的 if”来理解 Guarded Suspension 模式，不同于单线程中的 if，这个“多线程版本的 if”是需要等待的，而且还很执着，必须要等到条件为真。

但很显然这个世界，不是所有场景都需要这么执着，有时候我们还需要快速放弃。

需要快速放弃的一个最常见的例子是各种编辑器提供的自动保存功能。自动保存功能的实现逻辑一般都是隔一定时间自动执行存盘操作，存盘操作的前提是文件做过修改，如果文件没有执行过修改操作，就需要快速放弃存盘操作。

## DEMO

```java
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * <p>
 * Balking模式写的定时任务：保证线程安全安全
 * </p>
 *
 * @author Jonathan Chen
 */
public class BalkingTemplate {
    /**
     * Value: 路由集合
     */
    ConcurrentHashMap<String, CopyOnWriteArraySet<Router>> rt = new ConcurrentHashMap<>();
    /**
     * 路由表是否发生变化
     */
    volatile boolean changed;
    /**
     * 是否初始化
     */
    boolean inited = false;
    /**
     * 将路由表写入本地文件的线程池
     */
    ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

    /**
     * 启动定时任务
     * 将变更后的路由表写入本地文件
     */
    public void startLocalSaver() {
        ses.scheduleWithFixedDelay(() -> {
            autoSave();
        }, 1, 1, MINUTES);
    }

    synchronized void init() {
        if (inited) {
            return;
        }
        // 省略 doInit 的实现
        doInit();
        inited = true;
    }

    private void doInit() {
    }


    /**
     * 保存路由表到本地文件
     */
    public void autoSave() {
        if (!changed) {
            return;
        }
        changed = false;
        // 将路由表写入本地文件
        // 省略其方法实现
        this.save2Local();
    }

    private void save2Local() {

    }

    private static class Router {
        String iface;

    }

    /**
     * 删除路由
     */
    public void remove(Router router) {
        Set<Router> set = rt.get(router.iface);
        if (set != null) {
            set.remove(router);
            // 路由表已发生变化
            changed = true;
        }
    }

    /**
     * 增加路由
     */
    public void add(Router router) {
        Set<Router> set = rt.computeIfAbsent(router.iface, r ->
                new CopyOnWriteArraySet<>());
        set.add(router);
        // 路由表已发生变化
        changed = true;
    }

}
```

