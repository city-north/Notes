package cn.eccto.study.java.io.nio;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * <p>
 * BufferDemo 验证 position、limit 和 capacity 这几个值的变化过程
 * </p>
 *
 * @author EricChen 2020/05/11 12:30
 */
public class BufferDemo {
    public static void main(String[] args) throws Exception {
        //这用的是文件 IO 处理
        FileInputStream inputStream = new FileInputStream("fileTest.txt");
        //创建文件的操作 Channel 管道
        FileChannel channel = inputStream.getChannel();
        //分配一个 10 个大小的缓冲区,说白了就是分配一个 10 个大小的 byte 数组
        final ByteBuffer buffer = ByteBuffer.allocate(10);
        printBuffer("初始化", buffer);
        //读取一个
        int read = channel.read(buffer);
        printBuffer("调用 read()", buffer);
        //准备操作之前，先锁定操作范围
        buffer.flip();
        printBuffer("调用 flip()", buffer);
        //判断有没有可读数据
        while (buffer.remaining() > 0) {
            byte b = buffer.get();
            System.out.print(((char) b));
        }
        printBuffer("调用 get()", buffer);
        //可以理解为解锁
        buffer.clear();
        printBuffer("调用 clear()", buffer);
        //最后把管道关闭
        inputStream.close();
    }

    public static void printBuffer(String state, ByteBuffer buffer) {
        System.out.println(state + " : "); //容量，数组大小
        System.out.print("capacity: " + buffer.capacity() + ", "); //当前操作数据所在的位置，也可以叫做游标
        System.out.print("position: " + buffer.position() + ", "); //锁定值，flip，数据操作范围索引只能在 position - limit 之间
        System.out.println("limit: " + buffer.limit());
        System.out.println();
    }
}
