# 多量程读取优化-MRR

Muti-Range Read Optimization  

## 参数

- mrr

- mrr_cost_based

  > 表示是否通过基于成本的算法来确定开始 mrr 特性,
  >
  > - 设置为 on 表示自行判断
  > - off 表示强制开启 mrr

```
show variables like 'optimizer_switch';
```

```
...
mrr=on,mrr_cost_based=on,
...
```

## 原理

MRR 的原理很简单, MySQL 普通索引获取数据的方式,显示通过索引页的叶子节点找到对应的主键,再通过主键找到相对应的行数据记录, 

- 如果在一张表中对某一个字段创建一个普通索引,单这个字段有一些重复的值,那么根据这个字段取做 where 条件时, 每次去到的主键值可能不是按照顺序的,那么随机 IO 的行为就会发生
- MRR 的作用就是把呕吐索引的叶子节点上找到的主键值的集合存储到 read_rnd_buffer 中,然后再改该 buffer 中对主键值进行排序,最后再利用已经排序好的主键值的集合,去访问表中的数据,这样就由原来的随机 IO 变成了顺序 IO,降低了 Io 的查询开销

> 在生产环境中,read_rnd_buffer_size 的值可以是 4~8MB之间调整