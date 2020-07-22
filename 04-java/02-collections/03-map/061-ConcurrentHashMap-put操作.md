# ConcurrentHashMap - put操作

## 总体步骤

## 源码

```java
    public V put(K key, V value) {
        return putVal(key, value, false);
    }
```

## put方法第一阶段-初始化

- 判断是否非空, 可以看到 hashMap key 和 value 都不能为空
- ① 根据对象的 hashCode 取模获取在数组中的位置
- ② 自旋
- ③ 如果 列表 table 为空,进入**初始化阶段**
  - [initTable](#initTable) 初始化列表
  - [addCount](#addCount) 将 count的值累加 
- [put方法第二阶段-累加元素计数器](#put方法第二阶段-累加元素计数器)

```java
    final V putVal(K key, V value, boolean onlyIfAbsent) {
        if (key == null || value == null) throw new NullPointerException();
      	//① 根据对象的 hashCode 取模获取在数组中的位置
        int hash = spread(key.hashCode());
        int binCount = 0;
      // ②  自旋
        for (Node<K,V>[] tab = table;;) {
            Node<K,V> f; int n, i, fh;
            if (tab == null || (n = tab.length) == 0)
              //③ 如果 列表 table 为空,则初始化列表
                tab = initTable();
          // 通过 hash 值对应的数组下标得到第一个节点; 以 volatile 读的方式来读取 table 数组中的元素，保证每次拿到的数据都是最新的
            else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
              //如果该下标返回的节点为空，则直接通过 cas 将新的值封装成 node 插入即可;如果 cas 失败，说明存在竞争，则进入下一次循环
                if (casTabAt(tab, i, null, new Node<K,V>(hash, key, value, null)))
                    break;                   // no lock when adding to empty bin
            }
            else if ((fh = f.hash) == MOVED)
                tab = helpTransfer(tab, f);
            else {
              //
            }
        }
      //累加计数器
        addCount(1L, binCount);
        return null;
    }

```

#### spread

- (h >>> 16) :  hashcode 无符号右移 16 位, 即高 16 位
- 使用 hashcode 异或低 16 位(不同为 1 ) 自己的高 16 位 ,这样的目的是为了降低重复的可能性

```java
   static final int spread(int h) {
        return (h ^ (h >>> 16)) & HASH_BITS;
    }
```





#### initTable

##### sizeCtl的含义

这个标志是在 Node 数组初始化或者扩容的时候的一个控制位标识，**负数代表正在进行初始化或者扩容操作**

- -1 代表正在初始化
- -N 代表有 N-1 个线程正在进行扩容操作，这里不是简单的理解成 n 个线程，sizeCtl 就是 -N
- 0 标识 Node 数组还没有被初始化
- 正数代表初始化或者下一次扩容的大小

##### 步骤

- ① 通过 CAS 判断 SIZECTL 变量是否为 0 , 0 则改为 -1
- ② 再次判断 列表 table 为空 ,如果 sc 大于 0 则使用 sc 作为初始化容量, 否则使用 16
- ③ 计算下次扩容的大小，实际就是当前容量的 0.75 倍，这里使用了右移来计算
- ④ 将下次扩容的大小赋值给 sizeCTL
- ⑤ 可能在初始化,也可能在扩容 ,则直接出让 CPU 时间片

```java
    private final Node<K,V>[] initTable() {
        Node<K,V>[] tab; int sc;
        while ((tab = table) == null || tab.length == 0) {
            if ((sc = sizeCtl) < 0)
              // ⑤ 可能在初始化,也可能在扩容 ,则直接出让 CPU 时间片
                Thread.yield(); // lost initialization race; just spin
            else if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {
              // ① 通过 CAS 判断 SIZECTL 变量是否为 0 , 0 则改为 -1
                try {
                    if ((tab = table) == null || tab.length == 0) {
                      //② 再次判断 列表 table 为空 ,如果 sc 大于 0 则使用 sc 作为初始化容量, 否则使用 16
                        int n = (sc > 0) ? sc : DEFAULT_CAPACITY;
                        @SuppressWarnings("unchecked")
                        Node<K,V>[] nt = (Node<K,V>[])new Node<?,?>[n];
                        table = tab = nt;
                      //③ 计算下次扩容的大小，实际就是当前容量的 0.75 倍，这里使用了右移来计算
                        sc = n - (n >>> 2);
                    }
                } finally {
                  //④ 将下次扩容的大小赋值给 sizeCTL , 此时的 sizeCtl 变为正数
                    sizeCtl = sc;
                }
                break;
            }
        }
        return tab;
    }
```

#### 城北评语:

> 使用 CAS 从而只允许一个线程进行初始化,确保线程安全 ,其他线程

## put方法第二阶段-累加元素计数器

在 putVal 最后调用 addCount 的时候，传递了两个参数，分别是 1 和 binCount (链表长度)， 

- x 表示这次需要在表中增加的元素个数，
- check 参数表示是否需要进行扩容检查，大于等于 0 都需要进行检查

#### addCount

- ① 判断  counterCells 是否为空
  - 如果为空，就通过 cas 操作尝试修改 baseCount 变量，对这个变量进行原子累加操作(做这个操作的意义是:如果在没有竞争的情况下，仍然采用 baseCount 来记录元素个数
  - 如果 cas 失败说明存在竞争，这个时候不能再采用 baseCount 来累加，而是通过 CounterCell 来记录

```java
    private final void addCount(long x, int check) {
        CounterCell[] as; long b, s;
      //①判断  counterCells 是否为空
      //如果为空，就通过 cas 操作尝试修改 baseCount 变量，对这个变量进行原子累加操 作(做这个操作的意义是:如果在没有竞争的情况下，仍然采用 baseCount 来记录元素个数
      //如果 cas 失败说明存在竞争，这个时候不能再采用 baseCount 来累加，而是通过 CounterCell 来记录
        if ((as = counterCells) != null ||
            !U.compareAndSwapLong(this, BASECOUNT, b = baseCount, s = b + x)) {
            CounterCell a; long v; int m;
            boolean uncontended = true;//是否冲突标识，默认为没有冲突
          //这里有几个判断
          //  1. 计数表为空则直接调用 fullAddCount
          //  2. 从计数表中随机取出一个数组的位置为空，直接调用 fullAddCount
          //  3. 通过 CAS 修改 CounterCell 随机位置的值，如果修改失败说明出现并发情况(这里又用到了一种巧妙的方法)，调用 fullAndCount
          //  Random 在线程并发的时候会有性能问题以及可能会产生相同的随机数,ThreadLocalRandom.getProbe 可以解决这个问题，并且性能要比 Random 高
            if (as == null || (m = as.length - 1) < 0 ||
                (a = as[ThreadLocalRandom.getProbe() & m]) == null ||
                !(uncontended =U.compareAndSwapLong(a, CELLVALUE, v = a.value, v + x))) {
                fullAddCount(x, uncontended);
                return;
            }
            if (check <= 1)
                return;
            s = sumCount();
        }
        if (check >= 0) {
            Node<K,V>[] tab, nt; int n, sc;
            while (s >= (long)(sc = sizeCtl) && (tab = table) != null &&
                   (n = tab.length) < MAXIMUM_CAPACITY) {
                int rs = resizeStamp(n);
                if (sc < 0) {
                    if ((sc >>> RESIZE_STAMP_SHIFT) != rs || sc == rs + 1 ||
                        sc == rs + MAX_RESIZERS || (nt = nextTable) == null ||
                        transferIndex <= 0)
                        break;
                    if (U.compareAndSwapInt(this, SIZECTL, sc, sc + 1))
                        transfer(tab, nt);
                }
                else if (U.compareAndSwapInt(this, SIZECTL, sc,
                                             (rs << RESIZE_STAMP_SHIFT) + 2))
                    transfer(tab, null);
                s = sumCount();
            }
        }
    }
```

