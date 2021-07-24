package cn.eccto.study.java.io.nio;

import java.nio.ByteBuffer;

/**
 * <p>
 * Example of 只读缓冲区
 * </p>
 *
 * @author Jonathan 2020/05/11 20:54
 */
public class ReadOnlyBuffer {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
// 缓冲区中的数据 0-9
        for (int i = 0; i < buffer.capacity(); ++i) {
            buffer.put((byte) i);
        }
// 创建只读缓冲区
        ByteBuffer readonly = buffer.asReadOnlyBuffer();
// 改变原缓冲区的内容
        for (int i = 0; i < buffer.capacity(); ++i) {
            byte b = buffer.get(i);
            b *= 10;
            buffer.put(i, b);
        }
        readonly.position(0);
        readonly.limit(buffer.capacity());
// 只读缓冲区的内容也随之改变
        while (readonly.remaining() > 0) {
            System.out.println(readonly.get());
        }
    }
}
