# Cache 相关源码

### 核心接口

Cache 接口是 MyBatis 中的核心接口,实际上

![image-20200219212119652](../../assets/image-20200219212119652.png)

值得注意的是`TransactionalCacheManager` 事务缓存管理器,实际上维护了一个 HashMap,支持同事添加或者修改多个缓存,只有调用了 Commit 方法以后,缓存的方法才会被写入

![image-20200219222903814](../../assets/image-20200219222903814.png)

#### 默认的实现类

`PerpetualCache`是默认的实现类,主要是对 Cache 进行了简单的封装

![image-20200219212210521](../../assets/image-20200219212210521.png)

其中仅仅只维护了一个 HashMap 

![image-20200219212313889](../../assets/image-20200219212313889.png)

#### 装饰类

实现 Cache 接口的不光光有默认的实现类`perpetualCache`,还有一系列的装饰类,这里使用的是装饰器模式 [04-decorator-pattern.md](../../01-design-patterns/03-structural-patterns/04-decorator-pattern.md) 

![image-20200219212415724](../../assets/image-20200219212415724.png)

#### SynchronizedCache 

这是一个非常简单的装饰器,主要是用`synchronized`关键字封装了所有的方法来做到对 Cache 访问的同步

![image-20200219212523577](../../assets/image-20200219212523577.png)