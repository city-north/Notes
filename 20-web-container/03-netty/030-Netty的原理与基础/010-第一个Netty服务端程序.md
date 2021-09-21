# 010-第一个Netty服务端程序

[TOC]

```java
public class NettyDiscardServer {
    private final int serverPort;
    ServerBootstrap b = new ServerBootstrap();

    public NettyDiscardServer(int port) {
        this.serverPort = port;
    }

    public void runServer() {
        //创建reactor 线程组
        EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup();

        try {
            //1 设置reactor 线程组
            b.group(bossLoopGroup, workerLoopGroup);
            //2 设置nio类型的channel
            b.channel(NioServerSocketChannel.class);
            //3 设置监听端口
            b.localAddress(serverPort);
            //4 设置通道的参数
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            //5 装配子通道流水线
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                //有连接到达时会创建一个channel
                protected void initChannel(SocketChannel ch) throws Exception {
                    // pipeline管理子通道channel中的Handler
                    // 向子channel流水线添加一个handler处理器
                    ch.pipeline().addLast(new NettyDiscardHandler());
                }
            });
            // 6 开始绑定server
            // 通过调用sync同步方法阻塞直到绑定成功
            ChannelFuture channelFuture = b.bind().sync();
            Logger.info(" 服务器启动成功，监听端口: " +
                    channelFuture.channel().localAddress());

            // 7 等待通道关闭的异步任务结束
            // 服务监听通道会一直等待通道关闭的异步任务结束
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 8 优雅关闭EventLoopGroup，
            // 释放掉所有资源包括创建的线程
            workerLoopGroup.shutdownGracefully();
            bossLoopGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        int port = NettyDemoConfig.SOCKET_SERVER_PORT;
        new NettyDiscardServer(port).runServer();
    }
}
```

## Netty中的反应器模式

#### Netty中的反应器Reactor

反应器的作用是进行一个IO事件的select查询和dispatch分发

Netty中的反应器有很多种， 应用场景不同，用到的反应器也不相同， 一般来说是 NIOEventLoopGroup

上面的例子种用了两个 NioEventLoopGroup

- 第一个是包工头， 负责服务器通道新连接的IO事件的监听
- 第二个是工人， 主要负责传输通道的IO事件

#### Netty中的Handler

Handler处理器的作用时对应到IO事件， 实现IO事件的业务处理， Handler需要单独开发

#### Netty中的ServerBootstrap 

启动服务类ServerBootstrap的职责时给个组装和集成器， 将不同的Netty组件组装到一起

## 业务处理器-NettyDiscardHandler

在反应器（Reactor）模式中， 所有的业务处理都在Handler处理器中完成

```java
public class NettyDiscardHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf in = (ByteBuf) msg;
        try {
            Logger.info("收到消息,丢弃如下:");
            while (in.isReadable()) {
                System.out.print((char) in.readByte());
            }
            System.out.println();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
```

Netty的handler处理器需要多种IO事件（如可读可写），对应不同的IO事件

- 对于处理入站的IO事件的方法， 对应点额接口是 ChannalInboundHandler 入站处理器接口
- 对于处理出站的IO事件的方法， 对应的接口是 ChannelOutboundHandler，默认的实现是ChannelOutboundHandlerAdapter