package vip.ericchen.study.network.serial;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * <p>
 * description
 * </p>
 *
 * @author ericchen.vip@foxmail.com 2020/03/29 18:19
 */
public class Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 8080);
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        User user = new User();
        user.setAge(11);
        user.setName("EricChen");
        objectOutputStream.writeObject(user);
        socket.close();

    }
}
