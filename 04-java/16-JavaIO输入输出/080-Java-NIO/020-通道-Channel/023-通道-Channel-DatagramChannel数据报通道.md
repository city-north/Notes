# 023-通道-Channel-DatagramChannel数据报通道3

[TOC]

## 什么是DatagramChannel

和Socket套接字的TCP传输协议不同， UDP协议不是面向连接的协议，使用UDP协议时只需要知道服务器的IP和端口，就可以直接向对方发送数据，在Java中使用UDP协议传输数据比使用TCP传输数据要简单

在Java NIO 中， 使用 DatagramChannel 数据报通道来处理UDP协议的数据传输

## 值得讨论的话题

1. 获取 DatagramChannel 数据报通道
2. 读取 DatagramChannel 数据报通道的数据
3. 写入 DatagramChannel 数据报通道
4. 关闭 DatagramChannel 数据报通道

## 1.获取 DatagramChannel 数据报通道

获取数据报通道的方式很简单， 使用 DatagramChannel 的open 静态方法即可， 然后使用 configureBlocking(false) 方法， 设置成非阻塞模式

```java
//获取数据报
DatagramChannel datagramChannel = DatagramChannel.open();
//设置为非阻塞模式
datagramChannel.configureBlocking(false);
//调用bind方法绑定以一个数据报的监听端口
datagramChannel.bind(new InetSocketAddress(
        NioDemoConfig.SOCKET_SERVER_IP
        , NioDemoConfig.SOCKET_SERVER_PORT));
```

## 2.读取 DatagramChannel 数据报通道的数据

调用receive （ByteBufferbuf）方法将 DatagramChannel 读入

```java
//创建缓冲区
ByteBuffer buffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);
//从 DatagramChannel 读入， 再吸入到ByteBuffer缓冲区
SocketAddress client = datagramChannel.receive(buffer);
```

通道读取receive(ByteBufferbuf) 方法的返回值， SocketAddress类型，表示返回发送端的链接地址（包括 IP 和端口）

通过receive方法读取数据非常简单， 但是， 在非阻塞模式下， 如何知道 DatagramChannel  是否可读呢？ ---通过选择器 Selector

## 3.写入 DatagramChannel 数据报通道

向 DatagramChannel  发送数据，调用send方法

```java
buffer.flip();
// 操作三：通过DatagramChannel数据报通道发送数据
dChannel.send(buffer,
              new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP
                                    , NioDemoConfig.SOCKET_SERVER_PORT));
buffer.clear();
```

## 4.关闭 DatagramChannel 数据报通道

```java
chennel.close();
```

## 使用DatagramChannel  数据包通道发送数据的实践案例

功能是： 获取用户输入的数据， 通过 DatagramChannel  数据报通道， 将数据发送到远程的服务器

```java
public class UDPClient {

    public void send() throws IOException {
        //操作一：获取DatagramChannel数据报通道
        DatagramChannel dChannel = DatagramChannel.open();
        dChannel.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);
        Scanner scanner = new Scanner(System.in);
        Print.tcfo("UDP 客户端启动成功！");
        Print.tcfo("请输入发送内容:");
        while (scanner.hasNext()) {
            String next = scanner.next();
            buffer.put((Dateutil.getNow() + " >>" + next).getBytes());
            buffer.flip();
            // 操作三：通过DatagramChannel数据报通道发送数据
            dChannel.send(buffer,
                    new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP
                            , NioDemoConfig.SOCKET_SERVER_PORT));
            buffer.clear();
        }
        //操作四：关闭DatagramChannel数据报通道
        dChannel.close();
    }

    public static void main(String[] args) throws IOException {
        new UDPClient().send();
    }
}
```

## UDPServer

- 首先调用bind方法绑定datagramChannel的监听端口
- 当数据来到后，调用receive方法， 从datagramChannel数据包通道接收数据，再写入byteBuffer缓冲区中

```java
public class UDPServer {

    public void receive() throws IOException {
        //操作一：获取DatagramChannel数据报通道
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(false);
        datagramChannel.bind(new InetSocketAddress(
                NioDemoConfig.SOCKET_SERVER_IP
                , NioDemoConfig.SOCKET_SERVER_PORT));
        Print.tcfo("UDP 服务器启动成功！");
        Selector selector = Selector.open();
        datagramChannel.register(selector, SelectionKey.OP_READ);
        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            ByteBuffer buffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isReadable()) {
                    //操作二：读取DatagramChannel数据报通道数据
                    SocketAddress client = datagramChannel.receive(buffer);
                    buffer.flip();
                    Print.tcfo(new String(buffer.array(), 0, buffer.limit()));
                    buffer.clear();
                }
            }
            iterator.remove();
        }

        selector.close();
        datagramChannel.close();
    }

    public static void main(String[] args) throws IOException {
        new UDPServer().receive();
    }
}
```