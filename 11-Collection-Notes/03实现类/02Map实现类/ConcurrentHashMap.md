# 为什么要使用ConcurrentHashMap
- 在并发编程中使用`HashMap`可能会导致程序死循环。
- 而使用线程安全的`HashTable`效率又非常低下（其方法全部使用的是`Syschronzied`关键字修饰）。

#### 线程不安全的HashMap:
多线程下，使用HashMap进行put操作会引起死循环，导致CPU利用率接近100%，所以并发状态下不能使用HashMap:
例子：死循环的HashMap:

```java
final HashMap<String,String> map = new HashMap<String,String>(2);
	Thread t = new Thread(new Runnable(){
		@Override
		public void run(){
			for(int i = 0; i<1000l ; i++){
				new Thread(new Runnable(){
					@Override
					public void run (){
						map.put(UUID.randomUUID.toString(),"";)
					}
				},"ftf" + i).start(),
			}
		}
	},"ftf");
t.start();
t.join();
```
HashTable在并发执行put操作时会引起死循环，是因为多线程会导致HashMap的Entry链表形成环形数据结构，一旦形成环形数据结构，Entry的next节点永远不会空，就会产生死循环获取Entry.

#### 效率低下的HashTable
HashTable 容器使用synchronized来保证线程安全，单在线程竞争激烈的情况下HashTable的效率非常低下。多线程争夺时，会进入阻塞或者轮询的状态。
例如：
当线程1使用put方法进行元素添加，线程2不但不能使用put方法添加元素，而且不能使用get方法获取元素，所以竞争越激烈，效率越低。

#### ConcurrentHashMap 的锁分段技术可以提高并发访问率
锁分段技术：
加入容器里有多把锁，每一把锁用来锁住容器中的一部分数，那么当多线程访问容器里不同数据段的数据时，线程间就不会存在锁竞争，从而提高并发访问效率。
总结来说就是：
把数据分成一段一段地存储，然后给每一段数据配一把锁，当一个线程占用锁访问其中一段数据的时候，其他段的数据可能被其他线程访问。

#### ConcurrentHashMap的结构
先看图：

![](http://note.eccto.cn/server/../Public/Uploads/2018-11-13/5bead4696a923.png)

ConcurrentHashMap是由Segment 数组结构和HashEntry 数组结构组成。
- Segment是一种可重入锁（ReentrantLock）
- HashEntry用于存储键值对数据

一个ConcurrentHashMap 里包含一个Segment数组（一种数组和链表的结构）,一个Segment里包含一个HashEntry数组，每个HashEntry是一个链表结构的元素。

每个Segment里包含一个HashEntry数组，守护这个这个HashEntry数组里的元素，而对HashEntry数组的数据进行修改时，必须要先获得与它对应的Segment锁.
![](http://note.eccto.cn/server/../Public/Uploads/2018-11-13/5bead6129cbc9.png)