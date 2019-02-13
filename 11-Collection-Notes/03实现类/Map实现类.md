## Map实现类

Map实现类包括通用实现、特殊实现、同步实现

## 通用实现

- `HashMap`，Hash算法实现，效率高，无序

- `TreeMap`：实现`SortMap接口`，红黑树算法实现，效率比HashMap低，默认按照插入顺序排序，可以自定义按照访问顺序排序。插入、删除需要维护平衡会牺牲一些效率

- `LinkedHashMap`：与HashMap效率接近，LinkedHashMap 继承自 HashMap，在 HashMap 基础上，通过维护一条双向链表，解决了 HashMap 不能随时保持遍历顺序和插入顺序一致的问题，LinkedHashMap 对访问顺序也提供了相关支持。


## 特殊实现

- [`EnumMap`](https://docs.oracle.com/javase/8/docs/api/java/util/EnumMap.html)内部实现一个array，是一个高性能枚举Key的map，如果希望将枚举映射到值，应该始终优先使用枚举映射而不是数组。
-  [`WeakHashMap`](https://docs.oracle.com/javase/8/docs/api/java/util/WeakHashMap.html)`WeakHashMap`是一个`Map`接口的实现，只存储对其键的弱引用。仅存储弱引用允许键值对在其键不再被引用时被垃圾收集`WeakHashMap`。此类提供了利用弱引用功能的最简单方法。它对于实现“类似注册表”的数据结构很有用，其中当任何线程不再可以访问其Key时，value会消失。
-  [`IdentityHashMap`](https://docs.oracle.com/javase/8/docs/api/java/util/IdentityHashMap.html)：与HashMap的区别在于，`IdentityHashMap`使用== 比较key的值，而HashMap使用的是equals,此类对于保存拓扑的对象图转换(如序列化或深度复制)非常有用。另外性能很快



## 同步实现 

实现了[`ConcurrentMap`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentMap.html) 接口

- `ConcurrentHashMap` ，使用锁分段技术，支持并发访问与修改，它是一个高度并发、高性能的实现，此实现在执行检索时不会阻塞，并允许客户端选择更新的并发级别