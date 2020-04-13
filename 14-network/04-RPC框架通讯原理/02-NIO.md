# Java NIO

java Nio 的实现主要涉及三大核心内容

- Selector 选择器
- Channel 通道
- Buffer 缓冲区

Selector 用于监听多个 Channal 的事件,比如连接打开或者数据到达,因此,一个线程可以实现对多个数据 Channel的管理

- 传统 I/O 基于数据流进行 I/O 读写操作

- java NIO 基于 Channel 和 Buffer 进行 I/O 读写操作 并且数据总是被 从 Channel读取到 Buffer 中, 或者从 Buffer 写入 Channel 中

  

## Java NIO 和 java IO 最大的区别

- IO 是面向流的,NIO是面向缓冲区的:在面向流的操作中,数据只能在一个流中连续进行读写,数据没有缓冲,因此字节流无法前后移动
- NIO 每次都是讲数据从一个 Channel 读取到一个 Buffer 中,再从 Buffer 写入 Channel 中,因此可以方便地再缓冲区中进行数据的前后移动等操作

- 传统 IO的流操作是阻塞模式的,NIO 的操作模式是非阻塞模式的,在传统的模式下,用户线程调用 read() 或者 write 方法进行 IO 读写操作的时候,该线程一直被阻塞,知道数据被读取或者数据完全写入
- NIO 通过 Selector 监听 Channel 上事件的变化,在 Channel 上有数据发生变化时通知改线程进行读写操作,
  - 对于读请求而言,在通道上有可用的数据时,线程将进行 Buffer的读操作,在没有数据时,线程可以执行其他业务逻辑操作,
  - 对于写操作而言,在使用一个线程执行写操作将一些数据写入到某通道时,只需要将 Channel 上的数据异步写入 buffer 即可,buffer 上的数据会被异步写入目标 Channel 上,用户线层不需要等待整个数据完全被写入目标 Channel 就可以继续执行其他业务逻辑

**非阻塞IO 模型中的 Selector 线程通常将 IO 的空闲时间用于执行其他通道上的 I/O操作,所以 Selector 线程可以管理多个输入和输出通道**

## Channel

Channel 和 IO 中的 Stream 流类似,只不过 Stream 是单向的(InputStream , OutputStream),而 Channel 是双向的,既可以用来进行读操作,也可以用来写操作

NIO 中的 Channel 的主要实现有, FIleChannel , DatagramChannel , SocketChannel, ServerSocketChannel ,分别对应 I/O, UDP, TCP, Socket client ,Socket Server 

|      | Channel实现类       | 对应          |
| ---- | ------------------- | ------------- |
| 1    | FIleChannel         | I/O           |
| 2    | DatagramChannel     | UDP/TCP       |
| 3    | SocketChannel       | Socket client |
|      | ServerSocketChannel | Socket Server |

## Buffer 

buffer 实际上是一个容器, 器内部通过一个连续的字节数组存储 I/O 上的数据,在 NIO 中,Channel 在文件,网络上对数据进行读写都必须经过 Buffer

- 客户端向服务端发送数据时, 必须先写入 buffer
- 将 buffer 中的护具写入到服务器端的对应的 Channel 上
- 服务端在接受数据时必须要通过 Channel 将数据读入的 buffer 中,然后从 buffer 中读取数据并处理

|      | Buffer 子类  |      |
| ---- | ------------ | ---- |
| 1    | ByteBuffer   |      |
| 2    | IntBuffer    |      |
| 3    | CharBuffer   |      |
| 4    | LongBuffer   |      |
| 5    | DoubleBuffer |      |
| 6    | FloatBuffer  |      |
| 7    | ShortBuffer  |      |

## Selector

Selector 用于检测在多个注册的 Channel 上是否有 IO 事件发生, 并对检测到的 IO 事件进行相应的相应和处理,因此同构一个 Selector 线层可以实现对多个 Channel 的管理,不比为每个连接都创建一个线程,避免线程资源的浪费和多线程之间的上下文切换导致的开销

同时,Selector 值有在 Channel 上有读写事件发生时,才会调用 I/O 函数进行读写操作,可极大减少系统的开销,提高系统的并发量

