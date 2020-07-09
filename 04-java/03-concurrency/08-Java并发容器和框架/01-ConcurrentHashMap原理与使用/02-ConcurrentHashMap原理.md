# ConcurrentHashMap原理

### ConcurrentHashMap 的结构

`ConcurrentHashMap` 是由 Segment 数组结构和 HashEntry 结构组成的

- `Segment` 是一种重入锁(`ReentrrantLock`), 在 `ConcurrentHashMap` 中扮演锁的角色
- `HashEntry`则用于存储键值对相关数据
- 一个 `ConcurrentHashMap` 里包含一个` Segment `数组



