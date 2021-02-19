# 031-Redission实现可重入锁

---

[TOC]

## 典型实现

```java
public static void main(String[] args) throws InterruptedException {
  RLock rLock=redissonClient.getLock("updateAccount");
  // 最多等待100秒、上锁10s以后自动解锁
  if(rLock.tryLock(100,10, TimeUnit.SECONDS)){
    System.out.println("获取锁成功");
  }
  //Thread.sleep(20000);
  rLock.unlock();

  redissonClient.shutdown();
}
```

在获得 RLock 之后，只需要一个 tryLock 方法，里面有 3 个参数: 

- watiTime:获取锁的最大等待时间，超过这个时间不再尝试获取锁 
- leaseTime:如果没有调用 unlock，超过了这个时间会自动释放锁
- TimeUnit:释放时间的单位

## 官方文档

### 可重入锁（Reentrant Lock）

基于Redis的Redisson分布式可重入锁[`RLock`](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RLock.html) Java对象实现了`java.util.concurrent.locks.Lock`接口。同时还提供了[异步（Async）](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RLockAsync.html)、[反射式（Reactive）](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RLockReactive.html)和[RxJava2标准](http://static.javadoc.io/org.redisson/redisson/3.10.0/org/redisson/api/RLockRx.html)的接口。

```
RLock lock = redisson.getLock("anyLock");
// 最常见的使用方法
lock.lock();
```

大家都知道，如果负责储存这个分布式锁的Redisson节点宕机以后，而且这个锁正好处于锁住的状态时，这个锁会出现锁死的状态。为了避免这种情况的发生，Redisson内部提供了一个监控锁的看门狗，它的作用是在Redisson实例被关闭前，不断的延长锁的有效期。默认情况下，看门狗的检查锁的超时时间是30秒钟，也可以通过修改[Config.lockWatchdogTimeout](https://github.com/redisson/redisson/wiki/2.-配置方法#lockwatchdogtimeout监控锁的看门狗超时单位毫秒)来另行指定。

另外Redisson还通过加锁的方法提供了`leaseTime`的参数来指定加锁的时间。超过这个时间后锁便自动解开了。

```
// 加锁以后10秒钟自动解锁
// 无需调用unlock方法手动解锁
lock.lock(10, TimeUnit.SECONDS);

// 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
if (res) {
try {
  ...
} finally {
    lock.unlock();
}
}
```

Redisson同时还为分布式锁提供了异步执行的相关方法：

```
RLock lock = redisson.getLock("anyLock");
lock.lockAsync();
lock.lockAsync(10, TimeUnit.SECONDS);
Future<Boolean> res = lock.tryLockAsync(100, 10, TimeUnit.SECONDS);
```

`RLock`对象完全符合Java的Lock规范。也就是说只有拥有锁的进程才能解锁，其他进程解锁则会抛出`IllegalMonitorStateException`错误。但是如果遇到需要其他进程也能解锁的情况，请使用[分布式信号量`Semaphore`](https://github.com/redisson/redisson/wiki/8.-分布式锁和同步器#86-信号量semaphore) 对象.

## Redission中分布式锁的实现

```
tryLock() ——> tryAcquire() ——> tryAcquireAsync() ——> tryLockInnerAsync()
```

**最终也是调用了一段 Lua 脚本。里面有一个参数，两个参数的值。**

| 占位    | 填充                  | 含义             | 实际值                                 |
| ------- | --------------------- | ---------------- | -------------------------------------- |
| KEYS[1] | getName()             | 锁的名称(key)    | updateAccount                          |
| ARGV[1] | internalLockLeaseTime | 锁释放时间(毫秒) | 10000                                  |
| ARGV[2] | getLockName(threadId) | 线程名称         | b60a9c8c-92f8-4bfe-b0e7-308967346336:1 |

#### 加锁

```lua
// KEYS[1] 锁名称 updateAccount // ARGV[1] key 过期时间 10000ms // ARGV[2] 线程名称
// 锁名称不存在
if (redis.call('exists', KEYS[1]) == 0) then
  // 创建一个 hash，key=锁名称，field=线程名，value=1 
  redis.call('hset', KEYS[1], ARGV[2], 1);
  // 设置 hash 的过期时间
  redis.call('pexpire', KEYS[1], ARGV[1]);
  return nil;
end;
  // 锁名称存在，判断是否当前线程持有的锁
if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then
  // 如果是，value+1，代表重入次数+1 
  redis.call('hincrby', KEYS[1], ARGV[2], 1);
  // 重新获得锁，需要重新设置 Key 的过期时间 
  redis.call('pexpire', KEYS[1], ARGV[1]);
  return nil;
end;
// 锁存在，但是不是当前线程持有，返回过期时间(毫秒) return redis.call('pttl', KEYS[1]);
```

#### 释放锁

```
unlock——unlockInnerAsync
```

| 占位    | 填充                     | 含义         | 实际值                                 |
| ------- | ------------------------ | ------------ | -------------------------------------- |
| KEYS[1] | getName()                | 锁名称       | updateAccount                          |
| KEYS[2] | getChannelName()         | 频道名称     | redisson_lock__channel:{updateAccount} |
| ARGV[1] | LockPubSub.unlockMessage | 解锁时的消息 | 0                                      |
| ARGV[2] | internalLockLeaseTime    | 释放锁的时间 | 10000                                  |
| ARGV[3] | getLockName(threadId)    | 线程名称     | b60a9c8c-92f8-4bfe-b0e7-308967346336:1 |

```lua
// KEYS[1] 锁的名称 updateAccount
// KEYS[2] 频道名称 redisson_lock__channel:{updateAccount} // ARGV[1] 释放锁的消息 0
// ARGV[2] 锁释放时间 10000
// ARGV[3] 线程名称
// 锁不存在(过期或者已经释放了)
if (redis.call('exists', KEYS[1]) == 0) then
	// 发布锁已经释放的消息 
  redis.call('publish', KEYS[2], ARGV[1]); return 1;
end;
	// 锁存在，但是不是当前线程加的锁
if (redis.call('hexists', KEYS[1], ARGV[3]) == 0) then
	return nil; 
end;

	// 锁存在，是当前线程加的锁
	// 重入次数-1
local counter = redis.call('hincrby', KEYS[1], ARGV[3], -1);
	// -1 后大于 0，说明这个线程持有这把锁还有其他的任务需要执行 
if (counter > 0) then
	// 重新设置锁的过期时间 redis.call('pexpire', KEYS[1], ARGV[2]); return 0;
else
	// -1 之后等于 0，现在可以删除锁了 
  redis.call('del', KEYS[1]);
	// 删除之后发布释放锁的消息 
  redis.call('publish', KEYS[2], ARGV[1]); 
  return 1;
end; 
// 其他情况返回 nil 
return nil;
```

这个是 Redisson 里面分布式锁的实现，我们在调用的时候非常简单。