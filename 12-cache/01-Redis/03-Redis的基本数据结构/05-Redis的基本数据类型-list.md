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

uyyyyy56.,,,,,,,,,≤≤≤≤≤≤≤≤≤≤≤≤≤ hhhhhhhhhhhhhhhhhhhhj                                                  j vvvvvvvvvvvvvvvvvvvv			