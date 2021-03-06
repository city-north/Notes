# 030-Redis-hash-应用场景

[TOC]

## 存储对象类型的数据

比如对象或者一张表的数据，比 String 节省了更多 key 的空间，也更加便于集中管 理。

![image-20200805171843836](../../../../assets/image-20200805171843836.png)

## 购物车

key:用户 id;

field:商品 id;

value:商品数量。 +1:hincr。-1:hdecr。

删除:hdel。

全选:hgetall。

商品数:hlen。

