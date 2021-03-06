# 020-Redis-hash-底层存储

[TOC]

## Redis常用对象存储

| 对象           | 条件                                                 | 原始编码  | 升级条件 | 升级编码  |
| -------------- | ---------------------------------------------------- | --------- | -------- | --------- |
| 哈希对象(Hash) | 键和值的长度小于64byte<br />且Key-value不能超过512个 | quickList | 超过     | hashtable |

## 一言蔽之

Redis 的 Hash 本身也是一个 KV 的结构，类似于 Java 中的 HashMap。头插法

- 元素较少时  [ziplist 压缩列表](../16-Redis底层数据结构/01-压缩列表.md) 
-  [hashtable](../010-数据类型-String/011-Redis如何存储.md) 

## 值得注意的问题

-  [什么时候使用hash什么时候使用String](../010-数据类型-String/011-Redis如何存储.md#什么时候使用hash什么时候使用String) 

- [什么时候使用 zipList什么时候使用 hashtable](#什么时候使用 zipList什么时候使用 hashtable) 

Redis 的 KV 结构是通过一个 dictEntry 来实现的。

Redis 又对 dictEntry 进行了多层的封装。

```c
typedef struct dictEntry {
    void *key;   						/* key 关键字定义 */
    void *val;  					 	/* value 定义 */
    struct dictEntry *next; /* 指向下一个键值对节点 */
} dictEntry;
```

dictEntry 放到了 dictht (hashtable 里面): 这显然是一个数组

```c
typedef struct dictht {
    dictEntry **table;			/* 哈希表数组 */
    unsigned long size;			/* 哈希表大小 */
    unsigned long sizemask; /* 掩码大小，用于计算索引值。总是等于 size-1 */
    unsigned long used;    	/* 已有节点数 */
} dictht;
```

ht 放到了 dict 里面:

```c
typedef struct dict {
    dictType *type; /* 字典类型 */
    void *privdata; /* 私有数据 */
    dictht ht[2]; /* 一个字典有两个哈希表 */
    long rehashidx; /* rehashing not in progress if rehashidx == -1 *//* rehash 索引 */
    unsigned long iterators;/* 当前正在使用的迭代器数量 */
} dict;
```

![image-20200406200343297](../../../../assets/image-20200406200343297.png)

## 为什么要定义两个哈希表呢

redis 的 hash 默认使用的是 ht[0]，ht[1]不会初始化和分配空间。为了扩容

参考

- [011-Redis如何存储.md](../010-数据类型-String/011-Redis如何存储.md) 

-  [06-Redis扩容与缩容.md](../06-模式以及常见问题/06-Redis扩容与缩容.md) 

## 什么时候使用zipList什么时候使用hashtable 

当hash对象同时满足以下两个条件的时候，使用 ziplist 编码: 

- 所有的键值对的健和值的字符串长度都小于等于 64byte(一个英文字母一个字节);
- 哈希对象保存的键值对数量小于 512 个。

一个哈希对象超过配置的阈值(键和值的长度有>64byte，键值对个数>512 个)时， 会转换成哈希表(hashtable)。

#### 相关配置

```
hash-max-ziplist-value 64
hash-max-ziolist-entries =512
```

