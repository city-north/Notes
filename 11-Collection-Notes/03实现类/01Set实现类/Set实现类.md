# Set实现类

Set实现类分为通用实现和特殊实现

## 通用的Set实现类

- `HashSet`：速度快于`TreeSet`（大多数操作的各部分恒定时间与对数时间），无序
  - 使用场景：无序存储，快速存取。元素唯一
- `TreeSet`：实现`SortedSet`里面的操作、有序
  - 使用场景：用来对象元素进行排序,同样保证元素的唯一
- `LinkedHashSet`：使用哈希表+链表的方式实现，它提供插入排序迭代。
  - 实现备注：继承自`HashSet`,内部使用的是`LinkHashMap`,迭代访问性能优于`HashSet`,插入时效率略低于`HashSet`
  - 使用场景：需要使用插入顺序进行遍历的时候。

## 特殊Set实现类

- `EnumSet`：为Enum类型数据设计的高性能Set,所有的Set必须具
- `CopyOnWriteArraySet`：在`add`、`set`、`remove`都会对原Array进行拷贝后进行。无需上锁，此实现适合很少修改但是经常迭代的情况。

