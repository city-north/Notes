# 032-选择器-Selector-使用流程.md

[TOC]

## 简介

使用算择期， 主要有一下三步

1. 获取选择器实例
2. 将听到注册到选择器中
3. 轮询感兴趣的IO就绪事件（选择键集合）

## 第一步：获取选择器实例

选择器实例是通过调用静态方法open来获取的

```java
// 1、获取Selector选择器
Selector selector = Selector.open();
```

Selector 选择器类方法open 内部， 是向选择器SPI （SelectorProvider)发出请求， 通过默认的 SelectorProvider（选择器提供者）对象，获取一个新的选择器实例

JDK通过SPI的方式， 提供选择器的默认实现， 其他服务提供商可以SPI方式提供定制化实现

## 第二步：将通道注册到选择器实例

```java
// 2、获取通道
ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
// 3.设置为非阻塞
serverSocketChannel.configureBlocking(false);
// 4、绑定连接
serverSocketChannel.bind(new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_PORT));
Logger.info("服务器启动成功");
// 5、将通道注册到选择器上,并注册的IO事件为：“接收新连接”
serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
```

通过register方法，将serverSocketChannel 通道注册到一个选择器上， 当然， 在注册之前要准备好通道

#### 注册到选择器的通道，必须处于非阻塞模式下

否则将抛出IllegalBlockingModeExcepton 异常

- FileChannel 文件通道不能与选择器一起使用， 因为 FileChannel 文件通道只有阻塞模式， 不能切换到非阻塞模式

- 而Socket套接字相关的所有通道就全都可以

#### 一个通道不一定支持所有的四种IO事件

例如服务监听通道，ServerSocketChannel ， 仅仅支持 Accept 接收到新链接的IO事件

SocketChannel 传输通道， 则不支持Accept(接受到新链接的)IO事件

可以在注册之前，通过 validOps()方法获取所有支持的IO事件集合

## 第三步：选出感兴趣的IO就绪事件（选择键集合）

通过Selector选择器的selet()方法， 选出已经注册的， 已经就绪的IO事件， 保存在 SelectionKey选择键集合中

```java
// 6、轮询感兴趣的I/O就绪事件（选择键集合）
while (selector.select() > 0)
{
    // 7、获取选择键集合
    Iterator<SelectionKey> it = selector.selectedKeys().iterator();
    while (it.hasNext())
    {
        // 8、获取单个的选择键，并处理
        SelectionKey key = it.next();

        // 9、判断key是具体的什么事件，是否为新连接事件
        if (key.isAcceptable())
        {
            // 10、若接受的事件是“新连接”事件,就获取客户端新连接
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = server.accept();
            if (socketChannel == null) continue;
            // 11、客户端新连接，切换为非阻塞模式
            socketChannel.configureBlocking(false);
            // 12、将客户端新连接通道注册到selector选择器上
            SelectionKey selectionKey =
                    socketChannel.register(selector, SelectionKey.OP_READ);
            // 余下为业务处理
            Client client = new Client();
            client.remoteAddress
                    = (InetSocketAddress) socketChannel.getRemoteAddress();
            clientMap.put(socketChannel, client);
            Logger.debug(socketChannel.getRemoteAddress() + "连接成功...");

        } else if (key.isReadable())
        {
            processData(key);
        }
        // NIO的特点只会累加，已选择的键的集合不会删除
        // 如果不删除，下一次又会被select函数选中
        it.remove();
    }
```

处理完成后，需要将选择键从这个SelectionKey 集合中移除， 放置下一次循环的时候， 被重复处理

SelectionKey集合不能添加元素， 如果试图向SleectionKey 选择键集合中添加元素， 则会抛出 java.lang.UnsupportedOperationException 异常

## select()方法重载

1. select() 阻塞调用， 一直到至少有一个通道发生了IO事件
2. slect(long timeout) , 最长阻塞事件 timeout
3. slectNow(): 非阻塞， 不管有没有IO事件都返回