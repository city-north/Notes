package cn.eccto.study.java.io.nio;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * <p>
 * .使用 NIO 写入数据
 * </p>
 *
 * @author Jonathan 2020/05/11 21:25
 */
public class FileInputDemo {
    static public void main(String args[]) throws Exception {
        FileInputStream fin = new FileInputStream("E://test.txt");
// 获取通道
        FileChannel fc = fin.getChannel();
// 创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
// 读取数据到缓冲区
        fc.read(buffer);
        buffer.flip();
        while (buffer.remaining() > 0) {
            byte b = buffer.get();
            System.out.print(((char) b));
        }
        fin.close();
    }
}
