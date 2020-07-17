### conditionObj

>  [050-Condition 接口.md](050-Condition 接口.md) 

ConditionObj 可以直接访问 AQS 对象内部的变量, Condition 是一个条件变量,每个条件变量对应一个条件队列(单向链表队列), 用来存放调用条件变量的 await 方法后被 阻塞的线程

- 条件队列的头为 firstWaiter
- 条件队列的尾元素为 lastWaiter

