# LinkedBlockingQueue

链表实现的有界阻塞 FIFO 队列, 此队列的默认和最大长度为 **Integer.MAX_VALUE**。

`LinkedBlockingQueue` 的内部是通过单向链表实现的,使用头节点,尾结点来进行入队和出队操作,也就是入队操作都是对尾结点进行操作,出队列都是对头结点进行操作

#### ![image-20200715132205808](../../../assets/image-20200715132205808.png)

对头结点和尾结点的操作分别使用了单独的独占锁从而保证了原子性,所以出栈和入栈可以同时进行的,另外对于头节点和尾结点都配对了条件队列,用来存放被阻塞的线程, 并结合入队、出队操作实现了一个生产消费模型



## 类图

<img src="../../../assets/image-20200715121926015.png" alt="image-20200715121926015" style="zoom: 67%;" />

## 操作

- [offer操作](#offer操作)
- [put操作](#put操作)
- [poll操作](#poll操作)
- [peek操作](#peek操作)
- [take操作](#take操作)
- [remove操作](#remove操作)
- [size操作](#size操作)







#### offer操作