# List存储方式

## 一句话总结

3.2 之前 使用 ziplist 到临界点转化为 linkedList 进行存储

3.2 之后,统一使用 quickList 来存储 , quickList 存储了一个双向链表,每个节点都是一个zipList

 [02-快速列表.md](../16-Redis底层数据结构/02-快速列表.md) 

