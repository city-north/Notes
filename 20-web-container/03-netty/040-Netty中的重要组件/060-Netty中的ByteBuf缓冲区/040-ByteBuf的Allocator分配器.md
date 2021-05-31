# 040-ByteBuf的Allocator分配器

[TOC]

## 什么是Allocator

Netty通过 ByteBufAllocator分配器来创建缓冲区和分配内存空间， Netty提供两种实现

- PoolByteBufAllocator
- UnpoolByteBufAllocator

## PoolByteBufAllocator

池化ByteBuf 将ByteBuf实例放入池中， 提高了性能， 将内存碎片减少的最小， 这个池化分配器采用jemalloc高效分配策略， 内存使用量相对稳定， 系统不会因为内存耗尽而崩溃， 默认策略

## UnpoolByteBufAllocator

非池化的分配策略， 每次调用，返回一个新的ByteBuf , 随着系统的运行空间不断增长， 可能会导致内存溢出

