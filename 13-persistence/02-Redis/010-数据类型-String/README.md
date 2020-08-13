# Redis String 常见问题

#### 问题 1Redis 中字符串的实现

在 3.2 以后的版本中，SDS 又有多种结构(sds.h): sdshdr5、sdshdr8、sdshdr16、sdshdr32、sdshdr64，用于存储不同的长度的字符串，分别代表 2^5=32byte， 2^8=256byte，2^16=65536byte=64KB，2^32byte=4GB。

```c++
/* sds.h */
struct __attribute__ ((__packed__)) sdshdr8 {
uint8_t len; /* 当前字符数组的长度 */
uint8_t alloc; /*当前字符数组总共分配的内存大小 */
unsigned char flags; /* 当前字符数组的属性、用来标识到底是 sdshdr8 还是 sdshdr16 等 */ char buf[]; /* 字符串真正的值 */
};
```

```
struct SDS<T>{
	T capacity; //数组容量
	T len;		//数组长度
	byte flags; //特殊标志位
	byte[] content
}
```

- 字符串是可以修改的字符串,要支持 append 操作,

如果数组没有冗余空间,那么追加操作必然涉及到重新分配新数组然后将旧内容复制过来,再 append 新内容,这样如果字符串非常的长,内存分配和复制的开销就会非常大

- Redis 规定字符串的长度不能超过 512MB,创建字符串时,len 和 capacity 一样长,不会多分配冗余空间

#### 问题 2、为什么 Redis 要用 SDS 实现字符串?

我们知道，C 语言本身没有字符串类型(只能用字符数组 char[]实现)。 

- 使用字符数组必须先给目标变量分配足够的空间，否则可能会溢出。 
- 如果要获取字符长度，必须遍历字符数组，时间复杂度是 O(n)。
- C 字符串长度的变更会对字符数组做内存重分配。 
- 通过从字符串开始到结尾碰到的第一个'\0'来标记字符串的结束，因此不能保存图片、音频、视频、压缩文件等二进制(bytes)保存的内容，二进制不安全。

SDS 的特点:

- 1、不用担心内存溢出问题，如果需要会对 SDS 进行扩容。
- 2、获取字符串长度时间复杂度为 O(1)，因为定义了 len 属性。 
- 3、通过“空间预分配”( sdsMakeRoomFor)和“惰性空间释放”，防止多次重分配内存。
- 4、判断是否结束的标志是 len 属性(它同样以'\0'结尾是因为这样就可以使用 C语言中函数库操作字符串的函数了)，可以包含'\0'。



#### 问题 3、embstr 和 raw 的区别?

Redis 的字符串有两种存储方式

- 在长度特别短时,使用 embstr 形式存储
- 超过 44 字节,使用 raw 存储

embstr 的使用只分配一次内存空间(因为 RedisObject 和 SDS 是连续的)，而 raw 需要分配两次内存空间(分别为 RedisObject 和 SDS 分配空间)。
因此与 raw 相比，embstr 的好处在于创建时少分配一次空间，删除时少释放一次 空间，以及对象的所有数据连在一起，寻找方便。
而 embstr 的坏处也很明显，如果字符串的长度增加需要重新分配内存时，整个 RedisObject 和 SDS 都需要重新分配空间，因此 Redis 中的 embstr 实现为只读。

#### 问题 4:int 和 embstr 什么时候转化为 raw?

当 int 数据不再是整数，或大小超过了 long 的范围 (2^63-1=9223372036854775807)时，自动转化为 embstr。

```
127.0.0.1:6379> set k1 1
OK
127.0.0.1:6379> append k1 a (integer) 2
127.0.0.1:6379> object encoding k1 "raw"

```

#### 问题 5:明明没有超过阈值，为什么变成 raw 了?

```
127.0.0.1:6379> set k2 a
OK
127.0.0.1:6379> object encoding k2
```

#### 问题 6:当长度小于阈值时，会还原吗?

关于 Redis 内部编码的转换，都符合以下规律:编码转换在 Redis 写入数据时完 成，且转换过程不可逆，只能从小内存编码向大内存编码转换(但是不包括重新 set)。

#### 问题 7:为什么要对底层的数据结构进行一层包装呢?

通过封装，可以根据对象的类型动态地选择存储结构和可以使用的命令，实现节省 空间和优化查询速度。

## 应用场景

#### 缓存

String 类型 例如:热点数据缓存(例如报表，明星出轨)，对象缓存，全页缓存。 可以提升热点数据的访问速度。

#### 数据共享分布式

STRING 类型，因为 Redis 是分布式的独立服务，可以在多个应用之间共享 例如:分布式 Session

```
<dependency> 
	<groupId>org.springframework.session</groupId>
	<artifactId>spring-session-data-redis</artifactId>
</dependency>
```

#### 分布式锁

STRING 类型 setnx 方法，只有不存在时才能添加成功，返回 true。

http://redisdoc.com/string/set.html 建议用参数的形式

```
public Boolean getLock(Object lockObject){ 
	jedisUtil = getJedisConnetion();
	boolean flag = jedisUtil.setNX(lockObj, 1);
  if(flag){
				expire(locakObj,10); 
			}
			return flag; 
	}
​
public void releaseLock(Object lockObject){
	del(lockObj); 
}
```

#### 全局 ID

#### INT 类型，INCRBY，利用原子性

```
incrby userid 1000
```

(分库分表的场景，一次性拿一段)

#### 计数器

INT 类型，INCR 方法
例如:文章的阅读量，微博点赞数，允许一定的延迟，先写入 Redis 再定时同步到 数据库。

#### 限流

INT 类型，INCR 方法
以访问者的 IP 和其他信息作为 key，访问一次增加一次计数，超过次数则返回 false。

