package vip.ericchen.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * <p>
 * description
 * </p>
 *
 * @author ericchen.vip@foxmail.com 2020/04/13 23:14
 */
public class NIOClient {
    private int size = 1024;
    private SocketChannel socketChannel;
    private ByteBuffer byteBuffer;

    public static void main(String[] args) throws IOException {
        new NIOClient().connectServer();
    }

    private void connectServer() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(9999));
        socketChannel.configureBlocking(false);
        byteBuffer = ByteBuffer.allocate(size);
        receive();

    }

    private void receive() throws IOException {
        while (true){
            byteBuffer.clear();
            int count;
            //如果没有数据可以读,那read 方法会一直阻塞,,知道读取到新的值
            while ((count = socketChannel.read(byteBuffer)) > 0){
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()){
                    System.out.println((char) byteBuffer.get());
                }
                send2Server("say hi ".getBytes());
                byteBuffer.clear();
            }
        }

    }

    private void send2Server(byte[] bytes) throws IOException {
        byteBuffer.clear();
        byteBuffer.put(bytes);
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
    }


}
