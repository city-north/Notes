# 020-Redis-zset-底层存储

[TOC]

Zset 为有序的,自动去重的集合数据类型, zset 数据结构的实现为 字典(dict) + 跳表 (skipList) 存储

- 当数据量较少时,采用 ziplist 编码结构存储
- 元素个数超过 128 ,将采用 skiplist 编码
- 单个元素大小超过 64 byte ,则使用 skiplist编码

| 对象               | 条件                                                | 原始编码 | 升级条件 | 升级编码 |
| ------------------ | --------------------------------------------------- | -------- | -------- | -------- |
| 有序集合对象(ZSet) | 元素数量不超过128个且任何一个member的长度小于64字节 | zipList  | 超过     | skipList |

## 常用属性

```
zst-max-ziplist-entries 128 //元素个数超过 128 ,将采用 skiplist 编码
zset-max-ziplist-value 64   // 单个元素大小超过 64byte ,则使用 skiplist编码
```

## 存储结构

当数据量较少时,采用 ziplist 编码结构存储

![zset-ziplist](../../../../assets/zset-ziplist.png)

## 源码

```c
typedef struct zset {
    dict *dict;      //字典
    zskiplist *zsl;  //链表
} zset;
```

跳表结构

```c
typedef struct zskiplist {
    struct zskiplistNode *header, 
  	*tail;
    unsigned long length;
    int level;
} zskiplist;
```

节点

```c
typedef struct zskiplistNode {
    sds ele;
    double score;
    struct zskiplistNode *backward;
    struct zskiplistLevel {
        struct zskiplistNode *forward;
        unsigned long span;
    } level[];
} zskiplistNode;
```



我们可以以 O(1)时间复杂度 的原因是使用了字典

- 判断元素存不存在
- 获取 score

## 随机层数

没插入一个新接地那,都需要调用一个随机算法给它分配一个合理的层数

每一层的晋升率为 50% ,就像抛硬币 (Redis 晋升率实现只有 25%) 

- level1 的晋升几率是 50%
- level2 的晋升几率是 25%
- level3 的晋升几率是 12.5%
- 2^63 次方被分配到最顶层

Redis 晋升率实现只有 25%,是为了让跳表更加扁平化,层数相对低,跳跃次数少,但是遍历多一点

Redis 保存了最高层 maxLevel ,遍历时直接从 maxLevel 进行遍历

## 如果 score 都一样呢

如果所有的 score 都一样,那么查找性能会不会退化到 O(n) 呢? 

Redis 排序元素不仅看 score ,如果score相等还会比较 value 的值(字符串比较)

## rank 排名是怎么算出来的

Redis 在 skiplist  的 forword 指针上添加了一个 span 属性, 也就是跨度的意思,标识从前一个节点沿着当前层的 forward 指针跳到当前节点中间会跳过多少节点,Redis 在插入删除操作的时候会更新 span 的值