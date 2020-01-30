# ArrayList 和 LinkedList 的区别

> #### Difference between LinkedList vs ArrayList in Java

## 相同点

- 都继承`java.util.List`接口
- 都是非同步类

## 内部实现

两个集合都允许重复数据, 都保留插入顺序

- `LinkedList`实现的是双向队列
- `ArrayList`实现的是一个动态伸缩的array

这就会导致性能上的差异

## 性能

#### Add 操作

- `ArrayList`:如果不扩充 size ` O(1)`,如果扩充那么就是`O(log(n))`
- `LinkedList`:`O(1)`

####  Remove operation

- `ArrayList`:`O(1)`~`O(n)`
- `LinkedList`:`O(1)`,只需要修改一下链表 node 的指向

#### Iteration

都是`O(n)`

-  [06-random-access.md](06-random-access.md) 如果 LinkedList需要迭代,最好使用迭代器,直接使用 for 循环性能问题很大

####  Get operation

- `ArrayList`:  `O(1)`,直接根据索引下标获取
- `LinkedList`:`O(1)` ,从第一个检索到最后一个,所以是 In best case it is `O(1)` and in worst case it is `O(n)`.

