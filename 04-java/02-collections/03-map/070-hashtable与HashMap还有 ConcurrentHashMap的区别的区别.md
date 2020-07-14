# HashTable vs HashMap vs ConcurrentHashMap

#### 如何让 HashMap 线程安全

## 让 HashMap 线程安全的方式

- [使用`Collections.synchronizedMap(Map)` 创建线程安全的map集合](#Collections.synchronizedMap(Map))
- [`Hashtable `; 所有方法全部用 `sychrinzied`修饰](#Hashtable)
- [`ConcurrentHashMap`: 分段锁技术](#`ConcurrentHashMap`: 分段锁技术)

#### Collections.synchronizedMap(Map)

> 内部通过包装 Map 并设置一互斥锁,通过同步块锁住对象,属于重量级锁,可以升级

![image-20200714194124502](../../../assets/image-20200714194124502.png)

#### Hashtable

跟HashMap相比Hashtable是线程安全的，适合在多线程的情况下使用，但是效率可不太乐观。

它的所有方法都用`synchronized`进行声明

- Hashtable 是不允许键或值为 null 的

- HashMap 的键值则都可以为 null



#### `ConcurrentHashMap`: 分段锁技术