# 集合框架

核心接口封装了不同类型的Collections： 这些接口代表着各自类型的数据接口的操作细节，它们具有以下层级结构。

![Two interface trees.](assets/colls-coreInterfaces.gif)

从图中我们可以看出，Java Collections Framework 层级结构包含两个不同的接口树：

- 第一个树以`Collection`接口开始，它提供了所有集合使用的基本功能，例如`add`和`remove`方法。它的子接口 `Set`，`List`和`Queue`- 提供更专业的集合。
  - `Set`接口不允许重复元素。这对于存储诸如一副纸牌或学生记录之类的集合非常有用。该`Set`接口有一个子接口，`SortedSet`用于对集合中的元素进行排序。
  - `List`接口提供了一个有序的集合，您需要在其中每个元素的插入位置进行精确控制的情况。您可以从a `List`的确切位置检索元素。
  - `Queue`界面可实现额外的插入，提取和检查操作。`Queue`中的元素通常以FIFO为基础进行排序。
  - `Deque`接口可在两端启用插入，删除和检查操作。`Deque`中的元素`Deque`可用于LIFO和FIFO。
- 第二个树以`Map`接口开始，该接口映射与a类似的键和值`Hashtable`。
  - `Map`的子接口，`SortedMap`按升序或按照a指定的顺序维护其键值对`Comparator`。

这些接口允许独立于其表示的细节来操纵集合。

##  [Collection接口](01-interface/01-collection.md) 

Collection 接口时层级结构的根接口,它提供了所有集合都适用的特性,如果其实现类不支持某些特性,会抛出`UnsupportedOperationException`,它继承了 Iterable 接口(提供了迭代集合相关元素的方法)

所有的其他集合都继承自这个接口,**除了 Map**,下面是列表

| Collection子接口                  | 是否有序 | 重复元素 | 备注                                           |
| --------------------------------- | -------- | -------- | ---------------------------------------------- |
| [Set](04-set)                     | 无序     | 不允许   | 按升序维护其元素的集合，比如文字字母、会员名册 |
| [List](02-list/README.md)         | 有序     | 允许     | 代表有顺序的集合元素                           |
| [Queue](07-deque/README.md)       | 有序     | 允许     | FIFO先进先出                                   |
| [Deque](01-interface/02-deque.md) | 有序     | 允许     | FIFO先进先出，LIFO先进后出                     |

## [List](02-list/README.md) 

| List 实现类                                                  | 线程安全 | 备注 |      |
| ------------------------------------------------------------ | -------- | ---- | ---- |
| **[ArrayList](02-list/01-array-list.md)**                    | 否       |      |      |
| [**CopyOnWriteArrayList**](02-list/03-copy-on-write-array-list.md) |          |      |      |
| **[LinkedList](02-list/02-linked-list.md)**                  | 否       |      |      |
| **[Stack](02-list/04-stack.md)**                             |          |      |      |
| [**Vector**](02-list/05-vector.md)                           |          |      |      |

## [Map](03-map/README.md)

| Map子接口                 | 是否有序 |                                                            |
| ------------------------- | -------- | ---------------------------------------------------------- |
| [Map](03-map/README.md)   | 无序     |                                                            |
| SortedMap                 | 有序     | 不允许使用null键或null值，密钥按自然排序或指定的比较器排序 |
| **ConcurrentHashMap**     |          |                                                            |
| **ConcurrentSkipListMap** |          |                                                            |
| **EnumMap**               |          |                                                            |
| **HashMap**               |          |                                                            |
| **Hashtable**             |          |                                                            |
| **IdentityHashMap**       |          |                                                            |
| **LinkedHashMap**         |          |                                                            |
| **Properties**            |          |                                                            |
| **TreeMap**               |          |                                                            |
| **WeakHashMap**           |          |                                                            |

## [Set](04-set/README.md)

| Set 实现类                |      |      |      |
| ------------------------- | ---- | ---- | ---- |
| **ConcurrentSkipListSet** |      |      |      |
| **CopyOnWriteArraySet**   |      |      |      |
| **EnumSet**               |      |      |      |
| **HashSet**               |      |      |      |
| **LinkedHashSet**         |      |      |      |
| **TreeSet**               |      |      |      |

## [Stack](05-stack/README.md)

## [Queue](05-queue/README.md)

| Queue实现类           |      |      |      |
| --------------------- | ---- | ---- | ---- |
| **BlockingQueue**     |      |      |      |
| ArrayDeque            |      |      |      |
| ConcurrentLinkedDeque |      |      |      |
| ConcurrentLinkedQueue |      |      |      |
| DelayQueue            |      |      |      |
| LinkedBlockingDeque   |      |      |      |
| LinkedBlockingQueue   |      |      |      |
| LinkedList            |      |      |      |
| LinkedTransferQueue   |      |      |      |
| PriorityBlockingQueue |      |      |      |
| PriorityQueue         |      |      |      |
| SynchronousQueue      |      |      |      |

## [Deque](05-deque/README.md)

| ArrayDeque            |      |      |      |
| --------------------- | ---- | ---- | ---- |
| ConcurrentLinkedDeque |      |      |      |
| LinkedBlockingDeque   |      |      |      |
| LinkedList            |      |      |      |

