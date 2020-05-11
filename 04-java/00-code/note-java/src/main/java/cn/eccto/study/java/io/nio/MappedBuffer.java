package cn.eccto.study.java.io.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/05/11 21:02
 */
public class MappedBuffer {
    static private final int start = 0;
    static private final int size = 1024;

    static public void main(String args[]) throws Exception {
        RandomAccessFile raf = new RandomAccessFile("fileTest2.txt", "rw");
        FileChannel fc = raf.getChannel();
//把缓冲区跟文件系统进行一个映射关联 //只要操作缓冲区里面的内容，文件内容也会跟着改变
        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, start, size);
        mbb.put(0, (byte) 97);
        mbb.put(1023, (byte) 122);
        raf.close();
    }
}
