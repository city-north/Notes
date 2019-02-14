# HashMap

特点：无序、Hash表存放，效率高，允许null键和null值，在计算键的Hash值时，null对应的Hash值时0。

注意点：HashSet的初始容量是16，如果内存不够，容量翻倍。

## 哈希（Hash）

Hash(散列)是一种信息摘要算法，散列函数（Hash Function）是一种从任何一种数据中创建小的数字”指纹“的方法。散列函数把信息或者数据压缩成摘要，使数据量变小，将数据的格式固定下来。该函数将数据打乱混合，重新创建一个叫做**散列值**（hash values，hash codes，hash sums，或hashes）的指纹(维基百科)。

## HashMap的结构

HashMap包含数组、链表、红黑树，桶中的结构可能是链表，也可能是红黑树。

HashMap会根据内容的变化，转化存储方式，以达到最高的效率。



![](assets/616953-20160304192851940-1880633940.png)



#### 几个注意点：

- `HashMap`的默认初始容量是16，
- 负载因子是`0.75f`,也就是说如果`已占容量/总容量  > 0.75`，`HashMap`会进行扩容操作。
- 当hash值相等时，Hashmap会根据Key的值对链表或者红黑树进行查找，使用`equals`

#### 桶的树化阈值

当桶中元素大于8时，就会使用红黑树（O(log n)查找效率）节点替换链表节点。树化阈为8

`static final int TREEIFY_THRESHOLD = 8;`

#### 红黑树还原成链表阈值

当桶中的元素小于6时，就会把红黑树中的节点切换为链表结构。这个值应该比树化阈值小，避免频繁切换。

`static final int UNTREEIFY_THRESHOLD = 6;`

#### 哈希表的最小树形化容量

当Hash表的容量大于这个值是，表中的桶才会进行树化，否则扩容，为了避免进行扩容、树形化选择的冲突，这个值不能小于` 4 * TREEIFY_THRESHOLD`

```
static final int MIN_TREEIFY_CAPACITY = 64;
```

![img](assets/20161126224434590.png)



## HashMap线程非安全的原因

- `Put`方法在多线程情况下会数据不一致
  - 非原子操作，先Hash获取存储地址再进行插入操作，中间有可能被其他线程寻址并插入。
- `resize`时会导致死循环

