package cn.eccto.study.java.network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * ServerSocketExample
 * </p>
 *
 * @author Jonathan 2020/03/12 19:29
 */
public class ServerSocketExample {
    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(6666); // 监听指定端口
        System.out.println("server is running...");
        for (; ; ) {
            Socket sock = ss.accept();
            System.out.println("connected from " + sock.getRemoteSocketAddress());
            Thread t = new Handler(sock);
            t.start();
        }
    }

    static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket sock) {
            this.socket = sock;
        }

        @Override
        public void run() {
            try (InputStream input = socket.getInputStream()) {
                try (OutputStream output = socket.getOutputStream()) {
                    handle(input, output);
                }
            } catch (Exception e) {
                try {
                    this.socket.close();
                } catch (IOException ioe) {
                }
                System.out.println("client disconnected.");
            }
        }

        private void handle(InputStream input, OutputStream output) throws Exception {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            writer.write("hello\n");
            writer.flush();
            for (; ; ) {
                String s = reader.readLine();
                if (s.equals("bye")) {
                    writer.write("bye\n");
                    writer.flush();
                    break;
                }
                writer.write("ok: " + s + "\n");
                writer.flush();
            }
        }
    }
}
