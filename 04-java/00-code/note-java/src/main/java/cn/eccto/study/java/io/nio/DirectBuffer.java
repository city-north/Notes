package cn.eccto.study.java.io.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * <p>
 * Example of DirectBuffer
 * </p>
 *
 * @author Jonathan 2020/05/11 20:58
 */
public class DirectBuffer {
    static public void main(String args[]) throws Exception {
//首先我们从磁盘上读取刚才我们写出的文件内容
        String infile = "fileTest.txt";
        FileInputStream fin = new FileInputStream(infile);
        FileChannel fcin = fin.getChannel();
//把刚刚读取的内容写入到一个新的文件中
        String outfile = String.format("fileTest2.txt");
        FileOutputStream fout = new FileOutputStream(outfile);
        FileChannel fcout = fout.getChannel();
// 使用 allocateDirect，而不是 allocate
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        while (true) {
            buffer.clear();
            int r = fcin.read(buffer);
            if (r == -1) {
                break;
            }
            buffer.flip();
            fcout.write(buffer);
        }
    }
}