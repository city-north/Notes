# List

Redis列表是简单的字符串列表，按照插入顺序排序。你可以添加一个元素到列表的头部（左边）或者尾部（右边）

一个列表最多可以包含 232 - 1 个元素 (4294967295, 每个列表超过40亿个元素)。

### 实例

```
redis 127.0.0.1:6379> LPUSH runoobkey redis
(integer) 1
redis 127.0.0.1:6379> LPUSH runoobkey mongodb
(integer) 2
redis 127.0.0.1:6379> LPUSH runoobkey mysql
(integer) 3
redis 127.0.0.1:6379> LRANGE runoobkey 0 10

1) "mysql"
2) "mongodb"
3) "redis"
```

在以上实例中我们使用了 **LPUSH** 将三个值插入了名为 **runoobkey** 的列表当中。



### Redis 列表命令

下表列出了列表相关的基本命令：

| 序号 | 命令及描述 |                                                              |                                     |
| :--- | :--------- | ------------------------------------------------------------ | ----------------------------------- |
| 1    | blpop      | 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。 | [blpop.md](list/blpop.md)           |
| 2    | brpop      | 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。 | [brpop.md](list/brpop.md)           |
| 3    | brpoplpush | 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。 | [brpoplpush.md](list/brpoplpush.md) |
| 4    | lindex     | 通过索引获取列表中的元素                                     | [lindex.md](list/lindex.md)         |
| 5    | linsert    | 在列表的元素前或者后插入元素                                 | [linsert.md](list/linsert.md)       |
| 6    | llen       | 获取列表长度                                                 | [llen.md](list/llen.md)             |
| 7    | lpop       | 移出并获取列表的第一个元素                                   | [lpop.md](list/lpop.md)             |
| 8    | lpush      | 将一个或多个值插入到列表头部                                 | [lpush.md](list/lpush.md)           |
| 9    | lpushx     | 将一个值插入到已存在的列表头部                               | [lpushx.md](list/lpushx.md)         |
| 10   | lrange     | 获取列表指定范围内的元素                                     | [lrange.md](list/lrange.md)         |
| 11   | lrem       | 移除列表元素                                                 | [lrem.md](list/lrem.md)             |
| 12   | lset       | 通过索引设置列表元素的值                                     | [lset.md](list/lset.md)             |
| 13   | ltrim      | 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。 | [ltrim.md](list/ltrim.md)           |
| 14   | rpop       | 移除列表的最后一个元素，返回值为移除的元素。                 | [rpop.md](list/rpop.md)             |
| 15   | rpoplpush  | 移除列表的最后一个元素，并将该元素添加到另一个列表并返回     | [rpoplpush.md](list/rpoplpush.md)   |
| 16   | rpush      | 在列表中添加一个或多个值                                     | [rpush.md](list/rpush.md)           |
| 17   | rpushhx    | 为已存在的列表添加值                                         | [rpushhx.md](list/rpushhx.md)       |

## 模式: 安全的队列

值得注意的是,它可以提供一个**安全的队列**

Redis 中的列表经常被用作队列(queue),用于不同程序之间有序的交换消息:

- 客户端 A lpush 命令 将消息放入队列
- 客户端 B rpop 或者 brpop 命令去除队列中等待的最长消息

但是上面的队列方法**并不安全**,因为在这个过程中, 一个客户端可能在去除一个消息之后崩溃,从而未处理完的消息也就会因此丢失

#### 解决方案
使用 RPOPLPUSH 命令(或者它的阻塞版本 BRPOPLPUSH source destination timeout )可以解决这个问题：因为它不仅返回一个消息，同时还将这个消息添加到另一个备份列表当中，如果一切正常的话，当一个客户端完成某个消息的处理之后，可以用 LREM key count value 命令将这个消息从备份表删除。
最后，还可以添加一个客户端专门用于监视备份表，它自动地将超过一定处理时限的消息重新放入队列中去(负责处理该消息的客户端可能已经崩溃)，这样就不会丢失任何消息了。

## 模式: 循环列表

通过使用相同 key 作为 `rpoplpush` 命令的参数,客户端可以一个接一个地获取列表元使用相同的 key 作为 rpoplpush 命令的参数, 客户端可以用一个接一个地获取列表元素的方式,取得列表的所有元素,而不必像 `lrange` 一样一次性获取所有元素,两种方式的复杂度都是 O(n)

以上的模式可以应用在下面两种情况

- 有多个客户端同时对同一个列表进行旋转(rotating).它们获得不同的元素,直到所有元素被读取完,又重新开始
- 有客户端在向列表尾部(右边) 添加新元素

这个模式使得我们可以很容易实现这样一类系统：有 N 个客户端，需要连续不断地对一些元素进行处理，而且处理的过程必须尽可能地快。一个典型的例子就是服务器的监控程序：它们需要在尽可能短的时间内，并行地检查一组网站，确保它们的可访问性。

注意，使用这个模式的客户端是易于扩展(scala)且安全(reliable)的，因为就算接收到元素的客户端失败，元素还是保存在列表里面，不会丢失，等到下个迭代来临的时候，别的客户端又可以继续处理这些元素了。



#### 存储类型

  存储有序的字符串(从左到右)，元素可以重复。可以充当队列和栈的角色。在早期的版本中，数据量较小时用 ziplist 存储，达到临界值时转换为 linkedlist 进 行存储，分别对应 OBJ_ENCODING_ZIPLIST 和 OBJ_ENCODING_LINKEDLIST 

  3.2 版本之后，统一用 quicklist 来存储。quicklist 存储了一个双向链表，每个节点 都是一个 ziplist。

```
127.0.0.1:6379> object encoding queue "quicklist"
```

#### quicklist

quicklist(快速列表)是 ziplist 和 linkedlist 的结合体。 quicklist.h，head 和 tail 指向双向列表的表头和表尾

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

redis.conf 相关参数:

````
list-max-ziplist-size(fill)
正数表示单个 ziplist 最多所包含的 entry 个数。 负数代表单个 ziplist 的大小，默认 8k。 -1:4KB;-2:8KB;-3:16KB;-4:32KB;-5:64KB
list-compress-depth(compress)
压缩深度，默认是 0。
1:首尾的 ziplist 不压缩;2:首尾第一第二个 ziplist 不压缩，以此类推
````

