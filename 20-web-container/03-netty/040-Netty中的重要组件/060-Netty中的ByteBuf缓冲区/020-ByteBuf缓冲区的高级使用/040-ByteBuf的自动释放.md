# 040-ByteBuf的自动释放

[TOC]

## Netty创建入站的ByteBuf

- Netty的Reactor反应器线程会在底层的JavaNIO通道读取数据时, 也就是 `AbstractNIoByteChannel.NioByteUnsafe.read()`处. 
- 调用ByteBufAllocator方法, 创建ByteBuf实例, 从操作系统缓冲区把数据读取到Bytebuf实例中,
- 然后调用 pipleline.fireChannelRead(byteBuf)方法将读取到的数据包送入到入站处理流水线中

## 2种释放方式

- 方式1 : TailHandler自动释放
- 方式2: SimpleChannelInboundHandler自动释放

## 方式1 : TailHandler自动释放

Netty会默认在ChannelPipline 添加一个TailHandler末尾处理器, 实现了默认的处理方法

默认情况下, 每个InboundHandler入站处理器, 把最初的ByteBuf数据包一路往下传, 那么TailHandler末尾处理器会自动释放掉入站的ByteBuf实例

- 实现了ChannelInboundHandlerAdapter可以
  - 手动调用byteBuf.realease()
  - 调用父类的入站方法一路往后传 super.channelRead(ctx,msg);

## 方式2: SimpleChannelInboundHandler自动释放

```java
public abstract class SimpleChannelInboundHandler<I> extends ChannelInboundHandlerAdapter {

  
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        boolean release = true;
        try {
            if (acceptInboundMessage(msg)) {
                @SuppressWarnings("unchecked")
                I imsg = (I) msg;
                channelRead0(ctx, imsg);
            } else {
                release = false;
                ctx.fireChannelRead(msg);
            }
        } finally {
            if (autoRelease && release) {
              //释放
                ReferenceCountUtil.release(msg);
            }
        }
    }

}

```



