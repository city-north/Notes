# 010-ChannelOption通道选项

[TOC]

## ChannelOption

无论是 NioServerSocketChannel  父通道类型, 还是对于 NioSocketChannel 子通道类型, 都可以设置ChannelOption

## 参数列表

| 参数                 | 解释                         |
| -------------------- | ---------------------------- |
| SO_RVCBUF, SO_SNDBUF | 每个TCP Socket 套接字        |
| TCP_NODELAY          | 表示立即发送数据             |
| SO_KEEPALIVE         | 底层TCP的心跳机制            |
| SO_REUSEADDR         | 表示地址复用                 |
| SO_LINGER            | 表示关闭socket的延迟时间,    |
| SO_BACK_LOG          | 表示服务端接受连接的队列长度 |
| SO_BROADCAST         | 表示广播模式                 |

## SO_RVCBUF, SO_SNDBUF

是TCP的参数, 每个TCP Socket 套接字, 在内核中都有一个发送缓冲区和一个接受缓冲区, 这两个选项就是用来设置TCP 连接的这两个缓冲区大小的

TCP 的全双工工作模式以及TCP的滑动窗口有便是依赖于这两个独立的缓冲区以及填充的状态

## TCP_NODELAY

TCP参数, 表示立即发送数据, 默认值为true, (netty是true,操作系统 false), 该值用于设置Nagle算法的启用, 该算法将小的碎片连接成更大的报文(或者数据包)来最小化发送报文的数量

如果需要发送一些比较小的报文, 应该禁用这个算法, 

- 设置为true, 表示为关闭 算法, 适用于高实时性, 有数据就立刻发送
- 设置为false , 表示为开启 ,  如果要减少发送次数和减少网络交互的次数, 就设置为false

## SO_KEEPALIVE

TCP参数, 表示底层TCP的心跳机制

- true, 连接保持心跳, 默认为false

启用该功能时, TCP会主动探测空闲连接到有效性, 可以视为是TCP的心跳机制, 需要注意的是, 默认的心跳间隔是 7200s 即2小时, Netty默认关闭这个功能

## SO_REUSEADDR

TCP参数,四种情况 

- true 时表示地址复用, 默认为false

四种情况 

- 当有一个有相同本地地址和端口的socket1处于TIME_WAIT状态时，而你启动的程序的socket2要占用该地址和端口，你的程序就要用到该选项。
- SO_REUSEADDR允许同一port上启动同一服务器的多个实例(多个进程)。但每个实例绑定的IP地址是不能相同的。在有多块网卡或用IP?Alias技术的机器可以测试这种情况。
- SO_REUSEADDR允许单个进程绑定相同的端口到多个socket上，但每个socket绑定的ip地址不同。这和2很相似，区别请看UNPv1。
- SO_REUSEADDR允许完全相同的地址和端口的重复绑定。但这只用于UDP的多播, 不用于TCP
  

## SO_LINGER

此为TCP参数, 表示关闭socket的延迟时间, 默认为-1 , 表示为禁用该功能

- -1 表示 socket.close(), 方法立刻返回 ,但是操作系统底层会将发送缓冲区全部发送到对端
- 0 表示 socket.close (), 方法立即返回, 操作系统方琪发送缓冲区的数据, 直接向对端发送RST包, 对端收到复位错误
- 非0 表示调用socket.close()方法的线程被阻塞, 知道延迟时间的到来, 发送缓冲区中的数据发送完毕, 如果超时, 则对端会收到复位错误

## SO_BACK_LOG

TCP参数, 表示服务端接受连接的队列长度, 如果队列已满, 客户端连接将被拒绝, 默认值,在Windows中午为200, 其他操作系统是128

## SO_BROADCAST

TCP参数, 表示广播模式