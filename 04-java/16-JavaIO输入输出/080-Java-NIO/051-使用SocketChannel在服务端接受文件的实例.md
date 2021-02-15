# 051-使用SocketChannel在服务端接受文件的实例.md

[TOC]

## 简介

客户端每次传输文件， 都会分为多次传输：

- 首先传入文件名称
- 然后传入文件大小
- 然后传入文件内容

对于每个客户端socketChannel , 创建一个Client 客户端对象， 用于保存客户端状态， 分别保存文件名， 文件大小和写入的目标文件通道outChannel

socketChannel 和 Client对象是一对一关系

- 建立链接的时候， 以socketChannel 作为键 key, Client 对象作为值（Value) ， 将Client保存在map中
- 当socketChannel 传输通道有数据可读时， 通过选择键 key.channel （）方法， 取出IO事件所在的socketChannel通道，然后通过socketChannel通道，取出map中对应的CLient对象

接受到数据时， 如果文件名为空，先处理文件名称， 并把文件名保存到Client对象， 同事创建服务器上的目标文件

接下来再读到数据， 说明接收到了文件大小， 把文件大小保存到Client对象， 接下来再接到数据，说明是文件内容了， 则写入Clientd对象的outChannel文件通道中， 知道读取完毕

```java
/**
 * 文件传输Server端
 */
public class NioReceiveServer
{

    //接受文件路径
    private static final String RECEIVE_PATH = NioDemoConfig.SOCKET_RECEIVE_PATH;

    private Charset charset = Charset.forName("UTF-8");

    /**
     * 服务器端保存的客户端对象，对应一个客户端文件
     */
    static class Client
    {
        //文件名称
        String fileName;
        //长度
        long fileLength;

        //开始传输的时间
        long startTime;

        //客户端的地址
        InetSocketAddress remoteAddress;

        //输出的文件通道
        FileChannel outChannel;

        //接收长度
        long receiveLength;

        public boolean isFinished()
        {
            return receiveLength >= fileLength;
        }
    }

    private ByteBuffer buffer
            = ByteBuffer.allocate(NioDemoConfig.SERVER_BUFFER_SIZE);

    //使用Map保存每个客户端传输，当OP_READ通道可读时，根据channel找到对应的对象
    Map<SelectableChannel, Client> clientMap = new HashMap<SelectableChannel, Client>();


    public void startServer() throws IOException
    {
        // 1、获取Selector选择器
        Selector selector = Selector.open();

        // 2、获取通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverChannel.socket();

        // 3.设置为非阻塞
        serverChannel.configureBlocking(false);
        // 4、绑定连接
        InetSocketAddress address
                = new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_PORT);
        serverSocket.bind(address);
        // 5、将通道注册到选择器上,并注册的IO事件为：“接收新连接”
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        Print.tcfo("serverChannel is linstening...");
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
        }
    }

    /**
     * 处理客户端传输过来的数据
     */
    private void processData(SelectionKey key) throws IOException
    {
        Client client = clientMap.get(key.channel());

        SocketChannel socketChannel = (SocketChannel) key.channel();
        int num = 0;
        try
        {
            buffer.clear();
            while ((num = socketChannel.read(buffer)) > 0)
            {
                buffer.flip();
                //客户端发送过来的，首先处理文件名
                if (null == client.fileName)
                {
                    if (buffer.capacity() < 4)
                    {
                        continue;
                    }
                    int fileNameLen = buffer.getInt();
                    byte[] fileNameBytes = new byte[fileNameLen];
                    buffer.get(fileNameBytes);

                    // 文件名
                    String fileName = new String(fileNameBytes, charset);

                    File directory = new File(RECEIVE_PATH);
                    if (!directory.exists())
                    {
                        directory.mkdir();
                    }
                    Logger.info("NIO  传输目标dir：", directory);

                    client.fileName = fileName;
                    String fullName = directory.getAbsolutePath() + File.separatorChar + fileName;
                    Logger.info("NIO  传输目标文件：", fullName);

                    File file = new File(fullName.trim());

                    if (!file.exists())
                    {
                        file.createNewFile();
                    }
                    FileChannel fileChannel = new FileOutputStream(file).getChannel();
                    client.outChannel = fileChannel;

                    if (buffer.capacity() < 8)
                    {
                        continue;
                    }
                    // 文件长度
                    long fileLength = buffer.getLong();
                    client.fileLength = fileLength;
                    client.startTime = System.currentTimeMillis();
                    Logger.debug("NIO  传输开始：");

                    client.receiveLength += buffer.capacity();
                    if (buffer.capacity() > 0)
                    {
                        // 写入文件
                        client.outChannel.write(buffer);
                    }
                    if (client.isFinished())
                    {
                        finished(key, client);
                    }
                }
                //客户端发送过来的，最后是文件内容
                else
                {
                    client.receiveLength += buffer.capacity();
                    // 写入文件
                    client.outChannel.write(buffer);
                    if (client.isFinished())
                    {
                        finished(key, client);
                    }
                }
                buffer.clear();
            }
            key.cancel();
        } catch (IOException e)
        {
            key.cancel();
            e.printStackTrace();
            return;
        }
        // 调用close为-1 到达末尾
        if (num == -1)
        {
            finished(key, client);
        }
    }

    private void finished(SelectionKey key, Client client)
    {
        IOUtil.closeQuietly(client.outChannel);
        Logger.info("上传完毕");
        key.cancel();
        Logger.debug("文件接收成功,File Name：" + client.fileName);
        Logger.debug(" Size：" + IOUtil.getFormatFileSize(client.fileLength));
        long endTime = System.currentTimeMillis();
        Logger.debug("NIO IO 传输毫秒数：" + (endTime - client.startTime));
    }


    /**
     * 入口
     *
     * @param args
     */
    public static void main(String[] args) throws Exception
    {
        NioReceiveServer server = new NioReceiveServer();
        server.startServer();
    }
}
```

## 文件传输Client端

```java
public class NioSendClient
{


    /**
     * 构造函数
     * 与服务器建立连接
     */
    public NioSendClient()
    {

    }

    private Charset charset = Charset.forName("UTF-8");

    /**
     * 向服务端传输文件
     *
     * @throws Exception
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