# PriorityBlockingQueue

PriorityBlockingQueue 队列在内部使用二叉树堆维护元素优先级, 使用数组作为元素存储的数据结构,这个数组是可扩容的,

如果 **当前元素个数 > = 最大容量** 时, 会通过 CAS 算法扩容, 出队时始终保证出队的元素时堆树的根节点,而不是在队列里面停留时间最长的元素

使用 元素 compareTo 方法提供默认的元素优先级比较规则,用户可以自定义优先级比较规则

![image-20200715133034647](../../../assets/image-20200715133034647.png)

PriorityBlockingQueue 类似于 ArrayBlockingQueue, 在内部使用一个独占锁来控制同时只有一个线程可以入队和出队的操作

- PriorityBlockingQueue 使用一个 notEmpty 条件变量而没有使用 notFull , 这是因为它是无界的,执行 put 操作永远不会出于 await 状态, 所以也不需要被唤醒
- PriorityBlockingQueue 的 take 方法是阻塞方法,并且是可能被中断的,当需要存放有优先级的元素时该队列比较有用

 