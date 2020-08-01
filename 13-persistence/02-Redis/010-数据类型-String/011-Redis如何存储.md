# Redis底层如何存储

## 一句话总结

底层采用两个 hash表存储, 因为要扩容要渐进式 rehash, key 是SDS ,最大 512M ,value 是 redisObject, 和 hashmap 很像, 数组+链表+头插法

关于存储值得讨论的问题

-  [Redis扩容与缩容](../06-模式以及常见问题/06-Redis扩容与缩容.md) 

## 详细介绍

Redis 数据库的所有 key 和 value 组成了一个字典, 内部维护了两个 hashtable 

通常情况下只有一个 hashTable 有值,但是在字典扩容的时候

- 从第一个 hashtable **渐进式**搬迁到 第二个 hashtbale
- 搬迁后 第一个被删除

hashTable 的结构和 Java 的 1.8 之前的 hashMap几乎一样

- 数组+链表
- 头插法

数组中的每个元素叫 dictEntry 

<img src="../../../assets/image-20200801184925185.png" alt="image-20200801184925185" style="zoom: 67%;" />



dictEntry 中包含指向具体类型的指针

![image-20200801185922017](../../../assets/image-20200801185922017.png)

- 我们都知道 key 是 String 类型的,所以指向的是一个 sds 对象
- value 指向的是一个叫做 redisObject 的对象\,实际上五种常用的数据类型的任何一种，都是通过 redisObject 来存储 的。

```c
typedef struct redisObject {
	unsigned type:4; /* 对象的类型，包括:OBJ_STRING、OBJ_LIST、OBJ_HASH、OBJ_SET、OBJ_ZSET */ 		   		unsigned encoding:4; /* 具体的数据结构 */
   unsigned lru:LRU_BITS; /* 24 位，对象最后一次被命令程序访问的时间，与内存回收有关 */
    int refcount; /* 引用计数。当 refcount 为 0 的时候，表示该对象已经不被任何对象引用，则可以进行垃圾回收了
*/
void *ptr; /* 指向对象实际的数据结构 */
} robj;
```

- type  4bit
- encoding 4bit
- lru 24bit
- refcount 4 字节 32 bit
- ptr 8 字节,64bit

**一共是对象头就占用 16 个字节**

我们可以使用 type查看 redisObject 的类型

```java
127.0.0.1:6379> type key
string
```

可以使用 object encoding 查看 redisObject的编码

```java
127.0.0.1:6379> object encoding key
"embstr"
```

对象类型的存储方式看笔记

- [字符串存储方式.md](030-字符串存储方式.md) 







