# 022-通道-Channel-SocketChannel-套接字通道

[TOC]

## SocketChannel简介

在NIO中， 设计网络连接的通道有两个

- 一个是SocketChannel 负责连接传输 ， 对应 OIO 中的 Socket
- 一个 ServerSocketChannel 负责连接的监听 , 对应 OIO 中的 ServerSocket

SocketChannel 或者是ServerSocketChannel  都支持 非阻塞模式

- socketChannel.configureBlocking(false) 设置为非阻塞模式
- socketChannel.configureBlocking(true) 设置为阻塞模式 ， 与传统OIO操作一致

## 获取SocketChannel传输通道

在客户端， 

- 先通过 SocketChannel 的静态方法 open() 获得一个套接字传输通道
- 然后将socket套接字设置为非阻塞模式
- 最后， 通过 connect() 实例方法， 对服务器的ip和端口发起连接

```java
// 1、获取通道（channel）
SocketChannel socketChannel = SocketChannel.open(address);
// 2、切换成非阻塞模式
socketChannel.configureBlocking(false);
```

在非阻塞的清空下， 与服务器的连接还可能没有真正的建立，socketChannel.connect方法就返回了， 因此需要不断自旋，检查当前是否是连接到了主机

```
//不断的自旋、等待连接完成，或者做一些其他的事情
while (!socketChannel.finishConnect()) {

}
```

在服务器端，如何获取传输套接字呢？

当新连接事件来到时，在服务端的ServerSocketChannel 能成功的查询出一个新连接事件， 并且通过调用服务端 ServerSocketChannel监听套接字的accept()方法 来获取新连接的套接字通道

```
SocketChannel socketChannel = (SocketChannel) selectedKey.channel();
// 10、若选择键的IO事件是“连接就绪”事件,就获取客户端连接
 socketChannel = serverSocketChannel.accept();
// 11、切换为非阻塞模式
socketChannel.configureBlocking(false);
```

## 读取SocketChannel传输通道

当SocketChannel 通道可读时， 可以从 SocketChannel 读取数据， 具体方法与文件通道一致， 调用 read方法， 将数据读入缓冲区ByteBuffer

```java
ByteBuffer buf = ByteBuffer.allocate(CAPACITY);
int length =inChannel.read(buf);
```

在读取时， 因为是异步，因此必须要检查read的返回值，以便判断当前是否读取到了数据

- read方法的返回值， 是读取的字节数
- 如果返回-1 ， 代表读取到结束符，对方已经输出结束， 准备关闭连接
- 使用Selector选择器非阻塞的读取

## 写入SocketChannel

```
buffer.flip();
socketChannel.write(buffer);
```

## 关闭SocketChannel

```java
socketChannel.shutdownOutput();
IOUtil.closeQuietly(socketChannel);
```

## 使用SocketChannel发送文件的实践案例

#### NioSendClient

```java

/**
 * 文件传输Client端
 */
public class NioSendClient
{
    private Charset charset = Charset.forName("UTF-8");

    /**
     * 向服务端传输文件
     */
    public void sendFile()
    {
        try
        {
            String sourcePath = NioDemoConfig.SOCKET_SEND_FILE;
            String srcPath = IOUtil.getResourcePath(sourcePath);
            Logger.debug("srcPath=" + srcPath);

            String destFile = NioDemoConfig.SOCKET_RECEIVE_FILE;
            Logger.debug("destFile=" + destFile);

            File file = new File(srcPath);
            if (!file.exists())
            {
                Logger.debug("文件不存在");
                return;
            }
            FileChannel fileChannel = new FileInputStream(file).getChannel();

            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.socket().connect(
                    new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP
                            , NioDemoConfig.SOCKET_SERVER_PORT));
            socketChannel.configureBlocking(false);
            Logger.debug("Client 成功连接服务端");

            while (!socketChannel.finishConnect())
            {
                //不断的自旋、等待，或者做一些其他的事情
            }

            //发送文件名称
            ByteBuffer fileNameByteBuffer = charset.encode(destFile);

            ByteBuffer buffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);
            //发送文件名称长度
            int fileNameLen =     fileNameByteBuffer.capacity();
            buffer.putInt(fileNameLen);
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
            Logger.info("Client 文件名称长度发送完成:",fileNameLen);

            //发送文件名称
            socketChannel.write(fileNameByteBuffer);
            Logger.info("Client 文件名称发送完成:",destFile);
            //发送文件长度
            buffer.putLong(file.length());
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
            Logger.info("Client 文件长度发送完成:",file.length());


            //发送文件内容
            Logger.debug("开始传输文件");
            int length = 0;
            long progress = 0;
            while ((length = fileChannel.read(buffer)) > 0)
            {
                buffer.flip();
                socketChannel.write(buffer);
                buffer.clear();
                progress += length;
                Logger.debug("| " + (100 * progress / file.length()) + "% |");
            }

            if (length == -1)
            {
                IOUtil.closeQuietly(fileChannel);
                socketChannel.shutdownOutput();
                IOUtil.closeQuietly(socketChannel);
            }
            Logger.debug("======== 文件传输成功 ========");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 入口
     */
    public static void main(String[] args)
    {

        NioSendClient client = new NioSendClient(); // 启动客户端连接
        client.sendFile(); // 传输文件
    }

}
```



