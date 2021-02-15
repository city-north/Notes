# 040-缓冲区-Buffer

[TOc]

## Buffer缓冲区是什么

NIO的通道本质上是一个内存块， 既可以是写入数据， 也可以从中读取数据， NIO 的Buffer 类， 是一个抽象类， 位于 java.nio包中， 其内部是一个内存块（数组）

NIO的Buffer和普通的内存块(Java数组) 不同的是， NIO buffer 对象， 提供了一组更加有效的方法， 用来进行写入和读取交替访问

Buffer类是一个线程不安全的类

## Buffer的作用

应用程序与通道（Channel）主要的交互交互操作，就是进行数据的read读取和write写入。

为了能够确保数据的读取和写入， NIO为大家准备了第三个重要的组件-NIO Buffer ， 

- 通道的读取， 就是将数据从通到读取到缓冲区中
- 通道的写入， 就是从缓冲区中写入到通道中

## Buffer类

java.nio.Buffer 类是一个抽象类， 对应Java的主要数据类型， 在NIO 中有8种缓冲区类， 分别是

| Buffer子类       | 存储类型                         |
| ---------------- | -------------------------------- |
| ByteBuffer       |                                  |
| CharBuffer       |                                  |
| DoubleBuffer     |                                  |
| FloatBuffer      |                                  |
| IntBuffer        |                                  |
| LongBuffer       |                                  |
| ShortBuffer      |                                  |
| MappedByteBuffer | 专门用于内存映射的一种ByteBuffer |

## 缓冲区分片

在NIO中，可以根据现有的缓冲区对象创建一个子缓冲区，即在现有缓冲区上切出来一片作为新的缓冲区， 但是现有的缓冲区与创建的缓冲区分片底层数据层面上是共享的，也即是说

字缓冲区相当于现有缓冲区的一个视图窗口， 调用slice()方法创建一个子缓冲区

```java
/**
 * 缓冲区分片
 */
public class BufferSlice {  
    static public void main( String args[] ) throws Exception {  
        ByteBuffer buffer = ByteBuffer.allocate( 10 );  
          
        // 缓冲区中的数据0-9  
        for (int i=0; i<buffer.capacity(); ++i) {  
            buffer.put( (byte)i );  
        }  
          
        // 创建子缓冲区  
        buffer.position( 3 );  
        buffer.limit( 7 );  
        ByteBuffer slice = buffer.slice();  
          
        // 改变子缓冲区的内容  
        for (int i=0; i<slice.capacity(); ++i) {  
            byte b = slice.get( i );  
            b *= 10;  
            slice.put( i, b );  
        }  
          
        buffer.position( 0 );  
        buffer.limit( buffer.capacity() );  
          
        while (buffer.remaining()>0) {  
            System.out.println( buffer.get() );  
        }  
    }  
}
```

在上面的实例中，分配一个容器大小为10的缓冲区并在其中放入0-9，而且该缓冲区基础之上又创建了一个子缓冲区，并改变了缓冲区的内容， 子缓冲区改变的值在原缓冲区上也发生了变化，说明是共享的