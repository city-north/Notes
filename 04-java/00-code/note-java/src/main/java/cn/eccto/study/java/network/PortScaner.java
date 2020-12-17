package cn.eccto.study.java.network;

import java.io.IOException;
import java.net.Socket;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/11/30 22:03
 */
public class PortScaner {
    public String app;

    public static void main(String[] args) {
        String host = "localhost";
        for (int port = 1; port < 1024; port++) {
            try (Socket socket = new Socket(host, port)) {
                System.out.println("There is a server on port " + port);
            } catch (IOException e) {
                System.out.println("can not connect to port " + port);
            }
        }
    }
}
