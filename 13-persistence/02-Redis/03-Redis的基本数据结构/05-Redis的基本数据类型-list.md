

#### 存储类型

  存储有序的字符串(从左到右)，元素可以重复。可以充当队列和栈的角色。在早期的版本中，数据量较小时用 ziplist 存储，达到临界值时转换为 linkedlist 进 行存储，分别对应 OBJ_ENCODING_ZIPLIST 和 OBJ_ENCODING_LINKEDLIST 

  3.2 版本之后，统一用 quicklist 来存储。quicklist 存储了一个双向链表，每个节点 都是一个 ziplist。

```
127.0.0.1:6379> object encoding queue "quicklist"
```

#### quicklist



```
typedef struct quicklist {
quicklistNode *head; /* 指向双向列表的表头 */ quicklistNode *tail; /* 指向双向列表的表尾 */
unsigned long count;
unsigned long len;
int fill : 16;
unsigned int compress : 16; /* 压缩深度，0:不压缩; */
/* 所有的 ziplist 中一共存了多少个元素 */ /* 双向链表的长度，node 的数量 */
/* fill factor for individual nodes */
} quicklist;
```



````

````

## 快速列表

Redis 底层不是一个简单的 LinkedList,而是称之为"快速链表" quick list 的一个结构

- 首先在列表元素比较少的情况下,列表的存储结构叫做 **ziplist**, 会使用一块连续的内存存储 , 即压缩列表。它将所有的元素彼此紧挨着一起存储,分配的一块连续的内存。
- 当数据量比较多的时候,会变成 quicklist ,因为普通的链表需要的附加指针空间太大,会浪费空间,还会加重内存的碎片化

比如某普通链表里的存储的只是 int 类型的数据,结构上还需要加上额外的指针 prev 和 next,所以 Redis 将链表和 zipList 结合起来组成了 quickList, 也是将多个 ziplist 使用指针串联起来

**quicklist既满足了快速的插入删除性能,又不会出现太大的内存冗余**

## 存储结构

quicklist(快速列表)是 ziplist 和 linkedlist 的结合体。 quicklist.h，head 和 tail 指向双向列表的表头和表尾

```c++
typedef struct quicklist {
  quicklistNode *head; /* 指向双向列表的表头 */ quicklistNode *tail; /* 指向双向列表的表尾 */
  unsigned long count;
  unsigned long len;
  int fill : 16;
  unsigned int compress : 16; /* 压缩深度，0:不压缩; */
  /* 所有的 ziplist 中一共存了多少个元素 */ /* 双向链表的长度，node 的数量 */
  /* fill factor for individual nodes */
} quicklist;
```

redis.conf 相关参数:

```java
list-max-ziplist-size(fill)
正数表示单个 ziplist 最多所包含的 entry 个数。 负数代表单个 ziplist 的大小，默认 8k。 -1:4KB;-2:8KB;-3:16KB;-4:32KB;-5:64KB
list-compress-depth(compress)
压缩深度，默认是 0。
1:首尾的 ziplist 不压缩;2:首尾第一第二个 ziplist 不压缩，以此类推
```

### ziplist 压缩列表

#### ziplist 压缩列表是什么?

ziplist 是一个经过特殊编码的双向链表，它不存储指向上一个链表节点和指向下一 个链表节点的指针，而是存储上一个节点长度和当前节点长度，通过牺牲部分读写性能， 来换取高效的内存空间利用率，是一种**时间换空间**的思想。只用在字段个数少，字段值小的场景里面。

#### ziplist 的内部结构?

```java
typedef struct zlentry {
unsigned int prevrawlensize; /* 上一个链表节点占用的长度 */
unsigned int prevrawlen;/* 存储上一个链表节点的长度数值所需要的字节数 */
  unsigned int lensize;/* 存储当前链表节点长度数值所需要的字节数 */
  unsigned int len;/* 当前链表节点占用的长度 */
  unsigned int headersize;/* 当前链表节点的头部大小(prevrawlensize + lensize)，即非数据域的大小 */
  unsigned char encoding;/* 编码方式 */
  unsigned char *p;/* 压缩链表以字符串的形式保存，该指针指向当前节点起始位置 */
} zlentry;
```

![](assets/image-20200406193439072.png)



#### 问题:什么时候使用 ziplist 存储?

当 hash 对象同时满足以下两个条件的时候，使用 ziplist 编码: 

- 所有的键值对的健和值的字符串长度都小于等于 64byte(一个英文字母一个字节
- 哈希对象保存的键值对数量小于 512 个。

```java
/* src/redis.conf 配置 */
hash-max-ziplist-value 64 // ziplist 中最大能存放的值长度 hash-max-ziplist-entries 512 // ziplist 中最多能存放的 entry 节点数量
```

/* src/redis.conf 配置 */

```java
hash-max-ziplist-value 64 // ziplist 中最大能存放的值长度 hash-max-ziplist-entries 512 // ziplist 中最多能存放的 entry 节点数量
```



```
/* 源码位置:t_hash.c ，当达字段个数超过阈值，使用 HT 作为编码 */ if (hashTypeLength(o) > server.hash_max_ziplist_entries)
hashTypeConvert(o, OBJ_ENCODING_HT); /*源码位置: t_hash.c，当字段值长度过大，转为 HT */
for (i = start; i <= end; i++) {
if (sdsEncodedObject(argv[i]) &&
sdslen(argv[i]->ptr) > server.hash_max_ziplist_value) {
hashTypeConvert(o, OBJ_ENCODING_HT); break;
} }
```

一个哈希对象超过配置的阈值(键和值的长度有>64byte，键值对个数>512 个)时， 会转换成哈希表(hashtable)。