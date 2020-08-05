# Redis 字符串应用场景

- [缓存](#缓存)
- [数据共享分布式](#数据共享分布式)
- [分布式锁](#分布式锁)

- [全局ID](#全局ID)
- [计数器](#计数器)
- [限流](#限流)
- [位统计](#位统计)

## 缓存

String 类型 例如:热点数据缓存(例如报表，明星出轨)，对象缓存，全页缓存。 可以提升热点数据的访问速度。

## 数据共享分布式

STRING 类型，因为 Redis 是分布式的独立服务，可以在多个应用之间共享 例如:分布式 Session

```xml
<dependency> 
  <groupId>org.springframework.session</groupId> 
  <artifactId>spring-session-data-redis</artifactId>
</dependency>
```

## 分布式锁

STRING 类型 setnx 方法，只有不存在时才能添加成功，返回 true。
http://redisdoc.com/string/set.html 建议用参数的形式

```java
    public Boolean getLock(Object lockObject) {
        jedisUtil = getJedisConnetion();
        boolean flag = jedisUtil.setNX(lockObj, 1);
        if (flag) {
            expire(locakObj, 10);
        }
        return flag;
    }
    public void releaseLock(Object lockObject) {
        del(lockObj);
    }
```

## 全局ID

INT 类型，INCRBY，利用原子性

```java
incrby userid 1000
```

(分库分表的场景，一次性拿一段)

## 计数器

INT 类型，INCR 方法
例如:文章的阅读量，微博点赞数，允许一定的延迟，先写入 Redis 再定时同步到 数据库。

## 限流

INT 类型，INCR 方法
以访问者的 IP 和其他信息作为 key，访问一次增加一次计数，超过次数则返回 false。

或者使用 lua 脚本

## 位统计

 [01-基础操作.md](../040-数据结构-位图/01-基础操作.md) 

String 类型的 BITCOUNT(1.6.6 的 bitmap 数据结构介绍)。 字符是以 8 位二进制存储的。

```java
set k1 a 
setbit k1 6 1 
setbit k1 7 0 
get k1
```

a 对应的 ASCII 码是 97，转换为二进制数据是 01100001

 b 对应的 ASCII 码是 98，转换为二进制数据是 01100010

因为 bit 非常节省空间(1 MB=8388608 bit)，可以用来做大数据量的统计。 例如:在线用户统计，留存用户统计

```
setbit onlineusers 0 1 setbit onlineusers 1 1 setbit onlineusers 2 0
```

支持按位与、按位或等等操作。

```
BITOP AND destkey key [key ...] ，对一个或多个 key 求逻辑并，并将结果保存到 destkey 。 
BITOP OR destkey key [key ...] ，对一个或多个 key 求逻辑或，并将结果保存到 destkey 。 
BITOP XOR destkey key [key ...] ，对一个或多个 key 求逻辑异或，并将结果保存到 destkey 。 
BITOP NOT destkey key ，对给定 key 求逻辑非，并将结果保存到 destkey 。
```

计算出 7 天都在线的用户

```
BITOP "AND" "7_days_both_online_users" "day_1_online_users" "day_2_online_users" ... "day_7_online_users"
```

```
mset student:1:sno GP16666 student:1:sname 沐风 student:1:company 腾讯
```

获取值的时候一次获取多个值:

```
mget student:1:sno student:1:sname student:1:company
```

