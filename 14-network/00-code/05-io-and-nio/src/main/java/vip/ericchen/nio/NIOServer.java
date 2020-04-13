package vip.ericchen.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * <p>
 * description
 * </p>
 *
 * @author ericchen.vip@foxmail.com 2020/04/13 22:49
 */
public class NIOServer {

    private int size = 1024;
    private ServerSocketChannel serverSocketChannel;
    private ByteBuffer byteBuffer;
    private Selector selector;
    private int remoteClientNum = 0;//记录远程客户端的数量


    public NIOServer(int port) {
        try {
            initChannel(port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void initChannel(int port) throws IOException {
        //打开 Channel
        serverSocketChannel = ServerSocketChannel.open();
        //设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);
        //绑定端口
        serverSocketChannel.bind(new InetSocketAddress(port));
        System.out.println("listen on port: " + port);
        //创建选择器
        selector = Selector.open();
        //向选择器注册通道
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //分配缓冲区的大小
        byteBuffer = ByteBuffer.allocate(size);
    }

    private void listener() throws Exception {
        while (true) {
            //返回 int 值就表示有多少个 Channel 出于就绪状态
            int select = selector.select();
            if (select == 0) {
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                //如果 selcetkey 出于连续就绪状态,则开始接受客户端的链接
                if (key.isAcceptable()) {
                    //获取 Channel
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    //接受链接
                    SocketChannel accept = channel.accept();
                    //channel 注册
                    registerChannel(selector, accept, SelectionKey.OP_READ);
                    //远程客户端的连接数
                    remoteClientNum++;
                    System.out.println("online remote client num = " + remoteClientNum);
                    write(accept, "helloClient".getBytes());
                }
                if (key.isReadable()) {
                    //如果已经出于就绪状态
                    read(key);
                }
                iterator.remove();
            }

        }
    }

    private void registerChannel(Selector selector, SocketChannel channel, int opRead) throws IOException {
        if (channel == null){
            return;
        }
        channel.configureBlocking(false);
        channel.register(selector,opRead);

    }

    private void write(SocketChannel channel, byte[] bytes) throws IOException {
        byteBuffer.clear();
        byteBuffer.put(bytes);
        //bytebuffer 从写模式变为读模式
        byteBuffer.flip();
        //将缓冲区的数据写入通道中
        channel.write(byteBuffer);
    }

    private void read(SelectionKey key) throws Exception {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        int count;
        byteBuffer.clear();

        //从通道中读到缓冲器
        while ((count = socketChannel.read(byteBuffer)) > 0) {
            //byteBuffer 写模式变为读模式
            Buffer flip = byteBuffer.flip();
            while (flip.hasRemaining()) {
                System.out.println((char) byteBuffer.get());
            }
            byteBuffer.clear();
        }
        if (count < 0) {
            socketChannel.close();
        }


    }

    public static void main(String[] args) throws Exception {
        NIOServer nioServer = new NIOServer(9999);
        nioServer.listener();
    }
}
