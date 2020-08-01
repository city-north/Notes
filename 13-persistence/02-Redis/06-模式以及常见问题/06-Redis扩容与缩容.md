# Redis扩容与缩容

## 一句话总结

Redis 的扩容比较耗费时间, 需要重新分配一个新的数组,然后将旧的数组所有链表中的元素重新挂接到新的数组下面, 

**这是一个 O(n) 级别的操作 ,Redis 单线程所以很难接受**,所以 Redis 使用 **渐进式 Rehash**来进行搬迁

### 值得注意的问题

- [什么时候扩容](#什么时候扩容)

- [渐进式Rehash](#渐进式Rehash)
- [什么时候缩容](#什么时候缩容)
- [怎么缩容](#怎么缩容)

## 什么时候扩容

```c
static int dict_can_resize = 1;
static unsigned int dict_force_resize_ratio = 5;
```

- ratio = used / size，已使用节点与字典大小的比例
- dict_can_resize 为 1 并且 dict_force_resize_ratio **已使用节点数和字典大小之间**的 比率超过 1:5，触发扩容

## 渐进式Rehash

- 正常情况下,hash 表中的个数等于第一维数组的长度时,就会开始扩容,扩容的新数组是原来数组的两倍
- 如果 Redis 正在做 bgsave ,因为 bgsave 使用的是 copy on write 机制,所以为了减少内存页的过多分离,Redis 尽量不去扩容,但是字典中的元素的个数已经达到一维数组长度的 **5 倍**, 会强制扩容

渐进式 rehash 会在 rehash 的同时,保留新就两个 hash结构,**查询时会同时查询两个 hash 结构**,然后再后续的定时任务以及 hash 操作指令中,循序渐进地将旧的内容一点点地迁移到新的 hash结构中,当搬迁完成之后,就会使用新的 hash 取而代之

- 操作辅助 rehash : 在 redis 中每一个增删改查命令都会判断字典中的 hash 表是否正在进行 rehash,如果是则帮助执行一次
- 定时辅助 rehash : 如果服务器比较空间,那么 redis 数据库会很长时间内一直使用两个 hash 表,所以在 redis 的周期函数中,如果发现字典正在进行渐进式 rehash 操作,则会花费 1毫秒的时间,帮助一起进行渐进式 rehash操作

![image-20200427122511990](../../../assets/image-20200427122511990.png)

## 什么时候缩容

元素个数低于数组长度的 10% ,则缩容 , 不考虑是不是在 bgsave

## 怎么缩容

一样,渐进式 rehash 