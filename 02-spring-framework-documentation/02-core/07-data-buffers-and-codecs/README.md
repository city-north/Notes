# 数据缓冲区和编解码器(Data Buffers and Codecs)

Java NIO提供了`ByteBuffer`但是很多库有他们自己的 buffer API,尤其是网络操作,复用缓冲或者直接使用缓冲对性能有帮助

例如:

- Netty 的`ByteBuf`层级结构
- Undoertow 的 `XNIO`
- Jetty 使用字节缓冲池以及回调

Spring core 模块提供了一系列抽象,当你需要使用字节缓冲 API 时可以使用:

- [`DataBufferFactory`](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#databuffers-factory) 创建数据缓冲 abstracts the creation of a data buffer.
- [`DataBuffer`](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#databuffers-buffer) 字节缓冲,可以使用缓冲池represents a byte buffer, which may be [pooled](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#databuffers-buffer-pooled).
- [`DataBufferUtils`](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#databuffers-utils) 提供了一些工具方法 offers utility methods for data buffers.
- [Codecs](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#codecs) 编码或者解码流数据到更高级的对象decode or encode streams data buffer streams into higher level objects.

## `DataBufferFactory`

`DataBufferFactory`用来创建数据缓冲,有两种方式:

- 分配一个新的数据缓冲区，可以预先指定容量(如果已知)，这是更有效的，即使DataBuffer的实现可以根据需要增减。
- 封装现有的`byte[]`或`java.nio`。`ByteBuffer`，它用一个`DataBuffer`的实现类包装给定的数据，并且不涉及分配。

注意 WebFlux 应用不直接创建`DataBufferFactory`,你可以通过`ServerHttpResponse`,`ClientHttpRequest`间接的访问他,他依赖潜在的客户端以及服务例如:

- Reactor Netty : `NettyDataBufferFactory`
- 其他:`DefaultDataBufferFactory`

## `DataBuffer`

`DataBuffer`接口提供了与`java.nio.ByteBuffer`相似的操作,但是加了一些优势:

- 读写独立的位置,不需要调用`flip()`去切换读和写
- 可以使用 `java.lang.StringBuilder`拓展能力
- 通过 [`PooledDataBuffer`](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#databuffers-buffer-pooled).可以使用缓冲池和引用计数
- 视图缓冲区 as `java.nio.ByteBuffer`, `InputStream`, or `OutputStream`.
- 确定给定类型的索引或者最后索引



## `PooledDataBuffer`

正如 [ByteBuffer](https://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html) javadoc 中解释的,字节缓冲去可以是直接或者间接的,直接缓冲区可能位于Java堆之外，这消除了本地I/O操作的复制需求。这使得直接缓冲区对于通过套接字接收和发送数据特别有用，但是创建和释放它们也更昂贵，这就产生了池缓冲区的想法。

`PooledDataBuffer`是`DataBuffer`的扩展，它可以帮助进行引用计数，这对于字节缓冲池非常重要。它是如何工作的?当`PooledDataBuffer`被分配时，引用计数为1。调用` retain() `递增计数，而调用` release() `递减计数。只要计数大于`0`，就保证不会释放缓冲区。当计数减少到`0`时，可以释放池中的缓冲区，这实际上可能意味着为缓冲区保留的内存返回到内存池。

值得注意的是,不要直接操作`PooledDataBuffer`,在大部分情况下好有方便的方式是使用`DataBufferUtils`去应用释放和保存一个 `DataBuffer`,如果是一个`PooledDataBuffer`实例

## `DataBufferUtils`

`DataBufferUtils`工具类,提供了工具方法操作 DataBuffer

- Join a stream of data buffers into a single buffer possibly with zero copy, e.g. via composite buffers, if that’s supported by the underlying byte buffer API.如果底层字节缓冲区API支持的话，可以通过复合缓冲区将数据缓冲区流连接到单个缓冲区(可能是零拷贝)。
- Turn `InputStream` or NIO `Channel` into `Flux`, and vice versa a `Publisher` into `OutputStream` or NIO `Channel`.
- Methods to release or retain a `DataBuffer` if the buffer is an instance of `PooledDataBuffer`.
- Skip or take from a stream of bytes until a specific byte count.

## 编解码器Codecs

The `org.springframework.core.codec` package provides the following strategy interfaces:

- `Encoder` to encode `Publisher` into a stream of data buffers.
- `Decoder` to decode `Publisher` into a stream of higher level objects.

The `spring-core` module provides `byte[]`, `ByteBuffer`, `DataBuffer`, `Resource`, and `String` encoder and decoder implementations. The `spring-web` module adds Jackson JSON, Jackson Smile, JAXB2, Protocol Buffers and other encoders and decoders. See [Codecs](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-codecs) in the WebFlux section.

## 使用 `DataBuffer`

When working with data buffers, special care must be taken to ensure buffers are released since they may be [pooled](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#databuffers-buffer-pooled). We’ll use codecs to illustrate how that works but the concepts apply more generally. Let’s see what codecs must do internally to manage data buffers.

A `Decoder` is the last to read input data buffers, before creating higher level objects, and therefore it must release them as follows:

1. If a `Decoder` simply reads each input buffer and is ready to release it immediately, it can do so via `DataBufferUtils.release(dataBuffer)`.
2. If a `Decoder` is using `Flux` or `Mono` operators such as `flatMap`, `reduce`, and others that prefetch and cache data items internally, or is using operators such as `filter`, `skip`, and others that leave out items, then `doOnDiscard(PooledDataBuffer.class, DataBufferUtils::release)` must be added to the composition chain to ensure such buffers are released prior to being discarded, possibly also as a result an error or cancellation signal.
3. If a `Decoder` holds on to one or more data buffers in any other way, it must ensure they are released when fully read, or in case an error or cancellation signals that take place before the cached data buffers have been read and released.

Note that `DataBufferUtils#join` offers a safe and efficient way to aggregate a data buffer stream into a single data buffer. Likewise `skipUntilByteCount` and `takeUntilByteCount` are additional safe methods for decoders to use.

An `Encoder` allocates data buffers that others must read (and release). So an `Encoder` doesn’t have much to do. However an `Encoder` must take care to release a data buffer if a serialization error occurs while populating the buffer with data. For example:

```java
DataBuffer buffer = factory.allocateBuffer();
boolean release = true;
try {
    // serialize and populate buffer..
    release = false;
}
finally {
    if (release) {
        DataBufferUtils.release(buffer);
    }
}
return buffer;
```

The consumer of an `Encoder` is responsible for releasing the data buffers it receives. In a WebFlux application, the output of the `Encoder` is used to write to the HTTP server response, or to the client HTTP request, in which case releasing the data buffers is the responsibility of the code writing to the server response, or to the client request.

Note that when running on Netty, there are debugging options for [troubleshooting buffer leaks](https://github.com/netty/netty/wiki/Reference-counted-objects#troubleshooting-buffer-leaks).