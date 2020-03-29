package vip.ericchen.study.network.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p>
 * description
 * </p>
 *
 * @author ericchen.vip@foxmail.com 2020/03/29 18:14
 */
public class Server {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);
        Socket accept = serverSocket.accept();//开启一个 server

        InputStream inputStream = accept.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        User user = (User) objectInputStream.readObject();
        System.out.println(user);
    }
}
