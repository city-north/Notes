package cn.eccto.study.java.io.nio;

import java.nio.IntBuffer;

/**
 * <p>
 * Buffer 的例子 Example
 * </p>
 *
 * @author EricChen 2020/05/11 12:22
 */
public class IntBufferDemo {

    public static void main(String[] args) {
        //分配新的 int 缓冲区, 参数为缓冲区容量
        //新缓冲区的当前位置将为 0,其接线(限制位置)将为其容量,它将具有一个底层实现数组,器数组偏移量为 0
        IntBuffer buffer = IntBuffer.allocate(8);
        for (int i = 0; i < buffer.capacity(); i++) {
            int j = 2 * (i + 1);
            //将给定整数写入次缓冲区的当前位置,当前位置递增
            buffer.put(j);
        }
        //重设此缓冲区,将限制设置为当前位置,然后将当前位置设置为 0
        buffer.flip();
        while (buffer.hasRemaining()) {
            int j = buffer.get();
            System.out.println(j + " ");
        }

    }

}
